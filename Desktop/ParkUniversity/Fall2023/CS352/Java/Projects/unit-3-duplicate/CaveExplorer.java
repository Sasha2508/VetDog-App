
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CaveExplorer {
    private char[][] cave;
    private int startRow;
    private int startCol;

    // Constructor with no parameters
    public CaveExplorer() {
        // Define the cave layout as a 2D array
        cave = new char[][] {
                {'R', 'R', 'R', 'R', 'R', 'R'},
                {'R', '.', '.', 'S', 'R', 'R'},
                {'R', '.', 'R', 'R', 'R', 'R'},
                {'R', '.', 'M', 'R', 'R', 'R'},
                {'R', 'R', 'R', 'R', 'R', 'R'},
        };

        // Find the starting position 'S'
        for (int i = 0; i < cave.length; i++) {
            for (int j = 0; j < cave[0].length; j++) {
                if (cave[i][j] == 'S') {
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
        }
    }

    // Constructor with one String parameter to read from a file
    public CaveExplorer(String fname) throws FileNotFoundException {
        Scanner in = new Scanner(new File(fname));

        int rows = in.nextInt();
        int cols = in.nextInt();
        in.nextLine(); // Skip newline character

        cave = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = in.nextLine();
            for (int j = 0; j < cols; j++) {
                cave[i][j] = line.charAt(j);
                if (line.charAt(j) == 'S') {
                    startRow = i;
                    startCol = j;
                }
            }
        }
    }

    // toString method
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (char[] row : cave) {
            for (char cell : row) {
                result.append(cell);
            }
            result.append('\n');
        }
        return result.toString();
    }

    // solve method
    public boolean solve() {
        // Create a queue for BFS (Breadth-First Search)
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[cave.length][cave[0].length];

        // Start from the initial position
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        // Define directions: up, down, left, right
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            // Check if we reached the mirror pool ('M')
            if (cave[row][col] == 'M') {
                return true;
            }

            // Try moving in four directions
            for (int i = 0; i < 4; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                // Check if the new cell is a valid and unvisited path ('.' or 'M')
                if (isValid(newRow, newCol) && !visited[newRow][newCol] &&
                        (cave[newRow][newCol] == '.' || cave[newRow][newCol] == 'M')) {
                    // Mark the new cell as visited and enqueue it for exploration
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }

        // No path to the mirror pool found
        return false;
    }

    // getPath method
    public String getPath() {
        if (!solve()) {
            return ""; // No path, return an empty string
        }

        int row = startRow;
        int col = startCol;
        StringBuilder path = new StringBuilder();

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        char[] directions = {'w', 's', 'e', 'n'};

        boolean[][] visited = new boolean[cave.length][cave[0].length];

        while (cave[row][col] != 'M') {
            visited[row][col] = true;

            boolean foundPath = false;

            for (int i = 0; i < 4; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                if (isValid(newRow, newCol) && cave[newRow][newCol] == '.' && !visited[newRow][newCol]) {
                    row = newRow;
                    col = newCol;
                    path.append(directions[i]);
                    foundPath = true;
                    break;
                }
            }

            if (!foundPath) {
                return ""; // Return an empty string to indicate no path
            }
        }

        return path.toString();
    }


    // Helper method to check if a cell is within bounds and a valid path
    private boolean isValid(int row, int col) {
        return row >= 0 && row < cave.length && col >= 0 && col < cave[0].length;
    }

    // Main method to test the class
    public static void main(String[] args) {
        System.out.println("Starting CaveExplorer");

        // Create a CaveExplorer object and print the starting layout
        CaveExplorer ce1 = new CaveExplorer();
        System.out.println("Initial Layout:");
        System.out.println(ce1.toString());

        // Call solve
        boolean hasPath = ce1.solve();

        // Print the final layout, whether there is a path, and if so, what it is.
        System.out.println("Final Layout:");
        System.out.println(ce1.toString());
        if (hasPath) {
            String path = ce1.getPath();
            if (!path.isEmpty()) {
                System.out.println("Path to the mirror pool: " + path);
            } else {
                System.out.println("No path to the mirror pool.");
            }
        } else {
            System.out.println("No path to the mirror pool.");
        }

        System.out.println("Finished CaveExplorer");
    }
}
