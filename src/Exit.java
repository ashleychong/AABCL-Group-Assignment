import java.util.concurrent.BlockingQueue;

public class Exit implements Runnable {
    private BlockingQueue<Visitor> visitorQ;
    private Museum museum;

    public Exit(BlockingQueue<Visitor> visitorQ, Museum museum) {
        this.visitorQ = visitorQ;
        this.museum = museum;
    }

    @Override
    public void run() {
        while (!(Clock.isMuseumClose())) {
            Visitor visitor;
            if ((visitor = visitorQ.poll()) != null) {
                String ticketID = visitor.getTicketID();
                int numOfVisitors = visitor.getNumOfVisitors();
                String exit = visitor.getExit();

                museum.leaveMuseum(ticketID, numOfVisitors, exit);

//                System.out.println("Tickets " + ticketID + " exited through " + exit + " exit.");
            }
        }
    }
}
