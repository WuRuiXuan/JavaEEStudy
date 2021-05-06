![git](https://gitee.com/wuruixuan/markdown-images/raw/master/images/git.png)

#### 基本

##### 查看配置信息

```shell
git config --list
```

##### 查看日志记录

```shell
git log
```

#### 文件

##### 删除文件

```shell
# 删除后不需要add
git rm [file_name]
```

##### 将文件添加到暂存区

```shell
git add [file_name]
git add .
```

##### 将文件移除暂存区

```shell
git reset [file_name]
git reset .
```

##### 查看文件状态

```shell
# -s 简洁方式查看
git status
git status -s
```

#### 仓库

##### 初始化本地仓库

```shell
git init
```

##### 查看远程仓库

```shell
git remote -v
```

##### 本地添加远程仓库

```shell
git remote add [remote_name/origin] [git_repo_url]
```

##### 本地移除远程仓库

```shell
git remote rm [remote_name]
```

##### 克隆远程仓库到本地

```shell
# 每一个文件的每一个版本（包括主线和分支）
git clone [git_repo_url]
```

##### 推送到远程仓库

```shell
git push [remote_name/origin] [branch_name/master]
```

##### 关联远程仓库并拉取内容

```shell
git remote add [remote_name/origin] [git_repo_url]
git pull [remote_name/origin] [branch_name/master]
```

#### 分支

##### 查看分支

```shell
# -r 列出远程分支
# -a 列出本地和远程分支
git branch
git branch -r
git branch -a
```

##### 创建本地分支并推送到远程仓库

```shell
# 从本地创建远程分支
git branch [branch_name]
git push [remote_name/origin] [branch_name]
```

##### 切换分支

```shell
git checkout [branch_name]
```

##### 合并分支

```shell
git checkout [branch_name/dev]
git add .
git commit -m "[commit_desc]"
git checkout [branch_name/master]
git merge [branch_name/dev]
```

##### 删除本地分支

```shell
# -D 在分支内容有修改的情况下强制删除
git branch -d [branch_name]
git branch -D [branch_name]
```

##### 删除远程分支

```shell
git push [remote_name/origin] -d [branch_name]
```

#### 标签

##### 列出标签

```shell
# 标签是指某个分支某个特定时间点的状态
git tag
```

##### 创建标签

```shell
git tag [tag_name]
```

##### 查看标签信息

```shell
git show [tag_name]
```

##### 将标签推送到远程仓库

```shell
git push [remote_name/origin] [tag_name]
```

##### 检出标签

```shell
# 新建一个分支，指向一个标签，该分支的状态为创建该标签时的状态
git checkout -b [new_branch_name] [tag_name]
```

##### 删除本地标签

```shell
git tag -d [tag_name]
```

##### 删除远程标签

```shell
git push [remote_name/origin] :refs/tags/[tag_name]
```

#### 高级

##### 撤销add、commit

```shell
# 不删除改动代码，撤销commit，并且撤销add
git reset HEAD^
# 不删除改动代码，撤销commit，不撤销add
git reset --soft HEAD^
# 删除改动代码，撤销commit，撤销add
git reset --hard HEAD^
```

##### 完全重建版本库

```shell
# 解决git远程仓库过大，克隆时间过长的问题
rm -rf .git
git init
git add .
git commit -m "first commit"
git remote add origin [git_repo_url]
git push -f -u origin master
```

