import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class Clock implements Runnable {
    private static volatile Calendar currentTime = Calendar.getInstance();
    private static volatile Calendar entranceOpenTime = Calendar.getInstance();
    private static volatile Calendar ticketEndSalesTime = Calendar.getInstance();
    private static volatile Calendar lastEnterTime = Calendar.getInstance();
    private static volatile Calendar clearVisitorsTime = Calendar.getInstance();
    private static volatile Calendar museumCloseTime = Calendar.getInstance();
    private static volatile Instant clearVisitorsTimeInstant;
    private static volatile Instant museumCloseTimeInstant;
    private static volatile boolean isMuseumClose = false;
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");


    public static boolean exceedCloseTime (long delay) {
        int delayInMinutes = (int) (delay/1000);
        Instant endTime = currentTime.toInstant().plus(delayInMinutes, ChronoUnit.MINUTES);

//        return endTime.isAfter(museumCloseTimeInstant);
        return endTime.isAfter(clearVisitorsTimeInstant);
    }

    public static long getRemainingDuration() {
//        Duration res = Duration.between(currentTime.toInstant(), museumCloseTimeInstant);
        Duration res = Duration.between(currentTime.toInstant(), clearVisitorsTimeInstant);
        return (res.toMinutes() * 1000);
    }

    public static Calendar getCurrentTime() {
        return currentTime;
    }

    public static Calendar getEntranceOpenTime() {
        return entranceOpenTime;
    }

    public static Calendar getTicketEndSalesTime() {
        return ticketEndSalesTime;
    }

    public static Calendar getLastEnterTime() {
        return lastEnterTime;
    }

    public static void setLastEnterTime() {
        // lastEnterTime = clearVisitorsTime - 50 minutes
//        lastEnterTime.set(2022, Calendar.MAY,1,14,5, 0);
        lastEnterTime.set(2022, Calendar.MAY,1,17,5, 0);
    }

    public static void setClearVisitorsTime() {
        clearVisitorsTime.set(2022, Calendar.MAY,1,17,55, 0);
        clearVisitorsTimeInstant = clearVisitorsTime.toInstant();
    }

    public static void setTicketEndSalesTime() {
        // ticketEndSalesTime = museumCloseTime - 1 hour
        ticketEndSalesTime.set(2022, Calendar.MAY,1,17,0, 0);
    }

    public static void setMuseumCloseTime() {
        museumCloseTime.set(2022, Calendar.MAY,1,18,0,0);
        museumCloseTimeInstant = museumCloseTime.toInstant();
    }

    public static void setEntranceOpenTime() {
        entranceOpenTime.set(2022, Calendar.MAY,1,8,59, 0);
    }

    public static boolean isMuseumClose() {
        return isMuseumClose;
    }

    public static void startClock() {

        currentTime.set(2022, Calendar.MAY,1,8,0, 0);


//        String startTime = timeFormat.format(currentTime.getTime());
//        System.out.println(currentTime.getTime());
        System.out.println(timeFormat.format(currentTime.getTime()));
    }

    @Override
    public void run() {
        startClock();
        setEntranceOpenTime();
        setTicketEndSalesTime();
        setLastEnterTime();
        setClearVisitorsTime();
        setMuseumCloseTime();

        while (currentTime.before(museumCloseTime)) {
            long end = System.nanoTime() + 1000000000;

            while (System.nanoTime() < end) {
                //wait
            }
            currentTime.add(Calendar.MINUTE, 1);
            System.out.println("\n" + timeFormat.format(currentTime.getTime()));
//            System.out.println(currentTime.getTime());




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

//    public static void main(String[] args) {
//        Instant instant1
//                = Instant.parse("2018-12-30T19:34:00.63Z");
//
//        // create other Instant
//        Instant instant2
//                = Instant.parse("2018-12-30T19:24:00.63Z");
//
//        // print instances
//        System.out.println("Instance 1: " + instant1);
//        System.out.println("Instance 2: " + instant2);
//
//        // check if instant1 is after instant2
//        // using isAfter()
//        boolean value = instant1.isAfter(instant2);
//
//        // print result
//        System.out.println("Is Instant1 after Instant2: "
//                + value);
//
//        Duration res = Duration.between(instant1, instant2);
//        System.out.println(res.toMinutes());
//    }
}
