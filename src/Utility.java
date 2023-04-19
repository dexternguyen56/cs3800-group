import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {

    public static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();

        return now.toString();
    }

    public static LocalDateTime stringToLocalDateTime(String dateTimeString ){

        // Parse the string into a LocalDateTime object
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString);
        return localDateTime;
    }


    public static String formmatPayload(String tag, String msg, String time) {
        String[] response = { tag, msg, time };
        return String.join(",", response);
    
    }

    
    public static String formatTime(LocalDateTime time) {
       
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "[hh:mm:ss.SSS a]"
        );

        return "["+ time.format(formatter) +"] ";
    }



    
    
}
