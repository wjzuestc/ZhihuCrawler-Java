package crawler.zhihu.service;

import crawler.zhihu.bean.ZhihuPic;
import crawler.zhihu.utils.DownloadToFileUtils;
import crawler.zhihu.utils.WebHtmlUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 爬取某个问题下的图片实现
 * @Author: Jingzeng Wang
 * @Date: Created in 21:21  2017/8/8.
 */
public class ZhihuPicCrawler {

    /**
     * 爬取并下载某个问题下的图片
     *
     * @param url
     */
    public static void downloadQustionPic(String url) {
        String regex = "https://www.zhihu.com/question/(.*?)";  //验证url的合法性
        if (WebHtmlUtils.checkUrl(url, regex)) {
            ZhihuPic zhihuPicBean = new ZhihuPic(url);
            System.out.println("正在连接爬虫地址：" + url);
            String html = WebHtmlUtils.getHtmlByUrl(url, false);
            if (html != null) {
                System.out.println("连接成功：" + url);
                System.out.println("正在获取图片地址，请稍后...");
                //获得匹配问题标题
                Document document = Jsoup.parse(html);
                Elements title = document.head().getElementsByTag("title");
                zhihuPicBean.setQuestionName(title.html());
                // 通过正则表达式匹配图片地址 <noscript><img src="https://pic3.zhimg.com/6a239f643df22ead9a1d3b19f4a61a4a_b.jpg"。。
                // 这种方式有问题。。。很多匹配不到
                ArrayList<String> picUrl = new ArrayList<String>();
                Pattern pattern = Pattern.compile("</noscript><img.+?src=\"(https.+?)\".+?");  // TODO 匹配不全以及模拟js点击'更多'
                Matcher matcher = pattern.matcher(html);
                boolean isFind = matcher.find();
                while (isFind) {
                    picUrl.add(matcher.group(1));
                    isFind = matcher.find();
                }
                zhihuPicBean.setPicUrl(picUrl);
                System.out.println("图片地址获取成功，地址为：");
                for (String pic : picUrl) {
                    System.out.println(pic);
                }
                // 下载图片到磁盘文件
                System.out.println("开始下载图片:");
                String filePath = "D:" + File.separator + "知乎爬虫图片";
                if (DownloadToFileUtils.downloadToFile(zhihuPicBean, filePath)) {
                    System.out.println();
                    System.out.println("图片下载成功，请到" + filePath + File.separator + zhihuPicBean.getQuestionName() + "进行查看欣赏！");
                } else {
                    System.out.println("图片下载失败！！！");
                }
            } else {
                System.out.println(url + "连接失败，html为空！");
            }
        } else {
            System.out.println("url" + url + "不合法，无法爬取下载该url下的图片！");
        }

    }

}
