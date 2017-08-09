/**
 * Created by armenstepanians on 2017-08-05.
 */
public interface CellsInterface {


    /**
     * Function to create new cells, do nothing, or die based on conditions
     */
    public void cellsReact();

    /**
     * Make a new cell based off of the existing cell
     * @param X X coordinate of the existing cell
     * @param Y Y coordinate of the existing cell
     */
    public void reproduce(int X, int Y);

    /**
     * Count the surrounding number of cells of the given cell
     * @param X X coordinate of the given cell
     * @param Y Y coordinate of the given cell
     * @return number of surrounding cells
     */
    public int countSurroundings(int X, int Y);

    /**
     * Update the surroundings map based on the new number of cells
     */
    public void updateSurroundingsMap();

    /**
     * Produce the next cycle
     */
    public void nextCycle();

    /**
     * Initialize all the parameters
     * @param initialNumberOfCells the initial number of cells that we want to start with
     */
    public void initialize(int initialNumberOfCells);

    /**
     * print out the map
     */
    public void printMap();

    /**
     * print out the map of all the cells' surroundings
     */
    public void printSurroundingsMap();

}
