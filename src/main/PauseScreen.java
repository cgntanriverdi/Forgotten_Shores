package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PauseScreen extends JDialog{
    private JButton continueButton;
    private JButton saveButton;
    private JButton returnButton;
    private JButton exitButton;

    private Font font;
    private BufferedImage backgroundImage;
    private GamePanel gp;
    private JPanel buttonPanel;
    private JPanel backgroundPanel;
    

    public PauseScreen ( GamePanel gp) {
        super(Main.frame, true);
        setFocusableWindowState(false); 
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/res/fonts/Jersey15-Regular.ttf")).deriveFont(48f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/res/PauseScreen/PauseScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        this.gp = gp;

        setUndecorated(true); 
        setSize(300, 300); 
        setLocationRelativeTo(null); 

        backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JLabel pausedLabel = new JLabel("PAUSED", SwingConstants.CENTER);
        pausedLabel.setFont(font.deriveFont(Font.BOLD, 48f));
        pausedLabel.setForeground(new Color(230, 220, 200));
        pausedLabel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        buttonPanel.add(Box.createVerticalStrut(5));
        continueButton = createButton("CONTINUE", new Color(0xB7420E), new Color(0x8A320B));
        continueButton.addActionListener(new ContinueListener());

        saveButton = createButton("SAVE", new Color(0x2E8B22), new Color(0x246B1A));
        saveButton.addActionListener(new SaveListener());

        returnButton = createButton("RETURN MENU", new Color(0x2E8399), new Color(0x256877));
        returnButton.addActionListener(new ReturnListener());

        exitButton = createButton("EXIT", new Color(0x993333), new Color(0x732626));
        exitButton.addActionListener(new ExitListener());

        backgroundPanel.add(pausedLabel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
        setVisible(false);
    }
    
    
    class ContinueListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gp.gameState = gp.playState;
            gp.requestFocusInWindow();
            setVisible(false);
        }

    }
    
    class ReturnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Main.frame.getContentPane().removeAll();
            Main.frame.repaint();
            Main.frame.revalidate();
            Main.frame.dispose();
            new StartScreen();
        }
    }

    class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new SaveSlotWindow(gp);
        }
        
    }
    class ExitListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        } 
    }

    private JButton createButton(String name, Color bgColor, Color borderColor) {
        JButton button = new JButton(name);
    
        Dimension buttonSize = new Dimension(220, 40);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(borderColor, 3));
        button.setFont(font.deriveFont(Font.BOLD, 30f));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        button.setFocusable(false);
    
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(button);
    
        return button;
    }

}

