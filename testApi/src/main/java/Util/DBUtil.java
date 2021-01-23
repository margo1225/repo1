package Util;

import java.sql.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;

//封装数据库
public class DBUtil {
    private static Logger logger=Logger.getLogger(String.valueOf(DBUtil.class));
    public static Connection connection;
    public static Connection getConnection(String dburl,String user,String pwd){
       try {
           connection=DriverManager.getConnection(dburl, user, pwd);
       } catch (SQLException e) {
           e.printStackTrace();
           logger.info(String.valueOf(e));
       }
       return connection;
   }

   //封装数据库方法
    //查询方法
    public static List<Map<String, Object>> select(String sql) {
        List<Map<String, Object>> resList = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount(); //总列数

            resList = new ArrayList<>();
            while (resultSet.next()) {
                //先把每一行变成一个map对象
                Map<String, Object> lineMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    //得到列名称，作为键志对的key值
                    String key = metaData.getColumnLabel(i);
                    Object value = resultSet.getObject(i);
                    lineMap.put(key, value);
                }
                resList.add(lineMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info(String.valueOf(e));
        }
        return resList;
    }
    /*
    executeUpdate方法可以执行新增，更新，删除三种sql语句
     */
    public static int executeUpdate(String sql){
        Statement stmt =null;
        try{
            stmt =connection.createStatement();
            stmt.executeUpdate(sql);
            //更新了SQL语句的数量
            int updateCount = stmt.getUpdateCount();
            return updateCount;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (stmt!=null){
                try {
                    stmt.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info(String.valueOf(e));
        }
    }

    public static void main(String[] args) {
        //测试查询语句
        String url="jdbc:mysql://192.168.1.9:3306/mtx?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false";
        getConnection(url, "root", "123456");

        List<Map<String, Object>> data =select("SELECT * FROM s_order LIMIT 3");
        for (Map<String,Object> datum:data) {
            Set<Map.Entry<String,Object>> entries =datum.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                System.out.println(entry.getKey()+"======"+entry.getValue());
            }
        }


        //插入语句测试
        close();
    }

}
