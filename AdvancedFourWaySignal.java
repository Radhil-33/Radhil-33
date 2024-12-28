import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AdvancedFourWaySignal extends JFrame implements ActionListener {
    private final JButton road1, road2, road3, road4;
    private final JComboBox<String> ambulanceDirection;
    private final JButton emergencyButton, pauseButton, resumeButton, resetButton;
    private final JButton manualRoad1, manualRoad2, manualRoad3, manualRoad4;
    private final JLabel logLabel, counter1, counter2, counter3, counter4;
    private final JTextArea logArea;
    private final JSlider trafficDensity1, trafficDensity2, trafficDensity3, trafficDensity4;
    private Timer timer;
    private int currentRoad = 1;
    private int[] counters = {0, 0, 0, 0};
    private boolean isPaused = false;

    public AdvancedFourWaySignal() {
        // Frame setup
        setTitle("Advanced Four-Way Traffic Signal");
        setSize(800, 700);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top panel for roads
        JPanel trafficPanel = new JPanel(new GridLayout(3, 3));
        road1 = createRoadButton("Road 1");
        road2 = createRoadButton("Road 2");
        road3 = createRoadButton("Road 3");
        road4 = createRoadButton("Road 4");

        trafficPanel.add(new JLabel());  // Empty cell
        trafficPanel.add(road1);
        trafficPanel.add(new JLabel("North", SwingConstants.CENTER));
        trafficPanel.add(road4);
        trafficPanel.add(new JLabel("Traffic Signal Control", SwingConstants.CENTER));
        trafficPanel.add(road2);
        trafficPanel.add(new JLabel("West", SwingConstants.CENTER));
        trafficPanel.add(road3);
        trafficPanel.add(new JLabel("South", SwingConstants.CENTER));

        add(trafficPanel, BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Ambulance Direction:"));

        String[] directions = {"None", "Road 1", "Road 2", "Road 3", "Road 4"};
        ambulanceDirection = new JComboBox<>(directions);
        controlPanel.add(ambulanceDirection);

        emergencyButton = new JButton("Emergency");
        emergencyButton.addActionListener(this);
        controlPanel.add(emergencyButton);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        controlPanel.add(pauseButton);

        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(this);
        controlPanel.add(resumeButton);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Right panel for traffic counters and sliders
        JPanel counterPanel = new JPanel(new GridLayout(4, 2));
        counter1 = createCounterLabel("Road 1 Vehicles: 0");
        counter2 = createCounterLabel("Road 2 Vehicles: 0");
        counter3 = createCounterLabel("Road 3 Vehicles: 0");
        counter4 = createCounterLabel("Road 4 Vehicles: 0");

        trafficDensity1 = createTrafficSlider("Road 1 Density");
        trafficDensity2 = createTrafficSlider("Road 2 Density");
        trafficDensity3 = createTrafficSlider("Road 3 Density");
        trafficDensity4 = createTrafficSlider("Road 4 Density");

        counterPanel.add(counter1);
        counterPanel.add(trafficDensity1);
        counterPanel.add(counter2);
        counterPanel.add(trafficDensity2);
        counterPanel.add(counter3);
        counterPanel.add(trafficDensity3);
        counterPanel.add(counter4);
        counterPanel.add(trafficDensity4);
        add(counterPanel, BorderLayout.EAST);

        // Left panel for manual controls
        JPanel manualPanel = new JPanel(new GridLayout(4, 1));
        manualRoad1 = createManualControlButton("Open Road 1", 1);
        manualRoad2 = createManualControlButton("Open Road 2", 2);
        manualRoad3 = createManualControlButton("Open Road 3", 3);
        manualRoad4 = createManualControlButton("Open Road 4", 4);

        manualPanel.add(manualRoad1);
        manualPanel.add(manualRoad2);
        manualPanel.add(manualRoad3);
        manualPanel.add(manualRoad4);
        add(manualPanel, BorderLayout.WEST);

        // Log panel
        JPanel logPanel = new JPanel(new BorderLayout());
        logLabel = new JLabel("Event Log:");
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        logPanel.add(logLabel, BorderLayout.NORTH);
        logPanel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(logPanel, BorderLayout.NORTH);

        // Timer for automatic switching
        startTrafficSignalTimer();

        setVisible(true);
    }

    private JButton createRoadButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.RED);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private JLabel createCounterLabel(String text) {
        return new JLabel(text, SwingConstants.LEFT);
    }

    private JSlider createTrafficSlider(String text) {
        JSlider slider = new JSlider(0, 100, 50);
        slider.setMajorTickSpacing(25);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBorder(BorderFactory.createTitledBorder(text));
        return slider;
    }

    private JButton createManualControlButton(String text, int road) {
        JButton button = new JButton(text);
        button.addActionListener(e -> openRoad(road));
        return button;
    }

    private void startTrafficSignalTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    switchTrafficSignal();
                }
            }
        }, 0, 5000);
    }

    private void switchTrafficSignal() {
        resetSignals();
        openRoad(currentRoad);
        currentRoad = (currentRoad % 4) + 1;
    }

    private void openRoad(int road) {
        resetSignals();
        switch (road) {
            case 1 -> updateRoadStatus(road1, 0, counter1);
            case 2 -> updateRoadStatus(road2, 1, counter2);
            case 3 -> updateRoadStatus(road3, 2, counter3);
            case 4 -> updateRoadStatus(road4, 3, counter4);
        }
    }

    private void updateRoadStatus(JButton road, int index, JLabel counterLabel) {
        road.setBackground(Color.GREEN);
        counters[index]++;
        counterLabel.setText("Road " + (index + 1) + " Vehicles: " + counters[index]);
        logEvent("Road " + (index + 1) + " is now GREEN.");
    }

    private void resetSignals() {
        road1.setBackground(Color.RED);
        road2.setBackground(Color.RED);
        road3.setBackground(Color.RED);
        road4.setBackground(Color.RED);
    }

    private void logEvent(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        logArea.append("[" + timestamp + "] " + message + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == emergencyButton) {
            handleEmergency();
        } else if (e.getSource() == pauseButton) {
            isPaused = true;
            logEvent("Traffic signal paused.");
        } else if (e.getSource() == resumeButton) {
            isPaused = false;
            logEvent("Traffic signal resumed.");
        } else if (e.getSource() == resetButton) {
            resetSystem();
        }
    }

    private void handleEmergency() {
        String direction = (String) ambulanceDirection.getSelectedItem();
        resetSignals();
        logEvent("Emergency! Ambulance on " + direction);
        switch (direction) {
            case "Road 1" -> road1.setBackground(Color.GREEN);
            case "Road 2" -> road2.setBackground(Color.GREEN);
            case "Road 3" -> road3.setBackground(Color.GREEN);
            case "Road 4" -> road4.setBackground(Color.GREEN);
            default -> logEvent("No valid ambulance direction selected.");
        }
    }

    private void resetSystem() {
        resetSignals();
        logArea.setText("");
        counters = new int[]{0, 0, 0, 0};
        counter1.setText("Road 1 Vehicles: 0");
        counter2.setText("Road 2 Vehicles: 0");
        counter3.setText("Road 3 Vehicles: 0");
        counter4.setText("Road 4 Vehicles: 0");
        logEvent("System reset.");
    }

    public static void main(String[] args) {
        new AdvancedFourWaySignal();
    }
}
