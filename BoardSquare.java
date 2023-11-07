import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Objects;

/*
* Contains:
* - scoreMulti: Numeric value of the multiplier
* - scoreMultiID: Word or Letter multiplier
* - tile: Tile occupying the square
* - x, y: x, y 2D array coordinate of the square
* - isFilled: Whether the BoardSquare is filled or not
* - guiX, guiY: Top left GUI coordinate of the guiBoardSquare
* - isPressed: Whether the guiBoardSquare has been pressed
* */
public class BoardSquare extends StackPane {
    private int scoreMulti;
    private int scoreMultiID; //1 is word multiplier, 2 is letter
    private int x;
    private int y;
    private Tile tile;
    private boolean isFilled;
    private Rectangle guiBoardSquare;
    private double guiX;
    private double guiY;
    private boolean isPressed = false;

    /*
    * Overridden equals() to allow direct comparison (equality and collections matching)
    * of BoardSquare objects throughout the program.
    * */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardSquare that = (BoardSquare) o;
        return scoreMulti == that.scoreMulti && scoreMultiID == that.scoreMultiID && x == that.x && y == that.y && isFilled == that.isFilled && Objects.equals(tile, that.tile);
    }


    /*
    * Overridden hashCode() to allow for mapping throughout the Solver class.
    * */
    @Override
    public int hashCode() {
        return Objects.hash(scoreMulti, scoreMultiID, x, y, tile, isFilled);
    }

    /*
    * Constructor used for WordSolver
    * - xCrd: value to be assigned to x
    * - yCrd: value to be assigned to y
    * Assigns default values to other variables
    * */
    public BoardSquare(int xCrd, int yCrd){
        x = xCrd;
        y = yCrd;
        scoreMulti = 1;
        scoreMultiID = 0;
        isFilled = false;
        tile = new Tile('\0');
    }

    /*
    * Constructor used for Scrabble:
    * - x: value to be assigned to guiX
    * - y: value to be assigned to guiY
    * Initializes all the components for the guiBoardSquare
    * */
    public BoardSquare(int xCrd, int yCrd, double x, double y){
        this.x = xCrd;
        this.y = yCrd;
        guiX = x;
        guiY = y;
        setLayoutX(guiX);
        setLayoutY(guiY);
        guiBoardSquare = new Rectangle(guiX,guiY, 45, 45);
        guiBoardSquare.setStroke(Color.BLACK);
        if(this.x == 7 && this.y == 7) guiBoardSquare.setFill(Color.GOLD);
        else guiBoardSquare.setFill(Color.BLANCHEDALMOND);
        guiBoardSquare.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            isPressed = true;
            if(isPressed) System.out.print("Event is " + this.x + " and eventY is" + this.y + "\n");
        });
        getChildren().add(guiBoardSquare);

    }

    /*
    * Sets the scoreMulti and scoreMultiID:
    * - val: value to be assigned to scoreMulti
    * - id: value to be assigned to scoreMultiID
    * - isGUIGame: whether it is GUI game
    * If isGUIGame is true, assigns the corresponding color based on
    * the scoreMulti and scoreMultiID
    * */
    public void setMultiplier(int val, int id, boolean isGUIGame){
        scoreMulti = val;
        scoreMultiID = id;
        if(isGUIGame) {
            if (scoreMultiID == 1 && scoreMulti == 3) guiBoardSquare.setFill(Color.ORANGE);
            else if (scoreMultiID == 1 && scoreMulti == 2) guiBoardSquare.setFill(Color.LIGHTPINK);
            else if (scoreMultiID == 2 && scoreMulti == 3) guiBoardSquare.setFill(Color.BLUE);
            else if (scoreMultiID == 2 && scoreMulti == 2) guiBoardSquare.setFill(Color.LIGHTBLUE);
            else guiBoardSquare.setFill(Color.BLANCHEDALMOND);

        }
    }

    //Returns the current scoreMulti
    public int getScoreMulti(){
        return scoreMulti;
    }

    //Returns the current word multiplier
    public int getWordMulti(){
        if(scoreMultiID == 1) return scoreMulti;
        else return 0;
    }

    //Returns the current letter multiplier
    public int getLetterMulti(){
        if(scoreMultiID == 2) return scoreMulti;
        else return 0;
    }

    //Returns the current scoreMultiId
    public int getScoreMultiID(){
        return scoreMultiID;
    }

    //Sets isFilled to specified state parameter
    public void setFilled(boolean state){
        isFilled = state;
    }

    //Initializes a Tile set to the specified letter
    public void setTile(char letter){
        if(Character.isUpperCase(letter)){
            this.tile = new Tile(letter);
            setFilled(true);
        } else {
            this.tile = new Tile(letter);
            setFilled(true);
        }
    }

    //Returns the current Tile
    public Tile getTile() {
        return this.tile;
    }

    //Prints the current state of the BoardSquare
    public void printState(){
        if(isFilled){
            System.out.print(" " + tile.getLetter() + " ");
        } else {
            if(scoreMultiID == 1 && scoreMulti > 0) System.out.print(scoreMulti + ". ");
            else if(scoreMultiID == 2 && scoreMulti > 0) System.out.print("." + scoreMulti + " ");
            else System.out.print(".. ");
        }
    }

    //Returns the current state of isFilled
    public boolean isFilled() {
        return isFilled;
    }

    //Returns the current x value
    public int getX() {
        return x;
    }

    //Returns the current y value
    public int getY() {
        return y;
    }

    //Returns the current guiBoardSquare
    public Rectangle getGuiBoardSquare() {
        return guiBoardSquare;
    }

    //Returns the current state of isPressed
    public boolean isSelected(){return isPressed;}

    //Returns the current guiX
    public double getGuiX() {
        return guiX;
    }

    //Returns the current guiY
    public double getGuiY() {
        return guiY;
    }
}
