<a name="kgJlp"></a>
### 问题描述
线程数量占用超级高，导致服务请求超级慢，又因为这个服务没有加熔断降级，流量还一直进。node主机直接把java 进程给kill 掉了。

<a name="xb7IE"></a>
#### 线程 Dump
```latex
"SimpleAsyncTaskExecutor-149252" #157740 prio=5 os_prio=0 tid=0x00007f608808d000 nid=0x6e56 waiting on condition [0x00007f5fe3bb4000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x00000006c0d67ab0> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
	at ch.qos.logback.core.OutputStreamAppender.writeBytes(OutputStreamAppender.java:197)
	at ch.qos.logback.core.OutputStreamAppender.subAppend(OutputStreamAppender.java:231)
	at ch.qos.logback.core.rolling.RollingFileAppender.subAppend(RollingFileAppender.java:235)
	at ch.qos.logback.core.OutputStreamAppender.append(OutputStreamAppender.java:102)
	at ch.qos.logback.core.UnsynchronizedAppenderBase.doAppend(UnsynchronizedAppenderBase.java:84)
	at ch.qos.logback.core.spi.AppenderAttachableImpl.appendLoopOnAppenders(AppenderAttachableImpl.java:51)
	at ch.qos.logback.classic.Logger.appendLoopOnAppenders(Logger.java:270)
	at ch.qos.logback.classic.Logger.callAppenders(Logger.java:257)
	at ch.qos.logback.classic.Logger.buildLoggingEventAndAppend(Logger.java:421)
	at ch.qos.logback.classic.Logger.filterAndLog_0_Or3Plus(Logger.java:383)
	at ch.qos.logback.classic.Logger.debug(Logger.java:494)
```

<a name="pVI2A"></a>
#### 线程统计
![409775ecceb71cdc77d4d933188f3356_.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1692366703591-bfcd8d86-396f-4cbe-a9cc-bb38f535d1fe.png#averageHue=%2310100f&clientId=u2cfa1414-66ae-4&from=paste&height=54&id=uc897b12d&originHeight=67&originWidth=801&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=18265&status=done&style=none&taskId=u62c7d8b5-935b-4174-bc9d-28bd5c5369c&title=&width=640.8)

![833a7a2a9cfb494a595d373fb8f3be09_.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1692366721790-4b39d0ed-b8c1-4da8-84dd-9eaf2c524360.png#averageHue=%23e0ddda&clientId=u2cfa1414-66ae-4&from=paste&height=190&id=uf946682e&originHeight=238&originWidth=943&originalType=binary&ratio=1.25&rotation=0&showTitle=false&size=62400&status=done&style=none&taskId=ub7c4a29a-b64d-4fc4-bf0d-0f66d4626a2&title=&width=754.4)

<a name="ShoCc"></a>
#### Logback 配置
```xml
<root>
  <level value="INFO" />
  <appender-ref ref="infoAppender" />
  <appender-ref ref="errorAppender" />
  <appender-ref ref="console" />
</root>
```

<a name="Ezr5z"></a>
#### 初步诊断

1. 当前应用使用到了 Spring 非池化 TaskExecutor 实现，即 SimpleAsyncTaskExecutor
   1. 可能是由于不正确使用 @Async 导致
      1. Spring Cloud Slueth + Skywalking + Spring @Async

<a name="AyM9V"></a>
#### 修改建议

1. 可以增加池化 Spring TaskExecutor Bean 到当前应用
   1. 推荐使用：ThreadPoolTaskExecutor 或 ThreadPoolTaskScheduler
      1. 使用 Spring Bean 生命周期管理 J.U.C 线程池启停
