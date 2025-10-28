package game.entity.mob;

import game.Game;
import game.InputHandler;
import game.entity.CharacterStats;
import game.entity.Entity;
import game.entity.Team;
import game.entity.item.Inventory;
import game.entity.item.Item;
import game.entity.item.ItemEntity;
import game.entity.item.equipment.Equipment;
import game.entity.item.equipment.EquipmentItem;
import game.entity.item.equipment.EquipmentType;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.particle.FlowTextIconParticle;
import game.entity.particle.FlowTextParticle;
import game.entity.weapon.Arrow;
import game.entity.weapon.ArrowType;
import game.entity.weapon.Weapon;
import game.gfx.Font;
import game.gfx.Screen;
import game.gfx.gui.GuiInventory;
import game.gfx.gui.GuiManager;
import game.gfx.util.PaletteHelper;
import game.level.Level;
import game.level.tile.Tile;

import java.util.List;

public class Player extends Mob {

    private int col;
    private Game game;
    private int score = 1000;
    private int clearFogRadius = 4;
    private int shootDelay = 10;
    private Item activeItem;
    private EquipmentItem weapon;
    private EquipmentItem shoes;
    private EquipmentItem armor;
    private Inventory inventory = new Inventory();
    private Inventory averagedPickups = new Inventory();
    private long collectTime;

    public Player(Game game) {
        this.game = game;
        team = Team.PLAYER;
        col = PaletteHelper.getColor(-1, 100, 522, 555);
        touchItem(new ItemEntity(new EquipmentItem(Equipment.basicBow), 0, 0));
        touchItem(new ItemEntity(new ResourceItem(Resource.apple, 10), 0, 0));
    }

    public void init(Level level) {
        super.init(level);
        health = maxHealth = currentStats.getEndurance();
    }

    public void tick() {
        int xa = 0;
        int ya = 0;

        InputHandler input = InputHandler.getInstance(null);

        if (input.up.down) ya--;
        if (input.down.down) ya++;
        if (input.left.down) xa--;
        if (input.right.down) xa++;
        if (input.action.clicked) take();

        if (input.appleUse.clicked) {
            if (health < currentStats.getEndurance() && inventory.removeResource(Resource.apple, 1)) {
                health = Math.min(currentStats.getEndurance(), health + 1);
                level.add(new FlowTextParticle("+1", x, y, Font.GREEN_COL));
            }
        }

        if (input.berryUse.clicked) {
            if (health < currentStats.getEndurance() && inventory.removeResource(Resource.berry, 1)) {
                health = Math.min(currentStats.getEndurance(), health + 1);
                level.add(new FlowTextParticle("+1", x, y, Font.GREEN_COL));
            }
        }

        col = PaletteHelper.getColor(-1, 100, 522, 555);

        if (input.mouse.down && activeItem == null && weapon != null) {
            int xd = input.getMouseX() - x;
            int yd = input.getMouseY() - y;
            double m = Math.sqrt(xd * xd + yd * yd);
            double vx = xd / m;
            double vy = yd / m;
            if (tickTime % currentStats.getAttackDelay() == 0) {
                boolean isFireArrow = random.nextInt(5) == 0;
                Weapon.fire(isFireArrow ? ArrowType.FIRE : ArrowType.BASIC, team, x, y, vx, vy, currentStats.getStrength() + (isFireArrow ? currentStats.getStrength() : 0), level);
            }

            col = PaletteHelper.getColor(-1, 111, 444, 555);
        }

        move(xa, ya);

        if (System.currentTimeMillis() - collectTime > 300) {
            showAveragedPickups();
            collectTime = System.currentTimeMillis();
        }

        super.tick();
    }

    private void showAveragedPickups() {
        if (averagedPickups.getItems().size() == 0) return;
        Item item = averagedPickups.getItems().get(0);
        if (item != null) {
            int count = item instanceof ResourceItem ? ((ResourceItem) item).getCount() : 1;
            level.add(new FlowTextIconParticle("+" + count, x - 8, y - 8, Font.YELLOW_COL, item.getXSprite(), item.getYSprite(), item.getColor()));
            averagedPickups.getItems().remove(item);
        }
    }

    public void updateStats() {
        GuiInventory guiInventory = (GuiInventory) GuiManager.getInstance().get("inventory");
        if (guiInventory != null) {
            int speed;
            int slowPeriod = currentStats.getSlowPeriod() + groundSlowPeriod;
            if (slowPeriod < 3) speed = 30;
            else if (slowPeriod < 10) speed = 60;
            else if (slowPeriod < 40) speed = 90;
            else speed = 120;

            guiInventory.setSpeed(speed);
            guiInventory.setDef(currentStats.getDefense());
            guiInventory.setSta(currentStats.getEndurance());
            guiInventory.setStr(currentStats.getStrength());
        }
    }

