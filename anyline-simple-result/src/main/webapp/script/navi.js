/*
_navi_param必须返回json格式，不要执行类似$.param()的方法格式化，因为返回的数据上还要添加别的字段
*/
//标签直接调用
function _navi_init(conf){
    conf['flush'] = true;
    var page = 1;
    _navi_go(page,conf);
}
function _navi_load_cache(flag,conf){
	if(conf['type'] ==1){
		return;
	}
    let cache_param = al.cookie.get('al_cache_navi_params'); 
	if(cache_param){
		cache_param = JSON.parse(cache_param); 
	}
	if(cache_param){
		cache_param = cache_param[location.href+'_'+flag]; 
	}
    if(cache_param){
        $('#_navi_cache_page_'+flag).val(cache_param['_anyline_page']);
        let vol_key = $('#hid_page_rows_key_'+conf['_anyline_navi_conf_']).val() || '_anyline_page_rows';
        $('#_navi_cache_vol_'+flag).val(cache_param[vol_key]);
    }
}

function _navi_jump(conf){
    //历史版本有form
    var n = null;
    if(event){
        var frm = $(event.target).closest("form");
        n = frm.find('._anyline_jump_txt').val();
    }
    if(!n){
        n = $('#hid_jump_txt_'+conf['_anyline_navi_conf_']).val();
    }
    _navi_go(n,conf);
}
//跳转回车
function _navi_jump_enter(conf){
    var evt = window.event || e;
    if (evt.keyCode == 13){
        _navi_jump(conf);
    }
}
//刷新当前页
function _navi_refresh(conf){
    conf['flush'] = true;
    _navi_jump(conf);
}
//修改每页多少条
function _navi_change_vol(conf){
    if(conf){
        var k = conf['_anyline_navi_conf_'];
        var vol = $('#navi_val_set_'+k).val();
        $('#hid_page_rows_'+k).val(vol);
        conf['flush'] = true;
        $('#_navi_cache_vol_'+conf['_anyline_navi_conf_']).val(vol);
    }else{
        if(event){
            var frm = $(event.target).closest("form");
            var vol = frm.find('.navi-rows-set').val();
            if(vol){
                frm.find('._anyline_navi_cur_page').val(0);
                $('._anyline_navi_page_rows').val(vol);
            }
        }
    }
    _navi_go(1,conf);
}
//修改每页多少条
function _navi_flush(conf){
    var k = conf['_anyline_navi_conf_'];
    var vol = $('#navi_val_set_'+k).val();
    $('#hid_page_rows_'+k).val(vol);
    conf['flush'] = true;
    _navi_go(1,conf);
}
//分页标签onclick
function _navi_go(n,conf) {
    var frm;
    if(!n){
        n = 1;
    }
    if(conf){
        var k = conf['_anyline_navi_conf_'];
        var max = $('#hid_total_page_'+k).val();
        if(max || max ==0){
            if(n> max*1){
                return;
            }
        }
        if($('#hid_cur_page_'+k).val() == n && !conf['flush']){
            return;//当前页
        }
    }
    if(typeof(conf) != "undefined" && conf){
        _navi_go_ajax(n,conf);
        return;
    }
    var iEvent = window.event||argument.callee.caller.argument[0];
    if(iEvent){
        frm= $(iEvent.target).closest("form");
        if(frm.find('._anyline_navi_cur_page').val() == n){
            return;
        }
        frm.find('._anyline_navi_cur_page').val(n);
        frm.submit();
    }
}
//ajax分页
function _navi_go_ajax(n, conf){
    if (!n) {
        n = 1;
    }
    var _navi_url				= null;	//数据来源
    var _navi_param 			= null;	//参数收集函数
    var _navi_callback 			= null;	//回调
    var _navi_before			= null;	//渲染前调用
    var _navi_after				= null; //渲染后调用
    var _navi_container 		= null;	//内容显示容器(分页+内容)
    var _navi_bodyContainer 	= null;	//内容体显示容器
    var _navi_pageContainer 	= null;	//分页标签显示容器
    var _navi_empty 			= null; //查询无内容提示
    var _navi_over				= null; //最后一页提示
    var _navi_clear				= 0	  ; //覆盖上一页
    var _navi_type				= 0   ;	//0:下标 1:加载更多
    if(conf){
        _navi_url				= conf['url'];
        _navi_param 			= conf['param'];
        _navi_before 			= conf['before'];
        _navi_after 			= conf['after'];
        _navi_callback 			= conf['callback'];
        _navi_container 		= conf['container'];
        _navi_bodyContainer		= conf['bodyContainer'];
        _navi_pageContainer 	= conf['naviContainer'];
        _navi_empty 			= conf['empty'];
        _navi_over 				= conf['over'];
        _navi_type				= conf['type'];
        _navi_clear				= conf['clear'];
    }
    var _navi_data = {};
    if(typeof _navi_param == 'function' ){
        _navi_data = _navi_param();
        if(_navi_data == false){
            return false;
        }
    }else{
        //_navi_data = $("#_navi_frm").serialize();
    }
    _navi_data['_anyline_page'] = n;
    _navi_data['_anyline_navi_type'] = _navi_type;
    _navi_data['_anyline_navi_conf_'] = conf['_anyline_navi_conf_'];
    _navi_data['_anyline_navi_show_stat'] = conf['stat'];
    _navi_data['_anyline_navi_show_jump'] = conf['jump'];
	_navi_data['_anyline_navi_guide'] = conf['guide'];

    var vol = $('#_navi_cache_vol_'+conf['_anyline_navi_conf_']).val();
    var vol_key = $('#hid_page_rows_key_'+conf['_anyline_navi_conf_']).val() || '_anyline_page_rows';

    _navi_data[vol_key] = vol || $('#hid_page_rows_'+conf['_anyline_navi_conf_']).val();

	if(_navi_pageContainer){
		if(_navi_pageContainer.startWith('#') || _navi_pageContainer.startWith('\\.')){
			_navi_pageContainer = $(_navi_pageContainer);
		}else{
			_navi_pageContainer = $('#'+_navi_pageContainer);
		}
	}
    if(_navi_pageContainer){
        _navi_pageContainer.html('<div class="navi-loading">加载中...</div>');
    }

	if(_navi_container){
		if(_navi_container.startWith('#') || _navi_container.startWith('\\.')){
			_navi_container = $(_navi_container);
		}else{
			_navi_container = $('#'+_navi_container);
		}
	}
	if(_navi_bodyContainer){
		if(_navi_bodyContainer.startWith('#') || _navi_bodyContainer.startWith('\\.')){
			_navi_bodyContainer = $(_navi_bodyContainer);
		}else{
			_navi_bodyContainer = $('#'+_navi_bodyContainer);
		}
	}

	//排序
	if(typeof al_order_sets != "undefined" ){
		var order = al_order_sets[location.href];
		if(order){
			order = encodeURI(JSON.stringify(order)); 
			_navi_data['al_order'] = order;	 
		}
	}
    
    $('#_navi_cache_page_'+conf['_anyline_navi_conf_']).val(n);
    if(conf['is_loading']){
        return;
    }
    conf['is_loading'] = true;
	//缓存分页参数
	if(conf['cache']){
		var al_cache_navi_params = al.cookie.get('al_cache_navi_params');
		if(al_cache_navi_params){
			al_cache_navi_params = JSON.parse(al_cache_navi_params);
		}else{
			al_cache_navi_params = {};
		}
        al_cache_navi_params[location.href+'_'+conf['_anyline_navi_conf_']] = _navi_data;
        al.cookie.set('al_cache_navi_params',JSON.stringify(al_cache_navi_params),conf['cache']*1)
	}
		
    al.ajax({
        url:_navi_url,
        data:$.param(_navi_data,true),
        callback:function(result,data,msg,config){
            conf['is_loading'] = false;

            if(result){
                if(vol){
                    $('#navi_val_set_'+conf['_anyline_navi_conf_']).val(vol);
                    $('#hid_page_rows_'+conf['_anyline_navi_conf_']).val(vol);
                }
                var _body = data['BODY'];
				if(_body){
					_body = unescape(_body);
				}
                var _navi = data['NAVI'];
				if(_navi){
					_navi = unescape(_navi);
				} 
                //渲染前执行
                if(_navi_before && typeof _navi_before == 'function'){
                    _navi_before(result,data,msg,config);
                }
                //用户单独指定回调
                if(_navi_callback && typeof _navi_callback == 'function'){
                    _navi_callback(result,data,msg, config);
                }else{
                    //默认回调
                    if(_navi_container && _navi_container.length > 0){
                        if(_navi_type == 0 || _navi_clear==1){
                            _navi_container.html(_body);
                        }else{
                            _navi_container.append( _body);
                        }
                    }else if(_navi_bodyContainer && _navi_bodyContainer.length > 0){
                        if(_navi_type == 0 || _navi_clear==1){
                            _navi_bodyContainer.html(_body);
                        }else{
                            _navi_bodyContainer.append(_body);
                        }

                    }
                    if(_navi_pageContainer && _navi_pageContainer.length>0){
                        //没有数据
                        if(data['TOTAL_ROW']*1 == 0 && _navi_empty){
                            //一般在后台JSP中生成内容
                            _navi_pageContainer.html(_navi_empty);
                        }else if(data['CUR_PAGE']*1 > data['TOTAL_PAGE']*1 && _navi_over){//最后一页
                            //没有更多数据了
                            _navi_pageContainer.html('<div class="navi-no-more-data">'+_navi_over+'</div>');
                        }else{
                            _navi_pageContainer.html(_navi||'');
                        }
                    }
                }
				if(_navi_after){
					if(typeof _navi_after == 'function'){
						_navi_after(result, data, msg, config);
					}else if(typeof _navi_after == 'string'){
						var afters = _navi_after.split(',');
						var size = afters.length;
						for(var i=0; i<size; i++){
							try{
								var after = eval(afters[i]); 
								after(result, data, msg, config); 
							}catch(e){
								
							}
						}
					}
				}
            }else{
                _navi_pageContainer.html(msg);
                console.log(msg);
            }
        }
    });
}
//自动加载
function _navi_auto_load(conf, scroll){
    if(!conf['auto']){
        return;
    }
    if(conf['type'] != 1){
        return;
    }
    var flag = conf['_anyline_navi_conf_'];
    var cur_page = $('#hid_cur_page_'+flag).val()*1;
    var total_page = $('#hid_total_page_'+flag).val()*1;
    if(cur_page >= total_page){
        console.log('已到最后一页');
        return;
    }
    //console.log("scrollHeight:"+cur_page);
    //console.log("scrollTop+clientHeight:"+(al.scrollTop(scroll) + al.clientHeight(scroll)));
    //console.log("scrollHeight:"+al.scrollHeight(scroll));
    //if(cur_page && al.scrollTop(scroll) + al.clientHeight(scroll) >= al.scrollHeight(scroll)) {
	var scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop;  
 
    if(scrollTop+1 > $(document).height() - $(window).height()){
        _navi_go(cur_page+1,conf);
    }

}



