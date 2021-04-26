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
        while (!(Clock.isMuseumClose())) {

            museum.enterMuseum(ticketQ, visitorQ);

            long nextTicket = (long) (System.nanoTime() + (r.nextInt(4) + 1) * 1000000000L);

            while (System.nanoTime() < nextTicket) {
                //wait
            }

//            String[] ticInfo;
//            if ((ticInfo = ticketQ.poll()) != null) {
//                museum.enterMuseum(ticInfo[1]);
//
//                int choose = r.nextInt(2);
//                String exit = (choose == 0) ? "East" : "West";
//
//                long delay = (r.nextInt(101) + 50) * 1000;
//
//                try {
//                    visitorQ.put(new Visitor(ticInfo, exit));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
