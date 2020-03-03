package tictactoe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

// let's make a class for input handling to avoid Scanner instance being multiplied

class InputHandler {
    private Scanner in = new Scanner(System.in);
    private String input;

    public InputHandler() {
        this.input = "";
    }

    public String getInput() {
        return input;
    }

    public void setInput() {
        this.input = in.nextLine();
    }
}

// another class to store data of game field and analyze it

class GameField {
// char matrix to store symbols
    private char[][] field = new char[3][3];
// constructor is used to fill in the matrix from String input
    public GameField(String field) {
        for (int i = 0; i < 3; i++) {
            String temp = field.substring(3*i, 3*i + 3);
            for (int j = 0; j < 3; j++) {
                this.field[i][j] = temp.charAt(j);
            }
        }
    }

    public char[][] getField() {
        return field;
    }

    public void setField(char[][] field) {
        this.field = field;
    }
// in case of the need to change the field state
    public void setField(String field) {
        for (int i = 0; i < 3; i++) {
            String temp = field.substring(3*i, 3*i + 3);
            for (int j = 0; j < 3; j++) {
                this.field[i][j] = temp.charAt(j);
            }
        }
    }
// a method for field state analysis
    public FieldState getFieldState() {
        switch(analyseState()) {
            case "X":
                return FieldState.X_WINS;
            case"O":
                return FieldState.O_WINS;
            case "Draw":
                return FieldState.DRAW;
            case "Impossible":
                return FieldState.IMPOSSIBLE;
            case "Game not finished":
                return FieldState.GAME_NOT_FINISHED;
            default:
                System.out.println("Can't resolve");
                return null;
        }
    }
// method for blank spaces and their position detection
// the blanks are stored as indexes of matrix in a hash map, there key is the row number, value is the column
    public HashMap<Integer, Integer> blanksFinding() {
        HashMap<Integer, Integer> blanks = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.field[i][j] == ' ' || this.field[i][j] == '_') {
                    blanks.put(i, j);
                };
            }
        }
        return blanks;
    }
// a method for defining whether there are 3 Xs or Os in a row, column or diagonal
// the result depends on who wins
    public String analyseState() {
        boolean isXWinner = false;
        boolean isOWinner = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // iterating matrix, searching for Xs
                if (this.field[i][j] == 'X') {
                    if (j == 0 && !isXWinner) {
                        if (searchForRowsOfChars(1, i, 'X', 3) == 3) {
                            isXWinner = true;
                        }
                    }
                    if (i == 0 && !isXWinner) {
                        if (searchForRowsOfChars(2, j, 'X', 3) == 3) {
                            isXWinner = true;
                        }
// diagonal search
                    }
                    if (i == 0 && j == 0 && !isXWinner) {
                        if (searchForRowsOfChars(3, 0, 'X', 3) == 3) {
                            isXWinner = true;
                        }
                    }
                    if (i == 0 && j == 2 && !isXWinner) {
                        if (searchForRowsOfChars(4, 0, 'X', 3) == 3) {
                            isXWinner = true;
                        }
                    }
                } else if (this.field[i][j] == 'O') {
// iterating matrix, searching for Os
                    if (j == 0 && !isOWinner) {
                        if (searchForRowsOfChars(1, i, 'O', 3) == 3) {
                            isOWinner = true;
                        }
                    }
                    if (i == 0 && !isOWinner) {
                        if (searchForRowsOfChars(2, j, 'O', 3) == 3) {
                            isOWinner = true;
                        }
// diagonal search
                    }
                    if (i == 0 && j == 0 && !isOWinner) {
                        if (searchForRowsOfChars(3, 0, 'O', 3) == 3) {
                            isOWinner = true;
                        }
                    }
                    if (i == 0 && j == 2 && !isOWinner) {
                        if (searchForRowsOfChars(4, 0, 'O', 3) == 3) {
                            isOWinner = true;
                        }
                    }
                }
            }
        }
        if (isOWinner && isXWinner || Math.abs(countChars('X') - countChars('O')) >= 2) return "Impossible";
        if (isXWinner && !isOWinner) return "X";
        if (isOWinner && !isXWinner) return "O";
        if (!isOWinner && !isXWinner) {
            if (blanksFinding().isEmpty()) {
                return "Draw";
            } else return "Game not finished";
        }
        return null;
    }
