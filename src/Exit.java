import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Exit implements Runnable {
    private BlockingQueue<Visitor> visitorQ;
//    private ArrayBlockingQueue<Visitor> exitQ = new ArrayBlockingQueue<>(900);
    private ArrayBlockingQueue<Visitor> exitQ;
    private Museum museum;
    Random r = new Random();

    public Exit(BlockingQueue<Visitor> visitorQ, Museum museum, ArrayBlockingQueue<Visitor> exitQ) {
        this.visitorQ = visitorQ;
        this.museum = museum;
        this.exitQ = exitQ;
    }

    @Override
    public void run() {

        while (!(Clock.isMuseumClose())) {
            Visitor visitor;
            while ((visitor = visitorQ.poll()) != null) {
                try {
                    exitQ.put(visitor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!exitQ.isEmpty()) {
                museum.leaveMuseum(exitQ);
            }

            long nextTicket = (long) (System.nanoTime() + 1000000000L);

            while (System.nanoTime() < nextTicket) {
                //wait
            }
        }
    }
}
