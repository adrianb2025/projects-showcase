package game.screen;

import game.GameComponent;
import game.InputHandler;
import game.entity.Enemy;
import game.entity.Player;
import game.level.Level;

import java.awt.*;
import static game.Constants.*;

public class GameScreen extends Screen {

    private Level level;
    private Player player;

    public GameScreen() {
        newGame();
    }

    public void newGame() {
        level = new Level(this, 50, 30);
        player = new Player(0, 0);
        level.add(player);

        for (int i = 0; i < 3; i++) {
            Enemy.spawnRandom(level);
        }
    }

    public void tick(InputHandler input) {
        if (input.left.clicked) player.move(-1, 0);
        if (input.right.clicked) player.move(1, 0);
        if (input.up.clicked) player.move(0, -1);
        if (input.down.clicked) player.move(0, 1);

        level.tick();
    }

    public void render(Graphics2D g) {
        int xOffs = (GameComponent.WIDTH - CELL_SIZE * level.w) / 2;
        int yOffs = (GameComponent.HEIGHT - CELL_SIZE * level.h) / 2;

        level.setScreenOffset(xOffs, yOffs);
        level.render(g);
    }

}