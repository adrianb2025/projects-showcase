package game.screen;

import game.InputHandler;
import game.JumpComponent;
import game.entity.mob.Boss;
import game.entity.mob.Player;
import game.gfx.Art;
import game.level.Level;

import java.awt.*;

public class GameScreen extends Screen {

    private Level level;
    private Player player;

    public GameScreen() { newGame(); }

    public void newGame() {
        player = new Player((JumpComponent.WIDTH - 16) / 2, 7560);
        level = new Level(this, player, new Boss(), JumpComponent.WIDTH / 16, JumpComponent.HEIGHT / 16 * 32);
        level.addEntity(player);
    }

    public void tick(InputHandler input) {
        level.tick(input);
        player.handleInput(input);
    }

    public void render(Graphics2D g) {
        level.render(g);
        level.renderUI(g);
    }

}