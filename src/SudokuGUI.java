import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class SudokuGUI extends Application {
    private int[][] puzzle = new int[9][9];
    private TextField[][] textFields = new TextField[9][9];

    void getPuzzle(int gameNumber){
        try {
            String filename = "Sudoku" + gameNumber + ".txt";
            Scanner reader = new Scanner( new File( filename ) );
            int val;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j <9 ; j++) {
                    val = Integer.parseInt(reader.next());

                    puzzle[i][j] = val;

                }
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    void solvePuzzle(){
        int[][] tempPuzzle = new int[9][9];
        boolean isValid = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tempPuzzle[i][j] = Integer.parseInt(textFields[i][j].getText());
            }
        }

        for (int i = 0; i < 9; i++) { //check if a valid solution
            for (int j = 0; j < 9; j++) {
                isValid = Sudoku.isValid(i,j,tempPuzzle);
            }
        }

        if (!isValid) { //if not, make the incorrect numbers red, and the unfilled the correct number in blue

            Sudoku.search(puzzle); // find a solution for the puzzle
            Sudoku.printGrid(puzzle);

            //check for unfilled or invalid spots
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String myText = (puzzle[i][j] + "");

                    //if there is an invalid spot, make it red
                    if (!textFields[i][j].getText().equals(puzzle[i][j] + "")) {
                        textFields[i][j].setStyle("-fx-text-fill: red;");
                    }
                    //fill in the blank with correct blue numbers
                    if (textFields[i][j].getText().equals("0")) {
                        textFields[i][j].setText(myText);
                        textFields[i][j].setStyle("-fx-text-fill: blue;");
                    }
                }
            }
        }
        else{
            System.out.println("Correct Puzzle");
        }
    }

    void clearPuzzle(Stage newStage){
        puzzle = new int[9][9];
        textFields = new TextField[9][9];
        newStage.close();
        start(newStage);
    }

    public void start(Stage primaryStage){
        Random random = new Random();
        int rand = random.nextInt(8); //pick a random puzzle 0 - 7 to solve **7 takes a long time to solve**
        System.out.println(rand);
        getPuzzle(rand);
        if (!Sudoku.isValid(puzzle)){
            System.out.println("Invalid input");
            return;
        }

        GridPane sudokuPane = new GridPane();
        Pane myPane = new Pane();
        myPane.setPrefHeight(400);
        myPane.setPrefWidth(400);


        sudokuPane.setPadding(new Insets(20,20,20,20));
        sudokuPane.setHgap(5);
        sudokuPane.setVgap(5);

        Button solve = new Button("Solve");
        solve.setLayoutY(350);
        solve.setLayoutX(130);
        solve.setOnAction(e -> solvePuzzle());
        myPane.getChildren().add(solve);

        Button clear = new Button("Clear");
        clear.setLayoutY(350);
        clear.setLayoutX(220);
        clear.setOnAction(e -> clearPuzzle(primaryStage));
        myPane.getChildren().add(clear);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField text = new TextField();
                text.setMaxWidth(30);
                text.setMaxHeight(10);
                text.setText(puzzle[i][j] + "");
                textFields[i][j] = text;
                sudokuPane.add(text,j,i);
            }
        }

        sudokuPane.setLayoutX(25);
        sudokuPane.setLayoutY(25);
        myPane.getChildren().add(sudokuPane);

        for (int i = 0; i < 2; i++) { //horizontal borders
            Line border = new Line(47,139 + (i * 95),355,139 + (i * 95));
            myPane.getChildren().add(border);
        }

        for (int i = 0; i < 2; i++) { //vertical borders
            Line border = new Line(147 + (i * 105),47,147 + (i * 105),329);
            myPane.getChildren().add(border);
        }

        Scene scene = new Scene(myPane);
        primaryStage.setTitle("Sudoku"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }
}
