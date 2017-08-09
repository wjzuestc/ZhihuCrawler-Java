package crawler.zhihu.utils;

import org.junit.Test;

/**
 * @Description:
 * @Author: Jingzeng Wang
 * @Date: Created in 20:00  2017/8/9.
 */
public class UrlFilterUtilsTest {

    private final String[] URLS = {
            "http://www.csdn.net/",
            "http://www.baidu.com/",
            "http://www.google.com.hk",
            "http://www.cnblogs.com/",
            "http://www.zhihu.com/",
            "https://www.shiyanlou.com/",
            "http://www.google.com.hk",
            "https://www.shiyanlou.com/",
            "http://www.csdn.net/"
    };

    @Test
    public void add() throws Exception {
        for (String url : URLS) {
            if (!UrlFilterUtils.exist(url)) {
                UrlFilterUtils.add(url);
            } else {
                System.out.println("url已存在" + url);
            }
        }
    }

}