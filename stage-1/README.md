# 报名信息

## [[本期报名]](https://mqu.h5.xeknow.com/s/1I2W75a)

## [[福利报名]](https://github.com/mercyblitz/java-training-camp/blob/main/README.md#%E6%8A%A5%E5%90%8D%E7%A6%8F%E5%88%A9)

# 课程详情
<a name="POQVw"></a>
## [第一期 开营直播](https://mqu.h5.xeknow.com/sl/1tQV0o)
<a name="spGzM"></a>
## [第一期 课前预热直播](https://mqu.h5.xeknow.com/sl/2inj2W)
<a name="Y7mjD"></a>
## 第一周：基础框架与应用工程搭建
<a name="oeZp6"></a>
### 本周目标

- 基于 Spring 技术栈与 Maven 模块化构建企业工程
- 理解 Spring 模块化设计
- 掌握基础和业务组件模块化设计
- 掌握企业基础框架发布流程和升级策略
- 掌握企业标准化工程作业流程
<a name="bRLra"></a>
### [第一节：基础框架工程构建](https://mqu.h5.xeknow.com/sl/2inj2W)
<a name="hSLq3"></a>
#### 预备技能

- 熟悉 Maven 基本使用
- 了解模块化工程运用
- 了解 Maven 版本管理
- 掌握 Codebase 基本使用
- 了解 CI/CD 工具基本使用
<a name="Ny8cc"></a>
#### 主要内容

- Maven 基础：依赖关系、多模块层次、BOM，插件架构以及构建体系
- 基础框架工程结构设计：模块化工程组织，模块依赖设计，Java package 命名规范等
- 基础框架版本策略：企业内部基础框架版本选型策略，SNAPSHOT 还是 RELEASE
- 三方库升级策略：如何为业务应用优雅升级三方库，如 Spring Boot、Spring Cloud 等基础三方库
- 基础框架发布流程：配合 codebase 指定并执行企业级基础框架发布流程

<a name="YFqe4"></a>
### [第二节：业务工程模板定制](https://mqu.h5.xeknow.com/sl/2l3Xx7)
<a name="tg9BS"></a>
#### 预备技能

- 了解 Spring 常用模块（组件） Maven 依赖
- 基础掌握 Maven 依赖管理
- 了解 Maven BOM 基础
- 了解常见 Maven 插件使用
<a name="T2eWR"></a>
#### 主要内容

- Spring 模块化设计：介绍 Spring Framework、Spring Boot 以及 Spring Cloud 各个模块的组织关系
- 业务工程模块化设计：借鉴 Spring 模块化设计，设计业务应用模块化，实现高内聚、低耦合的业务模块，掌握最小化 Artifact 依赖提炼，解决不必要的传递依赖问题，Java package 命名规范等
- 业务组件 BOM 设计：将业务 API 通过 Maven BOM 的方式给其他应用使用
- 业务组件依赖管理：多方业务组件统一管理
- 业务工程构建：通过 Maven 插件为业务应用生成项目元信息，约束工程行为等

<a name="O5rZm"></a>
## 第二周：REST API 设计
<a name="E04NI"></a>
### 本周目标

- 掌握基于 Spring 技术栈设计和实现 REST API 通讯
- 掌握 API 模型无关性和编程友好性设计
- 掌握多版本 REST API 管理与升级
<a name="jlVOK"></a>
### [第三节：REST API 服务端设计](https://mqu.h5.xeknow.com/sl/1lO3ba)
<a name="bhF1w"></a>
#### 预备技能

- 了解 REST 理论
- 了解 Web 幂等性
- 掌握 Spring WebMVC 基本使用
- 掌握 Bean Validation 基本使用
<a name="K3nKf"></a>
#### 主要内容

- API 模型：定义统一的 REST 请求（Request）和 响应（Response） API 模型
- 服务端 API 校验：了解 Bean Validation 与 Spring WebMVC 校验原理
- 服务端 API 异常处理：使用 Spring WebMVC 实现统一异常处理
- 服务端 API POJO 通讯：使用 POJO 接口声明，基于 Spring WebMVC 实现  API 模型隐形包装
- 服务端 API 幂等性：基于 Spring WebMVC 无缝整合 REST  幂等性校验
- 服务端多版本 API 实现：基于 Spring WebMVC 实现多版本 API 并行，实现 API 版本平滑升级

