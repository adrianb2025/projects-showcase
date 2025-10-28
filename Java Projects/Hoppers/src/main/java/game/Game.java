package game;

import game.level.Level;
import game.screen.*;
import game.snd.Sound;

import java.awt.*;

public class Game {

    private Screen screen;

    private Player player;
    private Level level;

    public Game() {
        player = new Player(this);
        setScreen(new TitleScreen());
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
        this.screen.init(this);
    }

    public void tick(InputHandler input) {
        if (input.restart.clicked) {
            if (screen instanceof GameWinScreen) newGame();
            else restartLevel();
        }

        screen.tick(input);
    }

    public void render(Graphics2D g, double delta) { screen.render(g, delta); }

    public void newGame() {
        loadLevel(0);
        setScreen(new GameScreen(level, player));
    }

    public void loadLevel(int num) {
        Sound.startGame.play();
        level = new Level(num);
        player.init(level);
        setScreen(new GameScreen(level, player));
    }

    public void nextLevel() { loadLevel(level.num + 1); }
    public void restartLevel() { loadLevel(level.num); }

    public void win() {
        setScreen(new GameWinScreen((GameScreen) screen));
        Sound.win.play();
    }

    public void gameOver() {
        setScreen(new GameOverScreen((GameScreen) screen));
        Sound.gameOver.play();
    }
}