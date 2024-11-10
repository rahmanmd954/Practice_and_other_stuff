import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerUI {
    private int seconds = 0;
    private JLabel timerLabel;
    private Timer timer;
    private Timer flashTimer;

    public TimerUI() {
        // Create JFrame
        JFrame frame = new JFrame("Timer UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLayout(new BorderLayout());

        // Create Timer Label
        timerLabel = new JLabel("Time: 00:00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        frame.add(timerLabel, BorderLayout.CENTER);

        // Create Input Panel for Time Setting
        JPanel inputPanel = new JPanel();
        JTextField hoursField = new JTextField(2);
        JTextField minutesField = new JTextField(2);
        JTextField secondsField = new JTextField(2);
        inputPanel.add(new JLabel("Hours: "));
        inputPanel.add(hoursField);
        inputPanel.add(new JLabel("Minutes: "));
        inputPanel.add(minutesField);
        inputPanel.add(new JLabel("Seconds: "));
        inputPanel.add(secondsField);
        frame.add(inputPanel, BorderLayout.NORTH);

        // Create Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));

        // Start Button
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopFlashing();
                try {
                    int hrs = Integer.parseInt(hoursField.getText());
                    int mins = Integer.parseInt(minutesField.getText());
                    int secs = Integer.parseInt(secondsField.getText());
                    seconds = hrs * 3600 + mins * 60 + secs;
                    updateTimerLabel();
                    startTimer();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers for hours, minutes, and seconds.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(startButton);

        // Stop Button
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
                stopFlashing();
            }
        });
        buttonPanel.add(stopButton);

        // Add Button Panel to Frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Initialize Timer
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seconds > 0) {
                    seconds--;
                    updateTimerLabel();
                } else {
                    timer.stop();
                    startFlashing();
                }
            }
        });

        // Show frame
        frame.setVisible(true);
    }

    private void startTimer() {
        timer.start();
    }

    private void stopTimer() {
        timer.stop();
    }

    private void updateTimerLabel() {
        int hrs = seconds / 3600;
        int mins = (seconds % 3600) / 60;
        int secs = seconds % 60;
        timerLabel.setText(String.format("Time: %02d:%02d:%02d", hrs, mins, secs));
    }

    private void startFlashing() {
        flashTimer = new Timer(500, new ActionListener() {
            private boolean isRed = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRed) {
                    timerLabel.setForeground(Color.BLACK);
                } else {
                    timerLabel.setForeground(Color.RED);
                }
                isRed = !isRed;
            }
        });
        flashTimer.start();
    }

    private void stopFlashing() {
        if (flashTimer != null && flashTimer.isRunning()) {
            flashTimer.stop();
            timerLabel.setForeground(Color.BLACK);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TimerUI();
            }
        });
    }
}
