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
    private Condition notEmpty = lock.newCondition();
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
//                    System.out.println("South entrance: " + sCount + ", North entrance: " + nCount);
//                    System.out.println("Entrance now: " + entrance);
//                    System.out.println("Break");
                    break;
                }

                if (entrance.equalsIgnoreCase("South") && sCount + numOfVisitors <= 4) {
                    sCount += numOfVisitors;
                }
                else if (entrance.equalsIgnoreCase("North") && nCount + numOfVisitors <= 4) {
                    nCount += numOfVisitors;
                }
                else {
//                    if (entrance.equalsIgnoreCase("North")) {
//                        System.out.println("North entrance count: " + nCount + ", " + numOfVisitors + " " +
//                                " visitors trying to enter through North entrance. Break...");
//                    }
//                    else if (entrance.equalsIgnoreCase("South")) {
//                        System.out.println("South entrance count: " + sCount + ", " + numOfVisitors + " " +
//                                " visitors trying to enter through South entrance. Break...");
//                    }
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
                int turnstileNum = (entrance.equalsIgnoreCase("South")) ? (sCount - numOfVisitors) :
                        (nCount - numOfVisitors);

                long duration = Long.parseLong(ticInfo[2]);
                if (!(Clock.exceedCloseTime(duration))) {
                    visitorQ.put(new Visitor(ticInfo, exit, turnstileNum));
                }
                else {
                    ticInfo[2] = String.valueOf(Clock.getRemainingDuration());
                    visitorQ.put(new Visitor(ticInfo, exit, turnstileNum));
                }

                notEmpty.signalAll();
                numOfVisitorsInMuseum += numOfVisitors;
                System.out.println("Number of visitors in the museum: " + numOfVisitorsInMuseum);
            }

//            System.out.println("End of 1 entering. Number of visitors in the museum: " + numOfVisitorsInMuseum);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void leaveMuseum(BlockingQueue<Visitor> visitorQ) {
        lock.lock();

        try {
            while (numOfVisitorsInMuseum == 0) {
            System.out.println("No visitor in the museum. Waiting...");

            boolean awaken = notEmpty.await(3000, TimeUnit.MILLISECONDS);

            if (!awaken) {
                System.out.println("No visitor enters. Timeout...");
                break;
            }
        }
            Visitor visitor;
            int eCount = 0;
            int wCount = 0;

            // peek will return unexpired item in delay queue too
            while (((visitor = visitorQ.peek()) != null)) {
                String ticketID = visitor.getTicketID();
//                System.out.println("Check exit: " + ticketID);
                int numOfVisitors = visitor.getNumOfVisitors();
                String exit = visitor.getExit();


                if ((eCount >= 4 && exit.equalsIgnoreCase("East")) || (wCount >= 4 && exit.equalsIgnoreCase("West"))) {
//                    System.out.println("East exit: " + eCount + ", West exit: " + wCount);
//                    System.out.println("Exit now: " + exit);
//                    System.out.println("Break");
                    break;
                }

                if ((exit.equalsIgnoreCase("East") && eCount + numOfVisitors <= 4) || (exit.equalsIgnoreCase("West") && wCount + numOfVisitors <= 4)) {
                    String[] ticketIDs = ticketID.split(", ");

                    String turnstile;
                    int turnstileNum;

                    if (exit.equalsIgnoreCase("East")) {
                        turnstile = "EET";
                        turnstileNum = eCount;
                        eCount += numOfVisitors;
                    }
                    else {
                        turnstile = "WET";
                        turnstileNum = wCount;
                        wCount += numOfVisitors;
                    }

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < ticketIDs.length; i++) {
                        if (i < ticketIDs.length - 1) {
                            sb.append(String.format("Ticket %s exited through Turnstile %s%d.\n", ticketIDs[i],
                                    turnstile,
                                    (turnstileNum + i + 1)));
                        }
                        else {
                            sb.append(String.format("Ticket %s exited through Turnstile %s%d.", ticketIDs[i], turnstile,
                                    (turnstileNum + i + 1)));
                        }
                    }
                    System.out.println(sb);
//                    System.out.println(visitor.getExpireTime());
                    notFull.signalAll();
                }
                else {
//                    if (exit.equalsIgnoreCase("East")) {
//                        System.out.println("East exit count: " + eCount + ", " + numOfVisitors + " " +
//                                " visitors trying to exit through East exit. Break...");
//                    }
//                    else if (exit.equalsIgnoreCase("West")) {
//                        System.out.println("West exit count: " + wCount + ", " + numOfVisitors + " " +
//                                " visitors trying to exit through West exit. Break...");
//                    }
                    break;
                }
                visitorQ.poll();
                numOfVisitorsInMuseum -= numOfVisitors;
                System.out.println("Number of visitors in the museum: " + numOfVisitorsInMuseum);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

//    public void leaveMuseum(String ticketID, int numOfVisitors, String exit) {
//        lock.lock();
//        try {
//            numOfVisitorsInMuseum -= numOfVisitors;
//            System.out.println("Tickets " + ticketID + " exited through " + exit + " exit.");
//            notFull.signalAll();
//            System.out.println("Number of visitors in the museum: " + numOfVisitorsInMuseum);
//        }
//        finally {
//            lock.unlock();
//        }
//    }
}
