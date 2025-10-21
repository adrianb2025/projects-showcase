package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    private static final List<Key> keys = new ArrayList<Key>();
    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();

    public boolean leftClicked;
    public boolean leftPressed;
    private boolean m0p;

    public boolean rightClicked;
    public boolean rightPressed;
    private boolean m1p;

    public int x;
    public int y;
    private int mx;
    private int my;

    private Canvas canvas;
    public boolean onScreen;

    public InputHandler(Canvas canvas) {
        this.canvas = canvas;
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    public void tick() {
        leftClicked = !leftPressed && m0p;
        rightClicked = !rightPressed && m1p;
        leftPressed = m0p;
        rightPressed = m1p;
        x = mx;
        y = my;

        for (Key key : keys) {
            key.tick();
        }
    }

    public void releaseAll() {
        for (Key key : keys) {
            key.down = false;
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) { toggle(e, true); }
    public void keyReleased(KeyEvent e) { toggle(e, false); }

    public void mouseClicked(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        onScreen = true;
    }

    public void mousePressed(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        m0p = e.getButton() == 1;
        m1p = e.getButton() == 3;

        onScreen = true;
    }

    public void mouseReleased(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        m0p = false;
        m1p = false;

        onScreen = mx >= 0 && my >= 0 && mx < canvas.getWidth() && my < canvas.getHeight();
    }

    public void mouseEntered(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        onScreen = true;
    }

    public void mouseExited(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        onScreen = false;
    }

    public void mouseDragged(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        onScreen = true;
    }

    public void mouseMoved(MouseEvent e) {
        mx = e.getX();
        my = e.getY();

        onScreen = true;
    }

    private void toggle(KeyEvent e, boolean pressed) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed);
    }

    public class Key {
        private int absorbs;
        private int presses;

        public boolean down;
        public boolean clicked;

        public Key() { keys.add(this); }

        public void tick() {
            if (presses > absorbs) {
                absorbs++;
                clicked = true;
            } else clicked = false;
        }

        public void toggle(boolean pressed) {
            down = pressed;
            if (pressed) presses++;
        }

    }

}