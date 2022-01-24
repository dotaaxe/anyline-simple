/*
alert("a ".trim());								//trim
alert("abc".startWith("a"));						//是否以str开头
alert("abc".endWith("c"));							//是否以str结尾
alert("abca".replaceAll("a",''));					//替换全部
alert(al.validate.idcard('123'));					//验证身份证号
alert(al.validate.mobile('123'));					//验证手机号
alert(al.validate.email('a@a.com'));				//验证email
alert(al.validate.number('123'));					//验证数字
alert(al.validate.date('2000-01-01'));				//验证日期
alert(al.validate.int('123'));						//验证整数
alert(al.validate.url('123'));						//验证url
alert(al.validate.cn('123'));						//验证中文
alert(al.validate.password('123'));				//验证密码强度
alert($.random.string(10));							//10位随机字符
alert(al.random.number(10));						//10位随机数字
alert(al.random.digit(10));							//10位随机数字
alert(al.random.en(10));							//10位随机英文
alert(al.random.lower(10));							//10位随机小写英文
alert(al.random.upper(10));							//10位随机大写英文
alert(al.random.uuid());							//uuid(8-4-4-4-12)
alert(al.random.int(10,100));						//10到100之间随机整数
alert(al.random.int(10,100,3));						//3个10到100之间不重复的随机整数
al.parse.date('2000-01-01');						//解析日期
al.date.parse('2000-01-01');
alert(al.format.number(123,'0.0'));					//数字格式化	
alert(al.format.money(100));						//金额格式化
//日期格式化
alert(al.format.date('2008-08-08','yyyy-MM-dd'));
alert(al.date.format('2008-08-08','yyyy-MM-dd'));
alert(al.date.format(new Date(),'yyyy-MM-dd'));
alert(al.date.format(null,'yyyy-MM-dd'));
alert(new Date().format('yyyy-MM-dd'));
alert(al.avg(10,20));										//平均数
alert(al.nvl(null,1,2));									//第一个不是null 不是undefined的参数
alert(al.evl(null,1,2));									//第一个不是null 不是undefined不是''的参数

cookie
al.cookie.get('user');
al.cookie.set('user','zhang',10);					//保存10秒

//浏览器判断
alert(al.browser.info['name']+al.browser.info['version']);
alert(al.browser.name);
alert(al.browser.version);
alert(al.browser.is('ie'));
alert(al.browser.is('ie7'));


*/
if(!String.prototype.trim){
	String.prototype.trim = function () {
		if(!this){
			return this;
		}
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
}
if(!String.prototype.startWith){
	String.prototype.startWith=function(str){
		if(!this){
			return false;
		}
		if(typeof str === "undefined"){
			return false;
		}
		var reg=new RegExp("^"+str);     
		return reg.test(this);        
	} 
}
if(!String.prototype.endWith){
	String.prototype.endWith=function(str){  
		if(!this){
			return false;
		}
		if(typeof str === "undefined"){
			return false;
		}   
		var reg=new RegExp(str+"$");     
		return reg.test(this);        
	}
}
//是否包含
if(!String.prototype.contains){
	String.prototype.contains = function (subStr) {
		if(!this){
			return false;
		}
		if(typeof subStr === "undefined"){
			return false;
		}
		var currentIndex = this.indexOf(subStr);
		if (currentIndex != -1) {
			return true;
		}
		else {
			return false;
		}
	}
}
if(!String.prototype.replaceAll){
	String.prototype.replaceAll = function (ostr, nstr) {  
		if(!this){
			return this;
		}
		if(typeof ostr === "undefined" || typeof nstr === "undefined"){
			return this;
		}
		var regExp = new RegExp(ostr,'g');  
		return this.replace(regExp, nstr);  
	};  
}

Date.prototype.format=function(fmt) {
		if(!this){
			return '';
		}
    var o = {
    "M+" : this.getMonth()+1,																//月份
    "d+" : this.getDate(),																	//日
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12,								//小时(12)
    "H+" : this.getHours(),																	//小时(24)
    "m+" : this.getMinutes(),																//分
    "s+" : this.getSeconds(),																//秒
    "q+" : Math.floor((this.getMonth()+3)/3),												//季度
    "S" : this.getMilliseconds()															//毫秒
    };
    var week = {
    "0" : "/u65e5",
    "1" : "/u4e00",
    "2" : "/u4e8c",
    "3" : "/u4e09",
    "4" : "/u56db",
    "5" : "/u4e94",
    "6" : "/u516d"
    };
    if(/(y+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    if(/(E+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[this.getDay()+""]);
    }
    for(var k in o){
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
};

Date.prototype.add=function(interval,number) {
		if(!this){
			return this;
		}
		switch(interval){ 
			case "y": 
			case "year": 
				return new Date(this.setFullYear(this.getFullYear()+number)); 
			case "M":
			case "month":
				return new Date(this.setMonth(this.getMonth()+number)); 
			case "d": 
			case "day": 
				return new Date(this.setDate(this.getDate()+number)); 
			case "w": 
			case "week": 
				return new Date(this.setDate(this.getDate()+7*number)); 
			case "h": 
			case "hour": 
				return new Date(this.setHours(this.getHours()+number)); 
			case "m": 
			case "minute": 
				return new Date(this.setMinutes(this.getMinutes()+number)); 
			case "s": 
			case "second": 
				return new Date(this.setSeconds(this.getSeconds()+number)); 
			case "S":
			case "milli":
				return new Date(this.setMilliseconds(this.getMilliseconds()+number)); 
		} 
}
var al = {
	config:{
		ajax:{
			url				:null								, //请求的URL
			async			:true								, //是否异步请求
			data			:null								, //请求参数
			user			:null								, //用户名
			password		:null								, //密码
			success			:function(o){}						, //请求成功时的回调函数 与callback相同
			callback		:function(o){}						, //请求成功时的回调函数 与success相同
			fail			:function(data,msg){}				, //请求失败的回调函数
			type			:'POST'								, //请求方法类型
			form			:null								, //提交的form
			xhr				:null								, //xhr
			dataType		:'json'								, //返回的数据类型
			json			:null	 							  //返回的数据
		},
		upload:{
			server:null
		},
		oss:{}
	},
	request_status:{}								, //请求状态
	template_file:{}								, //模板文件内容
	//常用正则
	regexp:{
		money		: /^\d*(?:\.\d{0,2})?$/																, //金额
		email		: /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/										, //邮箱
		mobile		: /^[1][0-9]{10}$/																	, //手机
		cn			: /^[\u4e00-\u9fa5]{0,}$/															, //中文
		int			: /^0$|^[1-9]\d*$/																	, //整数
		number		: /^\d+(\.{1}\d+)?$/																, //数字
		password	: /^.*(?=.{8,})(?=.*\d)(?=.*[A-Z])(?=.*[a-z]).*$/									, //密码
		url			: /^((https?|ftp|file):\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/		, //url
		date		: /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/ ,
		html_a		: /<a\s(\s*\w*?=".+?")*(\s*href=".+?")(\s*\w*?=".+?")*\s*>[\s\S]*?<\/a>/,
		html_img	:/\w+(<\s*img\s*[^>]*>)\w+(<\s*img\s*[^>]*>)\w+/g
	}
};
al.type = function (obj){
		var type = typeof obj;
		if(type == 'object'){
			type = obj +"";
			type = type.replace('[object ','').replace(']','').toLowerCase();
		}
		return type;
	}

al.merge={
	//合并目录(文件或url)
	path:function(dir,path){
		var result = '';
		if(dir && path){
			while(dir.endWith('/')){
				dir = dir.substring(0, dir.length-1);
			}
			while(path.startWith('/')){
				path = path.substring(1);
			}
			result = dir + '/' + path;
		}else if(dir){
			result = dir;
		}else if(path){
			result = path;
		}
		return result;
	},
	param:function(url, params){
		var result = '';
		if(url){
			result = url;
		}
		if(params){
			if(url.contains('?')){
				result = result + '&' + params;
			}else{
				result = result + '?' + params;
			}
		}
		return result;
	}
}
al.init = {
	ajax:function (config){
		if(typeof config['async'] == "undefined") {config['async'] = true;}
		if(!config['success']){config['success']=al.config.ajax['success']}
		if(!config['callback']){config['callback']=al.config.ajax['callabck']}
		if(!config['fail']){config['fail']=al.config.ajax['fail']}
		config['_anyline_request_time'] = new Date().getMilliseconds();
		if(config['data'] == null){
			config['data'] = {};
		}
		if(config['container']){
			if(!al.validate(config['container'])){
				return false;
			}
			config['data'] = al.pack.param(container, config['data']);
		}
		return config;
	}
};

al.submit = function(frm,config){
	if(!frm){return false;}
	config = al.init.ajax(config);
	var request_status_key = config['url']+'?'+$.param(config.data, true);
	if(al.request_status[request_status_key] == true){
		console.log('数据处理中,请稍后重试:'+request_status_key);
		return false;
	}
	$(frm).ajaxSubmit({
		type:'post',
		dataType: 'json',
		success:function(json){
			al.request_status[request_status_key] = false;
			var result = json['result'];
			var message = json['message'];
			var url = json['url'];
			if(url){
				location.href = url;
			}
			var data;
			var type = json['type'];
			config['json'] = json;
			al.ajax_success(config);
		},
	   error:function(XMLHttpRequest, textStatus, errorThrown) {
			al.request_status[request_status_key] = false;
	   		al.ajax_error(XMLHttpRequest, textStatus, errorThrown);
	   }
	});
	
};
al.ajax = function(config){
	config = al.init.ajax(config);
	if(!config){
		return;
	}
	
	var request_status_key = config['url']+'?'+$.param(config.data, true);
	if(al.request_status[request_status_key] == true){
		console.log('数据处理中,请稍后重试',request_status_key);
		return false;
	}
	try{
		if(arguments && arguments.callee && arguments.callee.caller){
			config.data['js_caller'] = arguments.callee.caller.name;
		}

	}catch(e){}
	al.request_status[request_status_key] = true;
	$.ajax({
	   async: config.async,
	   type: config['type']?config['type']:al.config.ajax['type'],
	   dataType : config['dataType']?config['dataType']:al.config.ajax['dataType'],
	   url: config.url,
	   data: config.data,
	   success: function(json){
		   al.request_status[request_status_key] = false;
			var redirect = json['redirect'];
			if(redirect){
				location.href = redirect;
				return;
			}
	   		config.json = json;
			al.ajax_success(config);
	   },
	   error:function(XMLHttpRequest, textStatus, errorThrown) {
		   al.request_status[request_status_key] = false;
	   	   al.ajax_error(XMLHttpRequest, textStatus, errorThrown);
	   }
	});
};

al.upload = al.upload || function(config){
	$.ajax({
		url			: config['url'] || al.config.upload['server'],
	    type		: config['type'] || al.config.ajax['type'],
	    dataType 	: config['dataType'] || al.config.ajax['dataType'],
		cache 		: false,
		contentType : false,
		processData : false,
		data 		: config.data,
		success 	: function(json) {
			config.json = json;
			al.ajax_success(config);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
		   al.ajax_error(XMLHttpRequest, textStatus, errorThrown);
		}
	});
};

al.ajax_success = function(config){
	var result = config.json['result'];
	var message = config.json['message'];
	var data;
	var type = config.json['type'];
	var navi = config.json['navi'];
	
	//解析数据
	if(type=='string' || type=='number'){
		data = config.json['data'];
	}else if(type=='map'){
		data = config.json['data'];
	}else if(type == 'list'){
		data = config.json['data'];
	}else{
		data = config.json['data'];
	}
	if(data['code']*1 == 302){
		location.href = data['redirect'];
		return;
	}
	if(navi){
		config['navi'] = navi;
	}
	if(result){
		//函数回调
		var fn_success = config.success;
		if(fn_success){
			fn_success(data,message,config);
		}
	}else{
		var fn_fail = config.fail;
		if(fn_fail){
			fn_fail(data,message,config);
		}
		
	}

	var code = config.json['code'];
	var jscall = config.json['jscall'];
	if(jscall){
		eval(jscall);
	}


	var fn_callback = config['callback'];
	if(fn_callback){
		fn_callback(result,data,message,config);
	}
};
al.ajax_error = function(XMLHttpRequest, textStatus, errorThrown){
		console.log("状态:"+textStatus+"\n消息:"+XMLHttpRequest.responseText);
		console.log(errorThrown);
};

/**
 * 加载服务器端文件
 * path必须以密文提交 <al:des>/WEB-INF/template/a.jsp</al:des>
 * 以WEB-INF为相对目录根目录
 * al.template('/WEB-INF/template/a.jsp',function(result,data,msg){alert(data)});
 * al.template({path:'template/a.jsp', id:'1'},function(result,data,msg){});
 * 模板文件中以${param.id}的形式接收参数
 * 
 * 对于复杂模板(如解析前需要查询数据)需要自行实现解析方法js中 通过指定解析器{parser:'/al/tmp/load1.do'}形式实现
 *controller中通过 WebUtil.parseJsp(request, response, file)解析JSP
 *注意 parsejsp后需要对html编码(以避免双引号等字符在json中被转码) js接收到数据后解码
 *escape unescape
 */
al.template = function(config, callback){
	if(typeof config == 'string'){
		config = {path:config};
	}
	var parser_url = '/al/tmp/load';
	if(config['parser']){
		parser_url = config['parser'];
	}
	var cache = true;
	if(config['cache'] == false){
		cache = false;
	}
	var key = parser_url + "_" + config['path'];
	if(cache && al.template_file[key]){
		callback(true,al.template_file[key],'');
		return;
	}
	al.ajax({
		url:parser_url,
		data:config['data'],
		callback:function(result,data,msg){
			data = unescape(data);
			al.template_file[key] = data;
			callback(result,data,msg);
		}
	});
}
al.validate = function(p, msgBox){
	function config(tar){
		var config = {};
		$.each(tar.attributes, function() {
			if(this.specified) {
				var nm = this.name;
				var val = this.value;
				if(nm.startWith('data-validate') && val){
					nm = nm.replace('data-validate-','');
					config[nm] = val;
				}
			}
		  });
		return config;
	}
	var result = true;
	$(p).find("input[class='required'],select[class='required'],textarea[class='required']").each(function(item){
		var val = $(this).val();
		if(!val){
			$(this).focus();
			var title = al.string2map($(this).data('validate-msg'))['required'] || $(this).data('validate-msg') || $(this).attr('placeholder');
			if(!title && this.tagName =='SELECT'){
				title = $(this).find('option').first().text();
			}
			if(!title){
				title = '请确认信息完整';
				console.log(this);
			}else{
				if(title.indexOf('输入') != -1 || title.indexOf('确认') != -1 || title.indexOf('选择') != -1){
				}else{
					title = '请确认'+title;
				}
			}
			if(!msgBox){
				al.tips(title);
			}else{
				$(msgBox).html(title);
			}
			result = false;
			return result;
		}
	});
	$(p).find('input,select,textarea').each(function(item){
		var val = $(this).val();
		var configs = config(this);
		var max = configs['max'];
		if(!max){
			return;
		}
		var min = configs['min'];
		if(!min){
			return;
		}else{
		}
		var eq = configs['eq'];
		if(!eq){
			return;
		}else{
			eq = al.parse.value(eq);
			if(eq != val){
				al.tips('请输入相同值');
				return false;
			}
		}

	});


	return result;
  }
al.string2map=function(s){
	if(!s){
		return {};
	}
	var msgs = s.split(',');
	var json = {};
	for(var i in msgs) { 
		var kv = msgs[i].split(':');
		if(kv.length > 1){
			json.put(kv[0],kv[1]);
		}
	};
	return json;
};

//正则验证
//身份证
al.validate.idcard=function (cid) {
	var arrExp = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ];// 加权因子
	var arrValid = [ 1, 0, "X", 9, 8, 7, 6, 5, 4, 3, 2 ];// 校验码
	var reg = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
	if (reg.test(cid)) {
		var sum = 0, idx;
		for (var i = 0; i < cid.length - 1; i++) {
		// 对前17位数字与权值乘积求和
			sum += parseInt(cid.substr(i, 1), 10) * arrExp[i];
		}
		// 计算模（固定算法）
		idx = sum % 11;
		// 检验第18为是否与校验码相等
		return arrValid[idx] == cid.substr(17, 1).toUpperCase();
	} else {
		return false;
	}
};
al.validate.mobile=function(str){
	return al.regexp.mobile.test(str);
};
al.validate.email=function(str){
	return al.regexp.email.test(str);
};
al.validate.mail=function(str){
	return al.regexp.email.test(str);
};

al.validate.number=function(str){
	return al.regexp.number.test(str);
};
al.validate.date=function(str){
	return al.regexp.date.test(str);
};
al.validate.int=function(str){
	return al.regexp.int.test(str);
};
al.validate.url=function(str){
	return al.regexp.url.test(url);
};
al.validate.money=function(str){
	return al.regexp.money.test(str);
};
al.validate.cn=function(str){
	return al.regexp.cn.test(str);
};
al.validate.password=function(str){
	return al.regexp.password.test(str);
}; 
al.validate.weixin=al.validate.wechat=function(){  
    var u = navigator.userAgent.toLowerCase();  
    if(u.match(/MicroMessenger/i)=="micromessenger") {  
        return true;  
    } else {  
        return false;  
    }  
} ;
al.validate.android = function(){
	var u = navigator.userAgent.toLowerCase();
	var result = u.indexOf('android') > -1 || u.indexOf('adr') > -1; //android终端
	return result;
};
al.validate.ios = function(){
	var u = navigator.userAgent.toLowerCase()
	var result = !!u.match(/\(i[^;]+;( u;)? cpu.+mac os x/); //ios终端
	return result;
};
al.check = al.validate;

al.fn = {
	name:function(callee){
		if(!callee){
			return '';
		}
		if(typeof callee != 'function'){
			try{
				callee = eval(callee);
			}catch(e){}
		}
		if(typeof callee == 'function'){
			var _callee = callee.toString().replace(/[\s\?]*/g,""),
			comb = _callee.length >= 50 ? 50 :_callee.length;
			_callee = _callee.substring(0,comb);
			var name = _callee.match(/^function([^\(]+?)\(/);
			if(name && name[1]){
			  return name[1];
			}
		}else{
			return '';
		}
	}
};

al.parse={
	//日期解析
	date:function(obj){
		if(obj instanceof Date){
			return obj;
		}else if(typeof obj == "number"){
			return new Date(obj);
		}else if(typeof obj == "string"){
			obj = obj.replace(/-/g,"/");
			return new Date(obj);
		}else{
			return null;
		}
	},
	//解析html标签值
	//以.call或#id开头取val()否则原样返回
	value:function(param){
		if(param){
			if(param.startWith('\\.') || param.startWith('#')){
				param = $(param).val();
			}
		}
		return param;
	}
}
//动态加载js,css
al.load={
	script:function(url,callback) {
		var head = document.getElementsByTagName('head')[0];
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = url;
		if(typeof(callback)=='function'){
			script.onload = script.onreadystatechange = function () {
				if (!this.readyState || this.readyState === "loaded" || this.readyState === "complete"){
					callback();
					script.onload = script.onreadystatechange = null;
				}
			};
		}
		head.appendChild(script);
	},
	style:function(url,callback){
		callback = callback||function(){};
		var node = document.createElement("link");
		var supportOnload = "onload" in node;
		var isOldWebKit = +navigator.userAgent.replace(/.*(?:AppleWebKit|AndroidWebKit)\/?(\d+).*/i, "$1") < 536;// webkit旧内核做特殊处理
		var protectNum = 300000; // 阈值10分钟，一秒钟执行pollCss 50次

		node.rel = "stylesheet";
		node.type = "text/css";
		node.href = url;
		if( typeof id !== "undefined" ){
			node.id = id;
		}
		document.getElementsByTagName("head")[0].appendChild(node);
		if (isOldWebKit || !supportOnload) {
			setTimeout(function() {
				pollCss(node, callback, 0);
			}, 20);
			return;
		}

		if(supportOnload){
			node.onload = onload;
			node.onerror = function() {
				// 加载失败(404)
				onload();
			}
		}else{
			node.onreadystatechange = function() {
				if (/loaded|complete/.test(node.readyState)) {
					onload();
				}
			}
		}

		function onload() {
			// 确保只跑一次下载操作
			node.onload = node.onerror = node.onreadystatechange = null;
			// 清空node引用，在低版本IE，不清除会造成内存泄露
			node = null;
			callback();
		}

		/*循环判断css是否已加载成功
		* @param node -- link节点
		* @param callback -- 回调函数
		* @param step -- 计步器，避免无限循环
		*/
		function pollCss(node, callback, step){
			var sheet = node.sheet,  isLoaded;
			step += 1;
			// 保护，大于10分钟，则不再轮询
			if(step > protectNum){
				isLoaded = true;
				// 清空node引用
				node = null;
				callback();
				return;
			}

			if(isOldWebKit){
				// for WebKit < 536
				if(sheet){
					isLoaded = true;
				}
			}else if(sheet){
				// for Firefox < 9.0
				try{
					if(sheet.cssRules){
						isLoaded = true;
					}
				}catch(ex){
					// 火狐特殊版本，通过特定值获知是否下载成功
					if(ex.name === "NS_ERROR_DOM_SECURITY_ERR"){
						isLoaded = true;
					}
				}
			}

			setTimeout(function() {
				if(isLoaded){
					callback();
				}else{
					pollCss(node, callback, step);
				}
			}, 20);
		}
	},//end style
	js:function(url,callback){
		return al.load.script(url,callback);
	},
	css:function(url,callback){
		return al.load.style(url,callback);
	}
}//end load

al.format={
	//人民币大写
	money:function(n)   {
		var fraction = ['角', '分'];
		var digit = ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'];
		var unit = [ ['元', '万', '亿'], ['', '拾', '佰', '仟']  ];
		var head = n < 0? '欠': '';
		n = Math.abs(n);
		var s = '';
		for (var i = 0; i < fraction.length; i++) {
			s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, '');
		}
		s = s || '整';
		n = Math.floor(n);
		for (var i = 0; i < unit[0].length && n > 0; i++)    {
			var p = '';
			for (var j = 0; j < unit[1].length && n > 0; j++)  {
				p = digit[n % 10] + unit[1][j] + p;
				n = Math.floor(n / 10);
			}
			s = p.replace(/(零.)*零$/, '').replace(/^$/, '零')  + unit[0][i] + s;
		}
		return head + s.replace(/(零.)*零元/, '元').replace(/(零.)+/g, '零').replace(/^整$/, '零元整');
	} ,
	//日期格式
	date:function(date,format){
		if(!format){
			return '';
		}
		if(!date){
			date = new Date();
		}
		if(date instanceof Date){
			return date.format(format);
		}else{
			date = al.parse.date(date);
			return date.format(format);
		}
	},
	number:function(num,format){   
		var neg = false;
		if(num < 0){
			neg = true;
			num = - num;
		}
		var strarr = num?num.toString().split('.'):['0'];   
		var fmtarr = format?format.split('.'):[''];   
		var retstr='';   
		// 整数部分   
		var str = strarr[0];   
		var fmt = fmtarr[0];   
		var i = str.length-1;     
		var comma = false;   
		for(var f=fmt.length-1;f>=0;f--){   
			switch(fmt.substr(f,1)){   
				case '#':   
					if(i>=0 ) retstr = str.substr(i--,1) + retstr;   
					break;   
				case '0':   
					if(i>=0) retstr = str.substr(i--,1) + retstr;   
					else retstr = '0' + retstr;   
					break;   
				case ',':   
					comma = true;   
					retstr=','+retstr;   
					break;   
			}   
		}   
		if(i>=0){   
			if(comma){   
				var l = str.length;   
				for(;i>=0;i--){   
					retstr = str.substr(i,1) + retstr;   
					if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;    
				}   
			} else {
				retstr = str.substr(0,i+1) + retstr;   
			}
		}   

		retstr = retstr+'.';   
		// 处理小数部分   
		str=strarr.length>1?strarr[1]:'';   
		fmt=fmtarr.length>1?fmtarr[1]:'';   
		i=0;   
		for(var f=0;f<fmt.length;f++){   
			switch(fmt.substr(f,1)){   
				case '#':   
					if(i<str.length) retstr+=str.substr(i++,1);   
					break;   
				case '0':   
					if(i<str.length) retstr+= str.substr(i++,1);   
					else retstr+='0';   
					break;   
			}   
		}   
		var result = retstr.replace(/^,+/,'').replace(/\.$/,'');
		if(neg){
			result = '-' + result;
		}
		return result;   
	} 
};//end format

al.date={
	format:function(date,format){
		return al.format.date(date,format);
	},
	parse:function(str){
		return al.parse.date(str);
	},
	add:function(date,interval,number){ 
		date = al.date.parse(date).add(interval,number);
	} 
}
al.random={
	id:function(){
		var a = Math.random, b = parseInt;
		return Number(new Date()).toString() + b(10 * a()) + b(10 * a()) + b(10 * a());
	},
	//长度len的字符串(含数字)
	string:function(len, chrs){
		len = len || 16;
	    var str = '';
		if(chrs){
			var maxPos = chrs.length;
			for (i = 0; i < len; i++) {
				str += chrs.charAt(Math.floor(Math.random() * maxPos));
			}
			return str;
		}else{
			for ( ; str.length < len; str += Math.random().toString(36).substr(2) );
			return str.substr(0, len);
		}
	},
	//长度len的英文
	en:function(len){
		len = len || 16;
		var str = '';
		var chrs = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz';
		var maxPos = chrs.length;
		for (i = 0; i < len; i++) {
			str += chrs.charAt(Math.floor(Math.random() * maxPos));
		}
		return str;
	},
	lower:function(len){
		len = len || 16;
		var str = '';
		for (i = 0; i < len; i++) {
			str += String.fromCharCode(Math.floor( Math.random() * 26) + "a".charCodeAt(0));
		}
		return str;
	},
	upper:function(len){
		len = len || 16;
		var str = '';
		for (i = 0; i < len; i++) {
			str += String.fromCharCode(Math.floor( Math.random() * 26) + "A".charCodeAt(0));
		}
		return str;
	},
	//长度len的数字
	digit:function(len){
		len = len || 16;
		var str = '';
		for (i = 0; i < len; i++) {
			str += String.fromCharCode(Math.floor( Math.random() * 10) + 48);
		}
		return str;
	},
	number:function(len){
		len = len || 16;
		var str = '';
		for (i = 0; i < len; i++) {
			str += String.fromCharCode(Math.floor( Math.random() * 10) + 48);
		}
		return str;
	},
	//xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx (8-4-4-4-12)
	uuid:function () {
		function S4() {
		   return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
		}
		return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	},
	//min max之间的整数
	int:function(min,max){
		return Math.floor(Math.random()*(max-min+1)+min)
	},
	ints:function(min,max,qty){
		var list = new Array();
		while(true){
			var rdm = Math.floor(Math.random()*(max-min+1)+min);
			if(list.indexOf(rdm) == -1){
				list.push(rdm);
			}
			if(list.length >= qty){
				break;
			}
		}
		return list;
	}
};
//平均数
al.avg=function(){
	var result = 0;
	for (var i = 0; i < arguments.length; i++) {
		result += arguments[i];
	}
	return result/arguments.length;
};
//最大值
al.max=function(){
	var result = null;
	for (var i = 0; i < arguments.length; i++) {
		if(result == null || result < arguments[i]){
			result = arguments[i];
		}
	}
	return result;
};
//最小值
al.min=function(){
	var result = null;
	for (var i = 0; i < arguments.length; i++) {
		if(result == null || result > arguments[i]){
			result = arguments[i];
		}
	}
	return result;
};
//第一个不是null 不是undefined的参数
al.nvl=function(){
	for (var i = 0; i < arguments.length; i++) {
		var arg = arguments[i];
		if(null != arg && typeof arg != "undefined"){
			return arg;
		}
	}
	return null;
};
//第一个不是null 不是undefined不是''的参数
al.evl=function(){
	for (var i = 0; i < arguments.length; i++) {
		var arg = arguments[i];
		if(arg){
			return arg;
		}
	}
	return null;
};
al.cookie={
	get:function(k){
		var arr = document.cookie.match(new RegExp("(^| )" + k + "=([^;]*)(;|$)"));
		if (arr != null){
			return unescape(arr[2]);
	   }
	   return null;
	},
	set:function(k,v,s){
	    var d = new Date();
		var offset = 8;
		var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
		var nd = utc + (3600000 * offset);
		var exp = new Date(nd);
		exp.setTime(exp.getTime() + s * 1000);
		document.cookie = k + "=" + escape(v) + ";path=/;expires="
            + exp.toGMTString() ;
	}
} 

al.pack={
	param:function(p, src){
		var result = src || {};
		function fnPushParam(old, val){
			if(null != old){
				if(old instanceof Array){
					old.push(val);
					return old;
				}else{
					var list = new Array();
					list.push(old);
					list.push(val);
					return list;
				}
			}else{
				return val;
			}
		}
		$(p).find('input,select,textarea').each(function(){
				let val = $(this).val();
				let key = $(this).attr('name');
				let id = $(this).attr('id');
				let tag = this.tagName;
				if(key && !$(this).is(":disabled")){
					if(tag == 'INPUT'){
						var type = $(this).attr('type');
						if(type =='checkbox'){
							if($(this).is(':checked')){
								result[key] = fnPushParam(result[key],val);
								if(id && val){
									let text = $("label[for='"+id+"']").text();
									let text_key = key+'_text';
									result[key+'_text'] = fnPushParam(result[text_key],text);
								}
							}
						}else if(type == 'radio'){
							if($(this).is(':checked')){
								result[key] = fnPushParam(result[key],val);
								if(id && val){
									let text = $("label[for='"+id+"']").text();
									let text_key = key+'_text';
									result[key+'_text'] = fnPushParam(result[text_key],text);
								}
							}
						}else{
							result[key] = fnPushParam(result[key],val);
						}
					}else if(tag == 'SELECT'){
						result[key] = fnPushParam(result[key],val);
						if(val) {
							let text = $(this).find("option:selected").text();
							let text_key = key+'_text';
							result[key+'_text'] = fnPushParam(result[text_key],text);
						}
					}else if(tag == 'TEXTAREA'){
						result[key] = fnPushParam(result[key],val);
					}
				}
			}
		);

		return result;
	}
}
al.build={
	select:function(config){
		var select = null;
		var data = null;
		var valueKey = 'ID';
		var textKey = 'NM';
		var headerValue = '';
		var headerText = null;
		var defaultValue = null;
		var clear = true;
		var url = null;
		if(typeof config['select'] != "undefined") {
			select = config['select'];
		}
		if(typeof config['data'] != "undefined") {
			data = config['data'];
		}
		if(typeof config['valueKey'] != "undefined") {
			valueKey = config['valueKey'];
		}
		if(typeof config['textKey'] != "undefined") {
			textKey = config['textKey'];
		}
		if(typeof config['defaultValue'] != "undefined") {
			defaultValue = config['defaultValue'];
		}
		if(typeof config['headerText'] != "undefined") {
			headerText = config['headerText'];
		}
		if(typeof config['value'] != "undefined") {
			defaultValue = config['value'];
		}
		if(typeof config['clear'] != "undefined") {
			clear = config['clear'];
		}
		if(typeof config['url'] != "undefined") {
			url = config['url'];
		}
		if(!select){
			return;
		}
		if(clear){
			$(select).empty();
		}
		if(headerText != null ){
			$(select).append('<option value="'+headerValue+'">' + headerText + '</option>');
		}
		if(url){
			al.ajax({
				url:url,
				data:config['data'],
				callback:function(result,rtn,msg){
					if(result){
						config['data'] = rtn;
						config['url'] = null;
						al.build.select(config);
					}
				}
			});
			return;
		}
		if(data){
			var size = data.length;
			for(var i=0; i<size; i++){
				var value = data[i][valueKey];
				var text = data[i][textKey];
				var item = '<option value="'+value+'"';
				if(value == defaultValue){
					item += ' selected';
				}
				item += '>' + text + '</option>';
				$(select).append(item);
			}
		}
		if(config['after']){
			config['after'](data,config); 
		}
	}//end build.select
};//end build
al.browser={
	info:function(){
		var ua=window.navigator.userAgent.toLowerCase(),
		rMsie=/(msie\s|trident.*rv:)([\w.]+)/,										//IE
		rFirefox=/(firefox)\/([\w.]+)/,													//firefox
		rOpera=/(opera).+version\/([\w.]+)/,										//opera
		rChrome=/(chrome)\/([\w.]+)/,												//chrome
		rSafari=/version\/([\w.]+).*(safari)/;										//safari
		var match=rMsie.exec(ua);   
		if(match != null) { 
			return {name:"IE",version:match[2] || "0"};   
		}   
		var match=rFirefox.exec(ua); 
		if(match != null) {   
			return {name:match[1] || "",version:match[2] || "0"};   
		}   
		var match=rOpera.exec(ua);   
		if(match != null)   {   
			return {name:match[1] || "",version:match[2] || "0"};   
		}      
		var match=rChrome.exec(ua);   
		if(match != null)   {   
			return {name:match[1] || "",version:match[2] || "0"};   
		}   
		var match=rSafari.exec(ua);   
		if(match != null)   {   
			return {name:match[2] || "",version:match[1] || "0"};   
		}   
		if(match == null)   {   
			return {name:"",version:"0"};   
		}   
	}  
}
al.browser={
	is:function(type){//ie ie6 ie11 chrome firefox 360 qq
		var info = al.browser.info();
		info = info['name'] + info['version'];
		if(info.contains(type)){
			return true;
		}else{
			return false;
		}
	},
	name:function(){
		return al.browser.info()['name'];
	},
	version:function(){
		return al.browser.info()['version'];
	}
}

al.blob2file = function(urlData,fileType){
    var bytes=window.atob(urlData),
        n=bytes.length,
        u8arr=new Uint8Array(n);
    while(n--){
        u8arr[n]=bytes.charCodeAt(n);
    }
    return new Blob([u8arr],{type:fileType});
};
al.distance = function(lat1, lon1, lat2, lon2){
	var radLat1 = lat1*Math.PI / 180.0;
	var radLat2 = lat2*Math.PI / 180.0;
	var a = radLat1 - radLat2;
	var b = lon1*Math.PI / 180.0 
			- lon2*Math.PI / 180.0;
	var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
	Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	s = s *6378.137 ;
	s = Math.round(s * 10000) / 10000;
	return s;
}

al.isWechat = function(){
  //window.navigator.userAgent属性包含了浏览器类型、版本、操作系统类型、浏览器引擎类型等信息，这个属性可以用来判断浏览器类型
	  var ua = window.navigator.userAgent.toLowerCase();
	  //通过正则表达式匹配ua中是否含有MicroMessenger字符串
	  if(ua.match(/MicroMessenger/i) == 'micromessenger'){
		return true;
	  }else{
		return false;
	  }
}
//兼容 
al.inintSelect = al.build.select;
al.packParam = al.pack.param;
al.tips = function() {}
al.dir = $("script").last().attr("src") || '';
al.dir = al.dir.substring(0, al.dir.lastIndexOf('/'));
//al.load.script(al.dir+'/anyline.ext.js');
//al.load.style(al.dir+'/tips.css');
 