package org.anyline.simple.qq.map;

import org.anyline.entity.MapPoint;
import org.anyline.qq.map.util.QQMapConfig;
import org.anyline.qq.map.util.QQMapUtil;
import org.anyline.util.BeanUtil;


public class QQMapApplication  {

	public static void main(String[] args) {
		//在这申请 https://lbs.qq.com/dev/console/application/mine-
		QQMapConfig.register("XTWBZ-XEHKD-*****-*****-CQEBF-PYFGN","gKq8Ol3UM********YW7KNXXfdC");
		QQMapUtil util =   QQMapUtil.getInstance();
		//逆地址解析
		MapPoint point = util.regeo("120.3801102573153","36.204793643413055");
		System.out.println(BeanUtil.object2json(point));

		//IP物理地址解析
		String ip = "150.158.106.189";
		point = util.ip(ip);
		System.out.println(BeanUtil.object2json(point));

	}

}
