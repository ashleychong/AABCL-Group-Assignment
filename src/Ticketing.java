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

        System.out.println("Ticketing starts...");
//        System.out.println("Current time: " + Clock.getCurrentTime().getTime());
//        System.out.println("Tickets ends sale: " + Clock.getTicketEndSalesTime().getTime());
//        System.out.println(Clock.getCurrentTime().before(Clock.getTicketEndSalesTime()));

        while (Clock.getCurrentTime().before(Clock.getTicketEndSalesTime())) {
//            System.out.println("Inside loop");
            int numOfTickets = r.nextInt(4) + 1;

            String ticketID = "";

            for (int j = 1; j <= numOfTickets ; j++) {
                ticketID += "T" + i;
                if (j < numOfTickets) {
                    ticketID += ", ";
                }
                i++;
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
                System.out.println(ticketID + " sold.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long nextTicket = (long) (System.nanoTime() + (r.nextInt(4) + 1) * 1000000000L);

            while (System.nanoTime() < nextTicket) {
                //wait
            }

        }
    }
}
