package code;

import java.text.ParseException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Entrance implements Runnable {
    private ArrayBlockingQueue<String[]> ticketQ;
    private BlockingQueue<Visitor> visitorQ;
    private Museum museum;
    private Random r = new Random();

    public Entrance(ArrayBlockingQueue<String[]> ticketQ, Museum museum, BlockingQueue<Visitor> visitorQ) {
        this.ticketQ = ticketQ;
        this.museum = museum;
        this.visitorQ = visitorQ;
    }

    @Override
    public void run() {
        while (Clock.getCurrentTime().before(Clock.getLastEnterTime())) {

            museum.enterMuseum(ticketQ, visitorQ);

            long nextTicket = (long) (System.nanoTime() + 1000000000L);

            while (System.nanoTime() < nextTicket) {
                //wait
            }
        }
    }
}
