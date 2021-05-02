import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Museum {
    private int numOfVisitorsInMuseum = 0;
    private final int maxNumOfVisitorsInMuseum = 100;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
//    private ArrayBlockingQueue<String[]> ticketQ;
//    private BlockingQueue<Visitor> visitorQ = new DelayQueue<>();
    private Random r = new Random();


    public void enterMuseum(ArrayBlockingQueue<String[]> ticketQ, BlockingQueue<Visitor> visitorQ) {
        lock.lock();

        try {
            while (numOfVisitorsInMuseum == maxNumOfVisitorsInMuseum) {
                System.out.println("The museum is full now. Waiting...");
//                notFull.await();
                boolean awaken = notFull.await(3000, TimeUnit.MILLISECONDS);

                if (!awaken) {
                    System.out.println("No visitor leaves. Timeout...");
                    break;
                }
            }

            String[] ticInfo;
            int sCount = 0;
            int nCount = 0;

            int allowedNumOfVisitors = maxNumOfVisitorsInMuseum - numOfVisitorsInMuseum;

            outerloop:
            while (((ticInfo = ticketQ.peek()) != null) /* && ((sCount + nCount) < allowedNumOfVisitors)*/) {
                String entrance = ticInfo[3];
                int numOfVisitors = Integer.parseInt(ticInfo[1]);


//                if (numOfVisitorsInMuseum + numOfVisitors > maxNumOfVisitorsInMuseum) {
//                    System.out.println(numOfVisitorsInMuseum + " visitors in museum. " + numOfVisitors + " trying to " +
//                            "enter, break...");
//                    break;
//                }

                if ((sCount >= 4 && entrance.equalsIgnoreCase("South")) || (nCount >= 4 && entrance.equalsIgnoreCase(
                        "North"))) {
                    System.out.println("South entrance: " + sCount + ", North entrance: " + nCount);
                    System.out.println("Entrance now: " + entrance);
                    System.out.println("Break");
                    break;
                }

                if (entrance.equalsIgnoreCase("South") && sCount + numOfVisitors <= 4) {
                    sCount += numOfVisitors;
                }
                else if (entrance.equalsIgnoreCase("North") && nCount + numOfVisitors <= 4) {
                    nCount += numOfVisitors;
                }
                else {
                    if (entrance.equalsIgnoreCase("North")) {
                        System.out.println("North entrance count: " + nCount + ", " + numOfVisitors + " " +
                                " visitors trying to enter through North entrance. Break...");
                    }
                    else if (entrance.equalsIgnoreCase("South")) {
                        System.out.println("South entrance count: " + sCount + ", " + numOfVisitors + " " +
                                " visitors trying to enter through South entrance. Break...");
                    }
                    break;
                }


                while (numOfVisitorsInMuseum + numOfVisitors > maxNumOfVisitorsInMuseum) {
                    System.out.println(numOfVisitorsInMuseum + " visitors in museum. " + numOfVisitors + " visitors" +
                            " trying to " + "enter, waiting...");
//                    notFull.await();
                    boolean awaken = notFull.await(3000, TimeUnit.MILLISECONDS);

                    if (!awaken) {
                        System.out.println("No visitor leaves. Timeout...");
                        break outerloop;
                    }
                }


                ticketQ.poll();

                int choose = r.nextInt(2);
                String exit = (choose == 0) ? "East" : "West";

                long duration = Long.parseLong(ticInfo[2]);
                if (!(Clock.exceedCloseTime(duration))) {
                    visitorQ.put(new Visitor(ticInfo, exit));
                }
                else {
                    ticInfo[2] = String.valueOf(Clock.getRemainingDuration());
                    visitorQ.put(new Visitor(ticInfo, exit));
                }

                numOfVisitorsInMuseum += numOfVisitors;

                System.out.println("South entrance: " + sCount + ", North entrance: " + nCount);
            }

//            numOfVisitorsInMuseum += (sCount + nCount);
            System.out.println("End of 1 entering. Number of visitors in the museum: " + numOfVisitorsInMuseum);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void leaveMuseum(String ticketID, int numOfVisitors, String exit) {
        lock.lock();
        try {
            numOfVisitorsInMuseum -= numOfVisitors;
            System.out.println("Tickets " + ticketID + " exited through " + exit + " exit.");
            notFull.signalAll();
            System.out.println("Number of visitors in the museum: " + numOfVisitorsInMuseum);
        }
        finally {
            lock.unlock();
        }
    }
}
