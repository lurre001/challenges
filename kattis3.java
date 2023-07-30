import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
// Authour Lukas Eriksson
// Solution to kattis problem flood it
public class kattis3 {

    static int numberOfClicks = 0;
    static int[] numbersChosen = new int[6];

    public static void main(String[] args) {
        int dimensions;
        Scanner sc = new Scanner(System.in);

        int numTestCases = sc.nextInt();

        for (int i = 0; i < numTestCases; i++) {
            numberOfClicks = 0;
            numbersChosen = new int[6];

            dimensions = sc.nextInt();
            Node[][] matrix = new Node[dimensions][dimensions];
            for (int j = 0; j < dimensions; j++) {
                String line = sc.next();
                for (int k = 0; k < dimensions; k++) {
                    int n = Integer.parseInt(String.valueOf(line.charAt(k)));
                    Node node = new Node(n, j, k);
                    matrix[j][k] = node;
                }
            }
            for (int j = 0; j < dimensions; j++) {
                for (int k = 0; k < dimensions; k++) {
                    if (k > 0) {
                        matrix[j][k].setLeft(matrix[j][k - 1]);
                    }
                    if (k < dimensions - 1) {
                        matrix[j][k].setRight(matrix[j][k + 1]);
                    }
                    if (j > 0) {
                        matrix[j][k].setUp(matrix[j - 1][k]);
                    }
                    if (j < dimensions - 1) {
                        matrix[j][k].setDown(matrix[j + 1][k]);
                    }
                }
            }
            playGame(matrix);
        }

        sc.close();
    }

    private static void playGame(Node[][] matrix) {
        // Initialize counters
        int numberOfClicks = 0;
        int[] numbersChosen = new int[6];

        // first click
        Node startNode = matrix[0][0];
        int firstColor;

        // Main game loop
        while (true) {
            firstColor = getOptimalColor(startNode);
            if(firstColor == -1){
                break;
            }
            System.out.println("Next color chosen: " + firstColor);
            startNode.click(startNode.getNumber(), firstColor);
            printMatrix(matrix);
            startNode.visited.clear();
            numberOfClicks++;
            numbersChosen[firstColor - 1]++;
        }

        // Print results
        System.out.println("------------------------");
        System.out.println("Total number of clicks: " + numberOfClicks);
        System.out.println("Color frequencies: " + Arrays.toString(numbersChosen));
        System.out.println("------------------------");
    }

    private static int getOptimalColor(Node node) {
        final HashSet<Node> visited = new HashSet<>();
        int[] amountOfColors = new int[6];
        int[] opti = findOptimalColor(node, node.getNumber(), node.getNumber(), visited, amountOfColors,
                new int[2]);
        System.out.println("Optimal color: " + Arrays.toString(opti));
        int maxIndex = -1;
        int max = -1;
        for (int i = 0; i < opti.length; i++) {
            if (opti[i] > max) {
                max = opti[i];
                maxIndex = i + 1; // Adding 1 as color numbers start from 1
            } else if (opti[i] == max && (i + 1) < maxIndex) {
                maxIndex = i + 1; // Update maxIndex if current color has the same count but a lower index
            }
        }
        if(max == 0) {
            return -1;
        }
        return maxIndex;
    }

    private static int[] findOptimalColor(Node node, int hardStartColor, int startColor, HashSet<Node> visited,
            int[] amountOfColors, int[] fromDirection) {
        int thisColor = node.getNumber();
        ArrayList<Node> neighbours = node.getNeighbours();

        // Base case
        if (node == null || visited.contains(node) || (thisColor != startColor && thisColor != hardStartColor)) {
            return amountOfColors;
        }

        visited.add(node);

        if (thisColor == hardStartColor) {
            for (Node neighbour : neighbours) {
                int[] directionToNeighbour = getDirection(node, neighbour);
                if (Arrays.equals(directionToNeighbour, reverseDirection(fromDirection))) {
                    continue; // Skip the direction from which it came
                }
                findOptimalColor(neighbour, hardStartColor, neighbour.getNumber(), visited, amountOfColors,
                        directionToNeighbour);
            }
        } else if (thisColor == startColor) {
            amountOfColors[thisColor - 1]++;
            for (Node neighbour : neighbours) {
                int[] directionToNeighbour = getDirection(node, neighbour);
                if (Arrays.equals(directionToNeighbour, reverseDirection(fromDirection)) ) {
                    continue; // Skip the direction from which it came
                }else if(neighbour.getNumber() == hardStartColor) {
                    continue; // Skip the direction if it is the same color as the hard start color
                }
                findOptimalColor(neighbour, hardStartColor, startColor, visited, amountOfColors, directionToNeighbour);
            }
        }

        return amountOfColors;
    }

    private static int[] getDirection(Node node, Node neighbour) {
        return new int[] { neighbour.getX() - node.getX(), neighbour.getY() - node.getY() };
    }

    private static int[] reverseDirection(int[] direction) {
        return new int[] { -direction[0], -direction[1] };
    }

    private static void printMatrix(Node[][] matrix) {
        System.out.println("Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j].toString());
            }
            System.out.println();
        }
    }
}

class Node {
    int number, x, y;
    Node left, right, up, down;
    static HashSet<Node> visited = new HashSet<>();

    public Node(int number, int x, int y) {
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setUp(Node up) {
        this.up = up;
    }

    public void setDown(Node down) {
        this.down = down;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int setNumber(int number) {
        return this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public ArrayList<Node> getNeighbours() {
        ArrayList<Node> neighbours = new ArrayList<>();
        if (up != null)
            neighbours.add(up);
        if (down != null)
            neighbours.add(down);
        if (left != null)
            neighbours.add(left);
        if (right != null)
            neighbours.add(right);
        return neighbours;
    }

    public void click(int targetNumber, int newNumber) {
        if (!visited.contains(this) && this.number == targetNumber) {
            this.number = newNumber;
            visited.add(this);
            if (up != null)
                up.click(targetNumber, newNumber);
            if (down != null)
                down.click(targetNumber, newNumber);
            if (left != null)
                left.click(targetNumber, newNumber);
            if (right != null)
                right.click(targetNumber, newNumber);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }

}
