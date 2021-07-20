import java.awt.Color;
import java.awt.Graphics;
import javax.swing.*;

public class Simulation {
    private static Particle[] particles; // the array containing all the particles
    private static Vector dimensions = new Vector(400, 400);
    private static int n = 10; // the number of particles in the simulation
    private static JFrame frame;

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
            p1.position.x -= p1.velocity.x;
            p1.position.y -= p1.velocity.y;
            p2.position.x -= p2.velocity.x;
            p2.position.y -= p2.velocity.y;

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

        frame = new JFrame("Particle Collision Simulator") {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, (int) dimensions.x, (int) dimensions.y);
                g.setColor(Color.white);
                for (Particle particle : particles) {
                    g.fillOval((int) particle.position.x, (int) particle.position.y, 2 * particle.radius,
                            2 * particle.radius);
                }
            }
        };
        frame.setSize((int) dimensions.x, (int) dimensions.y);
        frame.setLayout(null);
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
        frame.repaint();

        Thread.sleep(5);
    }

}