import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class RandomTicketing implements Runnable {
    private ArrayBlockingQueue<String[]> ticketQ;
    private Random r = new Random();
    private final int maxTicketsSold = 900;

    public RandomTicketing(ArrayBlockingQueue<String[]> ticketQ) {
        this.ticketQ = ticketQ;
    }

    @Override
    public void run() {
        int ticketID = 1;

        System.out.println("Ticketing starts...");
//        System.out.println("Current time: " + Clock.getCurrentTime().getTime());
//        System.out.println("Tickets ends sale: " + Clock.getTicketEndSalesTime().getTime());
//        System.out.println(Clock.getCurrentTime().before(Clock.getTicketEndSalesTime()));

        while (Clock.getCurrentTime().before(Clock.getTicketEndSalesTime())) {
//
            if (ticketID > maxTicketsSold)  {
                System.out.println("Max ticket limit of 900 has been reached. Transaction declined.");
                break;
            }

            int numOfTickets = r.nextInt(4) + 1;

//            String ticketIDs = "";
//
//            for (int j = 1; j <= numOfTickets ; j++) {
//                ticketIDs += "T" + ticketID;
//                if (j < numOfTickets) {
//                    ticketIDs += ", ";
//                }
//                ticketID++;
//            }



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



            long stayDuration = (r.nextInt(101) + 50) * 1000;
//            long stayDuration = (r.nextInt(101) + 50) * 1000000000L;


            int choose = r.nextInt(2);
            String entrance = (choose == 0) ? "North" : "South";

            String[] ticInfo = new String[4];

//            ticInfo[0] = ticketIDs;
            ticInfo[0] = ticketIDs.toString();
            ticInfo[1] = String.valueOf(numOfTickets);
            ticInfo[2] = String.valueOf(stayDuration);
            ticInfo[3] = entrance;

            try {
                ticketQ.put(ticInfo);
//                System.out.println(ticketIDs + " sold.");


                if (numOfTickets == 1) {
                    System.out.println("Ticket " + ticketIDs + " sold.");
                }
                else {
                    System.out.println("Tickets " + ticketIDs + " sold.");
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long nextTicket = (long) (System.nanoTime() + (r.nextInt(2) + 1) * 1000000000L);

            while (System.nanoTime() < nextTicket) {
                //wait
            }

        }
    }
}
