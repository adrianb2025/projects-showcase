package game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputHandler extends MouseAdapter {
    private int mx;
    private int my;
    public int x;
    public int y;
    public boolean leftPressed;
    public boolean leftClicked;
    private boolean m0p;

    public InputHandler(Canvas canvas) {
        canvas.addMouseListener(this);
    }

    public void mousePressed(MouseEvent e) {
        mx = e.getX() / GameComponent.SCALE;
        my = e.getY() / GameComponent.SCALE;

        m0p = e.getButton() == MouseEvent.BUTTON1;
    }

    public void mouseReleased(MouseEvent e) {
        mx = e.getX() / GameComponent.SCALE;
        my = e.getY() / GameComponent.SCALE;

        m0p = false;
    }

    public void tick() {
        x = mx;
        y = my;
        leftClicked = leftPressed && !m0p;
        leftPressed = m0p;
    }

}