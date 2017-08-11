package crawler.zhihu.utils;

import com.alibaba.druid.pool.DruidDataSource;
import crawler.zhihu.bean.ZhihuUser;
import crawler.zhihu.module.BitSetAndQueueStore;
import crawler.zhihu.service.ZhihuUserCrawler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Properties;

/**
 * @Description: 持久化到数据库的工具类
 * @Author: Jingzeng Wang
 * @Date: Created in 15:30  2017/8/9.
 */
public class DBUtils {

    //Druid连接池
    private static final DruidDataSource DATA_SOURCE;

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final int InitialSize;
    private static final int MinIdle;
    private static final int MaxActive;
    private static final boolean PoolPreparedStatements;


    //获取数据库连接
    static {
        Properties props = PropsUtil.loadProps("jdbc.properties");
        DRIVER = PropsUtil.getString(props, "jdbc.driver");
        URL = PropsUtil.getString(props, "jdbc.url");
        USERNAME = PropsUtil.getString(props, "jdbc.username");
        PASSWORD = PropsUtil.getString(props, "jdbc.password");
        InitialSize = PropsUtil.getInt(props, "druid.initialSize");
        MinIdle = PropsUtil.getInt(props, "druid.minIdle");
        MaxActive = PropsUtil.getInt(props, "druid.maxActive");
        PoolPreparedStatements = PropsUtil.getBoolean(props, "druid.poolPreparedStatements");

        //把数据库管理交给数据库连接池进行管理。自动回收连接
        DATA_SOURCE = new DruidDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        DATA_SOURCE.setInitialSize(InitialSize);
        DATA_SOURCE.setMinIdle(MinIdle);
        DATA_SOURCE.setMaxActive(MaxActive);
        DATA_SOURCE.setPoolPreparedStatements(PoolPreparedStatements);
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            System.out.println("数据库连接池获取失败！");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 执行插入操作
     *
     * @param zhihuUser
     * @return
     */
    public static boolean insert(ZhihuUser zhihuUser) {
        //这说明程序出问题了，ip被封或者其他原因，没拿到数据，则强制退出，并保存BitSet和阻塞队列
        Connection connection = getConnection();
        PreparedStatement ps = null;
        int count = 0;
        if (connection != null) {
            try {
                String sql = "insert into user " +
                        "(username,sex,location,business,employment,position,education,agrees,thanks,asks,answers,posts,followees,followers,usertoken,major) " +
                        "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = connection.prepareStatement(sql);
                ps.setString(1, zhihuUser.getUsername());
                ps.setString(2, zhihuUser.getSex());
                ps.setString(3, zhihuUser.getLocation());
                ps.setString(4, zhihuUser.getBusiness());
                ps.setString(5, zhihuUser.getEmployment());
                ps.setString(6, zhihuUser.getPosition());
                ps.setString(7, zhihuUser.getEducation());
                ps.setInt(8, zhihuUser.getAgrees());
                ps.setInt(9, zhihuUser.getThanks());
                ps.setInt(10, zhihuUser.getAsks());
                ps.setInt(11, zhihuUser.getAnswers());
                ps.setInt(12, zhihuUser.getPosts());
                ps.setInt(13, zhihuUser.getFollowees());
                ps.setInt(14, zhihuUser.getFollowers());
                ps.setString(15, zhihuUser.getUserToken());
                ps.setString(16, zhihuUser.getMajor());
                //存储user
                count = ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                // 规定的name不可以是null
                System.out.println("程序出错了，没拿到数据，需要保存BisSet并退出！！");
                //保存数据
                BitSetAndQueueStore.serilzeAndStore(UrlFilterUtils.bits, ZhihuUserCrawler.urlQueue);
                //强制程序退出
                System.exit(1);
            } catch (SQLException e) {
                System.out.println("数据插入失败！");
                e.printStackTrace();
            } finally {
                try {
                    ps.close();
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("数据库连接关闭异常");
                    e.printStackTrace();
                }
            }
        }
        if (count <= 0) {
            System.out.println("数据插入到数据库失败！！");
            return false;
        } else {
            System.out.println("数据插入到数据库成功！！");
            return true;
        }
    }

}
