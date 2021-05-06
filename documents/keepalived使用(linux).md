##### 下载安装

下载地址：https://keepalived.org

下载文件名：keepalived-2.2.2.tar.gz

```shell
# 创建keepalived目录
mkdir keepalived
# 解压安装包
tar -zxvf keepalived-2.2.2.tar.gz -C keepalived/
# 对keepalived进行配置、编译和安装
cd keepalived/keepalived-2.2.2
./configure --sysconf=/etc --prefix=/usr/local
make && make install
```

##### 修改配置文件

```shell
# 先备份默认配置文件
cd /etc/keepalived
cp keepalived.conf keepalived.conf.bak
vim /etc/keepalived/keepalived.conf
```

##### VRRP配置

Master服务器和Backup服务器的vrrp_instance的state都配置成BACKUP，让它们通过priority来竞争，避免master宕机后又恢复导致第二次主备切换

```
# Master服务器

global_defs {
   notification_email {
     acassen@firewall.loc
     failover@firewall.loc
     sysadmin@firewall.loc
   }
   notification_email_from [user_email]
   smtp_server 192.168.200.1
   smtp_connect_timeout 30
   router_id [keepalived_master_server_name]
   vrrp_skip_check_adv_addr
   vrrp_strict
   vrrp_garp_interval 0
   vrrp_gna_interval 0
}

vrrp_instance VI_1 {
    state BACKUP
    interface [network_hardware_logical_name(ens33)]
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        [virtual_ip]
    }
}
```

```
# Backup服务器

global_defs {
   notification_email {
     acassen@firewall.loc
     failover@firewall.loc
     sysadmin@firewall.loc
   }
   notification_email_from [user_email]
   smtp_server 192.168.200.1
   smtp_connect_timeout 30
   router_id [keepalived_backup_server_name]
   vrrp_skip_check_adv_addr
   vrrp_strict
   vrrp_garp_interval 0
   vrrp_gna_interval 0
}

vrrp_instance VI_1 {
    state BACKUP
    interface [network_hardware_logical_name(ens33)]
    virtual_router_id 51
    priority 90
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        [virtual_ip]
    }
}
```

##### 启动keepalived

```shell
cd /usr/local/sbin
./keepalived
# 查看虚拟IP
ip a
```

##### 关闭keepalived

```shell
ps -ef | grep keepalived
kill -9 [pid]
```

##### 脚本实现监控和主备切换

编写脚本ck_nginx.sh

```sh
#! /bin/bash
num=`ps -C nginx --no-header | wc -l`
if [ $num -eq 0 ]; then
  /usr/local/nginx/sbin/nginx
  sleep 2
  if [ `ps -C nginx --no-header | wc -l` -eq 0 ]; then
    killall keepalived
  fi
fi
```

为脚本文件设置权限

```shell
chmod 755 ck_nginx.sh
```

在keepalived配置文件中添加脚本

```
vrrp_script ck_nginx {
	script "[script_path]" # 脚本路径
	interval [seconds] # 执行时间间隔
	weight -20 # 动态调整vrrp_instance的优先级，宕机后priority减20
}

vrrp_instance VI_1 {
    state BACKUP
    interface [network_hardware_logical_name(ens33)]
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
        auth_type PASS
        auth_pass 1111
    }
    virtual_ipaddress {
        [virtual_ip]
    }
    track_script {
    	ck_nginx
    }
}
```

