import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class EDSimulation extends Canvas {
    private static Particle[] particles; // the array containing all the particles
    private static Vector dimensions = new Vector(400, 400);
    private static int n = 10; // the number of particles in the simulation
    private static MinPQ minPQ; // the min priority queue containing the predicted events
    private static double t = 0; // the simulation clock
    private static double dr = 1; // the number of times to draw every tick
    private static JFrame frame;
    private static EDSimulation simulation;
    private static BufferedImage bufferedImage;
    private static Graphics2D big;
    private static boolean firstTime = true;

    EDSimulation() {
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
        System.out.println("Redrawing");
        minPQ.addEvent(new Event(null, null, t + 1.0 / dr));

        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // add predictions to PQ
    public static void predict(Particle p) {
        if (p == null)
            return;

        // particle-particle collisions
        for (int i = 0; i < particles.length; i++) {
            double dt = p.timeToHit(particles[i]);
            minPQ.addEvent(new Event(p, particles[i], t + dt));
        }

        // particle-wall collisions
        double dtX = p.timeToHitVerticalWall(dimensions);
        double dtY = p.timeToHitHorizontalWall(dimensions);
        minPQ.addEvent(new Event(p, null, t + dtX));
        minPQ.addEvent(new Event(null, p, t + dtY));
    }

    public static void main(String[] args) throws Exception {
        // Creating n random particles
        particles = new Particle[n];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(dimensions);
        }

        frame = new JFrame("Event Driven Particle Collision Simulator");
        simulation = new EDSimulation();
        simulation.setBounds(10, 10, (int) dimensions.x, (int) dimensions.y);
        frame.add(simulation);

        frame.pack();
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        minPQ = new MinPQ();
        for (int i = 0; i < particles.length; i++) {
            predict(particles[i]);
        }
        minPQ.addEvent(new Event(null, null, 0)); // redraw event

        // the main event-driven simulation loop
        while (minPQ.n != 0) {

            // get impending event, discard if invalidated
            Event e = minPQ.getNextEvent();
            if (!e.isValid())
                continue;
            Particle a = e.p1;
            Particle b = e.p2;

            // physical collision, so update positions, and then simulation clock
            for (int i = 0; i < particles.length; i++)
                particles[i].move(e.timestamp - t);
            t = e.timestamp;

            // process events
            if (a != null && b != null)
                a.collideWithParticle(b); // particle-particle collision
            else if (a != null && b == null)
                a.collideWithVerticalWall(); // particle-wall collision
            else if (a == null && b != null)
                b.collideWithHorizontalWall(); // particle-wall collision
            else if (a == null && b == null)
                simulation.repaint(); // redraw event

            // update the priority queue with new collisions involving a or b
            predict(a);
            predict(b);
        }
    }

}