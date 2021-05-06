##### redis下载安装（windows）

下载安装包

官网下载：https://github.com/microsoftarchive/redis/releases/tag/win-3.2.100

下载文件Redis-x64-3.2.100.zip，解压文件

服务端启动：redis-server.exe

客户端启动：redis-cli.exe

##### redis的特点

- 非关系型数据库：未采用关系模型来组织数据，以键值对存储，且结构不固定

- 操作是原子性的：一个操作的不可以再分，操作要么执行，要么不执行

- 单线程：命令是一个一个执行的，无需考虑并发带来的数据影响

##### redis的基本操作

清屏

```shell
clear
```

帮助

```shell
help [命令名称]
help [@组名]
```

退出

```shell
quit
exit
<ESC>
```

获取当前系统时间

```shell
time
```

查看运行属性值

```shell
info
```

##### redis的数据类型与存储格式

redis自身是一个Map，其中所有的数据都采用key: value的形式存储，key永远是字符串，value的类型有：

- string
- hash
- list
- set
- sorted_set
- Bitmaps
- HyperLogLog
- GEO

##### redis数据操作注意事项

- 数据操作运行结果：表示是否成功，0 - false - 失败，1 - true - 成功；表示结果值
- 数据未获取到返回nil，等同于null
- 数据最大存储量：512MB
- 数值计算最大范围：java中Long.MAX_VALUE（9223372036854775807）

##### string类型

一个存储空间保存一个字符串，如果字符串以整数的形式展示，可以作为数字操作使用

##### string类型数据操作

添加/修改数据

```shell
set [key] [value]
```

获取数据

```shell
get [key]
```

删除数据

```shell
del [key]
```

添加/修改多个数据

```shell
mset [key1] [value1] [key2] [value2] ...
```

获取多个数据

```shell
mget [key1] [key2]
```

获取数据字符个数（字符串长度）

```shell
strlen [key]
```

追加信息到原始信息后部（如果原始信息存在就追加，否则新建）

```shell
append [key] [value]
```

##### string作为数值操作

设置数值数据增加指定范围的值

```shell
incr [key]
incrby [key] [increment]
incrbyfloat [key] [increment]
```

设置数值数据减少指定范围的值

```shell
decr [key]
decrby [key] [increment]
```

##### string设置数据具有指定的生命周期

```shell
setex [key] [seconds] [value]
psetex [key] [milliseconds] [value]
```

##### string存储信息命名规范

```shell
set [表名:主键名:主键值:key] [value]
set [表名:主键名:主键值] {[主键名:主键值], [key1:value1], [key2:value2], ...}
```

##### hash类型

一个存储空间保存多个键值对数据，底层使用哈希表存储结构实现

##### hash类型数据操作

添加/修改数据

```shell
hset [hash_key] [field] [value]
```

只添加数据，不能修改数据

```shell
hsetnx [hash_key] [field] [value]
```

获取数据

```shell
hget [hash_key] [field]
```

删除数据

```shell
hdel [hash_key] [field1] [field2]
```

添加/修改多个数据

```shell
hmset [hash_key] [field1] [value1] [field2] [value2] ...
```

获取多个数据

```shell
hmget [hash_key] [field1] [field2] ...
```

获取哈希表中字段的数量

```shell
hlen [hash_key]
```

获取哈希表中是否存在指定的字段

```shell
hexsits [hash_key] [field]
```

获取哈希表中所有的字段名和字段值

```shell
hgetall [hash_key]
```

获取哈希表中所有的字段名

```shell
hkeys [hash_key]
```

获取哈希表中所有的字段值

```shell
hvals [hash_key]
```

设置指定字段的数值数据增加指定范围的值

```shell
hincrby [hash_key] [field] [increment]
hincrbyfloat [hash_key] [field] [increment]
```

##### hash类型数据操作注意事项

- value存数据不能嵌套存
- 每个hash存储的键值对上限为2^32 - 1个
- 不可滥用，不可作为对象列表使用
- 如果内部field很多，使用hgetall遍历整体数据效率会很低

##### list类型

一个存储空间保存多个数据，且可以体现进入顺序，底层使用双向链表存储结构实现

