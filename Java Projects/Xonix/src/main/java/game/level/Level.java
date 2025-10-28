package game.level;

import game.entity.Enemy;
import game.entity.Entity;
import game.entity.Player;
import game.particle.Particle;
import game.screen.GameScreen;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import static game.Constants.*;

public class Level {

    private List<Entity> entities = new ArrayList<>();
    private List<Particle> particles = new ArrayList<>();

    public int w;
    public int h;
    private int[] grid;
    private int tickTime;

    public boolean pathing;
    private boolean[] visited;

    private GameScreen gameScreen;

    private int screenOffsetX;
    private int screenOffsetY;

    public Level(GameScreen gameScreen, int w, int h) {
        this.gameScreen = gameScreen;
        this.w = w;
        this.h = h;
        grid = new int[w * h];
        visited = new boolean[w * h];

        for (int i = 0; i < w * h; i++) {
            int x = i % w;
            int y = i / w;
            int tile = NOTHING;

            if (x == 0 || y == 0 || x == w - 1 || y == h - 1) {
                tile = PERMANENT;
            }

//            if (tile == NOTHING && Math.random() < 0.5) {
//                tile = GROUND;
//            }

            grid[i] = tile;
        }
    }

    public void resetGame() {
        gameScreen.newGame();
    }

    public void steppedOn(Player p, int x, int y, int xo, int yo) {
        int prev = grid[xo + yo * w];
        int tile = grid[x + y * w];

        if ((prev == PERMANENT || prev == GROUND) && tile == NOTHING) {
            pathing = true;
        }

        if (prev == PATH && (tile == PERMANENT || tile == GROUND)) {
            pathing = false;
            processContours();
            p.xa = 0;
            p.ya = 0;

            for (int i = 0; i < w * h; i++) {
                if (grid[i] == PATH) {
                    grid[i] = GROUND;
                }
            }
        }

        if (tile == NOTHING) {
            grid[x + y * w] = PATH;
        }
    }

    public void add(Entity e) {
        entities.add(e);
        e.init(this);
    }

    public void add(Particle p) {
        particles.add(p);
        p.init(this);
    }

    public void tick() {
        tickTime++;
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (!e.removed) e.tick();
            if (e.removed) entities.remove(i--);
        }

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            if (!p.removed) p.tick();
            if (p.removed) particles.remove(i--);
        }
    }

    public void setScreenOffset(int xOffs, int yOffs) {
        this.screenOffsetX = xOffs;
        this.screenOffsetY = yOffs;
    }

    public void render(Graphics2D g) {
        AffineTransform at = g.getTransform();

        g.translate(screenOffsetX, screenOffsetY);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, CELL_SIZE * w, CELL_SIZE * h);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int tile = grid[x + y * w];
                int height = TILE_HEIGHTS[tile];
                Color color = height == 0 ? TILE_COLS[tile] : Color.BLACK;
                g.setColor(color);
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int tile = grid[x + y * w];
                int height = TILE_HEIGHTS[tile];
                Color color = TILE_COLS[tile];

                if (height == 0) continue;

                int xOffs = (CELL_SIZE / 4);
                int yOffs = (CELL_SIZE / 2);

                g.setColor(color);
                g.fillRect(x * CELL_SIZE + xOffs, y * CELL_SIZE - yOffs, CELL_SIZE, CELL_SIZE);
            }
        }

        for (Entity e : entities) {
            e.render(g);
        }

        for (Particle p : particles) {
            p.render(g);
        }

        g.setTransform(at);
    }

    private void clearVisited() {
        for (int i = 0; i < w * h; i++) {
            visited[i] = false;
        }
    }

    private void processContours() {
        clearVisited();

        List<List<Integer>> lists = new ArrayList<>();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (grid[i + j * w] == 0) {
                    List<Integer> contour = new ArrayList<>();
                    dfs(i, j, contour);
                    if (!contour.isEmpty()) {
                        lists.add(contour);
                    }
                }
            }
        }

        if (lists.size() >= 2) {
            for (int i = 0; i < lists.size(); i++) {
                List<Integer> contour = lists.get(i);
                if (hasDiagonalHoles(contour)) {
                    lists.remove(i--);
                } else {
                    for (Entity e : entities) {
                        if (!(e instanceof Enemy)) continue;
                        if (contour.contains(e.xt + e.yt * w)) {
                            lists.remove(i--);
                            break;
                        }
                    }
                }
            }

            int min = 10000;
            List<Integer> result = null;
            for (List<Integer> contour : lists) {
                if (contour.size() < min || result == null) {
                    min = contour.size();
                    result = contour;
                }
            }

            if (result != null) {
                fillContour(result);
            }
        }


    }

    private boolean hasDiagonalHoles(List<Integer> contour) {
        for (int index : contour) {
            int x = index % w;
            int y = index / w;

            if (x > 0 && y > 0 && grid[(x - 1) + (y - 1) * w] == NOTHING && !contour.contains((x - 1) + (y - 1) * w)) {
                return true;
            }

            if (x < w - 1 && y > 0 && grid[(x + 1) + (y - 1) * w] == NOTHING && !contour.contains((x + 1) + (y - 1) * w)) {
                return true;
            }

            if (x > 0 && y < h - 1 && grid[(x - 1) + (y + 1) * w] == NOTHING && !contour.contains((x - 1) + (y + 1) * w)) {
                return true;
            }

            if (x < w - 1 && y < h - 1 && grid[(x + 1) + (y + 1) * w] == NOTHING && !contour.contains((x + 1) + (y + 1) * w)) {
                return true;
            }
        }

        return false;
    }

    private void dfs(int x, int y, List<Integer> contour) {
        if (x < 0 || y < 0 || x >= w || y >= h || grid[x + y * w] != NOTHING || visited[x + y * w]) {
            return;
        }

        visited[x + y * w] = true;
        contour.add(x + y * w);

        dfs(x - 1, y, contour);
        dfs(x + 1, y, contour);
        dfs(x, y - 1, contour);
        dfs(x, y + 1, contour);
    }

    private void fillContour(List<Integer> contour) {
        for (Integer index : contour) {
            grid[index] = GROUND;
        }
    }

    public int getTile(int xt, int yt) {
        if (xt < 0 || yt < 0 || xt >= w || yt >= h) return PERMANENT;
        return grid[xt + yt * w];
    }

    public void setTile(int xt, int yt, int tile) {
        if (xt < 0 || yt < 0 || xt >= w || yt >= h) return;
        grid[xt + yt * w] = tile;
    }

}