<a name="zLnjq"></a>
### [第四节：REST API 客户端设计](https://mqu.h5.xeknow.com/sl/CQBlA)
<a name="JLuxG"></a>
#### 预备技能

- 掌握 Spring RestTemplate 基本使用
- 了解 Spring WebClient 基本使用
- 掌握 Spring Cloud Open Feign 基本使用
- 了解 Spring Retry 基本使用
- 了解 Bean Validation 基本使用
<a name="cFWXI"></a>
#### 主要内容

- 客户端 API 校验：基于 Spring RestTemplate、Spring WebClient  以及 Spring Cloud Open Feign 整合 Bean Validation，实现 REST API 客户端校验
- 客户端 API  异常处理：基于 Spring RestTemplate、Spring WebClient  以及 Spring Cloud Open Feign 实现统一异常处理，并预留国际化文案扩展
- 客户端 API  POJO 通讯：基于 Spring Cloud Open Feign 隐形包装 POJO 成为 API 模型，实现接口编程友好性目的
- 客户端多版本 API 调用：基于 Java 接口实现多版本 API 调用，达到 API 平滑升级的目的

<a name="UNlT7"></a>
## 第三周：站点国际化设计
<a name="JavKj"></a>
### 本周目标

- 理解 JDK 国际化以及 Spring 国际化
- 掌握国际化 API 设计
- 掌握 REST API 国际化文案整合实现
- 理解第三方框架国际化整合实现
- 掌握国际化文案动态配置实现
<a name="AC7ln"></a>
### [第五节：站点国际化设计](https://mqu.h5.xeknow.com/sl/2nFKVt)
<a name="b4gXF"></a>
#### 预备技能

- 了解 Java 国际化
- 了解 Java 格式化
- 了解 Spring 配置
- 了解 Spring 国际化
- 熟悉 Java 线程安全
<a name="owR9F"></a>
#### 主要内容

- 国际化基础：简介 JDK 和 Spring 国际化的同异，分析 Spring WebMVC 国际化实现
- 易用性设计：提供易用国际化 API，替代 JDK 和 Spring 国际化 API 
- 可配置设计：实现国际化文案配置化，优雅地处理字符编码问题
- 高性能设计：提供高性能国际化文案，提升国际化文案读取性能，以及解决传统 JDK 以及 Spring 文案格式化性能瓶颈
- 热部署设计：支持国际化文案热部署，实时获取内容变更，实现线程安全

<a name="xiWJe"></a>
### [第六节：站点国际化整合](https://mqu.h5.xeknow.com/sl/3vI89z)
<a name="gtdPF"></a>
#### 预备技能

- 掌握 Spring WebMVC 基本使用
- 掌握 Spring Cloud Open Feign  基本使用
- 掌握 Bean Validation 基本使用
<a name="Vy3I2"></a>
#### 主要内容

- 服务端 REST API  国际化整合：无缝整合国际化 API 与 REST API 模型，实现应用程序零修改
- 客户端 REST API 国际化整合：Spring Cloud Open Feign 整合
- 模板引擎整合：Spring Web 模板
- Bean Validation 整合：Bean Validation（Hibernate Validator）国际化整合
<a name="bog0Q"></a>
## 
<a name="JHrQD"></a>
## 第四周：Web 服务容错性设计
<a name="hygQ6"></a>
### 本周目标

- 理解 Apache Tomcat 架构以及核心组件
- 掌握 Resilience4j 核心模块和 API 使用
- 掌握基于 Apache Tomcat 实现 Web 服务容错性
- 掌握基于 Resilience4j 实现 Web 服务容错性
<a name="DWRg4"></a>
### [第七节：基于 Apache Tomcat 实现 Web 服务容错性](https://mqu.h5.xeknow.com/sl/MpzVy)
<a name="zbLlg"></a>
#### 预备技能

- 了解 Apache Tomcat 基本架构和核心组件
- 熟悉 Java 并发编程
- 了解 Servlet 线程模型
<a name="VKE1D"></a>
#### 主要内容

- Tomcat 线程模型：结合 Java AQS 和 线程池等基础，理解Tomcat 线程模型
- Tomcat 核心组件：理解 Tomcat 网络连接，协议处理等核心组件，掌握 Spring Boot 对其管控细节
- Tomcat 限流：利用 JMX 和 Tomcat API 实现全局 Web 服务限流
<a name="WvbfD"></a>
### [第八节：基于 Resilience4j 实现 Web 服务容错性](https://mqu.h5.xeknow.com/sl/IUHBL)
<a name="fgYnA"></a>
#### 预备技能

