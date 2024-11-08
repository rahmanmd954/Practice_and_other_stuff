import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 30;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private final int SCREEN_SIZE = WIDTH * HEIGHT;
    private final int DELAY = 150;
    private final LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = true;
    private Timer timer;
    private int score = 0;

    public SnakeGame() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        addKeyListener(new TAdapter());
        startGame();
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        direction = 'R';
        score = 0;
        spawnFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(WIDTH);
            y = rand.nextInt(HEIGHT);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            draw(g);
        } else {
            gameOver(g);
        }
    }

    private void draw(Graphics g) {
        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, WIDTH * TILE_SIZE - 100, 20);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        String message = "Game Over! Press R to Restart";
        g.drawString(message, WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2);
        g.setColor(Color.WHITE);
        g.drawString("Final Score: " + score, WIDTH * TILE_SIZE / 4, HEIGHT * TILE_SIZE / 2 + 20);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);
        switch (direction) {
            case 'U': newHead.y -= 1; break;
            case 'D': newHead.y += 1; break;
            case 'L': newHead.x -= 1; break;
            case 'R': newHead.x += 1; break;
        }
        
        if (newHead.equals(food)) {
            snake.addFirst(newHead);
            score += 10;
            spawnFood();
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }

        checkCollision();
    }

    private void checkCollision() {
        Point head = snake.getFirst();
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }
        if (snake.stream().skip(1).anyMatch(point -> point.equals(head))) {
            running = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_A:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_R:
                    if (!running) startGame();
                    break;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
