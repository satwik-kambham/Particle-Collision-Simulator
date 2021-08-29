import java.util.Random;

public class Particle {
    public int mass;
    public int radius;
    public Vector position;
    public Vector velocity;
    public static Random random = new Random();
    public static double acceleration = 0.005;
    public int count;

    Particle(Vector dimensions) {
        this.radius = random.nextInt(10) + 10;
        this.mass = (int) ((Math.PI * radius * radius) / 15);
        this.position = new Vector(random.nextInt((int) dimensions.x - 2 * radius) + radius,
                random.nextInt((int) dimensions.y - 2 * radius) + radius);
        this.velocity = new Vector(random.nextInt(9) - 4, random.nextInt(9) - 4);
        count = 0;
    }

    Particle(Vector dimensions, Vector position) {
        this.radius = 10;
        this.mass = (int) ((Math.PI * radius * radius) / 15);
        this.position = position;
        this.velocity = new Vector(random.nextInt(9) - 4, random.nextInt(9) - 4);
        count = 0;
    }

    Particle(Vector dimensions, int mass, int radius, Vector position, Vector velocity, Vector acceleration) {
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.velocity = velocity;
        count = 0;
    }

    public void progress() {
        // // giving the object slight acceleration
        velocity.x += Integer.signum((int) velocity.x) * acceleration;
        velocity.y += Integer.signum((int) velocity.y) * acceleration;

        position.x += velocity.x;
        position.y += velocity.y;
    }

    public void move(double dt) {
        this.position.x += this.velocity.x * dt;
        this.position.y += this.velocity.y * dt;
    }

    public void collideWithParticle(Particle p) {
        count++;
        p.count++;
        double distance = Math.pow((this.position.x - p.position.x), 2) + Math.pow((this.position.y - p.position.y), 2);
        double c1c2 = Math.sqrt(distance);

        if (c1c2 <= this.radius + this.radius) {
            double dx = this.position.x - p.position.x;
            double dy = this.position.y - p.position.y;
            double dvx = this.velocity.x - p.velocity.x;
            double dvy = this.velocity.y - p.velocity.y;
            double dvdr = dx * dvx + dy * dvy; // dv dot product dr
            double dist = p.radius + this.radius; // distance between particle centers at collision

            // magnitude of normal force
            double magnitude = 2 * p.mass * this.mass * dvdr / ((p.mass + this.mass) * dist);

            // normal force, and in x and y directions
            double fx = magnitude * dx / dist;
            double fy = magnitude * dy / dist;

            // update velocities according to normal force
            p.velocity.x += fx / p.mass;
            p.velocity.y += fy / p.mass;
            this.velocity.x -= fx / this.mass;
            this.velocity.y -= fy / this.mass;
        }
    }

    public void collideWithHorizontalWall() {
        count++;
        this.velocity.y = -this.velocity.y;
    }

    public void collideWithVerticalWall() {
        count++;
        this.velocity.x = -this.velocity.x;
    }

    public double timeToHit(Particle p) {
        if (this == p)
            return Double.POSITIVE_INFINITY;
        double dx = this.position.x - p.position.x;
        double dy = this.position.y - p.position.y;
        double dvx = this.velocity.x - p.velocity.x;
        double dvy = this.velocity.y - p.velocity.y;
        double dvdr = dx * dvx + dy * dvy; // dv dot product dr
        if (dvdr > 0)
            return Double.POSITIVE_INFINITY;
        double dvdv = dvx * dvx + dvy * dvy;
        if (dvdv == 0)
            return Double.POSITIVE_INFINITY;
        double drdr = dx * dx + dy * dy;
        double sigma = this.radius + p.radius;
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
        if (d < 0)
            return Double.POSITIVE_INFINITY;
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    public double timeToHitVerticalWall(Vector dimensions) {
        if (this.velocity.x > 0)
            return (dimensions.x - this.position.x - radius) / this.velocity.x;
        else if (this.velocity.x < 0)
            return (radius - this.position.x) / this.velocity.x;
        else
            return Double.POSITIVE_INFINITY;
    }

    public double timeToHitHorizontalWall(Vector dimensions) {
        if (this.velocity.y > 0)
            return (dimensions.y - this.position.y - radius) / this.velocity.y;
        else if (this.velocity.y < 0)
            return (radius - this.position.y) / this.velocity.y;
        else
            return Double.POSITIVE_INFINITY;
    }
}
