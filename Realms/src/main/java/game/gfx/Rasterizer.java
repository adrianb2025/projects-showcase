package game.gfx;

import game.Game;
import game.entity.particle.FireParticle;
import game.entity.particle.ParticleSystem;
import game.gfx.sprite.SpriteCollector;
import game.gfx.util.PaletteHelper;
import game.gfx.weather.WeatherManager;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.Raster;

public class Rasterizer extends Canvas {

    protected Screen screen;
    protected SpriteCollector collector;
    protected ParticleSystem fireParticles;
    protected WeatherManager weatherManager = new WeatherManager();
    private BufferStrategy bs;

    public Rasterizer() {
        screen = new Screen(Game.WIDTH, Game.HEIGHT, "/sprites.png");
        collector = new SpriteCollector(screen.getSprites());
        try {
            fireParticles = new ParticleSystem(FireParticle.class, 2000, 0.03, -0.01, 40);
        } catch (Exception e) {
            // ignore
        }
    }

    public void initRaster() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }
    }

    public void render(boolean isGray) {
        PaletteHelper.getInstance().wrap(screen.getViewPort(), isGray, weatherManager.getWeather());
        if (bs != null) {
            Graphics g = bs.getDrawGraphics();

            g.drawImage(screen.getViewPort().getImage(), 0, 0, getWidth(), getHeight(), null);

            g.dispose();
            bs.show();
        }
    }

    public ParticleSystem getFireParticles() { return fireParticles; }
    public SpriteCollector getSpriteCollector() { return collector; }

}