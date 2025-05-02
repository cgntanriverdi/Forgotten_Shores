package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import save.SaveStorage;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadSlotWindow extends JFrame {

    private JPanel panel;
    private JButton slot1Button, slot2Button, slot3Button, cancelButton;
    private BufferedImage image;
    private Font retroFont;
    private JFrame startScreenRef;

    public LoadSlotWindow(JFrame startScreenRef) {
        this.startScreenRef = startScreenRef;
        try {
            image = ImageIO.read(getClass().getResource("/res/StartScreen/StartScreen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            retroFont = Font
                    .createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/res/fonts/Jersey15-Regular.ttf"))
                    .deriveFont(32f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(retroFont);

        createPanel();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Load Game");
        setUndecorated(true);
        setResizable(false);
        setVisible(true);
    }

    class ClickListener implements ActionListener {
        private final int slot;

        public ClickListener(int slot) {
            this.slot = slot;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SaveStorage tempSave = new SaveStorage(null);
            if (tempSave.isSlotOccupied(slot)) {
                dispose(); // pencereyi kapat
                startScreenRef.dispose(); // ana menüyü kapat
                Main.startGameFromSlot(slot); // oyunu başlat
            } else {
                JOptionPane.showMessageDialog(
                        LoadSlotWindow.this,
                        "This slot is empty. Please select a slot that has a saved game.",
                        "Load Failed",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dispose();
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
        Dimension buttonSize = new Dimension(400, 70);
        Color background = new Color(0xFFD34E);
        Color foreground = new Color(0x4E2E1F);
        Border border = BorderFactory.createLineBorder(new Color(0x996633), 3);

        SaveStorage tempSave = new SaveStorage(null);

        String label1 = tempSave.isSlotOccupied(1)
                ? "LOAD SLOT 1 - DAY " + tempSave.getDayOfSlot(1)
                : "LOAD SLOT 1 - EMPTY";

        String label2 = tempSave.isSlotOccupied(2)
                ? "LOAD SLOT 2 - DAY " + tempSave.getDayOfSlot(2)
                : "LOAD SLOT 2 - EMPTY";

        String label3 = tempSave.isSlotOccupied(3)
                ? "LOAD SLOT 3 - DAY " + tempSave.getDayOfSlot(3)
                : "LOAD SLOT 3 - EMPTY";

        slot1Button = createSlotButton(label1, 1, buttonSize, background, foreground, border);
        slot2Button = createSlotButton(label2, 2, buttonSize, background, foreground, border);
        slot3Button = createSlotButton(label3, 3, buttonSize, background, foreground, border);

        cancelButton = new JButton("CANCEL");
        cancelButton.setBackground(background);
        cancelButton.setForeground(foreground);
        cancelButton.setBorder(border);
        cancelButton.setFont(retroFont);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cancelButton.setPreferredSize(buttonSize);
        cancelButton.setMaximumSize(buttonSize);
        cancelButton.setMinimumSize(buttonSize);
        cancelButton.setFocusable(false);
        cancelButton.addActionListener(new CancelListener());

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(slot1Button);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(slot2Button);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(slot3Button);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(cancelButton);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.setOpaque(true);
        this.add(panel);
    }

    private JButton createSlotButton(String label, int slot, Dimension size, Color bg, Color fg, Border border) {
        JButton button = new JButton(label);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(border);
        button.setFont(retroFont);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setFocusable(false);
        button.addActionListener(new ClickListener(slot));
        return button;
    }
}
