package MTXShopTest;

import Util.DBUtil;
import Util.ExcelUtil;
import Util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import httpFZ.HttpClientTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SubmitOrder extends TestBase {
    //家里IP
    //String host ="http://192.168.1.9";
    //公司IP
    //String host ="http://10.17.17.14";
    //学校IP：192.168.31.136
//    String host ="http://192.168.31.136";
//
//    Map<String,String> header = new HashMap<>();
//
//    @BeforeClass
//    public void init() throws IOException, URISyntaxException {
//        String url ="/mtx/index.php?s=/index/user/login.html";
//        Map<String,String> parmas = new HashMap<>();
//
//        parmas.put("accounts","shamo");
//        parmas.put("pwd","123456");
//
//        header.put("X-Requested-With","XMLHttpRequest");
//        String s = HttpClientTest.postForm(host + url,parmas,header);
//        System.out.println(s);
//
//    }

    @Test
    public void test001_SubmitOrder()throws IOException,URISyntaxException{
       // header.put("X-Requested-With","XMLHttpRequest");
        String url="/mtx/index.php?s=/index/buy/add.html";
        Map<String,String> params= new HashMap<>();
        /*
        参数	参数名称	参数类型	必填	参数举例	参数含义
	goods_id	int	Y	1	商品id
	stock	int	Y	15	购买数量
	buy_type	String	Y	goods	购买类型下单是固定的goods
 	address_id	int	Y	2	地址id
	payment_id	int	Y	1	付款id
	spec	String	Y	[{"type":"尺码","value":"M"}]	产品规格，可以传[]

         */
        params.put("goods_id","1");
        params.put("stock","1");
        params.put("buy_type","goods");
        params.put("address_id","2");
        params.put("payment_id","1");
        params.put("spec","\n" +
                "\tspec\tString\tY\t");

        String s = HttpClientTest.postForm(host + url, params, header);
        System.out.println(s);

        int statusCode = HttpClientTest.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,"提交成功");

    }
    @DataProvider
    public Object[][] getOrder(){
//        Object[][] obj ={
//                {"提交订单-正确","2","1","goods","1","1","[{\"type\":\"套餐\",\"value\":\"套餐一\"},{\"type\":\"颜色\",\"value\":\"银色\"},{\"type\":\"容量\",\"value\":\"64G\"}]","提交成功"},
//                {"提交订单-stock错误","1","-1","goods","2","1","[{\"type\":\"尺码\",\"value\":\"M\"}]","购买数量有误"},
//                {"提交订单-地址错误","1","1","goods","30","1","[{\"type\":\"尺码\",\"value\":\"M\"}]","没有相关规格"},
//                {"提交订单-支付错误","1","1","goods","2","90","[{\"type\":\"尺码\",\"value\":\"M\"}]","没有相关规格"}
//        };
//        return obj ;
        ExcelUtil excelUtil = new ExcelUtil("/Applications/JAVA_Study/JAVA_CODE/testApi/src/main/resources/data/mtxshop_data.xlsx");
        excelUtil.getCellData("提交订单",1,8);
        Object[][] data = excelUtil.getSheetData("提交订单");
        excelUtil.close();
        return data;
    }
    //提交订单
    @Test(dataProvider = "getOrder")
    public void test002_submitOder(String caseName,String goods_id,String stock,String buy_type,String address_id,String payment_id,String spec,String AssertName) throws IOException, URISyntaxException {

       // header.put("X-Requested-With","XMLHttpRequest");
        String url="/mtx/index.php?s=/index/buy/add.html";
        Map<String,String> params= new HashMap<>();
        //goods_id = RandomUtil.getRndNumByLen(3);
        params.put("goods_id",goods_id);
        params.put("stock",stock);
        params.put("buy_type",buy_type);
        params.put("address_id",address_id);
        params.put("payment_id",payment_id);
        params.put("spec",spec);


        String s = HttpClientTest.postForm(host + url, params, header);
        System.out.println(s);
        int statusCode = HttpClientTest.getStatusCode();
       Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,AssertName);


    }
    //取消订单
    @DataProvider
    public Object[][] CancellOrdeData(){
        Object[][] obj={
                {"取消订单-正确","139","取消成功"},
                {"取消订单-ID为负","-2","资源不存在或已被删除"},
                {"取消订单-ID为0","0","订单id有误"},
        };
        return obj;
    }
    @Test(dataProvider = "CancellOrdeData")
    public void test003_CancellOrde(String name,String Id,String AsserName) throws IOException, URISyntaxException {
        String url ="/mtx/index.php?s=/index/order/cancel.html";
        Map<String,String> parmas = new HashMap<>();
        parmas.put("id",Id);


       // header.put("X-Requested-With","XMLHttpRequest");
        String s = HttpClientTest.postForm(host + url,parmas,header);
        System.out.println(s);
        int statusCode = HttpClientTest.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,AsserName);

        String url2="dbc:mysql://192.168.31.136:3306/mtx?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false";
        DBUtil.getConnection(url2,"root","123456");
        DBUtil.select("select status from 's_order' ");




    }

}


