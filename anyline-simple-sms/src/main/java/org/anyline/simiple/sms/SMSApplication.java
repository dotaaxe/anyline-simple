package org.anyline.simiple.sms;

import org.anyline.aliyun.sms.util.SMSConfig;
import org.anyline.aliyun.sms.util.SMSResult;
import org.anyline.aliyun.sms.util.SMSUtil;
import org.anyline.entity.DataRow;
import org.anyline.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;


@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class SMSApplication {
    public static void main(String[] args) throws Exception{
        //签名 和 模板 在这里申请
        //https://dysms.console.aliyun.com/domestic/text/sign
        SpringApplication application = new SpringApplication(SMSApplication.class);
        application.run(args);

        SMSConfig.register("default", "ACCESS_KEY","ACCESS_SECRET");
        SMSUtil util = SMSUtil.getInstance();
        Map<String,String> params = new HashMap<>();
        params.put("code","000000");
        DataRow row = new DataRow();
        row.put("code", "111111");
        SMSResult result = util.send("签名","SMS_88550009","15800000000", row, "code");
        System.out.println(BeanUtil.object2json(result));
        result = util.send("签名","SMS_88550009","15800000000", params);
        System.out.println(BeanUtil.object2json(result));

        //查询发送状态
        result = util.status("15800000000");
        System.out.println(BeanUtil.object2json(result));
    }
}
