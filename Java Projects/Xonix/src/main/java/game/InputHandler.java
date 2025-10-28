package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {

    private static final List<Key> keys = new ArrayList<>();
    public Key up = new Key(KeyEvent.VK_W, KeyEvent.VK_UP);
    public Key down = new Key(KeyEvent.VK_S, KeyEvent.VK_DOWN);
    public Key left = new Key(KeyEvent.VK_A, KeyEvent.VK_LEFT);
    public Key right = new Key(KeyEvent.VK_D, KeyEvent.VK_RIGHT);

    public InputHandler(Canvas canvas) {
        canvas.addKeyListener(this);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        toggle(e, true);
    }

    public void keyReleased(KeyEvent e) {
        toggle(e, false);
    }

    public void tick() {
        for (Key key : keys) {
            key.tick();
        }
    }

    private void toggle(KeyEvent e, boolean pressed) {
        for (Key key : keys) {
            key.toggle(e.getKeyCode(), pressed);
        }
    }

    public class Key {
        private List<int[]> keyCodes = new ArrayList<>();
        private boolean pressedLastTick;

        public boolean clicked;
        public boolean down;

        public Key(int... codes) {
            keys.add(this);
            keyCodes.add(codes);
        }

        public void tick() {
            clicked = pressedLastTick && !down;
            pressedLastTick = down;
        }

        public void toggle(int key, boolean pressed) {
            for (int[] codes : keyCodes) {
                for (int k : codes) {
                    if (k == key) {
                        down = pressed;
                        return;
                    }
                }
            }
        }
    }

}