import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class Simulation extends Canvas {
    private static Particle[] particles; // the array containing all the particles
    private static Vector dimensions = new Vector(400, 400);
    private static int n = 10; // the number of particles in the simulation
    private static JFrame frame;
    private static Simulation simulation;
    private static BufferedImage bufferedImage;
    private static Graphics2D big;
    private static boolean firstTime = true;

    Simulation() {
        setBackground(Color.BLACK);
        bufferedImage = new BufferedImage((int) dimensions.x, (int) dimensions.y, BufferedImage.TYPE_INT_RGB);
        big = bufferedImage.createGraphics();
        big.dispose();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (firstTime) {
            Dimension dim = getSize();
            int w = dim.width;
            int h = dim.height;
            bufferedImage = (BufferedImage) createImage(w, h);
            big = bufferedImage.createGraphics();
            firstTime = false;
        }
        big.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        big.setColor(Color.BLACK);
        big.fillRect(0, 0, (int) dimensions.x, (int) dimensions.y);
        big.setColor(Color.WHITE);
        for (Particle particle : particles) {
            big.fillOval((int) particle.position.x, (int) particle.position.y, 2 * particle.radius,
                    2 * particle.radius);
        }

        g2.drawImage(bufferedImage, 0, 0, this);
    }

    // Updates the particle's variables when they collide
    private static void collide(Particle p1, Particle p2) {
        double distance = Math.pow((p1.position.x - p2.position.x), 2) + Math.pow((p1.position.y - p2.position.y), 2);
        double c1c2 = Math.sqrt(distance);

        if (c1c2 <= p1.radius + p2.radius) {
            double dx = p1.position.x - p2.position.x;
            double dy = p1.position.y - p2.position.y;
            double dvx = p1.velocity.x - p2.velocity.x;
            double dvy = p1.velocity.y - p2.velocity.y;
            double dvdr = dx * dvx + dy * dvy; // dv dot dr
            double dist = p2.radius + p1.radius; // distance between particle centers at collison

            // magnitude of normal force
            double magnitude = 2 * p2.mass * p1.mass * dvdr / ((p2.mass + p1.mass) * dist);

            // normal force, and in x and y directions
            double fx = magnitude * dx / dist;
            double fy = magnitude * dy / dist;

            // updating postion so that they do not overlap
            p1.position.x -= p1.velocity.x + Integer.signum((int) p1.velocity.x) * Particle.acceleration * 2;
            p1.position.y -= p1.velocity.y + Integer.signum((int) p1.velocity.y) * Particle.acceleration * 2;
            p2.position.x -= p2.velocity.x + Integer.signum((int) p2.velocity.x) * Particle.acceleration * 2;
            p2.position.y -= p2.velocity.y + Integer.signum((int) p2.velocity.y) * Particle.acceleration * 2;

            // update velocities according to normal force
            p2.velocity.x += fx / p2.mass;
            p2.velocity.y += fy / p2.mass;
            p1.velocity.x -= fx / p1.mass;
            p1.velocity.y -= fy / p1.mass;
        }
    }

    // Checks and updates when ball collides with wall
    private static void collideWithWall(Particle p) {
        if (p.position.x + p.radius >= dimensions.x) {
            p.velocity.x = -p.velocity.x;
            p.position.x = dimensions.x - p.radius - 1;
        } else if (p.position.x - p.radius <= 0) {
            p.velocity.x = -p.velocity.x;
            p.position.x = p.radius + 1;
        }

        if (p.position.y + p.radius >= dimensions.y) {
            p.velocity.y = -p.velocity.y;
            p.position.y = dimensions.y - p.radius - 1;
        } else if (p.position.y - p.radius <= 0) {
            p.velocity.y = -p.velocity.y;
            p.position.y = p.radius + 1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Creating n random particles
        particles = new Particle[n];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(dimensions);
        }

        frame = new JFrame("Particle Collision Simulator");
        simulation = new Simulation();
        simulation.setBounds(10, 10, (int) dimensions.x, (int) dimensions.y);
        frame.add(simulation);

        frame.pack();
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // calling the main loop
        while (true) {
            loop();
        }
    }

    // main loop
    private static void loop() throws InterruptedException {
        // Progressing the particle's variables by a given time
        for (Particle particle : particles) {
            collideWithWall(particle);
            particle.progress();
        }

        // Checking whether any two particles are colliding
        for (int i = 0; i < particles.length; i++) {
            for (int j = 0; j < particles.length; j++) {
                // Updating the particle's variables if they are colliding
                if (i != j) {
                    collide(particles[i], particles[j]);
                }
            }
        }

        // Drawing the particles
        simulation.repaint();

        Thread.sleep(15);
    }

}