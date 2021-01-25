package mtxshop.server;

import Util.PropertieUtil;
import httpFZ.HttpClientTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class LoginServer {
    public  String url ="/mtx/index.php?s=/index/user/login.html";
    public String paramPath =LoginServer.class.getResource("/mtxshop/login.properties").getPath();

    public String login(String host,Map<String, String> header) throws IOException, URISyntaxException {
//        Map<String,String> parmas = new HashMap<>();
//
//        parmas.put("accounts","shamo");
//        parmas.put("pwd","123456");
//        Map<String,String> header = new HashMap<>();
//        header.put("X-Requested-With","XMLHttpRequest");
        Map<String, String> allkeyValues = PropertieUtil.getAllkeyValues(paramPath);


        String res = HttpClientTest.postForm(host + url, allkeyValues, header);
        return res;
    }
}
