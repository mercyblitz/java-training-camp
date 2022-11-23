# 第十五节：基于 Pull 方式指标监控平台设计

## Prometheus Endpoint
讨论 Spring Boot Actautor Prometheus  Endpoint 与 Micrometer 适配细节
### 应用依赖
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>
```
### 应用配置
```properties
management.endpoints.web.exposure.include = *
```

## Prometheus 平台搭建
Prometheus 使用 Spring Cloud 注册中心发现服务实例，并拉取应用 Metrics 数据

### Eureka Server 服务发现配置
```yaml
  # Discover Eureka services to scrape.
  - job_name: 'eureka'
    metrics_path: "/actuator/prometheus"
    # Scrape Eureka itself to discover new services.
    eureka_sd_configs:
      - server: http://127.0.0.1:12345/eureka
    relabel_configs:
      - source_labels: [__meta_eureka_app_instance_metadata_prometheus_scrape]
        action: keep
        regex: true
      - source_labels: [__meta_eureka_app_instance_metadata_prometheus_path]
        action: replace
        target_label: __metrics_path__
        regex: (.+)
      - source_labels: [__address__, __meta_eureka_app_instance_metadata_prometheus_port]
        action: replace
        regex: ([^:]+)(?::\d+)?;(\d+)
        replacement: $1:$2
        target_label: __address__
```

### Spring Boot 服务配置
```properties
# Eureka Instance 配置
eureka.instance.metadataMap.prometheus.scrape = true
eureka.instance.metadataMap.prometheus.path = ${management.endpoints.web.basePath:/actuator}/prometheus
eureka.instance.metadataMap.prometheus.port = ${management.server.port}
```

## Grafana  平台搭建
整合 Prometheus 数据源，构建 Java 应用监控指标图形化

