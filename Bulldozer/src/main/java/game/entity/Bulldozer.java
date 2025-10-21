package game.entity;

import game.InputHandler;
import game.gfx.Art;

import java.awt.*;

@Deprecated
public class Bulldozer extends Entity {

    public Bulldozer(int x, int y) { super(x, y); }

    public void tick() {}

    public void handleInput(InputHandler input) {
        int movement = 1;

        if (input.up.clicked) ya -= movement;
        if (input.down.clicked) ya += movement;
        if (input.left.clicked) xa -= movement;
        if (input.right.clicked) xa += movement;

        if (xa * xa + ya * ya > 1) {
            xa = 0;
            ya = 0;
        }

        attemptMove(xa, ya);

        xa = 0;
        ya = 0;
    }

    public boolean blocks(Entity e) { return true; }
    public void render(Graphics2D g) { g.drawImage(Art.sheet[7], x << 4, y << 4, null); }

}