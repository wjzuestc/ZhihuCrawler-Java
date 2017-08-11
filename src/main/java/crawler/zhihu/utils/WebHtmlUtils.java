package crawler.zhihu.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 获取url的html页面
 * @Author: Jingzeng Wang
 * @Date: Created in 21:41  2017/8/8.
 */
public class WebHtmlUtils {

    /**
     * 通过代理ip的方式获取url的html页面
     *
     * @param url
     * @return
     */
    public static String getHtmlByUrlProxyIp(String url) {
        //设置代理
        ProxyIpUtils.getInstance().proxyIp();
        // 定义一个字符串用来存储网页内容
        String result = "";
        // 定义一个缓冲字符输入流
        BufferedReader in = null;
        try {
            // 将string转成url对象
            URL realUrl = new URL(url);
            // 初始化一个链接到那个url的连接
            URLConnection connection = realUrl.openConnection();
            // 开始实际的连接
            connection.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            // 用来临时存储抓取到的每一行的数据
            String line;
            while ((line = in.readLine()) != null) {
                // 遍历抓取到的每一行并将其存储到result里面
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取url的html
     *
     * @param url
     * @param isProxyIp 是否使用代理
     * @return
     */
    public static String getHtmlByUrl(String url, boolean isProxyIp) {
        String html = null;
        //创建httpClient对象
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //以get方式请求该URL
        HttpGet httpget = new HttpGet(url);
        // 用httpClient 代理出现错误    TODO  httpClient不同版本改动较大
/*        if (isProxyIp) {
            System.out.println(System.getProperty("http.proxyHost"));
            HttpHost proxy = new HttpHost(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")));
            httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
        }*/

        try {
            //得到responce对象
            HttpResponse responce = httpClient.execute(httpget);
            //返回码
            int resStatu = responce.getStatusLine().getStatusCode();
            //200正常  其他就不对
            if (resStatu == HttpStatus.SC_OK) {
                //获得相应实体
                HttpEntity entity = responce.getEntity();
                if (entity != null) {
                    //获得html源代码
                    html = EntityUtils.toString(entity);
                }
            }
        } catch (Exception e) {
            System.out.println("访问【" + url + "】出现异常!");
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return html;
    }

    /**
     * 判断url是否合法
     * 1. https://www.zhihu.com/question/2222 爬取图片的url
     *
     * @param url
     * @return
     */
    public static boolean checkUrl(String url, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
