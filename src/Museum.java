import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Museum {
    private int numOfVisitorsInMuseum = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();

    public void enterMuseum(int numOfVisitors) {
        lock.lock();
        try {
            numOfVisitorsInMuseum += numOfVisitors;
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