- 了解 Resilience4j 基本使用
- 熟悉 Java 并发编程
- 了解 Servlet API 使用
- 熟悉 Spring WebMVC 基本使用
- 了解 Spring WebFlux  基本使用
<a name="d809g"></a>
#### 主要内容

- Resilience4j 基础：掌握服务 CircuitBreaker、Bulkhead 以及 RateLimiter 等模块特性以及核心 API 使用
- Resilience4j Servlet 整合 ：基于 Resilience4j API 实现 Servlet 熔断、限流等特性
- Resilience4j Spring WebMVC 整合：基于 Resilience4j API 实现 Spring WebMVC 熔断、限流等特性
- Resilience4j  Spring WebFlux 整合：基于 Resilience4j API 实现Spring WebFlux 熔断、限流等特性

<a name="FTOKk"></a>
## 第五周：服务容错性高阶设计
<a name="r65P3"></a>
### 本周目标

- 掌握 Spring Cloud Config 动态变更设计与实现
- 熟悉 MyBatis Plug-in 扩展机制
- 熟悉 Spring Redis 核心 API
- 掌握 Resilience4j 底层原理
- 理解 Resilience4j 与 Spring Boot 以及 Spring Cloud 组件整合实现原理
- 掌握 Resilience4j  与第三方框架整合设计模式
<a name="VPUcW"></a>
### [第九节：Resilience4j  整合第三方框架](https://mqu.h5.xeknow.com/sl/2AC0mQ)
<a name="n4tZo"></a>
#### 预备技能

- Resilience4j API 使用
- Spring Cloud Open Feign 基础 
- MyBatis 基础
- Spring Redis 基础
<a name="KmRRV"></a>
#### 主要内容

- Resilience4j Spring Cloud Open Feign 扩展：基于 Resilience4j 实现通用 Spring Cloud Open Feign 熔断、限流等功能
- Resilience4j  MyBatis 扩展：基于 Resilience4j 实现 MyBatis  熔断、限流等功能
- Resilience4j Redis 扩展：基于 Resilience4j  实现 Spring Redis 熔断、限流等功能
<a name="jbNBs"></a>
### [第十节：服务容错性动态变更设计](https://mqu.h5.xeknow.com/sl/4wXJOK)
<a name="BOVKm"></a>
#### 预备技能

- 了解 Spring Cloud Config 基本使用
- 掌握 Spring Boot @ConfigurationProperties 基本使用
- 了解 Apache Tomcat 核心 API 基本使用
- 了解 Resilience4j 基本使用
<a name="rQpDS"></a>
#### 主要内容

- Spring Cloud Config 动态变更：理解 Spring Cloud Config 与 Spring Boot @ConfigurationProperties Bean 动态绑定的关系
- 动态 Tomcat 组件更新：使用 Spring Cloud Config 实现动态 Tomcat 组件更新
- 动态 Resilience4j 组件更新：使用 Spring Cloud Config 实现动态 Resilience4j 组件更新

<a name="JhpZF"></a>
## 第六周：服务柔性负载均衡设计
<a name="Rr2WJ"></a>
### 本周目标

- 了解常见负载均衡策略
- 了解监控指标数据设计
- 了解动态权重算法
- 掌握 Spring Cloud Commons 服务实例元信息设计
- 掌握 Spring Cloud Netflix Ribbon 负载均衡实现
- 掌握 Spring Cloud LoadBalancer 负载均衡实现

<a name="we6U9"></a>
### [第十一节：基于监控指标的负载均衡实现](https://mqu.h5.xeknow.com/sl/28I6Mv)
<a name="XLeIM"></a>
#### 预备技能

- 了解 JMX 架构和内建指标 MBean
- 了解系统监控指标相关术语
- 熟悉 Spring Cloud 服务注册与发现基本使用
- 了解 Spring Cloud 客户端负载均衡基本使用
<a name="OvtJi"></a>
#### 主要内容

