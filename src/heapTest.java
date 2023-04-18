import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

public class heapTest {
    public static void main(String[] args) {
        TimeHeap heap = new TimeHeap();

        for (int index = 0; index < 10; index++) {
            heap.addToQueue(LocalDateTime.now(), Integer.toString(index));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        while (!heap.isEmpty()) {
            System.out.println(heap.removeFromQueue());
        }
    }
}
