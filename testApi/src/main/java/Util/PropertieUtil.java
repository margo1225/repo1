package Util;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class PropertieUtil {
    private static Logger logger=Logger.getLogger(String.valueOf(PropertieUtil.class));
    public static String getProperty(String filepath, String key){
        Properties properties =new Properties();
        try {
            properties.load(new FileReader(filepath));
            //读到的对应值
            String property = properties.getProperty(key);
            return property;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(String.valueOf(e));

        }
        return "";
    }
   //读取所有文件
    public static Map<String, String> getAllkeyValues(String filepath)  {
        Map<String,String> all =new HashMap<>();

        Properties properties =new Properties();
        try {
            properties.load(new FileReader(filepath));
            Set<Map.Entry<Object,Object>> entries =properties.entrySet();
            for (Map.Entry<Object,Object> entry:entries) {
                all.put(entry.getKey().toString(),entry.getValue().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(String.valueOf(e));
        }
        return all;
    }
    public static void main(String[] args) {
        //获取路径
        String resource = Properties.class.getResource("/db.properties").getPath();
        System.out.println(resource);
        String url = getProperty(resource, "mtxshop.url");
        System.out.println(url);
    }
}
