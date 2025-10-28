package game.screen;

import game.InputHandler;
import game.PuzzleComponent;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Font;

public class TitleScreen extends Screen {
    private int tickTime;

    public void tick(InputHandler input) {
        tickTime++;
        if (tickTime > 240 && input.action.clicked) {
            setScreen(new SplashScreen(120, "Level 1", new GameScreen()));
            input.releaseAll();
        }
    }

    public void render(Bitmap screen) {
        int yOffs = tickTime * 2;
        if (yOffs > 560) yOffs = 560;

        screen.draw(Art.background, 0, 0);
        screen.draw(Art.title, 0, 0, 0, yOffs, PuzzleComponent.WIDTH, PuzzleComponent.HEIGHT);
        if (tickTime > 240) {
            String text = "Press Space to Start";
            Font.draw(text, screen, (PuzzleComponent.WIDTH - text.length() * 8) / 2, (PuzzleComponent.HEIGHT - 28) - (int) Math.abs(Math.sin(tickTime * 0.1) * 10));
        }
    }

}