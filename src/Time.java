import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Time implements Comparable {

    private LocalDateTime time;
    private String data;

    public Time(LocalDateTime time, String data){
        this.time = time;
        this.data = data;
    }

    @Override
    public int compareTo(Object o) {
        int difference = (this.time).compareTo(o.time);
        if (difference > 0) {
 
            // current object is greater
            return 1;
        }
        else if (diff < 0) {
 
            // compared object is greater
            return -1;
        }
        else {
 
            // objects are equal
            return 0;
        }
    }

}
