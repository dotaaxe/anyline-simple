package org.anyline.simple.office;


import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.html.TableBuilder;
import org.anyline.net.HttpUtil;
import org.anyline.office.docx.entity.WDocument;
import org.anyline.office.docx.entity.Wtable;
import org.anyline.office.docx.entity.Wtr;
import org.anyline.service.AnylineService;
import org.anyline.util.DateUtil;
import org.anyline.util.FileUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class WordApplication extends SpringBootServletInitializer {

	private static AnylineService service;
	private static File dir = null;
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WordApplication.class);
	}

	public static void main(String[] args) {

		String path = WordApplication.class.getResource("").getPath();
		dir = new File(path.substring(0,path.indexOf("target")),"/src/main/template");

		//SpringApplication application = new SpringApplication(WordApplication.class);
		//ConfigurableApplicationContext context = application.run(args);
		//service = (AnylineService)context.getBean("anyline.service");

		//简单替换书签、关键字
		//start();

		//html/css 转word
		//html();

		//支持<style>table{border:none}</style>标签
		//style();

		//对于复杂的表头 可以通过合并多个文档生成 <word bookmark="bm_table_td">D:\office\template\td.docx</word>
		//merge();

		//插入表格
		//table();

		//通过数据生成table(主要演示合并单元格)
		//data();

		//对角线拆分单元格
		//split();

		//排版方向(横版、竖版)
		//orient();

		//如果模版中已经有了表格，可以直接操作原生表格,先通过标签提取原生表格
		//原生表格操作
		//src();

		//在原生表格上插入行或列
		//主要演示复制模版行样式
		//insert();

		//签章等浮动图片
		floatImg();
		System.exit(0);
	}
	public static void floatImg(){

		/*
		position:			:定位方式
			fixed			:相对页面左上定位
			relative		:相对其他元素定位(需要设置relative-x,relative-y)

		offset-x			:水平偏移量
		offset-y			:垂直偏移量

		relative-x			:水平偏移参照
			character		:相对于锚点在运行内容中的位置
			column			:相对于包含锚的列的范围
			insideMargin	:相对于奇数页的左边距，偶数页的右边距
			leftMargin		:相对于左边距
			margin			:相对于页边距
			outsideMargin	:相对于奇数页的右边距，偶数页的左边距
			page			:相对于页面边缘
			rightMargin		:相对于右边距

		relative-y			:垂直偏移参照
			bottomMargin	:相对于底部边距
			insideMargin	:相对于当前页面的内边距
			line			:相对于包含锚字符的行
			margin			:相对于页边距
			outsideMargin	:相对于当前页面的外边距
			page			:相对于页面边缘
			paragraph		:相对于包含锚的段落
			topMargin		:相对于上边距
		z-index				:如果有多个元素重叠可以设置顺序
		*/
		//以右边距为参照 向左偏移150px
		//以书签所在行为参照 向上偏移100px 注意这里经常需要根据所在行为参照,当前行被其他内容挤下去的时候签章应该跟随
		WDocument doc = doc("float");
		File sign = new File(dir,"sign.png");
		doc.replace("img_sign","<img src='"+sign.getAbsolutePath()+"' style='width:163px;height:128px;position:relative;offset-x:-200px;offset-y:-80px;relative-x:rightMargin;relative-y:line;'/>");
		doc.replace("ymd_sign", DateUtil.format("yyyy-MM-dd"));
		doc.save();

	}
	public static void insert(){
		WDocument doc = doc("insert");
		Wtable table = doc.getTable("src_tc");
		//同样的可以获取一个tr,也可以通过下标获取
		Wtr template = table.getTr("src_tc");

		//追加2行 追加的行将复制上一行的样式(如背景色字体等)
		table.addRows(2);
		//注意这里在第1行位置接入行，将复制第0行的样式,但第0行是表头,所以新插入的行会带表头样式
		table.addRows(1,2);
		//但我们希望的是复制第1行数据行的样式,这种情况一般是设置一个数据行模板在插入行时指定模板行
		//也可以直接在模板行后追加，最后再删除模板行

		//删除行
		table.remove(template);
		table.remove(1);



		//插入一行数据(注意这里要保持每行列数量一致)
		table.insert(2,template, "<tr><td>1</td><td>2</td><td>3</td><td>4</td></tr>");
		//支持多个tr合并插入
		table.insert(1,template, "<tr><td>10</td><td>20</td><td>30</td><td>40</td></tr><tr><td>100</td><td>200</td><td>300</td><td>400</td></tr>");

		//如果不指定下标，则追加到最后一行
		table.insert(template, "<tr><td>110</td><td>120</td><td>130</td><td>140</td></tr>>");


		//插入行 同时 填充数据
		DataSet set = new DataSet();
		DataRow row = new DataRow();
		row.put("VERSION","V1.0");
		row.put("YMD", DateUtil.format("yyyy-MM-dd"));
		row.put("DEPARTMENT","市场部");
		row.put("PURPOSE","参考");
		set.add(row);

		table.insert(1,template,set,"VERSION","YMD","DEPARTMENT","PURPOSE");

		//没有下标球追加到最后一行
		row.put("VERSION","V1.1");
		table.insert(row,"VERSION","YMD","DEPARTMENT","PURPOSE");

		row.put("VERSION","V1.2");
		table.insert(template,row,"VERSION","YMD","DEPARTMENT","PURPOSE");


		//追加一列 追加的列装复制前一列的样式(如背景色字体等)
		table.addColumns(1);

		//在第0列位置抛入2列 如果是第0列 则复制后一列的样式
		table.insertColumns(0,2);
		doc.save();

	}
	/**
	 * 如果模版中已经有了表格，可以直接操作原生表格,先通过标签提取原生表格
	 * 演示操作单元格、增加行列、合并单元格
	 * 单元格内容设置重点还是html/css转word
	 * 以下操作基本上在 单元格、行、表上都有对应的函数,如设置字号，可以根据行列设置，也可以设置整行中所有单元格，也可以设置整个表格中所有单元格
	 */
	public static void src(){
		WDocument doc = doc("src");
		//通过书签定位到一个table,只要这个书签在table标签范围内即可
		Wtable table = doc.getTable("src_tc");
		int row =1 ;
		int col = 1;

		//根据下标设置一个单元格内容
		String ymd = DateUtil.format("yyyy-MM-dd");
		table.setText(row, col, ymd);

		//设置内容支持html标签(注意：这里是setHtml而不是setText)
		table.setHtml(row, col, "<span style='color:green;background-color:red;'>"+ymd+"</span>");
		//可以设置一张图片
		table.setHtml(1,2,"<img src='http://img.baidu.com/img/logo-80px.gif' style='width:100px;height:50px;'/>");

		//如果有样式比较多可以放在styles中
		Map<String,String> styles = new HashMap<>();
		styles.put("color","blue");
		styles.put("underline","none");	//覆盖原版本中的下划线样式 none或false
		styles.put("italic", "false");	//覆盖原版本中的斜体样式
		table.setText(row, col, ymd, styles);

		//覆盖样式也可以直接通过单元格设置
		table.setItalic(row, col, true);	//设置单元格非斜体
		//也可以设置一整行
		table.setItalic(row, true);	//设置单元格非斜体
		table.setColor(row, "red");
		table.removeColor();

		//替换内容(在单元格范围内)
		table.replace(row,3,"编制", "编辑");
		//替换内容(在一行范围内)
		table.replace(row,"编辑", "编制");
		//替换内容(在整个表范围内)
		table.replace("编制", "最终编制");

		//合并单元格(注意最后2个参数是rowspan,colspan而不是结束行列)
		//table.merge(1,0,1,2);

		//设置整个表背景色
		table.setBackgroundColor(row, col,"#AAAAAA");

		//设置字体颜色
		table.setColor(row, col,"blue");

		//设置边距
		table.setTopPadding(row, col, "10px");
		table.setPadding("10px");

		//设置对齐方式
		table.setVerticalAlign("top");
		table.setVerticalAlign(row, col,"center");

		//设置水平对齐方式
		table.setAlign("right");
		table.setAlign(row, col,"left");

		//设置行高
		table.setHeight(row,"50px");
		//设置整列宽度
		table.setWidth(col,"100px");


		table.setFontSize(row, col, "一号");
		table.setFontSize(row, col, "30pt");
		table.setFontFamily(row, col, "黑体");

		//粗体、斜体、下划线、删除线
		table.setBold(row, col, true);
		table.setUnderline(row, col, true);
		table.setStrike(row, col, true);
		table.setItalic(row, col, true);


		//删除样式、边框、背景
		table.removeBackgroundColor(row, col);
		table.removeColor(row, col);
		table.removeStyle(row, col);

		//追加内容
		table.addText(row, col, "(注)");

		//设置边框(边框线条以4为一个单位)
		table.setTopBorder(row, col, 8, "red","double");
		table.setLeftBorder(row, col, 8, "red","single");
		table.setBottomBorder(row,8, "red","single");

		//清除边框
		table.removeRightBorder(row, col);
		//清除第1行下边框(注意这里是行)
		table.removeBottomBorder(row);
		//清除所有行第1列左边框(注意这里是列)
		table.removeLeftBorder(col);

		//清除表格左边框(注意这里是表格，而不是所有单元格)
		table.removeLeftBorder();
		//清除表格边框(注意是表格是不单元格)
		table.removeBorder();

		//这里才是清除所有单元格边框
		table.removeTcBorder();

		//设置第1行所有边框(不含对角线)
		table.setBorder(1);
		//设置所有单元格边框(不含对角线)
		table.setCellBorder();


		//对角线拆分单元格
		table.setTl2brBorder(0,1,"右上","左下");
		doc.save();
	}

	/**
	 * 通过数据生成表格
	 * 这里主要演示 全并单元格，以及依赖合并
	 * 与excel导出主用到相同的TableBuilder
	 * 稍有区别的是在word中 table有宽度属性
	 */
	public static void data(){
		DataSet set = service.querys("V_HR_SALARY","YYYY:"+ (DateUtil.year()-1), "ORDER BY EMPLOYEE_ID, YM");
		TableBuilder builder = TableBuilder.init()
				.setDatas(set)									//设置数据源
				.setFields(										//需要导出的列
						"{num}(EMPLOYEE_NM)"					//{num}表示序号,(DEPARTMENT_NM)表示根据哪一列计算序号，这里部门名称需要分组合并，所以num不是按行计算
						,"DEPARTMENT_NM"
						,"EMPLOYEE_NM"
						,"YM"
						,"BASE_PRICE")
				.addUnion(										//需要合并的列
						"DEPARTMENT_NM"							//如果部门名称相同则合并
						,"EMPLOYEE_NM(DEPARTMENT_NM)"
						,"YM(DEPARTMENT_NM)"					//如果月份相同则合并，前提是部门已经合并
				)
				.setWidth("100%")
				//单独设置一个表头、并在每页重复
				.setHeader("<tr style='repeat-header:true;background-color:#CCCCCC;'><td>序号</td><td>部门</td><td>姓名</td><td>月份</td><td>薪资</td></tr>")
				.setReplaceEmpty("/")							//如果值为空则以/代替
				.addIgnoreUnionValue("/")						//不参与合并的值
				.setCellBorder(true)							//设置默认边框
				.setMergeCellHorizontalAlign("center")			//设置合并的列 水平对齐方式
				.setMergeCellVerticalAlign("top")				//设置合并的列 垂直对齐方式
				.setEmptyCellHorizontalAlign("center")			//设置空单元格 水平对齐方式(为空时有可能需要替换成其他值)
				.setEmptyCellVerticalAlign("top")				//设置空单元格 垂直对齐方式
				.setHorizontalAlign("YM","center")	//设置月份列 水平对齐方式
				.setVerticalAlign("middle")						//设置所有数据单元格 垂直对齐方式
				.setLineHeight("50px")							//设置数据区域行高
				.setWidth("YM","200px")				//设置月份列 宽度
				;
		String table = builder.build().build();
		WDocument doc = doc("data");
		doc.replace("html_table", table);
		doc.save();
	}
	/**
	 	word中可以调整排版方向(横版、竖版)，但在html中没有对应的标签和样式。可以通过page-size-orient的样式来设置

	 	放在内容之后,影响在此标签之前所有的页面,直到遇到更前一个 page-size-orient

		page-size-orient:landscape表示横版
		page-size-orient:portrait表示竖版
		也可以设置页面尺寸
		page-size-w:页面宽
		page-size-h:页面高
		page-margin-top:页面上边距
		page-margin-right:页面右边距
		page-margin-bottom:页面下边距
		page-margin-left:页面左边距
		page-margin-header:页头
		page-margin-footer:页脚

		如果只设置一个方向,宽高会取默认值
	 */
	public static void orient(){

		String table = FileUtil.read(new File(dir,"table.html")).toString();
		//这里是要插入一个表格，先通过portrait实现表格之前的内容竖版
		//在表格之后插入一个landscape，实现表格横版
		String html = "<div style='page-size-orient:portrait'></div>" + table +"<div style='page-size-orient:landscape'></div>";

		WDocument doc = doc("orient");
		doc.replace("html_table", html);
		doc.save();
	}

	/**
	 * 如果样式比较复杂，如单元格拆分成多部分，可以先手工生成子模板，再将子模板内容合并到主模板中
	 */
	public static void merge(){
		//<word bookmark="bm_table_td">D:\\office\\template\\td.docx</word>
		//通过标签word合并子模板  word标签体表示需要并的子模板path, bookmark：以书签标签子模板中需要被合并的部分
		String html = FileUtil.read(new File(dir,"merge.html")).toString();

		File subTemplate = new File(dir, "td.docx");
		html = html.replace("{template_path}", subTemplate.getAbsolutePath());

		WDocument doc = doc("merge");
		doc.replace("html_table", html);
		doc.save();
	}
	public static void style(){
		/*
		内容太多，直接从文件中读取,内容类似于
		<style>
			table,td{
				border:1px solid #2F74B5;
				border-collapse:collapse;
			}
		</style>
		<table><tr><td></td></tr></table>
		* */

		String html = FileUtil.read(new File(dir,"table.html")).toString();
		WDocument doc = doc("style");
		doc.replace("html_table", html);
		doc.save();
	}

	/**
	 * 这里的table要求html语法正确，不能有未闭合的标签,每行的td数量(colspan计算在内)必须一致
	 * 写法与html css一致即可
	 * 为了实现word中特定的样式,除了正常的html css，填写了扩展样式,如对角线拆分单元格、表头在每页重复显示等
	 * 注意扩展样式在浏览器中预览没有效果
	 * repeat-header:true 表示每页重复
	 */
	public static void table(){
		WDocument doc = doc("html表格");
		//表格上默认是带边框的，如果不需要边框可以通过style='border:none'删除边框
		//td中同样支持html标签
		StringBuilder builder = new StringBuilder();

		//支持多个html标签合并,但不要使用嵌套的 HTML
		builder.append("<div style='color:red;'>Before Table</div>");

		builder.append("<table style='width:100%;'>")
			//这里的repeat-header:true 表示跨页后，每页都需要显示
			.append("<tr style='font-weight:700;background-color:#CFCFCF;repeat-header:true'><td>序号</td><td>部门</td><td>人数</td></tr>")
			//加在这里显示不合理，但是只要加上也会重复显示
			.append("<tr style='repeat-header:true'><td><b>1</b></td><td style='color:red;'>财务部</td><td>10</td></tr>")
			.append("<tr><td><b>2</b></td><td>研发部</td><td>200</td></tr>")
			//但是不可能隔行，加在这里没有效果
			.append("<tr style='repeat-header:true'><td><b>3</b></td><td>生产部</td><td>3000</td></tr>")
			.append("<tr><td><b>4</b></td><td>市场部</td><td>300</td></tr>")
			.append("<tr><td><b>5</b></td><td>质检部</td><td>100</td></tr>");
			//这里添加100行用来演示表格垮行后，表头重复显示
		for(int i=0; i<100; i++){
			builder.append("<tr><td><b>").append(i+6).append("</b></td><td>质检").append(i+1).append("部</td><td>2</td></tr>");
		}
		builder.append("</table>");


		doc.replace("html_table", builder.toString());
		doc.save();

	}

	/**
	 * 对角经拆分单元格
	 */
	public static void split(){
		WDocument doc = doc("拆分单元格");
		StringBuilder builder = new StringBuilder();
		builder.append("<div>人员薪资表(拆分单元格)</div>");
		builder.append("<table style='width:100%;'>")
				.append("<tr><td style='border-tl2br:1px solid #2F74B5'>")
				.append("<div style='text-align:right;'>月份</div>")
				.append("<div style='text-align:left;'>人员</div>")
				.append("</td>")
				.append("<td>一月</td>")
				.append("<td>二月</td>")
				.append("<td>三月</td>")
				.append("</tr>")
				.append("<tr><td>张三</td><td>2000.00</td><td>3000.00</td><td>2500.00</td></tr>")
				.append("<tr><td>李四</td><td>2000.00</td><td style='background-color:red'>130000.00</td><td>2500.00</td></tr>")
				.append("<tr><td>王五</td><td>2000.00</td><td>3000.00</td><td>2500.00</td></tr>")
				.append("</table>");
		doc.replace("html_table", builder.toString());

		doc.save();
	}


	/**
	 * 这里需要一个模板文件，在模板中可以添加书签与${key}形式的关键字
	 * 尽量使用书签，${key}容易被拆分成多个部分，放到多个标签内,造成替换失败
	 */
	public static void start(){
		WDocument doc = doc("start");
		doc.replace("code","SLWH21100233");
		doc.replace("name","中国石油环境影响评价现状检测报告");
		doc.replace("customer","中国石油");
		doc.replace("type","噪音检测报告");
		doc.replace("ymd", DateUtil.format("yyyy-MM-dd"));
		//网络图片
		String logo = "http://img.baidu.com/img/logo-80px.gif";
		doc.replace("img_http","<img src='"+logo+"' style='width:100px;height:50px;'/>");
		//本地图片
		File localImg = new File(dir,"result/baidu.gif");
		HttpUtil.download(logo, localImg);
		doc.replace("img_local","<img src='"+localImg.getAbsolutePath()+"' style='width:100px;height:50px;'/>");
		doc.save();
	}

	/**
	 * html标签css样式 转成word标签
	 * 这里主要演示通过html标签以及css样式 转成word标签
	 * html,css转换可参考http://office.anyline.org/art/l?s=l82c99f6acv01ebf1d2da1920ab1b90294f719acc231ccaeaaad
	 */
	public static void html(){
		WDocument doc = doc("html");
		//这里支持html标签css样式
		//模板中这里的字号是30号，如果不指定的话默认原来的30号，原来的选颜色是黑色，如果指定了新选颜色将替换成新颜色
		doc.replace("name","<span style='color:red;'>中国石油环境影响评价现状检测报告</span>");
		//这里的字号支持word中的一号二号以及pt,px
		doc.replace("customer","<span style='font-size:二号'>中国石油</span>");
		StringBuilder builder = new StringBuilder();
		builder.append("<div style='color:red;text-indent:30px;'>这里输入一段长文字用来实现<span style='font-size:一号;color:blue;'>首行缩进</span>,支持多个html标签合并,但不要使用嵌套的 HTML支持多个html标签合并,但不要使用嵌套的 HTML支持多个html标签合并</div>");

		doc.replace("html_table", builder.toString());

		doc.save();
	}

	public static WDocument doc(String tag){
		File src = new File(dir, "template_101.docx");
		File tar = new File(dir,"result/"+System.currentTimeMillis()+"_"+tag+".docx");
		//先复制一个文件，在新的文件基础上修改
		FileUtil.copy(src, tar);
		WDocument doc = new WDocument(tar);
		return doc;
	}

}
