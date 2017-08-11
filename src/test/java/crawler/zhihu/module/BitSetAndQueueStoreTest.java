package crawler.zhihu.module;

import org.junit.Test;

import java.io.File;
import java.util.BitSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: 测试是否可以保存和读取成功
 * @Author: Jingzeng Wang
 * @Date: Created in 19:03  2017/8/11.
 */
public class BitSetAndQueueStoreTest {

    public static BitSet bitSet = new BitSet(50000);
    public static BlockingQueue<String> urlQueue = new LinkedBlockingQueue<String>();

    @Test
    public void serilzeAndStore() throws Exception {
        bitSet.set(50);
        bitSet.set(51);
        bitSet.set(52);
        bitSet.set(53);

        urlQueue.put("dasdsa");
        urlQueue.put("dasds");
        urlQueue.put("dasdsdda");
        urlQueue.put("dasdsdddda");

        BitSetAndQueueStore.serilzeAndStore(bitSet, urlQueue);

        BitSet bitSet1 = BitSetAndQueueStore.getBitSetFromFile();
        LinkedBlockingQueue<String> urlQueue = BitSetAndQueueStore.getQueueFromFile();

        System.out.println(bitSet1);
        System.out.println(urlQueue);

    }

    @Test
    public void testFile() {
        File file = new File("queue.txt");
        if (file.exists()) {
            System.out.println("文件存在");
        }
    }

}











