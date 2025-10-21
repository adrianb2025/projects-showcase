package game.screen;

import game.Game;
import game.InputHandler;
import game.PhantomBusters;
import game.gfx.Bitmap;

public class FadeScreen extends Screen {

    private int tick = 0;
    private final int splashTime;
    private Screen parent;

    public FadeScreen(int splashTime, Screen parent) {
        this.splashTime = splashTime;
        this.parent = parent;
    }

    @Override
    public void init(Game game) {
        super.init(game);
        this.parent.init(game);
    }

    @Override
    public void tick(InputHandler input) {
        ++this.tick;
        if (this.tick >= this.splashTime) {
            this.setScreen(this.parent);
        }
        this.parent.tick(input);
    }

    @Override
    public void render(Bitmap screen) {
        this.parent.render(screen);
        for (int i = 0; i < 307200; ++i) {
            int a = screen.pixels[i] >> 24 & 0xFF;
            int r = screen.pixels[i] >> 16 & 0xFF;
            int g = screen.pixels[i] >> 8 & 0xFF;
            int b = screen.pixels[i] & 0xFF;
            int rr = r * this.tick / this.splashTime;
            int gg = g * this.tick / this.splashTime;
            int bb = b * this.tick / this.splashTime;
            screen.pixels[i] = a << 24 | bb | gg << 8 | rr << 16;
        }
    }

}