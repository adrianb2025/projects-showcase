package game;

import game.gfx.Art;
import game.gfx.Bitmap;
import game.level.Level;
import game.screen.FadeScreen;
import game.screen.GameScreen;
import game.screen.Screen;

public class Game {

    private Screen screen;
    public Level level;
    public InputHandler input;

    public Game(InputHandler input) {
        this.input = input;
        level = new Level(1);
        setScreen(new FadeScreen(180, new GameScreen(level, level.player)));
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
        screen.init(this);
    }

    public void tick() { screen.tick(input); }
    public void render(Bitmap screen) {
        this.screen.render(screen);

        if (input.onScreen) screen.draw(Art.cursors[0][0], input.x - 1, input.y - 1);
    }

}