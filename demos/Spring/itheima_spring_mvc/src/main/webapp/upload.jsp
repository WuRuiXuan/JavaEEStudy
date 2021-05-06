<%--
  Created by IntelliJ IDEA.
  User: hp650
  Date: 2021/3/8
  Time: 14:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/user/quick22" method="post" enctype="multipart/form-data">
        名称<input type="text" name="username"><br/>
        文件<input type="file" name="uploadFile"><br/>
        <input type="submit" value="提交"><br/>
    </form><br/>
    <form action="${pageContext.request.contextPath}/user/quick23" method="post" enctype="multipart/form-data">
        名称<input type="text" name="username"><br/>
        文件1<input type="file" name="uploadFiles"><br/>
        文件2<input type="file" name="uploadFiles"><br/>
        <input type="submit" value="提交"><br/>
    </form><br/>
</body>
</html>
