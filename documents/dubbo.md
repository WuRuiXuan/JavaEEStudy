##### dubbo架构

![dubbo-architecture](https://gitee.com/wuruixuan/markdown-images/raw/master/images/dubbo-architecture.jpg)

节点角色说明：

- Provider：暴露服务的提供方
- Consumer：调用远程服务的消费方
- Registry：服务注册与发现中心
- Monitor：统计服务的调用次数和调用时间的监控中心
- Container：服务运行容器（通常是 Spring ）

##### dubbo服务路由

服务路由包含一条路由规则，路由规则决定了服务消费者的调用目标，即规定了服务消费者可调用哪些服务提供者

dubbo 提供三种服务路由：

- 条件路由 ConditionRouter
- 脚本路由 ScriptRouter
- 标签路由 TagRouter

##### dubbo条件路由

格式：

- [服务消费者匹配条件] => [服务提供者匹配条件]
- 如果服务消费者匹配条件为空，表示不对服务消费者进行限制
- 如果服务提供者匹配条件为空，表示对某些服务消费者禁用服务

常见路由配置

| 场景       | 配置                                                         | 说明                                                         |
| ---------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 白名单     | host!=[consumer_ip1],[consumer_ip2] =>                       | 只有 consumer_ip1、consumer_ip2 能调用服务                   |
| 黑名单     | host=[consumer_ip1],[consumer_ip2] =>                        | 只有 consumer_ip1、consumer_ip2 不能调用服务                 |
| 读写分离   | method=find,list,get,is => host=[provider_ip1],[provider_ip2]<br />method!=find,list,get,is => host=[provider_ip3],[provider_ip4] | 只要消费请求是以 find、list、get、is 开头的（读请求），就交给provider_ip1、provider_ip2来处理<br />只要消费请求不是以 find、list、get、is 开头的（增删改请求），就交给provider_ip3、provider_ip4来处理 |
| 前后台分离 | application=front => host=[provider_ip1],[provider_ip2]<br />application!=front => host=[provider_ip3],[provider_ip4] | 前台消费请求交给provider_ip1、provider_ip2来处理<br />后台消费请求交给provider_ip3、provider_ip4来处理 |

##### dubbo集群容错

- Failover Cluster
  失败自动切换（缺省），当出现失败，重试其它服务器，通常用于读操作，但重试会带来更长延迟，可通过 retries="2" 来设置重试次数（不含第一次）
  重试次数配置：<dubbo:service retries="2"/> 或 <dubbo:reference retries="2"/>
- Failfast Cluster
  快速失败，只发起一次调用，失败立即报错，通常用于非幂等性的写操作，比如新增记录
- Failsafe Cluster
  失败安全，出现异常时，直接忽略，通常用于写入日志等操作
- Failback Cluster
  失败自动恢复，后台记录失败请求，定时重发，通常用于消息通知操作
- Forking Cluster
  并行调用多个服务器，只要一个成功即返回，通常用于实时性要求较高的读操作，但需要浪费更多服务资源，可通过 forks="2" 来设置最大并行数
- Broadcast Cluster
  广播调用所有提供者，逐个调用，任意一台报错则报错，通常用于通知所有提供者更新缓存或日志等本地资源信息

##### dubbo负载均衡

- Random LoadBalance（缺省）：按照权重设置随机概率，无状态
- RoundRobin LoadBalance：轮询，有状态
- LeastActive LoadBalance：最少活跃数随机，方法维度地统计服务调用数，让处理能力慢的机器接收到更少的请求
- ConsistentHash LoadBalance：一致性 Hash，让相同参数的请求总是发送到一台机器上

##### dubbo管理控制台

安装步骤：

1. 将 dubbo-admin-2.6.0.war 复制到 tomcat 的 webapps 目录下
2. 启动 tomcat
3. 修改 webapps\dubbo-admin-2.6.0\WEB-INF\dubbo.properties 文件（配置 zookeeper 服务地址、访问控制台的用户和密码）
4. 重启 tomcat

访问地址：http://localhost:8080/dubbo-admin-2.6.0/