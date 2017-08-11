package crawler.zhihu.utils;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

/**
 * @Description: 测试代理ip是否可用
 * @Author: Jingzeng Wang
 * @Date: Created in 20:21  2017/8/10.
 */
public class ProxyIpUtilsTest {
    @Test
    public void proxyIp() throws Exception {

        List<String> ipList = ProxyIpUtils.IP_LIST;
        System.out.println(ipList);
        String url = "https://www.zhihu.com/people/wang-jing-zeng-36/following";
        int count = 0;

        for (String ipport : ipList) {
            String result = "";
            // 定义一个缓冲字符输入流
            BufferedReader in = null;
            try {
                String proxyIp = ipport.substring(0, ipport.lastIndexOf(":"));
                String proxyPort = ipport.substring(ipport.lastIndexOf(":") + 1, ipport.length());
                //进行代理ip  当代理ip不可用时，会直接调用本地网络来进行连接  caocaocaocaocao
//                System.getProperties().setProperty("proxySet", "true");
//                System.getProperties().setProperty("http.proxyHost", proxyIp);
//                System.getProperties().setProperty("http.proxyPort", proxyPort);

                //这样设置如果代理不可用，会直接抛出异常，不再用本地网络ip进行
                SocketAddress addr = new InetSocketAddress("180.126.61.99", Integer.parseInt("81"));
                Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

                System.out.println(System.getProperty("http.proxyHost"));
                // 将string转成url对象
                URL realUrl = new URL(url);
                // 初始化一个链接到那个url的连接
                URLConnection connection = realUrl.openConnection(proxy);
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
                System.out.println(ipport);
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
            System.out.println(count++);
        }
    }

    @Test
    public void vilidIp() {
        /**
         * 通过代理对象连接
         * @param address
         * @return
         */
        SocketAddress addr = new InetSocketAddress("121.232.145.80", Integer.parseInt("9000"));
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        try {
            URL url = new URL("https://www.zhihu.com/people/kou-de-er-20/following");
            URLConnection conn = url.openConnection(proxy);
            conn.connect();
            // 初始化 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            System.out.println(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}