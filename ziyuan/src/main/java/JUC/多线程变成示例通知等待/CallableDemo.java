package JUC.多线程变成示例通知等待;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 有返回值的线程使用Callable
 */
class CallableThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println(" come in call");
        return 1024;
    }
}

/**
 * 调用可以使用子类
 * new Thread(futureTask, "A").start();
 * 因为FutureTask也实现了Runnable接口
 */
public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new CallableThread());
        new Thread(futureTask, "A").start();
        Integer integer = futureTask.get();
        System.out.println(integer);
    }


}
