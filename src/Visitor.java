import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Visitor implements Delayed {
    private String ticketID;
    private String entrance;
    private String exit;
    private int numOfVisitors;
    private long startTime;
    private long expireTime;
    private int turnstileNum;

    public Visitor(String[] ticInfo, String exit, int turnstileNum) {
//        System.out.println(""  + ticketID + " expiry " + this.expireTime + " duration " + delay);

        this.ticketID = ticInfo[0];
        this.numOfVisitors = Integer.parseInt(ticInfo[1]);
        long stayDuration = Long.parseLong(ticInfo[2]);
        this.startTime = System.currentTimeMillis();
        this.expireTime = startTime + stayDuration;
        this.entrance = ticInfo[3];
        this.exit = exit;
        this.turnstileNum = turnstileNum;

        String[] ticketIDs = ticketID.split(", ");

        StringBuilder sb = new StringBuilder();
        String turnstile = (entrance.equalsIgnoreCase("South")) ? "SET" : "NET";
        int stayDurationInMinutes = (int) (stayDuration/1000);

        for (int i = 0; i < ticketIDs.length; i++) {
            if (i < ticketIDs.length - 1) {
                sb.append(String.format("Ticket %s entered through Turnstile %s%d. Staying for %d minutes.\n",
                        ticketIDs[i],
                        turnstile, (turnstileNum + i + 1), stayDurationInMinutes));
            }
            else {
                sb.append(String.format("Ticket %s entered through Turnstile %s%d. Staying for %d minutes.",
                        ticketIDs[i],
                        turnstile, (turnstileNum + i + 1), stayDurationInMinutes));
            }
        }
        System.out.println(sb);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = expireTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {

        if ((this.expireTime < ((Visitor) o).expireTime)) {
            return -1;
        }
        else if ((this.expireTime > ((Visitor) o).expireTime)) {
            return 1;
        }
        else {
            if ((this.startTime < ((Visitor) o).startTime)) {
                return -1;
            }
            else if ((this.startTime > ((Visitor) o).startTime)) {
                return 1;
            }
            else return 0;
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

    public long getExpireTime() {
        return expireTime;
    }
}
