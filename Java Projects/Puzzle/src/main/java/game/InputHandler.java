package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    private static final List<Key> keys = new ArrayList<Key>();
    public Key action = new Key();
    public Key mouse = new Key();
    public int x;
    public int y;

    public InputHandler(Canvas canvas) {
        canvas.addKeyListener(this);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
    }

    public void releaseAll() {
        for (Key key : keys) {
            key.down = false;
        }
    }

    public void tick() {
        for (Key key : keys) {
            key.tick();
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) { toggle(e, true); }
    public void keyReleased(KeyEvent e) { toggle(e, false); }
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) { mouse.toggle(true); }
    public void mouseReleased(MouseEvent e) { mouse.toggle(false); }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private void toggle(KeyEvent e, boolean pressed) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) action.toggle(pressed);
    }

    public void mouseDragged(MouseEvent e) {
        x = e.getX() / PuzzleComponent.SCALE;
        y = e.getY() / PuzzleComponent.SCALE;
    }

    public void mouseMoved(MouseEvent e) {
        x = e.getX() / PuzzleComponent.SCALE;
        y = e.getY() / PuzzleComponent.SCALE;
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