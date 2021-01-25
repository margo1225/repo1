package mtxshop.server;

import Util.PropertieUtil;
import httpFZ.HttpClientTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

public class CreateOrderServer {
    public static String url ="/mtx/index.php?s=/index/buy/add.html";
    public static String paramPath = AddCartServer.class.getResource("/mtxshop/addOrder.properties").getPath();

    public static String CreateOrder(String host, Map<String,String> headers) throws IOException, URISyntaxException {

        Map<String,String> allKeyValuee = PropertieUtil.getAllkeyValues(paramPath);
        String res = HttpClientTest.postForm(host + url, allKeyValuee, headers);
        return res;
    }
    public static int  getStatusCode(){
        return HttpClientTest.getStatusCode();
    }
    public static String CreateOrder(String host, Map<String,String> modifyParams,Map<String,String> headers) throws IOException, URISyntaxException {

        Map<String,String> allKeyValuee = PropertieUtil.getAllkeyValues(paramPath);
        Set<Map.Entry<String, String>> entries = modifyParams.entrySet();
        for (Map.Entry<String,String> entry:entries) {
            allKeyValuee.replace(entry.getKey(),entry.getValue());

        }


        String res = HttpClientTest.postForm(host + url, allKeyValuee, headers);
        return res;
    }


}
