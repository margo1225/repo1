package com.mtxshop.api;

import MTXShopTest.TestBase;
import com.alibaba.fastjson.JSONObject;
import mtxshop.server.AddCartServer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AddCartServerTest extends TestBase {
    @Test(description = "加入成功")
    public void test001_addCard() throws IOException, URISyntaxException {
        String res = AddCartServer.AddGoods(host, header);
        int statusCode = AddCartServer.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject msg = JSONObject.parseObject(res);
        String msg1 = msg.getString("msg");
        Assert.assertEquals(msg1,"加入成功");
    }

    @Test(description = "库存不足")
    public void test002_addCard() throws IOException, URISyntaxException {
        Map<String,String> stock =new HashMap<>();
        stock.put("goods_id","7");
        stock.put("stock","20890000890000");
        String res = AddCartServer.AddGoods(host,stock, header);
        int statusCode = AddCartServer.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject msg = JSONObject.parseObject(res);
        String msg1 = msg.getString("msg");
        Assert.assertEquals(msg1,"加入成功");

    }
}
