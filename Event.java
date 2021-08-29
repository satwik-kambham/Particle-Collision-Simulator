public class Event {
    public double timestamp;
    public Particle p1, p2;
    public int countA, countB;

    // Blank event
    Event(double t) {
        timestamp = t;
        p1 = p2 = null;
    }

    // Collision between two particles, p1 and p2 at time t
    Event(Particle P1, Particle P2, double t) {
        timestamp = t;
        p1 = P1;
        p2 = P2;
        if (p1 != null) countA = p1.count;
        if (p2 != null) countB = p2.count;
    }

    // Check if event is valid or not
    public boolean isValid() {
        if (p1 != null && p1.count != countA) return false;
        if (p2 != null && p2.count != countB) return false;
        return true;
    }
}
