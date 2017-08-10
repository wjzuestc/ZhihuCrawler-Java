package crawler.zhihu.utils;

import java.util.BitSet;

/**
 * @Description: url去重算法 Bloom Filter布隆过滤器算法
 * 在能容忍低错误率的应用场合下，Bloom Filter通过极少的错误（将“不在”集合内的元素判定为“在”）换取了存储空间的极大节省
 * 利用多个不同的Hash函数    不在 就一定不在   在  不一定在
 * HashSet和HashMap也可以，但是消耗内存的32倍
 * @Author: Jingzeng Wang
 * @Date: Created in 17:36  2017/8/9.
 */
public final class UrlFilterUtils {

    // BitSet初始分配2^28个bit
    private static final int DEFAULT_SIZE = 1 << 28;
    // 创建BitSet  大小为2^28
    private static BitSet bits = new BitSet(DEFAULT_SIZE);
    // 不同哈希函数的种子，一般应取质数
    private static final int[] SEEDS = new int[]{3, 5, 7, 11, 13, 31, 37, 61};

    /**
     * 添加元素  把8个hash函数映射的位置都置为1
     * 加synchronized类锁
     *
     * @param key
     * @return
     */
    public static synchronized boolean add(String key) {
        int keyCode[] = lrandom(key);
        bits.set(keyCode[0]);
        bits.set(keyCode[1]);
        bits.set(keyCode[2]);
        bits.set(keyCode[3]);
        bits.set(keyCode[4]);
        bits.set(keyCode[5]);
        bits.set(keyCode[6]);
        bits.set(keyCode[7]);
        return true;
    }

    /**
     * 判断key是否已存在：8个hash位置必须都为1才表示存在
     *
     * @param key
     * @return
     */
    public static boolean exist(String key) {
        int keyCode[] = lrandom(key);
        if (bits.get(keyCode[0]) &&
                bits.get(keyCode[1])
                && bits.get(keyCode[2])
                && bits.get(keyCode[3])
                && bits.get(keyCode[4])
                && bits.get(keyCode[5])
                && bits.get(keyCode[6])
                && bits.get(keyCode[7])) {
            return true;
        }
        return false;
    }

    /**
     * 得到8个hash值
     *
     * @param key
     * @return
     */
    private static int[] lrandom(String key) {
        int[] randomsum = new int[8];
        randomsum[0] = hashCode(key, SEEDS[0]);
        randomsum[1] = hashCode(key, SEEDS[1]);
        randomsum[2] = hashCode(key, SEEDS[2]);
        randomsum[3] = hashCode(key, SEEDS[3]);
        randomsum[4] = hashCode(key, SEEDS[4]);
        randomsum[5] = hashCode(key, SEEDS[5]);
        randomsum[6] = hashCode(key, SEEDS[6]);
        randomsum[7] = hashCode(key, SEEDS[7]);
        return randomsum;
    }

    /**
     * hash 函数
     *
     * @param key
     * @param seed
     * @return
     */
    private static int hashCode(String key, int seed) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = seed * h + key.charAt(i);
        }
        return changeInteger(h);
    }

    /**
     * 将8个hash值映射到bitset的位置
     *
     * @param h
     * @return
     */
    private static int changeInteger(int h) {
        return (DEFAULT_SIZE - 1) & h;
    }

}
