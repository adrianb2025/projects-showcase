package game;

import game.gfx.Bitmap;
import game.screen.GameScreen;
import game.screen.Screen;

import java.awt.*;

public class Game {

    protected Screen screen;

    public Game() { setScreen(new GameScreen()); }

    public void setScreen(Screen screen) {
        this.screen = screen;
        this.screen.init(this);
    }

    public void tick(InputHandler input) { screen.tick(input); }
    public void render(Graphics2D g) { screen.render(g); }
    public void postRender(Bitmap screenBitmap, Graphics2D g) { screen.postRender(screenBitmap, g); }

}