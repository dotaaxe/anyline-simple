<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/wap/common/inc/tag.jsp"%>

<div class="bk-content" style=";">
	<ul class="bdbr-1px bdbt-1px">
		<li class="bk-banner bdbr-1px">
			<div class="bk-link">
				<div class="my-item cf">
					<div class="fl">
						<span class="couponIn-icon w-18"> <img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_members.png">
						</span> <span class="bk-Order">合同管理</span>
					</div>
					<div class="fr">
						<span class="bk-AllOrder">合同/款号</span> <span class="bk-arrow"></span>
					</div>
				</div>
			</div>
		</li>
		<li class="bk-item cf">
			<dp:power role="10"><!--QC 现场跟单-->
			<div class="bk-item-border" onclick="location.href='/web/usr/sd/ctrt/fct/prg/l_task'">
				<div class="bk-item-cell">
					<span class="bk-item-cell-num">
					${order_task_qty1}/${order_task_qty}
					</span>
					<span class="bk-item-cell-text">我的任务</span>
				</div>
			</div>
			</dp:power>
			<dp:power role="20">
				<!--QA -->
			<div class="bk-item-border" onclick="location.href='/web/usr/sd/ctrt/l_chk_task'">
				<div class="bk-item-cell">
					<span class="bk-item-cell-num">
					${check_task_qty1}/${check_task_qty}
					</span>
					<span class="bk-item-cell-text">我的任务</span>
				</div>
			</div>
			</dp:power>
<dp:power role="15|10|8|9|900|999|17|19|20|21|22">
			<div class="bk-item-border" onclick="location.href='/web/usr/sd/ctrt/l'">
				<div class="bk-item-cell">
					<span class="bk-item-cell-num"><al:number value="${ctrt_qty}" format="0" def="0"></al:number>
					</span> <span class="bk-item-cell-text">合同列表</span>
				</div>
			</div>
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/usr/mm/pd/l'">
					<span class="bk-item-cell-num"><al:number format="0" def="0" value="${pd_qty}"/></span>
					<span class="bk-item-cell-text">款号列表</span>
				</div>
			</div>
</dp:power>
		</li>
	</ul>
</div>
<dp:power role="10">
<div class="bk-content" style=";">
	<ul class="bdbr-1px bdbt-1px">
		<li class="bk-banner bdbr-1px">
			<div class="bk-link">
				<div class="my-item cf">
					<div class="fl">
						<span class="couponIn-icon w-18"> <img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_members.png">
						</span> <span class="bk-Order">进度管理</span>
					</div>
					<div class="fr">
						<span class="bk-AllOrder">进度</span> <span class="bk-arrow"></span>
					</div>
				</div>
			</div>
		</li>
		<li class="bk-item cf">
			<div class="bk-item-border" onclick="location.href='/web/usr/sd/ctrt/fct/l_cut'">
				<div class="bk-item-cell">
					<span class="bk-item-cell-num"><al:number value="${ctrt_qty}" format="0" def="0"></al:number>
					</span> <span class="bk-item-cell-text">裁剪录入</span>
				</div>
			</div>
			<dp:power role="10">

				<div class="bk-item-border">
					<div class="bk-item-cell" onclick="location.href='/web/usr/sd/ctrt/fct/l_scene'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/loc.png" style="width: 26px;"/>
					</span>
						<span class="bk-item-cell-text">现场</span>
					</div>
				</div>
			</dp:power>
			<div class="bk-item-border" onclick="location.href='/web/usr/sd/ctrt/fct/l_prg'">
				<div class="bk-item-cell">
					<span class="bk-item-cell-num">缝制进度</span>
					<span class="bk-item-cell-text">上机下机日期</span>
				</div>
			</div>
		</li>
	</ul>
</div>
</dp:power>
<div class="bk-content">
	<ul class="bdbr-1px bdbt-1px">
		<li class="bk-banner bdbr-1px">
			<div class="bk-link">
				<div class="my-item cf">
					<div class="fl">
						<span class="couponIn-icon w-18"> <img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_members.png">
						</span> <span class="bk-Order">样品管理</span>
					</div>
					<div class="fr">
						<span class="bk-AllOrder">样品</span> <span class="bk-arrow"></span>
					</div>
				</div>
			</div>
		</li>

		<li class="bk-item cf">

			<dp:power role="10">
				<div class="bk-item-border">
					<div class="bk-item-cell" onclick="location.href='/web/usr/sd/ctrt/fct/l_osamp'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/sew.png" style="width: 26px;"/>
					</span>
						<span class="bk-item-cell-text">投产样</span>
					</div>
				</div>
			</dp:power>

			<dp:power role="20">
			<div class="bk-item-border">
					<div class="bk-item-cell" onclick="location.href='/web/usr/sd/ctrt/l_samp'">
						<span class="bk-item-cell-num">
							<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/chk.png?v=1" style="width: 26px;"/>
						</span>
						<span class="bk-item-cell-text">成衣船样</span>
					</div>
			</div>

			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/usr/sd/ctrt/l_samp_img?sort=9'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/chk.png" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">封样</span>
				</div>
			</div>
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/usr/sd/ctrt/l_qty'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/in.png" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">B品入库</span>
				</div>
			</div>
			</dp:power>
		</li>
	</ul>
