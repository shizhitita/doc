package JUC.八锁;

import java.util.concurrent.TimeUnit;

class Phone {

    public synchronized void sendEmail() throws Exception {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("***** sendEmail");
    }

    public synchronized void sendsMS() throws Exception {
        System.out.println("***** sendsMS");
    }

    public void sayHello() throws Exception {
        System.out.println("***** sayHel1o");
    }
}

/**
 * 1:标准访问，请问是先打印邮件还是短信？线程调度不一定
 * 2:暂停4秒在邮件，请问先打印邮件还是短信？
 * 3：新增普通sayHello方法，请问先打印邮件还是hello.
 * 4：两部手机，请问先打印邮件还是短信
 * 5：两个静态同步方法，同一部手机，请问先打印邮件还是短信
 * 6：两个静态同步方法，两部手机，请问先打印邮件还是短信
 * 7：1个静态同步方法，一个普通同步方法，同一个手机，请问先打印邮件还是短信
 * 8：1个静态同步方法，一个普通同步方法，2个手机，请问先打印邮件还是短信
 *
 *
 * *一个对象组面如果有多个synchronized方法，某一个时刻内，只要一个线程去调用其中的一个synchronized方法了，其它的线程都只能等待，换句话说，某一个时刻的，只能有唯一一个线程去访问这些synchronized方法锁的是当前对象this，被锁定后，其它的线程都不能进入到当前对象的其E它的synchronized方法加个普通方法后发现和同步锁无关
 * 换成两个对象后，不是同一把贫了，情况立刺变化。
 * 都换成静态同步方法后，情况又变化
 * 所有的非静态同步方法用的都是同一把续一实例对象本身，synchronized实观同步的基础：Java中的每一个对象都可以作为锁。
 * 具体表现为以3种形式。
 * 对于普通同步方法，锁是当前实倒对象。
 * 对于同步方法块，锁是synchonized括号组配置的对象。
 * 对于静态同步方法，锁是当前类的cLass对象。
 * 当一个线程试图访问同步代码块时，它首先必须得到绩，退出或继出异常时必须释放缆。|I也就是说如果一个实间对象的非静态同步方法获取倒后，该实向对象的其他非静态同步方法必须等待获取颜的方法释放锁后才能获取敛，
 * 可是别的实例对象的非静态同步方法因为跟该实阅对象的非静态同步方法用的是不同的锁，所以好须等待该实对象已获取锁的非静态同步方法释放锁就可以获取他们自己的锁。
 *
 * 所有的静态同步方法用的也是同一把键一类对象本身，这两把锁是两个不同的对象，所以静态同步方法与非静态同步方法之间是不会有意态条件的。
 * 但是一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后才能获取，而不管是同一个实例对象的静态同步方法之间，还是不同的实网对象的静态同步方法之间，只要它们同一个类的实例对象！
 *
 */
public class Lock8Demo {

    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();
        new Thread(() -> {
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();
        Thread.sleep(100);
        new Thread(() -> {
            try {
//                phone.sendEmail();
                phone.sayHello();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "b").start();
    }

}
