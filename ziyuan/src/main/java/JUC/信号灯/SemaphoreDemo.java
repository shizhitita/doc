package JUC.信号灯;

import java.util.concurrent.Semaphore;

/**
 * 抢车位
 *
 * @description: TODO
 * @author: wjwei
 * @modified By: wjwei
 * @date: Created in 2022/6/5 7:55
 * @version:v1.0
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 7; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    //线程做的事情
                    System.out.println(Thread.currentThread().getName() + "抢到了车位");
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + "离开了车位");
                }
            }, String.valueOf(i)).start();
        }
    }

}
