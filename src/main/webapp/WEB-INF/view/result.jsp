<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <title>${q} 搜索结果</title>
    <jsp:include page="include/commonfile.jsp"/>
</head>
<body>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${ctx}/index">叮叮搜索</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="${ctx}/index">百度云搜索 <span class="sr-only">(current)</span></a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="${ctx}/about">关于</a></li>
            </ul>
        </div>
    </div>
</nav>

<div>
    <div class="col-lg-12">
        <div class="col-md-6">
            <input type="text" class="form-control" id="searchinfo" placeholder="请输入关键词...">
        </div>
        <div class="col-md-4">
            <button type="button" class="btn btn-success" id="submit">查询</button>
            <button type="button" class="btn btn-success">返回</button>
        </div>
    </div>
</div>

</body>
</html>