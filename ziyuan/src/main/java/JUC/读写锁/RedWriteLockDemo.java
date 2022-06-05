package JUC.读写锁;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Duxie {

    public volatile Map<String, String> map = new HashMap<>();
    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    public void put(String key, String value) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            // 逻辑代码
            // 逻辑代码
            System.out.println(Thread.currentThread().getName() + "开始写入");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "写入完成");
        } catch (RuntimeException e) {
            // 异常处理代码
            e.printStackTrace();
        } finally {
            // 一定要执行的代码
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    public String get(String key) {
        reentrantReadWriteLock.readLock().lock();
        try {
            // 逻辑代码
            System.out.println(Thread.currentThread().getName() + "开始读取");
            return map.get(key);

        } catch (RuntimeException e) {
            // 异常处理代码
            e.printStackTrace();
        } finally {
            // 一定要执行的代码
            reentrantReadWriteLock.readLock().unlock();
        }
        return null;
    }
}

/**
 * 多个线程同时读一个资源类没有任何问题，所以为了满足并发量，读取共享资源应该可以同时进春但是
 * 如果有一个线程想去写共享资源来，就不应该再有其它线程可以对该资源进行读或写小总结：读读能其存
 * 读-写不能其存
 * 写写不能其存
 */
public class RedWriteLockDemo {

    public static void main(String[] args) {
        Duxie duxie = new Duxie();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                //线程做的事情
                duxie.put(String.valueOf(finalI), String.valueOf(finalI));
            }, String.valueOf(i)).start();
        }
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                //线程做的事情
                System.out.println(duxie.get(String.valueOf(finalI)));
            }, String.valueOf(i)).start();
        }
    }

}
