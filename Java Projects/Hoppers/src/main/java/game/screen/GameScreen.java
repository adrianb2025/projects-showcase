package game.screen;

import game.Hoppers;
import game.InputHandler;
import game.Player;
import game.gfx.GameGui;
import game.level.Level;
import game.particle.RockParticle;

import java.awt.*;

public class GameScreen extends Screen {

    private Level level;
    private Player player;
    private GameGui gui;

    public GameScreen(Level level, Player player) {
        this.level = level;
        this.player = player;
        gui = new GameGui(player);
    }

    public void tick(InputHandler input) {
        level.tick();
        player.tick(input);
        gui.tick();
    }

    public void render(Graphics2D g, double delta) {
        level.render(g, delta);
        gui.render(g);
    }

}