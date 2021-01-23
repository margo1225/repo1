package httpFZ;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

public class HttpClientTest {
    public static Logger logger = Logger.getLogger(String.valueOf(HttpClientTest.class));
    public static CloseableHttpClient httpclient;
    public static CloseableHttpResponse httpResponse;
    static {
        httpclient = HttpClients.createDefault();
    }


    public static String get_test(String url, Map<String,String> params, Map<String,String> headers) throws URISyntaxException, IOException {

        HttpGet httpGet = new HttpGet();
        //解析参数和URL进行拼接，形成get 请求的真实地址  http://baidu.com?&id=1&name=rtert;
        StringBuffer buffer =new StringBuffer("");
        if (params.size()>0){
            buffer.append("?");
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry:entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                buffer.append("&").append(key).append("=").append(value);
            }
            buffer.replace(1,2,"");
        }



        httpGet.setURI(new URI(url+buffer.toString()));
        System.out.println(url+buffer);
        logger.info("url地址是："+url+buffer.toString());

        Set<Map.Entry<String, String>> entries1 = headers.entrySet();
        for (Map.Entry<String, String> entry1:entries1) {
            String key1 = entry1.getKey();
            String value1 = entry1.getValue();
            logger.info("header:"+key1+"="+value1);
            httpGet.setHeader(key1,value1);

        }


        httpResponse = httpclient.execute(httpGet);
        HttpEntity responseEntity = httpResponse.getEntity();
        String res = EntityUtils.toString(responseEntity,"utf8");
        logger.info("reponse"+res);

        return res;

    }

    public static int getStatusCode(){
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.print("状态码：");
        logger.info("状态码是："+statusCode);
        return statusCode;
    }
    //封装form表单
    public static String postForm(String url, Map<String,String> params, Map<String,String> headers) throws IOException, URISyntaxException {
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(new URI(url));

        List<NameValuePair> paramsList =new ArrayList<>();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry:entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            NameValuePair param =new BasicNameValuePair(key,value);
            paramsList.add(param);

            logger.info("参数是"+key+"="+value);

        }
        HttpEntity entity =new UrlEncodedFormEntity(paramsList,"utf8");
        httpPost.setEntity(entity);


        Set<Map.Entry<String, String>> headersMap = headers.entrySet();
        for (Map.Entry<String, String> headerMap:headersMap) {
            String key = headerMap.getKey();
            String value = headerMap.getValue();
            logger.info("响应头是："+key+"="+value);
            httpPost.setHeader(key,value);
        }
        httpResponse = httpclient.execute(httpPost);
        HttpEntity resentity = httpResponse.getEntity();
        String content = EntityUtils.toString(resentity);
        logger.info("返回内容是"+content);
        return content;
    }
    //封装json格式的post 请求
    public static String postJson(String url, String json, Map<String,String> headers)throws Exception{
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(new URI(url));

        StringEntity entity = new StringEntity(json, "utf8");
        httpPost.setEntity(entity);

        Set<Map.Entry<String, String>> headersMap = headers.entrySet();
        for (Map.Entry<String, String> headerMap:headersMap) {
            String key = headerMap.getKey();
            String value = headerMap.getValue();
            httpPost.setHeader(key,value);
            logger.info("请求头是"+key+"="+value);
        }
        httpResponse = httpclient.execute(httpPost);
        HttpEntity resentity = httpResponse.getEntity();
        String content = EntityUtils.toString(resentity);
        logger.info(content);
        return content;


    }

    //封装上传文件的post接口
    public static String postUpload(String url,Map<String,String> params, Map<String,String> headers) throws Exception{
        HttpPost httpPost = new HttpPost();
        httpPost.setURI(new URI(url));


        Set<Map.Entry<String, String>> entries = params.entrySet();
        MultipartEntityBuilder builder =MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (Map.Entry<String, String> entry:entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(key.equals("file")){
                File file =new File(value);
                String filename = FilenameUtils.getName(value);
                String extension = FilenameUtils.getExtension(value);
                ContentType contentType = null;
                switch (extension.toLowerCase()){
                    case "png":
                       contentType=ContentType.IMAGE_PNG;
                        break;

                    case "jpg":
                        contentType=ContentType.IMAGE_JPEG;
                        break;

                    case "jpeg":
                        contentType =ContentType.IMAGE_JPEG;
                        break;

                    case "gif":
                        contentType=ContentType.IMAGE_BMP;
                        break;
                    case "txt":
                        contentType =ContentType.DEFAULT_TEXT;
                        break;
                    default:
                        break;
                }
             builder.addBinaryBody(key,file,contentType,filename);

            }else{
                  builder.addTextBody(key,value);
                  logger.info("file"+key+value);
            }

           HttpEntity entity= builder.build();
            httpPost.setEntity(entity);
        }
     //   httpPost.setEntity(reqEntity);
        Set<Map.Entry<String, String>> headersMap = headers.entrySet();
        for (Map.Entry<String, String> headerMap:headersMap) {
            String key = headerMap.getKey();
            String value = headerMap.getValue();
            httpPost.setHeader(key,value);
            logger.info("响应头为："+key+"="+value);
        }

        httpResponse = httpclient.execute(httpPost);
        HttpEntity resentity = httpResponse.getEntity();
        String content = EntityUtils.toString(resentity);
        logger.info("返回内容为："+content);
        return content;

    }
    public static Header[] getHeader(){
        Header[] allHeaders = httpResponse.getAllHeaders();
        for (Header header:allHeaders) {
            String name = header.getName();
            String value = header.getValue();
            logger.info("header:"+name+"="+value);

        }
        return allHeaders;
    }

    public static void main(String[] args) throws Exception {
       // String url, Map<String,String> params, Map<String,String> headers

          String url ="http://10.17.17.14:8080/pinter/com/getSku";
          Map<String,String> params =new HashMap<>();
          params.put("id","1");
        Map<String,String> headers =new HashMap<>();
        String test = get_test(url, params, headers);
        System.out.println(test);
        System.out.println(getStatusCode());
    }


}
