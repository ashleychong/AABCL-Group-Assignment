package code;

import controller.MuseumSceneController;
import gui.SingleLeavingVisitor;
import gui.SingleVisitor;

import java.text.ParseException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Museum {
    public static int totalNumOfVisitors = 0;
    public static int numOfVisitorsInMuseum = 0;
    private final int maxNumOfVisitorsInMuseum = 100;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private Random r = new Random();


    public void enterMuseum(ArrayBlockingQueue<String[]> ticketQ, BlockingQueue<Visitor> visitorQ) {
        lock.lock();

        try {
            while (numOfVisitorsInMuseum == maxNumOfVisitorsInMuseum) {
                System.out.println("The museum is full now. Waiting...");
                MuseumSceneController.messageHere_str = "The museum is full now. Waiting...";
                boolean awaken = notFull.await(3000, TimeUnit.MILLISECONDS);

                if (!awaken) {
                    System.out.println("No visitor leaves. Timeout...");
                    MuseumSceneController.messageHere_str = "No visitor leaves. Timeout...";
                    return;
                }
            }

            String[] ticInfo;
            int sCount = 0;
            int nCount = 0;


            outerloop:
            while (((ticInfo = ticketQ.peek()) != null)) {
                String entrance = ticInfo[3];
                int numOfVisitors = Integer.parseInt(ticInfo[1]);

                // if 4 visitors already entered through South entrance and current visitors try to use South
                // entrance again OR
                // 4 visitors already entered through North entrance and current visitors try to use North entrance
                // again
                if ((sCount >= 4 && entrance.equalsIgnoreCase("South")) || (nCount >= 4 && entrance.equalsIgnoreCase(
                        "North"))) {
//                    System.out.println("South entrance: " + sCount + ", North entrance: " + nCount);
//                    System.out.println("code.Entrance now: " + entrance);
//                    System.out.println("Break");
                    break;
                }

                if (entrance.equalsIgnoreCase("South") && sCount + numOfVisitors <= 4) {
                    sCount += numOfVisitors;
                } else if (entrance.equalsIgnoreCase("North") && nCount + numOfVisitors <= 4) {
                    nCount += numOfVisitors;
                } else {
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
                    String waitingQMsg = numOfVisitorsInMuseum + " visitors in the museum. " + numOfVisitors + " " +
                            "visitors" + " trying to " + "enter, waiting...";
                    System.out.println(waitingQMsg);
                    MuseumSceneController.messageHere_str = waitingQMsg;


                    boolean awaken = notFull.await(3000, TimeUnit.MILLISECONDS);

                    if (!awaken) {
                        System.out.println("No visitor leaves. Timeout...");
                        MuseumSceneController.messageHere_str = "No visitor leaves. Timeout...";
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
                } else {
                    ticInfo[2] = String.valueOf(Clock.getRemainingDuration());
                    visitorQ.put(new Visitor(ticInfo, exit, turnstileNum));
                }

                notEmpty.signalAll();
                numOfVisitorsInMuseum += numOfVisitors;
                totalNumOfVisitors += numOfVisitors;
                String visitorNumInMusuemMsg = "Number of visitors in the museum: " + numOfVisitorsInMuseum;
                System.out.println(visitorNumInMusuemMsg);
            }

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
                MuseumSceneController.messageHere_str = "No visitor in the museum. Waiting...";

                boolean awaken = notEmpty.await(3000, TimeUnit.MILLISECONDS);

                if (!awaken) {
                    System.out.println("No visitor enters. Timeout...");
                    MuseumSceneController.messageHere_str = "No visitor enters. Timeout...";
                    return;
                }
            }
            Visitor visitor;
            int eCount = 0;
            int wCount = 0;


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
                    } else {
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
                        } else {
                            sb.append(String.format("Ticket %s exited through Turnstile %s%d.", ticketIDs[i], turnstile,
                                    (turnstileNum + i + 1)));
                        }
                        new SingleLeavingVisitor(ticketIDs[i], exit, turnstile + (turnstileNum + i + 1));
                    }
                    System.out.println(sb);
//                    System.out.println(visitor.getExpireTime());
                    notFull.signalAll();
                } else {
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
                String visitorNumInMuseumMsg = "Number of visitors in the museum: " + numOfVisitorsInMuseum;
                System.out.println(visitorNumInMuseumMsg);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