##### list类型数据操作

添加/修改数据

```shell
lpush [list_key] [value1] [value2] ...
rpush [list_key] [value1] [value2] ...
```

获取数据

```shell
lrange [list_key] [start] [stop] # start、stop（非必要）表示索引，链表头从0递增，链表尾从-1递减
lindex [list_key] [index]
llen [list_key]
```

获取并移除数据

```shell
lpop [list_key]
rpop [list_key]
```

规定时间内获取并移除数据（阻塞）

```shell
blpop [list_key1] [list_key2] ... [timeout]
brpop [list_key1] [list_key2] ... [timeout]
```

移除指定数据

```shell
lrem [list_key] [count] [value] # count表示移除多少个相同的值
```

##### list类型数据操作注意事项

- list中保存的数据都是string类型的，数据总容量是有限的，最多2^32 - 1个
- list具有索引的概念，但是操作数据时通常以队列的形式进行入队出队操作，或以栈的形式进行入栈出栈操作
- 获取全部数据操作结束索引设置为-1
- list可以对数据进行分页操作，通常第一页的信息来自于list，第二页及更多的信息通过数据库的形式加载

##### set类型

一个存储空间能够保存大量的数据，且便于高效查询，与hash存储结构完全相同，仅存储键，不存储值（nil），并且不允许重复

##### set类型数据操作

添加数据

```shell
sadd [set_key] [member1] [member2]
```

获取全部数据

```shell
smembers [set_key]
```

删除数据

```shell
srem [set_key] [member1] [member2]
```

获取集合数据总量

```shell
scard [set_key]
```

判断集合中是否包含指定数据

```shell
sismember [set_key] [member]
```

随机获取集合中指定数量的数据

```shell
srandmember [set_key] [count]
```

随机获取集合中的某个数据并将该数据移出集合

```shell
spop [set_key]
```

求两个集合的交、并、差集

```shell
sinter [set_key1] [set_key2]
sunion [set_key1] [set_key2]
sdiff [set_key1] [set_key2]
```

求两个集合的交、并、差集并存储到指定集合中

```shell
sinterstore [dest_set_key] [set_key1] [set_key2]
sunionstore [dest_set_key] [set_key1] [set_key2]
sdiffstore [dest_set_key] [set_key1] [set_key2]
```

将指定数据从原始集合中移动到目标集合中

```shell
smove [src_set_key] [dest_set_key] [member]
```

##### set类型数据操作注意事项

- set类型不允许数据重复，如果添加的数据在set中已经存在，将只保留一份
- set虽然与hash的存储结构相同，但是无法启用hash中存储值的空间

##### sorted_set类型

一个存储空间既能够保存大量的数据，又支持排序，在set的存储结构上添加可排序字段scores

##### sorted_set类型数据操作

添加数据

```shell
zadd [sorted_set_key] [score1] [member1] [score2] [member2] ...
```

获取全部数据

```shell
zrange [sorted_set_key] [start] [stop] # 正向获取
zrevrange [sorted_set_key] [start] [stop] # 反向获取
zrange [sorted_set_key] [start] [stop] withscores # 获取包括scores
zrevrange [sorted_set_key] [start] [stop] withscores # 获取包括scores
```

删除数据

```shell
zrem [sorted_set_key] [member1] [member2] ...
```

按条件获取数据

```shell
zrangebyscore [sorted_set_key] [min_score] [max_score] withscores
zrangebyscore [sorted_set_key] [min_score] [max_score] withscores limit [offset] [count] # 限制查询数量
zrevrangebyscore [sorted_set_key] [min_score] [max_score] withscores
```

按条件删除数据

```shell
zremrangebyrank [sorted_set_key] [start] [stop] # 按索引删除
zremrangebyscore [sorted_set_key] [min_score] [max_score] # 按score删除
```

获取集合数据总量

```shell
zcard [sorted_set_key]
zcount [sorted_set_key] [min_score] [max_score]
```

集合交、并操作

```shell
# 默认会对score进行求和操作，也可以在末尾添加其他数值操作
zinterstore [dest_sorted_set_key] [number_of_keys] [sorted_set_key1] [sorted_set_key2] ...
zunionstore [dest_sorted_set_key] [number_of_keys] [sorted_set_key1] [sorted_set_key2] ...
```

