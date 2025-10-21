package game.entity.mob.hostile;

import game.entity.Entity;
import game.entity.Team;
import game.entity.item.Inventory;
import game.entity.item.Item;
import game.entity.item.ItemEntity;
import game.entity.item.equipment.EquipmentItem;
import game.entity.item.resource.Resource;
import game.entity.item.resource.ResourceItem;
import game.entity.mob.Mob;
import game.entity.mob.Player;
import game.entity.particle.FlowTextParticle;
import game.gfx.Font;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;

import java.util.ArrayList;
import java.util.List;

public class Bird extends Mob {

    private Inventory inventory = new Inventory();
    private int xt;
    private int yt;
    private double angle;
    private int speed;
    private int xTarget;
    private int yTarget;
    private double angleInc;

    public Bird() {
        xr = 2;
        yr = 2;
        xt = 0;
        yt = 7;
        angle = 0.0;
        speed = 1;
        xTarget = 0;
        yTarget = 0;
        angleInc = 10.0;
        health = 20;
        team = Team.HOSTILE;
    }

    public void tick() {
        super.tick();

        if (tickTime / 100 % 2 == 0) angleInc = 7 + random.nextInt(10);

        if (tickTime / 500 % 2 == 0) {
            xTarget = 80 + random.nextInt((level.getWidth() - 5) << 4);
            yTarget = 80 + random.nextInt((level.getHeight() - 5) << 4);
        }

        if (level.getPlayer() != null && !level.getPlayer().isRemoved()) {
            int xd = level.getPlayer().getX() - x;
            int yd = level.getPlayer().getY() - y;
            int vr = viewRadius << 4;

            if (xd * xd + yd * yd <= vr * vr) {
                xTarget = level.getPlayer().getX();
                yTarget = level.getPlayer().getY();
            }
        }

        int rr = 80;
        List<Entity> entities = level.getEntities(x - rr, y - rr, x + rr, y + rr, null);
        for (Entity e : entities) {
            if (e instanceof ItemEntity) {
                xTarget = e.getX();
                yTarget = e.getY();
            }
        }

        double testAngle = (angle + angleInc) <= 360 ? angle + angleInc : (angle - angleInc) - 360;
        double testAngle1 = (angle - angleInc) >= 0 ? angle - angleInc : 360 + (angle - angleInc);

        int xx = x + (int) Math.round(Math.cos(Math.toRadians(testAngle))) * speed;
        int yy = y + (int) Math.round(Math.sin(Math.toRadians(testAngle))) * speed;

        int xd = xTarget - xx;
        int yd = yTarget - yy;

        int dist = xd * xd + yd * yd;

        int xx1 = x - (int) Math.round(Math.cos(Math.toRadians(testAngle1))) * speed;
        int yy1 = y - (int) Math.round(Math.sin(Math.toRadians(testAngle1))) * speed;

        int xd1 = xTarget - xx1;
        int yd1 = yTarget - yy1;

        int dist1 = xd1 * xd1 + yd1 * yd1;

        if (dist < dist1) angle = testAngle;
        else if (dist > dist1) angle = testAngle1;

        if (System.currentTimeMillis() % 2 == 0) xt = xt < 3 ? xt + 1 : 0;

        int xa = (int) Math.round(Math.cos(Math.toRadians(angle))) * speed;
        int ya = (int) Math.round(Math.sin(Math.toRadians(angle))) * speed;

        move(xa, ya);
    }

    public void render(Screen screen) {
        int xo = x - 4;
        int yo = y - 4;
        int col = PaletteHelper.getColor(-1, 0, 421 + level.getNumber() * 10, 532);
        if (hurtTime > 0) col = PaletteHelper.getColor(-1, 555, 555, 555);
        screen.render(angle * speed + 90.0, xo, yo, xt * 8, yt * 8, 8, 8, col, 0);
    }

    public void touchItem(ItemEntity e) {
        if (e.isRemoved() || isRemoved()) return;
        e.take(this);
        inventory.add(e.getItem());
        level.add(new FlowTextParticle("+1", x, y - 8, Font.YELLOW_COL));
    }

    public void touchedBy(Entity e) {
        if (e instanceof Player) e.hurt(this, level.getNumber() + 1, dir);
        super.touchedBy(e);
    }

    public void die() {
        super.die();

        for (Item item : inventory.getItems()) {
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                for (int i = 0; i < ri.getCount(); i++) {
                    level.add(new ItemEntity(new ResourceItem(ri.getResource()), x + random.nextInt(11) - 5, y + random.nextInt(11) - 5));
                }
            }

            if (item instanceof EquipmentItem) {
                EquipmentItem ei = (EquipmentItem) item;
                level.add(new ItemEntity(new EquipmentItem(ei.getEquipment()), x + random.nextInt(11) - 5, y + random.nextInt(11) - 5));
            }
        }
    }

    public boolean ignoreBlocks() { return true; }

}