</div>

<div class="bk-content">
	<ul class="bdbr-1px bdbt-1px">
		<li class="bk-banner bdbr-1px">
			<div class="bk-link">
				<div class="my-item cf">
					<div class="fl">
						<span class="couponIn-icon w-18"> <img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_members.png">
						</span> <span class="bk-Order">样品室</span>
					</div>
					<div class="fr">
						<span class="bk-AllOrder">样品</span> <span class="bk-arrow"></span>
					</div>
				</div>
			</div>
		</li>

		<li class="bk-item cf">


			<dp:power role="26|17">
				<div class="bk-item-border">
					<div class="bk-item-cell" onclick="location.href='/web/usr/mm/pd/samp/l_my'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/sew.png" style="width: 26px;"/>
					</span>
						<span class="bk-item-cell-text">样衣任务</span>
					</div>
				</div>
				<div class="bk-item-border">
					<div class="bk-item-cell" onclick="location.href='/web/usr/mm/pd/samp/l_my_score'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/sew.png" style="width: 26px;"/>
					</span>
						<span class="bk-item-cell-text">样衣绩效</span>
					</div>
				</div>
				<div class="bk-item-border">
					<div class="bk-item-cell" onclick="location.href='/web/usr/mm/pd/samp/l_img'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/sew.png" style="width: 26px;"/>
					</span>
						<span class="bk-item-cell-text">样衣图库</span>
					</div>
				</div>
			</dp:power>

		</li>
	</ul>
</div>
<div class="bk-content">
	<ul class="bdbr-1px bdbt-1px">
		<li class="bk-banner bdbr-1px" onclick="location.href='/wap/usr/rl/tt/index'">
			<div class="bk-link">
				<div class="my-item cf">
					<div class="fl">
						<span class="couponIn-icon w-18"> <img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_members.png">
						</span> <span class="bk-Order">财务管理</span>
					</div>
					<div class="fr">
						<span class="bk-AllOrder">付款、结算</span> <span class="bk-arrow"></span>
					</div>
				</div>
			</div>
		</li>

		<li class="bk-item cf">
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/usr/fi/pay/req/a?sort=9'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/add.png?v=1" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">申请付款</span>
				</div>
			</div>
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/usr/fi/pay/req/l'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/list.png" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">付款申请记录</span>
				</div>
			</div>
		</li>
	</ul>
</div>

<div class="bk-content">
	<ul class="bdbr-1px bdbt-1px">
		<li class="bk-banner bdbr-1px">
			<div class="bk-link">
				<div class="my-item cf">
					<div class="fl">
						<span class="couponIn-icon w-18"> <img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_members.png">
						</span> <span class="bk-Order">人事管理</span>
					</div>
					<div class="fr">
						<span class="bk-AllOrder">请假/考勤</span> <span class="bk-arrow"></span>
					</div>
				</div>
			</div>
		</li>

		<li class="bk-item cf">
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/home/hr/duty/lve/a'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/add.png?v=1" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">请假申请</span>
				</div>
			</div>
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/home/hr/duty/lve/l'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/list.png?v=1" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">申请记录</span>
				</div>
			</div>
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/home/hr/duty/lve/apr/l?status=0'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/list.png" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">请假审批</span>
				</div>
			</div>
			<div class="bk-item-border">
				<div class="bk-item-cell" onclick="location.href='/web/home/hr/duty/lve/apr/ld'">
					<span class="bk-item-cell-num">
						<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/list.png" style="width: 26px;"/>
					</span>
					<span class="bk-item-cell-text">审批记录</span>
				</div>
			</div>
		</li>
	</ul>
</div>
<div class="bk-content">
	<ul class="in-list bdbr-1px bdbt-1px">
		<li>
			<div class="cell4-box">
				<div class="cell-item" 　>
					<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_mg.png"/>
					<div class="cell-item-text"><sso:safe>帐户安全</sso:safe></div>
				</div>
				<div class="cell-item" onclick="location.href='/wap/usr/adr/l'">
					<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_adr.png"/>
					<div class="cell-item-text">审批</div>
				</div>
				<div class="cell-item" onclick="location.href='/wap/usr/msg/l'">
					<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_msg.png"/>
					<div class="cell-item-text">我的消息(<al:number format="0" value="${SESSION_MSSAGE_QTY }">0</al:number>)</div>
				</div>
				<div class="cell-item" onclick="location.href='/wap/usr/col/l'">
					<img src="//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/rectbg_col.png"/>
					<div class="cell-item-text">收藏</div>
				</div>
			</div>
		</li>
	　
	</ul>
</div>
