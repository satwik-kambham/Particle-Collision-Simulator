import java.util.Random;

public class Particle {
    public int mass;
    public int radius;
    public Vector position;
    public Vector velocity;
    public static Random random = new Random();
    public static double acceleration = 0.005;

    Particle(Vector dimensions) {
        this.radius = random.nextInt(10) + 10;
        this.mass = (int) ((Math.PI * radius * radius) / 15);
        this.position = new Vector(random.nextInt((int) dimensions.x - 2 * radius) + radius,
                random.nextInt((int) dimensions.y - 2 * radius) + radius);
        this.velocity = new Vector(random.nextInt(9) - 4, random.nextInt(9) - 4);
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
        // giving the object slight acceleration
        velocity.x += Integer.signum((int) velocity.x) * acceleration;
        velocity.y += Integer.signum((int) velocity.y) * acceleration;

        position.x += velocity.x;
        position.y += velocity.y;
    }
}
