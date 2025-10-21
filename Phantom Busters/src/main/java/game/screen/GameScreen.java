package game.screen;

import game.InputHandler;
import game.PhantomBusters;
import game.Player;
import game.entity.Entity;
import game.entity.unit.Unit;
import game.entity.unit.mob.Mob;
import game.entity.unit.mob.buster.Grabber;
import game.entity.unit.mob.buster.Hunter;
import game.entity.unit.mob.task.AttackTask;
import game.entity.unit.mob.task.MoveTask;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Sprite;
import game.level.Level;

import java.util.List;

public class GameScreen extends Screen {

    public Level level;
    public double xScroll;
    public double yScroll;
    public double xScrollA;
    public double yScrollA;
    public double xScrollT;
    public double yScrollT;
    public int scrollSteps;
    public double scrollSpeed = 2.0;
    public Bitmap shadows;
    public Player player;
    public Hunter hunter;

    public GameScreen(Level level, Player player) {
        this.level = level;
        this.player = player;
        hunter = new Hunter(player);
        level.add(hunter);
        player.add(hunter);
        hunter.findPos();
        addGrabber(2);
    }

    public void addGrabber(int num) {
        for (int i = 0; i < num; i++) {
            Grabber grabber = new Grabber(player, hunter);
            level.add(grabber);
            player.add(grabber);
            grabber.findPos();
        }
    }

    public void tick(InputHandler input) {
        level.player.tick();
        super.tick(input);
        xScrollA *= 0.7;
        yScrollA *= 0.7;
        scrollSpeed = input.up.down | input.down.down | input.left.down | input.right.down ? (scrollSpeed += (6.0 - scrollSpeed) * 0.05) : 2.0;

        if (input.up.down) yScrollA -= scrollSpeed;
        if (input.down.down) yScrollA += scrollSpeed;
        if (input.left.down) xScrollA -= scrollSpeed;
        if (input.right.down) xScrollA += scrollSpeed;

        xScroll += xScrollA;
        yScroll += yScrollA;

        if (scrollSteps > 0) {
            xScroll += (xScrollT - xScroll) / (double)scrollSteps;
            yScroll += (yScrollT - yScroll) / (double)scrollSteps;
            scrollSteps--;
        }

        if (input.leftClicked) sendTo(hunter, input.x, input.y);

        centerOn(hunter);
        level.tick();
    }

    public void sendTo(Mob unit, int x, int y) {
        double xx = (x + xScroll) / 0.75 / 2.0;
        double yy = (y + yScroll) / 0.75;
        double xt = xx + yy;
        double yt = yy - xx;
        if (!level.getTile((int) xt / 64, (int) yt / 64).blocks()) {
            double r = 40.0;
            List<Entity> entities = level.getEntities(xt - r, yt - r, 0.0, xt + r, yt + r, r);
            for (Entity e : entities) {
                if (!(e instanceof Mob) || e == unit || !unit.isLegalTarget((Mob)e)) continue;
                unit.setTask(new AttackTask((Mob)e));
                ((Mob)e).selectInterval = 20;
                return;
            }
            
            unit.setTask(new MoveTask(xt, yt));
        }
    }

    private void centerOn(Unit u) {
        if (u == null) return;
        
        double xx = (int)Math.floor((u.x - u.y) * 0.75);
        double yy = (int)Math.floor((u.y + u.x - 6.0) * 0.375);
        
        scrollSteps = (int)(Math.sqrt(Math.sqrt(u.distanceToScreenSpaceSqr(xScroll + 80.0, yScroll + 60.0))) / 2.0 + 1.0);
        xScrollT = xx - PhantomBusters.WIDTH / 2;
        yScrollT = yy - PhantomBusters.HEIGHT / 2;
    }

    public void render(Bitmap screen) {
        if (shadows == null) shadows = new Bitmap(screen.w, screen.h);
        shadows.clear(0);
        int xScroll = (int) Math.floor(this.xScroll);
        int yScroll = (int) Math.floor(this.yScroll);
        screen.xOffs = -xScroll;
        screen.yOffs = -yScroll;
        level.renderBg(screen, xScroll, yScroll, player.visMap);
        screen.xOffs = 0;
        screen.yOffs = 0;
        shadows.xOffs = -xScroll;
        shadows.yOffs = -yScroll;
        level.renderShadows(shadows, xScroll, yScroll, player.visMap);
        screen.shade(shadows);
        screen.xOffs = -xScroll;
        screen.yOffs = -yScroll;
        level.renderSprites(screen, xScroll, yScroll, player.visMap);
        level.renderInvis(screen, xScroll, yScroll, player.visMap);
        screen.xOffs = 0;
        screen.yOffs = 0;
    }

}