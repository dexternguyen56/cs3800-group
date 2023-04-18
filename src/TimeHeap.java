import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class TimeHeap {
    private PriorityQueue<TimeData> priorityQueue = new PriorityQueue<>();

    // Add a LocalDateTime object with associated String data to the synchronized priority queue
    public synchronized void addToQueue(LocalDateTime localDateTime, String data) {
        priorityQueue.add(new TimeData(localDateTime, data));
    }

    // Remove and return the highest-priority LocalDateTime object from the synchronized priority queue
    public synchronized TimeData removeFromQueue() {
        return priorityQueue.poll();
    }

    // Check if the synchronized priority queue is empty
    public synchronized boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    // Get the size of the synchronized priority queue
    public synchronized int size() {
        return priorityQueue.size();
    }

    // Other methods for manipulating the synchronized priority queue as needed

    // Internal class to represent an item in the priority queue with LocalDateTime and associated data
    private static class TimeData implements Comparable<TimeData> {
        private LocalDateTime localDateTime;
        private String data;

        public TimeData(LocalDateTime localDateTime, String data) {
            this.localDateTime = localDateTime;
            this.data = data;
        }

        public LocalDateTime getLocalDateTime() {
            return localDateTime;
        }

        public String getData() {
            return data;
        }

        @Override
        public int compareTo(TimeData other) {
            return localDateTime.compareTo(other.getLocalDateTime());
        }
    }
}

//