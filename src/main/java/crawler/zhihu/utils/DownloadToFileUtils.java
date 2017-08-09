package crawler.zhihu.utils;

import crawler.zhihu.bean.ZhihuPic;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * @Description: 文件下载工具类
 * @Author: Jingzeng Wang
 * @Date: Created in 9:47  2017/8/9.
 */
public class DownloadToFileUtils {

    /**
     * 下载图片到本地磁盘
     *
     * @param zhihuPicBean
     * @param filePath
     * @return
     */
    public static boolean downloadToFile(ZhihuPic zhihuPicBean, String filePath) {
        //创建文件路径
        filePath = filePath + File.separator + zhihuPicBean.getQuestionName();
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //图片地址
        ArrayList<String> picUrls = zhihuPicBean.getPicUrl();
        int count = 1;
        for (String s : picUrls) {
            System.out.println("正在下载第" + count + "张图片，请稍后...");  //用logger4j来处理。。。这样拼接字符串输出效率太低
            StringBuilder sb = new StringBuilder();
            sb.append(filePath).append(File.separator).append("第").append(count).append("张图片").append(".jpg");
            String imagePath = sb.toString();
            BufferedInputStream inputStream = null;
            FileOutputStream outputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            try {
                URL picUrl = new URL(s);
                //打开网络输入流
                inputStream = new BufferedInputStream(picUrl.openStream());
                outputStream = new FileOutputStream(new File(imagePath));
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                byte[] bytes = new byte[1024];
                int length = -1;
                while ((length = inputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, length);
                }
                count++;
            } catch (IOException e) {
                System.out.println("文件下载出现异常！");
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    bufferedOutputStream.close();
                    outputStream.close();  //后关
                } catch (IOException e) {
                    System.out.println("流关闭异常！！");
                    e.printStackTrace();
                }
            }
            System.out.println("此图片下载完成！");
        }
        return true;
    }
}
