package org.anyline.simple.qq.map;

import org.anyline.entity.MapPoint;
import org.anyline.qq.map.util.QQMapConfig;
import org.anyline.qq.map.util.QQMapUtil;
import org.anyline.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


public class QQMapApplication  {

	public static void main(String[] args) {
		//在这申请 https://lbs.qq.com/dev/console/application/mine-
		QQMapConfig.register("XTWBZ-XEHKD-*****-*****-CQEBF-PYFGN","gKq8Ol3UM********YW7KNXXfdC");
		QQMapUtil util =   QQMapUtil.getInstance();
		MapPoint point = util.regeo("120.3801102573153","36.204793643413055");
		System.out.println(BeanUtil.object2json(point));
	}

}
