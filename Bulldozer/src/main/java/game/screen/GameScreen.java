package game.screen;

import game.InputHandler;
import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class GameScreen extends Screen {

    public Level level;

    public GameScreen() { newGame(); }

    public void newGame() { level = new Level(0, this); }
    public void restart() { level = new Level(level.level, this); }
    public void nextLevel() { level = new Level(level.level + 1, this); }

    public void tick(InputHandler input) {
        if (level != null) {
            level.player.handleInput(input);
            level.tick();
        }

        if (input.restart.clicked) restart();
    }

    public void render(Graphics2D g) { if (level != null) level.render(g); }

}