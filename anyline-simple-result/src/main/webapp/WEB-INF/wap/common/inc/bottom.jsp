<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.anyline.org/core" prefix="al"%>
<%@ taglib uri="http://www.anyline.org/wechat" prefix="wx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<table class="index-footer" style="display: none;">
	<tr class="tr-img">
		<td><a href="/"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/menu/home${menu_0_status }.png"></a></td>
		<td><a href="/near/index"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/menu/near${menu_2_status }.png"></a></td>
		<td rowspan="2" onclick="cfScanCode();">
			<al:checkClient type="wechat">
				<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/menu/code1.png" style="width:40px;height:40px;">
				<al:else>
				<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/menu/code0.png" style="width:40px;height:40px;">
				</al:else>
			</al:checkClient>
		</td>

		<td><a href="/wap/usr/cart/index">
			<div  class="alert-box">
				<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/menu/cart${menu_3_status }.png?v=${al.STYLE_VERSION}">
				<div class="alert-num cart-sum-pd-qty" style="display: none;">
					${cart_qty}
				</div>
			</div>
		</a></td>
		<td><a href="/wap/usr/index"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/menu/my${menu_4_status }.png"></a></td>
	</tr>
	<tr class="tr-txt">
		<td><a href="/" id="aMenu0">首页</a></td>
		<td><a href="/near/index" id="aMenu2">附近</a></td>
		<td><a href="/wap/usr/cart/index" id="aMenu3">购物车</a></td>
		<td><a href="/wap/usr/index" id="aMenu4">我的</a></td>
	</tr>
</table>
	<div style="position: fixed; bottom:10px; left:10px; width:36px;height:36px;border-radius: 50%;background-color:#ccc;padding:3px;z-index: 999;">
		<a href="/index"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/home_f.png" style="width:30px;height:30px;"></a>
	</div>
	<div style="position: fixed; bottom:10px; right:10px; width:36px;height:36px;border-radius: 50%;background-color:#ccc;padding:3px;z-index: 999;display: none;">
		<a href="/wap/usr/index"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/my_f.png" style="width:30px;height:30px;"></a>
	</div>
<div style="margin-bottom: 50px;clear: both;">
</div>
<style>
#aMenu${cur_menu_idx}{
	color:#FF3957;
}
</style>

<script type="text/javascript">
	var login_back_url = '${LOGIN_BACK_URL}';
</script>
