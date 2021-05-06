##### 启动节点

```bash
RABBITMQ_NODE_PORT=5673 RABBITMQ_NODENAME=rabbit1 rabbitmq-server start
RABBITMQ_NODE_PORT=5674 RABBITMQ_SERVER_START_ARGS="-rabbitmq_management listener [{port,15674}]" RABBITMQ_NODENAME=rabbit2 rabbitmq-server start
```

##### 关闭节点

```bash
rabbitmqctl -n rabbit1 stop
rabbitmqctl -n rabbit2 stop
```

##### rabbit1作为主节点

```bash
rabbitmqctl -n rabbit1 stop_app
rabbitmqctl -n rabbit1 reset
rabbitmqctl -n rabbit1 start_app
```

##### rabbit2作为从节点

```bash
rabbitmqctl -n rabbit2 stop_app
rabbitmqctl -n rabbit2 reset
rabbitmqctl -n rabbit2 join_cluster rabbit1@'[主机名]'
rabbitmqctl -n rabbit2 start_app
```

##### 设置镜像队列

进入管理界面 -> Admin -> Policies -> Add / update a policy
Name: 随便输
Pattern: ^
ha-mode: all 

##### 安装HAProxy

1. 下载安装包

  ```bash
  yum install gcc vim wget
  wget http://www.haproxy.org/download/1.6/src/haproxy-1.6.5.tar.gz
  ```

2. 解压

  ```bash
  tar -zxvf haproxy-1.6.5.tar.gz -C /usr/local
  ```

3. 进入目录进行编译、安装

  ```bash
  cd /usr/local/haproxy-1.6.5
  make TARGET=linux31 PREFIX=/usr/local/haproxy
  make install PREFIX=/usr/local/haproxy
  mkdir /etc/haproxy
  ```

4. 赋权

  ```bash
  groupadd -r -g 149 haproxy
  useradd -g haproxy -r -s /sbin/nologin -u 149 haproxy
  ```

5. 创建HAProxy配置文件

  ```bash
  vim /etc/haproxy/haproxy.cfg
  ```

6. 配置HAProxy

  ```
  #logging options
  global 
  	log 127.0.0.1 local0 info
  	maxconn 5120
  	chroot /usr/local/haproxy
  	uid 99
  	gid 99
  	daemon
  	quiet
  	nbproc 20
  	pidfile /var/run/haproxy.pid
  
  defaults 
  	log global
  	mode tcp
  	option tcplog
  	option dontlognull
  	retries 3
  	option redispatch
  	maxconn 2000
  	contimeout 5s
  	clitimeout 60s
  	srvtimeout 15s
  #front-end Ip for consumers and producters
  
  listen rabbitmq_cluster
  	bind 0.0.0.0:5672
  	
  	mode tcp
  	#balance url_param userid
  	#balance url_param session_id check_post 64
  	#balance hdr(User-Agent)
  	#balance hdr(host)
  	#balance hdr(Host) use_domain_only
  	#balance rdp-cookie
  	#balance leastconn
  	#balance source //ip
  
  	balance roundrobin
  		server node1 127.0.0.1:5673 check inter 5000 rise 2 fall 2
  		server node2 127.0.0.1:5674 check inter 5000 rise 2 fall 2
  
  listen stats
  	bind 192.168.3.26:8100
  	mode http
  	option httplog
  	stats enable
  	stats uri /rabbitmq-stats
  	stats refresh 5s
  ```

7. 启动HAProxy负载

  ```bash
  /usr/local/haproxy/sbin/haproxy -f /etc/haproxy/haproxy.cfg
  ```

8. 查看haproxy进程状态

  ```bash
  ps -ef | grep haproxy
  ```

9. 对mq节点进行监控
    访问如下地址：http://192.168.3.26:8100/rabbitmq-stats，代码中访问mq集群地址，则变为访问haproxy地址：5672

  
