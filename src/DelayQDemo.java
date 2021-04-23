import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

public class DelayQDemo {
    public static void main(String[] args) {
        BlockingQueue<Visitor> delayQ = new DelayQueue<>();
        Random r = new Random();

        Clock clock = new Clock();
        Thread clockThread = new Thread(clock);
        clockThread.start();

        //producer thread
        new Thread(()->{
            int i = 0;
            while (!Clock.isMuseumClose()) {
//            for(int i = 1; i < 6; i++){
                try {
//                    long delay = (long) (r.nextInt(2001) + 1000);
                    long delay = (r.nextInt(101) + 50) * 1000;
                    delayQ.put(new Visitor("Element" + i, delay));

                    long nextWindow = (r.nextInt(6) + 5) * 1000;
//                    Thread.sleep(50);
                    Thread.sleep(nextWindow);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                i++;
            }
        }).start();

        //consumer thread
        new Thread(()->{
            while (!Clock.isMuseumClose()) {
//            for(int i = 0; i < 5; i++){
                try {
                    System.out.println(" Consumer got - " + delayQ.take().toString());
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
