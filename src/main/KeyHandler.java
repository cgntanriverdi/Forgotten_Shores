package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener, MouseListener {
    GamePanel gp;

    public boolean upPressed;
    public boolean downPressed;
    public boolean rightPressed;
    public boolean leftPressed;
    public boolean fPressed;
    public boolean leftClicked = false;
    public boolean cPressed = false;
    public boolean escPressed = false;
    public boolean rPressed = false;
    public boolean ePressed = false;
    public boolean gPressed = false;
    public boolean kPressed = false;
    public boolean qPressed = false;
    public boolean enterPressed = false;

    public boolean rightArrowPressed = false;
    public boolean leftArrowPressed = false;

    public boolean onePressed = false;
    public boolean twoPressed = false;
    public boolean threePressed = false;
    public boolean fourPressed = false;
    public boolean fivePressed = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void keyTyped(KeyEvent e) {
        // Not used
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = true;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = true;
        }
        if (code == KeyEvent.VK_G) {
            gPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE && gp.gameState != gp.gameOverState) {
            if (gp.gameState == gp.playState) {
                gp.gameState = gp.pauseState;
                gp.pausePanel.setVisible(true);
            } else if (gp.gameState == gp.pauseState) {
                gp.gameState = gp.playState;
                gp.pausePanel.setVisible(false);
            } else if (gp.gameState == gp.dialogueState) {
                gp.gameState = gp.playState;
            }
        }

        if (code == KeyEvent.VK_1) {
            onePressed = true;
        }
        if (code == KeyEvent.VK_2) {
            twoPressed = true;
        }
        if (code == KeyEvent.VK_3) {
            threePressed = true;
        }
        if (code == KeyEvent.VK_4) {
            fourPressed = true;
        }
        if (code == KeyEvent.VK_5) {
            fivePressed = true;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escPressed = true;
        }
        if (code == KeyEvent.VK_R) {
            rPressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (code == KeyEvent.VK_K) {
            kPressed = true;
        }
        if (code == KeyEvent.VK_Q) {
            qPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightArrowPressed = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftArrowPressed = true;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_F) {
            fPressed = false;
        }
        if (code == KeyEvent.VK_C) {
            cPressed = false;
        }
        if (code == KeyEvent.VK_E) {
            ePressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if (code == KeyEvent.VK_K) {
            kPressed = false;
        }
        if (code == KeyEvent.VK_Q) {
            qPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            rightArrowPressed = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            leftArrowPressed = false;
        }
    }
    // Method for game over. Currently unused
    /*public void gameOver(KeyEvent e)
    {

        int inputCode = e.getKeyCode();

        if (inputCode == KeyEvent.VK_R)
        {
            gp.gameState = gp.playState;
        }
        if (inputCode == KeyEvent.VK_ESCAPE)
        {
            gp.gameState = gp.playState;
        }
        
    }*/

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftClicked = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
