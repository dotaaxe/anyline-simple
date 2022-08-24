package org.anyline.simple.map;


import org.anyline.entity.MapPoint;
import org.anyline.map.MapProxy;
import org.anyline.util.BeanUtil;
import org.anyline.util.GISUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class MapApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MapApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        MapPoint point = MapProxy.regeo(GISUtil.COORD_TYPE.GCJ02LL, "120.378355","36.06256");
        System.out.println(BeanUtil.object2json(point));
    }
}
