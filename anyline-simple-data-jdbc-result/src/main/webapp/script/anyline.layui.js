//在弹出层中，再弹出一层提示信息时，第一层会自动销毁
//如果要保持住第一层，需要设置type:1
//但设置了type=1后，默认没有确定btn(即使设置了yes方法),所以需要显示指定btn
al.open = function(config,yes){
	var url = config['url'];
	config = config || {};	
	if(!config['area']){
		var success = config['success'];
		var success_ = function(lay,index){
			if(success){
				success(lay,index);
			}
			function fnSetSize(lay){
				var width = 0;
				var height = 0;
				$(lay).find('*').each(function(){
					if($(this).outerWidth() > width){
						width = $(this).outerWidth();
					}
				});
				width = width * 1.1;
				if(width > $(window).width()){
					width = $(window).width();
				}
				if(width < 300){
					width == 300;
				}
				layer.style(index,{width:config['width']||width+'px'});
				//layer.style(index,{width:(config['width']||width)+'px',height:(config['height']||height)+'px'});
			}
			fnSetSize(lay);
			$(lay).find('img').each(function(){
				$(this).load(function(){
					fnSetSize(lay);
				});
			});
		}
		config['success'] = success_;
	}
	if(yes){
		config['yes'] = function(idx){
			if(yes() != false){
				layer.close(idx);
			}
		};
	}
	if(config['yes'] && !config['btn']){
		config['btn'] = ['确定'];
	}
	if(url){
		al.template({parser:url,data:config['data'],cache:false},function(result,data,msg){
			config['content'] = data;
			config['url'] = null;
			al.open(config);
		});
	}else{
		layer.open(config);
	}
};

al.confirm = function(config, yes, no){
	if(yes){
		config['yes'] = function(idx){
			if(yes() != false){
				layer.close(idx);
			}
		};
	}
	if(no){
		config['no'] = no;
	}
	var btn = config['btn'];
	if(config['yes'] && !btn){
		config['btn'] = ['确定','取消'];
	}
	al.open(config);
};

al.tips = layui.layer.msg;
al.alert = function(msg){
layui.layer.alert(msg, {icon: 7});
}
