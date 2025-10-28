package ps;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements MouseListener, MouseMotionListener {

    public boolean mouseClicked;
    public boolean mousePressed;
    private boolean mp;

    public int mx;
    public int my;

    public InputHandler(Canvas canvas) {
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    public void tick() {
        mouseClicked = !mousePressed && mp;
        mousePressed = mp;
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) { mp = true; }
    public void mouseReleased(MouseEvent e) { mp = false; }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        mx = e.getX() / ParticleComponent.SCALE;
        my = e.getY() / ParticleComponent.SCALE;
    }

    public void mouseMoved(MouseEvent e) {
        mx = e.getX() / ParticleComponent.SCALE;
        my = e.getY() / ParticleComponent.SCALE;
    }

}