package org.anyline.simple.oracle;


import oracle.jdbc.OraclePreparedStatement;
import org.anyline.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;

@ComponentScan(basePackages = {"org.anyline"})
@SpringBootApplication
public class OracleApplication {
    private static JdbcTemplate jdbc;
    public static void main(String[] args) throws Exception{
        SpringApplication application = new SpringApplication(OracleApplication.class);
        ConfigurableApplicationContext ctx = application.run(args);
        jdbc = SpringContextUtil.getBean(JdbcTemplate.class);
         //jdbc.execute("TRUNCATE TABLE CRM_USER");
        //ids();
        //params();
        //names();
       // names2();
        //oracle();
       // n();
        try {
            String version =  jdbc.getDataSource().getConnection().getMetaData().getDatabaseProductVersion();
            System.out.println(version);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void ids(){
        System.out.println("-------------ids---------------------");
        String sql =" INSERT INTO CRM_USER (ID, NAME) \n " +
                " SELECT SIMPLE_SEQ.nextval , M.*\n " +
                " FROM( \n" +
                " \tSELECT 'A_IDS'  AS NAME   FROM DUMMY \n " +
                " \tUNION ALL\n" +
                " \tSELECT 'B_IDS'     AS NAME  FROM DUMMY ) M ";
        System.out.println(sql);
        int qty = jdbc.update(sql);
        System.out.println("影响行数:"+qty);

    }
    public static void params(){

        System.out.println("---------params2-------------------------");

        String sql ="  begin INSERT INTO CRM_USER (ID, NAME)   " +
                " SELECT SIMPLE_SEQ.nextval , M.*  " +
                " FROM(  " +
                " SELECT 'a_param'  AS NAME   FROM DUMMY " +
                " UNION ALL " +
                " SELECT 'b_param'    AS NAME  FROM DUMMY ) M; end;";
        System.out.println(sql);
        PreparedStatementCreator creator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws java.sql.SQLException {
               // PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID"});
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int qty = jdbc.update(creator,holder);
        System.out.println("影响行数:"+qty);
        System.out.println(holder.getKeyList());

    }
    public static void params2(){

        System.out.println("---------params2-------------------------");

        String sql =" begin  INSERT INTO CRM_USER (ID, NAME)   " +
                " SELECT SIMPLE_SEQ.nextval , M.*  " +
                " FROM(  " +
                " SELECT ?  AS NAME   FROM DUMMY " +
                " UNION ALL " +
                " SELECT ?    AS NAME  FROM DUMMY ) M; end;";
        System.out.println(sql);
        PreparedStatementCreator creator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws java.sql.SQLException {
                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setObject(1, "A");
                ps.setObject(2, "B");
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        int qty = jdbc.update(creator,holder);
        System.out.println("影响行数:"+qty);
        System.out.println(holder.getKeyList());

    }
    public static void names(){

        System.out.println("---------------names-------------------");
        String sql = "INSERT INTO CRM_USER(ID, NAME) VALUES(SIMPLE_SEQ.nextval, :param1)";
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("param1", "userid") ;
        final KeyHolder holder = new GeneratedKeyHolder();
        int qty = new NamedParameterJdbcTemplate(jdbc).update( sql, parameters, holder,  new String[] {"ID"});
        System.out.println("影响行数:"+qty);
        System.out.println(holder.getKeyList());

    }
    public static void names2(){

        System.out.println("---------------names2-------------------");
        String sql ="BEGIN   INSERT INTO CRM_USER (ID, NAME)   " +
                " SELECT SIMPLE_SEQ.nextval , M.*  " +
                " FROM(  " +
                " SELECT :P1  AS NAME   FROM DUMMY " +
                " UNION ALL " +
                " SELECT :P2    AS NAME  FROM DUMMY ) M; END; ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("P1", "A")
                .addValue("P2", "B");
        final KeyHolder holder = new GeneratedKeyHolder();
        int qty = new NamedParameterJdbcTemplate(jdbc).update( sql, parameters, holder,  new String[] {"ID"});
        System.out.println("影响行数:"+qty);
        System.out.println(holder.getKeyList());
    }
    public static void oracle() throws Exception{
        Connection conn = jdbc.getDataSource().getConnection();
        String sql = "insert into CRM_USER(id,code) values(simple_seq.nextval,?)  returning id into :2";

        PreparedStatement src = conn.prepareStatement(sql);
        OraclePreparedStatement ps = src.unwrap(OraclePreparedStatement.class);
        ps.setObject(1,"aaa");
        ps.registerReturnParameter(2, Types.BIGINT);
        ps.executeUpdate();
        ResultSet rs=ps.getReturnResultSet();
        rs.next();
        int id=rs.getInt(1);
        rs.close();
        ps.close();
        System.out.println("id:("+id+")");


    }
    public static void oracle2() throws Exception{
        Connection conn = jdbc.getDataSource().getConnection();
        String sql = "insert into CRM_USER(id,code) values(simple_seq.nextval,?)  returning id into :2";

        PreparedStatement src = conn.prepareStatement(sql);
        OraclePreparedStatement ps = src.unwrap(OraclePreparedStatement.class);
        ps.setObject(1,"aaa");
        ps.registerReturnParameter(2, Types.BIGINT);
        ps.executeUpdate();
        ResultSet rs=ps.getReturnResultSet();
        rs.next();
        int id=rs.getInt(1);
        rs.close();
        ps.close();
        System.out.println("id:("+id+")");


    }
    public static void n() throws Exception{
        String sql = "insert into CRM_USER (id, code) values (SIMPLE_SEQ.nextval,?) returning id into :2";
        Connection connection = jdbc.getDataSource().getConnection();
        connection.setAutoCommit(false);
        OraclePreparedStatement ps = connection.prepareStatement(sql).unwrap(OraclePreparedStatement.class);
        ps.registerReturnParameter(2, Types.BIGINT);
        for (int i=0; i<100; i++) {
            ps.setString(1, "aa"+i);
            ps.addBatch();
        }
        ps.executeBatch();
        connection.commit();
    }
}
