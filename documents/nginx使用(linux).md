##### 安装GCC编译器

```shell
yum install -y gcc
gcc --version
```

##### 安装PCRE

```shell
yum install -y pcre pcre-devel
rpm -qa pcre pcre-devel
```

##### 安装zlib

```shell
yum install -y zlib zlib-devel
rpm -qa zlib zlib-devel
```

##### 安装OpenSSL

```shell
yum install -y openssl openssl-devel
rpm -qa openssl openssl-devel
```

##### 安装以上所有

```shell
yum install -y gcc pcre pcre-devel zlib zlib-devel openssl openssl-devel
```

##### nginx源码安装

下载安装包：https://nginx.org/en/download.html

下载文件名为：nginx-1.16.1.tar.gz

安装nginx（注意先关闭selinux）

```shell
# 进入用户主目录
cd ~
# 创建安装目录
mkdir -p nginx/core
# 把安装包移动到目录中
mv nginx-1.16.1.tar.gz nginx/core
# 进入安装目录
cd nginx/core
# 解压安装包
tar -zxvf nginx-1.16.1.tar.gz
# 进入nginx目录
cd nginx-1.16.1
# 执行配置
./configure
# 执行编译和安装（安装目录为：/usr/local/nginx）
make && make install
```

启动nginx

```shell
cd /usr/local/nginx/sbin
./nginx
```

##### yum安装

https://nginx.org/en/linux_packages.html#RHEL-CentOS

##### 查询nginx目录

```shell
whereis nginx
```

##### 查看nginx版本及配置信息

```shell
# 查看nginx版本及配置信息
./nginx -V
# 查看nginx命令帮助
./nginx -h
# 检测配置文件有没有语法错误
./nginx -t
# 测试期间只输出错误信息（-tq）
./nginx -q
# signal
# stop 快速关闭
# quit 优雅关闭
# reopen 重新打开日志文件
# reload 重读配置文件并使用服务对新配置项生效
./nginx -s [signal]
# 设置nginx安装目录路径（/usr/local/nginx）
./nginx -p [path]
# 设置nginx配置文件路径（conf/nginx.conf）
./nginx -c [path]
# 指定参数配置
./nginx -g "[name] [value];"
```

##### 卸载nginx

```shell
# 将nginx进程关闭
./nginx -s stop
# 将安装的nginx删除
rm -rf /usr/local/nginx
# 进入安装目录
cd ~/nginx/core/nginx-1.16.1
# 将安装包之前的编译环境清除掉
make clean
```

##### 查看nginx进程信息

```shell
ps -ef | grep nginx
```

##### 查看master进程id

```shell
cat /usr/local/nginx/logs/nginx.pid # 新master的pid
cat /usr/local/nginx/logs/nginx.pid.oldbin # 老master的pid
```

##### 信号控制

```shell
# signal
# TERM 立即关闭整个服务
# QUIT 优雅地关闭整个服务
# HUP 重读配置文件并使用服务对新配置项生效
# USR1 重新打开日志文件，可以用来进行日志切割
# USR2 平滑升级到最新版的nginx（会有新旧两个master进程）
# WINCH 所有子进程不再接收处理新连接，相当于给worker进程发送QUIT指令
kill -[signal] [PID]
```

##### nginx服务器版本升级

将新版本安装包解压，进入安装目录，进行参数配置和编译，不需要安装

```shell
./configure
make
```

进入老版本nginx的sbin目录，将nginx文件进行备份

```shell
cd /usr/local/nginx/sbin
mv nginx nginxold
```

进入新版本nginx安装目录的objs目录，将nginx文件拷贝到老版本nginx的sbin目录下

```shell
cp nginx /usr/local/nginx/sbin
```

方案一：使用服务信号升级

```shell
kill -USR2 `cat /usr/local/nginx/logs/nginx.pid`
kill -QUIT `cat /usr/local/nginx/logs/nginx.pid.oldbin`
```

方案二：使用安装目录的make命令升级

```shell
# 进入新版本nginx安装目录，执行以下命令
make upgrade
```

##### nginx配置成系统服务

创建service文件

```shell
vim /usr/lib/systemd/system/nginx.service
```

添加以下内容

```
[Unit]
Description=nginx web service
Documentation=http://nginx.org/en/docs/
After=network.target

[Service]
Type=forking
PIDFile=/usr/local/nginx/logs/nginx.pid
ExecStartPre=/usr/local/nginx/sbin/nginx -t -c /usr/local/nginx/conf/nginx.conf
ExecStart=/usr/local/nginx/sbin/nginx
ExecReload=/usr/local/nginx/sbin/nginx -s reload
ExecStop=/usr/local/nginx/sbin/nginx -s stop
PrivateTmp=true

[Install] 
WantedBy=default.target
```

