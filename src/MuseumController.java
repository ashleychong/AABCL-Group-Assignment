import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class MuseumController {
    private final static int maxTickets = 900;
    private static int ticketsSold = 0;

    public static void main(String[] args) {

        // read TicketInfo
        // 8am: start to random assign the ticket to south or north entrance (put() into blockingqueue)

        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

        Clock clock = new Clock();
        Thread clockThread = new Thread(clock);
        clockThread.start();

        ArrayBlockingQueue<String[]> ticketQ = new ArrayBlockingQueue<>(900);
        Thread ticketingThread = new Thread(new Ticketing(ticketQ));
        ticketingThread.start();

        BlockingQueue<Visitor> visitorQ = new DelayQueue<>();
        Museum museum = new Museum();
        Thread entranceThread = new Thread(new Entrance(ticketQ, museum, visitorQ));

        while (Clock.getCurrentTime().before(Clock.getEntranceOpenTime())) {
            //wait
        }

        System.out.println("Entrance open");
        entranceThread.start();

        Thread exitThread = new Thread(new Exit(visitorQ, museum));
        exitThread.start();













//        System.out.println("Millis: " + System.currentTimeMillis());
//        Calendar calendar = clock.getCurrentTime();
//        System.out.println("Time is " + timeFormat.format(calendar.getTime()));

//        Runnable printTime = () -> {
//            System.out.println("Time is " + timeFormat.format(calendar.getTime()));
//        };
//
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.schedule(printTime, 10*1000, TimeUnit.MILLISECONDS);
//
//        scheduler.schedule(printTime, 12*1000, TimeUnit.MILLISECONDS);
//
//        scheduler.schedule(printTime, 15*1000, TimeUnit.MILLISECONDS);
//
//        scheduler.schedule(printTime, 60*1000, TimeUnit.MILLISECONDS);
//
//        scheduler.schedule(printTime, 100*1000, TimeUnit.MILLISECONDS);
//
//        scheduler.schedule(printTime, 150*1000, TimeUnit.MILLISECONDS);

//        try {
//            Thread.sleep(500*10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 10);
//        calendar.set(Calendar.MINUTE, 1);
//        calendar.set(Calendar.SECOND, 0);


//        String timestamp = timeFormat.format(calendar.getTime());
//
//        MyTimerTask mtt = new MyTimerTask(timestamp);
//        Timer timer = new Timer();
//
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = dateFormatter.parse("2022-04-01 10:01:00");
//            timer.schedule(mtt, date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date time = calendar.getTime();
//        System.out.println(time);



//        MyTimerTask mtt = new MyTimerTask(timestamp);
//        Timer timer = new Timer();
//        timer.schedule(mtt, time);
//        timer.schedule(mtt, date);
    }
}

