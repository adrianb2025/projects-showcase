package game.gfx.weather;

import game.entity.rain.Rain;
import game.level.Level;

import java.util.Random;

public class WeatherManager {

    private Random random = new Random();
    private Weather current = new Weather();
    private int rainTime;
    private int speed = 2;

    public void tick(Level level) {
        current.setRain(false);
        current.setThunder(false);

        if (rainTime > 0) {
            rainTime--;
            if (random.nextInt(500) == 0 && speed < 4) speed++;
            current.setRain(true);
            if (rainTime > 100) level.add(new Rain(level.getPlayer(), -0.5, 1, speed));
        }

        if (random.nextInt(5000) == 0 && rainTime <= 0) {
            speed = 2;
            rainTime = 5000;
        }

        if (current.isRain() && rainTime < 4000 && rainTime > 1000 && random.nextInt(100) == 0) current.setThunder(true);

    }

    public Weather getWeather() { return current; }

}
