package org.anyline.simple.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@SpringBootTest
public class RedisTest {

    @Autowired
    protected RedisTemplate<String, String> redis;

    @Test
    public void init(){
       Set set = redis.opsForSet().members("jtt808_wait_client");
       for(Object item:set){
           System.out.println(item);
       }
        //Set<String> keys = redis.keys("jtt808_table*");
        //redis.delete(keys);

    }
}