设置访问权限

```shell
chmod 755 /usr/lib/systemd/system/nginx.service
```

使用系统命令操作nginx服务

```shell
# 启动
systemctl start nginx
# 停止
systemctl stop nginx
# 重启
systemctl restart nginx
# 重新加载配置文件
systemctl reload nginx
# 查看nginx状态
systemctl status nginx
# 开机启动
systemctl enable nginx
```

##### nginx配置系统环境变量

修改环境变量文件

```shell
vim /etc/profile
```

在末尾添加

```
export PATH=$PATH:/usr/local/nginx/sbin
```

使之立即生效

```shell
source /etc/profile
```

检查是否配置成功

```shell
nginx -v
```

##### 查看nginx错误日志

```shell
tail -f /usr/local/nginx/logs/error.log
```

##### 查看/修改nginx配置文件

```shell
# 查看
cat /usr/local/nginx/conf/nginx.conf
# 修改
vim /usr/local/nginx/conf/nginx.conf
# 测试修改是否有误
./nginx -t
# 使修改生效
./nginx -s reload
```

##### 使用默认nginx配置文件

```shell
cd /usr/local/nginx/conf
mv nginx.conf nginx_old.conf
cp nginx.conf.default nginx.conf
```

##### 查看configure命令属性

```shell
# 列出所有配置指令
./configure --help
# 使用指令配置
./configure --[name1]=[value1] --[name2]=[value2] ...
```

##### nginx配置-全局

| 配置项                       | 作用                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| user [用户名]                | 设置用户（创建用户命令：useradd [用户名]）                   |
| master_process [on\|off]     | 是否开启worker进程（生效需要重启nginx服务）                  |
| worker_processes [num\|auto] | worker进程的数量，建议将该值与服务器CPU内核数保持一致（生效需要重启nginx服务） |
| daemon [on\|off]             | 设置nginx是否以守护进程的方式启动<br />守护进程是linux后台执行的一种服务进程，特点是独立于控制终端，不会随着终端关闭而停止 |
| pid [path]                   | 配置nginx当前master进程的pid文件路径                         |
| error_log [path] [level]     | 配置nginx的错误日志存放路径<br />日志级别（level）：debug\|info\|notice\|warn\|error\|crit\|alert\|emerg，建议不要设置成info以下的等级，否则会影响性能 |
| include [path]               | 引入其它配置文件                                             |

##### nginx配置-events

| 配置项                      | 作用                                                         | 位置   |
| --------------------------- | ------------------------------------------------------------ | ------ |
| accept_mutex [on\|off]      | on：当客户端发来一个请求时，对休眠的worker进程挨个唤醒接收（性能开销小，效率低）<br />off：当客户端发来一个请求时，同时唤醒多个worker进程，共同竞争来获取这个请求（性能开销大，效率高） | events |
| multi_accept [on\|off]      | 是否允许一个worker进程同时接收多个网络连接，默认值为off，建议设置为on | events |
| worker_connections [number] | 单个worker进程最大的连接数（不能大于系统支持的最大文件句柄数量） | events |
| use [method]                | nginx服务器选择哪种事件驱动来处理网络消息<br />事件驱动（method）：select\|poll\|epoll\|kqueue，建议设置为epoll | events |

##### nginx配置-http

| 配置项                                  | 作用                                                         | 位置                   |
| --------------------------------------- | ------------------------------------------------------------ | ---------------------- |
| default_type [mime_type]                | nginx响应请求默认的MIME类型                                  | http、server、location |
| access_log [path] [format_name]         | 设置日志文件的位置及日志的输出格式<br />name：声明的日志输出格式名 | http、server、location |
| log_format [format_name]  '[text]'      | 声明日志的输出格式<br />name：格式名（自定义）               | http                   |
| keepalive_timeout [time(s\|m\|h\|d\|y)] | 设置长连接（保持TCP连接以复用）的超时时间                    | http、server、location |
| keepalive_request [number]              | 设置长连接使用的次数（使用多少次后断开不再复用）             | http、server、location |
| sendfile [on\|off]                      | 设置nginx服务器是否使用sendfile（零拷贝技术）传输文件，该属性可以大大提高nginx处理静态资源的性能，默认值为on | http、server、location |
| tcp_nopush [on\|off]                    | 在sendfile打开的状态下才会生效，用来提升网络包的传输效率，默认值为off | http、server、location |
| tcp_nodeplay [on\|off]                  | 在keepalive连接开启的情况下才会生效，用来提高网络包传输的实时性，默认值为on | http、server、location |

