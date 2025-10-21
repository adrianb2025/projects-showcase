package game.gfx.gui.menu;

import game.InputHandler;
import game.gfx.Screen;
import game.gfx.gui.GuiManager;
import game.gfx.gui.GuiPanel;
import game.gfx.gui.GuiTextPanel;

import java.util.LinkedList;
import java.util.List;

public class GuiMenu extends GuiPanel {

    protected List<GuiTextPanel> panels = new LinkedList<GuiTextPanel>();
    protected Callback callback;
    protected int currentCell = -1;

    public GuiMenu(int x, int y) {
        this.x = x;
        this.y = y;
        visible = false;
    }

    public void showMenu(List<String> options, Callback callback) {
        if (options == null || callback == null || options.size() == 0) {
            System.err.println("[GuiMenu] Null Pointer");
            return;
        }

        if (visible) callback.result(-1);

        GuiManager.isMenuOpen = true;

        this.callback = callback;
        int i = 0;
        panels.clear();

        for (String o : options) {
            panels.add(new GuiTextPanel(o, x, y + (i * (8 * 3)), GuiManager.FONT_COL, (i++ == 0) ? GuiManager.MENU_COL_SEL : GuiManager.MENU_COL));
        }

        currentCell = 0;
        changed = true;
        visible = true;

    }

    public void tick() {
        if (!visible) return;

        if (InputHandler.getInstance(null).down.clicked) selectNext();
        if (InputHandler.getInstance(null).up.clicked) selectPrev();
        if (InputHandler.getInstance(null).action.clicked) select();
    }

    public void selectPrev() {
        if (!visible) return;

        int lastCell = currentCell;
        currentCell--;

        if (currentCell < 0) currentCell = panels.size() - 1;
        panels.get(lastCell).setPanelColor(GuiManager.MENU_COL);
        panels.get(currentCell).setPanelColor(GuiManager.MENU_COL_SEL);
        changed = true;
    }

    public void selectNext() {
        if (!visible) return;

        int lastCell = currentCell;
        currentCell++;

        if (currentCell >= panels.size()) currentCell = 0;
        panels.get(lastCell).setPanelColor(GuiManager.MENU_COL);
        panels.get(currentCell).setPanelColor(GuiManager.MENU_COL_SEL);
        changed = true;
    }

    public void select() {
        if (!visible) return;
        if (!callback.result(currentCell)) setVisible(false);
    }

    public void render(Screen screen) {
        if (!visible) return;
        for (GuiTextPanel panel : panels) {
            panel.render(screen);
        }
    }
}