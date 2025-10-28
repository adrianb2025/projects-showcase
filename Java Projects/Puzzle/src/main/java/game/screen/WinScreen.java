package game.screen;

import game.InputHandler;
import game.PuzzleComponent;
import game.gfx.Art;
import game.gfx.Bitmap;
import game.gfx.Font;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WinScreen extends Screen {

    private int tickTime;
    private List<String> lines = new ArrayList<String>();

    public WinScreen() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(WinScreen.class.getResourceAsStream("/win.txt")));
            String line = "";
            while ((line = br.readLine()) != null) lines.add(line);
            br.close();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load win.txt");
        }
    }

    public void tick(InputHandler input) {
        tickTime++;
        if (tickTime > lines.size() * 30 + 180 || input.action.clicked) setScreen(new TitleScreen());
    }

    public void render(Bitmap screen) {
        screen.draw(Art.background, 0, 0);
        int yo = tickTime / 2;
        for (int y = 0; y <= 20; y++) {
            int yl = yo / 8 - 20 + y;
            if (yl < 0 || yl >= lines.size()) continue;
            Font.draw(lines.get(yl), screen, (PuzzleComponent.WIDTH - lines.get(yl).length() * 8) / 2, y * 8 - yo % 8);
        }
    }

}