##### nginx配置-server和location

| 配置项                                | 作用                                                         | 位置                   |
| ------------------------------------- | ------------------------------------------------------------ | ---------------------- |
| listen [ip:port\|port] default_server | 设置监听端口<br />default_server（可选）：如果配置了多个server，则默认将第一个顺序的 server 设为默认服务，配置了default_server则可以直接指定该server为默认服务 | server                 |
| server_name [name1] [name2] ...       | 设置虚拟主机服务名称<br />匹配顺序：<br />1. 准确匹配server_name<br />2. 通配符在开始时匹配server_name成功<br />3. 通配符在结束时匹配server_name成功<br />4. 正则表达式匹配server_name成功<br />5. 被默认的default_server处理，如果没有指定默认找第一个server | server                 |
| location [uri] {...}                  | 设置请求的URI                                                | server、location       |
| root [path]                           | 设置nginx服务器查找资源的根目录路径<br />资源访问路径 = root路径 + location路径 | http、server、location |
| alias [path]                          | 用来更改location的URI（location路径与真实资源路径不一致时）<br />资源访问路径 = alias路径（注意末尾加/） | location               |
| index [file1] [file2] ...             | 设置网站的默认首页（依次查找）                               | http、server、location |
| error_page [code1] [code2] ... [uri]  | 设置网站的错误页面<br />[code1] [=code2]：如果服务器返回code1，则使用code2替代code1返回 | http、server、location |
| server_name_in_redirect [on\|off]     | 设置重定向的地址是否以server_name来拼接，设置成off则会以服务器IP地址来拼接，nginx版本0.8.48之前默认是on，之后默认是off | http、server、location |
| port_in_redirect [on\|off]            | 设置重定向的端口是否按照当前正在监听的端口，设置成off则会按照默认的80端口，默认值为on | http、server、location |

##### nginx配置-gzip

| 配置项                                   | 作用                                                         | 位置                   |
| ---------------------------------------- | ------------------------------------------------------------ | ---------------------- |
| gzip [on\|off]                           | 开启或关闭gzip压缩功能                                       | http、server、location |
| gzip_types [mime_type1] [mime_type2] ... | 根据指定的MIME类型选择性地开启gzip压缩功能                   | http、server、location |
| gzip_comp_level [1-9]                    | 设置gzip压缩程度（1-9，数字越大，压缩程度越高，效率越低，时间越长，建议设置为6） | http、server、location |
| gzip_vary [on\|off]                      | 设置使用gzip压缩的数据是否携带"Vary:Accept-Encoding"的响应头，作用是告诉接收方，数据经过了gzip压缩处理 | http、server、location |
| gzip_buffers [number] [size(k)]          | 设置gzip压缩缓冲区的大小（申请number个大小为size的内存空间，建议不设置，使用默认值） | http、server、location |
| gzip_disable "[regex]"                   | 根据浏览器标志（user-agent）来设置，排除不支持gzip的浏览器   | http、server、location |
| gzip_http_version [version]              | 指定使用gzip压缩的http协议最低版本，建议使用默认值           | http、server、location |
| gzip_min_length [length]                 | 设置使用gzip压缩的数据的大小（Content-Length）的最小值，建议设置为1K或以上 | http、server、location |
| gzip_proxied [option]                    | 设置如何对服务端返回的结果进行gzip压缩<br />off：关闭nginx服务器对后台服务器返回结果的gzip压缩<br />expired：启用压缩，如果header头中包含"Expires"头信息<br />no-cache：启用压缩，如果header头中包含"Cache-Control:no-cache"头信息<br />no-store：启用压缩，如果header头中包含"Cache-Control:no-store"头信息<br />private：启用压缩，如果header头中包含"Cache-Control:private"头信息<br />no_last_modified：启用压缩，如果header头中不包含"Last-Modified"头信息<br />no_etag：启用压缩，如果header头中不包含"ETag"头信息<br />auth：启用压缩，如果header头中包含"Authorization"头信息<br />any：无条件启用压缩 | http、server、location |
| gzip_static [on\|off\|always]            | 解决sendfile开启和gzip压缩共存的问题<br />开启需添加模块http_gzip_static_module，并关闭gzip<br />资源文件需要使用gzip命令压缩成gz文件 | http、server、location |

