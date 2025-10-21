package game.gfx.gui.menu;

import game.Game;
import game.InputHandler;
import game.gfx.CreditsScreen;
import game.gfx.gui.GuiManager;
import game.gfx.gui.GuiTextPanel;

public class GuiMainMenu extends GuiMenu implements Callback {
    private Game game;

    public GuiMainMenu(Game game) {
        super(Game.WIDTH / 3 + 15, Game.HEIGHT / 3);

        panels.add(new GuiTextPanel("Resume", x, y, GuiManager.FONT_COL, GuiManager.MENU_COL_SEL));
        panels.add(new GuiTextPanel("Controls", x, y + 8 * 3, GuiManager.FONT_COL, GuiManager.PANEL_COL));
        panels.add(new GuiTextPanel("Exit", x, y + 8 * 6, GuiManager.FONT_COL, GuiManager.MENU_COL));

        callback = this;

        this.game = game;
        currentCell = 0;
        visible = false;
        changed = true;
    }

    public void tick() {
        super.tick();

        if (InputHandler.getInstance(null).pauseMenu.clicked) setVisible(!visible);
    }

    public boolean result(int result) {
        switch (result) {
            case 0:
                GuiManager.isMenuOpen = false;
                break;
            case 1:
                GuiManager.getInstance().get("helpWindow").show();
                return true;
            case 2:
                System.exit(0);
                return true;
        }

        return false;
    }

    public void setVisible(boolean v) {
        super.setVisible(v);
        GuiManager.isMenuOpen = v;
        GuiManager.pauseMenu = v;
    }
}