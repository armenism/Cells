import java.io.IOException;
import java.util.Random;

/**
 * Created by armenstepanians on 2017-08-05.
 */
public class Cells implements CellsInterface {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private Boolean[][] map;
    private Boolean[][] reproductionMap;
    private Boolean[][] deathMap;
    private int[][] surroundingsMap;

    private int cycle;
    private int size;

    private Cells(int size, int initialNumberOfCells) {
        this.map = new Boolean[size][size];
        this.reproductionMap = new Boolean[size][size];
        this.deathMap = new Boolean[size][size];
        this.surroundingsMap = new int[size][size];

        this.cycle = 0;
        this.size = size;

        initialize(initialNumberOfCells);
    }

    public static void main(String[] args) {
        Cells cells = new Cells(45, 150);
        cells.updateSurroundingsMap();
        cells.printMap();
//        cells.printSurroundingsMap();

        cells.nextCycle();
    }

    @Override
    public void cellsReact() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (surroundingsMap[y][x] == 2) reproduce(x, y);
                else if (map[y][x] && (surroundingsMap[y][x] == 0 || surroundingsMap[y][x] > 3)) deathMap[y][x] = true;
            }
        }
    }

    @Override
    public void reproduce(int X, int Y) {
        Random r = new Random();
        int x = 0, y = 0;

        try {

        while (map[y = normalize(Y + r.nextInt(3) - 1)][x = normalize(X + r.nextInt(3) - 1)]
                || (x == X && y == Y)) ;
        reproductionMap[y][x] = true;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("X " + x + "\nY " + y);
        }

    }

    private int normalize(int rand) {
        return Math.min(Math.max(rand, 0), size - 1);
    }

    @Override
    public int countSurroundings(int X, int Y) {
        int surroundings = 0;

        for (int y = Math.max(Y - 1, 0); y <= Math.min(Y + 1, size - 1); y++) {
            for (int x = Math.max(X - 1, 0); x <= Math.min(X + 1, size - 1); x++) {
                if (!(x == X && y == Y) && map[y][x])
                    surroundings++;
            }
        }

        return surroundings;
    }

    @Override
    public void updateSurroundingsMap() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (map[y][x]) surroundingsMap[y][x] = countSurroundings(x, y);
                else surroundingsMap[y][x] = 0;
            }
        }
    }

    @Override
    public void nextCycle() {

        while (true) {
            try {
                Thread.sleep(100);
                clearScreen();
                cellsReact();

                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (reproductionMap[y][x]) map[y][x] = true;
                        if (deathMap[y][x]) map[y][x] = false;
                    }
                }

                cleanMap(reproductionMap);
                cleanMap(deathMap);
                updateSurroundingsMap();

                cycle++;

                printMap();
                System.out.println("\nCycle " + cycle + "\n");
//                printSurroundingsMap();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(int initialNumberOfCells) {
        cleanMap(map);
        cleanMap(reproductionMap);
        cleanMap(deathMap);

        Random r = new Random();
        int x, y;

        for (int i = 0; i < initialNumberOfCells; i++) {
            while (map[x = r.nextInt(size)][y = r.nextInt(size)]) ;
            map[y][x] = true;
        }
    }

    private void cleanMap(Boolean[][] map) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                map[y][x] = false;
            }
        }
    }

    @Override
    public void printMap() {

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (map[y][x]) System.out.print(colorize(x, y));
                else System.out.print(ANSI_BLACK + "- " + ANSI_RESET);
            }
            System.out.println();
        }
    }

    @Override
    public void printSurroundingsMap() {

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                System.out.print(surroundingsMap[y][x] + " ");
            }
            System.out.println();
        }
        System.out.println("\nCycle " + cycle + "\n");
    }

    private void clearScreen() {
        try {
            Runtime.getRuntime().exec("clear");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String colorize(int x, int y) {
        if (surroundingsMap[y][x] == 2) return ANSI_GREEN + "* " + ANSI_RESET;
        if (surroundingsMap[y][x] == 1 || surroundingsMap[y][x] == 3) return ANSI_BLUE + "* " + ANSI_RESET;
        else return ANSI_RED + "* " + ANSI_RESET;
    }
}