##### nginx配置-浏览器头信息

| 配置项                                                       | 作用                                                         | 位置                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------- |
| expires [time(s\|m\|h\|d\|y)]<br />expires [epoch\|max\|off] | 设置"Expires"和"Cache-Control"<br />time：指定过期时间（Cache-Control: max-age=[time换算成秒]），如果是负数，表示永远过期（Cache-Control: no-cache）<br />epoch：过期时间为永远过期（Expires: 1 January 1970 00:00:01 GMT; Cache-Control: no-cache）<br />max：过期时间为缓存最大周期10年（Expires: 31 December 2037 23:59:59 GMT; Cache-Control: max-age=315360000）<br />off：默认值，不缓存 | http、server、location |
| add_header [name] [value] always                             | 添加指定的响应头<br />always（可选）：不管浏览器是否支持该响应头，都默认添加 | http、server、location |
| valid_referers none blocked [server_name1\|regex1] [server_name2\|regex2] ... | 设置判断"Referer"是否有效的条件，有一个匹配到了就有效（$invalid_referer=0），没有匹配到就无效（$invalid_referer=1），匹配过程中不区分大小写<br />none（可选）：Referer为空<br />blocked（可选）：Referer不以http/https开头（允许不带协议的请求访问资源） | server、location       |

##### nginx配置-rewrite

| 配置项                                                       | 作用                                                         | 位置                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------------- |
| set $[variable_name] [variable_value]                        | 设置一个新的变量                                             | server、location、if       |
| if ([condition]) {...}                                       | 条件判断<br />! 否定<br />= 相等<br />~ 匹配<br />-f 文件存在<br />-d 目录存在<br />-e 目录或文件存在<br />-x 文件可执行 | server、location           |
| break                                                        | 所属块里后面的配置代码都不会执行                             | server、location、if       |
| return [code] '[text]'<br />return [code] [url]<br />return [url] | 完成对请求的处理，直接向客户端返回                           | server、location、if       |
| rewrite [regex] [replacement] [last\|break\|redirect\|permanent] | 通过匹配正则表达式来改变URL、URI<br />last：如果匹配成功，就在全局location块中寻找重写后的URI来处理<br />break：如果匹配成功，就在当前location块中寻找重写后的URI来处理<br />redirect：如果匹配成功，就临时重定向到重写的URI，状态码为302<br />permanent：如果匹配成功，就永久重定向到重写的URI，状态码为301 | server、location、if       |
| rewrite_log [on\|off]<br />error_log [path] [level]          | 是否输出URL重写日志，开启后，URL重写的日志将以notice级别输出到error_log配置的日志文件中 | http、server、location、if |

##### nginx配置-反向代理

| 配置项                                                       | 作用                                                         | 位置                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------- |
| proxy_pass [url]                                             | 设置被代理服务器地址<br />url末尾不加/，则会将location的URI拼接到url后面，加了/则不会 | location               |
| proxy_set_header [field] [value]                             | 更改客户端请求的头信息，将新的请求头发送给代理服务器<br />头信息获取：$http_[field] | http、server、location |
| proxy_redirect [url] [replacement]<br />proxy_redirect [default\|off] | 重置头信息中的"Location"和"Refresh"<br />避免重定向在Location中暴露被代理服务器的地址<br />default：将location块的uri作为replacement<br />off：关闭该功能 | http、server、location |
| proxy_buffering [on\|off]                                    | 开启或关闭代理服务器的缓冲区                                 | http、server、location |
| proxy_buffers [number] [size]                                | 指定单个连接从代理服务器读取响应的缓存区的个数和大小         | http、server、location |
| proxy_buffer_size [size]                                     | 设置从被代理服务器获取的第一部分响应数据的大小，建议设置为与proxy_buffers中的size一致 | http、server、location |
| proxy_busy_buffers_size [size]                               | 限制同时处于BUSY状态的缓冲总大小                             | http、server、location |
| proxy_temp_path [path]                                       | 设置当缓冲区存满后，响应数据临时存放在磁盘上的文件路径       | http、server、location |
| proxy_temp_file_write_size [size]                            | 设置磁盘上缓冲文件的大小                                     | http、server、location |

##### nginx配置-SSL

