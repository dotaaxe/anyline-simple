package org.anyline.simple.spider;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class SeleniumApplication {
    public static void main(String[] args) throws InterruptedException { //谷歌浏览器的本地驱动

        WebDriver driver;
        String baseurl;
        JavascriptExecutor js;
        System.setProperty("webdriver.chrome.driver", "D:\\develop\\chrome\\chromedriver.exe");
        //谷歌浏览器
        driver = new ChromeDriver();
        //设置访问网址
        baseurl = "https://www.baidu.com/";
        //
        //将WebElement类型的driver强制转换为js类型的
        js = (JavascriptExecutor) driver;
        //设置隐性等待
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        //窗口最大化
        driver.manage().window().maximize();
        //打开网址
        //使用Javascript语言打开百度网址
        js.executeScript("window.location='https://www.baidu.com/';");
        System.out.println("执行Javascript命令完成");
        //等待3秒钟
        Thread.sleep(3000);
        //查找元素
        //向下转型，将WebElement 转换为object
        WebElement TestBOx = (WebElement) js.executeScript(
                "return document.getElementById('kw');");
        System.out.println("执行Javascript，根据ID查找元素完成");
        TestBOx.sendKeys("test");
        //等待
        Thread.sleep(3000);
        //关闭浏览器
        driver.quit();
    }
}