获取数据对应的索引（排名）

```shell
zrank [sorted_set_key] [member] # 按score从小到大
zrevrank [sorted_set_key] [member] # 按score从大到小
```

score值获取与修改

```shell
zscore [sorted_set_key] [member]
zincrby [sorted_set_key] [increment] [member]
```

##### sorted_set类型数据操作注意事项

- score保存的数据存储空间是64位，如果是整数，范围是-9007199254740992~9007199254740992
- score保存的数据也可以是一个双精度的double值，基于双精度浮点数的特征，可能会丢失精度，使用时要慎重
- sorted_set底层存储还是基于set结构的，因此数据不能重复，如果重复添加相同的数据，score值将被反复覆盖（即使返回失败），保留最后一次修改的结果

##### Bitmaps类型

一个存储空间保存多个连续的比特位，用单个比特位（1和0，默认0）来映射某个元素的状态，并提供位级别的操作

##### Bitmaps类型数据操作

设置指定key对应偏移量上的bit值，value只能是0或1

```shell
setbit [key] [offset] [value]
```

获取指定key对应偏移量上的bit值

```shell
getbit [key] [offset]
```

统计指定key中1的数量

```shell
bitcount [key] [start] [end]
```

对指定key按位进行交、并、非、异或操作，并将结果保存到destKey中

```shell
bitop [and|or|not|xor] [dest_key] [key1] [key2] ...
```

##### HyperLogLog类型

用来做基数统计（基数是数据集去重后的元素个数），运用了LogLog的算法（存在一定误差：0.81%），不是集合，不保存数据，只记录数量

##### HyperLogLog类型数据操作

添加数据

```shell
pfadd [key] [member1] [member2] ...
```

统计数据

```shell
pfcount [key1] [key2] ...
```

合并数据

```shell
pfmerge [dest_key] [src_key1] [src_key2] ...
```

##### GEO类型

一个存储空间保存地理位置坐标信息

##### GEO类型数据操作

添加坐标点

```shell
geoadd [key] [longitude1 latitude1 member1] [longitude2 latitude2 member2] ...
```

获取坐标点

```shell
geopos [key] [member1] [member2] ...
```

计算坐标点距离（水平距离）

```shell
geodist [key] [member1] [member2] ... [unit(m|km|ft|mi)]
```

根据坐标求范围内的数据

```shell
georadius [key] [longitude] [latitude] [radius] [unit(m|km|ft|mi)]
```

根据点求范围内数据

```shell
georadiusbymember [key] [member] [radius] [unit(m|km|ft|mi)]
```

获取指定点对应的坐标hash值

```shell
geohash [key] [member1] [member2] ...
```

##### key通用操作

删除指定key

```shell
del [key]
```

获取key是否存在

```shell
exists [key]
```

获取key的类型

```shell
type [key]
```

为指定key设置有效期

```shell
expire [key] [seconds]
pexpire [key] [milliseconds]
expireat [key] [timestamp]
pexpireat [key] [milliseconds-timestamp]
```

获取key的有效时间

```shell
ttl [key] # 返回值：-2 -> key不存在；-1 -> key存在，未设置有效期或有效期是永久；有效时长（s）
pttl [key] # ms
```

切换key从时效性转换为永久性

```shell
persist [key]
```

查询key

```shell
# pattern
# * 匹配任意数量的任意符号
# ? 匹配一个任意符号
# [符号] 匹配[]内的指定符号
keys pattern
```

为key改名

```shell
rename [key] [new_key] # 如果new_key已存在，则原来key的值会覆盖new_key的值
renamenx [key] [new_key] # 避免以上情况
```

对所有key排序

```shell
sort # 不会改变原数据的顺序
sort [key] desc
```

其他key通用操作

```shell
help @generic
```

##### 数据库通用操作

redis为每个服务提供16个数据库，编号从0到15（默认操作0号），每个数据库之间的数据相互独立

切换操作

```shell
select [db_index]
```

其他操作

```shell
quit
ping # 测试与服务器是否连通
echo [message]
```

