##### zookeeper的三种角色

- leader：负责处理集群写请求，并发起投票，只有超过半数的节点同意后才会提交该写请求
- follower：处理读请求，响应结果，转发写请求到 leader，在选举 leader 过程中参与投票
- observer：可以理解为没有投票权的 follower，主要职责是协助 follower 处理读请求（由于 leader 在提出写请求提案时，需要半数以上的 follower 投票节点同意，因此增加 follower 节点会增大 leader 和 follower 的通信压力，降低写操作效率）

##### zookeeper的两种模式

- 恢复模式：当服务启动或 leader 崩溃后，zookeeper 进入恢复状态，选举 leader，leader 选出后，将完成 leader 和其他机器的数据同步，当大多数 server 完成和 leader 的同步后，恢复模式结束
- 广播模式：当 leader 已经和多数的 follower 进行了状态同步后，进入广播模式，如果有新加入的服务器，会自动从 leader 中同步数据，leader 在接收客户端请求后，会生成事务提案广播给其他机器，有超过半数以上的 follower 同意该提案后，再提交事务

##### zookeeper的应用场景

- 配置中心
- 负载均衡
- 命名服务
- DNS 服务
- 集群管理
- 分布式锁
- 分布式队列

##### 下载安装

下载地址：https://archive.apache.org/dist/zookeeper/zookeeper-3.4.6/

下载文件名：zookeeper-3.4.6.tar.gz

安装 zookeeper（需要先安装 jdk）

```shell
# 解压安装包
tar -zxvf zookeeper-3.4.6.tar.gz
# 进入安装目录，创建data目录
cd zookeeper-3.4.6
mkdir data
# 修改配置文件
cd conf
mv zoo_sample.cfg zoo.cfg
vim zoo.cfg
```

修改内容

```
dataDir=/root/zookeeper-3.4.6/data
# zookeeper3.5以后默认使用8080端口，容易被占用
admin.serverPort=8888
```

zookeeper 服务相关命令

```shell
cd /root/zookeeper-3.4.6/bin
# 启动zookeeper服务
./zkServer.sh start
# 查看zookeeper服务状态
./zkServer.sh status
# 停止zookeeper服务
./zkServer.sh stop
# 重启zookeeper服务
./zkServer.sh restart
```

客户端连接 zookeeper 服务

```shell
./zkCli.sh -server [ip]:[port]
```

##### 集群搭建

创建集群目录和集群节点目录

```shell
# 创建集群目录
mkdir /usr/local/zkcluster
# 解压安装包到集群目录
tar -zxvf zookeeper-3.4.6.tar.gz -C /usr/local/zkcluster
# 进入安装目录，创建data目录
cd /usr/local/zkcluster/zookeeper-3.4.6
mkdir data
# 进入集群目录
cd /usr/local/zkcluster
# 创建集群节点目录
mv zookeeper-3.4.6 zookeeper01
cp -r zookeeper01/ zookeeper02
cp -r zookeeper01/ zookeeper03
```

创建每个集群节点的服务器id（myid 内容分别是1、2、3）

```shell
cd /usr/local/zkcluster/zookeeper01/data
touch myid
vim myid
```

配置每个集群节点

```shell
cd /usr/local/zkcluster/zookeeper01/conf
mv zoo_sample.cfg zoo.cfg
vim zoo.cfg
```

修改内容

```
# zookeeper01 zookeeper02 zookeeper03
dataDir=/usr/local/zkcluster/zookeeper01/data
# zookeeper3.5以后默认使用8080端口，容易被占用
admin.serverPort=8888
# 2181 2182 2183
clientPort=2181
# server.[server_id]=[server_ip]:[client_port]:[vote_port]
server.1=192.168.3.26:2181:3881
server.2=192.168.3.26:2182:3882
server.3=192.168.3.26:2183:3883
```

启动集群

```shell
# 依次启动三个zookeeper服务，最终server2是leader，server1和server3是follower
cd /usr/local/zkcluster/zookeeper01/bin
./zkServer.sh start
cd /usr/local/zkcluster/zookeeper02/bin
./zkServer.sh start
cd /usr/local/zkcluster/zookeeper03/bin
./zkServer.sh start
```

##### Znode节点类型

- 持久化目录节点（PERSISTENT）：客户端与 zookeeper 断开连接后，该节点依旧存在
- 持久化顺序编号目录节点（PERSISTENT_SEQUENTIAL）：客户端与 zookeeper 断开连接后，该节点依旧存在，zookeeper 会给该节点按照顺序编号
- 临时目录节点（EPHEMERAL）：客户端与 zookeeper 断开连接后，该节点被删除
- 临时顺序编号目录节点（EPHEMERAL_SEQUENTIAL）：客户端与 zookeeper 断开连接后，该节点被删除，zookeeper 会给该节点按照顺序编号

##### 客户端命令

查看节点

```shell
# 查看当前节点所包含的节点
ls /
# 查看当前节点所保存的数据
ls2 /
# 监听节点路径的变化（有效期一次）
ls /[znode_name] watch
```

创建节点

```shell
# 创建持久化目录节点
create /[znode_name] [znode_data]
# 创建持久化顺序编号目录节点
create -s /[znode_name] [znode_data]
# 创建临时目录节点
create -e /[znode_name] [znode_data]
# 创建临时顺序编号目录节点
create -e -s /[znode_name] [znode_data]
```

获取节点值和状态

```shell
get /[znode_name]
# 监听节点值的变化（有效期一次）
get /[znode_name] watch
```

修改节点值

```shell
set /[znode_name]
```

查看节点状态

```shell
stat /[znode_name]
```

删除节点（存在子节点则无法删除）

```shell
delete /[znode_name]
```

递归删除节点（存在子节点则一并删除）

```shell
rmr /[znode_name]
```

