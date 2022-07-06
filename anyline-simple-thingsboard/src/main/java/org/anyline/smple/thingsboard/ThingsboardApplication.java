package org.anyline.smple.thingsboard;


import org.anyline.net.*;
import org.anyline.util.BeanUtil;


public class ThingsboardApplication{
    public static void main(String[] args) throws Exception {
        mqtt();
    }
    public static void mqtt(){
        //curl -v -X POST -d "{\"temperature\": 22.5,"lat":36.089957,"lng":120.373291}" http://192.168.220.102:8080/api/v1/wituEKzWypavSPDcpM0r/telemetry --header "Content-Type:application/json"

        String token = "wituEKzWypavSPDcpM0r";
        String url = "http://192.168.220.101:8080/api/v1/"+token+"/telemetry";
        HttpResult result = HttpBuilder.init().setUrl(url)
                .addHeader("Content-Type","application/json")
                .setEntity(BeanUtil.array2map("temperature","22.5","lat","36.089957","lng","120.373291"))
                .build()
                .post();
        System.out.println("api status:"+result.getStatus());

    }
}
