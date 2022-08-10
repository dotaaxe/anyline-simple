package org.anyline.smple.thingsboard;


import org.anyline.entity.DataSet;
import org.anyline.net.*;
import org.anyline.thingsboard.util.ThingsBoardClient;
import org.anyline.util.BasicUtil;
import org.anyline.util.BeanUtil;
import org.anyline.util.ConfigTable;
import org.anyline.util.DateUtil;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.asset.Asset;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.page.SortOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThingsboardApplication{

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        String token = BasicUtil.getRandomString(16);
        ThingsBoardClient client = ThingsBoardClient.getInstance();
        //创建设备
        Device device = new Device();
        device.setName("测试设备("+ DateUtil.format() +")");
        device = client.saveDevice(device);
        String deviceId = device.getId().getId().toString();
        System.out.println("[创建新设备][name:"+device.getName()+"][id:"+deviceId+"]");

        device.setName("测试设备(改名)("+ DateUtil.format() +")");
        device = client.saveDevice(device);
        deviceId = device.getId().getId().toString();
        System.out.println("[创建新设备][name:" + device.getName() + "][id:" + deviceId + "]");


        //重名会抛出异常
        device = new Device();
        device.setName("测试设备(带token)("+ DateUtil.format() +")");
        device = client.createDevice(device, token);
        deviceId = device.getId().getId().toString();
        System.out.println("[创建新设备][name:"+device.getName()+"][id:"+deviceId+"][token:"+token+"]");


        //curl -v -X POST -d "{\"temperature\": 22.5,"lat":36.089957,"lng":120.373291}" http://192.168.220.102:8080/api/v1/wituEKzWypavSPDcpM0r/telemetry --header "Content-Type:application/json"
        //根据token上传遥测数据
        String url = "http://192.168.220.101:8080/api/v1/"+token+"/telemetry";
        HttpResponse response = HttpBuilder.init().setUrl(url)
                .addHeader("Content-Type","application/json")
                .setEntity(BeanUtil.array2map("temperature","122.5","lat","136.089957","lng","1120.373291"))
                .build()
                .post();
        System.out.println("[HTTP添加遥测数据][status:"+response.getStatus()+"]");

        //添加遥测数据
        Map<String,Object> row = new HashMap();
        row.put("lat","test-lat:"+System.currentTimeMillis());
        row.put("lng","test-lng:"+System.currentTimeMillis());
        boolean result = client.saveDeviceTelemetry(deviceId, System.currentTimeMillis(), row);
        System.out.println("[添加遥测数据][result:"+result+"]");

        //添加遥测数据(批量)
        List<Map<?,?>> list = new ArrayList<>();
        for(int i=0;i <100; i++){
            Map<String,Object> map = new HashMap();
            Map<String,Object> values = new HashMap<>();
            values.put("lat","test-lat:"+i+":"+System.currentTimeMillis());
            values.put("lng","test-lng:"+i+":"+System.currentTimeMillis());
            values.put("speed", BasicUtil.getRandomNumber(10,100));
            map.put("ts", System.currentTimeMillis()+1000+i); //不要添加相同时间的数据 会覆盖
            map.put("values", values);
            list.add(map);
        }
        result = client.saveDeviceTelemetry(deviceId, list);
        System.out.println("[添加遥测数据(批量)][result:"+result+"]");
        //最近的遥测数据
        DataSet set = client.getLatestDeviceTimeseries(deviceId,"lat,lng");
        System.out.println("[最近遥测数据]"+set.toJSON());
        //时间段遥测数据
        //每页多少条 是指每个key多少条 这里指定了2个key 第个key3行数据  一共返回2*3=6行数据
        //[{"TS":1657361433409,"KEY":"lat","VALUE":"test-lat:99:1657361433310"},{"TS":1657361433408,"KEY":"lat","VALUE":"test-lat:98:1657361433310"},{"TS":1657361433407,"KEY":"lat","VALUE":"test-lat:97:1657361433310"},{"TS":1657361433409,"KEY":"lng","VALUE":"test-lng:99:1657361433310"},{"TS":1657361433408,"KEY":"lng","VALUE":"test-lng:98:1657361433310"},{"TS":1657361433407,"KEY":"lng","VALUE":"test-lng:97:1657361433310"}]

        set = client.getDeviceTimeseries(deviceId, "lat,lng",  System.currentTimeMillis()-100000, System.currentTimeMillis()+2000, 10);
        System.out.println("[时间段遥测数据][size:"+set.size()+"]"+set.toJSON());

        //第3毫秒分一组，算出平均值
        set = client.getTimeseries( EntityType.DEVICE, deviceId, "speed", 3L,
                Aggregation.AVG, SortOrder.Direction.DESC, System.currentTimeMillis()-100000, System.currentTimeMillis()+2000, 33, true);
        System.out.println("[时间段遥测聚合数据][size:"+set.size()+"]"+set.toJSON());



        client.deleteDevice(device.getId());
        System.out.println("[删除测试设备:"+device.getName()+"]");
        System.exit(0);
    }
}
