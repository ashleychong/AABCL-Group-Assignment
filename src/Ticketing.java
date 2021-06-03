import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Ticketing implements Runnable {
    private ArrayList<String[]> allPurchases;
    private ArrayBlockingQueue<String[]> ticketQ;
    private final int maxTicketsSold = 900;
    private Random r = new Random();

    public Ticketing(ArrayList<String[]> allPurchases, ArrayBlockingQueue<String[]> ticketQ) {
        this.allPurchases = allPurchases;
        this.ticketQ = ticketQ;
    }


    @Override
    public void run() {
        int ticketID = 1;

        System.out.println("Ticketing starts...");


        while (Clock.getCurrentTime().before(Clock.getTicketEndSalesTime()) && !(allPurchases.isEmpty())) {

            if (ticketID > maxTicketsSold)  {
                System.out.println("Max ticket limit of 900 has been reached. Transaction declined.");
                break;
            }

            String[] curTickets = allPurchases.get(0);

            //minutesToNextPurchase, numOfTickets, Duration
            int minutesToNextPurchase = Integer.parseInt(curTickets[0]);
            int numOfTickets = Integer.parseInt(curTickets[1]);
            long stayDuration = Integer.parseInt(curTickets[2]) * 1000L;


            long nextTicket = (long) (System.nanoTime() + (minutesToNextPurchase * 1000000000L));

            while (System.nanoTime() < nextTicket) {
                //wait
            }


            StringBuilder ticketIDs = new StringBuilder();

            for (int i = 0; i < numOfTickets; i++) {
                if (Math.log10(ticketID) < 1) {
                    ticketIDs.append("T000");
                    ticketIDs.append(ticketID);
                }
                else if (Math.log10(ticketID) < 2) {
                    ticketIDs.append("T00");
                    ticketIDs.append(ticketID);
                }
                else if (Math.log10(ticketID) < 3) {
                    ticketIDs.append("T0");
                    ticketIDs.append(ticketID);
                }

                if (i < numOfTickets - 1) {
                    ticketIDs.append(", ");
                }
                ticketID++;
            }


            int choose = r.nextInt(2);
            String entrance = (choose == 0) ? "North" : "South";

            String[] ticInfo = new String[4];

            ticInfo[0] = ticketIDs.toString();
            ticInfo[1] = String.valueOf(numOfTickets);
            ticInfo[2] = String.valueOf(stayDuration);
            ticInfo[3] = entrance;

            try {
                ticketQ.put(ticInfo);

                if (numOfTickets == 1) {
                    System.out.println("Ticket " + ticketIDs + " sold.");
                }
                else {
                    System.out.println("Tickets " + ticketIDs + " sold.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            allPurchases.remove(0);
        }
    }
}
