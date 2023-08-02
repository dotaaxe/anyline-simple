package org.anyline.simple.tables;

import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.TableBuilder;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.ConfigTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"org.anyline"})
public class TablesApplication {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(TablesApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		AnylineService service = (AnylineService)context.getBean("anyline.service");
		ConfigTable.IS_SQL_DELIMITER_OPEN = true;
		DataSet set = service.querys(TableBuilder.init().setTable("HR_SALARY(M.ID, M.BASE_PRICE, FE.NM AS EMPLOYEE_NM, FD.NM AS DEPARTMENT_NM) AS M")
				.left("HR_EMPLOYEE AS FE","M.EMPLOYEE_ID = FE.ID")
				.left("HR_DEPARTMENT AS FD", "FE.DEPARTMENT_ID = FD.ID")
				.build()
				//condition("FE.NM:%nm%"); //如果 是在controller可以加这个参数
				,"M.yyyy:2017"
		);
		service.querys(TableBuilder.init()
				.setTable("HR_EMPLOYEE(U.ID AS USER_ID,D.NM AS DEPARTMENT_NM)").setAlias("U")
				.left("HR_DEPARTMENT D","U.DEPARTMENT_ID = D.ID").build(), "U.ID:>=100");

		service.querys(TableBuilder.init("HR_EMPLOYEE(U.ID AS USER_ID,D.NM AS DEPARTMENT_NM) AS U")
				.left("HR_DEPARTMENT D","U.DEPARTMENT_ID = D.ID").build(), "U.ID:>=100");


		service.querys("SELECT E.ID AS EMPLOYEE_ID, D.NM AS DEPT_NM FROM HR_EMPLOYEE AS E LEFT JOIN HR_DEPARTMENT AS D ON E.DEPARTMENT_ID = D.ID"
		,"E.ID:>=1", "E.NM:张%");

		//如果查的列比较多可以通过ConfigStore来设置
		ConfigStore configs = new DefaultConfigStore();
		configs.columns("M.ID","F.CODE AS DEPARTMENT_CODE");
		service.query(TableBuilder.init("HR_EMPLOYEE AS M").left("HR_DEPARTMENT AS F", "F.DEPARTMENT_ID = F.ID").build(), configs);
		/*SELECT
			M.ID,F.CODE AS DEPARTMENT_CODE
		FROM
			HR_EMPLOYEE
		  M
			LEFT JOIN HR_DEPARTMENT  F ON F.DEPARTMENT_ID = F.ID
	   */
		System.exit(0);
	}

}
