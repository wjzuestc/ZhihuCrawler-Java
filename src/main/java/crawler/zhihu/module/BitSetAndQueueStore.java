package crawler.zhihu.module;

import java.io.*;
import java.util.BitSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: 序列化BitSet 防止出现错误，多次爬，后边没法去重了
 * @Author: Jingzeng Wang
 * @Date: Created in 22:16  2017/8/10.
 */
public class BitSetAndQueueStore {

    /**
     * 程序出问题后，保存BitSet 为下一次爬做准备
     *
     * @param bitSet
     */
    public static void serilzeAndStore(BitSet bitSet, BlockingQueue<String> queue) {
        try {
            ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(new FileOutputStream(new File("bitSet.txt")));
            ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(new FileOutputStream(new File("queue.txt")));
            objectOutputStream1.writeObject(bitSet);
            objectOutputStream2.writeObject(queue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("程序出问题终止了！保存BitSet和urlQueue成功！");
    }

    /**
     * 反序列化出上次的爬的BitSet
     *
     * @return
     */
    public static BitSet getBitSetFromFile() {
        BitSet bitSet = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("bitSet.txt")));
            Object object = objectInputStream.readObject();
            bitSet = (BitSet) object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("读取bitSet成功！");
        return bitSet;
    }

    /**
     * 反序列化出上次的爬的还未保存的urlQueue
     *
     * @return
     */
    public static LinkedBlockingQueue<String> getQueueFromFile() {
        LinkedBlockingQueue<String> queue = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("queue.txt")));
            Object object = objectInputStream.readObject();
            queue = (LinkedBlockingQueue<String>) object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("读取urlQueue成功！");
        return queue;
    }

}
