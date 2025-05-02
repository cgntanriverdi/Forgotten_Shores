package main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class StartScreen extends JFrame {

    // Instance variables
    private JPanel panel;
    private JButton startButton;
    private JButton creditButton;
    private JButton reloadButton;
    private JButton exitButton;
    private BufferedImage image;
    private Font retroFont;

    // Constructor
    public StartScreen() {
        try {
            this.image = ImageIO.read(getClass().getResource("/res/StartScreen/StartScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            retroFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/res/fonts/Jersey15-Regular.ttf")).deriveFont(48f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(retroFont);
        createPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setTitle("FORGOTTEN SHORES");
        setUndecorated(true);
        setResizable(false);
        setVisible(true);
    }

    class ClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Dispose the start screen
            dispose();
            // Launch the game frame using the Main class's static method
            Main.startGame(false);
        }
    }
    
    class ClickListener2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new LoadSlotWindow(StartScreen.this).setVisible(true);

        }
    }

    class ClickListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new CreditScreen(StartScreen.this , retroFont);
        }

    }

    class ClickListener4 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
        
    }

    class Panel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void createPanel() {
        this.panel = new Panel();
        panel.setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(220, 63);

        this.startButton = new JButton("START GAME");
        startButton.setBackground(new Color(0xFFD34E));
        startButton.setForeground(new Color(0x4E2E1F));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(0x996633), 3));
        startButton.setFont(retroFont);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(buttonSize);
        startButton.setMaximumSize(buttonSize);
        startButton.setMinimumSize(buttonSize);
        startButton.setFocusable(false);
        startButton.addActionListener(new ClickListener());

        this.reloadButton = new JButton("LOAD");
        reloadButton.setBackground(new Color(0xFFD34E));
        reloadButton.setForeground(new Color(0x4E2E1F));
        reloadButton.setBorder(BorderFactory.createLineBorder(new Color(0x996633), 3));
        reloadButton.setFont(retroFont);
        reloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reloadButton.setPreferredSize(buttonSize);
        reloadButton.setMaximumSize(buttonSize);
        reloadButton.setMinimumSize(buttonSize);
        reloadButton.setFocusable(false);
        reloadButton.addActionListener(new ClickListener2());

        this.creditButton = new JButton("  CREDITS  ");
        creditButton.setBackground(new Color(0xFFD34E));
        creditButton.setForeground(new Color(0x4E2E1F));
        creditButton.setBorder(BorderFactory.createLineBorder(new Color(0x996633), 3));
        creditButton.setFont(retroFont);
        creditButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        creditButton.setPreferredSize(buttonSize);
        creditButton.setMaximumSize(buttonSize);
        creditButton.setMinimumSize(buttonSize);
        creditButton.setFocusable(false);
        creditButton.addActionListener(new ClickListener3());

        this.exitButton = new JButton("  EXIT  ");
        exitButton.setBackground(new Color(0xFFD34E));
        exitButton.setForeground(new Color(0x4E2E1F));
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(0x996633), 3));
        exitButton.setFont(retroFont);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setPreferredSize(buttonSize);
        exitButton.setMaximumSize(buttonSize);
        exitButton.setMinimumSize(buttonSize);
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ClickListener4());

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(startButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(reloadButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(creditButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(exitButton);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.setOpaque(true);
        this.add(panel);
    }
}
