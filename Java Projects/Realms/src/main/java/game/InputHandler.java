package game;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    private static final List<Key> keys = new ArrayList<Key>();
    private static InputHandler instance;
    public Key up = new Key();
    public Key down = new Key();
    public Key left = new Key();
    public Key right = new Key();
    public Key mouse = new Key();
    public Key action = new Key();
    public Key inventory = new Key();
    public Key pauseMenu = new Key();
    public Key helpWindow = new Key();
    public Key berryUse = new Key();
    public Key appleUse = new Key();
    public Key minimap = new Key();
    public Key sound = new Key();

    private int mouseX;
    private int mouseY;

    private Game game;

    public static InputHandler getInstance(Game game) {
        if (instance == null) instance = new InputHandler(game);
        return instance;
    }

    private InputHandler(Game game) {
        this.game = game;
        game.addKeyListener(this);
        game.addMouseListener(this);
        game.addMouseMotionListener(this);
    }

    public void init(Game game) {
        this.game = game;
        game.addKeyListener(this);
        game.addMouseListener(this);
        game.addMouseMotionListener(this);
    }

    public void tick() {
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
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) { mouse.toggle(true); }
    public void mouseReleased(MouseEvent e) { mouse.toggle(false); }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public int getMouseX() { return mouseX / Game.SCALE + game.getXScroll(); }
    public int getMouseY() { return mouseY / Game.SCALE + game.getYScroll(); }

    private void toggle(KeyEvent e, boolean pressed) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_NUMPAD0 || e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_C || e.getKeyCode() == KeyEvent.VK_ENTER) action.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_ESCAPE) pauseMenu.toggle(pressed);

        if (e.getKeyCode() == KeyEvent.VK_Q) appleUse.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_E) berryUse.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_Z) sound.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_M) minimap.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_H) helpWindow.toggle(pressed);
        if (e.getKeyCode() == KeyEvent.VK_I) inventory.toggle(pressed);
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