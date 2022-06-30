package org.anyline.simiple.sms;

import org.anyline.aliyun.sms.util.SMSConfig;
import org.anyline.aliyun.sms.util.SMSResult;
import org.anyline.aliyun.sms.util.SMSTemplate;
import org.anyline.aliyun.sms.util.SMSUtil;
import org.anyline.entity.DataRow;
import org.anyline.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class SMSApplication {
    public static void main(String[] args) throws Exception{
        //签名 和 模板 在这里申请
        //https://dysms.console.aliyun.com/domestic/text/sign
        SpringApplication application = new SpringApplication(SMSApplication.class);
        application.run(args);

        //如果不用配置文件可以临时注册一个
        SMSConfig.register("default", "ACCESS_KEY","ACCESS_SECRET");
        SMSUtil util = SMSUtil.getInstance();

        //模板参数 根据模板内容设置,如:您的验证码${code},5分钟内有效
        Map<String,String> params = new HashMap<>();
        params.put("code","000000");

        SMSResult result = util.send("签名","SMS_88550009","15800000000", params);
        System.out.println(BeanUtil.object2json(result));


        DataRow row = new DataRow();
        row.put("code", "111111");
        row.put("userNmae","张三");
        row.put("userAge","20");
        //验证码:${code},姓名:${name},年龄:${age}
        //这里会根据 属性列表到指定对象中提取属性值 生成模板参数
        result = util.send("签名","SMS_88550009","15800000000", row, "code","name:userNmae","age:userAge");
        System.out.println(BeanUtil.object2json(result));

        //查询发送状态
        result = util.status("15800000000");
        System.out.println(BeanUtil.object2json(result));

        //申请模板 type(0:验证码 1:通知短信 2:推广短信)
        String code = util.template.request("退款申请",1, "订单${order}已申请退款,金额:${price}", "退款申请的短信,请审核通过");

        //查询模板列表 这里的模板状态为AUDIT_STATE_PASS(已审核通过)的才可以使用
        //全部
        List<SMSTemplate> templates = util.template.list();
        System.out.println(BeanUtil.object2json(templates));
        //第1页 每页10条
        templates = util.template.list(1,10);
        System.out.println(BeanUtil.object2json(templates));

        //根据状态
        templates = util.template.list(SMSTemplate.STATUS.AUDIT_STATE_PASS);
        System.out.println(BeanUtil.object2json(templates));
        //根据是否可用
        templates = util.template.list(true);
        System.out.println(BeanUtil.object2json(templates));

        //删除模板
        util.template.delete("SMS_000000");

        //根据编号查询模板信息，主要查询审核状态
        SMSTemplate template = util.template.info("SMS_120405942");

    }
}
