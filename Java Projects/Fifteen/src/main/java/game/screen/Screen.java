package game.screen;

import game.Game;
import game.InputHandler;

import java.awt.*;
import java.util.Random;

public abstract class Screen {
    protected static final Random random = new Random();
    protected Game game;

    public void init(Game game) {
        this.game = game;
    }

    public abstract void tick(InputHandler input);
    public abstract void render(Graphics2D g);
}