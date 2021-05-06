##### 下载PL/SQL Developer

官网下载地址：https://www.allroundautomations.com/try-it-free/

##### 下载Instant Client

官网下载地址：https://www.oracle.com/cn/database/technology/winx64soft.html

##### 配置PL/SQL Developer

配置 -> 首选项

Oracle 主目录 -> 填入解压后的instantclient_12_2文件夹路径

OCI 库 -> 填入解压后的instantclient_12_2文件夹下的oci.dll路径

##### 连接oracle

**直接登录**

用户名：system

密码：system

数据库：[ip地址]:1521/XE

连接为：Normal

**使用配置文件**

1. 在instantclient_12_2文件夹下新建config文件夹
2. 复制oracle服务器上的文件tnsnames.ora到config文件夹中，修改tnsnames.ora的HOST为服务器的ip地址
3. 设置windows系统环境变量，新建 TNS_ADMIN = [config文件夹路径]
4. 登录时数据库项可以不用手动填写，而是选择配置

##### 执行光标所在行SQL语句

配置 -> 首选项 -> SQL窗口 -> 自动选择语句

按F8就会执行光标所在行

##### 修改字符编码

设置windows系统环境变量，新建 NLS_LANG = AMERICAN_AMERICA.ZHS16GBK

默认字符编码格式可能导致中文字符占用多个字节