// just realising repeated code as a method
// direction = direction of search: 1 - row, 2 - column, 3 - main diagonal, 4 - sec diagonal
    public int searchForRowsOfChars(int direction, int indexToStart, char ch, int howMany) {
        int counter = 0;
        switch (direction) {
            case 1:
                for (int k = 0; k < howMany; k++) {
                    if (this.field[indexToStart][k] == ch) {
                        counter++;
                    } else break;
                }
                break;
            case 2:
                for (int k = 0; k < howMany; k++) {
                    if (this.field[k][indexToStart] == ch) {
                        counter++;
                    } else break;
                }
                break;
            case 3:
                for (int k = 0; k < howMany; k++) {
                    if (this.field[k][k] == ch) {
                        counter++;
                    } else break;
                }
                break;
            case 4:
                for (int k = howMany - 1, i = 0; k >= 0 && i < howMany; k--, i++) {
                    if (this.field[i][k] == ch) {
                        counter++;
                    } else break;
                }
                break;
            }
            return counter;
    }
    // method for specific char counting
    public int countChars(char ch) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (this.field[i][j] == ch) {
                    counter++;
                }
            }
        }
        return counter;
    }
    // method for field visualization
    public void visualizeField() {
        char[][] visualizationArray = copyField(true);
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.printf("| %c %c %c |%n", visualizationArray[i][0], visualizationArray[i][1], visualizationArray[i][2]);
        }
        System.out.println("---------");
    }
    public char[][] copyField(boolean blanksForEmpties) {
        char[][] array = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (blanksForEmpties && this.field[i][j] == '_') {
                    array[i][j] = ' ';
                } else array[i][j] = this.field[i][j];
            }
        }
        return array;
    }

    // processing User's coordinates
    public boolean processCoordinates(String input) {
        String[] stringCoordinates = input.split(" ");
        int x, y;
        try {
            x = Integer.parseInt(stringCoordinates[0]);
            y = Integer.parseInt(stringCoordinates[1]);
        } catch (IllegalArgumentException e) {
            System.out.println("You should enter numbers!");
            return false;
        }
        if (x < 1 || x > 3 || y < 1 || y > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }
        if (this.field[3 - y][x - 1] != 'X' && this.field[3 - y][x - 1] != 'O') {
            if (Main.xOrOs) {
                this.field[3 - y][x - 1] = 'X';
            } else this.field[3 - y][x - 1] = 'O';
            return true;
        } else {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
    }
}



// defining enumeration with possible field states
enum FieldState {
    GAME_NOT_FINISHED, DRAW, X_WINS, O_WINS, IMPOSSIBLE
}


public class Main {
    // global variable, true = Xs move, false = Os move
    public static boolean xOrOs = true;

    public static void main(String[] args) {
        // write your code here
// creating an instance of Scanner and taking input line to the private InputHandler class variable
        InputHandler in = new InputHandler();
// creating an instance of GameField and filling its matrix from String
        GameField field = new GameField("_________");
// estimating state
        field.visualizeField();
        boolean proceed = true;
        while (proceed) {
            System.out.print("Enter the coordinates: ");
            in.setInput();
            proceed = !field.processCoordinates(in.getInput());
            if (!proceed) {
                field.visualizeField();
                xOrOs = !xOrOs;
                switch(field.getFieldState()) {
                    case DRAW:
                        field.visualizeField();
                        System.out.println("Draw");
                        proceed = false;
                        break;
                    case X_WINS:
                        field.visualizeField();
                        System.out.println("X wins");
                        proceed = false;
                        break;
                    case O_WINS:
                        field.visualizeField();
                        System.out.println("O wins");
                        proceed = false;
                        break;
                    case GAME_NOT_FINISHED:
                        proceed = true;
                }
            }
        }
    }
}
