package org.anyline.simple.net;

import org.anyline.entity.DataRow;
import org.anyline.entity.adapter.KeyAdapter;
import org.anyline.net.*;
import org.apache.http.entity.StringEntity;
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
@ComponentScan(basePackages = {"org.anyline"})
public class NetApplication extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(NetApplication.class);
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NetApplication.class);
	}

	public static void main(String[] args) throws Exception {
		//String txt = HttpUtil.get("http://zhannei.baidu.com/cse/site?q=%E4%B8%AA%E4%BA%BA%E6%89%80%E5%BE%97&p=1&cc=www.ushui.net").getText();
		//System.out.println(txt);
		//http();
		//download();
		//jtt808();

	}
	public static void jtt808(){

		String clientId = "1111111111"; //终端ID
		//断电
		String url = "http://xxx.com/terminal/control";
		Map<String,Object> map = new HashMap<>();
		map.put("clientId",clientId);
		map.put("command","100");
		HttpResponse result = HttpUtil.post(url,"UTF-8", map);
		System.out.println(result.getStatus());
		System.out.println(result.getText());
		//通电
		map.put("command","101");
		result = HttpUtil.post(url,"UTF-8", map);
		System.out.println(result.getStatus());
		System.out.println(result.getText());

		url = "http://xxx.com/terminal/location?clientId="+clientId;
		result = HttpUtil.get(url);
		System.out.println(result.getStatus());
		System.out.println(result.getText());
	}
	public static void http(){
	    String url = "http://www.baidu.com";
	    //读取html
		HttpResponse result = HttpUtil.get(url);
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
		result = HttpUtil.get(url,"UTF-8", params);
		log.warn("status:{}", result.getStatus());

		//检测http状态
		int status = HttpUtil.status(url);
		log.warn("status:{}", status);

		//对于一些参数比较复杂的请求可以通过 HttpBuilder
		result = HttpBuilder.init()
				.setUrl(url)
				.setHeaders(headers)
				.setParams(params)
				.setCharset("UTF-8")
				.build().post();

		//包括文件上传下载
		Map<String, Object> files = new HashMap<>();
		files.put("idcard", new File(""));
		HttpBuilder.init()
				.setUrl(url)
				.setUploadFiles(files)
				.setParams(params)
				.build().upload();



	}
	public static void download(){
		//简单下载文件
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
	//文件上传
	public static void upload(){
		String url = "";
		Map<String, Object> files = new HashMap<>();
		files.put("idcard", new File(""));
		HttpUtil.upload(url, files);
	}
}