| 配置项                                                       | 作用                                                         | 位置         |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------ |
| ssl [on\|off]<br />listen 443 ssl                            | 开启HTTPS<br />开启需要添加模块http_ssl_module               | http、server |
| ssl_certificate [file(.crt\|.pem)]                           | 为当前虚拟主机指定一个PEM证书                                | http、server |
| ssl_certificate_key [file(.key)]                             | 指定PEM secret key文件的路径                                 | http、server |
| ssl_session_cache [off\|none]<br />ssl_session_cache builtin:[size]<br />ssl_session_cache shared:[name]:[size] | 设置用于SSL会话的缓存，默认值为none<br />off：禁用会话缓存<br />none：客户端可以使用会话缓存，但nginx服务器不使用<br />builtin：内置OpenSSL缓存，只在一个worker进程中使用<br />shared：所有worker进程之间共享缓存，缓存的信息用name和size来指定 | http、server |
| ssl_session_timeout [time]                                   | 设置缓存中会话参数的使用时间                                 | http、server |
| ssl_ciphers [cipher]                                         | 设置ssl证书所需的加密套件<br />可通过openssl ciphers查看加密套件列表 | http、server |
| ssl_prefer_server_ciphers [on\|off]                          | 设置服务器密码是否优先客户端密码                             | http、server |

##### nginx配置-七层负载均衡

| 配置项                 | 作用                 | 位置     |
| ---------------------- | -------------------- | -------- |
| upstream [name] {...}  | 定义一组服务器       | http     |
| server [name] [params] | 指定服务器名称和参数 | upstream |

##### nginx配置-缓存

| 配置项                                                       | 作用                                                         | 位置                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------- |
| proxy_cache_path [dir_path] levels=[num:num] keys_zone=[zone_name]:[zone_size] inactive=[time] max_size=[size] | 设置缓存文件的存放路径<br />levels：缓存空间对应的目录，最多可设置3层，每层取值为1或2（1:2表示有两层目录，目录名第一层为1个字符，第二层为2个字符）<br />keys_zone：缓存区名称和大小，1MB大概能存8000个key<br />inactive：缓存的数据多长时间未被访问就被删除<br />max_size：最大缓存空间，如果缓存空间存满，默认会覆盖缓存时间最长的资源 | http                   |
| proxy_cache [zone_name\|off]                                 | 开启或关闭代理缓存                                           | http、server、location |
| proxy_cache_key [key]                                        | 设置web缓存的key值，nginx会根据key的MD5密文存缓存，默认值$scheme$proxy_host$request_uri | http、server、location |
| proxy_cache_valid [code1] [code2] ... [time]                 | 对不同返回状态码的URL设置不同的缓存时间<br />proxy_cache_valid any 1m：对所有响应状态码的URL都设置1分钟缓存 | http、server、location |
| proxy_cache_min_uses [number]                                | 设置资源被访问多少次后被缓存                                 | http、server、location |
| proxy_cache_methods [method1] [method2] ...                  | 设置缓存哪些HTTP方法（GET\|HEAD\|POST）                      | http、server、location |
| proxy_no_cache [string1] [string2] ...                       | 定义不将数据缓存的条件（MISS）<br />例：$cookie_nocache $arg_nocache $arg_comment<br />cookie带有参数nocache以及访问URL带有参数nocache或comment，将不缓存（参数不为0） | http、server、location |
| proxy_cache_bypass [string1] [string2] ...                   | 定义不从缓存中获取数据的条件（BYPASS）<br />例：$cookie_nocache $arg_nocache $arg_comment | http、server、location |

##### nginx配置-下载站点

| 配置项                                    | 作用                                                         | 位置                   |
| ----------------------------------------- | ------------------------------------------------------------ | ---------------------- |
| autoindex [on\|off]                       | 启用或禁用目录列表输出                                       | http、server、location |
| autoindex_exact_size [on\|off]            | 是否在目录列表显示文件的详细大小<br />on：显示确切大小，单位是bytes<br />off：显示大概大小，单位是KB、MB或GB | http、server、location |
| autoindex_format [html\|xml\|json\|jsonp] | 设置目录列表的格式                                           | http、server、location |
| autoindex_localtime [on\|off]             | 是否在目录列表上显示当地时间<br />on：显示文件的服务器时间<br />off：显示GMT时间 | http、server、location |

##### nginx配置-用户认证

| 配置项                      | 作用                                                         | 位置                                 |
| --------------------------- | ------------------------------------------------------------ | ------------------------------------ |
| auth_basic [string\|off]    | 使用“HTTP基本身份验证”协议启用用户名和密码的验证<br />开启后，服务端会返回401，指定的字符串会返回到客户端，给用户以提示信息，不同的浏览器对返回内容的展示不一致 | http、server、location、limit_except |
| auth_basic_user_file [file] | 指定用户名和密码所在文件<br />文件使用htpasswd工具（httpd-tools）生成 | http、server、location、limit_except |