- 核心监控指标：掌握 CPU 使用率、系统负载（Load）、线程状态（Threading）、响应时间（RT）、QPS 以及 TPS 等核心指标
- Netflix Servo：理解 Netflix Servo 架构和监控指标组件
- 上报监控指标：基于 Spring Cloud 服务注册接口实现监控指标上报
- 负载均衡实现：基于 Netflix Servo 监控指标实现 Spring Cloud Netflix Ribbon（老版本）负载均衡策略

<a name="Nj8iV"></a>
### [第十二节：基于动态权重的负载均衡实现](https://mqu.h5.xeknow.com/sl/36Zt22)
<a name="QT9Hb"></a>
#### 预备技能

- 了解权重计算规则
- 熟悉 Spring Cloud 服务注册与发现基本使用
- 了解 Spring Cloud 客户端负载均衡基本使用
<a name="b2ITF"></a>
#### 主要内容

- 动态权重算法：基于服务实例启动时间（uptime） 以及初始化权重，动态计算权重值
- 元数据上报：基于 Spring Cloud 服务注册接口实现服务实例启动时间（uptime）以及初始化权重数据上报
- 负载均衡实现：基于 动态权重算法 实现 Spring Cloud LoadBalancer（新版本）负载均衡策略

<a name="BWEyL"></a>
## 中期回顾 
<a name="Tjv49"></a>
### [第一期 中期回顾（上）- 四到六周](https://mqu.h5.xeknow.com/sl/4aqvhv)
<a name="fWs3I"></a>
### [第一期 中期回顾（下）- 一到三周](https://mqu.h5.xeknow.com/sl/3MLveo)
<a name="jR56p"></a>
## 第七周：服务监控基础
<a name="okKS5"></a>
### 本周目标

- 理解监控指标核心概念
- 掌握 JMX HTTP 桥接框架 Jolokia
- 掌握指标框架 Micrometer
- 理解 Spring Boot 与 Micrometer 整合的底层细节
- 掌握 Micrometer 与第三方框架整合
<a name="jIUav"></a>
### [第十三节：Micrometer 基础](https://mqu.h5.xeknow.com/sl/39xeAl)
<a name="pULRI"></a>
#### 预备技能

- 了解 JMX 架构和内建指标 MBean
- 了解 Spring Boot 自动装配
- 了解 Spring Boot Actuator Metrics
<a name="AdLAu"></a>
#### 主要内容

- 指标核心概念：理解指标基本类型 - Timer, Counter, Gauge, DistributionSummary 等，以及指标 Tags
- Micrometer 核心 API：掌握 Timer, Counter, Gauge, DistributionSummary，MeterBinder，MeterRegistry 等 API 使用和底层原理
- Micrometer 内建 Binder：讨论 Micrometer 内建 Binder，包括 JVM 、Kafka、Logging、系统、Tomcat 等

<a name="ySvi3"></a>
### [第十四节：Micrometer 整合第三方框架](https://mqu.h5.xeknow.com/sl/2x1rIo)
<a name="ujXGb"></a>
#### 预备技能

- 了解 Netflix Servo 基本架构
- 了解 Redis Spring 核心 API 使用
- 了解 MyBatis 核心 API 使用
- 了解 JDBC 核心 API 使用
<a name="JwLth"></a>
#### 主要内容

- Micrometer 适配 Netflix Ribbon 监控指标：适配 Ribbon 内部 Servo 监控指标到 Micrometer 方式
- Micrometer 整合 Redis Spring：Redis Spring API 监控指标注册到 MeterRegistry
- Micrometer 整合 MyBatis ：基于 MyBatis Plug-in 机制将监控指标注册到 MeterRegistry
- Micrometer 整合 JDBC：基于 JDBC 核心 API 将监控指标注册到 MeterRegistry

<a name="q3Vdb"></a>
## 第八周：服务监控平台设计
<a name="Yi0LU"></a>
### 本周目标

- 掌握 Micrometer 注册中心实现
- 了解 Prometheus 工作原理
- 了解 Grafana 工作原理
- 搭建 Java 微服务服务监控平台
<a name="tlav0"></a>
### [第十五节：基于 Pull 方式指标监控平台设计](https://mqu.h5.xeknow.com/sl/1BuiyP)
<a name="nhKYI"></a>
#### 预备技能

- 了解 Prometheus 基本使用
- 了解 Grafana 基本使用
- 掌握激活 Spring Boot Actautor Prometheus  Endpoint 方式
<a name="clWlj"></a>
#### 主要内容

