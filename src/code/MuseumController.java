package code;

import controller.MuseumSceneController;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class MuseumController {
    public static ArrayBlockingQueue<String[]> ticketQ = new ArrayBlockingQueue<>(900);
    public static volatile BlockingQueue<Visitor> visitorQ = new DelayQueue<>();
    public static volatile ArrayBlockingQueue<Visitor> exitQ = new ArrayBlockingQueue<>(900);
    static String[] options = {"1. Execute random scenario", "2. Execute test case 'Exceed 100 visitors at one time'", "3. Execute test case 'Exceed 900 tickets'", "4. Execute test case 'Exceed 6:00 p.m.'"};

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String userOption;

        do {
            System.out.println("\nOptions: ");
            for (String o : options) {
                System.out.println(o);
            }
            System.out.print("Your option: ");

            userOption = scanner.nextLine().trim();

        } while (!(userOption.equals("1") || userOption.equals("2") || userOption.equals("3") || userOption.equals("4")));

        System.out.println();

        redirectToSelectedOption(userOption);

    }

    public static boolean allVisitorsLeft() {
        return exitQ.isEmpty();
    }

    public static void redirectToSelectedOption(String userOption) {
        if (userOption.equals("1")) {
//            ExecutorService service = Executors.newCachedThreadPool();
//            Clock clock = new Clock();
//            Future<String> clockThread = service.submit(clock);
//            service.shutdown();
            Clock clock = new Clock();
            Thread clockThread = new Thread(clock);
            clockThread.start();

            Thread randomTicketingThread = new Thread(new RandomTicketing(ticketQ));
            randomTicketingThread.start();

            Museum museum = new Museum();
            Thread entranceThread = new Thread(new Entrance(ticketQ, museum, visitorQ));

            while (Clock.getCurrentTime().before(Clock.getEntranceOpenTime())) {
                //wait
            }

            System.out.println(printEntranceOpenMsg());
            MuseumSceneController.messageHere_str += printEntranceOpenMsg();
            entranceThread.start();

            Thread exitThread = new Thread(new Exit(visitorQ, museum, exitQ));
            exitThread.start();
//            service.shutdown();
        } else {
            String fileName = "";

            switch (userOption) {
                case "2":
                    fileName += "Exceed100Visitors.txt";
                    break;
                case "3":
                    fileName += "Exceed900Tickets.txt";
                    break;
                case "4":
                    fileName += "Exceed6pm.txt";
                    break;
            }
//            System.out.println(fileName);

            TextReader tr = new TextReader(fileName);
            ArrayList<String[]> allPurchases = tr.readTextFile();

            Clock clock = new Clock();
            Thread clockThread = new Thread(clock);
            clockThread.start();
//            ExecutorService service = Executors.newCachedThreadPool();
//            Clock clock = new Clock();
//            Future<String> clockThread = service.submit(clock);

            Thread ticketingThread = new Thread(new Ticketing(allPurchases, ticketQ));
            ticketingThread.start();

            Museum museum = new Museum();
            Thread entranceThread = new Thread(new Entrance(ticketQ, museum, visitorQ));

            while (Clock.getCurrentTime().before(Clock.getEntranceOpenTime())) {
                //wait
            }

            System.out.println(printEntranceOpenMsg());
            entranceThread.start();


            Thread exitThread = new Thread(new Exit(visitorQ, museum, exitQ));
            exitThread.start();
//            service.shutdown();
        }
    }

    public static String getOptions(int userOpt) {
        return options[userOpt-1];
    }

    public static String printEntranceOpenMsg(){
        return "Entrance opens...";
    }
}

