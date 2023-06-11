package org.anyline.simple.map;


import org.anyline.entity.geometry.Coordinate;
import org.anyline.map.MapProxy;
import org.anyline.util.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class MapApplication {

    public static void main(String[] args) {
        //注意直辖市的区级(相当于省下的市级)返回编号与国家统计局编码不一致,但再下一级一致
        // 如北京朝阳区 统计局：110105  地图接口返回1101
        // 因为在统计局编码中 北京市一级是 市辖区 1101  再下一级是朝阳区
        // 一般需要查出最低一级编码后与标签库对比后修正上几级编码
        SpringApplication application = new SpringApplication(MapApplication.class);
        ConfigurableApplicationContext context = application.run(args);
        Coordinate coordinate = MapProxy.regeo(Coordinate.TYPE.BD09LL, "115.9893280","40.4673940");
        System.out.println(BeanUtil.object2json(coordinate));
    }
}
