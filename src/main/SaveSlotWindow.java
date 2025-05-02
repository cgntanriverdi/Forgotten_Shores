package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SaveSlotWindow extends JDialog {

    private Font font;
    private BufferedImage backgroundImage;
    private GamePanel gp;
    private JPanel buttonPanel;
    private JPanel backgroundPanel;

    public SaveSlotWindow(GamePanel gp) {
        super(Main.frame, true);
        this.gp = gp;

        try {
            font = Font
                    .createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/res/fonts/Jersey15-Regular.ttf"))
                    .deriveFont(48f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/res/PauseScreen/PauseScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        JLabel titleLabel = new JLabel("SAVE GAME", SwingConstants.CENTER);
        titleLabel.setFont(font.deriveFont(Font.BOLD, 48f));
        titleLabel.setForeground(new Color(230, 220, 200));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(backgroundPanel);

        addSlotButtons();

        setVisible(true);
    }

    private void addSlotButtons() {
        Dimension buttonSize = new Dimension(220, 40);
        Color bgColor = new Color(0x2E8B22);
        Color borderColor = new Color(0x246B1A);

        for (int i = 1; i <= 3; i++) {
            String label;
            if (gp.saveStorage.isSlotOccupied(i)) {
                int day = gp.saveStorage.getDayOfSlot(i);
                label = "SAVE SLOT " + i + " - DAY " + day;
            } else {
                label = "SAVE SLOT " + i + " - EMPTY";
            }

            JButton slotButton = new JButton(label);
            slotButton.setBackground(bgColor);
            slotButton.setForeground(Color.WHITE);
            slotButton.setBorder(BorderFactory.createLineBorder(borderColor, 3));
            slotButton.setFont(font.deriveFont(Font.BOLD, 24f));
            slotButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            slotButton.setPreferredSize(buttonSize);
            slotButton.setMaximumSize(buttonSize);
            slotButton.setMinimumSize(buttonSize);
            slotButton.setFocusable(false);

            int slot = i;
            slotButton.addActionListener(e -> handleSlotClick(slot));

            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(slotButton);
        }

        JButton cancelButton = new JButton("CANCEL");
        cancelButton.setBackground(new Color(0x993333));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createLineBorder(new Color(0x732626), 3));
        cancelButton.setFont(font.deriveFont(Font.BOLD, 24f));
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(cancelButton);
    }

    private void handleSlotClick(int slot) {
        if (gp.saveStorage.isSlotOccupied(slot)) {
            int result = JOptionPane.showConfirmDialog(this,
                    "This slot already has a save.\nDo you want to overwrite it?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION);

            if (result != JOptionPane.YES_OPTION)
                return;
        }

        gp.saveStorage.saveGame(slot);
        JOptionPane.showMessageDialog(this, "Game saved successfully to slot " + slot + "!");
        dispose();
    }
}
