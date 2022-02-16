<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    .menu a{display: inline-block;padding:10px;min-width: 200px;}
</style>
<div>打开连接后参考后台SQL日志</div>
<div class="menu">
    <a href="/like?nm=燕" target="_blank">like</a>
</div>

<div class="menu">
    <a href="/in?dept=1&dept=2" target="_blank">in</a>
    <a href="/in1?cd=11&cd=12" target="_blank">in1(多参数)</a>
    <a href="/in1" target="_blank">in1(默认值)</a>
</div>

<div class="menu">
    <a href="/or?dept=1&sex=0" target="_blank">or</a>
    <a href="/or1?d1=1&d2=2" target="_blank">or1(多参数 默认值)</a>
    <a href="/or1?d2=2" target="_blank">or1(注意看代码注释)</a>
    <a href="/or2?d1=1&d2=2" target="_blank">or2(默认值)</a>
    <a href="/or2?d2=2" target="_blank">or2(默认值)</a>
    <a href="/or2" target="_blank">or2(默认值)</a>

    <a href="/or9?d1=1" target="_blank">or9(多参数)</a>
    <a href="/or9?d2=2" target="_blank">or9(多参数)</a>
    <a href="/or9" target="_blank">or9(默认值)</a>
</div>
<hr/>
