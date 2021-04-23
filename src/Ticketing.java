import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Ticketing implements Runnable {
    private ArrayBlockingQueue<String[]> ticketQ;
    private Random r = new Random();

    public Ticketing(ArrayBlockingQueue<String[]> ticketQ) {
        this.ticketQ = ticketQ;
    }

    @Override
    public void run() {
        int i = 1;

        while (Clock.getCurrentTime().before(Clock.getTicketEndSalesTime())) {
            int numOfTickets = r.nextInt(4) + 1;

            String ticketID = "";

            for (int j = 1; j <= numOfTickets ; j++) {
                ticketID += "T" + j ;
                if (j < numOfTickets) {
                    ticketID += ", ";
                }
            }

            long stayDuration = (r.nextInt(101) + 50) * 1000;

            int choose = r.nextInt(2);
            String entrance = (choose == 0) ? "North" : "South";

            String[] ticInfo = new String[4];

            ticInfo[0] = ticketID;
            ticInfo[1] = String.valueOf(numOfTickets);
            ticInfo[2] = String.valueOf(stayDuration);
            ticInfo[3] = entrance;

            try {
                ticketQ.put(ticInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long nextTic = System.nanoTime() + (r.nextInt(4) + 1) * 1000000000;

            while (System.nanoTime() < nextTic) {
                //wait
            }

        }
    }
}
