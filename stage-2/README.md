# 报名信息
[[点击报名]](https://mqu.xet.tech/s/1UDiMh)
## 课程福利
为感谢长期对小马哥信任和支持的小伙伴，曾报名以下任一课程： 

- SF（思否）
   - Spring Boot/Spring Cloud  系列
   - 《一入 Java 深似海》系列
- 极客时间
   - Spring 编程思想（核心 + AOP）
   - 小马哥 Java 训练营 0 期
   - 小马哥 Java 训练营 1 期

报名本系列后，可返现 100 元，若报名多个系列，返现可叠加，最高返现 500元。
若小伙伴极客时间的课程全报名过，可选择返现 300 元或免费获取 SF（思否）全套课程。
由于人数较多，请小伙伴在第三节课后，找小马哥，告知报名信息，返现将从支付渠道返回。
# 课程详情
## 第二期 课程规划
## 第二期 开营直播
## 第一周：分布式理论基础
### 本周目标

- 理解 CAP 与 BASE 理论知识
- 掌握一致性模型的分类和特征
- 理解 Paxos 算法
### [第一节：CAP 与 BASE 理论](https://mqu.h5.xeknow.com/sl/ctsmf)
#### 主要内容

- CAP 理论：掌握 CAP 等概念，理解为什么 CAP 很难同时满足
- CAP 技术选型：掌握目前市场流行的 CAP 基础设施实现，如 Eureka、Zookeeper、Nacos、Consul、ectd 等
- BASE 理论：理解基本可用（Basically Available）、软状态（Soft State）和最终一致性（Eventually Consistent）等
- 一致性模型：理解强一致性、弱一致性、最终一致性、因果一致性等

### [第二节：分布式共识算法 - Paxos ](https://mqu.h5.xeknow.com/sl/3IGy9a)
#### 主要内容

- Paxos 算法：了解 Paxos 引入的背景、历史和发展
- Quorum：理解 Quorum 定义、机制以及应用
- 工作流程：理解 Paxos 角色之间交互和工作流程

## 第二周：分布式共识算法实现
### 本周目标

- 理解 复制日志算法 - RAFT
- 掌握原子广播算法  - ZAB
- 了解 SOFAJRaft 的使用场景、工作原理以及 API 运用
### [第三节：复制日志算法 - RAFT](https://mqu.h5.xeknow.com/sl/veIbD)
#### 主要内容

- Raft 理论：理解 Raft Leader选举、日志复制、一致性状态机等核心概念和工作流程
- SOFAJRaft 简介：了解 SOFAJRaft 的历史、功能以及局限性
- SOFAJRaft 核心引擎设计：讨论 JRaft Node、存储、状态机以及复制等核心组件
- SOFAJRaft 架构与 API：掌握 SOFAJRaft 算法、算法以及 API 之间的联系和交互，熟练掌握其中 API

### [第四节：原子广播算法  - ZAB](https://mqu.h5.xeknow.com/sl/2F3Tq1)
#### 主要内容

- ZAB 算法介绍：了解 ZAB 算法的使用场景以及它与 Paxos 的区别
- ZAB 四个阶段：掌握选举、发现、同步和广播这四个阶段
- ZAB 算法细节：掌握 ZAB 1.0 协议在各个阶段时的处理过程

## 第三周：分布式共识算法运用
### 本周目标

- 理解 Zookeeper ZAB 协议
### [第五节：Alibaba Nacos 2.x 在共识算法中的运用](https://mqu.h5.xeknow.com/sl/22EHuF)
#### 主要内容

- Nacos 2.0 一致性协议：讨论 SOFAJRaft 在 Nacos 2.0 一致性 CP 协议上的运用
- Nacos 自研协议 Distro
### [第六节：Apache Zookeeper 在共识算法中的运用](https://mqu.h5.xeknow.com/sl/lOrSE)
#### 主要内容

- 服务注册与发现：使用 Zookeeper 作为服务注册中心
- 配置管理：使用 Zookeeper 数据发布/订阅的特性，实现实时性配置推送和变更
- 分布式锁：利用 Zookeeper 的特性实现强一致性分布式锁

## 第四周：Zookeeper 核心技术
### 本周目标

- 掌握 Apache Zookeeper 技术细节
- 学会利用 Zookeeper 特性设计合理的使用场景
### [第七节：Zookeeper 数据模型与通讯](https://mqu.h5.xeknow.com/sl/4vUMmP)
#### 主要内容

- 数据模型：理解 Zookeeper 树形结构、节点类型、版本控制等
- 数据变更：理解 Watcher 机制，掌握通知状态和事件类型
- 访问控制：了解 Zookeeper ACL 控制机制，理解模式、授权对象以及权限等要素
- 序列化：了解 Zookeeper Jute 序列化
- 通讯协议：掌握 Zookeeper 网络请求和响应通讯协议设计
### [第八节：Zookeeper 会话与选举](https://mqu.h5.xeknow.com/sl/2lMUaj)
#### 主要内容

- 客户端：理解 Zookeeper 实例、ClientWatchManager、HostProvider 以及 ClientCnxn API
- 会话（Session）：理解会话状态、会话创建、管理
- Leader 选举：掌握 Zookeeper 集群服务器启动以及 Leader 选举等环节交互过程

## 第五周：数据库事务基础
### 本周目标

- 理解传统数据库事务特性、隔离级别
- 掌握 EJB / Spring 本地事务管理原理和实现，以及与 JDBC 规范的关系
- 了解 MySQL 数据库事务实现原理
### [第九节：本地数据库事务](https://mqu.h5.xeknow.com/sl/27HH76)
#### 主要内容

- 事务特性：理解数据库事务 ACID 特性，原子性、一致性、隔离性以及持久性
- 事务隔离级别：介绍 JDBC 兼容数据库事务隔离级别，并讨论 MySQL 事务隔离级别的特点以及实现原理
- MySQL 事务实现原理：理解 Redo Log、Undo Log 以及 Bin Log 基本原理和存储机制等，并讨论 MySQL 事务的执行流程。
### [第十节：Java EE 本地事务管理原理和实现](https://mqu.h5.xeknow.com/sl/1UPVJ1)
#### 主要内容

- EJB 事务传播机制：了解 Java EE EJB 规范中的事务传播机制，并理解事务传播、与 JDBC Savepoing 以及线程模型之间的关系
- Spring 本地事务实现原理：掌握 Spring 事务接口 PlatformTransactionManager、TransactionDefinition 以及 TransactionStatus 接口的区别和联系，理解 Spring TX 是如何实现事务传播的逻辑

## 第六周：分布式事务 Java EE 解决方案
### 本周目标

- 理解分布式事务引入的背景
- 理解强一致性事务 XA 使用场景
- 掌握 Java EE 分布式事务解决方案
### [第十一节：JTA 和 XA 原理与实现实现](https://mqu.h5.xeknow.com/sl/1w98iy)
#### 主要内容

- JTA 架构介绍：理解 JTA 架构已经核心 API，通过对比分析掌握其与 Spring 事务抽象的联系和区别
- XA 规范介绍：了解分布式事务标准 XA，并讨论 MySQL 5 对 XA 的支持情况
- JTA/XA 实现：掌握 JTA/XA 分布式事务实现框架 Atomikos，并且完成处理分布数数据库数据

### [第十二节：Java 分布式事务整合](https://mqu.h5.xeknow.com/sl/4rRg4m)
#### 主要内容

- JTA 与 Spring  TX 整合：理解 Spring TX 事务抽象，掌握 JtaTransactionManager 的实现细节
- JTA 与 ShardingSphere 整合：掌握 ShardingSphere 对 JTA 的封装，以及对 Atomikos 的支持

## 中期回顾 
### 第二期 中期回顾（上）- 四到六周
### 第二期 中期回顾（下）- 一到三周
## 第七周：分布式事务常规解决方案
### 本周目标

- 掌握最终一致性分布式事务解决方案
- 理解 TCC 解决方案
- 理解基于可靠事件（消息）队列的分布式事务原理
### [第十三节：可靠事件队列分布式事务原理和实现](https://mqu.h5.xeknow.com/sl/2ITXIm)
#### 主要内容

- 可靠事件队列：理解基于事件（消息）基础设施实现分布式事务的数据最终一致性
- 可靠事件队列实现：实现消息数据自动装配逻辑、致力于业务应用依赖组件即用的目的

### [第十四节：TCC(Try-Confirm-Cancel) 分布式事务原理和实现](https://mqu.h5.xeknow.com/sl/2U2zOf)
#### 主要内容

- TCC 原理：理解 分布式事务各个阶段的执行过程以及原理
- TCC 实现：基于 Java 注解实现业务无感的 TCC 分布式事务操作

## 第八周：分布式事务开源解决方案
### 本周目标

- 了解业界相对成熟的分布式事务开源解决方案
- 掌握 Alibaba Seata 在分布式事务场景中的使用
- 理解 Alibaba Seata 技术内幕
### [第十五节：Alibaba Seata 分布式事务开发实战](https://mqu.h5.xeknow.com/sl/407TRV)
#### 主要内容

- AT 模式开发：结合 Dubbo、Nacos 分布式 RPC 调用场景演示 分布式事务在 Seata AT 模式下的工作情况
- TCC 模式开发：基于 Seata TCC  API 实现账户转账的业务
### [第十六节：Alibaba Seata 架构和原理](https://mqu.h5.xeknow.com/sl/46Hbnr)
#### 主要内容

- Seata 简介：介绍 Seata 的发展历、整体架构以及支持的事务模式
- Seata AT 模式：理解 AT 的工作原理、事务日志、资源管理器以及两阶段提交实现
- Seata TCC 模式：比较 AT 模式，掌握 TCC 模式的实现原理
- Seata RPC 设计：掌握 Seata RPC 网络通讯、消息类型以及序列化细节
- Seata 事务协调器：理解 Seata 事务消息处理的细节

## 第九周：分布式服务
### 本周目标

- 掌握 Netty 网络编程基础
- 了解 RPC 框架设计模式
- 掌握分布式服务通用特性
### [第十七节：RPC 微内核设计](https://mqu.h5.xeknow.com/sl/2PnSpj)
#### 主要内容

- 服务通讯：基于 Netty 实现客户端和服务端之间的服务通讯
- 消息序列化：理解并使用目前业界成熟的序列化协议，如 Hession、Kyro、JSON 等
- 消息协议：设计请求和响应消息通讯协议，并支持序列化以及其他特性扩展
- 负载均衡：设计负载均衡接口，并内建通用的负载均衡算法
- 服务路由：设计服务消息路由接口，提升业务定义路由能力
### [第十八节：RPC 生态整合](https://mqu.h5.xeknow.com/sl/3XyMWa)
#### 主要内容

- 服务注册与发现：基于 SOFTJRAFT 实现 CP 注册中心和服务发现客户端
- 分布式事务支持：提供分布式事务扩展接口，整合分布式事务框架
## 第十周：分布式配置
### 本周目标

- 掌握分布式配置服务端和客户端的设计与实现
- 理解共识算法分布式配置中的运用
- 了解 Apollo 配置中心的架构和设计
### [第十九节：分布式配置中心设计](https://mqu.h5.xeknow.com/sl/1d7JUo)
#### 主要内容

- 配置存储：理解配置中心中心化存储和分布式存储的优劣，并且理解共识算法的作用
- 配置获取：理解配置中心长链接和短链接的实现差异，并掌握场景选型
- 集群部署：理解配置中心使用 RAFT 和 Gossip 协议实现集群部署
- Open API 设计：掌握配置中心 API 设计，包括配合以及认证和授权
### [第二十节：分布式配置客户端设计](https://mqu.h5.xeknow.com/sl/1VpMwp)
#### 主要内容

- 配置操作：理解配置客户端通过 Open API 操作配置的设计
- 生态整合：将配置客户端 API 整合业界成熟的框架，如 Spring 配置、MicroProfile 配置等
- 配置验证：实现客户端与服务端的配置版本控制，提供合法性校验等手段，确保客户端配置是合法有效的
## 第十一周：分布式数据存储
### 本周目标

- 掌握数据库读写分离在业务上实践
- 掌握数据库分库分表框架在业务上的整合
- 掌握 ShardingSphere 在数据分片以及读写分离上的运用
### [第二十一节：通用数据读写分离设计](https://mqu.h5.xeknow.com/sl/1rKzI1)
#### 主要内容

- MySQL 主从复制：掌握 MySQL 主从复制技术，并且根据业务场景有选择性的进行读写分离
- DB 读写分离数据源：设计与实现动态数据库 读写分离数据源
- Redis 读写分离数据源：设计与实现 Redis 读写分离数据源
### [第二十二节：基于 ShardingSphere 实现数据分片和读写分离](https://mqu.h5.xeknow.com/sl/1gkIcN)
#### 主要内容

- 数据分片：使用 ShardingSphere 实现分库、分表、分库+分表以及强制路由
- 读写分离：使用 ShardingSphere 实现数据读写分离
- 底层原理（加餐）：讨论 ShardingSphere 底层实现原理
## 第十二周：分布式缓存
### 本周目标

- 理解分布式缓存的基本原理
- 掌握多级缓存的设计和实现
- 了解分布式缓存的数据集群存储特点
### [第二十三节：分布式缓存设计](https://mqu.h5.xeknow.com/sl/2edBGU)
#### 主要内容

- 缓存基础理论：理解缓存吞吐量、命中率、淘汰策略，以及扩展特性等
- 客户端缓存设计：掌握客户端缓存头、Last-Modified 和 If-Modified-Since 等前端缓存手段
- 服务端缓存设计：理解本地和分布式服务端缓存，掌握多级缓存的设计手段
- 分布式缓存：掌握集中式缓存和副本式缓存的优劣，了解内存网格型缓存 infinispan
### [第二十四节：分布式缓存实战](https://mqu.h5.xeknow.com/sl/2A67pI)
#### 主要内容

- 限流场景：基于分布式缓存实现 RateLimiter
- 分布式锁：基于 Redis 实现分布式锁
- 分布式 Session：基于 Redis Hash 实现分布式 Session
- 幂等性服务：基于 分布式 Session 实现服务幂等性

## 第二期 结营
### 第三期规划
### 第二期 结营直播
