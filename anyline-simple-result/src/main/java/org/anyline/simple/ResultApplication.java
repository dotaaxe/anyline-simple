package org.anyline.simple;

import org.anyboot.jdbc.ds.DynamicDataSourceRegister;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.html.Table;
import org.anyline.entity.html.TableBuilder;
import org.anyline.service.AnylineService;
import org.anyline.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
@Import(DynamicDataSourceRegister.class)
public class ResultApplication extends SpringBootServletInitializer {
	private static Logger log = LoggerFactory.getLogger(ResultApplication.class);
	private static AnylineService service;
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(ResultApplication.class);
	}

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(ResultApplication.class);
		//给每个人随机生成1-12月份薪资
		application.addListeners(new ApplicationAware());
		ConfigurableApplicationContext context = application.run(args);
		service = (AnylineService)context.getBean("anyline.service");

		pivot();
		//result();


	}

	public static void pivot(){


		DataSet set = service.querys("<iot>rpt_check_result");


		String union = "ITEM_NAME";					//需要合同的列
		String pks = "ITEM_NAME,FIELD_NAME";		//逻辑主键
		String clazzKey = "RT";						//根据RT列的值把行转成列
		String valueKey = "VAL";					//取值


		String headers = "检测项,属性";				//表头(与逻辑主键对应)
		String valueLabel = "检测值";				//表头(与取值列对应)

		DataSet ds = set.pivot(pks, clazzKey, valueKey);
		List<String> clazzList = set.getDistinctStrings(clazzKey);			//取出不重复的clazzKey值列表

		Table table = TableBuilder.init()
				.setDatas(ds)												//设置数据源
				.setReplaceEmpty("#")										//替换空值成#
				.setHeaders(headers.split(",")).addHeaders(clazzList)	//添加表头
				.setFields(pks.split(",")).addFields(clazzList)		//需要显示的属性与表头要对应
				.addUnions(union)											//需要合并的列
				.build();

		//table.addRows(2);
		//table.addColumns(2);

		String html = table.build();
		System.out.println(html);
		System.out.println(ds.toJSON());

		System.out.println("<br/><br/>");
		//html += "<table style='width:100%;'><tr><td colspan='2'>临时表</td></tr><tr><td>1</td><td>2</td></tr></table>";


		union = "ITEM_NAME";				//需需合并的列
		pks = "ITEM_NAME,RT";				//逻辑主键
		clazzKey = "FIELD_NAME";			//根据FIELD_NAME把行转成列
		valueKey = "VAL";					//取值

		headers = "检测项,频次";				//表头(与逻辑主键对应)
		valueLabel = "检测值";				//表头(与取值列对应)
		set.order("ITEM_NAME");				//排序
		ds = set.pivot(pks, clazzKey,valueKey);

		clazzList = set.getDistinctStrings(clazzKey);

		html = TableBuilder.init()
				.setDatas(ds)															//设置数据源
				.setReplaceEmpty("#")													//替换空值
				.setHeaders(headers.split(",")).addHeaders(clazzList)				//设置表头
				.setFields(pks.split(",")).addFields(clazzList)					//需要显示的属性值与表头要对应
				.addUnions(union)														//合并列
				.build().build();
		System.out.println(html);
		System.out.println(ds.toJSON());

		System.out.println("<br/><br/>");


		union = "ITEM_NAME";				//需要合并的列
		pks = "ITEM_NAME,FIELD_NAME";		//逻辑主键
		clazzKey = "RT";					//根据RT把行转成列
		valueKey = "VAL,REF,CHK";			//多列取值
		headers = "检测项,属性";				//表头(与逻辑主键对应)
		valueLabel = "检测值,参考值,判定结果";  //表头(与取值列对应)
		set.order("ITEM_NAME");

		ds = set.pivot(pks, clazzKey,valueKey);
		clazzList = set.getDistinctStrings(clazzKey);
		//组合表头与取值列
		List<List<String>> fieldArrays = BeanUtil.descartes(clazzList, BeanUtil.array2list(valueKey.split(",")));
		List<List<String>> headArrays = BeanUtil.descartes(clazzList, BeanUtil.array2list(valueLabel.split(",")));
		List<String> fields = new ArrayList<>();
		List<String> heads = new ArrayList<>();
		heads.addAll(BeanUtil.array2list(headers.split(",")));
		fields.addAll(BeanUtil.array2list(pks.split(",")));
		for(List<String> headArray:headArrays){
			String head = BeanUtil.concat(headArray,"-");
			heads.add(head);
		}
		for(List<String> fieldArray:fieldArrays){
			String field = BeanUtil.concat(fieldArray,"-");
			fields.add(field);
		}
		html = TableBuilder.init()
				.setDatas(ds)
				.setReplaceEmpty("#")
				.setHeaders(heads)
				.setFields(fields)
				.addUnions(union)
				.build().build();

		System.out.println(html);
		System.out.println("<br/><br/>");
		System.out.println(ds.toJSON());


		union = "ITEM_NAME";				//需要合并的列
		pks = "ITEM_NAME,FIELD_NAME";		//逻辑主键
		clazzKey = "RT";					//根据RT把行转成列
		valueKey = "VAL,REF,CHK";			//多列取值
		headers = "检测项,属性";				//表头(与逻辑主键对应)
		set.order("ITEM_NAME");
		int vol = 2; 						//每2次一组
		clazzList = set.getDistinctStrings(clazzKey);
		int size = clazzList.size();
		html = "";
		int page = (size-1)/vol+1;
		for(int i=0; i<page; i++){
			List<String> pageClazzList = BeanUtil.cuts(clazzList, i*vol, i*vol+vol-1);
			ds = set.select.in(clazzKey, pageClazzList).pivot(pks, clazzKey,valueKey);
			fieldArrays = BeanUtil.descartes(pageClazzList, BeanUtil.array2list(valueKey.split(",")));
			headArrays = BeanUtil.descartes(pageClazzList, BeanUtil.array2list(valueLabel.split(",")));
			fields = new ArrayList<>();
			heads = new ArrayList<>();
			heads.addAll(BeanUtil.array2list(headers.split(",")));
			fields.addAll(BeanUtil.array2list(pks.split(",")));
			for(List<String> headArray:headArrays){
				String head = BeanUtil.concat(headArray,"-");
				heads.add(head);
			}
			for(List<String> fieldArray:fieldArrays){
				String field = BeanUtil.concat(fieldArray,"-");
				fields.add(field);
			}
			System.out.println(ds.toJSON());
			html += "<div>第"+(i+1)+"页</div>";
			html += TableBuilder.init().setHeaders(heads)
					.setDatas(ds).setFields(fields).addUnions(union)
					.setReplaceEmpty("#")
					.build().build();
		}


		System.out.println(html);

		union = "ITEM_NAME";			//需要合并的列
		pks = "ITEM_NAME,RT";			//逻辑主键
		clazzKey = "FIELD_NAME";		//根据FIELD_NAME把行转成列
		valueKey = "VAL,REF,CHK";		//多列取值
		headers = "检测项,频次";			//表头(与逻辑主键对应)
		set.order("ITEM_NAME");

		ds = set.pivot(pks, clazzKey,valueKey);
		clazzList = set.getDistinctStrings(clazzKey);
		headArrays = BeanUtil.descartes(clazzList, BeanUtil.array2list(valueLabel.split(",")));
		fieldArrays = BeanUtil.descartes(clazzList, BeanUtil.array2list(valueKey.split(",")));
		heads = new ArrayList<>();
		fields = new ArrayList<>();
		heads.addAll(BeanUtil.array2list(headers.split(",")));
		fields.addAll(BeanUtil.array2list(pks.split(",")));
		for(List<String> headArray:headArrays){
			String head = BeanUtil.concat(headArray,"-");
			heads.add(head);
		}
		for(List<String> fieldArray:fieldArrays){
			String field = BeanUtil.concat(fieldArray,"-");
			fields.add(field);
		}
		html = TableBuilder.init()
				.setDatas(ds)
				.setReplaceEmpty("#")
				.setHeaders(heads)
				.setFields(fields)
				.addUnions(union)
				.build().build();
		System.out.println(html);
		System.out.println(ds.toJSON());
/*		WDocument doc = doc("a");
		doc.replace("tab", html);
		doc.save();*/


		System.exit(0);
	}

	//更多参考 http://doc.anyline.org/a?id=p298pn6e9o1r5gv78ac1vice624c62387fc25603ac848d20e44d3e0a75448cfe7d
	public static void result(){
		//薪资列表
		DataSet set = service.querys("V_HR_SALARY");
		log.warn("集合长度:{}",set.size());
		//抽取不重复的年月列表
		List<String> yms = set.getDistinctStrings("YM");
		int i=0;
		for(String ym:yms){
			log.warn("ym:{}", ym);
			if(i++>=10) {
				break;
			}
		}
		//如果需要多列 不重复的 姓名+年月
		DataSet eyms = set.distinct("EMPLOYEE_NM","YM");
		i = 0;
		for(DataRow eym:eyms){
			log.warn("[姓名:{}][年月:{}]", eym.getString("EMPLOYEE_NM"), eym.getString("YM"));
			if(i++>=10) {
				break;
			}
		}
		//根据部门ID和部门名称分组
		DataSet groups = set.group("DEPARTMENT_ID","DEPARTMENT_NM");
		log.warn("groups:{}", groups.size());
		for(DataRow group:groups){
			DataSet items = group.getItems();
			log.warn("[部门:{}][条目:{}]\n[top:{}]", group.getString("DEPARTMENT_NM"), items.size(), items.getRow(0).toJSON());
		}

		//筛选符合条件的集合
		//注意如果String类型 1与1.0比较不相等, 可以先调用convertNumber转换一下数据类型
		//key1,value1,key2:value2,key3,value3
		//"NM:zh%","AGE:>;20","NM","%zh%"

		//抽取人员ID=1的薪资列表
		DataSet items = set.getRows("EMPLOYEE_ID","1");
		log.warn("size:{}", items.size());
		//也可以用:分隔
		items = set.getRows("EMPLOYEE_ID:1");
		log.warn("size:{}", items.size());

		//多条件筛选
		items = set.getRows("EMPLOYEE_ID","2","DEPARTMENT_ID","1");
		log.warn("size:{}", items.size());

		//模糊查询
		items = set.getRows("BASE_PRICE:>12000","EMPLOYEE_NM:%雪%");
		log.warn("size:{}", items.size());

		//更复杂的条件可以通过select筛选
		items = set.select.between("BASE_PRICE", 10000,12000);
		log.warn("size:{}", items.size());

		items = set.select.like("EMPLOYEE_NM","%雪%");
		log.warn("size:{}", items.size());

		//指定佛为空的集合(任何一列不为空的都不返回)
		items = set.select.empty("EMPLOYEE_NM","EMPLOYEE_ID");
		log.warn("size:{}", items.size());

		items = set.select.lessEqual("BASE_PRICE",10000);
		log.warn("size:{}", items.size());

		items = set.select.in("DEPARTMENT_ID","1","2");
		log.warn("size:{}", items.size());

	}

}
