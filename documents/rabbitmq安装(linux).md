##### 安装依赖环境

```bash
yum install build-essential openssl openssl-devel unixODBC unixODBC-devel make gcc gcc-c++ kernel-devel m4 ncurses-devel tk tc xz
```

##### 安装erlang

```bash
rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm
```

##### 安装socat

```bash
rpm -ivh socat-1.7.3.2-5.el7.lux.x86_64.rpm
```

##### 安装rabbitmq

```bash
rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm
```

##### 开启管理界面

```bash
rabbitmq-plugins enable rabbitmq_management
```

##### 修改默认配置信息

```bash
vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app
```


loopback_users中的[<<"guest">>]改为[guest]

##### 启动rabbitmq服务

```bash
service rabbitmq-server start
```

##### 登录

浏览器访问：http://localhost:15672/

账号/密码：guest/guest

##### 添加配置文件

```bash
cp /usr/share/doc/rabbitmq-server-3.6.5/rabbitmq.config.example /etc/rabbitmq/rabbitmq.config
```

##### 开启消息追踪插件

```bash
rabbitmq-plugins enable rabbitmq_tracing
```

