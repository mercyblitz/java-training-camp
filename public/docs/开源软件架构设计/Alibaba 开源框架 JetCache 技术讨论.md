<a name="pz5cl"></a>
# Alibaba JetCache
<a name="SlIoz"></a>
## API 使用体验

<a name="z6OsJ"></a>
### 缓存类型 - CacheType 
本地：Local<br />远程：Remote
> 个人建议：Cache 可以为任意方式，并且能够组合 N 中
> Composite：
> 1. Local
> 2. Remote
> 3. Local + Remote
> 4. Remote + Local
> 
比如：Spring Caching CompositeCacheManager，内部可以组合有序多个 CacheManager 


<a name="YYH3X"></a>
#### 企业用户案例
有一个交易应用 API 平均的缓存读取次数 N 次，<br />Spring Caching 实现：
```java
@Cacheable
public List<User> getUsers() {
    // 读取数据库加载 Users 列表
}
```
N 级缓存设计（垂直）：

1. HttpServletRequest 上下文 （Servelt HTTP N-1）
2. ThreadLocal（非 Servlet HTTP N-1）
3. Redis（1）
4. Local

N 级缓存设计加载顺序：

- 水平
- 垂直
- 水平 + 垂直


Key 生成规则 - Key Generator<br />拦截点：

- 方法：方法参数
- 构造器：构造器参数
- 字段：注解等辅助元信息


软件架构设计<br />考虑点：

- 接入成本：易用性、文档、API 友好性
- 使用成本：机器成分、人力投入

<a name="pS4nw"></a>
### Cache 操作注解
```java
public interface UserService {
    @Cached(name="userCache-", key="#userId", expire = 3600)
    User getUserById(long userId);

    @CacheUpdate(name="userCache-", key="#user.userId", value="#user")
    void updateUser(User user);

    @CacheInvalidate(name="userCache-", key="#userId")
    void deleteUser(long userId);
}
```
<a name="DEw96"></a>
#### @Cached
Spring Caching @Cacheable + TTL
<a name="uCLKu"></a>
#### @CacheUpdate
Spring Caching @CachePut + TTL
<a name="Xzflq"></a>
#### @CacheInvalidate
Spring Caching @CacheEvict + TTL

<a name="VvzoA"></a>
### 缓存刷新注解 - @CacheRefresh

<a name="z2H6b"></a>
### 缓存防止渗透 - @CachePenetrationProtect

<a name="jAHvX"></a>
### 底层 Caching API
<a name="X7USa"></a>
### 高级 Caching API
<a name="IgWuw"></a>
#### 异步 Asynchronous API
<a name="rjzlZ"></a>
#### 分布式锁 Distributed lock

<a name="ud3cZ"></a>
# 如何通过 Spring Caching 实现 Alibaba JetCache 特性
<a name="pOqeW"></a>
## @Cached 注解实现
Spring Caching @Cacheable + TTL = @TTLCacheable
<a name="xH506"></a>
### 引入对应的注解 - @TTLCacheable
<a name="i7Ucf"></a>
### 扩展 CacheableOperation - TTLCacheableOperation
<a name="ufghY"></a>
### 实现 CacheResolver - TTLCacheResolver
```java
public class TTLCacheResolver implements CacheResolver, ApplicationContextAware {

    public static final String BEAN_NAME = "ttlCacheResolver";

    private ApplicationContext context;

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {

        Collection<Cache> caches = Collections.emptyList();
        // 自定义注解元信息同步到 CacheOperation 扩展类
        BasicOperation operation = context.getOperation();
        if (operation instanceof TTLCacheableOperation) {
            TTLCacheableOperation ttlCacheableOperation = (TTLCacheableOperation) operation;
            long expire = ttlCacheableOperation.getExpire();
            TimeUnit timeUnit = ttlCacheableOperation.getTimeUnit();
            Set<String> cacheNames = operation.getCacheNames();
            String cacheManagerValue = ttlCacheableOperation.getCacheManager();
            // cacheManager = local
            // cacheManager = remote
            // cacheManager = local,remote
            caches = new ArrayList<>(cacheNames.size());
            if (hasText(cacheManagerValue)) {
                String[] cacheManagerBeanNames = commaDelimitedListToStringArray(cacheManagerValue);
                for (String cacheManagerBeanName : cacheManagerBeanNames) {
                    CacheManager cacheManager = this.context.getBean(cacheManagerBeanName, CacheManager.class);
                    // CacheManager 可能是 CompositeCacheManager
                    for (String cacheName : cacheNames) {
                        Cache cache = cacheManager.getCache(cacheName);
                        caches.add(cache);
                    }
                    // 假设 CacheManager 为 RedisCacheManager 的话
                }
            }
        }
        // 处理其他 CacheOperation

        return caches;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
```


