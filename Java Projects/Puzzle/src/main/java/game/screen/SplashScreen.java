package game.screen;

import game.Game;
import game.InputHandler;
import game.PuzzleComponent;
import game.gfx.Bitmap;
import game.gfx.Font;

public class SplashScreen extends Screen {

    private int tickTime;
    private final int splashTime;
    private Screen screen;
    private final String text;

    public SplashScreen(int splashTime, String text, Screen screen) {
        this.splashTime = splashTime;
        this.text = text;
        this.screen = screen;
    }

    public void init(Game game) {
        super.init(game);
        screen.init(game);
    }

    public void tick(InputHandler input) {
        tickTime++;
        if (tickTime >= splashTime) setScreen(screen != null ? screen : new GameScreen());
        if (screen != null) screen.tick(input);
    }

    public void render(Bitmap screen) {
        if (screen != null) this.screen.render(screen);

        for (int i = 0; i < screen.pixels.length; i++) {
            int c = screen.pixels[i];
            int a = (c >> 24) & 0xFF;
            int r = (c >> 16) & 0xFF;
            int g = (c >> 8) & 0xFF;
            int b = c & 0xFF;

            int rr = r * tickTime / splashTime;
            int gg = g * tickTime / splashTime;
            int bb = b * tickTime / splashTime;

            screen.pixels[i] = (a << 24) | (rr << 16) | (gg << 8) | bb;
        }

        Font.draw(text, screen, (PuzzleComponent.WIDTH - text.length() * 8) / 2, PuzzleComponent.HEIGHT - 84);
    }

}