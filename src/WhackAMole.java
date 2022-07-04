import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class WhackAMole {
    int score;
    int molesLeft;
    int attemptsLeft;
    int gridDimension;
    char[][] moleGrid;

    public WhackAMole(int numAttempts, int gridDimension) { // constructor has no return, no need to specify

        this.score = 0;
        this.molesLeft = 0;
        this.attemptsLeft = numAttempts;
        this.gridDimension = gridDimension;
        this.moleGrid = new char[gridDimension][gridDimension];

        // Initalise grid
        for (int row = 0; row < this.moleGrid.length; row++) {
            Arrays.fill(this.moleGrid[row], '*');
        }
    }

    public static void main(String[] args) {
        WhackAMole whackAMole = new WhackAMole(50, 10);
        whackAMole.placeMoles(10);
        whackAMole.play();
        whackAMole.printGrid();
//        whackAMole.printGrid();
//        whackAMole.place(3,4);
//        whackAMole.whack(3,4);
//        whackAMole.printGrid();
    }

    /*
     Playing game
    */
    private void play() {
        /**
         * Play method runs the game and waits for user inputs.
         * Moles have already been placed by this point.
         */
        // init
        boolean playAnotherRound = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("You have " + this.attemptsLeft + "attempts to find all the moles.\n");

        while (playAnotherRound) {
            // get user input
            // TODO input sanitisation
            System.out.println("x coordinate: ");
            int x_coord = scanner.nextInt();
            System.out.println("y coordinate: ");
            int y_coord = scanner.nextInt();

            // break if exit coords given
            if (x_coord == -1 && y_coord == -1) { break; }

            // Whack and determine if another round can be played
            this.whack(x_coord, y_coord);
            playAnotherRound = this.attemptsLeft > 0 && this.molesLeft > 0;
        }
        System.out.println("Game over.");
    }

    /*
      Printing grid completely
     */
    public void printGrid() {
        /**
         * Prints the grid, including showing where all unwhacked moles are.
         * M - mole, W - whacked, * - empty
         */
        // M - mole, W - whacked, * - empty
        for (int row = 0; row < this.moleGrid.length; row++) {
            System.out.println(this.moleGrid[row]);
        }
    }

    /*
     Placing moles in grid.
    */
    public boolean place(int x, int y) {
        /**
         * Given a location, place a mole at that location
         */
        int upper_bound = this.gridDimension; // cannot place mole outside of grid
        if (x <= upper_bound && y <= upper_bound) {
            this.moleGrid[x][y] = 'M'; // place mole
            this.molesLeft++; // update moles left
            return true;
        }
        return false;
    }

    private void placeMoles(int numOfMoles) {
        /**
         * Randomly place n moles in grid
         */
        int upper_bound = this.gridDimension; // cannot place mole outside of grid

        // random position then place
        while (this.molesLeft < numOfMoles) { // why while rather than for? automatic retries without having to check grid
            int x_rand = new Random().nextInt(upper_bound); // upper bound not inclusive
            int y_rand = new Random().nextInt(upper_bound);
            this.place(x_rand, y_rand); // places and updates molesLeft
        }
    }

    /*
     Whacking moles
     Must determine if mole is present in coordinates, then update moleGrid if required.
    */
    public void whack(int x, int y) {
        /**
         * Checks coordinates of mole grid to see if a mole exists there.
         * If a mole does exist, it updates the moleGrid to reflect that the mole has been whacked
         */
        if (isMoleHere(x, y)) { // TODO contains mole method
            this.moleGrid[x][y] = 'W'; // update mole to whacked
            this.score++;
            this.molesLeft--;
        }
        this.attemptsLeft--;
    }

    private boolean isMoleHere(int x, int y) { // custom function for see if mole exists
        /**
         * Checks coordinates of mole grid to see if a mole exists there.
         * Returns true or false, but does not update the grid.
         */
        boolean moleFound = this.moleGrid[x][y] == 'M';
        if (moleFound) {
            System.out.println("Mole found!");
        } else {
            System.out.println("No mole here.");
        }
        return moleFound;
    }

    /*
     Displaying Mole grid to users. Requires moles be hidden before printing to console.
    */
    public void printGridToUser() {
        /**
         * Displays a version of the mole grid to users where unwhacked moles are hidden.
         */
        char[][] userMoleGrid = hideMolesInGridForUser(); // hide any unwhacked moles before printing. Doesn't update original moleGrid.
        for (int row = 0; row < userMoleGrid.length; row++) {
            System.out.println(userMoleGrid[row]);
        }
    }

    private char[][] hideMolesInGridForUser() {
        /**
         * Returns the original moleGrid with any unwhacked moles hidden.
         * Whacked moles are still displayed as 'W'
         */
        char[][] userMoleGrid = this.moleGrid; // copies moleGrid into a version for users.
        for (int row = 0; row < userMoleGrid.length; row++) {
            for (int col = 0; col < userMoleGrid[row].length; col++) {
                userMoleGrid[row][col] = hideSingleMole(userMoleGrid[row][col]);
            }
        }
        return userMoleGrid;
    }

    private char hideSingleMole(char gridValue) {
        /**
         * Looks at a single mole grid value and hides it if a mole exists.
         * This is to prevent users from seeing moles.
         */
        if (gridValue == 'M') {
            return '*'; // hides mole value only if unwhacked
        }
        return gridValue;
    }

}
