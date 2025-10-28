package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener, MouseListener {

    private List<Key> keys = new ArrayList<Key>();
    public Key start = new Key();
    public Key n0 = new Key();
    public Key n1 = new Key();
    public Key n2 = new Key();
    public Key n3 = new Key();
    public Key n4 = new Key();
    public Key n5 = new Key();
    public Key n6 = new Key();
    public Key n7 = new Key();
    public Key n8 = new Key();

    public int x;
    public int y;
    private int mx;
    private int my;

    public boolean leftClicked;
    public boolean leftPressed;
    private boolean m0p;

    public boolean rightClicked;
    public boolean rightPressed;
    private boolean m1p;

    private Canvas canvas;
    public boolean onScreen;

    public InputHandler(Canvas canvas) {
        this.canvas = canvas;
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
    }

    public void tick() {
        leftClicked = !leftPressed && m0p;
        rightClicked = !rightPressed && m1p;
        leftPressed = m0p;
        rightPressed = m1p;
        x = mx;
        y = my;
        for (Key key : keys) key.tick();
    }


    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) { toggle(e, true); }
    public void keyReleased(KeyEvent e) { toggle(e, false); }

    public void mouseClicked(MouseEvent e) {
        mx = e.getX() / GridPacer.SCALE;
        my = e.getY() / GridPacer.SCALE;

        onScreen = true;
    }

    public void mousePressed(MouseEvent e) {
        mx = e.getX() / GridPacer.SCALE;
        my = e.getY() / GridPacer.SCALE;

        m0p = e.getButton() == 1;
        m1p = e.getButton() == 3;

        onScreen = true;
    }

    public void mouseReleased(MouseEvent e) {
        mx = e.getX() / GridPacer.SCALE;
        my = e.getY() / GridPacer.SCALE;

        m0p = false;
        m1p = false;

        onScreen = mx >= 0 && my >= 0 && mx < canvas.getWidth() && my < canvas.getHeight();
    }

    public void mouseEntered(MouseEvent e) { onScreen = true; }
    public void mouseExited(MouseEvent e) { onScreen = false; }

    private void toggle(KeyEvent e, boolean pressed) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) start.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_1 || e.getKeyCode() == KeyEvent.VK_NUMPAD1) n0.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_2 || e.getKeyCode() == KeyEvent.VK_NUMPAD2) n1.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_3 || e.getKeyCode() == KeyEvent.VK_NUMPAD3) n2.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_4 || e.getKeyCode() == KeyEvent.VK_NUMPAD4) n3.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_5 || e.getKeyCode() == KeyEvent.VK_NUMPAD5) n4.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_6 || e.getKeyCode() == KeyEvent.VK_NUMPAD6) n5.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_7 || e.getKeyCode() == KeyEvent.VK_NUMPAD7) n6.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_8 || e.getKeyCode() == KeyEvent.VK_NUMPAD8) n7.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_9 || e.getKeyCode() == KeyEvent.VK_NUMPAD9) n8.toggle(pressed);
    }

    public class Key {
        private int absorbs;
        private int presses;

        public boolean clicked;
        public boolean down;

        public Key() { keys.add(this); }

        public void tick() {
            if (presses > absorbs) {
                absorbs++;
                clicked = true;
            } else clicked = false;
        }

        public void toggle(boolean pressed) {
            if (pressed != down) down = pressed;
            if (pressed) presses++;
        }

    }

}