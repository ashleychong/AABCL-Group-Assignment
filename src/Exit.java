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


        // original
//        while (!(Clock.isMuseumClose())) {
//            Visitor visitor;
//            if ((visitor = visitorQ.poll()) != null) {
//                String ticketID = visitor.getTicketID();
//                int numOfVisitors = visitor.getNumOfVisitors();
//                String exit = visitor.getExit();
//
//                museum.leaveMuseum(ticketID, numOfVisitors, exit);
//
////                System.out.println("Tickets " + ticketID + " exited through " + exit + " exit.");
//            }
//        }






//        while (!(Clock.isMuseumClose())) {
//            Visitor visitor;
//            if ((visitor = visitorQ.poll()) != null) {
//                String ticketID = visitor.getTicketID();
//                int numOfVisitors = visitor.getNumOfVisitors();
//                String exit = visitor.getExit();
//
//                museum.leaveMuseum(visitorQ);
//
//                System.out.println("Tickets " + ticketID + " exited through " + exit + " exit.");
//            }
//        }


//        while (!(Clock.isMuseumClose())) {
//            museum.leaveMuseum(visitorQ);
//
//            long nextTicket = (long) (System.nanoTime() + (r.nextInt(3) + 1) * 1000000000L);
//
//            while (System.nanoTime() < nextTicket) {
//                //wait
//            }
//        }

        while (!(Clock.isMuseumClose())) {
            Visitor visitor;
            while ((visitor = visitorQ.poll()) != null) {
                try {
                    exitQ.put(visitor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                String ticketID = visitor.getTicketID();
//                int numOfVisitors = visitor.getNumOfVisitors();
//                String exit = visitor.getExit();
//
//                museum.leaveMuseum(visitorQ);
//
//                System.out.println("Tickets " + ticketID + " exited through " + exit + " exit.");
            }
            if (!exitQ.isEmpty()) {
                museum.leaveMuseum(exitQ);
            }

            long nextTicket = (long) (System.nanoTime() + 1000000000L);
//            long nextTicket = (long) (System.nanoTime() + (r.nextInt(2) + 1) * 1000000000L);

            while (System.nanoTime() < nextTicket) {
                //wait
            }
        }
    }
}
