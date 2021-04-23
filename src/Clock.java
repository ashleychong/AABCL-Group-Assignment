import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Clock implements Runnable {
    private static volatile Calendar currentTime = Calendar.getInstance();
    private static volatile Calendar ticketEndSalesTime = Calendar.getInstance();
    private static volatile Calendar museumCloseTime = Calendar.getInstance();
    private static volatile boolean isMuseumClose = false;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

    public static Calendar getCurrentTime() {
        return currentTime;
    }

    public static Calendar getTicketEndSalesTime() {
        return ticketEndSalesTime;
    }

    public static void setTicketEndSalesTime() {
//        ticketEndSalesTime.set(Calendar.HOUR_OF_DAY, 17);
//        ticketEndSalesTime.set(Calendar.MINUTE, 0);
//        ticketEndSalesTime.set(Calendar.SECOND, 0);
        ticketEndSalesTime.set(2022, Calendar.MAY,1,17,0);
    }

    public static void setMuseumCloseTime() {
//        museumCloseTime.set(Calendar.HOUR_OF_DAY, 18);
//        museumCloseTime.set(Calendar.MINUTE, 0);
//        museumCloseTime.set(Calendar.SECOND, 0);
        museumCloseTime.set(2022, Calendar.MAY,1,15,0);
    }

    public static boolean isMuseumClose() {
        return isMuseumClose;
    }

    public static void startClock() {

//        currentTime.set(Calendar.HOUR_OF_DAY, 8);
//        currentTime.set(Calendar.MINUTE, 0);
//        currentTime.set(Calendar.SECOND, 0);

        currentTime.set(2022, Calendar.MAY,1,7,58, 0);


        String startTime = timeFormat.format(currentTime.getTime());
        System.out.println(currentTime.getTime());
    }

    @Override
    public void run() {
        startClock();
        setTicketEndSalesTime();
        setMuseumCloseTime();

        while (currentTime.before(museumCloseTime)) {
            long end = System.nanoTime() + 1000000000;

            while (System.nanoTime() < end) {
                //wait
            }
            currentTime.add(Calendar.MINUTE, 1);
            System.out.println(currentTime.getTime());




//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            finally {
//                currentTime.add(Calendar.MINUTE, 1);
////                System.out.println(timeFormat.format(currentTime.getTime()));
//                System.out.println(currentTime.getTime());
//            }
        }
        isMuseumClose = true;
    }
}
