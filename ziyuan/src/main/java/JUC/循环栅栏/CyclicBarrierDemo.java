package JUC.循环栅栏;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 集齐七颗龙珠召唤神龙
 *
 * @description: TODO
 * @author: wjwei
 * @modified By: wjwei
 * @date: Created in 2022/6/5 7:29
 * @version:v1.0
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,()->{System.out.println(" 召唤神龙");});
        for (int i = 1; i <= 7; i++) {
            new Thread(() -> {
                //线程做的事情
                System.out.println("收集到第" + Thread.currentThread().getName() + "颗龙珠");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                } finally {
                    cyclicBarrier.reset();
                }
            }, String.valueOf(i)).start();
        }

    }

}
