# 简介
多活架构作为 小马哥 Java 分布式训练营第四期的内容，将采用项目驱动的方式，基于小马哥开源工程 [Microsphere Projects](https://github.com/microsphere-projects) 以及 Github 知名 Java 开源电商项目 - [Shopizer](https://github.com/shopizer-ecommerce/shopizer) 相结合的方式讨论。同时，四期强关联 [一期（Java 分布式架构 - 服务治理）](https://mqu.h5.xeknow.com/s/1I2W75a) 、  [二期（Java 分布式架构 - 模式、设计与实现）](https://mqu.xet.tech/s/1UDiMh)和[三期（Java 分布式架构 - 高并发、高性能、高可用）](https://www.yuque.com/mercyblitz/java-training-camp/stage-3)的积累，实现技能闭环。
# 课程报名
## 分期报名
### [[第一期 Java 分布式架构 - 服务治理]](https://mqu.h5.xeknow.com/s/1I2W75a)
### [[第二期 Java 分布式架构 - 模式、设计与实现]](https://mqu.xet.tech/s/1UDiMh)
### [[第三期 分布式高并发、高性能、高可用架构]](https://mqu.xet.tech/s/9PPWG)
### [[第四期 Java 分布式架构 - 多活架构（本期）]](https://mqu.xet.tech/s/9PPWG)
## 报名福利
报名过 [[第一期 Java 分布式架构 - 服务治理]](https://mqu.h5.xeknow.com/s/1I2W75a) 或 [[第二期 Java 分布式架构 - 模式、设计与实现]](https://mqu.xet.tech/s/1UDiMh) 或 [[第三期 Java 分布式架构 - 高并发、高性能、高可用](https://www.yuque.com/mercyblitz/java-training-camp/stage-3)]

- 早鸟优惠：报名一期减免 **500** 元、依次类推
   - 时间限制：2023.10.15 截止
## 课程合集

- 一、二期合集

原价：~~11998~~ 元，优惠价：**5998** 元，打包价：**4999**

- 一、二、三期

原价：~~17997~~ 元，优惠价：**8997** 元，打包价：**6999**

## [[超级会员] 小马哥 Java 分布式架构训练营 2023](https://mqu.xet.tech/s/3GN2Xy)
[超级会员] 原价：~~23996~~ 元，优惠价：**11996** 元，打包价：**8999，**内容包括：
> 若过去报名过任一期的训练营，购买超级会员后，小马哥将在后台退还之前的报名实际费用，相当于补差价

### 小马哥 Java 分布式架构训练营 2023

- [[第一期 Java 分布式架构 - 服务治理]](https://mqu.h5.xeknow.com/s/1I2W75a)
- [[第二期 Java 分布式架构 - 模式、设计与实现]](https://mqu.xet.tech/s/1UDiMh)
- [[第三期 分布式高并发、高性能、高可用架构]](https://mqu.xet.tech/s/9PPWG)
- [[第四期 Java 分布式架构 - 异地多活架构]](#)
### 技术专题课程

- 小马哥 SF（思否）课程（选其一）
   - Spring Boot/Spring Cloud  系列
   - 《一入 Java 深似海》系列
### 课程关联视频

- 小马哥技术周报
- 小马哥公开课
- 小马哥企业培训视频
> 视频资料：与训练营相关的视频不定期更新

### 小马哥 Java 星球（知识星球）
![8f0a76b471551735387a44c3054981fe.jpeg](https://cdn.nlark.com/yuque/0/2023/jpeg/222258/1695808934694-e49399ec-1dde-4a7a-8362-d1146d2f63fe.jpeg#averageHue=%234aceb6&clientId=ue1a87f6b-0e06-4&from=paste&height=440&id=u3a592418&originHeight=968&originWidth=750&originalType=binary&ratio=1.100000023841858&rotation=0&showTitle=false&size=113800&status=done&style=none&taskId=u6860e9a0-f2be-48a7-a9a6-3b87a7c1055&title=&width=340.9090835200855)
### 小马哥电子书籍

- Spring Boot 编程思想（运维篇）
# 课程详情
## 开营周：训练营规划和开营
### [公开课] 多活架构整体规划
#### 主要内容

- 解读第四期异地多活架构的整体规划
- 讨论第四期与一到三期之间的联系和区别
- 讨论第四期异地多活架构与第五期云平台建设的关系

### [公开课] 多活架构基础
#### 主要内容

- 软件架构变化：从单体架构、SOA、微服务、云原生到多活架构
- 多活概念：理解 Source-Repica 架构、同城灾备、同城多活、异地多活等

## 第一周：Eureka 注册中心多活架构
### 本周目标

- 掌握 Eureka Server 机群搭建
- 理解 Eureka Server Pear-To-Pear 状态复制机制以及优化实现（关联三期）
- 优化 Eureka Server 机群状态同步，支持 AP 和 CP 模式切换（关联二期）
- 了解 Eureka Availability Zones 基础
### [公开课] 第一节：Eureka Server 多活架构设计与实现
#### 主要内容

- 基于 Eureka Server Cluster Replication 机制部署多活架构
- 理解 Eureka Availability Zones 概念和设计，实现多区域 Eureka Server 部署
### 第二节：优化 Eureka Server 多活架构
#### 主要内容

- 优化 Eureka Server 高可用多活架构：基于第三期 Eureka Server JGroup 广播 服务实例状态，实现“高性能、高可用以及高并发” 多活 Eureka Server
- Eureka Server 强一致性多活架构：基于第二期 JRAFT 注册中心原型，为 Eureka Server 提供 RAFT 共识算法实现，增加 CP 模型，提升状态复制的强一致性

## 第二周：Eureka 服务注册与发现多活架构
### 本周目标

- 掌握 Eureka Client 核心特性：服务注册、发现、状态同步、心跳机制等
- 掌握 Availability Zones 在 Eureka Server 和 Client 之间的对应关系和通讯方式
- 优化 Eureka Client 服务发现方式，按需订阅服务实例
- 实现 Eureka Client 多服务注册与发现特性，提供跨域服务能力
### 第三节：Eureka Client 服务发现多活架构设计、实现与优化
#### 主要内容

- 理解 Eureka Region 概念和设计，掌握 Eureka Client 根据 Reigon 获取 Availability Zones，进一步扩展多活架构范围
- 基于 Microsphere Spring Config 框架实现动态变更 Eureka Client 配置，提升动态实时故障转移的能力
- 基于 Eureka Client 实现按需服务订阅，解决由于注册中心实例过多，导致客户端内存消耗过大的问题
### 第四节：Eureka Client 服务注册多活架构设计、实现与优化
#### 主要内容

- 基于 Eureka Client 实现多注册中心服务注册特性，提供跨域服务注册能力
- 基于 Eureka Client 实现多注册中心服务订阅特性，提供跨域服务发现能力
- 基于 Eureka Client 实现灰度/蓝绿/金丝雀等发布策略

## 第三周 通用服务注册与发现多活架构
### 本周目标

- 理解 Spring Cloud Commons 抽象服务注册和发现 API
- 理解  [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) 多活架构 Availability Zones Locator 抽象设计
- 掌握  Spring Cloud Commons 抽象服务注册和发现 API与多种主流注册中心 Client/SDK/Open API 整合，如 Eureka、Nacos、Zookeeper、consul，Kubernetes API Server、etcd 以及 istio XDS
- 基于通用服务注册与发现，实现不同类型注册中心迁移和过渡
### 第五节：Spring Cloud 服务注册与发现多活架构通用设计与实现
#### 主要内容

- 介绍 [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) 多活架构框架整体架构，理解 Availability Zones Locator 抽象设计，支持 AWS、Aliyun 等云平台，同时为分布式服务调用、分布式缓存以及数据库提供
- 基于 Availability Zones Locator 抽象实现通用服务注册与发现多活架构，适配 Netflix Eureka、Alibaba Nacos、Apache Zookeeper、consul 等注册中心以及客户端
### 第六节：Cloud-Native 服务注册与发现多活架构通用设计与实现
#### 主要内容

- 基于 Availability Zones Locator 抽象设计，适配 Kubernetes API Server 、etcd 以及 istio XDS（Envoy）注册中心
- 整合 [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) 项目，实现不同注册中心 Hybird 服务服务注册与发现
## 第四周：客户端负载均衡多活架构
### 本周目标

- 熟悉 Netflix OSS 组件，Eureka Client 与 Ribbon 针对 Availability Zones 实现同区域优先以及故障转移
- 掌握 Spring Cloud LoadBalancer API 对不同注册中心的如 Eureka、Nacos、Zookeeper、consul、Kubernetes API Server、etcd 以及 Envoy
- 理解  [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) 多活架构 Availability Zones Locator API 使用
- 了解 Microsphere Spring Config 框架提升 Availability Zones 实时切换的能力
### 第七节：Spring Cloud Netflix Ribbon 负载均衡多活架构设计与实现
#### 主要内容

- 理解 Netflix Ribbon 与 Eureka Availability Zones  官方实现同区域优先和 Zone 多活架构
- 基于 Availability Zones Locator 在 Netflix Eureka 的整合，实现 Ribbon 实现同区域优先以及 Zone 多活架构
- 基于 Availability Zones Locator 在 Kubernetes API Server 的整合，实现 Ribbon 实现同区域优先以及 Zone 多活架构

### 第八节：Spring Cloud LoadBalancer 负载均衡多活架构设计与实现
#### 主要内容

- 基于 Eureka Availability Zones 整合 Spring Cloud LoadBalancer，实现同区域优先和 Zone 多活架构
- 基于 Availability Zones Locator 在 Netflix Eureka 的整合，实现 Spring Cloud LoadBalancer 实现同区域优先以及 Zone 多活架构
- 基于 Availability Zones Locator 在 Kubernetes API Server 的整合，实现 Spring Cloud LoadBalancer 实现同区域优先以及 Zone 多活架构

## 第五周：RPC 多活架构
### 本周目标

- 熟悉 Spring RestTemplate、WebClient、HTTP Interface 和 OpenFeign 对 Netflix Ribbon 以及 Spring Cloud LoadBalancer 整合（关联一期）
- 掌握 Apache Dubbo Router SPI 整合 [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) 多活架构 Availability Zones Locator API（关联二、三期）
- 理解  [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) RPC 全链路多活架构
- 了解 Microsphere Spring Config 框架提升 Apache Dubbo 实时切换的能力
### 第九节：Spring REST Client 多活架构设计与实现
#### 主要内容

- 基于 Spring 3.0+ RestTemplate 实现通用同区域优先以及 Zone 多活架构
- 基于 Spring Cloud OpenFeign 实现通用同区域优先以及 Zone 多活架构
- 基于 Spring 5.0+ WebClient 实现通用同区域优先以及 Zone 多活架构
- 基于 Spring 6.0+ HTTP Interface 实现通用同区域优先以及 Zone 多活架构
### 第十节：Apache Dubbo 多活架构实现
#### 主要内容

- 基于[Microsphere Projects](https://github.com/microsphere-projects) 多活架构框架，结合 Apache Dubbo Router SPI 实现通用同区域优先以及 Zone 多活架构
- 整合 [Microsphere](https://github.com/microsphere-projects) Config Project 动态配置特性，提供多活路由实时动态更新能力

## 第六周：API 网关多活架构
### 本周目标

- 理解 Spring Cloud Gateway 架构和实现原理（关联一、三期）
- 掌握 Spring Cloud Gateway 动态路由实时变更特性（关联三期）
- 掌握 Spring Cloud Gateway 上游 Web Endpoints 自动发现特性（关联三期）
- 掌握 Spring Cloud Gateway 与 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构框架整合，实现同区域优先、灰度路由、全链路区域切换、故障转移等特性
### 第十一节：Spring Cloud Gateway 多活架构设计与实现
#### 主要内容

- 基于 [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) 多活架构 Availability Zones Locator API 实现 Spring Cloud Gateway 同区域优先和故障转移
- Spring Cloud Gateway 统一抽象 Availability Zone 标识，实现服务调用上下游路由规则统一
- 整合 Microsphere Spring Config 框架，为 Spring Cloud Gateway 多活架构配置与规则配置提供动态实时变更能力
### 第十二节：Spring Cloud Gateway 多活架构优化
#### 主要内容

- 根据  [Microsphere Spring Cloud](https://github.com/microsphere-projects/microsphere-spring-cloud) 多活架构 Availability Zones Locator API，实现区域化 Web Endpoints 路由规则优化，减少内存足迹，提升运行效率
- 引入 Spring Cloud Gateway 自动探测 Apache Dubbo 上游服务，集合 Dubbo 多活能力，提升网关的请求响应速度以及 gRPC 处理能力（依赖 Dubbo 3 Triple 协议）
- 统一抽象 Spring Cloud Gateway 路由规则，使其内聚区域优先、灰度路由、全链路区域切换、故障转移等特性，便于后续开发和理解
## 第七周：MySQL 多活架构
### 本周目标

- 理解 MySQL 集群部署多活架构的设计（关联二期、三期）
- 掌握 MySQL Source-Replica 同步、跨区 Source-Source 同步
- 理解 MySQL Binlog 同构和异构系统 Binlog 复制原理
- 掌握 MySQL JDBC Driver 整合 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构 Availability Zones Locator API 实现同区域优先、故障转移、负载均衡以及读写分离等特性（关联三期）
### 第十三节：MySQL Server 多活架构实现
#### 主要内容

- 回顾第二期 MySQL Source-Replica Replication 和第三期 MySQL Group Replication 架构
- 实战 MySQL Server Source-Replica 同步、跨区 Source-Source 同步
- 理解 MySQL Server Binlog 订阅机制，掌握 Java 实现，如：Alibaba Canal、Maxwell 等框架，实现异构 Database 同步，如 MySQL 到 ES、MySQL 到 Redis 等
### 第十四节：MySQL JDBC 多活架构设计与实现
#### 主要内容

- 基于 MySQL JDBC Driver 负载均衡策略整合 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构 Availability Zones Locator API，实现 Multi-Host 同区域优先以及多活架构
- 基于 MySQL JDBC Driver Replication API 整合 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构 Availability Zones Locator API ，实现故障转移，如 Source 到 Replica，或 Source 到 Source，以及读写分离
- 基于 MySQL JDBC Driver Failover API 整合 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构 Availability Zones Locator API ，实现故障恢复，自动复联等特性

## 第八周：Redis 多活架构
### 本周目标

- 了解 Redis Sentinel 高可用集群方案
- 了解 Redis Cluster Data Sharding 架构
- 掌握 Redis 集群多活架构部署和数据复制
- 理解 Redis Client 数据多区域复制方案
### 第十五节：Redis Server 多活架构
#### 主要内容

- 实战 Redis Sentinel 高可用集群部署
- 实战 Redis Cluster Data Sharding 架构
- 实战 Redis 集群以及 Redis Cluster 集群多区域数据复制
- 基于 Spring Cloud Gateway 作为 Redis Proxy，整合 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构 Availability Zones Locator API ，实现故障转移以及读写分离
### 第十六节：Redis Client 多活架构
#### 主要内容

- 基于 Microsphere Spring Redis Replicator 框架，针对 Redis 写入操作，实现分布式多区域数据复制，提升复制性能，减少 Redis Server 负载。同时，框架支持多种复制管道，如：Apache Kafka、Apache RabbitMQ 等。
- 基于 Microsphere Spring Redis 框架整合 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构 Availability Zones Locator API 实现 Redis Client 同区域优先、故障转移等多活特性

## 第九周：分布式消息多活架构
### 本周目标

- 了解 Apache Kafka 高可用集群方案
- 了解 Kafka Server 同城/异地多活架构防范
- 掌握 Kafka 网关实现 Kafka Server 多活
- 掌握 Kafka Client 增强，实现同区域优先、故障转移等多活架构
### 第十三节：Kafka Server 多活架构设计与实现
#### 主要内容

- 多区域共享单个 Apache Kafka Server 集群，单个区域对应指定的 Topic
- 多区域部署独立的 Apache Kafka Server 集群，Kafka 集群之间数据相互复制
- 基于 Spring Cloud Gateway 实现 Kafka Server Proxy，整合 [Microsphere Projects](https://github.com/microsphere-projects) 多活架构 Availability Zones Locator API 实现 Redis Client 同区域优先、故障转移等多活特性
### 第十四节：Kafka Client 多活架构设计与实现
#### 主要内容

- 针对 Kafka Server 同城多活架构，Kafka Client 根据 Availability Zones Locator 信息实现 Topic 路由
- 针对 Kafka Server 异地多活架构，Kafka Client 实现同区域优先以及故障转移
- 针对 Kafka Server Proxy 架构，Kafka Client 与 Gateway 协商，实现 Proxy 层面同区域优先以及故障转移
## 第十周：动态 Spring 应用上下文多活架构
### 本周目标

- 掌握 [Microsphere Projects](https://github.com/microsphere-projects) 动态 Spring 应用上下文实现 DataSource 同区域优先和故障转移等特性
- 理解 [Microsphere Projects](https://github.com/microsphere-projects) 动态 Spring 应用上下文插件机制实现 JDBC 组件，实现单应用 MyBatis、MyBatis-Plus 以及 JPA 等 JDBC 框架并存，并且支持独立事务
- 了解 [Microsphere Projects](https://github.com/microsphere-projects) 动态 Spring 应用上下文的设计和实现原理
### 第二十一节：动态 JDBC 组件多活架构
#### 主要内容

- 基于 [Microsphere Projects](https://github.com/microsphere-projects) 动态 Spring 应用上下文框架整合多活架构 Availability Zones Locator API，实现 MySQL DataSource 实现同区域优先、动态路由以及多活架构
- [Microsphere Projects](https://github.com/microsphere-projects) 动态 Spring 应用上下文插件机制，提供单应用 MyBatis、MyBatis-Plus 以及 JPA 等 JDBC 框架并存，并且支持独立事务管理（本地、分布式）以及分库分表（Sharding）框架支持，如 ShardingSphere
- 基于 Spring Boot Actuator 实现动态 JDBC 组件实现指标（Metrics）以及健康检查（Health Check）
### 第二十二节：动态 Spring 组件多活架构
#### 主要内容

- 基于 [Microsphere Projects](https://github.com/microsphere-projects) 动态 Spring 应用上下文框架整合多活架构 Availability Zones Locator API 实现动态 Spring 组件同区域优先、动态路由以及故障转移等特性，如：Spring Redis、Spring Kafka 和 ElasticSearch 等
- 理解 [Microsphere Projects](https://github.com/microsphere-projects) 动态 Spring 应用上下文框架实现原理，掌握动态组件插件机制
## 学员须知

- 实际课程安排可能与大纲小有出入，请以最终上课内容为主
- 学员全课程学习完后会有结业证书
