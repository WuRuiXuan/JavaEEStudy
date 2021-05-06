##### 下载安装

下载地址：https://www.elastic.co/cn/downloads/past-releases/elasticsearch-5-6-8

下载文件名：elasticsearch-5.6.8.tar.gz

```shell
# 解压安装包
tar -zxvf elasticsearch-5.6.8.tar.gz -C /usr/local/
# 添加es用户
useradd es
passwd es
# es目录访问授权
chown -R es:es /usr/local/elasticsearch-5.6.8
# 切换为es用户
su - es
# 启动es（以守护进程方式启动：./elasticsearch &）
cd /usr/local/elasticsearch-5.6.8/bin
./elasticsearch
# 验证启动是否成功
curl -X GET http://localhost:9200
```

##### ES允许跨域和远程访问

执行命令

```shell
cd /usr/local/elasticsearch-5.6.8/config
vim elasticsearch.yml
```

添加内容

```yaml
http.cors.enabled: true
http.cors.allow-origin: "*"
network.host: 0.0.0.0
```

切换到root用户，修改 ES 用户的软硬限制

```shell
su - root
vim /etc/security/limits.conf
```

添加内容

```
es soft nofile 65535
es hard nofile 65537
```

修改 sysctl.conf 文件

```shell
vim /etc/sysctl.conf
```

添加内容

```
vm.max_map_count=262144
```

使配置立即生效

```shell
sysctl -p
```

切换回 ES 用户，然后重启 ES 服务

##### ES启动报错

报错：error='Cannot allocate memory' (errno=12)

解决办法：修改 ES 的 JVM 运行内存

```shell
vim /usr/local/elasticsearch-5.6.8/config/jvm.options
```

修改内容（适当改小）

```
-Xms128m
-Xmx128m
```

##### 安装ES图形化界面插件

下载地址：https://github.com/mobz/elasticsearch-head

下载文件名：elasticsearch-head-master.zip

解压安装包，在 elasticsearch-head-master 目录下执行命令

```shell
# 安装grunt-cli（需要先安装nodejs）
npm install -g grunt-cli
# 安装项目依赖包
npm install
# 启动
grunt server
```

##### ES核心概念

- 索引 index -> 数据库 Database
- 类型 type -> 数据库表 Table
- 映射 mappings -> 数据库表的属性 Table Attributes
- 文档 document -> 数据库表一行数据 Row
- 字段 field -> 数据库表一个字段 Column

##### 集成IK分词器

下载地址：https://github.com/medcl/elasticsearch-analysis-ik/releases

下载文件名：elasticsearch-analysis-ik-5.6.8.zip

```shell
# 解压安装包
unzip elasticsearch-analysis-ik-5.6.8.zip
# 重命名文件夹
mv elasticsearch ik-analyzer
# 移动到ES插件目录
mv ik-analyzer /usr/local/elasticsearch-5.6.8/plugins
```

重启 ES 服务

##### 集群搭建

创建集群目录和集群节点目录

```shell
# 删除ES目录下的data目录（清空数据）
rm -rf /usr/local/elasticsearch-5.6.8/data
# 创建集群目录
cd /usr/local
mkdir escluster
# 创建集群节点目录
cp -r elasticsearch-5.6.8 elasticsearch01
mv elasticsearch01 /usr/local/escluster
cd escluster
cp -r elasticsearch01/ elasticsearch02
cp -r elasticsearch01/ elasticsearch03
```

配置每个集群节点

```shell
vim /usr/local/escluster/elasticsearch01/config/elasticsearch.yml
```

修改内容

```yaml
cluster.name: my-escluster # 集群名称，每个节点配置相同
node.name: es01 # 节点名称，每个节点配置唯一
http.port: 9201 # 服务端口号，在同一机器下必须不一样
transport.tcp.port: 9301 # 集群节点间通信端口号，在同一机器下必须不一样
discovery.zen.ping.unicast.hosts: ["127.0.0.1:9301", "127.0.0.1:9302", "127.0.0.1:9303"] # 集群自动发现机器ip集合
```

es 用户授权

```shell
chown -R es:es /usr/local/escluster/elasticsearch01
```

启动每个节点

```shell
cd /usr/local/escluster/elasticsearch01/bin
./elasticsearch
```

