package MTXShopTest;

import Util.DBUtil;
import Util.PropertieUtil;
import httpFZ.HttpClientTest;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static Util.PropertieUtil.getProperty;

public class TestBase {

    public static String host;
    public static Map<String,String> header = new HashMap<>();


    @BeforeSuite
    public void init() throws IOException, URISyntaxException {
        String resource = PropertieUtil.class.getResource("/db.properties").getPath();
        String dburl = PropertieUtil.getProperty(resource, "mtxshop.url");
        String user = PropertieUtil.getProperty(resource, "mtxshop.user");
        String password=PropertieUtil.getProperty(resource, "mtxshop.password");
        DBUtil.getConnection(dburl, user, password);
        //读取host
        String path = PropertieUtil.class.getResource("/host.properties").getPath();
        host = getProperty(path, "http.mtxshop");

        String url ="/mtx/index.php?s=/index/user/login.html";
        System.out.println("执行");
        Map<String,String> parmas = new HashMap<>();
        parmas.put("accounts","shamo");
        parmas.put("pwd","123456");

        header.put("X-Requested-With","XMLHttpRequest");
        String s = HttpClientTest.postForm(host + url,parmas,header);
        System.out.println(s);


    }
    @AfterSuite
    public void afterSuit(){
        DBUtil.close();
    }
}
