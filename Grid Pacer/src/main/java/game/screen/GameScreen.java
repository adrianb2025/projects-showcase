package game.screen;

import game.Game;
import game.InputHandler;
import game.level.Level;

import java.awt.*;

public class GameScreen extends Screen {

    private Font smallFont = new Font(null, Font.PLAIN, 10);
    private Level level;
    private String[] levels = "5,5,20;6,6,30;7,10,40;12,5,50;10,12,80;18,10,100;20,12,160".split(";");
    private static int num;

    public GameScreen() { nextLevel(); }

    public void tick(InputHandler input) {
        if (input.onScreen && input.leftClicked) level.placeBlockAt(input.x - level.xOffset >> 4, input.y - level.yOffset >> 4);

        level.tick();

        if (input.rightClicked && !level.started || input.start.clicked && !level.started) level.startGame();

        if (input.n0.clicked) level.pacerSpeed = 1;
        if (input.n1.clicked) level.pacerSpeed = 2;
        if (input.n2.clicked) level.pacerSpeed = 3;
        if (input.n3.clicked) level.pacerSpeed = 4;
        if (input.n4.clicked) level.pacerSpeed = 5;
        if (input.n5.clicked) level.pacerSpeed = 6;
        if (input.n6.clicked) level.pacerSpeed = 7;
        if (input.n7.clicked) level.pacerSpeed = 8;
        if (input.n8.clicked) level.pacerSpeed = 9;
    }

    public void render(Graphics2D g) {
        level.render(g);

        String s = "Level " + ((num - 1) % levels.length + 1) + " steps required: " + level.steps + " pace: " + level.pacerSpeed;
        g.setColor(Color.WHITE);
        g.setFont(smallFont);
        g.drawString(s, 10, 10);
        s = "Numpad/Key 1-9 changes pace";
        g.drawString(s, 10, 20);
        if (!level.started) {
            s = "Place blocks(left-click)";
            g.drawString(s, 10, 220);

            s = "Start(spacebar/right-click)";
            g.drawString(s, 10, 230);
        }
    }

    public void nextLevel() {
        String[] l = levels[num++ % levels.length].split(",");
        int w = Integer.valueOf(l[0]);
        int h = Integer.valueOf(l[1]);
        int steps = Integer.valueOf(l[2]);
        level = new Level(this, w, h, steps);
    }

}