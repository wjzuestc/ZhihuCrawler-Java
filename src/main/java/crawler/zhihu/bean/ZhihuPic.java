package crawler.zhihu.bean;

import java.util.ArrayList;

/**
 * @Description: 知乎问题中的图片地址
 * @Author: Jingzeng Wang
 * @Date: Created in 20:36  2017/8/8.
 */
public class ZhihuPic {

    private String url;  //知乎爬取地址(某一个问题)

    private String questionName; //该问题的名字

    private ArrayList<String> picUrl;  //该问题的所有图片地址

    public ZhihuPic(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }
}
