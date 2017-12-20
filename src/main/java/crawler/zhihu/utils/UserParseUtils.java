package crawler.zhihu.utils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import crawler.zhihu.bean.ZhihuUser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * @Description: 解析用户页面
 * @Author: Jingzeng Wang
 * @Date: Created in 9:52  2017/8/10.
 */
public class UserParseUtils {

    private volatile static UserParseUtils instance;

    /**
     * 单例模式工厂
     * 不能用太多static方法（有变成过程性语言的危险）
     *
     * @return
     */
    public static UserParseUtils getInstance() {
        if (instance == null) {
            synchronized (UserParseUtils.class) {
                if (instance == null) {
                    instance = new UserParseUtils();
                }
            }
        }
        return instance;
    }

    private UserParseUtils() {
    }


    /**
     * 解析html页面，获得user
     *
     * @param html html页面内容
     * @param url  此url
     * @return list[UserInfo,followingUser]  注意：list存储的类型是不一致的
     */
    public List pageParser(String html, String url) {
        ZhihuUser user = new ZhihuUser();
        // 取用户token  根据知乎url规则
        user.setUserToken(url.substring(url.indexOf("people/") + 7, url.indexOf("/following")));
        Document document = Jsoup.parse(html);
        String dataStateJson = document.select("[data-state]").first().attr("data-state");
        String type = "['" + user.getUserToken() + "']";  //转义
        String commonJsonPath = "$.entities.users." + type;
        try {
            JsonPath.parse(dataStateJson).read(commonJsonPath);
        } catch (PathNotFoundException e) {
            commonJsonPath = "$.entities.users.null";
        }

        setUserInfoByJsonPth(user, "username", dataStateJson, commonJsonPath + ".name");//username
        setUserInfoByJsonPth(user, "followees", dataStateJson, commonJsonPath + ".followingCount");//关注人数
        setUserInfoByJsonPth(user, "location", dataStateJson, commonJsonPath + ".locations[0].name");//位置
        setUserInfoByJsonPth(user, "business", dataStateJson, commonJsonPath + ".business.name");//行业
        setUserInfoByJsonPth(user, "employment", dataStateJson, commonJsonPath + ".employments[0].company.name");//公司
        setUserInfoByJsonPth(user, "position", dataStateJson, commonJsonPath + ".employments[0].job.name");//职位
        setUserInfoByJsonPth(user, "education", dataStateJson, commonJsonPath + ".educations[0].school.name");//学校
        setUserInfoByJsonPth(user, "major", dataStateJson, commonJsonPath + ".educations[0].major.name");//专业
        setUserInfoByJsonPth(user, "answers", dataStateJson, commonJsonPath + ".answerCount");//回答数
        setUserInfoByJsonPth(user, "asks", dataStateJson, commonJsonPath + ".questionCount");//提问数
        setUserInfoByJsonPth(user, "posts", dataStateJson, commonJsonPath + ".articlesCount");//文章数
        setUserInfoByJsonPth(user, "followers", dataStateJson, commonJsonPath + ".followerCount");//粉丝数
        setUserInfoByJsonPth(user, "agrees", dataStateJson, commonJsonPath + ".voteupCount");//赞同数
        setUserInfoByJsonPth(user, "thanks", dataStateJson, commonJsonPath + ".thankedCount");//感谢数
        try {
            Integer gender = JsonPath.parse(dataStateJson).read(commonJsonPath + ".gender");
            if (gender != null && gender == 1) {
                user.setSex("male");
            } else if (gender != null && gender == 0) {
                user.setSex("female");
            }
        } catch (PathNotFoundException e) {
            //没有该属性
            e.printStackTrace();
        }
        System.out.println(user.toString());

        //注意：集合里面放的元素类型不一样，不推荐这样用，但是为了减少url请求数，暂时这样吧！
        List arrayList = new ArrayList();
        arrayList.add(user);
        arrayList.add(getFollowingId(dataStateJson));
        return arrayList;
    }

    /**
     * jsonPath获取值，并通过反射直接注入到user中
     *
     * @param user
     * @param fieldName
     * @param json
     * @param jsonPath
     */
    private void setUserInfoByJsonPth(ZhihuUser user, String fieldName, String json, String jsonPath) {
        try {
            Object o = JsonPath.parse(json).read(jsonPath);
            Field field = user.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(user, o);
        } catch (PathNotFoundException e1) {
            //no results
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得关注者列表  第一个是原用户，不取
     *
     * @param dataJson
     */
    public Set<String> getFollowingId(String dataJson) {
        Object o = JsonPath.parse(dataJson).read("$.entities.users");
        // 可返回的是LinkedHashMap
        LinkedHashMap hashMap = (LinkedHashMap) o;
        Set<String> keySet = hashMap.keySet();
        return keySet;
    }

}
