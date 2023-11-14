> <a name="rATcf"></a>
### 主要议程
> - 阿里云故障
> - 本期回顾
> - 问题讨论

<a name="lTUSq"></a>
# 2023.11.12 阿里云大面积服务事故
<a name="TLGxt"></a>
## 亲生经历
<a name="brpMQ"></a>
### 语雀文档不可用
2023.11.12 下午本人在使用语雀文档时遇到故障，发现仅有文档不可用，其他功能正常：
<a name="t8727"></a>
#### 文档编辑
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699876975189-40fb7d60-88ed-4f54-aa32-3cf5b5f8bd8e.png#averageHue=%23fdfdfd&clientId=u939ca230-85bf-4&from=paste&height=934&id=JTNdQ&originHeight=934&originWidth=2126&originalType=binary&ratio=1&rotation=0&showTitle=false&size=141467&status=done&style=none&taskId=u6b5457ba-28a5-429e-bca3-2c4d582db35&title=&width=2126)
<a name="iLs0d"></a>
#### 目录服务
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699876955760-6dcb5cc7-80d3-4f14-961e-2be4ee99226d.png#averageHue=%23fcfcfc&clientId=u939ca230-85bf-4&from=paste&height=982&id=ue8154ea1&originHeight=982&originWidth=2840&originalType=binary&ratio=1&rotation=0&showTitle=false&size=256006&status=done&style=none&taskId=u31a192c3-643e-4afe-b9b2-7078f1c5f41&title=&width=2840)
<a name="Q0GE1"></a>
#### 逛逛
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699876992329-2679090e-e28d-464b-a559-b590349ff30d.png#averageHue=%23fbfbfb&clientId=u939ca230-85bf-4&from=paste&height=1052&id=u17f36ddb&originHeight=1052&originWidth=2876&originalType=binary&ratio=1&rotation=0&showTitle=false&size=586247&status=done&style=none&taskId=u46335386-aae4-4fff-ae60-981bd3fee5e&title=&width=2876)
<a name="wHGzM"></a>
#### 帮助
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699877033670-9850db69-d918-41b4-8221-e2ec84661e3f.png#averageHue=%23fbfaf9&clientId=u939ca230-85bf-4&from=paste&height=1386&id=u831083b7&originHeight=1386&originWidth=2872&originalType=binary&ratio=1&rotation=0&showTitle=false&size=840868&status=done&style=none&taskId=u5aa19842-3f5a-4cfe-aa10-e23a0a86538&title=&width=2872)
<a name="lxzpW"></a>
#### 问题反馈
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699877058094-061ef6cc-1cfb-4617-96de-cc17408c8a52.png#averageHue=%23fdfdfd&clientId=u939ca230-85bf-4&from=paste&height=1578&id=ue6571869&originHeight=1578&originWidth=2578&originalType=binary&ratio=1&rotation=0&showTitle=false&size=482101&status=done&style=none&taskId=u234875af-42be-43df-a436-e2734396596&title=&width=2578)
<a name="pD9YW"></a>
## 问题分析
由于语雀大部分工作能工作，仅有文档有误，并且提出 OSS 错误：<br />![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699876904627-e32d4eaf-864c-4b92-89a0-d45ff3f1d1e0.png#averageHue=%23f7f7f7&clientId=u939ca230-85bf-4&from=paste&height=788&id=qpIgZ&originHeight=788&originWidth=2236&originalType=binary&ratio=1&rotation=0&showTitle=false&size=337652&status=done&style=none&taskId=u4a6996c9-e6f8-4ef8-9db9-3a4bf1b89b3&title=&width=2236)<br />怀疑**阿里云出现故障**。
<a name="oqwc3"></a>
## 网络消息
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699876723104-1753e4e0-9d46-41ed-9412-f935aed41cc1.png#averageHue=%23eae7e3&clientId=u939ca230-85bf-4&from=paste&height=733&id=u5b9e86cb&originHeight=733&originWidth=687&originalType=binary&ratio=1&rotation=0&showTitle=false&size=104603&status=done&style=none&taskId=u57f8aaef-e794-4e89-8445-86969de342a&title=&width=687)
<a name="LpAAX"></a>
## 网络文章
<a name="iuagu"></a>
### 《从互联网35岁失业现象看阿里系产品全线崩溃的必然性》
> 第一次见阿里云出这么大的事故，全球性的大故障，阿里系多款产品崩溃，如阿里云、钉钉、阿里云盘、淘宝、闲鱼、饿了么、天猫精灵、千牛等重要产品几乎都崩了。 这次受影响的应用非常广泛，涉及到上百种产品，例如数据库、日志存储、OSS、VPN网关、负载均衡等等。如下图所示，密密麻麻一片全是受影响的产品。 包括IOT（物联网）凡是使用阿里云IOT的服务的厂家也都崩了，比如大学里的自助洗衣机、饮水机、吹风机等等。
> 虽然阿里系产品第一次全面崩溃，实际上阿里系的作风让全面崩溃必然发生。
> 阿里的汇报风格想必大家都有了解，互联网上有大量令人啼笑皆非的阿里体，什么问题的抓手，重建创新文化啊，一堆务虚的东西，跟领导开会强调解决问题的关键是要找到关键问题，是不是有的一拼？
> 体制僵化是阿里系产品全线崩溃的一个原因，最主要的的问题是，互联网公司普遍存在的35岁失业现象。
> 中国的互联网公司，已经发展到垄断的程度，不需要有新的技术进步，仅仅靠业务模式的创新就可以获取大量的利润。在这种情形下，高额的研发费用成为老板眼中必须要砍掉，能增厚利润的地方。研发费用大头就是程序员的工资，老员工的工资更高。互联网公司偏向于裁掉大龄的员工。
> 而技术的累积需要长期的工作经验。去医院看病时大家会觉得50多的主任医师比30出头才工作没几年的主治医师靠谱，导致互联网行业怎么反而会留下年轻人，裁掉老员工呢？
> 本质原因是，现在互联网不需要技术上的革新，不需要研发，只需要垄断。如果一个公司来招聘，说他们公司平均年龄30岁以下，你可以把这个公司和没有技术画上等号。
> 计算机领域的创新，绝大多数发生在美国，而且大多数创新是大龄程序员的创造。java之父詹姆斯·高斯林39岁发明了java，54岁的linus之父还在review linux kernel代码，并且他在36岁时还发明了目前广泛使用的git.
> 互联网行业35岁危机和阿里巴巴的企业文化结合在一起导致这次严重的危机。技术男，技术女的典型特征是不善于沟通，他们极其重要的工作成果在滥竽充数的表演者叙述下变成了别人的功劳，干活干的好不如PPT写的好，PPT写的好，不如讲得好。在这种文化下，真正有技术的研发人员不断被排挤走。
> $阿里巴巴(BABA)$ $阿里巴巴-SW(09988)$ $腾讯控股(00700)$
> 造成互联网行业乃至整个计算机行业35岁失业现象的根本原因是，我们不需要创造技术。在国外技术大神捣鼓了一个新的框架后，我们拿来用就行了，国内的程序员经常被嘲笑只会ctrl+c 和ctrl+v，创新研发有一定概率失败，公司为了赚更多的钱，肯定倾向于用成熟的东西，像互联网行业，发展到现在已经能实现外包化。app找外包搞一下，服务器架设给卖服务器厂商搞搞，数据库由于原先已经搭建好，养一些运维缝缝补补就能用。初期利润会比较好看，但风险会不断累积，某一天可能就崩掉了。

<a name="qK2AT"></a>
## 关联故障
<a name="Ra8B3"></a>
### 2023.10.23 语雀超长时间宕机故障
<a name="qQYRp"></a>
# 本期回顾
<a name="KwNef"></a>
## [第三期 Java 分布式架构 - 高并发、高性能与高可用](https://github.com/mercyblitz/java-training-camp/tree/main/stage-3)
<a name="PTRhb"></a>
## 作业安排
[https://github.com/mercyblitz/java-training-camp/issues](https://github.com/mercyblitz/java-training-camp/issues)
<a name="nYMYK"></a>
## 简历优化
<a name="Uc0yk"></a>
## 模拟面试
<a name="qT2d0"></a>
## 一对一指导
<a name="vMdiU"></a>
## 问题答疑
![image.png](https://cdn.nlark.com/yuque/0/2023/png/222258/1699877910804-aea74df5-bce6-483c-a1da-e48621204d5f.png#averageHue=%234aceb6&clientId=u00355ebb-3a25-4&from=paste&height=727&id=u8b3a1c66&originHeight=968&originWidth=750&originalType=binary&ratio=1&rotation=0&showTitle=false&size=264406&status=done&style=none&taskId=u028e58d3-7425-4c7f-9970-e7d1f596982&title=&width=563)



<a name="fBpVJ"></a>
# 问题讨论
<a name="TFPgf"></a>
## 训练营内部讨论
<a name="NsHpj"></a>
### 刚才面个中间件岗位，那个面试官说session没办法持久化，他说的对吗？

<a name="ct2f5"></a>
## 近期小伙伴焦虑议题
<a name="YZBzk"></a>
### 关于 AI / AIGC 对 Java 从业人员的影响
有，但是它可以作为辅助工具。<br />从业人员更加需要具备专业技能：

- 成型案例 *
- 架构策略 *（八股文）
- 丰富代码设计能力
- 较强的架构能力（技能、沟通）
- 较深的底层功力
   - OS
   - 存储
   - 网络
   - 算法
   - 部分硬件
<a name="jhE9l"></a>
### 关于 Java/互联网 是否还有前途？
有人认为互联网已经发展到瓶颈了，缺少下一个风口。

国内的信息化、数字化程度远没有达到饱和状态。

先进生产力 AI 的担忧<br />落后的思想的顾忌

