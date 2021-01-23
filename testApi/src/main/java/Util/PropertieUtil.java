package Util;

import sun.rmi.runtime.Log;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
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

    public static void main(String[] args) {
        //获取路径
        String resource = Properties.class.getResource("/db.properties").getPath();
        System.out.println(resource);
        String url = getProperty(resource, "mtxshop.url");
        System.out.println(url);
    }
}
