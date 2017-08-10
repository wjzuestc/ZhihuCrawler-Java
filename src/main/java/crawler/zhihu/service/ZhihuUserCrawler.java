package crawler.zhihu.service;

import crawler.zhihu.bean.ZhihuUser;
import crawler.zhihu.utils.DBUtils;
import crawler.zhihu.utils.UrlFilterUtils;
import crawler.zhihu.utils.UserParseUtils;
import crawler.zhihu.utils.WebHtmlUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Description: 爬取用户信息数据
 * @Author: Jingzeng Wang
 * @Date: Created in 20:29  2017/8/9.
 */
public class ZhihuUserCrawler {
    //阻塞队列，存储未爬取的url 无界的queue 其实maxPoolSize 也没什么意义了
    private static BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>();
    //创建固定线程数量的连接池  将线程数设置为10
    private static Executor executor = Executors.newFixedThreadPool(10);

    public static void startCrawler(String startUrl) throws InterruptedException {
        urlQueue.put(startUrl);   //先将初始url放入队列
        System.out.println("开始进行用户信息爬取...........");

        //线程池创建线程执行
        for (int i = 0; i < 5; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        String url = getUrlFromQueue();
                        if (url != null) {
                            if (!UrlFilterUtils.exist(url)) {
                                UrlFilterUtils.add(url);
                                spilderTheUrl(url);
                            } else {
                                System.out.println("此url重复，不再进行爬取");
                            }
                        }
                    }
                }
            });
        }
        //监听线程，当线程池的线程数量因为某些情况终止了，则创建线程进入线程池
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (((ThreadPoolExecutor) executor).getActiveCount() < 5) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                while (true) {
                                    String url = getUrlFromQueue();
                                    if (url != null) {
                                        if (!UrlFilterUtils.exist(url)) {
                                            UrlFilterUtils.add(url);
                                            spilderTheUrl(url);
                                        } else {
                                            System.out.println("此url重复，不再进行爬取");
                                        }
                                    }
                                }
                            }
                        });
                    }
                    // 如果线程池的线程数量稳定，则休眠本线程，让出cpu
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        System.out.println("线程在休眠时出现意外！！");
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    /**
     * 从阻塞队列中获得一个url进行爬取
     *
     * @return
     */
    private static String getUrlFromQueue() {
        String url = null;
        try {
            url = urlQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 爬取此url，获得相关信息并进行持久化
     *
     * @param url
     */
    private static void spilderTheUrl(String url) {
        //通过代理的方式url的html页面
        String html = WebHtmlUtils.getHtmlByUrlProxyIp(url);
        //解析页面
        List list = UserParseUtils.getInstance().pageParser(html, url);
        ZhihuUser zhihuUser = (ZhihuUser) list.get(0);
        //插入到数据库中
        DBUtils.insert(zhihuUser);
        //把用户所关注的人的url加入到阻塞队列
        Set<String> userIdSet = (Set<String>) list.get(1);
        addUserFollowingUrl(userIdSet);
    }

    /**
     * 将第一页的关注者添加到阻塞队列：不爬取所有的关注：1. 怕关注者太多，阻塞队列会出现异常
     * 2. 这样可减少爬取次数，不用再更新另一页了
     * int i = 1;   // 就拿第一页的关注者  爬多页方法
     * String userFollowingUrl = url + "?page=" + i;
     * https://www.zhihu.com/people/wang-ni-ma-94/following
     *
     * @param userIdSet
     */
    private static void addUserFollowingUrl(Set<String> userIdSet) {
        //判断当前页关注人数是否为0，是的话就跳出循环
        //因为第一个是用户本身，不是它的关注者  可以通过bloom filter来判断，但在这里进行判断消除更好！
        if (userIdSet.size() > 1) {
            int index = 1;
            for (String userId : userIdSet) {
                if (index == 1) {
                    index++;
                    continue;
                }
                try {
                    urlQueue.put("https://www.zhihu.com/people/" + userId + "/following");
                } catch (InterruptedException e) {
                    System.out.println("将url添加到阻塞队列失败！！");
                    e.printStackTrace();
                }
            }
        }
    }
}
