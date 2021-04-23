import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Entrance implements Runnable {
    private ArrayBlockingQueue<String[]> ticketQ;
    private Museum museum;
    private BlockingQueue<Visitor> visitorQ;

    public Entrance(ArrayBlockingQueue<String[]> ticketQ, Museum museum, BlockingQueue<Visitor> visitorQ) {
        this.ticketQ = ticketQ;
        this.museum = museum;
        this.visitorQ = visitorQ;
    }

    @Override
    public void run() {
        while (!(Clock.isMuseumClose())) {
            String[] ticInfo;

            if ((ticInfo = ticketQ.poll()) != null) {
                museum.enterMuseum(ticInfo);
//                visitorQ.
            }
        }
    }
}
