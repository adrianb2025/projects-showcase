package game;

import game.screen.GameScreen;
import game.screen.Screen;
import game.screen.TitleScreen;

import java.awt.*;

public class Game {

    private Screen screen;

    public Game() { setScreen(new TitleScreen()); }

    public void setScreen(Screen screen) {
        this.screen = screen;
        this.screen.init(this);
    }

    public void tick(InputHandler input) { screen.tick(input); }
    public void render(Graphics2D g) { screen.render(g); }

    public boolean clearScreen() { return screen.clearScreen(); }

}