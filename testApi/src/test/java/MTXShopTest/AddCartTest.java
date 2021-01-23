package MTXShopTest;

import Util.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import httpFZ.HttpClientTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.jvm.hotspot.oops.ObjectHeap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddCartTest extends TestBase {

   @DataProvider
   public Object[][] addCartData(){
       Object[][] obj ={

       };

       return obj;
   }

   @Test(description = "添加购物车")
    public void test001_addCart() throws IOException, URISyntaxException {
       Map<String,String> parmas = new HashMap<>();
       String url ="/mtx/index.php?s=/index/cart/save.html";

       parmas.put("goods_id","1");
       parmas.put("stock","12");
       //header.put("X-Requested-With","XMLHttpRequest");
       String response = HttpClientTest.postForm(host + url, parmas, header);
       int statusCode = HttpClientTest.getStatusCode();
       Assert.assertEquals(statusCode,200);
       JSONObject jsonObject = JSONObject.parseObject(response);
       String msg = jsonObject.getString("msg");
       Assert.assertEquals(msg,"加入成功");



   }


  @DataProvider
  public Object[][] getCartData(){

//      Object[][] obj={
//              {"正确添加货物","1","2","加入成功"},
//              {"添加货物ID不存在","12344","13","商品不存在或已删除"},
//              {"添加货物库存不足","1","2000000","加入失败"},
//              {"添加货物已删除","1","2","加入失败"},
//              {"购物车-商品已下架","12","2","商品不存在或已删除"}
//      };
//      return obj;


      ExcelUtil excelUtil =new ExcelUtil("/Applications/JAVA_Study/JAVA_CODE/testApi/src/main/resources/data/mtxshop_data.xlsx");
      excelUtil.getCellData("添加购物车",1,3);
      Object[][] data = excelUtil.getSheetData("添加购物车");
      excelUtil.close();
      return data;


  }




  @Test(description = "添加购物车",dataProvider ="getCartData")
  public void test002_addCart(String caseName,String goods_id,String stock,String AssertName ) throws IOException, URISyntaxException {
      Map<String,String> parmas = new HashMap<>();
      String url ="/mtx/index.php?s=/index/cart/save.html";

      parmas.put("goods_id",goods_id);
      parmas.put("stock",stock);
      //header.put("X-Requested-With","XMLHttpRequest");
      String response = HttpClientTest.postForm(host + url, parmas, header);
      int statusCode = HttpClientTest.getStatusCode();
      Assert.assertEquals(statusCode,200);
      JSONObject jsonObject = JSONObject.parseObject(response);
      String msg = jsonObject.getString("msg");
      Assert.assertEquals(msg,AssertName);
  }


}