- Prometheus  Endpoint：讨论 Spring Boot Actautor Prometheus  Endpoint 与 Micrometer 适配细节
- Prometheus 平台搭建：Prometheus 使用 Spring Cloud 注册中心发现服务实例，并拉取应用 Metrics 数据
- Grafana  平台搭建：整合 Prometheus 数据源，构建 Java 应用监控指标图形化
<a name="wNDKv"></a>
### [第十六节：基于 Push 方式指标监控平台设计](https://mqu.h5.xeknow.com/sl/4hizeB)
<a name="fLfyM"></a>
#### 预备技能

- 了解 Prometheus 架构
- 了解 InfluxDB 基本使用
- 了解 Micrometer 注册中心相关内容
<a name="nqCIk"></a>
#### 主要内容

- Prometheus Pushgateway 搭建：搭建 Prometheus Pushgateway，为 Java 应用推送指标做准备
- Micrometer Prometheus 注册中心：掌握 Micrometer Prometheus 注册中心使用方法，了解基本底层实现
- Micrometer InfluxDB 注册中心：切换 Micrometer  InfluxDB 注册中心，了解两种时序数据库的差异
- 指标监控平台混合模式：掌握 Pull 和 Push 监控数据混搭模式

<a name="dE0wS"></a>
## 第九周：服务链路追踪设计
<a name="CPdCW"></a>
### 本周目标

- 掌握 Spring Cloud Sleuth 实现原理
- 掌握 Java Instrument 编程技巧
- 理解 Pointpint、Skywalking 链路追踪基本原理
<a name="CXkdN"></a>
### [第十七节：基于 Java 应用层追踪服务链路](https://mqu.h5.xeknow.com/sl/3bvvgs)
<a name="DG0WK"></a>
#### 预备知识

- 理解 Dapper 分布式跟踪理论
- 熟悉 Spring Cloud 组件
- 熟悉 Spring WebMVC 组件
- 熟悉 Java Logging 架构
<a name="E47qw"></a>
#### 主要内容

- Spring Cloud Sleuth 引入：借助 Spring Cloud Sleuth 组件理解 Java 应用层服务链路追踪基本架构
- Spring Cloud Sleuth 核心 API：理解 Tracer、Span 等 API 基本使用方法
- Spring Cloud Sleuth 第三方整合：使用 Span API 整合第三方框架，如 MyBatis、Redis 等
<a name="CW9VT"></a>
### [第十八节：基于 Java Instrument 追踪服务链路重构](https://mqu.h5.xeknow.com/sl/1V4Iql)
<a name="LC4rP"></a>
#### 预备知识

- Java ClassLoader 类加载机制
- Java 字节码提升基础
- Java Instrument 基础
<a name="DH80I"></a>
#### 主要内容

- Java Instrument 机制：理解 Java Instrument 机制，并掌握字节码提升编程
- Web 服务链路重构：基于字节码提升工具 Spring Cloud Open Feign 以及 Spring WebMVC 
- 第三方服务链路重构：重构 Redis、JDBC 以及 MyBatis 服务链路实现
<a name="wPMAS"></a>
## 第十周：服务网关整合设计
<a name="pktuM"></a>
### 本周目标

- 掌握 Spring Cloud Gateway 使用和底层原理
- 掌握 Spring Cloud Gateway 整合 Resilience4j 实现服务容错性
- 掌握 Spring Cloud Gateway 整合 LoadBalancer  实现柔性负载均衡策略
- 掌握 Spring Cloud Gateway 整合 Spring Cloud Sleuth 实现追踪 Web 服务链路
- 掌握 Spring Cloud Gateway 整合 Mircometer 监控服务指标
<a name="lO3EG"></a>
### [第十九节：服务网关稳定性设计](https://mqu.h5.xeknow.com/sl/2g9CpX)
<a name="Ab7Tk"></a>
#### 预备技能

- 了解 Spring Cloud Gateway
- 熟悉 Resilience4j 模块 API 以及扩展
- 熟悉 Spring Cloud LoadBalancer API 以及扩展
<a name="pn2XM"></a>
#### 主要内容

