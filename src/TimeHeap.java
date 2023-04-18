import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class TimeHeap {
    private PriorityQueue<TimeData> priorityQueue = new PriorityQueue<>();

    // Add a LocalDateTime object with associated String data to the synchronized priority queue
    public synchronized void addToQueue(LocalDateTime localDateTime, String data) {
        priorityQueue.add(new TimeData(localDateTime, data));
    }

    public synchronized String removeFromQueue() {
        TimeData timeData = priorityQueue.poll();
        if (timeData != null) {
            return timeData.getData();
        } else {
            return null; 
        }
    }

    // Check if the synchronized priority queue is empty
    public synchronized boolean isEmpty() {
        return priorityQueue.isEmpty();
    }

    // Get the size of the synchronized priority queue
    public synchronized int size() {
        return priorityQueue.size();
    }

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
            return this.localDateTime.compareTo(other.getLocalDateTime()); // Compare based on LocalDateTime
        }
    }
}
