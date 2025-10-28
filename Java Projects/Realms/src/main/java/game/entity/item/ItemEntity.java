package game.entity.item;

import game.entity.Entity;
import game.entity.mob.Player;
import game.entity.particle.SmashParticle;
import game.gfx.Screen;
import game.gfx.util.PaletteHelper;
import game.snd.Sound;

public class ItemEntity extends SmashParticle {
    private Item item;

    public ItemEntity(Item item, int x, int y) {
        super(x, y, 0);
        this.item = item;
        time = item.getMaxTime();
    }

    public void tick() {
        super.tick();

        if (zz == 0 && level.getPlayer() != null) {
            int xd = level.getPlayer().getX() - x;
            int yd = level.getPlayer().getY() - y;

            if (xd * xd + yd * yd < 400) {
                if (xd < 0) xa = -1;
                if (xd > 0) xa = +1;
                if (yd < 0) ya = -1;
                if (yd > 0) ya = +1;
            }

            move((int) xa, (int) ya);
        }
    }

    public void render(Screen screen) {
        if (time < item.getMaxTime() / 3 && time / 6 % 2 == 0) return;

        screen.render(item.getScale(), x - 4 * item.getScale(), y - 4 * item.getScale(), item.getXSprite() * 8, item.getYSprite() * 8, PaletteHelper.getColor(-1, 0, 0, 0), 0);
        screen.render(item.getScale(), x - 4 * item.getScale(), y - 4 * item.getScale() - (int) zz, item.getXSprite() * 8, item.getYSprite() * 8, item.getColor(), 0);
    }

    public void touchedBy(Entity e) { e.touchItem(this); }

    public void take(Entity e) {
        if (e instanceof Player) Sound.pickup.play();
        item.onTake(this);
        removed = true;
    }

    public boolean ignoreBlocks() { return true; }

    public Item getItem() { return item; }

}
