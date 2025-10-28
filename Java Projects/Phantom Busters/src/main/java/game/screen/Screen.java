package game.screen;

import game.Game;
import game.InputHandler;
import game.gfx.Bitmap;

public class Screen {

    protected Game game;
    public int tickTime;

    public void init(Game game) { this.game = game; }
    public void setScreen(Screen screen) { game.setScreen(screen); }

    public void tick(InputHandler input) { tickTime++; }
    public void render(Bitmap screen) {}

}