这不是一个完整的爬虫产品，主要用来实现  
会话保持(JSESSIONID,JWT)  
JavaScript执行(有些登录接口在登录之前加个密算个签名之类的情况)  
内容解析标签识别等还是需要 org.anyline.util.regular.RegularUtil  

一些简单的算法直接用Java算一下就可以，太啰嗦的直接执行js.function  
在执行js时经常遇到文件中引用浏览器内置对象如window,navigator等,(一般用来判断个浏览器类型版本等)根据自己的环境直接替换一下  
遇到更复杂的情况，咨询一下前端同事  

```
(function (a) {
...各种计算
a.B = function(){}
}(window));
var result = B('abc');
>>>
var win = {};
(function (a) {
...各种计算
a.B = function(){}
}(win);
var result = B('abc');
```
模拟更真实的浏览器可以参考selenium  