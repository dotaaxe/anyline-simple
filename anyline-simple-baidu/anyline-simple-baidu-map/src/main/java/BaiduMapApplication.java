import org.anyline.baidu.map.util.BaiduMapClient;
import org.anyline.baidu.map.util.BaiduMapConfig;
import org.anyline.entity.geometry.Coordinate;
import org.anyline.util.BeanUtil;


public class BaiduMapApplication {

	public static void main(String[] args) {
		//在这申请 https://lbsyun.baidu.com/apiconsole/key#/home
		BaiduMapConfig.register("4KKFwk**j0SB8HXNlGVhH","RVNp**7sB1U");
		BaiduMapClient util = BaiduMapClient.getInstance();
		Coordinate point = util.regeo("113.134651","36.195654");
		System.out.println(BeanUtil.object2json(point));
	}

}
