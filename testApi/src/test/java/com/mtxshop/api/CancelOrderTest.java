package com.mtxshop.api;

import MTXShopTest.CancelOrder;
import MTXShopTest.TestBase;
import Util.DBUtil;
import com.alibaba.fastjson.JSONObject;
import mtxshop.server.CancelOrderServer;
import mtxshop.server.CreateOrderServer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class CancelOrderTest extends TestBase {
    @Test
    public void test001_cancelOrder() throws IOException, URISyntaxException {
        String id = CancelOrderServer.getData().get("id");
        DBUtil.executeUpdate("UPDATE s_order SET STATUS=1 WHERE id='"+id+"'");
        String res = CancelOrderServer.cancelOrder(host, header);
    }

    @Test(description = "创建订单提取id")
    public void test002_cancelOrder() throws IOException, URISyntaxException {
        //调用生成订单接口，产生ID
        String order = CreateOrderServer.CreateOrder(host, header);
        JSONObject jsonObject = JSONObject.parseObject(order);
        String id = jsonObject.getJSONObject("data").getJSONObject("order").getString("id");
        System.out.println(id);
        //应用ID，取消订单
        Map<String,String> parmas = new HashMap<>();
        parmas.put("id",id);
        String res = CancelOrderServer.cancelOrder(host, parmas, header);
        int statusCode = CancelOrderServer.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject1 = JSONObject.parseObject(res);

        String msg = jsonObject1.getString("msg");
        Assert.assertEquals(msg,"取消成功");
    }

}
