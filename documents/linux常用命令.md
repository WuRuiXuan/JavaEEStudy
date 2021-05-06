##### 查看当前目录下的文件

```bash
ls # 显示非隐藏文件
ls -a # 显示所有文件，包括隐藏文件
ls -l # 显示文件详细信息
ll # 等同于 ls -l
```

##### 切换目录

```shell
cd [目录名/路径]
cd .. # 退回上级目录
cd ~ # 切换到用户主目录
cd - # 切换到上次所在目录
cd / # 切换到系统根目录
pwd # 查看当前目录路径
```

##### 创建/移除目录

```shell
mkdir [目录名] # 创建目录
rmdir [目录名] # 移除目录
mkdir -p [目录名/路径] # 创建多级目录
```

##### 查看文件

```shell
cat [文件名/路径] # 查看文件内容，完整显示
more [文件名/路径] # 查看文件内容，分段显示，空格键显示下一个画面，回车键显示下一行内容，q键退出查看
less [文件名/路径] # 效果同more，新增上下键查看
tail -[数字] [文件名/路径] # 查看文件最后几行
tail -f [文件名/路径] # 追踪文件变化，动态展示新增的内容
```

##### 文件复制/移动

```shell
cp [文件名/路径] [目录名/路径] # 复制文件到指定目录
cp [文件名/路径] [文件名/路径] # 复制文件到指定目录并改名
mv [文件名/路径] [目录名/路径] # 移动文件到指定目录
```

##### 文件/目录删除

```shell
rm [文件名/路径] # 询问是否删除文件（y/n）
rm -r [目录名/路径] # 询问是否删除目录（y/n）
rm -f [文件名/路径] # 不询问直接删除文件
rm -rf [目录名/路径] # 不询问直接删除目录
rm -rf /* # 删除当前目录下所有文件及目录
```

##### 打包/解压

```shell
tar -cvf [文件名/路径（.tar）] ./* # 打包当前目录
tar -zcvf [文件名/路径（.tar.gz）] ./* # 打包当前目录并压缩
tar -xvf [文件名/路径（.tar）] # 解压到当前目录
tar -zxvf [文件名/路径（.tar.gz）] # 解压到当前目录
tar -zxvf [文件名/路径（.tar.gz）] -C [目录名/路径] # 解压到指定目录
```

##### 查找文件/文件内容

```shell
find [目录名/路径] -name [文件名] # 查找指定目录指定名称的文件
grep [文件内容] [文件名/路径] --color # 查找文件内容并高亮显示关键字
```

##### 创建空文件

```shell
touch [文件名/路径]
```

##### 清屏

```shell
clear
```

##### 拷贝文件内容

```shell
cat [文件名/路径] > [文件名/路径] # 拷贝文件内容并覆盖保存到另一文件中
cat [文件名/路径] >> [文件名/路径] # 拷贝文件内容并追加保存到另一文件中
```

##### 进程管理

```shell
ps -ef # 查看所有进程
ps -ef | grep [进程名] # 查找指定名称的进程
kill -9 [进程id] # 杀死进程
```

##### 修改文件访问权限

```shell
chmod 755 [文件名/路径]
```

##### 修改hostname

```shell
vim /etc/sysconfig/network
```

##### 修改ip地址

```shell
vim /etc/sysconfig/network-scripts/ifcfg-eth0
```

##### 域名映射

```shell
vim /etc/hosts
```

##### 使用root账户自动登录

```bash
vim /etc/gdm/custom.conf
```

在[daemon]下增加

```
AutomaticLoginEnable=True
AutomaticLogin=root
```

保存文件，然后重启查看效果

##### 查看/卸载/安装程序

```bash
rpm -qa | grep [程序名] # 查看
rpm -e --nodeps [查看的结果] # 卸载
rpm -ivh [文件名/路径（.rpm）] # 安装
```

##### 修改系统环境变量

```shell
vim /etc/profile
source /etc/profile # 加载新环境变量
```

##### 关闭防火墙

```bash
service iptables stop # linux
systemctl stop firewalld # centos7
```

##### 禁止防火墙开机启动

```bash
chkconfig iptables off # linux
systemctl disable firewalld # centos7
```

##### 查看防火墙运行状态

```shell
systemctl status firewalld # centos7
```

##### 关闭系统自动更新服务

```shell
vi /etc/yum
yum install yum-cron -y
vi /etc/yum/yum-cron.conf
```

修改内容

```
update_messages = no
download_updates = no
```

##### 查看系统位数

```shell
getconf LONG_BIT
```

##### 关闭selinux

查看selinux运行状态

```shell
sestatus
```

修改selinux配置文件

```shell
vim /etc/selinux/config
```

修改内容

```
SELINUX=disabled
```

保存退出，重启系统

##### 安装tree工具

```shell
# 安装
yum install -y tree
# 使用
tree [dir_path]
```

##### 修改host文件

```shell
# 用户在访问域名时，系统会先从hosts文件中寻找对应的ip地址，一旦找到则直接访问该ip地址，未找到则提交DNS域名解析服务器进行ip地址的解析
# 配置格式（每一行）：[ip] [域名]
vim /etc/hosts
```

##### 查看网络硬件信息

```shell
lshw -class network
```

