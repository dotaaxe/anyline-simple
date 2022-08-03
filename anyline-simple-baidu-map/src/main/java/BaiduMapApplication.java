import org.anyline.baidu.map.util.BaiduMapConfig;
import org.anyline.baidu.map.util.BaiduMapUtil;
import org.anyline.entity.MapPoint;
import org.anyline.util.BeanUtil;


public class BaiduMapApplication {

	public static void main(String[] args) {
		//在这申请 https://lbsyun.baidu.com/apiconsole/key#/home
		BaiduMapConfig.register("YuFLj**************0PnOzEhBAKh","gKq8Ol3UM********YW7KNXXfdC");
		BaiduMapUtil util =   BaiduMapUtil.getInstance();
		MapPoint point = util.regeo("120.3801102573153","36.204793643413055");
		System.out.println(BeanUtil.object2json(point));
	}

}
