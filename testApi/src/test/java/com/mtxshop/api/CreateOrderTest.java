package com.mtxshop.api;

import MTXShopTest.TestBase;
import Util.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import httpFZ.HttpClientTest;
import mtxshop.server.CreateOrderServer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class CreateOrderTest extends TestBase {

    @DataProvider
    private Object[][] getData(){
        String path = ExcelUtil.class.getResource("/data/mtxshop_data.xlsx").getPath();
        ExcelUtil excelUtil = new ExcelUtil(path);
        Object[][] data = excelUtil.getSheetData("提交订单");
        return data;

    }

    @Test(dataProvider ="getData",description = "Excel驱动")
    public void test001_createOrder(String caseName,String goods_id,String stock,String buy_type,String address_id,String payment_id,String spec,String user_note,String AssertName) throws IOException, URISyntaxException {


        Map<String,String> params= new HashMap<>();
        //goods_id = RandomUtil.getRndNumByLen(3);
        params.put("goods_id",goods_id);
        params.put("stock",stock);
        params.put("buy_type",buy_type);
        params.put("address_id",address_id);
        params.put("payment_id",payment_id);
        params.put("spec",spec);
        params.put("user_note",user_note);

        String res = CreateOrderServer.CreateOrder(host,params,header);
        int statusCode= CreateOrderServer.getStatusCode();



        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(res);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,AssertName);

    }
    @DataProvider
    private Object[][] getData1(){
        String path = ExcelUtil.class.getResource("/data/mtxshop_dataV1.0.xlsx").getPath();
        ExcelUtil excelUtil = new ExcelUtil(path);
        Object[][] data = excelUtil.getSheetData("提交订单修改");
        return data;

    }

    @Test(dataProvider ="getData1" )
    public void test002_createOrder(String caseName,String parmas,String AssertName) throws IOException, URISyntaxException {
      Map<String,String> modifyParams =new HashMap<>();
       //将json数据转化为map格式
       if (!parmas.equalsIgnoreCase("")){
           JSONObject jsonObject = JSONObject.parseObject(parmas);

           modifyParams = jsonObject.toJavaObject(jsonObject, Map.class);

       }
        String res = CreateOrderServer.CreateOrder(host, modifyParams, header);

        int statusCode = CreateOrderServer.getStatusCode();
        Assert.assertEquals(statusCode,200);
        JSONObject jsonObject = JSONObject.parseObject(res);
        String msg = jsonObject.getString("msg");
        Assert.assertEquals(msg,AssertName);

    }
}
