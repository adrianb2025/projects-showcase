package game.gfx.gui;

import game.Game;
import game.gfx.Screen;
import game.gfx.gui.menu.GuiMainMenu;
import game.gfx.gui.menu.GuiMenu;
import game.gfx.gui.minimap.GuiMinimap;
import game.gfx.util.PaletteHelper;
import game.level.Level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class GuiManager {

    private static final ConcurrentLinkedQueue<GuiPanel> queuedPanels = new ConcurrentLinkedQueue<GuiPanel>();
    private static final ConcurrentMap<String, GuiPanel> panels = new ConcurrentHashMap<String, GuiPanel>();
    private static GuiManager guiManager = null;

    public static final int PANEL_COL = PaletteHelper.getColor(-1, 1, 30, 445);
    public static final int FONT_COL = PaletteHelper.getColor(-1, -1, -1, 555);
    public static final int MENU_COL = PaletteHelper.getColor(-1, 1, 30, 445);
    public static final int MENU_COL_SEL = PaletteHelper.getColor(-1, 1, 141, 445);

    public static boolean pauseMenu;
    public static boolean isMenuOpen;

    private boolean findSamePanel(GuiPanel toFind) {
        for (GuiPanel panel : panels.values()) {
            if (panel.getX() == toFind.getX() && panel.getY() == toFind.getY()) {
                System.out.println("Duplicate Panel: " + toFind.toString());
                return false;
            }
        }

        return false;
    }

    public void add(GuiPanel panel, String name) {
        if (!findSamePanel(panel)) panels.put(name, panel);
        else System.out.println("Duplicate Panel: " + name);
    }

    public void tick() {
        List<String> strings = new LinkedList<String>();

        Set<String> keys = panels.keySet();
        for (String key : keys) {
            GuiPanel panel = panels.get(key);
            if (panel.closed) strings.add(key);
            else panel.tick();
        }

        for (String s : strings) {
            System.out.println("Removed: " + s);
            panels.remove(s);
        }
    }

    public void render(Screen screen) {
        for (GuiPanel panel : panels.values()) {
            panel.render(screen);
        }
    }

    public void remove(String name) {
        GuiPanel p = panels.get(name);
        if (p != null) p.close();
    }

    public static GuiManager getInstance() {
        if (guiManager == null) guiManager = new GuiManager();
        return guiManager;
    }

    public void initDefaultGUI(Level level) {
        panels.clear();

        add(new GuiStatusPanel(10, 220, 5, 3, 123, PaletteHelper.getColor(300, 555, 311, -1)), "health");
        add(new GuiStatusPanel(100, 220, 3, 3, 123, PaletteHelper.getColor(430, 430, 540, -1)), "money");
        add(new GuiSpeedIndicator(150, 220, PaletteHelper.getColor(531, 531, 531, -1), level.getSpriteCollector()), "speed");
        add(new GuiInventory(1, 5, level.getPlayer()), "inventory");
        add(new GuiMenu(50, 100), "menu");
        add(new GuiHelpWindow(1, 5), "helpWindow");
        add(new GuiMainMenu(null), "mainMenu");

        add(new GuiMinimap(Game.WIDTH - (level.getWidth() + 8 * 5) / 2, 8 / 2, level), "minimap");
    }

    public GuiPanel get(String name) { return panels.get(name); }
    public void addToQueue(GuiPanel panel) { queuedPanels.add(panel); }

}