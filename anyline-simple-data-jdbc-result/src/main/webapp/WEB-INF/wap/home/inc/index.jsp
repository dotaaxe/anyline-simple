<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/wap/common/inc/tag.jsp"%>
<div class="w">
<!-- 全部订单 -->
		<div class="dd">
			<span><i><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/dd.png"/></i><a href="/usr/od/l">全部订单</a></span>
		</div>
		<div class="huo clearfix">
			<ul>
				<li><a href="/usr/od/l?s=1"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/dfk.png"/></a><span>${waitPay }</span>待付款</li>
				<li><a href="/usr/od/l?s=2"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/dfh.png"/></a>待发货</li>
				<li><a href="/usr/od/l?s=3"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/dsh.png"/></a>待签收</li>
				<li><a href="/usr/od/l?s=4" class="pd"><img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/sh.png"/></a>退款/售后</li>
			</ul>
		</div>
		<div style="clear:both;"></div>
		<div class="zw"></div>
		<div class="clearfix"></div>
			<div style="clear:both;"></div>
			<div class="item">
				<div class="item_text">
					<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/cj.png" />
					<a href="/usr/rmo/l">我的抽奖</a>
				</div>
			</div>
			<div class="item">
				<div class="item_text">
					<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/fd.png" />
				</div>
				<div class="item_text">
					<a href="/usr/gp/l">我的福袋</a>
				</div>
			</div>
			<div class="item">
				<div class="item_text">
					<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/shdz.png" />
				</div>
				<div class="item_text">
					<a href="/usr/adr/l">地址管理</a>
				</div>
			</div>
			<div class="item">
				<div class="item_text">
					<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/about.png" />
				</div>
				<div class="item_text">
					<a href="javascript:void(0)">关于${APP_NM }</a>
				</div>
			</div>
			<div class="item">
				<div class="item_text">
					<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/yj.png" />
				</div>
				<div class="item_text">
					<a href="javascript:void(0)">意见反馈</a>
				</div>
			</div>
			<c:if test="${SESSION_CUR_USER.ROLE_FLAG == 99 }">
			<div class="item">
				<div class="item_text">
					<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/about.png" />
				</div>
				<div class="item_text">
					<a href="/mg/od/l?s=1">管理员订单管理</a>
				</div>
			</div>
			<div class="item">
				<div class="item_text">
					<img alt="" src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/about.png" />
				</div>
				<div class="item_text">
					<a href="/mg/usr/l?s=1">会员列表</a>
				</div>
			</div>
			</c:if>
		</div>