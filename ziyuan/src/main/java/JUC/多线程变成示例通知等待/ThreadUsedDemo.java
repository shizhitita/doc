package JUC.多线程变成示例通知等待;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock 替换 synchronized
 * synchronized -> lock
 * this -> lock.newCondition();
 * wait -> await
 * notifyAll -> signalAll
 */
class AirCondition {

    private int number = 0;

    public synchronized void increment() throws Exception {
        //1 判断
//        if (number != 0) {
        while (number != 0) {
            this.wait();
        }
        //2干活
        number++;
        System.out.println(Thread.currentThread().getName() + "\t" + number);
        //3通知
        this.notifyAll();
    }

    public synchronized void decrement() throws Exception {
        //1 判断
//        if (number != 0) {
        while (number == 0) {
            this.wait();
        }
        //2干活
        number--;
        System.out.println(Thread.currentThread().getName() + "\t" + number);
        //3通知
        this.notifyAll();
    }

    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void decrementLock() throws Exception {
        //1 判断
        lock.lock();
        try {
            while (number == 0) {
                condition.await();
            }
            //2干活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //3通知
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }

    public void incrementLock() throws Exception {
        //1 判断
        lock.lock();
        try {
            while (number != 0) {
                condition.await();
            }
            //2干活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //3通知
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }


}

/**
 * 备注：多线程之间按顺序调用，实A->B->C
 * 三个线程启动，要求如下：
 * AA打5次，BB打印10次，cc打15次
 * 接着
 * AA打印5次，BB打印10次，cc打15次
 * 10轮
 */
class ShareData {

    private int number = 1; // A:1 B:2 C:3

    /**
     * lock 对比 synchronized
     * lock可以拥有多把锁，通知可以精确通知，A通知B,B通知C,C通知A
     * 防止出现虚假唤醒。
     * <p>
     * 为什么要用lock？
     * 1：可以精确通知，synchronized只能使用线程优先级来处理，比较繁琐。
     * 2：JVM层次的？todo 已经忘记了，想起再来补充
     */
    Lock printLock = new ReentrantLock();
    Condition conditionA = printLock.newCondition();
    Condition conditionB = printLock.newCondition();
    Condition conditionC = printLock.newCondition();

    public void print5() {
        //1 判断
        printLock.lock();
        try {
            while (number != 1) {
                conditionA.await();
            }
            //2干活
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 2;
            //3通知
            conditionB.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            printLock.unlock();
        }

    }

    public void print10() {
        //1 判断
        printLock.lock();
        try {
            while (number != 2) {
                conditionB.await();
            }
            //2干活
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 3;
            //3通知
            conditionC.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            printLock.unlock();
        }

    }

    public void print15() {
        //1 判断
        printLock.lock();
        try {
            while (number != 3) {
                conditionC.await();
            }
            //2干活
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            number = 1;
            //3通知
            conditionA.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            printLock.unlock();
        }

    }



}

/**
 * 题月：现在两个线程，可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 实现交替，变量初始值为0。
 * 1：高集低合前提下，线程操作资源类
 * 2：判断/干活/通知
 * 3：防止虚假唤醒
 */
public class ThreadUsedDemo {


    public static void main(String[] args) throws Exception {

        AirCondition airCondition = new AirCondition();
//        new Thread(() -> {
//            for (int i = 0; i <= 10; i++) {
//                try {
//                    Thread.sleep(100);
//                    airCondition.incrementLock();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "A").start();
//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {
//                    Thread.sleep(200);
//                    airCondition.decrementLock();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "B").start();
//        new Thread(() -> {
//            for (int i = 0; i <= 10; i++) {
//                try {
//                    Thread.sleep(300);
//                    airCondition.incrementLock();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "C").start();
//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {
//                    Thread.sleep(400);
//                    airCondition.decrementLock();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "D").start();
        ShareData shareData = new ShareData();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(400);
                    shareData.print5();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(400);
                    shareData.print10();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(400);
                    shareData.print15();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

    }


}