##### nginx配置-特殊符号

| 符号 | 作用                                                         | 示例                    |
| ---- | ------------------------------------------------------------ | ----------------------- |
| !    | 否定                                                         | !=、!~                  |
| =    | 精确匹配（相等）                                             | $uri = /images/...      |
| ~    | 区分大小写匹配                                               | $request_uri ~ /.*\.js$ |
| ~*   | 不区分大小写匹配                                             | $remote_user ~* XXX     |
| ^~   | 功能和不加符号的一致，唯一不同的是，一旦匹配上，就不需要继续正则匹配 | location ^~ /html/...   |
| @    | 用于location的URI，表示该location只展示信息，不需要跳转地址，@后的字段值是引用名（其它块通过@引用名来使用） | @fallback               |

##### nginx配置-常用全局变量

| 变量                   | 说明                                                         |
| ---------------------- | ------------------------------------------------------------ |
| $args                  | URL中的请求参数（arg1=value1&arg2=value2...）                |
| $http_user_agent       | 用户访问服务器的代理信息（如果通过浏览器访问，该变量为浏览器的相关版本信息） |
| $host                  | 请求服务器的server_name                                      |
| $uri                   | 请求地址的URI（/images）                                     |
| $document_uri          | 和$uri一样                                                   |
| $document_root         | 请求对应的location的root（未设置则是nginx自带html目录所在位置） |
| $content_length        | 请求头中"Content-Length"的值                                 |
| $content_type          | 请求头中"Content-Type"的值                                   |
| $http_cookie           | 客户端的cookie信息                                           |
| $limit_rate            | nginx配置中limit_rate的值（默认是0，不限制）                 |
| $remote_addr           | 客户端的IP地址                                               |
| $remote_port           | 客户端的端口号                                               |
| $remote_user           | 客户端的用户名（需要有认证模块才能获取）                     |
| $scheme                | 访问协议                                                     |
| $server_addr           | 服务端的IP地址                                               |
| $server_name           | 服务端的名称                                                 |
| $server_port           | 服务端的端口号                                               |
| $server_protocol       | 请求协议的版本（HTTP/1.1）                                   |
| $request               | 请求的信息                                                   |
| $request_body_file     | 客户端发送的本地文件资源的名称                               |
| $request_method        | 客户端的请求方式（GET、POST...）                             |
| $request_filename      | 客户端请求资源文件的路径                                     |
| $request_uri           | 携带请求参数的$uri                                           |
| $upstream_cache_status | 缓存命中状态（MISS\|HIT\|BYPASS）                            |
| $cookie_nocache        | 当前请求的cookie中键名nocache对应的值                        |
| $arg_nocache           | 当前请求的参数中键名nocache对应的值                          |
| $arg_comment           | 当前请求的参数中键名comment对应的值                          |

##### 添加模块

```shell
# 查询nginx配置参数
nginx -V
# 将nginx主目录下的sbin目录下的nginx二进制文件更名备份
cd /usr/local/nginx/sbin
mv nginx nginxold
# 进入nginx安装目录
cd ~/nginx/core/nginx-1.16.1
# 清空之前编译的内容
make clean
# 重新配置
./configure [查询到的配置参数] --with-[module]
# 重新编译
make
# 将objs目录下的nginx二进制文件移动到nginx安装目录下的sbin目录中
mv objs/nginx /usr/local/nginx/sbin
# 执行更新命令
make upgrade
```

##### 浏览器头信息字段

| header        | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| Expires       | 缓存过期的日期和时间，等同max-age的效果，但是如果同时存在，则被Cache-Control的max-age覆盖 |
| Cache-Control | 设置和缓存相关的配置信息<br />must-revalidate：可缓存但必须再向源服务器进行确认<br />no-cache：缓存前必须向源服务器确认其有效性（弱缓存）<br />no-store：不缓存请求或响应的任何内容<br />no-transform：代理不可更改媒体类型<br />public：可向任意方提供响应的缓存<br />private：仅向特定用户返回响应<br />proxy-revalidate：要求代理缓存服务器对缓存的响应有效性再进行确认<br />max-age=[seconds]：缓存的过期时间（强缓存）<br />s-maxage=[seconds]：代理缓存服务器缓存的过期时间，优先级高于max-age |
| Last-Modified | 请求资源最后修改时间                                         |
| ETag          | 请求变量的实体标签的当前值，比如文件的MD5值                  |
| Referer       | 请求来源                                                     |
| Set-Cookie    | 设置cookie                                                   |

