package crawler.zhihu.utils;

import crawler.zhihu.bean.ZhihuUser;
import org.junit.Test;

/**
 * @Description: 测试持久化
 * @Author: Jingzeng Wang
 * @Date: Created in 16:44  2017/8/9.
 */
public class DBUtilsTest {
    @Test
    public void insert() throws Exception {
        ZhihuUser zhihuUser = new ZhihuUser();
        zhihuUser.setUsername("张三");
        zhihuUser.setSex("男");
        zhihuUser.setLocation("Beijing");
        zhihuUser.setBusiness("互联网");
        zhihuUser.setEmployment("Alibaba");
        zhihuUser.setPosition("CTO");
        zhihuUser.setEducation("清华大学");
        zhihuUser.setMajor("计算机");
        zhihuUser.setAgrees(4500);
        zhihuUser.setThanks(5656);
        zhihuUser.setAsks(120);
        zhihuUser.setAnswers(564);
        zhihuUser.setPosts(12);
        zhihuUser.setFollowees(544);
        zhihuUser.setFollowers(4545);
        DBUtils.insert(zhihuUser);
    }

}