package org.anyline.simple.encrypt;

import org.anyline.util.encrypt.MD5Util;
import org.anyline.util.encrypt.RSAUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@SpringBootTest
public class EncryptTest {
    private static Logger log = LoggerFactory.getLogger(EncryptTest.class);
    @Test
    public void md5(){
        String src = "000000";
        log.warn("src:{}", src);
        String md5 = MD5Util.crypto(src,"UTF-8");
        log.warn("md5:{}", md5);
        //执行两次的意思
        String md52 = MD5Util.crypto2(src,"UTF-8");
        System.out.println(md52);
        log.warn("md52:{}", md52);
        Assertions.assertEquals(md5,"670b14728ad9902aecba32e22fa4f6bd");
        Assertions.assertEquals(md52,"ff92a240d11b05ebd392348c35f781b2");
    }
    @Test
    public void rsa() throws Exception{
        //创建密钥对
        KeyPair keys = RSAUtil.create();
        //分别获取私钥公钥
        //一般用公钥加密，私钥解密 或 私钥签名，公钥验签
        PrivateKey privateKey = keys.getPrivate();
        PublicKey publicKey = keys.getPublic();
        //如果需要交换密钥一般生成base64
        String private64 = RSAUtil.base64(privateKey);
        String public64 = RSAUtil.base64(publicKey);
        log.warn("私钥:{}", private64);
        log.warn("公钥:{}", public64);

        //在收到密钥string后需要生成密钥
        privateKey = RSAUtil.createPrivateKey(private64);
        publicKey = RSAUtil.createPublicKey(public64);

        String src = "000000";
        log.warn("明文:{}", src);
        String encode = RSAUtil.encrypt(src, publicKey);
        log.warn("公钥加密:{}", encode);
        encode = RSAUtil.encrypt(src, public64);
        log.warn("公钥加密:{}", encode);

        String decode = RSAUtil.decrypt(encode, privateKey);
        log.warn("私钥解密:{}", decode);
        Assertions.assertEquals(src, decode);
        decode = RSAUtil.decrypt(encode, private64);
        log.warn("私钥解密:{}", decode);
        Assertions.assertEquals(src, decode);

        String sign = RSAUtil.sign(src, privateKey);
        log.warn("私钥签名:{}", sign);
        sign = RSAUtil.sign(src, private64);
        log.warn("私钥签名:{}", sign);

        boolean verify = RSAUtil.verify(src, publicKey, sign);
        log.warn("公钥验签:{}", verify);
        Assertions.assertTrue(verify);
        verify = RSAUtil.verify(src, public64, sign);
        log.warn("公钥验签:{}", verify);
        Assertions.assertTrue(verify);
    }
}