数据移动

```shell
move [key] [db_index] # 如果目标数据库存在key，则移动操作失败
```

数据清除

```shell
dbsize # 当前数据库的key的数量
flushdb # 清除当前数据库的数据
flushall # 清除所有数据库的数据
```

##### redis持久化

| 对比项   | 快照形式（RDB）              | 日志形式（AOF） |
| -------- | ---------------------------- | --------------- |
| 存储内容 | 存储数据结果                 | 存储操作过程    |
| 占用空间 | 较小                         | -               |
| 存储速度 | -                            | 较快            |
| 恢复速度 | 较快                         | -               |
| 性能开支 | -                            | 较小            |
| 版本兼容 | -                            | 支持            |
| 实时保存 | -                            | 支持            |
| 启动优先 | -                            | 高              |
| 应用范围 | 数据备份、全量复制、灾难恢复 | 数据敏感        |

##### RDB

手动执行一次保存操作

```shell
save # save指令的执行会阻塞当前redis服务器，直到当前RDB过程完成为止，有可能会造成长时间阻塞，线上环境不建议使用
```

手动启动后台保存操作，但不是立即执行

```shell
bgsave # bgsave命令是针对save阻塞问题做的优化，建议使用
```

服务器运行过程中重启保存

```shell
debug reload
```

关闭服务器时保存

```shell
shutdown save
```

自动保存：在redis配置文件中配置

查看/修复RDB文件

```shell
redis-check-rdb [rdb_filename]
redis-check-rdb --fix [rdb_filename]
```

##### AOF

保存数据的三种策略：

- always：每次操作均同步，数据零误差，性能较低
- everysec：每秒同步一次，数据准确性较高，性能较高
- no：系统控制同步，整体过程不可控

重写规则：

- 进程内已超时的数据不再写入文件
- 忽略无效指令，重写时使用进程内数据直接生成，这样新的AOF文件只保留最终数据的写入命令
- 对同一数据的多条写命令合并为一条命令

手动重写

```shell
bgrewriteaof
```

自动重写：在redis配置文件中配置

查看/修复AOF文件

```shell
redis-check-aof [aof_filename]
redis-check-aof --fix [aof_filename]
```

##### 事务

开启事务

```shell
multi
```

执行事务（提交）

```shell
exec
```

取消事务

```shell
discard
```

事务回滚（手动）

记录执行事务之前的数据状态，设置指令恢复所有被修改的项

注意事项：

- 事务中所包含的命令存在语法错误，则所有命令都不会执行
- 事务中所包含的命令无法正确执行，则运行正确的命令会执行，运行错误的命令不会被执行

##### 锁

对key添加监视锁，在事务执行前如果key发生了变化，则事务执行会返回(nil)，表示被终止

```shell
watch [key1] [key2] ... # 必须在开启事务前
```

取消对所有key的监视

```shell
unwatch
```

设置一个公共锁（悲观锁）

```shell
# 设置失败，排队或等待
# 设置成功，进行下一步的业务操作
# 操作完毕释放公共锁
setnx lock-[key] [value]
```

释放公共锁

```shell
del lock-[key]
```

设置有时间限定的公共锁，到时不释放，放弃锁

```shell
expire lock-[key] [seconds]
pexpire lock-[key] [milliseconds]
```

##### 过期数据删除策略

|      | 定时删除                                                     | 惰性删除                                                     | 定期删除                                                     |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 策略 | 创建一个定时器，当key设置有过期时间，且过期时间到达时，由定时器任务立即执行对键的删除操作 | 数据到达过期时间，不做处理，等下次访问该数据时，发现已过期，删除，返回不存在 | 周期性轮询redis库中的时效性数据，采用随机抽取的策略，利用过期数据占比的方式控制删除频度 |
| 优点 | 节约内存，到时就删除，快速释放掉不必要的内存占用             | 节约CPU性能，发现必须删除的时候才删除                        | CPU性能占用设置有峰值，检测频度可自定义设置                  |
| 缺点 | CPU压力很大，无论CPU此时负载量多高，均占用CPU，会影响redis服务器响应时间和指令吞吐量 | 内存压力很大，出现长期占用内存的数据                         | 内存压力不是很大，长期占用内存的冷数据会被持续清理           |
| 总结 | 用处理器性能换取存储空间                                     | 用存储空间换取处理器性能                                     | 周期性抽查存储空间                                           |

