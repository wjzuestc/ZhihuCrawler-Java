package crawler.zhihu.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description: 属性文件工具类
 * @Author: Jingzeng Wang
 * @Date: Created in 10:08  2017/5/30.
 * @since 1.0.0
 */
public final class PropsUtil {

    /**
     * 加载属性文件
     *
     * @param fileName 属性文件名
     * @return
     * @since 1.0.0
     */
    public static Properties loadProps(String fileName) {
        Properties props = null;
        InputStream inputStream = null;
        //转换为流
        inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        try {
            if (inputStream == null) {
                throw new FileNotFoundException(fileName + "file is not found");
            }
            props = new Properties();
            props.load(inputStream);
        } catch (IOException e) {
            System.out.println("load properties file failure");
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("close input stream failure");
                    e.printStackTrace();
                }
            }
        }
        return props;
    }

    /**
     * 获得key对应的字符串属性值(默认为空)
     *
     * @param props 属性文件
     * @param key   要获得的属性key
     * @return 返回key对应于文件中的value
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    /**
     * 获得key对应的字符属性(指定默认值)
     *
     * @param props        属性文件
     * @param key          要获得的属性key
     * @param defaultValue 默认为空
     * @return 返回key对应于文件中的value
     */
    public static String getString(Properties props, String key, String defaultValue) {
        String value = defaultValue;
        if (props.containsKey(key)) {
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取key对应的数值型属性(默认为0)
     *
     * @param props 属性文件
     * @param key   要获得的属性key
     * @return 返回key对应于文件中的整形value
     * @since 1.0.0
     */
    public static int getInt(Properties props, String key) {
        return getInt(props, key, 0);
    }

    /**
     * 获取key对应的数值型属性(指定默认为0)
     *
     * @param props        属性文件
     * @param key          要获得的属性key
     * @param defaultValue 默认值0
     * @return 返回key对应于文件中的整形value
     * @since 1.0.0
     */
    public static int getInt(Properties props, String key, int defaultValue) {
        int value = defaultValue;
        if (props.containsKey(key)) {
            value = Integer.valueOf(props.getProperty(key));
        }
        return value;
    }

    /**
     * 获取key对应的布尔型属性(默认为false)
     *
     * @param props 属性文件
     * @param key   要获得的属性key
     * @return 返回key对应于文件中的布尔型value
     * @since 1.0.0
     */
    public static boolean getBoolean(Properties props, String key) {
        return getBoolean(props, key, false);
    }

    /**
     * 获取key对应的数值型属性(指定默认为false)
     *
     * @param props        属性文件
     * @param key          要获得的属性key
     * @param defaultValue 默认值false
     * @return 返回key对应于文件中的布尔型value
     * @since 1.0.0
     */
    public static boolean getBoolean(Properties props, String key, Boolean defaultValue) {
        boolean value = defaultValue;
        if (props.containsKey(key)) {
            value = Boolean.valueOf(props.getProperty(key));
        }
        return value;
    }
}
