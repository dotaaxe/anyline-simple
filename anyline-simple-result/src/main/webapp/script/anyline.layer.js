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
			layer.close(idx);
			yes();
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

al.tips = layer.msg;
al.alert = function(msg){
layer.alert(msg, {icon: 7});
}
