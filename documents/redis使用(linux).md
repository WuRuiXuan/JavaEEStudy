##### redis下载安装

下载安装包

官网下载：https://redis.io/download

下载文件redis-6.2.1.tar.gz到系统根目录

```shell
# 进入系统根目录
cd /
# 解压文件
tar -xzvf redis-6.2.1.tar.gz
# 进入安装文件夹目录
cd redis-6.2.1
# 查看安装设定
cat Makefile
# 安装
make install
```

服务端启动

```shell
redis-server
```

客户端启动

```shell
redis-cli
```

##### 指定端口启动redis

服务端指定端口启动

```shell
redis-server --port [端口号]
```

客户端连接指定端口的服务端

```shell
redis-cli -p [端口号]
```

##### 使用配置文件启动redis

进入redis安装文件夹目录

```shell
cd /redis-6.2.1
```

查看redis配置文件

```shell
cat redis.conf
# 过滤注释和空白
cat redis.conf | grep -v "#" | grep -v "^$"
```

创建配置文件目录和服务文件目录

```shell
mkdir config
mkdir data
```

修改redis配置文件

```shell
# 拷贝到配置目录中，过滤注释和空白
cat redis.conf | grep -v "#" | grep -v "^$" > config/redis-6379.conf
```

修改redis-6379.conf的内容为：

```
bind localhost
port 6379
daemonize no
dir /redis-6.2.1/data
databases 16
dbfilename dump-6379.rdb
rdbcompression yes
rdbchecksum yes
appendonly yes
appendfsync always
appendfilename appendonly-6379.aof
cluster-enabled yes
cluster-config-file nodes-6379.conf
cluster-node-timeout 10000
```

使用配置文件启动server

```shell
redis-server config/redis-6379.conf
```

查看是否启动

```shell
ps -ef | grep redis-
```

##### 允许远程访问

修改redis配置文件

```shell
vim /root/redis-6.2.1/config/redis-6379.conf
```

修改内容

```
# bind 127.0.0.1 -::1
protected-mode no
```

使用该配置文件重启server

##### 配置文件-基础

- bind [ip]：绑定主机地址
- port [port]：当前服务启动端口号
- daemonize [yes|no]：是否以守护进程方式启动，使用本启动方式，redis将以服务的形式存在，日志将不再打印到命令窗口中
- dir [path]：设定当前服务文件保存位置，包含日志文件、持久化文件等
- databases  [number]：设置数据库的数量，默认值为16

##### 配置文件-日志

- loglevel [debug|verbose|notice|warning]：设置日志级别，默认值为verbose，开发建议verbose，生产建议notice
- logfile "[xxxx.log]"：日志文件名

##### 配置文件-客户端

- maxclients [number]：设置同一时间最大客户端连接数，默认无限制，当客户端连接到达上限时，redis会关闭新的连接
- timeout [seconds]：客户端闲置等待最大时长，达到最大值后关闭连接，如需关闭该功能，设置为0

##### 配置文件-多服务器快捷配置

- include [conf_path]：导入并加载指定配置文件，用于快速创建公共配置较多的redis实例配置文件，便于维护

##### 配置文件-RDB

- dbfilename [dump-xxxx.rdb]：RDB持久化文件名
- rdbcompression [yes|no]：是否开启压缩
- rdbchecksum [yes|no]：是否开启加载检测
- stop-writes-on-bgsave-error [yes|no]：后台存储过程中如果出现错误，是否停止保存操作，默认开启
- save [seconds] [number_of_changes]：限定时间范围内key的变化数量达到指定数量即进行持久化（不进行数据比对，两次修改为相同的值依然算一次变化）

##### 配置文件-AOF

- appendfilename "[appendonly-xxxx.aof]"：AOF持久化文件名
- appendonly [yes|no]：是否开启AOF持久化功能，默认不开启
- appendfsync [always|everysec|no]：AOF持久化数据的策略
- auto-aof-rewrite-min-size [size(kb|mb|...)]：AOF缓存区大小（aof_current_size）达到指定大小触发重写
- auto-aof-rewrite-percentage [0-100]：AOF缓存区大小（aof_current_size）超过基础大小（aof_base_size）达到指定百分比触发重写

##### 配置文件-数据逐出

- maxmemory [size(kb|mb|...)]：最大可使用内存，默认值为0，表示不限制，通常设置在物理内存的50%以上
- maxmemory-samples [number]：每次选取待删除数据的个数，默认值为5，选取数据时如果全库扫描，会导致严重的性能消耗，降低读写性能，因此采用随机获取数据的方式作为待检测删除数据
- maxmemory-policy [policy]：达到最大可使用内存后，对被挑选出来的数据进行删除的策略

##### 配置文件-主从复制

- slaveof [master_ip] [master_port]：在slave配置文件中，设置master
- requirepass [password]：在master配置文件中，设置客户端连接需要的密码
- masterauth [password]：在slave配置文件中，设置连接master需要的密码
- repl-backlog-size [size(kb|mb|...)]：复制缓冲区大小，默认值为1MB，用于保存master收到的所有指令，最优设置 = 2 * master到slave的重连平均时长 * master平均每秒产生写命令数据总量
- repl-timeout [seconds]：复制连接超时时间，默认值为60，需设置为repl-ping-slave-period的5-10倍，否则很容易判定超时
- repl-ping-slave-period [seconds]：ping指令发送的频度，默认值为10
- slave-serve-stale-data [yes|no]：主从复制中，服务端是否可以响应外部客户端请求，设置成no时，有客户端请求会返回“SYNC with master in progress”
- min-slaves-to-write [number]：当slave的数量少于指定数量时，强制关闭master写功能，停止数据同步
- min-slaves-max-lag [seconds]：当所有slave的延迟都大于指定时间时，强制关闭master写功能，停止数据同步

##### 配置文件-集群

- cluster-enabled [yes|no]：是否开启集群模式
- cluster-config-file [nodes-xxxx.conf]：集群配置文件名
- cluster-node-timeout [milliseconds]：集群节点超时时间
- cluster-migration-barrier [count]：master连接的slave最小数量

##### 配置文件-慢查日志

- slowlog-log-slower-than [number]：设置慢查询的时间下限（微秒）
- slowlog-max-len [number]：设置慢查询命令对应的日志显示长度（命令数）

##### 哨兵

查看哨兵配置文件

```shell
cat sentinel.conf | grep -v "#" | grep -v "^$"
```

配置参数说明：

- port [port]：服务端口
- dir [path]：服务文件保存位置
- sentinel monitor [master_name] [master_ip] [master_port] [number]：设置监视的master，以及认定master挂了需要多少个哨兵同意，通常设置为 (哨兵数量 + 1) / 2
- down-after-milliseconds [master_name] [milliseconds]：设置master连接多长时间没响应，哨兵就认定master挂了
- parallel-syncs [master_name] [number]：设置当老master挂了，新master上任时，有几条线同时开始数据同步
- failover-timeout [master_name] [milliseconds]：设置数据同步超时时间

##### 集群搭建

安装ruby环境（redis5以前）

```shell
yum install ruby
```

执行搭建命令

```shell
# number：一个master连多少个slave

# redis5以前（/src目录下）
./src/redis-trib.rb create --replicas [number] [ip:port] [ip:port] ...

# redis5以后
redis-cli --cluster create [ip:port] [ip:port] ... --cluster-replicas [number]
```

客户端连接集群

```shell
redis-cli -c
```

查看节点信息

```shell
cluster nodes
```

