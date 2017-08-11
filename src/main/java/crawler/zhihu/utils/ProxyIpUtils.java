package crawler.zhihu.utils;

import com.sun.deploy.net.HttpUtils;

import java.io.BufferedReader;
import java.io.IOException;
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

    private volatile static ProxyIpUtils instance;

    public final static List<String> IP_LIST;

    static  {
        IP_LIST = new ArrayList<>();
        BufferedReader proxyIpReader = new BufferedReader(new InputStreamReader(HttpUtils.class.getResourceAsStream("/proxyip.txt")));

        String ip = "";
        try {
            while ((ip = proxyIpReader.readLine()) != null) {
                IP_LIST.add(ip);
            }
        } catch (IOException e) {
            System.out.println("读取ip错误！！");
            e.printStackTrace();
        }
    }

    /**
     * 单例模式工厂
     * 不能用太多static方法（有变成过程性语言的危险）
     *
     * @return
     */
    public static ProxyIpUtils getInstance() {
        if (instance == null) {
            synchronized (UserParseUtils.class) {
                if (instance == null) {
                    instance = new ProxyIpUtils();
                }
            }
        }
        return instance;
    }

    private ProxyIpUtils() {
    }

    public void proxyIp() {

            Random random = new Random();
            int randomInt = random.nextInt(IP_LIST.size());
            String ipport = IP_LIST.get(randomInt);
            String proxyIp = ipport.substring(0, ipport.lastIndexOf(":"));
            String proxyPort = ipport.substring(ipport.lastIndexOf(":") + 1, ipport.length());

            //进行代理ip
            System.getProperties().setProperty("proxySet", "true");
            System.getProperties().setProperty("http.proxyHost", proxyIp);
            System.getProperties().setProperty("http.proxyPort", proxyPort);

//            System.out.println("设置代理ip为：" + proxyIp + "端口号为：" + proxyPort);
    }

}
