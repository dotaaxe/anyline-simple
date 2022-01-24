<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/web/common/inc/tag.jsp"%>
<style>
    .menu a{display: inline-block;padding:10px;min-width: 200px;}
</style>
<div class="menu">
<a href="/web/home/hr/dept/l">部门列表</a>
|
<a href="/web/home/hr/emp/l">人员列表</a>
|
<a href="/web/home/hr/emp/table">Auto Table</a>
|
<a href="/web/home/hr/slr/l">薪资列表</a>
<br/>
<a href="/web/home/hr/emp/group">人员按部门分组</a>
|
<a href="/web/home/hr/slr/pivot">1-12月薪资(列转列、聚合)</a>

|
<a href="/web/home/hr/slr/pivots">1-12月薪资(动态DataSet)</a>
|
<a href="/web/home/hr/slr/pivotm">1-12月薪资(动态Map)</a>
</div>
<hr/>
