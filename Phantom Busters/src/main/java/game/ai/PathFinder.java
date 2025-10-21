package game.ai;

public class PathFinder {

    private int[] estimatedCosts;
    private int[] costs;
    private int[] travelCosts;
    private int xStart;
    private int yStart;
    private int xEnd;
    private int yEnd;
    private int xTile;
    private int yTile;
    public int[] path;
    public int[] from;
    public int pathP = 0;
    public boolean isPathing = false;
    private int calls = 0;
    private int[] estimates;
    private int ep = 0;
    private int closestValue;
    private int xClosest;
    private int yClosest;
    private int w;
    private int h;

    public PathFinder(int w, int h) {
        this.w = w;
        this.h = h;
        path = new int[w * h];
        from = new int[w * h];
        estimates = new int[w * h];
    }

    public void startFindingPath(int[] travelCosts, int xStart, int yStart, int xEnd, int yEnd) {
        this.travelCosts = travelCosts;
        estimatedCosts = new int[w * h];
        costs = new int[w * h];
        ep = 0;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        xTile = xStart;
        yTile = yStart;
        costs[xTile + yTile * w] = 1;
        isPathing = true;
        calls = 0;
        closestValue = -1;
        if (xStart == xEnd && yStart == yEnd) isPathing = false;
    }

    public void continueFindingPath(int maxVisits) {
        calls++;
        do {
            int p = xTile + yTile * w;
            int baseCost = costs[p];
            estimatedCosts[p] = -1;
            for (int x = xTile - 1; x <= xTile + 1; x++) {
                for (int y = yTile - 1; y <= yTile + 1; y++) {
                    if (x == xTile && y == yTile) continue;
                    if (x < 4 || y < 4 || x >= w - 4 || y >= h - 4) continue;
                    p = x + y * w;
                    if (estimatedCosts[p] < 0 || travelCosts[p] == 0) continue;

                    int dist = x == xTile || y == yTile ? 10 : 14;
                    if (travelCosts[xTile + y * w] == 0 || travelCosts[x + yTile * w] == 0) dist *= 10;
                    if (travelCosts[xTile + y * w] == 0 && travelCosts[x + yTile * w] == 0) continue;
                    int costSoFar = baseCost + travelCosts[p] * dist;
                    int xd = xEnd - x;
                    int yd = yEnd - y;
                    if (xd < 0) xd = -xd;
                    if (yd < 0) yd = -yd;

                    int remainingCost = (xd + yd) * 100;
                    int estimatedCost = costSoFar + remainingCost;
                    if (estimatedCosts[p] > 0) {
                        if (estimatedCosts[p] < estimatedCost) continue;
                    } else estimates[ep++] = p;

                    if (closestValue == -1 || remainingCost < closestValue) {
                        closestValue = remainingCost;
                        xClosest = x;
                        yClosest = y;
                    }

                    from[p] = xTile + yTile * w;
                    costs[p] = costSoFar;
                    estimatedCosts[p] = estimatedCost;
                }
            }

            xTile = -1;
            yTile = -1;
            int lowestCost = -1;
            int epi = -1;
            for (int i = 0; i < ep; i++) {
                p = estimates[i];
                if (estimatedCosts[p] <= 0 || estimatedCosts[p] >= lowestCost && lowestCost >= 0) continue;
                epi = i;
                lowestCost = estimatedCosts[p];
                xTile = p % w;
                yTile = p / w;
            }

            if (epi < 0) continue;
            estimates[epi] = estimates[--ep];
        } while ((xTile != xEnd || yTile != yEnd) && (xTile >= 0 || yTile >= 0));

        isPathing = false;
        if (xTile == -1 && yTile == -1 && closestValue != -1) {
            xTile = xClosest;
            yTile = yClosest;
        }

        if (xTile != -1 || yTile != -1) {
            pathP = 0;
            path[pathP++] = xTile + yTile * w;
            while (xTile != xStart || yTile != yStart) {
                int pp = from[xTile + yTile * w];
                xTile = pp % w;
                yTile = pp / w;
                path[pathP++] = xTile + yTile * w;
            }
        }
    }
    
}