    public void updateEquipment() {
        currentStats = defaultStats.mergeStats(new CharacterStats());

        weapon = inventory.findEquipmentByType(EquipmentType.WEAPON);
        if (weapon != null) currentStats = currentStats.mergeStats(weapon.getBonusStats());

        shoes = inventory.findEquipmentByType(EquipmentType.SHOES);
        if (shoes != null) currentStats = currentStats.mergeStats(shoes.getBonusStats());

        armor = inventory.findEquipmentByType(EquipmentType.ARMOR);
        if (armor != null) currentStats = currentStats.mergeStats(armor.getBonusStats());
    }

    public boolean take() {
        boolean done = false;

        int yo = -2;
        int range = 12;

        if (dir == 0 && interact(x - 8, y + 4 + yo, x + 8, y + range + yo)) done = true;
        if (dir == 1 && interact(x - 8, y - range + yo, x + 8, y - 4 + yo)) done = true;
        if (dir == 3 && interact(x + 4, y - 8 + yo, x + range, y + 8 + yo)) done = true;
        if (dir == 2 && interact(x - range, y - 8 + yo, x - 4, y + 8 + yo)) done = true;

        if (activeItem != null) {
            int xt = x >> 4;
            int yt = (y + yo) >> 4;
            int r = 12;


            if (dir == 0) yt = (y + r + yo) >> 4;
            if (dir == 1) yt = (y - r + yo) >> 4;
            if (dir == 2) xt = (x - r) >> 4;
            if (dir == 3) xt = (x + r) >> 4;

            if (xt >= 0 && yt >= 0 && xt < level.getWidth() && yt < level.getHeight()) {
                level.getTile(xt, yt).interact(level, xt, yt, this, activeItem, dir);

                activeItem.interactOn(level.getTile(xt, yt), level, xt, yt, this, dir);
                if (activeItem.isDepleted()) activeItem = null;
            }
        }

        return done;
    }

    private boolean interact(int x0, int y0, int x1, int y1) {
        List<Entity> entities = level.getEntities(x0, y0, x1, y1, null);
        for (Entity e : entities) {
            e.interact(activeItem, this, dir);
        }

        return false;
    }

    public void touchedBy(Entity e) {
        if (e.getTeam() != team) e.touchedBy(this);
    }

    public void render(Screen screen) {
        int xt = 0;
        int yt = 18;

        int flip1 = (walkDist >> 3) & 1;
        int flip2 = (walkDist >> 3) & 1;

        if (dir == 1) xt += 2;

        if (dir > 1) {
            flip1 = 0;
            flip2 = ((walkDist >> 4) & 1);

            if (dir == 2) flip1 = 1;

            xt += 4 + ((walkDist >> 3) & 1) * 2;
        }

        int xo = x - 8;
        int yo = y - 11;

        if (isSwimming()) {
            yo += 4;
            int waterCol = PaletteHelper.getColor(-1, -1, -1, 444);
            if (tickTime / 8 % 2 == 0) waterCol = PaletteHelper.getColor(-1, 444, -1, -1);
            screen.render(xo + 0, yo + 3, 5 * 8, 13 * 8, waterCol, 0);
            screen.render(xo + 8, yo + 3, 5 * 8, 13 * 8, waterCol, 1);
        }

        int col = PaletteHelper.getColor(-1, 100, 0, 542);

        if (hurtTime > 0) col = PaletteHelper.getColor(-1, 555, 555, 555);

        screen.render(xo + 8 * flip1, yo + 0, xt * 8, yt * 8, col, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, (xt + 1) * 8, yt * 8, col, flip1);

        if (!isSwimming()) {
            screen.render(xo + 8 * flip2, yo + 8, xt * 8, (yt + 1) * 8, col, flip2);
            screen.render(xo + 8 - 8 * flip2, yo + 8, (xt + 1) * 8, (yt + 1) * 8, col, flip2);
        }
    }

    public boolean ignoreBlocks() { return shoes != null && shoes.getEquipment() == Equipment.masterShoes; }

    public void touchItem(ItemEntity e) {
        if (e.isRemoved()) return;

        e.take(this);
        inventory.add(e.getItem());
        averagedPickups.add(e.getItem());

        if (e.getItem() instanceof EquipmentItem) updateEquipment();

        if (e.getItem() instanceof ResourceItem) {
            ResourceItem item = (ResourceItem) e.getItem();

            if (item.getResource() == Resource.health) {
                CharacterStats healthStat = new CharacterStats(0, item.getCount(), 0, 0, 0);

                currentStats = currentStats.mergeStats(healthStat);
                defaultStats = defaultStats.mergeStats(healthStat);
            }
        }
    }

    public boolean findStartPos(Level level) {
        while (true) {
            int x = random.nextInt(level.getWidth());
            int y = random.nextInt(level.getHeight());
            while (level.getTile(x, y) == Tile.grass) {
                this.x = x * 16 + 8;
                this.y = y * 16 + 8;
                return true;
            }
        }
    }

    public boolean canRegenerate() { return true; }
    public int getScore() { return score; }
    public boolean canSwim() { return true; }
    public int getClearFogRadius() { return Math.min(clearFogRadius, 10); }
    public Inventory getInventory() { return inventory; }

}