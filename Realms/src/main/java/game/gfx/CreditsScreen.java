package game.gfx;

import game.Game;
import game.InputHandler;
import game.gfx.gui.GuiManager;
import game.gfx.util.BitmapHelper;

import java.util.ArrayList;
import java.util.List;

public class CreditsScreen {

    private static CreditsScreen i;
    private boolean show;
    private List<String> lines = new ArrayList<String>();
    private int textY = Game.HEIGHT;
    private  long tick;

    public static CreditsScreen getInstance() {
        if (i == null) i = new CreditsScreen();
        return i;
    }

    public CreditsScreen() {
        lines.add("Game Credits");
        lines.add("Programming:");
        lines.add("Chilla");
        lines.add(" ");
        lines.add("Discord:");
        lines.add("wchilla");
        lines.add(" ");
        lines.add("E-mail:");
        lines.add("gelembeza@gmail.com");
        lines.add(" ");
        lines.add("Special thanks to everyone for playing");
    }

    public void tick() {
        if (InputHandler.getInstance(null).pauseMenu.clicked) show = false;

        if (tick < System.currentTimeMillis()) {
            tick = System.currentTimeMillis() + 50;
            textY--;
            if (textY < 0) show = false;
        }
    }

    public void render(Screen screen) {
        BitmapHelper.fill(screen.getViewPort(), 0x000000);

        for (int i = 0; i < lines.size(); i++) {
            Font.draw(lines.get(i), screen, 0, textY + (i * 8), GuiManager.FONT_COL);
        }
    }

    public void show() {
        show = true;
        textY = Game.HEIGHT;
    }

    public boolean isVisible() { return show; }

}