##### 添加cookie

```
add_header Set-Cookie '[cookie_name]=[cookie_value]';
```

##### 跨域

浏览器同源策略：协议、域名（IP）、端口相同

```
add_header Access-Control-Allow-Origin *;
add_header Access-Control-Allow-Methods GET,POST,PUT,DELETE;
```

##### 域名跳转

```
server {
	rewrite ^(.*) [target_domain]$1;
}
```

##### 域名镜像

只为某子目录下的资源做镜像

```
server {
	location [uri] {
		rewrite ^/[dir_path](.*)$ [target_domain]$1;
	}
}
```

##### 独立域名

为每一个功能模块设置独立的域名

```
server {
	listen 81;
	server_name [search_domain];
	rewrite ^(.*) [main_domain]/search$1;
}
server {
	listen 82;
	server_name [item_domain];
	rewrite ^(.*) [main_domain]/item$1;
}
server {
	listen 83;
	server_name [cart_domain];
	rewrite ^(.*) [main_domain]/cart$1;
}
```

##### 目录自动添加"/"

加"/"和不加"/"的区别：URL以目录名结尾，如果结尾不加"/"，nginx服务器内部会自动做一个301的重定向，重定向的地址由指令server_name_in_redirect来决定，加"/"则不会有这个重定向

```
# 如果server_name_in_redirect设置成on，则重定向的地址会以server_name来拼接，当server_name设置为localhost时，会出现客户端访问不到的情况

server {
	listen 80;
	server_name localhost;
	location [uri] {
		if (-d $request_filename) {
			rewrite ^(.*)([^/])$ http://$host:$server_port$1$2/ permanent;
		}
	}
}
```

##### 合并目录

减少URL中包含的目录层级，有利于搜索引擎优化（SEO）

```
# /dir/name1/name2/name3/name4.html -> /dir-name1-name2-name3-name4.html

server {
	location [uri] {
		rewrite ^/[dir]-([regex_name1])-([regex_name2])-([regex_name3])-([regex_name4])\.html$ /[dir]/$1/$2/$3/$4.html last;
	}
}
```

##### 静态资源防盗链

资源盗链：利用别人网站的资源链接，将资源展示在自己网站上

```
location /images {
	valid_referers none blocked [server_name|regex];
	if ($invalid_referer) {
		# return 403;
		rewrite ^/ /images/forbidden.png break;
	}
}
```

添加模块ngx_http_accesskey_module

##### SSL证书

方式一：在阿里云、腾讯云等第三方服务购买
方式二：使用openssl生成证书

```shell
# 确认当前系统是否有安装openssl
openssl version
# 生成证书
mkdir /root/cert
cd /root/cert
openssl genrsa -des3 -out server.key 1024
openssl req -new -key server.key -out server.csr
cp server.key server.key.org
openssl rsa -in server.key.org -out server.key
openssl x509 -req -days 365 -in server.csr -signkey server.key -out server.crt
```

##### 反向代理缓冲与缓存

一般配置

```
proxy_buffering on;
proxy_buffer_size 4 32k;
proxy_busy_buffers_size 64k;
proxy_temp_file_write_size 64k;
```

##### 七层负载均衡

```
upstream [group_name(backend)] {
	ip_hash;
	least_conn;
	hash $request_uri;
	fair;
	server [url1] [status|weight] [status|weight] ...;
	server [url2] [status|weight] [status|weight] ...;
	server [url3] [status|weight] [status|weight] ...;
}

server {
	location / {
		proxy_pass http://[group_name(backend)];
	}
}
```

status负载均衡状态（可选）：

- down：标记该服务器不参与负载均衡，该状态一般会对需要停机维护的服务器进行设置
- backup：标记该服务器为备份服务器，当主服务器不可用时，用来接收和处理请求
- max_conns=[number]：设置代理服务器同时活动链接的最大数量，默认为0，表示不限制，可以根据服务器处理请求的并发量来进行设置，防止服务器被压垮
- max_fails=[number]：设置允许请求代理服务器失败的次数，默认为1
- fail_timeout=[seconds]：设置经过max_fails失败后，服务暂停的时间，默认为10秒

policy负载均衡策略（可选）：

