package org.anyline.simple.office;

import org.anyline.entity.DataSet;
import org.anyline.entity.html.Table;
import org.anyline.entity.html.TableBuilder;
import org.anyline.poi.excel.ExcelUtil;
import org.anyline.service.AnylineService;
import org.anyline.util.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.List;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline","org.anyboot"})
public class ExcelApplication extends SpringBootServletInitializer {

	private static AnylineService service;
	private static File dir = null;
	private static Table table = null;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(ExcelApplication.class);
	}

	public static void main(String[] args) {

		String path = ExcelApplication.class.getResource("/").getPath();
		dir = new File(path.substring(0,path.indexOf("target")),"/src/main/template");
		SpringApplication application = new SpringApplication(ExcelApplication.class);
		ConfigurableApplicationContext context = application.run(args);
		service = (AnylineService)context.getBean("anyline.service");
		//导出简单的excel
		exportList();
		//先生成tabel再导出,包括单元格合并、样式设置
		exportTable();
		//操作多个sheet
		exportSheet();
		//读取excel返回二维数组
		read();
		System.exit(0);
	}

	/**
	 * 导出excel
	 */
	public static void exportList(){
		DataSet set = service.querys("V_HR_SALARY","YYYY:"+ (DateUtil.year()-1), "ORDER BY EMPLOYEE_ID, YM");
		//最简单的导出一个列表,如果文件已存在，则在原文件内容基础上插入行
		File file = new File(dir,"result/export_list.xlsx");
		//1表示从第1行插入，如果原来文件有内容(一般是有页脚)，则下移
		//{num}表示第几行，下标从1开始
		//这里支持复合KEY
		ExcelUtil.export(file,1, set,"序号:{num}","部门:DEPARTMENT_NM","姓名:EMPLOYEE_NM","月份:YM","底薪:{BASE_PRICE}+{REWARD_PRICE}");

		//如果表头、表尾格式比较复杂，可先创建模板，再根据模板导出
		File template = new File(dir,"template_102.xlsx");//这里是一个模板文件
		//根据模板导出时就不需要指定表头了，只要对应好顺序，并计算好从哪一行开始写入
		if(template.exists()) {
			ExcelUtil.export(template, file, 2, set, "{num}", "DEPARTMENT_NM", "EMPLOYEE_NM", "YM", "{BASE_PRICE}+{REWARD_PRICE}");
		}
	}
	public static void exportTable(){
		//导出复杂的表格需要借助TableBuilder先生成Table，再将Table导出到excel中
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
		table = builder.build();
		File file = new File(dir, "result/export_table.xlsx");
		ExcelUtil.export(file, table);
	}


	/**
	 * 读取excel
	 */
	public static void  read(){
		File file = new File(dir,"result/export_table.xlsx");
		List list = ExcelUtil.read(file);	//默认读取第0个sheet从第0行开始
		list = ExcelUtil.read(file,0,3);	//读取第1个sheet从第3行读取

		//遇到合并单元格的，将拆分开未合并前的状态，拆分后补上每个单元格的值
		//返回的是一个二维数组
		//为了操作方便可以把返回值转换成DataSet,DataSet中的条目(DataRow)以excel列下标作为属性key
		DataSet set = DataSet.build(list);
	}

	/**
	 * 导出excel,多个sheet
	 */
	public static void exportSheet(){
		//默认情况下导出的第0个sheet也就是sheet1
		//如果要导出到多个sheet需要执行多次export导出到同一个文件,每次执行时指定sheet名称或下标
		File file = new File(dir, "result/export_sheet.xlsx");
		ExcelUtil.export(file, "sheet A", table);
		ExcelUtil.export(file, "sheet B", table);
	}
}
