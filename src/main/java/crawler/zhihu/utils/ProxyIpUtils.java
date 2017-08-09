package crawler.zhihu.utils;

import com.sun.deploy.net.HttpUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description: 解析url使用代理ip 仿真被封ip
 * @Author: Jingzeng Wang
 * @Date: Created in 21:43  2017/8/9.
 */
public final class ProxyIpUtils {

    public static void proxyIp() {
        try {
            List<String> ipList = new ArrayList<>();
            BufferedReader proxyIpReader = new BufferedReader(new InputStreamReader(HttpUtils.class.getResourceAsStream("/proxyip.txt")));

            String ip = "";
            while ((ip = proxyIpReader.readLine()) != null) {
                ipList.add(ip);
            }

            Random random = new Random();
            int randomInt = random.nextInt(ipList.size());
            String ipport = ipList.get(randomInt);
            String proxyIp = ipport.substring(0, ipport.lastIndexOf(":"));
            String proxyPort = ipport.substring(ipport.lastIndexOf(":") + 1, ipport.length());

            //进行代理ip
            System.setProperty("http.maxRedirects", "50");
            System.getProperties().setProperty("proxySet", "true");
            System.getProperties().setProperty("http.proxyHost", proxyIp);
            System.getProperties().setProperty("http.proxyPort", proxyPort);

            System.out.println("设置代理ip为：" + proxyIp + "端口号为：" + proxyPort);
        } catch (Exception e) {
            System.out.println("重新设置代理ip");
            proxyIp();
        }
    }

}
