<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.anyline.org/core" prefix="al"%>
<%@ taglib uri="http://www.anyline.org/aliyun" prefix="aliyun"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="apple-touch-fullscreen" content="yes">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="x5-fullscreen" content="true">
<title>
    ${WEB_USER_CUR_PAGE_TITLE}
    <c:if test="${empty WEB_USER_CUR_PAGE_TITLE}">B.BLOSSOM Fashion</c:if>
</title>
<meta name="description" content="${SEO_DESCRIPTION}">
<link rel="icon" href="//alcdn.oss-cn-shanghai.aliyuncs.com/img/logo/bk/logo.ico" />
<link href="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/common-wap.css?v=${al.STYLE_VERSION}21101" rel="stylesheet" type="text/css" />
<link rel="stylesheet" media="all" type="text/css" href="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/common-wap-user.css?v=${al.STYLE_VERSION}1" />
<script>
    var cur_host = '${SESSION_ATTR_CUR_HOST}';
    var cur_domain = '${SESSION_ATTR_CUR_DOMAIN}';
</script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/jquery-1.11.3.min.js"></script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/layer/mobile/layer.js"></script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.jquery.js?v=11121${al.STYLE_VERSION}"></script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.jquery.ext.js?v=1${al.STYLE_VERSION}"></script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.layer.wap.js?v=1"></script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.upload.js?v=11112"></script>
<script src='//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/dplog.js' data-user='${USER_SESSION_KEY.ID}' data-app='bls' data-ask='www' data-pk='${row.ID}'></script>
<script src="/script/bk.js?v=${al.STYLE_VERSION}_12"></script>
<link href="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/common-wap-reset.css" rel="stylesheet" type="text/css" />
<jsp:include page="/WEB-INF/wap/common/inc/interface.jsp"></jsp:include>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.wx_16.js?V=1"></script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.ui.js?v=1112"></script>
<script src="//alcdn.oss-cn-shanghai.aliyuncs.com/plugin/v1_23/anyline.layui.admin.js?v=11"></script>
<script>
    al.config.upload['server'] = 'http://blossom.file.deepbit.cn/up?token=iu0qBLqgH0lxSUtMHSYdStce6KEFlNfH';
    al.config.upload['dir'] = '<al:date nvl="true" format="yyyyMM"></al:date>';
</script>

<jsp:include page="/WEB-INF/wap/common/inc/share.jsp"></jsp:include>

<!--开启调试模式-->
<c:if test="${not empty SESSION_ATTR_CMD_DEBUG}">
    <script type="text/javascript" src="//cdn.jsdelivr.net/npm/eruda"></script>
    <script>eruda.init();</script>
</c:if>

<style>
    <al:checkClient type="wechat">
    /*微信环境中不显示页头*/
    #ly_header{display:none;}
    </al:checkClient>
</style>
<link href="/style/common.css" rel="stylesheet" type="text/css" />
<script src="/script/common.js"></script>
<script src="/script/cart.js"></script>


