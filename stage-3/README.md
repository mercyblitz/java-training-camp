<a name="Dc7E9"></a>
# 简介
高并发、高性能、高可用（“三高”）是现代分布式应用架构的核心宏观指标，也是工程师和架构师的重要技术分水岭。随着互联网技术的普及，“三高”架构已成为企业级应用的必经之路。尽管“三高”架构被大量的技术资源讨论，然而有些缺少实战性的内容，有些则陷入某些技术的底层细节，鲜有通过项目的方式，将理论和实践高度整合，无形中降低了参考价值，也陡峭了学习曲线。

本期训练营作为 Java 分布式架构的第三期，将采用项目驱动的方式，结合 Github 知名 Java 开源电商项目 - [Shopizer](https://github.com/shopizer-ecommerce/shopizer)实现技术、架构和理论无缝整合。该项目基于 Spring 技术栈（Spring Boot、Spring Security）实现前、后端以及管理端分离，支持 CI/CD、漏洞检测、开源支持和开放标准。本期将在项目原有的技术和架构的基础上进行深度挖掘和改造，从基础设施到内部服务逐步模块化地实现高并发、高性能与高可用架构。同时，三期强关联 [一期（Java 分布式架构 - 服务治理）](https://mqu.h5.xeknow.com/s/1I2W75a) 和  [二期（Java 分布式架构 - 模式、设计与实现）](https://mqu.xet.tech/s/1UDiMh)的内容，实现技能闭环，并且待项目迭代的优化后，均有压力测试，评估性能的变化。
<a name="tUQU7"></a>
# 报名信息
<a name="JykoE"></a>
## [[本期报名]](https://mqu.xet.tech/s/9PPWG)
<a name="W5kN1"></a>

## [[福利报名]](https://github.com/mercyblitz/java-training-camp/blob/main/README.md#%E6%8A%A5%E5%90%8D%E7%A6%8F%E5%88%A9)

<a name="MMln1"></a>
## 
<a name="UpMpa"></a>
# 课程详情
<a name="BMKTP"></a>
## 开营周：训练营规划和开营
<a name="gbc9o"></a>
### [[公开课] 第三期 课程规划](https://mqu.h5.xeknow.com/sl/ZUYuD)
<a name="POQVw"></a>
### [[公开课] 第三期 课程开营](https://mqu.h5.xeknow.com/sl/3Z96d0)
<a name="quOI2"></a>
## 热身周：训练营项目准备
<a name="cXW5K"></a>
### [[公开课] 电商项目 Shopizer 介绍](https://mqu.h5.xeknow.com/sl/1xOmw3)
<a name="Ny8cc"></a>
#### 主要内容

- 功能特性：介绍 Java 开源电商项目 Shopizer 功能，包括：在线商店、市场、产品列表、B2B应用程序、交易门户以及安全等
- 技术架构：Shopizer 基于 Spring 技术栈实现前、后端以及管理端分离，支持 CI/CD、漏洞检测、开源支持和开放标准
- 项目结构：讨论 Shopizer 店铺、类目、商品、优惠券、用户、购物车、订单和搜索等模块

<a name="n5D7E"></a>
### [[公开课] Shopizer 项目“高并发、高性能与高可用”优化计划](https://mqu.h5.xeknow.com/sl/rRBM)
<a name="x7Ksg"></a>
#### 主要内容

- 性能评估：测试 Shopizer 后台服务 API 性能， 汇总多维度 API 性能报告（RT、TPS、QPS、内存、CPU 和 I/O 等）
- 重构评估：结合 API 性能报告，评估店铺、类目、商品、用户、购物车等模块功能
- 优化清单：列举架构优化清单，包括：服务进程、注册中心、服务调用、数据库、分布式事件、API 网关、分布式缓存、分布式锁、分布式文件系统、配置中心、监控平台等

<a name="PoLtv"></a>
## 第一周：“高并发、高性能与高可用”架构优化准备
<a name="YFqe4"></a>
### [[公开课] 第一节：“高并发、高性能与高可用”架构优化准备（一）](https://mqu.h5.xeknow.com/sl/1GLvKy)
<a name="T2eWR"></a>
#### 主要内容

- 可观测性整合：Shopizer 项目整合 Micrometer + Promethues + Grafana，针对 JVM、REST、Tomcat、JDBC、JPA 等监控（关联第一期 第八周：服务监控平台设计）
- 服务容错性整合：Shopizer 项目整合 Alibaba Sentinel，适配 Sentinel Metrics 到 Micrometer

<a name="hUmqv"></a>
### [[公开课] 第二节：“高并发、高性能与高可用”架构优化准备（二）](https://mqu.h5.xeknow.com/sl/yhNtu)
<a name="S7VTF"></a>
#### 主要内容

- JFR 整合：根据 API 性能报告，分析目标服务，引入 JFR 事件，并输出 JFR 报告

<a name="O5rZm"></a>
## 第二周：“高并发、高性能与高可用”服务进程
<a name="jlVOK"></a>
### [第三节：高并发、高性能服务容器调优](https://mqu.h5.xeknow.com/sl/1GdryY)
<a name="K3nKf"></a>
#### 主要内容

- Web 服务调优：动态调整 Tomcat 线程池、请求队列、网络参数，调优 Web 服务并发和性能
- JVM 调优：调整 JVM Heap 大小，优化新老生代空间比率，选择合适 GC 算法，平衡性能和吞吐量
- Spring Boot 优化：最小化 Spring Boot 自动装配组件，降低内存足迹和减少 CPU 计算

<a name="eLZBa"></a>
### [第四节：高可用微服务架构升级](#)
<a name="xYDOl"></a>
#### 主要内容

- 微服务化改造：将 Shopizer 项目中店铺、类目、商品、优惠券、用户、购物车、订单和搜索等模块进行微服务改造
- Spring Cloud 架构升级：将上下游服务之间通过 Spring Cloud 方式调用，评估架构升级后的各应用 API 的性能指标，对比前后性能变化

<a name="szEzE"></a>
## 第三周：“高并发、高性能与高可用”注册中心 - Eureka
<a name="zLnjq"></a>
### [第五节：“高可用” Eureka 服务注册与发现](https://mqu.h5.xeknow.com/sl/34Oz47)
<a name="V51IV"></a>
#### 主要内容

- 自我保护机制：理解 Eureka Server Self Preservation Mode，以及在高可用场景下的价值
- P2P 协议：掌握 Eureka Sever 点对点通讯协议的细节
- Eureka Sever 调优：优化 Eureka Server 配置，压测对比调优前后的性能指标

<a name="dD0yX"></a>
### [第六节：“高性能”Eureka Server 架构](https://mqu.h5.xeknow.com/sl/3h3U0K)
<a name="RVDWd"></a>
#### 主要内容

- P2P 协议改造：基于 JGroup 广播 Eureka Server 服务实例状态，代替 Eureka Server 

<a name="UNlT7"></a>
## 第四周：“高并发、高性能与高可用”服务调用
<a name="jAXM7"></a>
### [第七节：“高性能”HTTP 服务架构升级](https://mqu.h5.xeknow.com/sl/1Mw8fb)
<a name="Ueg6r"></a>
#### 主要内容

- 异步 HTTP 升级：基于 Servlet 3.0+ 异步特性升级  Shopizer 项目 API 服务，对比升级前后性能变化
- 非阻塞 HTTP 升级：基于 Servlet 3.1+ 非阻塞特性升级  Shopizer 项目 API 服务，对比升级前后性能变化
- HTTP 2.0 架构升级：基于 Servlet 4.0+ 实现 Shopizer 项目支持 HTTP 2.0 服务，对比 HTTP 1.1 与 2.0 性能变化

<a name="QW2sc"></a>
### [第八节：“高并发、高可用”RPC 架构升级](https://mqu.h5.xeknow.com/sl/471EY6)
<a name="eMi21"></a>
#### 主要内容

- gRPC 服务升级：实现 Shopizer 项目 API gRPC 服务优化，对比 REST 与 gRPC 性能变化
- Dubbo Triple 协议升级：使用 Dubbo Triple 优化 Shopizer 项目 API 性能
- RPC 服务定制：根据业务权重为 RPC 服务定制线程消费、协议和负载均衡

<a name="kw44m"></a>
## 第五周：“高并发、高性能与高可用”数据存储
<a name="AC7ln"></a>
### [第九节：“高可用”MySQL 数据库](https://mqu.h5.xeknow.com/sl/2IsOU5)
<a name="Nmuwe"></a>
#### 主要内容

- 主从复制：搭建 MySQL 一主多从的数据库架构
- 读写分离：基于 JDBC 实现客户端读写分离，使用 MySQL 数据库数据读写分离
- MGR 架构升级：使用 MySQL Group Replication 架构提到 MySQL 主从复制，提高数据同步一致性

<a name="tuJKK"></a>
### [第十节：“高并发、高性能”数据存储](https://mqu.h5.xeknow.com/sl/2FD4r6)
<a name="b25Zm"></a>
#### 主要内容

- SQL 优化：减少 Shopzier 关系表之间的外键，级联关系，以及合理构建 SQL 索引
- MyBatis 升级：使用 MyBatis 部分替换 JPA 实现，提升执行 SQL 效率
- 分库分表：基于 ShardingSphere 重构订单、商品等应用数据

<a name="JHrQD"></a>
## 第六周：“高并发、高性能与高可用”异步服务
<a name="DWRg4"></a>
### [第十一节：“高性能、高可用”分布式事件](https://mqu.h5.xeknow.com/sl/391VAv)
<a name="VKE1D"></a>
#### 主要内容

- Kafka 集群：构建“高可用”Kafka Broker 集群
- 分布式事件：基于 Kafka 技术实现分布式事件体系，为未来 MySQL、Redis 、ES 数据同步提供抽象基础
- 商品事件：重构商品事件，移除 AOP 动态拦截、采用静态拦截方式发布分布式事件，异步监听事件，提升性能

<a name="WvbfD"></a>
### [第十二节：“高并发”Reactive 异步服务](https://mqu.h5.xeknow.com/sl/2jVypP)
<a name="d809g"></a>
#### 主要内容

- 客户端 Reactive：基于 WebFlux 重构用户管理 REST 服务，比对 Servlet 异步 + 非阻塞的性能变化
- 服务端 Reactive：基于 RSocket 实现服务端 Reactive 化，并利用背压（Back-Pressure） 重构订单处理，提升系统吞吐量

<a name="r8dKV"></a>
## 第七周：“高并发、高性能与高可用”网关
<a name="XbrLb"></a>
### [第十三节：“高并发、高性能与高可用”API 网关](#)
<a name="mYM2u"></a>
#### 主要内容

- 服务聚合网关：基于 Spring Cloud Gateway 聚合 Shopzier 项目个应用 API 服务，统一抽象和收集 Spring MVC、Spring WebFlux 以及 Servlet Mappings 上报元信息
- 模块化网关：根据业务权重，使业务模块独立映射和部署，实现资源优化配置

<a name="nPWN1"></a>
### [第十四节：“高并发、高性能与高可用”RPC 网关](https://mqu.h5.xeknow.com/sl/3cdJmG)
<a name="vhnOQ"></a>
#### 主要内容

- gRPC 网关：为 Shopzier Spring Cloud Gateway API  整合 gRPC HTTP/2
- Dubbo 网关：基于 Dubbo 泛化特性实现 Dubbo 网关，无缝整合 gRPC 和 Triple

<a name="eTDAo"></a>
## 第八周：“高并发、高性能与高可用”Service Mesh
<a name="D4Opn"></a>
### [第十五节：“高并发、高性能与高可用”Istio](https://mqu.h5.xeknow.com/sl/2B0OjG)
<a name="nk80o"></a>
#### 主要内容

- Istio 网关：新增 Shopzier 类目 API 部署为 Istio 网关
- 流量控制：基于版本、流量分配的流量控制，以及流量负载均衡
- 可观测性：整合 Prometheus监测、自定义Metrics 以及分布式追踪

<a name="wz70o"></a>
### [第十六节：“高并发、高性能与高可用”Dubbo Mess](https://mqu.h5.xeknow.com/sl/2yquHP)
<a name="eHeRE"></a>
#### 主要内容

- Dubbo K8s：使用 K8s 注册中心，逐步替代 Eureka 注册中心，实现 Shopzier API 在 Spring Cloud 和 Dubbo 场景双注册
- Dubbo Mess：理解 Dubbo Mess 架构，如：Proxy Mesh 和 Proxyless Mesh、Control Plane（控制面）
- Mess 重构：重构 Shopzier 类目 RPC 部署为 Dubbo Mess

<a name="GHTkc"></a>
## 第九周：“高并发、高性能与高可用”配置中心
<a name="QGmLU"></a>
### [第十七节：“高并发、高性能与高可用”配置中心 - Nacos](https://mqu.h5.xeknow.com/sl/pGLTk)
<a name="LzFmD"></a>
#### 主要内容

- Raft ⼀致性模型：基于 Nacos CP 一致性模型，集群部署 Nacos Server，压测性能和稳定性
- DB 模型：基于高可用 MySQL Server，实现  Nacos Server 部署，压测性能和稳定性

<a name="cWpxe"></a>
### [第十八节：“高并发、高性能与高可用”配置中心 - etcd](https://mqu.h5.xeknow.com/sl/2E1LvH)
<a name="f7fQv"></a>
#### 主要内容

- etcd 简介：掌握 etcd 安装、存储、Watch 机制、高性能、数据一致性（RAFT），以及使用场景：键值对存储、服务注册与发现、消息发布与订阅和分布式锁等
- 高可用 etcd ：使用 etcd 网关构建 ectd 集群，提高可伸缩性
- etcd Java 配置客户端：基于 microsphere-spring-config 实现 etcd Java 配置客户端

<a name="fO4qP"></a>
## 第十周：“高并发、高性能与高可用”监控平台
<a name="p5Ruc"></a>
### [第十九节：“高并发、高性能与高可用”日志平台](https://mqu.h5.xeknow.com/sl/4F8MTT)
<a name="CyiDz"></a>
#### 主要内容

- ELK 搭建：简单搭建 Elasticsearch、Logstash 以及 Kibana 日志基础平台
- Kafka 集群整合：Java 客户端日志 Appender 整合 Kafka，Logstash 与 Kafka 整合
- ES集群搭建：部署 ES Master Node、Data Node 以及 Client Node，定期清理 ES 上的历史数据

<a name="oDwPp"></a>
### [第二十节：“高并发、高性能与高可用”监控平台](https://mqu.h5.xeknow.com/sl/4xvq6m)
<a name="XIdxv"></a>
#### 主要内容

- Victoria Metrics 简介：理解 Victoria Metrics 的使用场景，功能特性，以及如何无缝迁移 Prometheus
- Victoria Metrics 集群：掌握 Victoria Metrics 集群部署方式
- OpenTSDB 整合：基于 OpenTSDB  编写业务指标（Metrics），导出 Victoria Metrics 集群

<a name="TybZO"></a>
## 第十一周：“高并发、高性能与高可用”Native 应用
<a name="iKyxN"></a>
### [第二十一节：“高并发、高性能与高可用”Spring Native 应用](https://mqu.h5.xeknow.com/sl/YQ645)
<a name="XsZ7I"></a>
#### 主要内容

- 构建 Spring Native 应用：基于 Spring Native 构建 Shopzier Native 应用，并且再次压测，比较单体、微服务以及 Native 应用的性能差异
- 构建 Nacos Server Native 应用：将 Nacos Server 部署为 Native 应用

<a name="i7rTF"></a>
### [第二十二节：“高并发、高性能与高可用”Java Native 应用](#)
<a name="JHacC"></a>
#### 主要内容

- Java Native 基础：了解 GraalVM Java Native 构建基础，包括：Class Loading 限制、反射限制以及其他提前部署
- Dubbo Native 应用：重构部分 Shopzier 类目等应用使其成为 Dubbo Native 应用

<a name="eRo8B"></a>
## 第三期 结营
<a name="XARlp"></a>
### [[公开课] 第三期 课程结营](https://mqu.h5.xeknow.com/sl/15QbmL)
<a name="iyPIn"></a>
## 第四期规划
