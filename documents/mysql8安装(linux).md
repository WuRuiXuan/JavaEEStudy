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

下载的文件名为：mysql-8.0.22-1.el7.x86_64.rpm-bundle.tar

##### 解压mysql安装包

```shell
tar -xvf mysql-8.0.22-1.el7.x86_64.rpm-bundle.tar
```

##### 安装mysql

cd 到解压目录

必要安装（注意顺序）

```bash
rpm -ivh mysql-community-common-8.0.22-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-8.0.22-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-8.0.22-1.el7.x86_64.rpm
rpm -ivh mysql-community-server-8.0.22-1.el7.x86_64.rpm
```

非必要安装（注意顺序）

```bash
rpm -ivh mysql-community-libs-compat-8.0.22-1.el7.x86_64.rpm
rpm -ivh mysql-community-embedded-compat-8.0.22-1.el7.x86_64.rpm
rpm -ivh mysql-community-devel-8.0.22-1.el7.x86_64.rpm
rpm -ivh mysql-community-test-8.0.22-1.el7.x86_64.rpm
```

提示：mariadb-libs 被 mysql-community-libs-8.0.22-1.el7.x86_64 取代

```bash
yum remove mysql-libs -y
```

提示：pkgconfig(openssl) 被 mysql-community-devel-8.0.22-1.el7.x86_64 需要

```bash
yum install pkgconfig -y
yum install openssl-devel.x86_64 openssl.x86_64 -y
```

提示：perl(Data::Dumper) 被 mysql-community-test-8.0.22-1.el7.x86_64 需要

```bash
yum install autoconf -y
```

提示：perl(JSON) 被 mysql-community-test-8.0.22-1.el7.x86_64 需要

```bash
yum install perl.x86_64 perl-devel.x86_64 -y
yum install perl-JSON.noarch -y
```

提示：perl(Test::More) 被 mysql-community-test-8.0.22-1.el7.x86_64 需要

```bash
yum install perl-Test-Simple.noarch -y
```

##### 初始化数据库

```bash
mysqld --initialize --console
```

##### 目录授权

```bash
chown -R mysql:mysql /var/lib/mysql/
```

##### 启动mysql服务

```bash
systemctl start mysqld
```

##### 停止mysql服务

```bash
service mysqld stop
```

##### 查看mysql服务的状态

```bash
service mysqld status
```

##### 查看初始化密码

```bash
cat /var/log/mysqld.log
```

##### 使用密码登录

```bash
mysql -uroot -p
```

##### 修改账户密码加密规则并更新用户密码

解决Authentication plugin 'caching_sha2_password' cannot be loaded

```mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'password' PASSWORD EXPIRE NEVER;
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
FLUSH PRIVILEGES;
alter user 'root'@'localhost' identified by '[新密码]';
```

##### 查看所有用户是否可以远程连接

```mysql
show databases;
use mysql;
select host, user, authentication_string, plugin from user;
```

host为localhost，说明用户只能本地连接mysql服务

##### 允许root用户远程登录

```mysql
update user set host="%" where user='root';
flush privileges;
```

注意以后操作root账号要写'root'@'%'

##### 查看所有用户

```mysql
SELECT user, host FROM mysql.user;
```

##### 忘记root密码

停止mysql服务

```shell
net stop mysql
```

使用无验证方式启动mysql服务

```shell
mysqld --skip-grant-tables
```

新建命令窗口登录mysql

```shell
mysql
```

修改密码

```mysql
USE mysql;
update user set password=password('root') where user='root';
```

手动结束进程mysqld.exe

启动mysql服务

```shell
net start mysql
```

登录mysql

```shell
mysql -uroot -proot
```

