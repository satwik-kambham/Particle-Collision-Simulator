public class Event {
    public int timestamp;
    public Particle p1, p2;

    // Blank event
    Event(int t) {
        timestamp = t;
        p1 = p2 = null;
    }

    // Collision between two particles, p1 and p2 at time t
    Event(Particle P1, Particle P2, int t) {
        timestamp = t;
        p1 = P1;
        p2 = P2;
    }

    // Collision between particle p and wall at time t
    Event(Particle p, int t) {
        timestamp = t;
        p1 = p;
        p2 = null;
    }
}
