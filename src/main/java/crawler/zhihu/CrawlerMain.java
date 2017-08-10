package crawler.zhihu;

import crawler.zhihu.service.ZhihuPicCrawler;
import crawler.zhihu.service.ZhihuUserCrawler;

/**
 * @Description: 运行主函数
 * 1. 获取某个问题下的图片，并存到本地
 * 2. 爬取知乎用户信息，并持久化到数据库，然后进行分析
 * @Author: Jingzeng Wang
 * @Date: Created in 20:46  2017/8/8.
 */
public class CrawlerMain {

    public static void main(String[] args) {

        // picSpider();// 爬取某个问题下的图片

        userSpider();   //爬取用户信息

    }

    /**
     * 爬取下载知乎某个问题的图片，存到本地
     * url格式："https://www.zhihu.com/question/30116337"
     */
    public static void picSpider() {
        //定义爬虫入口url地址
        String url = "https://www.zhihu.com/question/21100397";

        ZhihuPicCrawler.downloadQustionPic(url);
    }

    /**
     * 爬取知乎用户信息，持久化到数据库
     * url为个人主页如："https://www.zhihu.com/people/wang-ni-ma-94/following"
     */
    public static void userSpider() {
        //定义爬虫入口url 以我的知乎链接为入口  （可换成知乎一些名人进行爬取）
        String url = "https://www.zhihu.com/people/wang-jing-zeng-36/following";

        try {
            ZhihuUserCrawler.startCrawler(url);
        } catch (InterruptedException e) {
            System.out.println("线程意外终止！！");
            e.printStackTrace();
        }
    }
}
