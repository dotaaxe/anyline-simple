package org.anyline.simple;

import org.anyline.net.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class NetApplication extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(NetApplication.class);
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NetApplication.class);
	}

	public static void main(String[] args) throws Exception {
		//http();
		download();
	}
	public static void http(){
	    String url = "http://www.baidu.com";
	    //读取html
		HttpResult result = HttpUtil.get(url);
		log.warn("status:{}",result.getStatus());
		log.warn("html:{}", result.getText().length());

		//读取cookie
		Map<String, HttpCookie> cookies = result.getCookies();
		for(String key:cookies.keySet()){
			HttpCookie cookie = cookies.get(key);
			log.warn("[cookie][{}={}]", key, cookie.getValue());
		}
		//读取header
		Map<String, String> headers = result.getHeaders();
		for(String key:headers.keySet()){
			log.warn("[header][{}={}]", key, headers.get(key));
		}
		//参数
		Map<String,Object> params = new HashMap<>();
		params.put("id","1");
		result = HttpUtil.get(url, params);
		log.warn("status:{}", result.getStatus());

		//提交header
		result = HttpUtil.get(headers, url, "utf-8");
		log.warn("status:{}", result.getStatus());


		//如果要保持会话需要使用固定的client
		//每次调用使用相同的client
		CloseableHttpClient client = HttpUtil.createClient();
		result = HttpUtil.get(client, url, "utf-8");
		log.warn("status:{}", result.getStatus());
		result = HttpUtil.get(client, url, "utf-8");
		log.warn("status:{}", result.getStatus());


	}
	public static void download(){
		//下载文件
		String url = "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";
		File file = new File("D:\\baidu.png");
		HttpUtil.download(url, file);

		//如果需要显示下载进度
		DownloadProgress process = new DownloadProgress() {
			@Override
			public void init(String url, String thread, long total, long past) {}
			@Override
			public void step(String url, String thread, long finish) {}
			@Override
			public void finish(String url, String thread) {}
			@Override
			public void error(String url, String thread, int code, String message) {}
			@Override
			public void setErrorListener(DownloadListener listener) {}
			@Override
			public void setFinishListener(DownloadListener listener) {}
			@Override
			public int getAction() {return 0;}
			@Override
			public void stop() {}
		};
		HttpUtil.download(process, url, file, true);

		//如果需要多线程下载
		//或控制下载任务启停
		//或需要实时显示下载进度、速度等参数可以创建task
		//task开启后从task获取下载信息
		DownloadTask task = new DownloadTask(url, file);
		task.setThreads(10);		//线程数量
		//task.openLog();				//开启下载日志
		task.start();
		//可以开启一个线程定时读取
		log.warn("下载速度:{}",task.getAvgSpeedFormat());
		log.warn("已耗时:{}", task.getExpendFormat());
		log.warn("预计剩余时间:{}", task.getExpectFormat());
		//[进度:15.08kb/15.08kb(100.0%)][耗时:0毫秒/0毫秒][瞬时:0byte/s/平均:0byte/s][url:][local:]
		log.warn(task.getMessage());
		//停止任务
		task.stop();

		//如果需要执行多个下载任务
		Downloader downloader = new Downloader();
		downloader.add(task);
		downloader.add(task);
		downloader.add(task);
		downloader.start(10);	//开启10个下载线程
	}
}
