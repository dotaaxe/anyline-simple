<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>AnyLine Start</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta content="telephone=no" name="format-detection">
<meta content="email=no" name="format-detection" />
<link rel="stylesheet" type="text/css" href="/plugin/layui/css/layui.css">
<script src="/script/jquery-2.1.4.min.js"></script>
<script src="/plugin/layui/layui.all.js"></script>
<script src="/script/anyline.jquery.js"></script>
<script src="/script/anyline.layui.js"></script>
<style>
    [type="text"],[type="password"],[type="date"],[type="number"],textarea,select{
        height: 38px;
        line-height: 1.3;
        line-height: 38px\9;
        border-width: 1px;
        border-style: solid;
        background-color: #fff;
        border-radius: 2px;
        border-color:#e6e6e6;
        padding-left:10px;
        padding-right:10px;
    }
    div.label, div.data{display:inline-block;}
    table{border-collapse:collapse}
    .info .label, .view .label{text-align:right;padding-right:10px;padding-left:10px;min-width:100px;}
    .info .label, .info .data{padding-top:5px;padding-bottom:5px;padding-left:10px;}
    .view .data{border:1px solid #f2f2f2;height:40px;line-height:40px;padding-left:10px;margin:5px;min-width:500px;}



    .query{padding:15px;}
    .query td{white-space: nowrap;}
    .query .item{ white-space:nowrap;  display: inline-block;}
    .query .label{font-weight: 550;min-width:60px;display: inline-block; margin:0px 5px 10px 20px;text-align:right;}
    .query .data{margin:0px 20px 10px 0px;min-width:200px;text-align:left;}
    .query  input[type='text'] , .query select{min-width:200px;width:200px !important;}




    .btn:hover {
        color: #333333;
        text-decoration: none;
        background-position: 0px;
    }
    .info, .view{width:100%;padding-top:15px;padding-bottom:20px}
    .info .label, .view .label{height:40px;display:inline-block;background-color:#f5f5f5;width: 100px;text-align: right;padding-right: 10px;font-size: 15px;color: #000;font-weight: bold;text-shadow:none;}
    .info .data, .view .data{height:40px;display:inline-block;text-align: right;padding-right: 0;font-size: 15px;color: #000;}
    .info .data input,.info .view input{border-radius:0px;margin-bottom: 0px;}
    .view .item .data{
        cursor: not-allowed;
        background-color: #eeeeee;
        border: 1px solid #cccccc;
        height:30px;
        line-height:30px;
        padding:0px 10px 0px 10px;
        text-align:left;
        min-width:220px;
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
    .min-table input, .min-table select{
        height: 20px;
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
    input{vertical-align:middle;}
    thead, thead tr, .head{
        z-index: 999999999;
    }
    .list-box{
        width: 100%;
        overflow:auto;
        height:70%;
        border-width: 1px;
        border-style: solid;
        border-color: #e6e6e6;
    }
    .list {
        width: 100%;
        background-color: #fff;
        color: #666
    }

    .list tr {
        transition: all .3s;
        -webkit-transition: all .3s
    }

    .list td, .list th {
        /* 设置td,th宽度高度 */
        border:1px solid #e6e6e6;
        white-space: nowrap;
    }
    .list {
        overflow:visible;
    }
    .list th {
        text-align: left;
        font-weight: 400
    }
    .list .list-head{
        font-weight: 700;
    }
    .list tbody tr:hover,.list .list-head tr,.list-total tr, .list tr:nth-child(even) {
        background-color: #f2f2f2
    }


    .list td,.list th{
        border-width: 1px;
        border-style: solid;
        border-color: #e6e6e6;
        border-top:none;
        padding-top: 5px;
        padding-bottom: 5px;
        padding-left: 5px;
        padding-right: 5px;
        min-height: 20px;
        line-height: 20px;
        font-size: 14px;
    }
    .list-box .list td:last-child, .list-box .list th:last-child{
        border-right:none;
    }
    .list-box .list td:first-child, .list-box .list th:first-child{
        border-left:none;
    }

    .list td,.list th {
        padding: 5px 5px;
        min-height: 20px;
        line-height: 20px;
        font-size: 14px
    }

    .list input, .list select{
        height: 20px;
    }

    .list .list-head tr:nth-child(1) td {
        position:sticky;
        background-color:#F2F2F2;
        top:0px; /* 列首永远固定在头部  */
    }
    .list .list-head tr:nth-child(2) td {
        position:sticky;
        background-color:#F2F2F2;
        top:30px; /* 列首永远固定在头部  */
    }

    .list tfoot tr:nth-last-child(1) td {
        position:sticky;
        background-color:#F2F2F2;
        bottom:0;
        font-weight: 700;
    }
    .list tfoot tr:nth-last-child(2) td {
        position:sticky;
        background-color:#F2F2F2;
        bottom:30px;
        font-weight: 700;
    }
    .list tfoot tr:nth-last-child(3) td {
        position:sticky;
        background-color:#F2F2F2;
        bottom:60px;
        font-weight: 700;
    }
    .list tfoot tr:nth-last-child(4) td {
        position:sticky;
        background-color:#F2F2F2;
        bottom:90px;
        font-weight: 700;
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
        color: #3366cc;
    }
    .color-096-true{
        color:#009966 !important;
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
    .date-cell{text-align: center}
    .code-cell{text-align: center}
    .number-cell{text-align: right;}
    .center-cell{text-align: center;}
    .btn-cell{text-align: center;}
    .info .label{font-weight: 550;}
    img{cursor: pointer;}
    .list-head{background-color: #F2F2F2;font-weight: 700;}

</style>
<script>

    //删除
    al.delete = function (id,url,config) {
        if(!id){
            al.tips('未提供id');
            return;
        }
        if(!url){
            al.tips('未提供url');
            return;
        }
        var title = "<div class='subject-title'>删除确认</div>";
        var content = '确实要删除当前数据吗';
        if(config){
            title = config['title'] || title;
            content = config['content'] || content;
        }
        al.confirm({
            title: title,
            content: content
        },function () {
            al.ajax({
                url: url,
                data: $.param({id:id},true),
                callback: function (result, data, msg) {
                    if(config && config['callback']){
                        var callback =config['callback'];
                        var callback_result = false;
                        if((callback instanceof String) || (typeof callback).toLowerCase() == 'string'){
                            callback_result = eval(callback+"(result, data, msg)");
                        }else {
                            callback_result =  callback(result, data, msg);
                        }
                        if(callback_result){return;}
                    }
                    if (result) {
                        if(config && config['container']){
                            $(config['container']).remove();
                        }else{
                            if(Array.isArray(id)){
                                var size = id.length;
                                for(var i=0; i<size; i++){
                                    $("#row_" + id[i]).remove();
                                }
                            }else{
                                $("#row_" + id).remove();
                            }
                        }

                    } else {
                        al.alert(msg);
                    }
                }
            });
        });
    }

</script>