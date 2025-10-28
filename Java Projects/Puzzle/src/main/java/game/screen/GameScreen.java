package game.screen;

import game.InputHandler;
import game.PuzzleComponent;
import game.entity.mob.Ghost;
import game.entity.mob.Player;
import game.gfx.Bitmap;
import game.gfx.Font;
import game.level.Level;

public class GameScreen extends Screen {

    public static final int LEVELS = 6;
    public Level level;
    public int levelNum = 1;
    public Player player = new Player();


    public GameScreen() { level = new Level(PuzzleComponent.WIDTH, PuzzleComponent.HEIGHT, this, new Ghost()); }

    private void reset() {
        player = new Player();
        changeLevel(1);
    }

    public void tick(InputHandler input) {
        int currLevel = player.score / 2000 + 1;
        if (levelNum != currLevel) {
            changeLevel(currLevel);
            return;
        }

        level.tick();

        if (input.mouse.clicked) {
            int mx = input.x - level.xOffset >> 4;
            int my = input.y - level.yOffset >> 4;
            level.crystalHandler.selectCrystal(mx, my);
        }
    }

    public void render(Bitmap screen) {
        level.renderSprites(screen);

        String text = "Score " + player.score;
        Font.draw(text, screen, PuzzleComponent.WIDTH - text.length() * 8 - 2, PuzzleComponent.HEIGHT - 10);

        if (player.prevScore > 0) {
            text = (player.prevScore > 300 ? "Prev " : "")  + player.prevScore;
            Font.draw(text, screen, (PuzzleComponent.WIDTH - text.length() * 8) / 2, 2);
        }

        text = "Level " + levelNum + "/" + 6;
        Font.draw(text, screen, 2, PuzzleComponent.HEIGHT - 10);
    }

    public void restartLevel() {
        setScreen(new SplashScreen(120, "Level Failed", this));
        level = new Level(PuzzleComponent.WIDTH, PuzzleComponent.HEIGHT, this, new Ghost());
    }

    public void changeLevel(int levelNum) {
        if (levelNum > LEVELS) {
            setScreen(new WinScreen());
            return;
        }

        this.levelNum = levelNum;
        setScreen(new SplashScreen(120, "Level " + levelNum, this));
        level = new Level(PuzzleComponent.WIDTH, PuzzleComponent.HEIGHT, this, new Ghost());
    }

}
