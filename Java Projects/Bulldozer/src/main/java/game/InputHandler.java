package game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.*;
import java.util.List;

public class InputHandler implements KeyListener {

    private static final List<Key> keys = new ArrayList<Key>();
    public Key up = new Key(KeyEvent.VK_W).key(KeyEvent.VK_NUMPAD8).key(KeyEvent.VK_UP);
    public Key down = new Key(KeyEvent.VK_S).key(KeyEvent.VK_NUMPAD2).key(KeyEvent.VK_DOWN);
    public Key left = new Key(KeyEvent.VK_A).key(KeyEvent.VK_NUMPAD4).key(KeyEvent.VK_LEFT);
    public Key right = new Key(KeyEvent.VK_D).key(KeyEvent.VK_NUMPAD6).key(KeyEvent.VK_RIGHT);
    public Key restart = new Key(KeyEvent.VK_R);

    public InputHandler(Canvas canvas) { canvas.addKeyListener(this); }

    public void tick() {
        for (Key key : keys) {
            key.tick();
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) { toggle(e.getKeyCode(), true); }
    public void keyReleased(KeyEvent e) { toggle(e.getKeyCode(), false); }

    private void toggle(int keyCode, boolean pressed) {
        for (Key key : keys) {
            key.toggle(keyCode, pressed);
        }
    }

    public class Key {
        private int absorbs;
        private int presses;
        public Set<Integer> keyCodes = new HashSet<Integer>();

        public boolean down;
        public boolean clicked;

        public Key(int key) {
            keys.add(this);
            keyCodes.add(key);
        }

        public Key key(int key) {
            keyCodes.add(key);
            return this;
        }

        public void tick() {
            if (presses > absorbs) {
                absorbs++;
                clicked = true;
            } else clicked = false;
        }

        public void toggle(int keyCode, boolean pressed) {
            if (!keyCodes.contains(keyCode)) return;

            down = pressed;
            if (pressed) presses++;
        }
    }
}
