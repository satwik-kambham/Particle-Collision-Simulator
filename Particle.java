import java.util.Random;

public class Particle {
    public int mass;
    public int radius;
    public Vector position;
    public Vector velocity;
    public static Random random = new Random();

    Particle(Vector dimensions) {
        this.radius = random.nextInt(10) + 10;
        this.mass = (int) ((Math.PI * radius * radius) / 15);
        this.position = new Vector(random.nextInt((int) dimensions.x - 2 * radius) + radius,
                random.nextInt((int) dimensions.y - 2 * radius) + radius);
        this.velocity = new Vector(random.nextInt(3) - 1, random.nextInt(3) - 1);
    }

    Particle(Vector dimensions, Vector position) {
        this.mass = 10;
        this.radius = 5;
        this.position = position;
        this.velocity = new Vector();
    }

    Particle(Vector dimensions, int mass, int radius, Vector position, Vector velocity, Vector acceleration) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
    }

    public void progress() {
        position.x += velocity.x;
        position.y += velocity.y;
    }

    public static void main(String[] args) {
        Vector dimensions = new Vector(400, 400);
        Particle[] particles = new Particle[2];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(dimensions);
        }
        System.out.println(particles[0].position.x);
    }
}
