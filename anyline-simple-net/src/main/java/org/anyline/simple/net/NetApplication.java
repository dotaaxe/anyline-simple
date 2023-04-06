package org.anyline.simple.net;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.adapter.KeyAdapter;
import org.anyline.net.*;
import org.anyline.util.DateUtil;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.Date;
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
		test();
	}
	public static void test() throws Exception{
		String url = "https://api.jiandaoyun.com/api/v4/app/636b3a0342e9fa00084ab1ca/entry/63971ce8892298000aa82566/data";
		Map<String,String> headers = new HashMap<>();
		headers.put("Authorization","Bearer Aw4fIl8bc7hr0NdG0goB2p2tjZ5XTdCD");
		DataRow param = new DataRow(KeyAdapter.KEY_CASE.SRC);
		param.put("limit",2);
		HttpResponse response = HttpUtil.post(headers, url, "utf-8", new StringEntity(param.toJSON()));
		HttpClient client = new HttpClient();
		client.setEntity(new StringEntity(param.toJSON()));
		client.setUrl(url);
		client.setHeaders(headers);
		HttpResponse res = client.post();
		System.out.println(res.getText());
		//{"data":[{"creator":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"}
		// ,"updater":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},
		// "deleter":null,"createTime":"2022-12-12T12:23:23.390Z","updateTime":"2022-12-12T12:23:23.390Z",
		// "deleteTime":null,"_widget_1670847721466":1,"_widget_1670847721467":"孙涛"
		// ,"_widget_1670847721469":"男","_id":"63971d3b9ae454000a8cf9d8","appId":"636b3a0342e9fa00084ab1ca"
		// ,"entryId":"63971ce8892298000aa82566"},{"creator":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"updater":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"deleter":null,"createTime":"2022-12-12T12:23:40.774Z","updateTime":"2022-12-12T12:23:40.774Z","deleteTime":null,"_widget_1670847721466":2,"_widget_1670847721467":"张三","_widget_1670847721469":"女","_id":"63971d4c7f10a5000a03b8bf","appId":"636b3a0342e9fa00084ab1ca","entryId":"63971ce8892298000aa82566"},{"creator":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"updater":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"deleter":null,"createTime":"2022-12-12T12:24:07.658Z","updateTime":"2022-12-12T12:24:07.658Z","deleteTime":null,"_widget_1670847721466":3,"_widget_1670847721467":"刘丽","_widget_1670847721469":"女","_id":"63971d67a2e4ab000855bcaa","appId":"636b3a0342e9fa00084ab1ca","entryId":"63971ce8892298000aa82566"},{"creator":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"updater":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"deleter":null,"createTime":"2022-12-12T12:24:21.808Z","updateTime":"2022-12-12T12:24:21.808Z","deleteTime":null,"_widget_1670847721466":4,"_widget_1670847721467":"王琦","_widget_1670847721469":"男","_id":"63971d75959e16000aac3c27","appId":"636b3a0342e9fa00084ab1ca","entryId":"63971ce8892298000aa82566"},{"creator":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"updater":{"name":"HEBE","username":"#admin","status":1,"type":0,"_id":"605c1c6280e9de2aa14e73c3"},"deleter":null,"createTime":"2022-12-14T07:47:43.949Z","updateTime":"2022-12-14T07:47:43.949Z","deleteTime":null,"_widget_1670847721466":5,"_widget_1670847721467":"张无忌","_widget_1670847721469":"男","_id":"63997f9f888442000a58379f","appId":"636b3a0342e9fa00084ab1ca","entryId":"63971ce8892298000aa82566"}]}

		DataRow result = DataRow.parseJson(res.getText());
		DataSet items = result.getSet("data");
		System.out.println(items.size());

		/*
		DataSet inserts = new DataSet();
		for(DataRow item:items){
			DataRow creator = item.getRow("creator");
			DataRow updater = item.getRow("updater");
			String sex = item.getString("_widget_1670847721469");

			DataRow insert = new DataRow();
			insert.copy(item, "ID:_id", "SEX:_widget_1670847721469");
			//需处理的就先处理一下格式
			Date createTime = item.getDate("createTime");
			insert.put("CREATE_TIME", DateUtil.format(createTime, DateUtil.FORMAT_DATE_TIME));
			inserts.add(insert);
		}*/
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
