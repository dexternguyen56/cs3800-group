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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

}