##### 数据逐出策略

检测易失数据（可能会过期的数据集server.db[i].expires）：

- volatile-lru：挑选最近最少使用的数据淘汰
- volatile-lfu：挑选最近使用次数最少的数据淘汰
- volatile-ttl：挑选将要过期的数据淘汰
- volatile-random：任意选择数据淘汰

检测全库数据（所有数据集server.db[i].dict）：

- allkeys-lru：挑选最近最少使用的数据淘汰
- allkeys-lfu：挑选最近使用次数最少的数据淘汰
- allkeys-random：任意选择数据淘汰

放弃数据逐出：

- no-enviction：禁止逐出数据（redis4.0中默认策略），会引发错误OOM（Out Of Memory）

##### 主从复制

主（master）：

- 写数据
- 执行写操作时，将出现变化的数据自动同步到slave
- 读数据（可忽略）

从（slave）：

- 读数据
- 写数据（禁止）

特征：一个master可以拥有多个slave，一个slave只能对应一个master

概念：将master中的数据即时、有效地复制到slave中

作用：读写分离、负载均衡、故障恢复、数据冗余、高可用基石

##### 主从连接

方式一：客户端发送命令

```shell
slaveof [master_ip] [master_port]
```

方式二：启动服务器参数

```shell
redis-server -slaveof [master_ip] [master_port]
```

方式三：服务器配置

```shell
slaveof [master_ip] [master_port]
```

断开连接

```shell
slaveof no one
```

master设置客户端访问密码

```shell
config set requirepass [password]
```

slave设置客户端访问密码

```shell
auth [password]
```

客户端通过密码访问服务端

```shell
redis-cli -a [password]
```

##### 缓存预热

问题现象：

服务器启动后迅速宕机

问题原因：

- 请求数量较高
- 主从之间数据吞吐量较大，数据同步操作频度较高

解决方法：

系统启动前，提前将相关的缓存数据加载到缓存系统，使用户直接查询事先被预热的缓存数据，从而避免在用户请求时，先查询数据库，然后再将数据缓存

##### 缓存雪崩

问题现象：

数据库连接量突然激增，redis服务器崩溃，redis集群崩溃，数据库崩溃，重启无效

问题原因：

- 在一个较短的时间内，缓存中较多的key集中过期
- 此周期内请求访问过期的数据，redis未命中，redis向数据库获取数据
- 数据库同时接收到大量的请求无法及时处理
- redis大量请求被积压，开始出现超时现象
- 数据库流量激增，数据库崩溃
- 重启后仍然面对缓存中无数据可用
- redis服务器资源被严重占用，redis服务器崩溃
- redis集群呈现崩塌，集群瓦解
- 应用服务器无法及时得到数据响应请求，来自客户端的请求数量越来越多，应用服务器崩溃
- 应用服务器、redis、数据库全部重启，效果不理想

解决方法（设计）：

- 更多的页面静态化处理
- 构建多级缓存架构：Nginx缓存 + redis缓存 + ehcache缓存
- 检测mysql严重耗时业务进行优化，对数据库的瓶颈进行排查，例如超时查询、耗时较高的事务等
- 灾难预警机制，监控redis服务器性能指标：CPU占用、CPU使用率、内存容量、查询平均响应时间、线程数
- 限流、降级，短时间范围内牺牲一些客户体验，限制一部分请求访问，降低应用服务器压力，待业务低速运转后再逐步放开访问

解决方法（redis）：

- LRU与LFU切换
- 数据有效期策略调整：根据业务数据有效期进行分类错峰，A类90分钟，B类80分钟，C类70分钟，过期时间使用固定时间+随机值的形式，稀释集中到期的key的数量
- 超热数据使用永久key
- 定期维护（自动+人工），对即将过期数据做访问量分析，确认是否延时，配合访问量统计，做热点数据的延时
- 加锁（慎用！）

##### 缓存击穿

问题现象：

