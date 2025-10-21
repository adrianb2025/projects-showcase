package game.screen;

import game.Game;
import game.InputHandler;

import java.awt.*;

public abstract class Screen {

    protected Game game;

    public void init(Game game) { this.game = game; }
    public void setScreen(Screen screen) { game.setScreen(screen); }

    public abstract void tick(InputHandler input);
    public abstract void render(Graphics2D g);

    public boolean clearScreen() { return true; }

}