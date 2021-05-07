import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class MuseumController {
    private static ArrayBlockingQueue<String[]> ticketQ = new ArrayBlockingQueue<>(900);
    private static volatile BlockingQueue<Visitor> visitorQ = new DelayQueue<>();
    private static volatile ArrayBlockingQueue<Visitor> exitQ = new ArrayBlockingQueue<>(900);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String userOption;

        do {
            System.out.println("\nOptions: ");
            System.out.println("1. Execute random scenario");
            System.out.println("2. Execute test case 'Exceed 100 visitors at one time'");
            System.out.println("3. Execute test case 'Exceed 900 tickets'");
            System.out.println("4. Execute test case 'Exceed 6:00 p.m.'");
            System.out.print("Your option: ");

            userOption = scanner.nextLine().trim();

        } while (!(userOption.equals("1") || userOption.equals("2") || userOption.equals("3") || userOption.equals("4")));

        System.out.println();

        if (userOption.equals("1")) {
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

            System.out.println("Entrance opens...");
            entranceThread.start();

            Thread exitThread = new Thread(new Exit(visitorQ, museum, exitQ));
            exitThread.start();
        }
        else {
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

            Thread ticketingThread = new Thread(new Ticketing(allPurchases, ticketQ));
            ticketingThread.start();

            Museum museum = new Museum();
            Thread entranceThread = new Thread(new Entrance(ticketQ, museum, visitorQ));

            while (Clock.getCurrentTime().before(Clock.getEntranceOpenTime())) {
                //wait
            }

            System.out.println("Entrance opens");
            entranceThread.start();


            Thread exitThread = new Thread(new Exit(visitorQ, museum, exitQ));
            exitThread.start();
        }
    }

    public static boolean allVisitorsLeft() {
        return exitQ.isEmpty();
    }
}