数据库连接量突然激增，redis服务器无大量key过期，内存平稳，CPU正常，数据库崩溃

问题原因：

- redis中某个key过期，该key访问量巨大
- 多个数据请求从服务器直接压到redis后，均末命中
- redis在短时间内发起了大量对数据库中同一数据的访问

解决方法：

- 预先设定：以电商为例，每个商家根据店铺等级，指定若干款主打商品，在购物节期间，加大此类信息key的过期时长（注意：购物节不仅指当天，还包括后续若干天，访问峰值呈现逐渐降低的趋势）
- 现场调整监控访问量，对自然流量激增的数据延长过期时间或设置为永久性key
- 后台刷新数据：启动定时任务，高峰期来临之前，刷新数据有效期，确保不丢失
- 二级缓存：设置不同的失效时间，保障不会被同时淘汰就行
- 加锁：分布式锁，防止被击穿，但是要注意也是性能瓶颈，慎重！

##### 缓存穿透

问题现象：

服务器流量随时间逐步增大，redis服务器命中率随时间逐步降低，redis服务器内存平稳，CPU占用激增，数据库压力激增，数据库崩溃

问题原因：

- 获取的数据在数据库中也不存在，数据库查询未得到对应数据
- redis获取到null数据未进行持久化，直接返回
- 下次此类数据到达重复上述过程
- 出现黑客攻击服务器

解决方法：

- 缓存null对查询结果为null的数据进行缓存（长期使用，定期清理），设定短时限，例如30-60秒，最高5分钟
- 白名单策略：
  提前预热各种分类数据id对应的bitmaps，id作为bitmaps的offset，相当于设置了数据白名单，当加载正常数据时，放行，加载异常数据时拦截（效率偏低）
  使用布隆过滤器（有关布隆过滤器的命中问题对当前状况可以忽略）
- 实时监控：
  监控redis命中率（业务正常范围时，通常会有一个波动值）与null数据的占比
  非活动时段波动：通常检测3-5倍，超过5倍纳入重点排查对象
  活动时段波动：通常检测10-50倍，超过50倍纳入重点排查对象
  根据倍数不同，启动不同的排查流程，然后使用黑名单进行防控（运营）
- key加密：
  问题出现后，临时启动防灾业务key，对key进行业务层传输加密服务，设定校验程序，给过来的key校验
  例如每天随机分配60个加密串，挑选2到3个，混到页面数据id中，发现访问key不满足规则，驳回数据访问

##### 性能监控

| 监控指标                     | 监控数值                                                     |
| ---------------------------- | ------------------------------------------------------------ |
| 性能指标：Performance        | latency：redis响应一个请求的时间<br />instantaneous_ops_per_sec：平均每秒处理请求总数<br />hit rate (calculated)：缓存命中率（计算出来的） |
| 内存指标：Memory             | used_memory：已使用内存<br />mem_fragmentation_ratio：内存碎片率<br />evicted_keys：由于最大内存限制被移除的key的数量<br />blocked_clients：由于BLPOP、BRPOP、or BRPOPLPUSH而被阻塞的客户端 |
| 基本活动指标：Basic Activity | connected_clients：客户端连接数<br />connected_slaves：slave数量<br />master_last_io_seconds_ago：最近一次主从交互之后的秒数<br />keyspace：数据库中的key值总数 |
| 持久性指标：Persistence      | rdb_last_save_time：最后一次持久化保存到硬盘的时间戳<br />rdb_changes_since_last_save：自最后一次持久化以来数据库的更改数 |
| 错误指标：Error              | rejected_connections：由于达到maxclients限制而被拒绝的连接数<br />keyspace_misses：key值查找失败（没有命中）次数<br />master_link_down_since_seconds：主从断开的持续时间（秒） |

##### 监控工具

- Cloud Insight Redis
- Prometheus
- Redis-stat
- Redis-faina
- RedisLive
- zabbix

##### 监控命令

性能测试

```shell
redis-benchmark [option] [option_value]
```

打印服务器调试信息

```shell
monitor
```

慢查日志

```shell
# get 获取慢查日志
# len 获取慢查日志条目数
# reset 重置慢查日志
slowlog [get|len|reset]
```

