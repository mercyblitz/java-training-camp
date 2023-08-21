<a name="DPaM7"></a>
### 机器情况
![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692252842126-b03337e0-4ee0-4008-a1bb-bcc133517391.png#averageHue=%231a1a1a&clientId=u336e93ac-7800-4&from=paste&height=149&id=u5c239e13&originHeight=149&originWidth=722&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40993&status=done&style=none&taskId=udebe0f95-121c-42bf-9d45-dc4ad7b8ff9&title=&width=722)<br />启动参数 :  -Xms4700m -Xmx4700m
<a name="W2XFv"></a>
### 简单监控图
![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692243239633-5772ce33-29ec-4132-9e1a-ede5dc91476a.png#averageHue=%23e9e9e9&clientId=u2a4ef6e0-6bc0-4&from=paste&height=1071&id=u774076be&originHeight=1071&originWidth=1342&originalType=binary&ratio=1&rotation=0&showTitle=false&size=207138&status=done&style=none&taskId=ubb8a725c-249e-47ce-a66e-2bd57156d30&title=&width=1342)

![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692243269513-ae416e42-4da6-46e9-a2ed-d441e5bea78d.png#averageHue=%23d1d1d1&clientId=u2a4ef6e0-6bc0-4&from=paste&height=299&id=u00106147&originHeight=299&originWidth=1139&originalType=binary&ratio=1&rotation=0&showTitle=false&size=65832&status=done&style=none&taskId=u55270fe2-7e44-4a39-8d87-a8bba7f3c0d&title=&width=1139)

![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692243291465-00b9d18c-f5c4-4767-b8c2-ad026de83e5e.png#averageHue=%23e7e7e6&clientId=u2a4ef6e0-6bc0-4&from=paste&height=741&id=u34642dd4&originHeight=741&originWidth=1334&originalType=binary&ratio=1&rotation=0&showTitle=false&size=162067&status=done&style=none&taskId=ub504f7b7-1bec-44f2-b547-240a6e76e11&title=&width=1334)

![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692243648212-ebbcd9ef-d24f-4cdf-b93f-4e5d7d209b1c.png#averageHue=%232c2c2c&clientId=u128b0bcc-3579-4&from=paste&height=232&id=EjJPO&originHeight=232&originWidth=775&originalType=binary&ratio=1&rotation=0&showTitle=false&size=94030&status=done&style=none&taskId=u3783bbd2-5083-458d-8561-4b238d9dda9&title=&width=775)
<a name="df0i4"></a>
### jstack 分析

[jstack0727.txt](https://www.yuque.com/attachments/yuque/0/2023/txt/222258/1692259897529-e073ebf7-b7b3-4742-82be-a7f2fba55367.txt?_lake_card=%7B%22src%22%3A%22https%3A%2F%2Fwww.yuque.com%2Fattachments%2Fyuque%2F0%2F2023%2Ftxt%2F222258%2F1692259897529-e073ebf7-b7b3-4742-82be-a7f2fba55367.txt%22%2C%22name%22%3A%22jstack0727.txt%22%2C%22size%22%3A1378256%2C%22ext%22%3A%22txt%22%2C%22source%22%3A%22%22%2C%22status%22%3A%22done%22%2C%22download%22%3Atrue%2C%22taskId%22%3A%22u19b946f3-34aa-4862-b8ce-a6458cdd5e3%22%2C%22taskType%22%3A%22transfer%22%2C%22type%22%3A%22text%2Fplain%22%2C%22mode%22%3A%22title%22%2C%22id%22%3A%22u8eb16829%22%2C%22card%22%3A%22file%22%7D)   7月27号jstack文件
<a name="EsDeY"></a>
#### fastthread.io 分析 
[https://fastthread.io/ft-thread-report.jsp?dumpId=1&oTxnId_value=dd91aa40-8056-4ec5-a914-22c4778dc16c](https://fastthread.io/ft-thread-report.jsp?dumpId=1&oTxnId_value=dd91aa40-8056-4ec5-a914-22c4778dc16c)<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692252662900-1af8c157-d3cc-4373-8018-e9a9729af90c.png#averageHue=%2380c48c&clientId=u128b0bcc-3579-4&from=paste&height=1797&id=u96be289d&originHeight=1797&originWidth=1618&originalType=binary&ratio=1&rotation=0&showTitle=false&size=393218&status=done&style=none&taskId=u65eb4719-e6c8-401f-a11d-3bc34f2f379&title=&width=1618)

有较多未命名的线程.  但项目中搜了使用线程的地方都加了命名<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692252730083-47ca2421-2b79-4135-b0ea-d287870d696e.png#averageHue=%2380807f&clientId=u128b0bcc-3579-4&from=paste&height=704&id=uf159014f&originHeight=704&originWidth=862&originalType=binary&ratio=1&rotation=0&showTitle=false&size=220612&status=done&style=none&taskId=u0c6804cc-c043-4722-870b-b600261ae4a&title=&width=862)

<a name="Ca1X1"></a>
#### 初步诊断

1. 应用存在大量的 ThreadPoolExecutor 以及派生类对象生成，可能的情况：
   1. 预初始化（启动）核心（core）线程（数量：1），并且没有任务来执行
   2. 已经执行过一次任务，再也没有新任务执行
      1. 可能造成的原因：
         1. ThreadPoolExecutor 由于某种缓存机制失效，导致了 computeIfAbsent
         2. ThreadPoolExecutor 没有合理 Shutdown，比如在方法内部创建 ThreadPoolExecutor，示例代码如下：
```java
public void sum() {
    ThreadPoolExecutor executor  = Executors.newFixedThreadPool(1);
	for(int i=0;i<10;i++){
    	executor.submit(...);
    }
    // core thread 一直在执行，不过没有任务罢了，当下 Keep-Live = 0 ，永久等待
    // core thread Thread.State == Thread.State.WAITING
    // core thread 是否是 ThreadPoolExecutor 成员
    // ThreadPoolExecutor -> workers(HashSet<Worker>) -> Worker -> Thread
    // executor.shutdown();
}
```

<a name="UF0yJ"></a>
#### 修改建议

1. 排查问题代码，并且及时关闭 ThreadPoolExecutor
2. ThreadPoolExecutor 采用单例共享
3. ~~如果需要这么多 ThreadPoolExecutor，那么合理地设置 Keep-ALive = 60s~~

<a name="Ga0fu"></a>
### OOM KILLER 日志
grep "Out of memory" /var/log/messages<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692243425611-dbab2be4-d5aa-4200-88d0-45a6534e2828.png#averageHue=%23252525&clientId=u128b0bcc-3579-4&from=paste&height=179&id=uaf4695c7&originHeight=179&originWidth=1526&originalType=binary&ratio=1&rotation=0&showTitle=false&size=147798&status=done&style=none&taskId=u3a206216-b99a-4544-ae91-bd5c16ba174&title=&width=1526)



ps -A -ostat,ppid,pid,cmd | grep -e '^[Zz]'  查看僵尸进程    ->  无<br />arthas -> vmtool --action forceGc   强制full gc,  并不能减少进程的内存占用

目前线上未开启 VM.native_memory

---



<a name="UZ374"></a>
### 其他
![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692256007677-032a4df5-01ec-403b-a146-6742ba2541b1.png#averageHue=%23acd257&clientId=uc7dbb0fa-c268-4&from=paste&height=384&id=mjF11&originHeight=384&originWidth=618&originalType=binary&ratio=1&rotation=0&showTitle=false&size=61290&status=done&style=none&taskId=ua77c1e6d-0bff-4248-804d-9edaae893d2&title=&width=618)

![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692243648212-ebbcd9ef-d24f-4cdf-b93f-4e5d7d209b1c.png#averageHue=%232c2c2c&clientId=u128b0bcc-3579-4&from=paste&height=232&id=Nn44m&originHeight=232&originWidth=775&originalType=binary&ratio=1&rotation=0&showTitle=false&size=94030&status=done&style=none&taskId=u3783bbd2-5083-458d-8561-4b238d9dda9&title=&width=775)<br />操作系统对内存的分配管理典型地分为两个阶段：保留（reserve）和提交（commit）。保留阶段告知系统从某一地址开始到后面的dwSize大小的连续虚拟内存需要供程序使用，进程其他分配内存的操作不得使用这段内存；提交阶段将虚拟地址映射到对应的真实物理内存中，这样这块内存就可以正常使用[1]。

<a name="vmiN4"></a>
### top 与 jvm实际占用内存分析
![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692257310131-492bfd83-ce54-465d-b385-0e55aaddcb67.png#averageHue=%2381807f&clientId=u51183b6f-3785-4&from=paste&height=466&id=u6defc24f&originHeight=466&originWidth=877&originalType=binary&ratio=1&rotation=0&showTitle=false&size=127539&status=done&style=none&taskId=u16c5ecce-1886-4ddb-b32e-bfb61d40e7e&title=&width=877)<br />比较结果发现<br />实际600MB > （jvm堆内+堆外）319MB<br />但是有一点是可以知道的，就是这些600M 内存的确被jvm所属进程使用了。

<a name="D0mLC"></a>
#### 使用  jcmd pid VM.native_memory summary
-XX:NativeMemoryTracking=summary<br />jcmd pid VM.native_memory summary 查看jvm 真实占用的内存分布情况

committed就是实际使用的内存<br />所以一个java进程的内存占用：

1. heap:堆内存，即-Xmx限制的最大堆大小的内存。
2. class：加载的类与方法信息，其实就是 metaSpace，包含两部分：
   1. 一是metadata，被-XX:MaxMetaspaceSize限制最大大小，
   2. 二是classSpace，被-XX:CompressedClassSpaceSize限制最大大小
3. **thread：线程与线程栈占用内存，每个线程栈占用大小受-Xss限制，但是总大小没有限制**
4. code:JIT 即时编译后（C1 C2 编译器优化）的代码占用内存，受-XX:ReservedCodeCacheSize限制
5. gc：垃圾回收占用内存，例如垃圾回收需要的 CardTable，标记数，区域划分记录，还有标记 GC Root等等，都需要内存。这个不受限制，一般不会很大的。Parallel GC 不会占什么内存，G1 最多会占堆内存 10%左右额外内存，ZGC 会最多会占堆内存 15~20%左右额外内存，但是这些都在不断优化。（注意，不是占用堆的内存，而是大小和堆内存里面对象占用情况相关）
6. compiler:C1 C2 编译器本身的代码和标记占用的内存，这个不受限制，一般不会很大的
7. internal：命令行解析，JVMTI 使用的内存，这个不受限制，一般不会很大的
8. symbol：常量池占用的大小，字符串常量池受-XX:StringTableSize个数限制，总内存大小不受限制
9. Native Memory Tracking：内存采集本身占用的内存大小，如果没有打开采集（那就看不到这个了）
10. Arena Chunk：所有通过 arena 方式分配的内存，这个不受限制，一般不会很大的

由下图可以看到 600M实际的占用分布 , total的 600M也基本符合<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/22532014/1692258119493-f27378ea-44d0-4467-affa-678e0b49fbb8.png#averageHue=%2310204a&clientId=u1b7dc335-1ebc-4&from=paste&height=725&id=u4805eb11&originHeight=725&originWidth=865&originalType=binary&ratio=1&rotation=0&showTitle=false&size=224895&status=done&style=none&taskId=ub8a20032-895f-48dc-b6ff-79feea350eb&title=&width=865)


