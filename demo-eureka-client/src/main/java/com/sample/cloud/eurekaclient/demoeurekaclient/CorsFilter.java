package com.sample.cloud.eurekaclient.demoeurekaclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Profile("cors")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@Slf4j
public class CorsFilter implements Filter {

   private final Map<String, List<ServiceInstance>> catalog = new ConcurrentHashMap<>();

   private final DiscoveryClient discoveryClient;

   /**
    * 서비스 토폴로지 정보를 얻기 위해  DiscoveryClient를 사용한다.
    * @param discoveryClient
    */
   @Autowired
   public CorsFilter(DiscoveryClient discoveryClient) {
      this.discoveryClient = discoveryClient;
      this.refreshCatalog();
   }

   @Override
   public void init(FilterConfig filterConfig) throws ServletException {

   }

   /**
    * 서블릿의 필터의 doFilter를 오버라이드 해서 필요한 필터링 로직을 넣는다. isClientAllowed() 결과가 true면
    * ACCESS_CONTROL_ALLOW_ORIGIN을 응답 헤더에 추가한다.
    * @param servletRequest
    * @param servletResponse
    * @param filterChain
    * @throws IOException
    * @throws ServletException
    */
   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      HttpServletResponse response = HttpServletResponse.class.cast(servletResponse);
      HttpServletRequest request = HttpServletRequest.class.cast(servletRequest);
      String originHeaderValue = originFor(request);
      boolean clientAllowed = isClientAllowed(originHeaderValue);

      if (clientAllowed) {
         response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, originHeaderValue);
      }
      filterChain.doFilter(servletRequest, servletResponse);
   }

   /**
    * 요청 클라이언트의 오리진이 다운스트림 서비스별 화이트리스트(코드에는 catalog로 되어 있음)에 포함되어 있으면 true를 반환한다.
    * @param origin
    * @return
    */
   private boolean isClientAllowed(String origin) {
      if (StringUtils.hasText(origin)) {
         URI originUri = URI.create(origin);
         int port = originUri.getPort();
         String match = originUri.getHost() + ":" + (port <= 0 ? 80 : port);
         this.catalog.forEach((k, v) -> {
            String collect = v.stream()
               .map(
                  si -> si.getHost() + ":" + si.getPort() + "(" + si.getServiceId() + ")")
               .collect(Collectors.joining());
         });
         boolean svcMatch = this.catalog
            .keySet()
            .stream()
            .anyMatch(
               serviceId -> this.catalog.get(serviceId).stream()
               .map(si -> si.getHost() + ":" + si.getPort())
               .anyMatch(hp -> hp.equalsIgnoreCase(match))
            );
         return svcMatch;
      }
      return false;
   }

   /**
    * DiscoveryClient가 생존 신호 이벤트를 받을 때마다 화이트리스트가 포함된 다운스트림 서비스 정보를 저장하고 있는 로컬 캐시를 폐기하고
    * 새 정보로 갱신한다.
    * @param event
    */
   @EventListener(HeartbeatEvent.class)
   public void onHeartbeatEvent(HeartbeatEvent event) {
      this.refreshCatalog();
   }

   private void refreshCatalog() {
      discoveryClient.getServices()
         .forEach(
            svc -> this.catalog.put(svc, this.discoveryClient.getInstances(svc))
         );
   }

   @Override
   public void destroy() {

   }

   private String originFor(HttpServletRequest request) {
      return StringUtils.hasText(request.getHeader(HttpHeaders.ORIGIN))
         ? request.getHeader(HttpHeaders.ORIGIN)
         : request.getHeader(HttpHeaders.REFERER);
   }
}
