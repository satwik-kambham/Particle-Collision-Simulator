import java.util.Random;

public class MinPQ {
    private Event arr[]; // array holding the priority queue
    private int n; // number of elements in the queue

    // Constructor
    MinPQ(Event a[]) {
        arr = a;
        n = a.length;
        arrayToPQ(a);
    }

    // Doubles the size of the array
    private void doubleSize() {
        Event newArr[] = new Event[arr.length * 2];
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = arr[i];
        }
        arr = newArr;
    }

    // Converts array to PQ
    private void arrayToPQ(Event a[]) {
        for (int i = n / 2; i > 0; i--) {
            sink(i);
        }

        for (int i = 0; i < n; i++) {
            System.out.print(arr[i].timestamp + " ");
        }
        System.out.println("\n----------------");
    }

    // Sinks the element to the correct position
    private void sink(int i) {
        // stop if it is a leaf node
        if (i > n / 2)
            return;

        // check which of the children nodes is smaller
        int j = 2 * i;
        if (j < n && (arr[j - 1].timestamp > arr[j].timestamp))
            j++;

        // stop if it is in the right position
        if (arr[i - 1].timestamp < arr[j - 1].timestamp)
            return;

        // swap the parent node with the smaller child node
        Event event = arr[i - 1];
        arr[i - 1] = arr[j - 1];
        arr[j - 1] = event;

        // check if the node is in right position again
        sink(j);
    }

    // Add event to the PQ
    public void addEvent(Event event) {
        if (arr.length <= ++n) {
            doubleSize();
        }

    }

    // Get the event with min key
    public Event getNextEvent() throws Exception {
        n--;
        if (n == -1)
            throw new Exception("No more events");

        Event event = arr[0];
        arr[0] = arr[n];
        arr[n] = event;

        sink(1);

        return arr[n];
    }

    // Check when the next event will happen
    public int getTimeToNextEvent() {
        return arr[0].timestamp;
    }

    public static void tests() throws Exception {
        Random random = new Random();
        int timestamps[] = new int[random.nextInt(20) + 1];
        for (int i = 0; i < timestamps.length; i++) {
            timestamps[i] = random.nextInt(50);
        }
        Event a[] = new Event[timestamps.length];

        for (int i = 0; i < a.length; i++) {
            a[i] = new Event(timestamps[i]);
        }

        MinPQ pq = new MinPQ(a);
        int x = pq.getNextEvent().timestamp, y;
        for (int i = 1; i < a.length; i++) {
            System.out.print(x + " ");
            y = x;
            x = pq.getNextEvent().timestamp;
            if (y > x) {
                throw new Exception("Incorrect");
            }
        }
    }

    // Main function to test PQ
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            tests();
        }
    }
}
