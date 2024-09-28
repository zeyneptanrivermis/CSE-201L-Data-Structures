import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HW01_20220808038 {
    public static void main(String[] args) {
        // Create a TrafficControl instance with a grid of size 5x5
        TrafficControl trafficControl = new TrafficControl(5, 5);

        // Print the city grid
        System.out.println(trafficControl);

        // Count and print the number of malfunctioning lights
        int malfunctioningCount = trafficControl.countMalfunctioningLights();
        System.out.println("Number of malfunctioning lights: " + malfunctioningCount);

        // Find and print the row or column with the most malfunctioning lights
        String mostMalfunction = trafficControl.mostMalfunction();
        System.out.println(mostMalfunction);

        // Test count of malfunctioning neighbors for a specific intersection
        int row = 2;
        int col = 2;
        int neighborCount = trafficControl.countMalfunctioningNeighbors(row, col);
        System.out.println("Malfunctioning neighbors of (" + row + ", " + col + "): " + neighborCount);

        // Find and print crucial intersections
        List<int[]> crucialIntersections = trafficControl.crucialIntersections();
        System.out.println("Crucial Intersections (2 or more malfunctioning neighbors):");
        for (int[] intersection : crucialIntersections) {
            System.out.println("(" + intersection[0] + ", " + intersection[1] + ")");
        }
    }
}

interface ITrafficControl {
    int[][] getCityGrid();
    int countMalfunctioningLights();
    String mostMalfunction();
    int countMalfunctioningNeighbors(int row, int col);
    List<int[]> crucialIntersections();
    String toString();
}

class TrafficControl implements ITrafficControl {
    int[][] cityGrid;

    //  Creating a 2D array with random numbers for city grid
    public TrafficControl(int m, int n) {
        this.cityGrid = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cityGrid[i][j] = (int) (Math.random() * 3);
            }
        }
    }

    public TrafficControl(int[][] cityGrid) {
        this.cityGrid = cityGrid;
    }

    @Override
    public int[][] getCityGrid() {
        return cityGrid;
    }

    //  Finding the number of malfunctioning lights
    @Override
    public int countMalfunctioningLights() {
        int count = 0;

        for (int i = 0; i < cityGrid.length; i++) {
            for (int j = 0; j < cityGrid[i].length; j++) {
                if (cityGrid[i][j] == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    //  Finding which row or column has the most malfunctioning lights
    @Override
    public String mostMalfunction() {
        int countRow = 0;
        int whichRow = 0;
        int countColumn = 0;
        int whichColumn = 0;

        //  Checking which row have most malfunction
        for (int i = 0; i < cityGrid.length; i++) {
            int count = 0;
            for (int j = 0; j < cityGrid[i].length; j++) {
                if (cityGrid[i][j] == 1) {
                    count++;
                }
            }
            if (count >= countRow) {
                countRow = count;
                whichRow = i;
            }
        }

        //  Checking which coloumn have most malfunction
        for (int j = 0; j < cityGrid[0].length; j++) {
            int count = 0;
            for (int i = 0; i < cityGrid.length; i++) {
                if (cityGrid[i][j] == 1) {
                    count++;
                }
            }
            if (count >= countColumn) {
                countColumn = count;
                whichColumn = j;
            }
        }

        if (countColumn > countRow) {
            return "Column: " + whichColumn;
        }

        return "Row: " + whichRow;
    }

    //  Checking how many neighbors of a specific coordinate have malfunctioning lights
    @Override
    public int countMalfunctioningNeighbors(int row, int col) {
        int count = 0;

        // Up
        if (row - 1 >= 0 && cityGrid[row - 1][col] == 1) {count++;}
        // Right
        if (col + 1 < cityGrid[0].length && cityGrid[row][col + 1] == 1) {count++;}
        // Down
        if (row + 1 < cityGrid.length && cityGrid[row + 1][col] == 1) {count++;}
        // Left
        if (col - 1 >= 0 && cityGrid[row][col - 1] == 1) {count++;}

        return count;
    }

    //  That returns a list of all intersections that have two or more malfunctioning neighbors
    @Override
    public List<int[]> crucialIntersections() {
        List<int[]> crucialList = new ArrayList<>();

        for (int i = 0; i < cityGrid.length; i++) {
            for (int j = 0; j < cityGrid[i].length; j++) {
                if (countMalfunctioningNeighbors(i, j) >= 2) {
                    crucialList.add(new int[]{i, j});
                }
            }
        }
        return crucialList;
    }

    //  Writing the city grid
    @Override
    public String toString() {
        //  We create a string builder so that we can space between rows and columns
        StringBuilder sb = new StringBuilder();
        sb.append("Traffic Control \n");
        sb.append("City Grid: \n");

        for (int i = 0; i < cityGrid.length; i++) {
            for (int j = 0; j < cityGrid[i].length; j++) {
                // Append each element with a space
                sb.append(cityGrid[i][j]).append(" ");
            }
            // New line after each row
            sb.append("\n");
        }
        return sb.toString();
    }
}