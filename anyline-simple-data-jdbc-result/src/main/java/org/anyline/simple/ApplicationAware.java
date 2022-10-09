package org.anyline.simple;

import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.service.AnylineService;
import org.anyline.util.BasicUtil;
import org.anyline.util.DateUtil;
import org.anyline.util.NumberUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Component
@WebListener
public class ApplicationAware implements ApplicationListener<ApplicationReadyEvent>, ServletContextListener {

    private ServletContext servletContext;
    private AnylineService service;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();

        service = (AnylineService) applicationContext.getBean("anyline.service");
        init();
    }

    private void init(){
        int qty = service.count("HR_SALARY");
        if(qty ==0) {
            int yyyy = BasicUtil.parseInt(DateUtil.format("yyyy"),0);
            DataSet employees = service.querys("HR_EMPLOYEE");
            for(int y=yyyy-5; y<yyyy; y++){
                //计算5年工资
                for (DataRow employee : employees) {
                    DataSet set1 = new DataSet();
                    DataSet set2 = new DataSet();
                    for (int i = 1; i <= 12; i++) {
                        String ym = y + "-" + BasicUtil.fillChar(i, 2);
                        DataRow row1 = new DataRow();
                        row1.put("EMPLOYEE_ID", employee.getId());
                        row1.put("YYYY", y);
                        row1.put("YM", ym);
                        int base = NumberUtil.random(100, 300)*100;
                        int reward = NumberUtil.random(1, 50)*100;
                        int deduct = NumberUtil.random(1, 10)*100;
                        row1.put("BASE_PRICE", base);
                        row1.put("REWARD_PRICE", reward);
                        row1.put("DEDUCT_PRICE", deduct);
                        set1.add(row1);

                        DataRow row21 = new DataRow();
                        row21.put("EMPLOYEE_ID", employee.getId());
                        row21.put("YYYY", y);
                        row21.put("YM", ym);
                        row21.put("TYPE_CODE", "BASE");
                        row21.put("PRICE", base);
                        set2.add(row21);

                        DataRow row22 = new DataRow();
                        row22.put("EMPLOYEE_ID", employee.getId());
                        row22.put("YYYY", y);
                        row22.put("YM", ym);
                        row22.put("TYPE_CODE", "REWARD");
                        row22.put("PRICE", reward);
                        set2.add(row22);

                        DataRow row23 = new DataRow();
                        row23.put("EMPLOYEE_ID", employee.getId());
                        row23.put("YYYY", y);
                        row23.put("YM", ym);
                        row23.put("TYPE_CODE", "DEDUCT");
                        row23.put("PRICE", -deduct);
                        set2.add(row23);

                    }
                    service.insert("HR_SALARY", set1);
                    service.insert("HR_SALARY_TYPE", set2);
                }
            }

        }
    }
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
    }

    public void contextDestroyed(ServletContextEvent event){

    }
}