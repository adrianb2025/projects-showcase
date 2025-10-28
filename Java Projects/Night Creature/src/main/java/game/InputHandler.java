package game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputHandler implements MouseListener {

    public int x;
    public int y;

    public boolean leftClicked;
    public boolean leftPressed;
    private boolean m0p;

    public boolean rightClicked;
    public boolean rightPressed;
    private boolean m1p;

    public InputHandler(Canvas canvas) { canvas.addMouseListener(this); }

    public void tick() {
        leftClicked = !leftPressed && m0p;
        rightClicked = !rightPressed && m1p;
        leftPressed = m0p;
        rightPressed = m1p;
    }

    public void mouseClicked(MouseEvent e) {
        x = e.getX() / NightCreature.SCALE;
        y = e.getY() / NightCreature.SCALE;
    }

    public void mousePressed(MouseEvent e) {
        x = e.getX() / NightCreature.SCALE;
        y = e.getY() / NightCreature.SCALE;

        m0p = e.getButton() == 1;
        m1p = e.getButton() == 3;
    }

    public void mouseReleased(MouseEvent e) {
        x = e.getX() / NightCreature.SCALE;
        y = e.getY() / NightCreature.SCALE;

        m0p = false;
        m1p = false;
    }

    public void mouseEntered(MouseEvent e) {
        x = e.getX() / NightCreature.SCALE;
        y = e.getY() / NightCreature.SCALE;
    }

    public void mouseExited(MouseEvent e) {}

}