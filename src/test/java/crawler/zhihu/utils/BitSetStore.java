package crawler.zhihu.utils;

import java.io.*;
import java.util.BitSet;

/**
 * @Description:
 * 代理ip（没有可用的ip）  验证码输入  若无法解决
 * 最次的方法：持久化BitSet，多次爬取，下次爬的时候去重  (blockingqueue也可以持久化)
 * @Author: Jingzeng Wang
 * @Date: Created in 22:23  2017/8/10.
 */
public class BitSetStore {

    public static void main(String[] args) {
        BitSet bitSet = new BitSet(50000);
        bitSet.set(50);
        bitSet.set(51);
        bitSet.set(52);
        bitSet.set(53);
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("bit.txt")));
            objectOutputStream.writeObject(bitSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("bit.txt")));
            Object object = objectInputStream.readObject();
            BitSet bitSet1 = (BitSet) object;
            System.out.println(bitSet1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(bitSet.toString());
    }
}
