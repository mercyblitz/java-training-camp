
Spring Cloud Gateway 选型的优势：

- SpringCloud Gateway 有很多开箱即用的功能，且扩展点多
- 适合 Java 技术栈
- Spring/SpringCloud 社区生态好
- 适合跟 SpringBoot/ SpringCloud 微服务生态集成

Spring Cloud Gateway 缺陷/不足：

- <br />


Resilience4j -> Micrometer -> Micrometer -> Prometheus


spring.cloud.gateway.default-filters

spring.cloud.gateway.default-filters[0].name=xxx<br />spring.cloud.gateway.default-filters[0].args=...

spring.cloud.gateway.default-filters[1].name=xxx<br />spring.cloud.gateway.default-filters[2].args=...

<a name="PwqSw"></a>
# lb://sca-provider

a -> http://127.0.0.1:8080<br />b -> http://127.0.0.2:8080

```java
   @EventListener(RefreshRoutesResultEvent.class)
    public void onEvent(RefreshRoutesResultEvent event) {
        if (event.isSuccess()) {
            List<GatewayFilter> globalFilters = new LinkedList<>();
            RouteLocator routeLocator = (RouteLocator) event.getSource();
            routeLocator.getRoutes().toStream().forEach(route -> {
                String routeId = route.getId();
                List<GatewayFilter> filters = route.getFilters();
                List<GatewayFilter> allFilters = new LinkedList();
                allFilters.addAll(globalFilters);
                allFilters.addAll(filters);
                AnnotationAwareOrderComparator.sort(allFilters);
                // Atomically
                routedFiltersCache.put(routeId, allFilters);
            });
        }
    }
```

<a name="Ig0dq"></a>
# 参考文章
<a name="wTuVI"></a>
## [《SpringCloud Gateway 在微服务架构下的最佳实践》](https://developer.aliyun.com/article/1281193)

<a name="MLdtL"></a>
## [《得物自研API网关实践之路》](https://mp.weixin.qq.com/s/IXInfuWkKe5D1fmtpQQ1SA)

