package game.gfx.gui;

import game.InputHandler;
import game.entity.item.Inventory;
import game.entity.item.Item;
import game.entity.item.equipment.Equipment;
import game.entity.item.equipment.EquipmentItem;
import game.entity.item.equipment.EquipmentType;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.mob.Player;
import game.gfx.Font;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

import java.util.LinkedList;
import java.util.List;

public class GuiInventory extends GuiPanel {


    private int str;
    private int sta;
    private int def;
    private int speed;

    GuiPanel[] panels = new GuiPanel[7];

    private Item weapon;
    private Item armor;
    private Item boots;
    private ResourceItem apple;
    private ResourceItem berry;

    private Player player;

    public GuiInventory(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        List<String> stats = new LinkedList<String>();

        stats.add("Strength");
        stats.add("Endurance");
        stats.add("Defense");
        stats.add("Speed");


        int x1 = x - 1;

        panels[0] = new GuiPanel(x1, y, 14, 15);
        panels[1] = new GuiTextPanel(stats, x - 1, y + 135, 15);
        panels[2] = new GuiPanel(x1 + 8, y + 8, 5, 6, PaletteHelper.getColor(-1, 1, 111, 445));
        panels[3] = new GuiPanel(x1 + 8 * 8, y + 8, 5, 6, PaletteHelper.getColor(-1, 1, 111, 445));
        panels[4] = new GuiPanel(x1 + 8 * 8, y + 9 * 8, 5, 5, PaletteHelper.getColor(-1, 1, 111, 445));
        panels[5] = new GuiPanel(x1 + 8, y + 9 * 8, 1, 1, PaletteHelper.getColor(-1, 1, 111, 445));
        panels[6] = new GuiPanel(x1 + 8, y + 12 * 8, 1, 1, PaletteHelper.getColor(-1, 1, 111, 445));

        visible = false;
        changed = true;
    }

    public void render(Screen screen) {
        if (!visible) return;

        for (GuiPanel panel : panels) {
            panel.render(screen);
        }

        int posX = panels[1].getX() + 12 * 8 + ((speed < 100) ? 8 : 0);
        int posY = panels[1].getY() + 4 * 8;

        Font.drawToBitmap("" + speed + "%", screen, posX, posY, GuiManager.FONT_COL, screen.getViewPort());

        posX = panels[1].getX() + 14 * 8 + ((str < 10) ? 8 : 0);
        posY = panels[1].getY() + 8;

        Font.drawToBitmap("" + str, screen, posX, posY, GuiManager.FONT_COL, screen.getViewPort());

        posX = panels[1].getX() + 14 * 8 + ((sta < 10) ? 8 : 0);
        posY += 8;

        Font.drawToBitmap("" + sta, screen, posX, posY, GuiManager.FONT_COL, screen.getViewPort());

        posX = panels[1].getX() + 14 * 8 + ((def < 10) ? 8 : 0);
        posY += 8;

        Font.drawToBitmap("" + def, screen, posX, posY, GuiManager.FONT_COL, screen.getViewPort());

        Inventory inventory = player.getInventory();

        EquipmentItem item = inventory.findEquipmentByType(EquipmentType.WEAPON);
        if (item != null) item.renderInventory(screen, x + 16, y + 16);

        item = inventory.findEquipmentByType(EquipmentType.ARMOR);
        if (item != null) item.renderInventory(screen, x + 5 * 16, y + 16);

        item = inventory.findEquipmentByType(EquipmentType.SHOES);
        if (item != null) item.renderInventory(screen, x + 5 * 16, y + 77);

        ResourceItem resourceItem = inventory.findResource(Resource.getResourceByName("apple"));
        if (resourceItem != null) resourceItem.renderInventory(screen, x + 10, y + 77, x + 4 * 8, y + 10 * 8);

        resourceItem = inventory.findResource(Resource.getResourceByName("berry"));
        if (resourceItem != null) resourceItem.renderInventory(screen, x + 10, y + 103, x + 4 * 8, y + 13 * 8);
    }

    public void tick() {
        if (InputHandler.getInstance(null).inventory.clicked) setVisible(!visible);
    }

    public void setStr(int str) {
        if (this.str == str) return;
        this.str = str;
        changed = true;
    }

    public void setSta(int sta) {
        if (this.sta == sta) return;
        this.sta = sta;
        changed = true;
    }
    public void setDef(int def) {
        if (this.def == def) return;
        this.def = def;
        changed = true;
    }

    public void setSpeed(int speed) {
        if (this.speed == speed) return;
        this.speed = speed;
        changed = true;
    }

}