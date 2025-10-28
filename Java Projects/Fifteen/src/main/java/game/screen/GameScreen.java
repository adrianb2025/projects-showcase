package game.screen;

import game.InputHandler;
import game.TweenHandler;
import game.entity.Block;

import java.awt.*;

public class GameScreen extends Screen {

    public int[] dirs = {
            1, 0,
            0, 1,
            -1, 0,
            0, -1
    };

    public boolean anim;
    private Block[] blocks = new Block[16];
    private Block emptyBlock;
    private int shuffleTime;
    private int lastShuffleIndex = -1;

    public GameScreen() {
        for (int i = 0; i < 16; i++) {
            Block b = new Block(i, i % 4, i / 4, i == 15);
            if (i == 15) emptyBlock = b;
            b.init(this);
            blocks[i] = b;
        }

        startGame();
    }

    public void tick(InputHandler input) {
        if (shuffleTime-- > 0 && !anim) {
            Block nextBlockToSwap = null;
            do {
                int dir = random.nextInt(4);

                int x = emptyBlock.xt + dirs[dir * 2];
                int y = emptyBlock.yt + dirs[dir * 2 + 1];

                int index = x + y * 4;
                if (index == lastShuffleIndex) continue;

                nextBlockToSwap = getBlock(x, y);
                if (nextBlockToSwap != null) lastShuffleIndex = emptyBlock.xt + emptyBlock.yt * 4;
            } while (nextBlockToSwap == null);

            swap(nextBlockToSwap.xt + nextBlockToSwap.yt * 4, emptyBlock.xt + emptyBlock.yt * 4);
            return;
        }

        if (input.leftClicked) {
            int xt = input.x >> 5;
            int yt = input.y >> 5;

            Block block = getBlock(xt, yt);
            if (block != null && !block.isEmpty) {
                Block empty = getEmptyNeighbor(xt, yt);
                if (empty != null) {
                    swap(block.xt + block.yt * 4, empty.xt + empty.yt * 4);
                }
            }
        }
    }

    public void render(Graphics2D g) {
        for (Block block : blocks) {
            block.render(g);
        }
    }

    public void startGame() {
        shuffleTime = 300;
        lastShuffleIndex = emptyBlock.xt + emptyBlock.yt * 4;
    }

    public void swap(int index, int emptyBlock) {
        if (anim) return;

        Block from = blocks[index];
        Block to = blocks[emptyBlock];

        anim = true;

        game.tweenHandler.tween(shuffleTime > 0 ? 3 : 10, new TweenHandler.Tween.TweenAction() {
            public void update(double t) {
                from.x = (from.xt + (to.xt - from.xt) * t) * 32;
                from.y = (from.yt + (to.yt - from.yt) * t) * 32;

                to.x = (to.xt + (from.xt - to.xt) * t) * 32;
                to.y = (to.yt + (from.yt - to.yt) * t) * 32;
            }

            public void onComplete() {
                anim = false;

                int temp = from.xt;
                from.xt = to.xt;
                to.xt = temp;

                temp = from.yt;
                from.yt = to.yt;
                to.yt = temp;

                blocks[emptyBlock] = from;
                blocks[index] = to;

                if (completed()) startGame();
            }
        });
    }

    private Block getEmptyNeighbor(int xt, int yt) {
        Block up = getBlock(xt, yt - 1);
        if (up != null && up.isEmpty) return up;
        Block down = getBlock(xt, yt + 1);
        if (down != null && down.isEmpty) return down;
        Block left = getBlock(xt - 1, yt);
        if (left != null && left.isEmpty) return left;
        Block right = getBlock(xt + 1, yt);
        if (right != null && right.isEmpty) return right;
        return null;
    }

    private Block getBlock(int xt, int yt) {
        if (xt < 0 || yt < 0 || xt >= 4 || yt >= 4) return null;
        return blocks[xt + yt * 4];
    }

    private boolean completed() {
        for (int i = 0; i < 16; i++) {
            if (blocks[i].value != i) return false;
        }

        return true;
    }

}