//获取滚动条当前的位置
al.scrollTop = function(scroll) {
    var scrollTop = 0;
    if(scroll) {
        scrollTop = scroll.scrollTop;
    }
    if(!scrollTop){
        scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    }
    return scrollTop;
}

//获取当前可视范围的高度
al.clientHeight = function(scroll) {
    var clientHeight = 0;
    if(scroll){
        clientHeight = scroll.clientHeight;
    }
    if(!clientHeight){
        if(document.body.clientHeight && document.documentElement.clientHeight) {
            clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight);
        } else {
            clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight);
        }
    }
    return clientHeight;
}

//获取文档完整的高度
al.scrollHeight = function(scroll) {
    var scrollHeight = 0;
    if(scroll){
        scrollHeight = scroll.scrollHeight;
    }
    if(!scrollHeight){
        scrollHeight = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
    }
    return scrollHeight;
}

function _navi_get_cache_page(url){
    var key = md5(url) + '_navi';
    var navi = localStorage.getItem(key);
    if(navi){
        navi = JSON.parse(navi);
        return navi['page'];
    }
    return 0;
}
function _navi_set_cache_page(url, page){
    var key = md5(url) + '_navi';
    var navi = localStorage.getItem(key);
    if(navi){
        navi = JSON.parse(navi);
        navi['page'] = page;
    }else{
        navi = {'page':page};
    }
    localStorage.setItem(key, JSON.stringify(navi));
}