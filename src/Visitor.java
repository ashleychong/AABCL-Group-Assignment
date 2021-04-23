import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Visitor implements Delayed {
    private String ticketID;
    private String entrance;
    private String exit;
    private int numOfVisitors;
    private long expireTime;

    public Visitor(String[] ticInfo, String exit) {
//        System.out.println(""  + ticketID + " expiry " + this.expireTime + " duration " + delay);

        this.ticketID = ticInfo[0];
        this.numOfVisitors = Integer.parseInt(ticInfo[1]);
        long stayDuration = Long.parseLong(ticInfo[2]);
        this.expireTime = System.currentTimeMillis() + stayDuration;
        this.entrance = ticInfo[3];
        this.exit = exit;

        System.out.println("Tickets " + ticketID + " entered through " + entrance + " entrance . Staying for " + (stayDuration/1000) + " minutes.");
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = expireTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
            return -1;
        }
        else if (this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
            return 1;
        }
        else {
            return 0;
        }

    }

    @Override
    public String toString(){
        return ticketID + " Expiry Time= " + expireTime;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getExit() {
        return exit;
    }

    public int getNumOfVisitors() {
        return numOfVisitors;
    }
}