<style>
    .underline{
        border-top: none !important;
        border-left: none !important;
        border-right: none !important;
        border-top-color: #FFFFFF;
        border-left-color: #FFFFFF;
        border-right-color: #FFFFFF;
        appearance:none;
        -moz-appearance:none; /*火狐*/
        -webkit-appearance:none; /*苹果和谷歌*/
        width:300px;
    }
    .tips{
        background:url('//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/alt_16.png') no-repeat top right;
        background-size: 10px 10px;
    }
    .upload-bg{
        background:url('//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/upload.png') no-repeat top right;
    }
    .paste-bg{
        background:url('//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/8a/img_cut.png') no-repeat top right;
    }




    .layui-table td, .layui-table th {
        position: relative;
        padding-top: 5px;
        padding-bottom: 5px;
        padding-left: 5px;
        padding-right: 5px;
        min-height: 20px;
        line-height: 20px;
        font-size: 14px;
    }

    .list td, .list th {
        position: relative;
        padding-top: 5px;
        padding-bottom: 5px;
        padding-left: 5px;
        padding-right: 5px;
        min-height: 20px;
        line-height: 20px;
        font-size: 14px;
    }
    .min-table input, .min-table select{
        height: 20px !important;
    }
    .layui-nav-item a {
        height: 30px !important;
        line-height: 30px !important;
        padding-left: 45px;
        padding-right: 30px;
    }
    .layui-side-menu .layui-nav .layui-nav-item .layui-icon {
        margin-top: -13px !important;
    }
    /*必选项*/
    .label.required:after{
        content: "*";
        color:red;
    }
    tfoot{background-color: #f2f2f2;}
    .layui-nav-child dd{
        border-bottom: 1px solid #252525;
    }
    .alert, .alert0, .alert_true, .alerttrue{
        color:red;
    }
    .passtrue{
        color:#18ff59;
    }
    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    input[type="number"]{
        -moz-appearance: textfield;
        text-align: right;
        padding-right: 10px;
    }
    .number{
        text-align: right;
        padding-right: 10px;
    }
    .navi-go-txt{
        padding-left: 0px !important;
    }
    .item-operate img{width:20px;height:20px;}
    thead tr td,tfoot tr td{text-align: center;white-space: nowrap;}
    .layui-table td{white-space: nowrap;}
    .wrap td{white-space: normal !important;}
    .query td{white-space: nowrap;}
    .date-cell{text-align: center}
    .number-cell{text-align: right;}
    .center-cell{text-align: center;}
    .btn-cell{text-align: center;}
    .query .label{padding:5px 5px 5px 35px;font-weight: 550;}
    .info .label{font-weight: 550;}
    img{cursor: pointer;}
</style>
<script>
    $(function () {
        fnAfterPageLoad();

    });
    var al_cookie_conditions = {};
    //保存查询条件
    function fnSetCookieCondition(src){
        let conditions = al_cookie_conditions[location.href];
        if(!conditions){
            conditions = {};
        }
        let key = $(src).attr('name');
        if(key){
            let value = $(src).val();
            conditions[key] = value;
        }
        al_cookie_conditions[location.href] = conditions;
        sessionStorage.setItem('al_cookie_conditions',JSON.stringify(al_cookie_conditions))

    }
    function fnLoadCookieCondition(){
        al_cookie_conditions = sessionStorage.getItem('al_cookie_conditions');
        if(!al_cookie_conditions){
            return;
        }
        al_cookie_conditions = JSON.parse(al_cookie_conditions);
        if(!al_cookie_conditions){
            return;
        }
        al_cookie_conditions = al_cookie_conditions[location.href];
        if(al_cookie_conditions) {
            for (var key in al_cookie_conditions) {
                var value = al_cookie_conditions[key];
                $("[name='"+key+"']").val(value);
            }
        }
        al.cookie.set('al_cookie_conditions','{}',999999999)
    }
    /*
    function fnSetCookieCondition(src){
        let conditions = al_cookie_conditions[location.href];
        if(!conditions){
            conditions = {};
        }
        let key = $(src).attr('name');
        if(key){
            let value = $(src).val();
            conditions[key] = value;
        }
        al_cookie_conditions[location.href] = conditions;
        al.cookie.set('al_cookie_conditions',JSON.stringify(al_cookie_conditions),999999999)

    }
    function fnLoadCookieCondition(){
        let al_cookie_conditions = al.cookie.get('al_cookie_conditions');
        if(!al_cookie_conditions){
            return;
        }
        al_cookie_conditions = JSON.parse(al_cookie_conditions);
        if(!al_cookie_conditions){
            return;
        }
        al_cookie_conditions = al_cookie_conditions[location.href];
        if(al_cookie_conditions) {
            for (var key in al_cookie_conditions) {
                var value = al_cookie_conditions[key];
                console.log(key+"="+value);
                $("[name='"+key+"']").val(value);
            }
        }
    }*/
    function fnAfterPageLoad() {
        //加载查询条件
        fnLoadCookieCondition();
        //必选项
        $('input').attr('autocomplete','off');
        $('.required').parent().prev().addClass('required');

        //回车查询
        $('.query input').bind('keypress',function(event){
            if(event.keyCode == "13"){
                fnSetCookieCondition(event.target);
                $('#btnQuery').click();
            }
        });
        $('.query input').bind('change',function(event){
            fnSetCookieCondition(event.target);
            $('#btnQuery').click();
        });
        $('.query select').bind('change',function(event){
            fnSetCookieCondition(event.target);
            $('#btnQuery').click();
        });
        //记住上次查询条件



        //帮助说明
        $('.help-tips').click(function(){
            al.ajax({
                url:'/js/hlp/v',
                data:{id:'tips-'+$(this).data('help')},
                callback:function (result,data,msg) {
                    if(result){
                        al.open({
                            title:data['TITLE'],
                            content:data['CONTENT']
                        });
                    }else{
                        al.tips('信息完善中');
                    }
                }
            });
        });
        $('.help-url').click(function(){
            location.href = '/wap/hlp/v?id=tips-'+$(this).data('help');
        });

        //关闭弹出层
        $('.info').click(function (){
            $('.auto-query-result').hide();
        });
        $('.query').click(function (){
            $('.auto-query-result').hide();
        });
        $('table').click(function (){
            $('.auto-query-result').hide();
        });

        $('.layui-table').each(function(){
            var head = $(this).find('thead');
            if(head.length > 0 && $(this).find('tfoot').length ==0){
                $(this).append('<tfoot>'+head.html()+'</tfoot>');
            }
        });
        try{
            if(typeof fnAfterNavi === "function"){
                fnAfterNavi();
            }
        }catch(e){}
        al.order.init(function(){$('#btnQuery').click();});
    }




    //供应商支付状态
    function fnSetCustomerPayCheckStatus(sd,cust,sort,cost){
        var img = $(event.target);
        var src = img.attr('src');
        var status = 0;
        var newStatus = 1;
        if(src.indexOf('1.png')>0){
            status = 1;
            newStatus = 0;
        }
        al.confirm({
            title:'修改状态',
            content:'确认修改状态'
        },function(){
            img.attr('src','//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096'+newStatus+'.png');
            al.ajax({
                url:'/js/usr/sd/ctrt/cust/prg/sps',
                data:{sd:sd, cust:cust, status:newStatus, sort:sort, cost:cost},
                callback:function (result,data,msg){
                    if(!result){
                        al.alert(msg);
                        img.attr('src','//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096'+status+'.png');
                    }else{
                        //fnReloadHash();
                    }
                }
            });
        });
    }

    //加工厂支付状态
    function fnSetFactoryPayCheckStatus(sd,fct,sort, url){
        var img = $(event.target);
        var src = img.attr('src');
        var status = 0;
        var newStatus = 1;
        if(src.indexOf('1.png')>0){
            status = 1;
            newStatus = 0;
        }
        al.confirm({
            title:'修改状态',
            content:'确认修改状态'
        },function(){
            img.attr('src','//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096'+newStatus+'.png');
            al.ajax({
                url:'/js/usr/sd/ctrt/fct/prg/sps',
                data:{sd:sd, fct:fct, status:newStatus, sort:sort},
                callback:function (result,data,msg){
                    if(!result){
                        al.alert(msg);
                        img.attr('src','//alcdn.oss-cn-shanghai.aliyuncs.com/img/icon/chk_status_096'+status+'.png');
                    }else{
                    }
                }
            });
        });
    }

    al.cookie.set('user_login_token','${SESSION_CUR_USER.LOGIN_TOKEN}',3600);
</script>


<!--排序-->
<style>
    .al-order img{
        cursor: pointer;
    }
</style>
