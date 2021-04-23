import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Visitor implements Delayed {
    private String m;
    private long expireTime;

    public Visitor(String m, long delay) {
        this.m = m;
        this.expireTime = System.currentTimeMillis() + delay;
        System.out.println("Putting queueElement "  + m + " expiry " + this.expireTime + " duration " + delay);
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
        return m + " Expiry Time= " + expireTime;
    }
}
