import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.Objects;
/*
* Contains:
* - letter: letter assigned to the Tile
* - points: points of the Tile
* - guiTile: GUI representation of the Tile
* - x: x coordinate of the GUI representation
* - y: y coordinate of the GUI representation
* - letterLabel: letter label of the GUI representation
* - pointsLabel: points label of the GUI representation
* - isPressed: whether the GUI representation has been pressed
* */
public class Tile extends StackPane {
    private char letter;
    private int points;
    private Rectangle guiTile;
    private double x;
    private double y;
    private Label letterLabel;
    private Label pointsLabel;
    private boolean isPressed;

    /*
    * Overridden equals() to all direct comparison and collection
    * searching throughout Solver and Board
    * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && points == tile.points && Double.compare(tile.x, x) == 0 && Double.compare(tile.y, y) == 0 && isPressed == tile.isPressed && Objects.equals(guiTile, tile.guiTile) && Objects.equals(letterLabel, tile.letterLabel) && Objects.equals(pointsLabel, tile.pointsLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, points, guiTile, x, y, letterLabel, pointsLabel, isPressed);
    }

    //Constructor used for WordSolver
    public Tile(char newLetter){
        letter = newLetter;
        setPoints(letter);
        isPressed = false;
    }

    /*
    * Constructor used for Scrabble:
    * Initializes all the standard variables and the GUI representation
    * of the Tile.
    * */
    public Tile(char newLetter, double x, double y){
        letter = newLetter;
        setPoints(letter);
        guiTile = new Rectangle(x, y, 45, 45);
        guiTile.setStroke(Color.BLACK);
        guiTile.setFill(Color.WHITE);
        this.x = x+5;
        this.y = y+5;
        setLayoutX(this.x);
        setLayoutY(this.y);
        letterLabel = new Label(String.valueOf(Character.toUpperCase(letter)));
        pointsLabel = new Label(String.valueOf(points));
        letterLabel.setTextFill(Color.BLACK);
        pointsLabel.setTextFill(Color.BLACK);
        pointsLabel.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, FontPosture.REGULAR,12));
        letterLabel.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 20));
        addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            System.out.print("EventX is " + event.getX() + " and eventY is" + event.getY() + "\n");
        });
        getChildren().add(guiTile);
        getChildren().addAll(letterLabel, pointsLabel);
        setAlignment(letterLabel, Pos.CENTER);
        setAlignment(pointsLabel, Pos.BOTTOM_RIGHT);
        isPressed = false;
    }


    //Assigns the points of the tile corresponding to the letter
    private void setPoints(char letter){
        if(letter == 'a'|| letter == 'e' || letter == 'i' ||
                letter == 'o' || letter == 'u' || letter == 'l' ||
                letter == 'n' || letter == 's' || letter == 't' ||
                letter == 'r'){
            points = 1;
        } else if(letter == 'd' || letter == 'g'){
            points = 2;
        } else if(letter == 'b' || letter == 'c' ||
                letter == 'm' || letter == 'p'){
            points = 3;
        } else if(letter == 'f' || letter == 'h' ||
                letter == 'v' || letter == 'w' || letter == 'y'){
            points = 4;
        } else if(letter == 'k'){
            points = 5;
        } else if(letter == 'j' || letter == 'x'){
            points  = 8;
        } else if(letter == 'q' || letter == 'z'){
            points = 10;
        } else {
            points = 0;
        }
    }

    //Returns the points of the Tile
    public int getPoints(){
        return points;
    }

    //Returns the character of the Tile
    public char getLetter(){return this.letter;}

    /*
    * Initializes all the components of the GUI representation
    * of the Tile.
    * */
    public void setGuiTile(double newX, double newY){
        x = newX;
        y = newY;
        guiTile = new Rectangle(x+3, y, 45, 45);
        setLayoutX(x);
        setLayoutY(y);
        guiTile.setStrokeWidth(1.25);
        guiTile.setStroke(Color.BLACK);
        guiTile.setFill(Color.BLANCHEDALMOND);
        letterLabel = new Label(String.valueOf(Character.toUpperCase(letter)));
        pointsLabel = new Label(String.valueOf(points));
        letterLabel.setTextFill(Color.BLACK);
        pointsLabel.setTextFill(Color.BLACK);
        pointsLabel.setFont(Font.font("Calibri", FontWeight.SEMI_BOLD, FontPosture.REGULAR,12));
        letterLabel.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 20));
        addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            //System.out.print("EventX is " + event.getX() + " and eventY is" + event.getY() + "\n");
            isPressed = true;
        });
        this.getChildren().removeAll();
        getChildren().add(guiTile);
        getChildren().addAll(letterLabel, pointsLabel);
        setAlignment(letterLabel, Pos.CENTER);
        setAlignment(pointsLabel, Pos.BOTTOM_RIGHT);
        isPressed = false;
    }

    //Returns the current value of isPressed
    public boolean isSelected(){return isPressed;}
}