- 网关容错性设计：Spring Cloud Gateway 结合 Resilience4j 实现熔断和限流
- 网关柔性设计：Spring Cloud Gateway 整合柔性 LoadBalancer 实现
<a name="nh8yu"></a>
### [第二十节：服务网关可观测性设计](https://mqu.h5.xeknow.com/sl/4aZWTT)
<a name="wPLAn"></a>
#### 预备技能

- 熟悉 Micrometer 扩展
- 熟悉 Spring Cloud Slueth API 
<a name="oAod9"></a>
#### 主要内容

- 网关监控设计：Spring Cloud Gateway 整合 Micrometer 实现指标监控
- 网关链路跟踪设计：Spring Cloud Gateway 整合 Spring Cloud Slueth 实现链路跟踪
<a name="sKcMN"></a>
## 第十一周：服务性能优化
<a name="pCkHp"></a>
### 本周目标

-  掌握 Spring Framework 性能优化
- 掌握 Spring Boot 性能优化
- 掌握 Spring Cloud 性能优化
<a name="lk2qr"></a>
### [第二十一节：Spring Web 性能优化](https://mqu.h5.xeknow.com/sl/1yeQC8)
<a name="pK3vR"></a>
#### 预备技能

- 了解 Spring WebMVC 核心组件和执行流程
- 了解 Spring Boot 自动装配
- 了解 Spring AOP 底层机制
<a name="A4nrm"></a>
#### 主要内容

-  Spring AOP 优化：集合 Spring Web 场景使用静态代理替换 Spring AOP 代理对象
- Spring Web 组件优化：优化非必需 Web 组件，减少计算时间和内存开销
- Spring Web 缓存优化：Spring WebMVC REST Request Body 和 Response Body 对象缓存优化，减少重复序列化和反序列化计算
- Spring Web REST 序列化/反序列化：提升 REST 序列化/反序列化性能，减少不必要的计算
<a name="r5Do3"></a>
### [第二十二节：Spring Cloud 性能优化](https://mqu.h5.xeknow.com/sl/3EFIyj)
<a name="kteOa"></a>
#### 预备技能

- 了解 Spring Boot 自动装配
- 了解 Spring Cloud 核心组件和执行流程
- 了解 Spring Web REST 序列化/反序列化
<a name="ZMjg7"></a>
#### 主要内容

- @RefreshScope 优化：替换 @RefreshScope 实现，减少 Spring 应用上下文停顿的风险
- Spring Cloud OpenFeign 优化：提升 REST 序列化/反序列化性能，提高 HTTP 传输效率，减少负载均衡计算消耗
- Spring Cloud 配置优化：失效 Bootstrap 应用上下文，优化配置读取实现，避免 System Properties 并发锁阻塞等问题
<a name="FbKBa"></a>
## 第十二周：工程脚手架定制
<a name="wF6fE"></a>
### 本周目标

- 理解 Spring 脚手架工作原理
- 为基础框架和业务组件定制脚手架模块
- 根据企业架构特点实现 Spring 脚手架“本土化”
<a name="fmmcO"></a>
### [第二十三节：Spring 脚手架运用、架构与定制](https://mqu.h5.xeknow.com/sl/5QRxC)
主要内容

- Spring 脚手架搭建：在本地和测试环境搭建 Spring 脚手架，并了解 CI/CD 环境中的注意事项
- Spring 脚手架架构：了解 Spring Start 与 Spring Initialzr 之间的关系，掌握 Spring Initialzr  各个模块的职责以及它们之间的联系
- Spring 脚手架定制：根据基础框架和业务组件的 BOM 以及依赖信息，定制它们的模块
<a name="MWVPa"></a>
### [第二十四节：Spring 脚手架原理、实现与扩展](https://mqu.h5.xeknow.com/sl/2fZTy8)
主要内容

- Spring 脚手架原理：理解 Spring Initialzr 工程构建的实现原理，包括：项目元信息、Project 子应用上下文以及 Web Endpoints
- Spring 脚手架实现：理解 Spring Initialzr 底层实现，包括：Maven 构建系统，代码生成原理以及元信息处理
- Spring 脚手架扩展：根据项目依赖组件实现多模块和动态 Maven 业务模板工程

<a name="eRo8B"></a>
## 第一期 结营
<a name="iyPIn"></a>
### [第二期规划](https://mqu.h5.xeknow.com/sl/MPC7x)
<a name="yrjvG"></a>
### [第一期 结营直播](https://mqu.h5.xeknow.com/sl/3schXR)
