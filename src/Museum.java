import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Museum {
    private int numOfVisitorsInMuseum = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();

    public void enterMuseum(String[] ticInfo) {
        String ticketID = ticInfo[0];
        int numOfVisitors = Integer.parseInt(ticInfo[1]);
        long stayDuration = Long.parseLong(ticInfo[2]);
        String entrance = ticInfo[3];

        lock.lock();
        try {
            numOfVisitorsInMuseum += numOfVisitors;
            System.out.println("Tickets " + ticketID + " entered through " + entrance + ". Staying for " + (stayDuration/1000) + " minutes.");

        }
        finally {
            lock.unlock();
        }
    }

    public void leaveMuseum(int numOfVisitors) {
        lock.lock();
        try {
            numOfVisitorsInMuseum -= numOfVisitors;
        }
        finally {
            lock.unlock();
        }
    }
}
