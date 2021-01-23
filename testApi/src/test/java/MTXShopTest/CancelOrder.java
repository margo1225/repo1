package MTXShopTest;

import Util.DBUtil;
import Util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.JsonAdapter;
import com.mongodb.DB;
import httpFZ.HttpClientTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelOrder extends TestBase {
//    @DataProvider
//    public Object[][] CancellOrdeData(){
//        Object[][] obj={
//                {"取消订单-正确","139","取消成功"},
//                {"取消订单-ID为负","-2","资源不存在或已被删除"},
//                {"取消订单-ID为0","0","订单id有误"},
//        };
//        return obj;
//    }
//    @Test(dataProvider = "CancellOrdeData")
//    public void test003_CancellOrde(String name,String Id,String AsserName) throws IOException, URISyntaxException {
//        String url ="/mtx/index.php?s=/index/order/cancel.html";
//        Map<String,String> parmas = new HashMap<>();
//        parmas.put("id",Id);
//
//
//        // header.put("X-Requested-With","XMLHttpRequest");
//        String s = HttpClientTest.postForm(host + url,parmas,header);
//        System.out.println(s);
//        int statusCode = HttpClientTest.getStatusCode();
//        Assert.assertEquals(statusCode,200);
//        JSONObject jsonObject = JSONObject.parseObject(s);
//        String msg = jsonObject.getString("msg");
//        Assert.assertEquals(msg,AsserName);
//
//
//    }
    @Test
    public void test001_CancelOrder() throws IOException, URISyntaxException {
        String caUrl="/mtx/index.php?s=/index/order/cancel.html";
        Map<String,String> pararms =new HashMap<>();
        pararms.put("id","162");
        String s = HttpClientTest.postForm(host + caUrl, pararms, header);
        System.out.println(s);
        int statusCode = HttpClientTest.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,"取消成功");

        List<Map<String,Object>> dbData =DBUtil.select("SELECT `status` FROM s_order WHERE  order_no=20210121212731964028");

        Map<String, Object> ObjectMap = dbData.get(0);
        int status = (int) ObjectMap.get("status");
        System.out.println("状态是"+status);
        Assert.assertEquals(status,5);



    }
    @Test
    public void test002_CancelOrde() throws IOException, URISyntaxException {
        String id = RandomUtil.getRndNumByLen(8);
        String orderno = RandomUtil.getRndNumByLen(20);


        DBUtil.executeUpdate("INSERT INTO `s_order` VALUES ('"+id+"', '"+orderno+"', 1, '', 0, '', 1, 1, 1, '', 1, 0.00, 0.00, 238.00, 238.00, 238.00, 0.00, 0, 'pc', 0, 1611235652, 1611235651, 0, 1611235888, 0, 0, 0, 0, 0, 0, 0, 1611235651, 1611235888)");
        String caUrl="/mtx/index.php?s=/index/order/cancel.html";
        Map<String,String> pararms =new HashMap<>();
        pararms.put("id",id);
        String s = HttpClientTest.postForm(host + caUrl, pararms, header);
        System.out.println(s);
        int statusCode = HttpClientTest.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,"取消成功");
        List<Map<String,Object>> dbData =DBUtil.select("SELECT `status` FROM s_order WHERE id='"+id+"'");
        Map<String,Object> objectMap=dbData.get(0);
        Object status = objectMap.get("status");
        DBUtil.executeUpdate("DELETE FROM s_order where id='"+id+"'");
        Assert.assertEquals(status,5);


    }
    /*
    数据库插入数据的方式造数据

     */
    @Test
    public void test003_SqlInOrder() throws IOException, URISyntaxException {
        //调用随机数生成ID
        String id = RandomUtil.getRndNumByLen(8);
        //调用数据书生成orderNO,id与orderno是唯一的
        String orderno = RandomUtil.getRndNumByLen(20);


        DBUtil.executeUpdate("INSERT INTO `s_order` VALUES ('"+id+"','"+orderno+"', 1, '', 0, '', 1, 1, 0, '', 1, 0.00, 0.00, 2100.00, 2100.00, 0.00, 0.00, 0, 'pc', 0, 0, 1610968739, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1610968739, 0);");
        Map<String,String> params =new HashMap<>();
        params.put("id",id);
        String caUrl="/mtx/index.php?s=/index/order/cancel.html";
        String s = HttpClientTest.postForm(host + caUrl, params, header);
        System.out.println(s);
        int statusCode = HttpClientTest.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,"取消成功");
        List<Map<String,Object>> dbData =DBUtil.select("SELECT `status` FROM s_order WHERE id='"+id+"'");
        Map<String,Object> objectMap =dbData.get(0);
        Object status = objectMap.get("status");
        DBUtil.executeUpdate("DELETE FROM s_order where id ='"+id+"'");
        Assert.assertEquals(status,5);

    }
    /*
    采用调用接口，生成订单的方式造数据，测试取消订单
     */
    @Test
    public void test004_cancdlOrde() throws IOException, URISyntaxException {
        //提交订单,生成ID
        String url="/mtx/index.php?s=/index/buy/add.html";
        Map<String,String> params= new HashMap<>();
        params.put("goods_id","1");
        params.put("stock","1");
        params.put("buy_type","goods");
        params.put("address_id","2");
        params.put("payment_id","1");
        params.put("spec","\n" +
                "\tspec\tString\tY\t");
        String s1 = HttpClientTest.postForm(host + url, params, header);
        int statusCode1 = HttpClientTest.getStatusCode();
        System.out.println("提交订单"+statusCode1);
        JSONObject jsonObject = JSONObject.parseObject(s1);

        //提取ID
        String id = jsonObject.getJSONObject("data").getJSONObject("order").getString("id");
        params.put("id",id);
        String caUrl="/mtx/index.php?s=/index/order/cancel.html";
        String s = HttpClientTest.postForm(host + caUrl, params, header);
        System.out.println(s);
        int statusCode = HttpClientTest.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        String msg = jsonObject1.getString("msg");
        Assert.assertEquals(msg,"取消成功");

    }


    @Test(description = "已支付的订单不能被取消")
    public void test005_orderCaner() throws IOException, URISyntaxException {

        List<Map<String, Object>> select = DBUtil.select("SELECT id FROM s_order WHERE `status`=2 LIMIT 1");
        Object id;
        if (select.size()>0){
            id =select.get(0).get("id");
        }else {
            //造数据
            //调用随机数生成ID
            id = RandomUtil.getRndNumByLen(8);
            //调用数据书生成orderNO,id与orderno是唯一的
            String orderno = RandomUtil.getRndNumByLen(20);

            DBUtil.executeUpdate("INSERT INTO `s_order` VALUES ('"+id+"', '"+orderno+"', 1, '', 0, '', 1, 2, 1, '', 1, 0.00, 0.00, 238.00, 238.00, 238.00, 0.00, 0, 'pc', 0, 1611235652, 1611235651, 0, 1611235888, 0, 0, 0, 0, 0, 0, 0, 1611235651, 1611235888)");

        }



        //提取ID
        Map<String,String> params =new HashMap<>();
        params.put("id",id.toString());
        String caUrl="/mtx/index.php?s=/index/order/cancel.html";
        String s = HttpClientTest.postForm(host + caUrl, params, header);
        System.out.println(s);
        int statusCode = HttpClientTest.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        String msg = jsonObject1.getString("msg");
        Assert.assertEquals(msg,"资源不存在或已被删除");


    }

    }

