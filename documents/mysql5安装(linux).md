##### 检查是否有自带的mysql

```bash
rpm -qa | grep mysql
```

##### 卸载自带的mysql

```bash
rpm -e --nodeps [自带的mysql(mysql-libs-xxx)]
```

##### 下载mysql安装包

官网下载：https://downloads.mysql.com/archives/community/

下载选择 Red Hat Enterprise Linux / Oracle Linux 7，下载 RPM Bundle

下载的文件名为：mysql-5.7.32-1.el7.x86_64.rpm-bundle.tar

##### 解压mysql安装包

```shell
tar -xvf mysql-5.7.32-1.el7.x86_64.rpm-bundle.tar
```

##### 安装mysql

cd 到解压目录

必要安装

```shell
rpm -ivh mysql-community-server-5.7.32-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-5.7.32-1.el7.x86_64.rpm
```

##### 启动mysql服务

```shell
service mysql start
```

##### 查看mysql初始密码

```shell
cat /root/.mysql_secret
```

##### 登录mysql

```shell
mysql -uroot -p
```

##### 设置root用户密码

```mysql
set password = password('root');
```

##### 允许远程访问

```mysql
grant all privileges on *.* to 'root'@'%' identified by 'root';
flush privileges;
```

