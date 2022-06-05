package JUC.倒计时器;

import java.util.concurrent.CountDownLatch;

/**
 * @description: TODO
 * @author: wjwei
 * @modified By: wjwei
 * @date: Created in 2022/6/5 7:13
 * @version:v1.0
 */
public class CountDownLatchDemo {


    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                //线程做的事情
                System.out.println("第"+Thread.currentThread().getName()+"位同学离开教室");
                countDownLatch.countDown();
            }, String.valueOf(i)).start();

        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" 班长关门走人");
    }

}