- 轮询：默认策略，每个请求会按时间顺序逐个分配到不同的服务器，不需要额外的配置
- weight=[number]：加权轮询，设置服务器的权重，默认为1，权重越大，被分配到请求的几率越大，可以根据服务器硬件配置来进行设置，此策略适合服务器硬件配置差别大的情况
- ip_hash：将某个客户端IP的请求通过hash算法定位到同一台服务器上，解决session在多台服务器之间不共享的问题，缺点是无法保证服务器的负载均衡，且服务器权重等将不起作用，建议不使用该策略，而是将session保存到redis中，多台服务器访问同一个redis
- least_conn：最少连接，把请求转发给连接数较少的服务器，相比轮询平均转发，效果更好，此策略适合请求处理时间长短不一造成服务器过载的情况
- hash $request_uri：按访问资源的url的hash结果来分配请求，使同一个资源请求会到达同一台服务器，要配合缓存命中（同一个资源，服务器第一次请求会把资源文件缓存下来）来使用
- fair：根据页面大小、加载时间长短智能的进行负载均衡，需要添加模块nginx-upstream-fair

##### 添加模块nginx-upstream-fair

下载地址：https://github.com/gnosek/nginx-upstream-fair

```shell
# 上传到服务器并解压缩
cd ~
unzip nginx-upstream-fair-master.zip
# 创建模块目录
mkdir /root/nginx/module
# 移动并重命名资源
mv nginx-upstream-fair-master fair
mv fair /root/nginx/module
# 将nginx主目录下的sbin目录下的nginx二进制文件更名备份
cd /usr/local/nginx/sbin
mv nginx nginxold
# 将资源添加到nginx模块中
cd ~/nginx/core/nginx-1.16.1
nginx -V
make clean
./configure [查询到的配置参数] --add-module=/root/nginx/module/fair
make
# 将objs目录下的nginx二进制文件移动到nginx安装目录下的sbin目录中
mv objs/nginx /usr/local/nginx/sbin
# 执行更新命令
make upgrade
```

##### 带有URL重写的负载均衡

```
upstream backend {
	server [url1];
	server [url2];
	server [url3];
}

server {
	location /file/ {
		rewrite ^(/file/.*) /server/$1 last;
	}
	location /server {
		proxy_pass http://backend;
	}
}
```

##### 四层负载均衡

需要添加stream模块

```
stream {
	upstream redisbackend {
        server [redis_server_url1];
        server [redis_server_url2];
        server [redis_server_url3];
	}

    upstream tomcatbackend {
        server [tomcat_server_url1];
        server [tomcat_server_url2];
        server [tomcat_server_url3];
    }
    
	server {
		listen 81;
		proxy_pass redisbackend;
	}
	
	server {
		listen 82;
		proxy_pass tomcatbackend;
	}
}
```

##### 缓存配置

```
http {
	proxy_cache_path /usr/local/proxy_cache levels=2:1 keys_zone=jscache:200m inactive=1d max_size=20g;
	
	upstream backend {
		server [url];
	}
	
	server {
		location / {
			proxy_cache jscache;
			proxy_cache_min_uses 5;
			proxy_cache_valid 200 5d;
			proxy_cache_valid 404 30s;
			proxy_cache_valid any 1m;
			add_header nginx-cache "$upstream_cache_status";
			proxy_pass http://backend/js/;
		}
	}
}
```

##### 缓存清除

方式一：删除缓存目录

```shell
rm -rf /usr/local/proxy_cache
```

方式二：使用第三方模块ngx_cache_purge（需添加）

```
# 访问/purge/资源文件名来删除指定缓存

server {
	location ~ /purge(/.*) {
		proxy_cache_purge [cache_name] [cache_key];
	}
}
```

##### 设置不缓存指定资源

```
server {
	location / {
		if ($request_uri ~ /.*\.js$) {
			set $my_nocache 1;
		}
		proxy_no_cache $cookie_nocache $arg_nocache $arg_comment $my_nocache;
	}
}
```

##### 动静分离

```
upstream webservice {
	server [tomcat_server_url];
}

server {
	# 动态资源访问
	location /demo {
		proxy_pass http://webservice;
	}
	# 静态资源访问
	location ~/.*\.(png|jpg|gif|js) {
		root html/web;
	}
	
	location / {
		root html/web;
		index index.html index.htm;
	}
}
```

##### htpasswd工具

安装httpd-tools

```shell
yum install -y httpd-tools
```

使用

```shell
# 创建一个新文件记录用户名和密码（文件无后缀名）
htpasswd -c [file_path] [username]
# 在指定文件新增一个用户名和密码
htpasswd -b [file_path] [username] [password]
# 从指定文件删除一个用户信息
htpasswd -D [file_path] [username]
# 验证用户名和密码
htpasswd -v [file_path] [username]
```

