package game.gfx.gui;

import game.Game;
import game.InputHandler;

import java.util.ArrayList;
import java.util.List;

public class GuiHelpWindow extends GuiTextPanel {

    public GuiHelpWindow(int x, int y) {
        super("", x, y);

        List<String> controls = new ArrayList<String>();
        controls.add("Movement:");
        controls.add("w - forward");
        controls.add("s - backwards");
        controls.add("a - left");
        controls.add("d - right");
        controls.add("Interaction:");
        controls.add("space - use");
        controls.add("q - eat apple");
        controls.add("e - eat berry");
        controls.add("Miscellaneous:");
        controls.add("z - toggle sound");
        controls.add("i - inventory");
        controls.add("ESC - pause");
        controls.add("H - Help Window");

        setFormattedText(controls, ((int)(Game.WIDTH - Game.HEIGHT * 0.4)) >> 3);
        visible = false;
    }

    public void tick() {
        if (InputHandler.getInstance(null).helpWindow.clicked) setVisible(!visible);
    }

}