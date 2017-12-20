# ZhihuCrawler简介
利用原生Java实现，不依赖爬虫框架。并针对线程池性能、异常以及反爬虫措施进行多次
         优化，容错性及爬取速度良好，并在后期利用 Redis 实现分布式爬虫。此爬虫项目可完成百万级知乎用户信息爬取，并进行分析展示。
**可实现：**

    1.百万知乎用户信息的爬取、持久化与展示；
    2.某一个知乎问题下的图片爬取，并存储到本地，让你轻松看美女；
    3.扩展Redis作为爬取队列实现分布式爬取。

## ZhihuCrawler项目模块介绍

  **ZhihuCrawler**
   
    -- bean包     知乎用户信息及某一问题下的图片bean抽象
    -- module包   断点续爬实现及json的格式输出
    -- service包  爬虫业务类：多线程知乎用户爬取及图片爬取
    -- utils包    工具类：包括反爬代理、html页面获取、页面解析、持久化的工具类
    -- CrawlerMain  启动类
    
## 百万知乎用户信息展示

利用百度开源框架Echarts进行展示,由于没搭栈，就直接放了图片：

https://github.com/wjzuestc/ZhihuCrawler-Java/blob/master/数据分析结果1.png

https://github.com/wjzuestc/ZhihuCrawler-Java/blob/master/数据分析结果2.png