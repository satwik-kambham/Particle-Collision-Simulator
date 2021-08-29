import java.util.Random;

public class MinPQ {
    private Event[] arr; // array holding the priority queue
    public int n; // number of elements in the queue

    // Constructor
    MinPQ(Event[] a) {
        arr = a;
        n = a.length;
        arrayToPQ();
    }

    // Constructor
    MinPQ() {
        arr = new Event[10];
        n = 0;
    }

    // Doubles the size of the array
    private void doubleSize() {
        Event[] newArr = new Event[arr.length * 2];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        arr = newArr;
    }

    // Converts array to PQ
    private void arrayToPQ() {
        for (int i = n / 2; i > 0; i--) {
            sink(i);
        }

        printPQ();
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

        arr[n - 1] = event;
        int p = n / 2;
        int c = n;
        while (c != 1 && arr[p-1].timestamp > arr[c-1].timestamp) {
            sink(p);
            c = p;
            p /= 2;
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
    public double getTimeToNextEvent() {
        return arr[0].timestamp;
    }

    // Print the pq array in the form of a tree
    public void printPQ() {
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i].timestamp + " ");
        }

        System.out.println("\n----------------");
    }

    public static void tests() throws Exception {
        Random random = new Random();
        int[] timestamps = new int[random.nextInt(20) + 1];
        for (int i = 0; i < timestamps.length; i++) {
            timestamps[i] = random.nextInt(50);
        }
        Event[] a = new Event[timestamps.length];

        for (int i = 0; i < a.length; i++) {
            a[i] = new Event(timestamps[i]);
        }

        MinPQ pq = new MinPQ(a);
        for (int i = 0; i < 1; i++) {
//            int n = random.nextInt(20);
            int n = 0;
            System.out.println("Adding: " + n);
            pq.addEvent(new Event(n));
        }

        int count = pq.n;
        double x = pq.getNextEvent().timestamp, y;
        for (int i = 1; i < count; i++) {
            System.out.print(x + " ");
            y = x;
            x = pq.getNextEvent().timestamp;
            if (y > x) {
                 throw new Exception("Incorrect");
            }
        }
        System.out.println(x + "\n----------------------------------------------------------");
    }

    // Main function to test PQ
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            tests();
        }
    }
}
