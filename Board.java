import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.LinkedList;

public class Board extends Group {
    private final int size;
    private final TileBag bag;
    private final ArrayList<Tile> human;
    private final ArrayList<Tile> comp;
    private BoardSquare[][] gameBoard;
    private Lexicon dictionary = new Lexicon();
    private boolean isHumanTurn = true;
    private boolean isFirstTurn = true;
    private boolean firstPress = true;
    private Tile selectedTile;
    private BoardSquare anchorSquare;
    private BoardSquare lastSelectedSquare;
    private LinkedList<BoardSquare> availablePositions;
    private ArrayList<Tile> allSelectedTiles;
    private ArrayList<BoardSquare> allSelectedSquares;
    private boolean isFirstSquare = true;
    private int direction = 0;
    private int humanScore;
    private int compScore;
    private int numTilesUsed = 0;
    private Solver humanSolver;
    private Solver compSolver;
    private StringBuilder wordAttempt = new StringBuilder();
    private Text humanScoreText;
    private Text compScoreText;


    /*
    * This constructor is used solely for the GUI version of the game.
    * - stdinBoard: string representation of an empty board to allocate appropriate
    * letter and word multipliers
    * - newDictionary: Lexicon for this game
    * - eventHandler: The majority of the game logic/loop is contained within this eventHandler.
    * I opted for this as it simplified the turn swaps.
    */
    public Board(ArrayList<String> stdinBoard, Lexicon newDictionary){
        size = 15;
        gameBoard = new BoardSquare[15][15];
        bag = new TileBag();
        dictionary = newDictionary;
        human = bag.getHand(false);
        comp = bag.getHand(true);
        allSelectedTiles = new ArrayList<>();
        allSelectedSquares = new ArrayList<>();
        humanScore = 0;
        for(int i = 0; i < 15; i++){
            int scoreMulti;
            char[] multi = new char[2];
            int multiIndex = 0;
            int index = 0;
            for(int j = 0; j < stdinBoard.get(i).length()+1; j++){
                if(j % 3 != 2){
                    multi[multiIndex] = stdinBoard.get(i).charAt(j);
                    multiIndex++;
                } else {
                    gameBoard[i][index] = new BoardSquare(i, index, i * 45, index * 45);
                    if(multi[0] == '.' && multi[1] == '.') {
                        getChildren().addAll(gameBoard[i][index], gameBoard[i][index].getGuiBoardSquare());
                        index++;
                    }
                    else if(Character.isDigit(multi[0])){
                        scoreMulti = Character.getNumericValue(multi[0]);
                        gameBoard[i][index].setMultiplier(scoreMulti, 1, true);
                        getChildren().addAll(gameBoard[i][index], gameBoard[i][index].getGuiBoardSquare());
                        index++;
                    } else if(Character.isDigit(multi[1])){
                        scoreMulti = Character.getNumericValue(multi[1]);
                        gameBoard[i][index].setMultiplier(scoreMulti, 2, true);
                        getChildren().addAll(gameBoard[i][index], gameBoard[i][index].getGuiBoardSquare());
                        index++;
                    }
                    multiIndex = 0;
                }
            }
        }
        availablePositions = getAllPositions();
        //Will automatically add any existing letters on the board to the attempted word if they intersect
        //in the current direction.
        addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if(isHumanTurn){
                if(isFirstTurn){
                    if(firstPress){
                        findPressedTile();
                        firstPress = false;
                    } else {
                        BoardSquare selectedSquare = findPressedSquare();
                        if(isFirstSquare && selectedSquare.getX() == 7 && selectedSquare.getY() == 7){
                            anchorSquare = selectedSquare;
                            firstPress = true;
                            playTile(selectedTile, selectedSquare, 1);
                            isFirstSquare = false;
                            isFirstTurn = false;
                        } else {
                            System.out.print("First play must be from the center square!\n");
                            firstPress = true;
                            availablePositions.add(selectedSquare);
                            allSelectedSquares.remove(selectedSquare);
                        }
                    }
                } else {
                    if(firstPress){
                        findPressedTile();
                        firstPress = false;
                    } else {
                        BoardSquare selectedSquare = findPressedSquare();
                        if(isFirstSquare && selectedSquare != null && humanSolver.findAnchors().contains(selectedSquare)){
                            anchorSquare = selectedSquare;
                            firstPress = true;
                            playTile(selectedTile, selectedSquare, 1);
                            isFirstSquare = false;
                        } else if(!isFirstSquare && !selectedSquare.isFilled() && selectedTile != null){
                            if(allSelectedTiles.size() < 2 && validPlacement(anchorSquare, selectedSquare)){
                                if(anchorSquare.getY() == selectedSquare.getY()) direction = 1;
                                else direction = 2;
                                firstPress = true;
                                playTile(selectedTile, selectedSquare, 1);
                            } else if(validPlacement(lastSelectedSquare, selectedSquare)){
                                firstPress = true;
                                playTile(selectedTile, selectedSquare, 1);
                            }
                        } else {
                            System.out.print("Must be played from an anchor square!\n");
                            availablePositions.add(selectedSquare);
                            allSelectedSquares.remove(selectedSquare);
                            allSelectedTiles.remove(selectedTile);
                        }
                    }
                }
            }
        });
        initScoreBox();
        initButtons();
        displayGUIHand();
    }

    /*
    * Constructor used for getBoardCopy()
    * - size: Size of the board
    * - newHand: Current computer hand
    * - newDict: Current Lexicon
    * */
    public Board(int size, ArrayList<Tile> newHand, Lexicon newDict){
        this.size = size;
        gameBoard = new BoardSquare[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                gameBoard[i][j] = new BoardSquare(i, j);
            }
        }
        bag = new TileBag();
        human = bag.getHand(false);
        comp = newHand;
    }

    /*
    * Constructor used for WordSolver.
    * - stdinBoard: String representation of given board
    * - size: Size of the given board
    * - compHand: Given hand for computer
    * - dict: Lexicon created from the given word list
    * */
    public Board(ArrayList<String> stdinBoard, int size, ArrayList<Tile> compHand, Lexicon dict){
        gameBoard = new BoardSquare[size][size];
        dictionary = dict;
        bag = new TileBag();
        this.size = size;
        for(int i = 0; i < stdinBoard.size(); i++){
            int scoreMulti;
            char[] multi = new char[2];
            int multiIndex = 0;
            int index = 0;
            for(int j = 0; j < stdinBoard.get(i).length() + 1; j++){
                if(j % 3 != 2){
                    multi[multiIndex] = stdinBoard.get(i).charAt(j);
                    multiIndex++;
                } else {
                    gameBoard[i][index] = new BoardSquare(i, index);
                    if(multi[0] == '.' && multi[1] == '.') index++;
                    else if(Character.isDigit(multi[0])){
                        scoreMulti = Character.getNumericValue(multi[0]);
                        gameBoard[i][index].setMultiplier(scoreMulti, 1, false);
                        index++;
                    } else if(Character.isDigit(multi[1])){
                        scoreMulti = Character.getNumericValue(multi[1]);
                        gameBoard[i][index].setMultiplier(scoreMulti, 2, false);
                        index++;
                    } else if(Character.isAlphabetic(multi[1])){
                        gameBoard[i][index].setTile(multi[1]);
                        gameBoard[i][index].setFilled(true);
                        bag.removeLetter(gameBoard[i][index].getTile());
                        index++;
                    }
                    multiIndex = 0;
                }
            }
        }
        human = bag.getHand(false);
        comp = compHand;
    }

    public LinkedList<BoardSquare> getAllPositions(){
        LinkedList<BoardSquare> result = new LinkedList<>();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                result.add(gameBoard[i][j]);
            }
        }
        return result;
    }

    /* Finds and returns a BoardSquare corresponding to the 'pos' parameter.
     * Mainly used for the Solver class.
     */
    public BoardSquare getSquare(int[] pos){
        if(validPosition(pos)) return gameBoard[pos[0]][pos[1]];
        else return null;
    }

    /*
     * Sets the specified BoardSquare (pos) tile to the specified character (newTile)
     * Mainly used for the Solver class.
     */
    public void setSquareTile(int[] pos, char newTile){
        gameBoard[pos[0]][pos[1]].setTile(newTile);
    }

    /*
    * Determines if the passed coordinate (pos) corresponds to a valid board position
    * Mainly used for the Solver class.
    * */
    public boolean validPosition(int[] pos){
       if(pos[0] >= 0 && pos[0] < size
                && pos[1] >= 0 && pos[1] < size) return true;
       else return false;
    }

    /*
    * Creates and returns a copy of the current board.
    * Mainly used for the Solver class.
    * */
    public Board getBoardCopy(){
        Board result = new Board(size, comp, dictionary);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                int[] temp = new int[]{i, j};
                result.getSquare(temp).setMultiplier(gameBoard[i][j].getScoreMultiID(), gameBoard[i][j].getScoreMulti(), false);
                if(gameBoard[i][j].isFilled()) result.setSquareTile(temp, this.getSquare(temp).getTile().getLetter());
            }
        }
        return result;
    }

    public ArrayList<Tile> getHuman(){
        return human;
    }

    public ArrayList<Tile> getComp() {
        return comp;
    }


    public void printBoard(){
        for (BoardSquare[] boardSquares : gameBoard) {
            for (BoardSquare boardSquare : boardSquares) {
                if(boardSquare != null) boardSquare.printState();
            }
            System.out.print("\n");
        }
    }

    //Prints the computers current hand/rack
    public void printCompHand(){
        for(int i = 0; i < comp.size(); i++){
            System.out.print(comp.get(i).getLetter());
        }
        System.out.print("\n");
    }

    //Gets the size of the board
    public int getSize() {
        return size;
    }


    //Displays/updates the players current hand/rack
    public void displayGUIHand(){
        double x = 1;
        double y = 690;
        for(int i = 0; i < human.size(); i++){
            human.get(i).setGuiTile(x+i*45, y);
            getChildren().add(human.get(i));
            x+=5;
        }
    }

    /*
    * Locates and returns the last pressed square and removes it from the
    * availablePositions list.
    * */
    private BoardSquare findPressedSquare(){
        BoardSquare pressedSquare;
        for(BoardSquare temp : availablePositions){
            if(temp.isSelected()){
                pressedSquare = temp;
                availablePositions.remove(temp);
                return pressedSquare;
            }
        }
        return null;
    }

    /*
    * Locates the last selected tile from the player's rack,
    * then assigns it to selectedTile.
    * */
    private void findPressedTile(){
        for(int i = 0; i < human.size(); i++){
            if(human.get(i).isSelected()) {
                selectedTile = human.get(i);
                human.remove(i);
            }
        }
    }

    /*
    * Initializes and defines the event handlers for the "Submit Play"
    * and "Draw Tile" button.
    * - Submit Play: Tests the current play to see if it is legal. If it is
    * it sets the selected BoardSquares to filled and switches the turn, and
    * resets necessary attempted play values.
    * - Draw Tile: If it is the player's turn, if there are less than 7 tiles in
    * the current rack, and they haven't begun a play, it will add a tile to the
    * player's rack.
    * */
    private void initButtons(){
        Button submitPlay = new Button("Submit Play");
        submitPlay.setLayoutX(575);
        submitPlay.setLayoutY(695);
        submitPlay.addEventHandler(MouseEvent.MOUSE_CLICKED, event1 -> {
            if(isHumanTurn){
                int[] testCrd = new int[]{anchorSquare.getX(), anchorSquare.getY()};
                if(humanSolver.validPlay(wordAttempt.toString(), testCrd, direction, numTilesUsed)){
                    for(BoardSquare temp : allSelectedSquares) {temp.setFilled(true);}
                    allSelectedSquares.removeAll(allSelectedSquares);
                    allSelectedTiles.removeAll(allSelectedTiles);
                    isHumanTurn = false;
                    firstPress = true;
                    isFirstSquare = true;
                    numTilesUsed = 0;
                    lastSelectedSquare = null;
                    anchorSquare = null;
                    humanScore += humanSolver.getHighestScore();
                    wordAttempt = new StringBuilder();
                    humanSolver = new Solver(dictionary, human, this);
                    initScoreBox();
                    computerPlay();
                    if(isEOG()) System.exit(0);
                } else {
                    for(BoardSquare temp : allSelectedSquares){
                        temp.setTile('\0');
                        availablePositions.add(temp);
                    }
                    for(Tile temp2 : allSelectedTiles){
                        temp2.setVisible(false);
                        human.add(temp2);
                    }
                    for(Tile temp3 : human) temp3.setVisible(true);
                    allSelectedSquares.removeAll(allSelectedSquares);
                    allSelectedTiles.removeAll(allSelectedTiles);
                    humanSolver = new Solver(dictionary, human, this);
                    firstPress = true;
                    isFirstSquare = true;
                    numTilesUsed = 0;
                    lastSelectedSquare = null;
                    anchorSquare = null;
                    wordAttempt = new StringBuilder();
                    displayGUIHand();
                }
            }
        });
        getChildren().add(submitPlay);
        Button drawTile = new Button("Draw Tile");
        drawTile.setLayoutX(500);
        drawTile.setLayoutY(695);
        drawTile.addEventHandler(MouseEvent.MOUSE_CLICKED, event1 -> {
            if(human.size() < 7 && isFirstSquare){
                human.add(bag.drawTile(false));
                displayGUIHand();
            } else {
                System.out.print("You already have 7 tiles in your rack!\n");
            }
        });
        getChildren().add(drawTile);
    }

    /*
    * Tests the given BoardSquare (newSquare) against the last BoardSquare in the
    * current play (lastSquare) to ensure it is only moving in one direction.
    * */
    private boolean validPlacement(BoardSquare lastSquare, BoardSquare newSquare){
        if(lastSquare.getX() == newSquare.getX() && lastSquare.getY()+1 == newSquare.getY()) return true;
        else if(lastSquare.getY() == newSquare.getY() && lastSquare.getX()+1 == newSquare.getX()) return true;
        System.out.print("validPlacement() failed\n");
        return false;
    }

    /*
    * This displays the selected tile of the current play on the board. This specifically doesn't
    * mark it as filled (for player) so as to not interfere with the Solver's play checking.
    * It also adds any intersecting letters to the wordAttempt based on the current direction.
    * Will display tiles for either the player or the computer based on the 'player' parameter.
    * */
    public void playTile(Tile selectedTile, BoardSquare selectedSquare, int player){
        if(player == 1){
            human.remove(selectedTile);
            selectedTile.setGuiTile(selectedSquare.getGuiX(), selectedSquare.getGuiY());
            getChildren().add(selectedTile);
            allSelectedTiles.add(selectedTile);
            allSelectedSquares.add(selectedSquare);
            selectedSquare.setTile(selectedTile.getLetter());
            selectedSquare.setFilled(false);
            wordAttempt.append(selectedTile.getLetter());
            lastSelectedSquare = selectedSquare;
            numTilesUsed++;
            if(isFirstSquare){
                int offset = 1;
                while(selectedSquare.getY() - offset >= 0 && gameBoard[selectedSquare.getX()][selectedSquare.getY()-offset].isFilled()){
                    wordAttempt.insert(0, gameBoard[selectedSquare.getX()][selectedSquare.getY() - offset].getTile().getLetter());
                    System.out.print("y-1 word is: " + wordAttempt + "\n");
                    offset++;
                }
                offset = 1;
                while(selectedSquare.getX() - offset >= 0 && gameBoard[selectedSquare.getX()-offset][selectedSquare.getY()].isFilled()){
                    direction = 1;
                    wordAttempt.insert(0, gameBoard[selectedSquare.getX()-offset][selectedSquare.getY()].getTile().getLetter());
                    System.out.print("x-1 word is: " + wordAttempt + "\n");
                    offset++;
                }
            }
            int offset = 1;
            while(selectedSquare.getY() + offset < 15 && gameBoard[selectedSquare.getX()][selectedSquare.getY()+offset].isFilled()){
                direction = 2;
                wordAttempt.append(gameBoard[selectedSquare.getX()][selectedSquare.getY()+1].getTile().getLetter());
                if(direction == 2) lastSelectedSquare = gameBoard[selectedSquare.getX()][selectedSquare.getY()+offset];
                System.out.print("y+1 word is: " + wordAttempt + "\n");
                offset++;
            }
            offset = 1;
            while(selectedSquare.getX() + offset < 15 && gameBoard[selectedSquare.getX()+offset][selectedSquare.getY()].isFilled()){
                wordAttempt.append(gameBoard[selectedSquare.getX()+1][selectedSquare.getY()].getTile().getLetter());
                if(direction == 1) lastSelectedSquare = gameBoard[selectedSquare.getX()+offset][selectedSquare.getY()];
                System.out.print("x+1 word is: " + wordAttempt + "\n");
                offset++;
            }
            displayGUIHand();
        } else {
            comp.remove(selectedTile);
            selectedTile.setGuiTile(selectedSquare.getGuiX(), selectedSquare.getGuiY());
            getChildren().add(selectedTile);
            selectedSquare.setTile(selectedTile.getLetter());
        }

    }

    //Sets the Solver objects for each player
    public void setSolvers(Solver newHumanSolver, Solver newCompSolver){
        humanSolver = newHumanSolver;
        compSolver = newCompSolver;
    }

    /*
    * Initializes/updates the Score Box. Scores are linked to the humanScore
    * and compScore variables.
    * */
    public void initScoreBox(){
        Rectangle scoreBox = new Rectangle(700, 15, 300, 100);
        Label scoreBoxLabel = new Label("Score");
        humanScoreText = new Text(720,70,"Your Score:  " + this.humanScore);
        compScoreText = new Text(720, 90, "Computer Score:  " + this.compScore);
        scoreBox.setStrokeWidth(2);
        scoreBox.setArcHeight(15);
        scoreBox.setArcWidth(15);
        scoreBox.setStroke(Color.BLACK);
        scoreBox.setFill(Color.WHITE);
        scoreBoxLabel.setLayoutX(830);
        scoreBoxLabel.setLayoutY(25);
        scoreBoxLabel.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 20));
        humanScoreText.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 15));
        compScoreText.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 15));
        getChildren().add(scoreBox);
        getChildren().addAll(scoreBoxLabel, humanScoreText, compScoreText);
    }

    /*
    * Handles the play logic and bookkeeping for the computer's turn. At the end
    * it refills the computer's rack and resets its Solver object.
    * */
    public void computerPlay(){
        System.out.print("computerPlay() called\n");
        printCompHand();
        compSolver.findAllPlays(true);
        compScore += compSolver.getHighestScore();
        initScoreBox();
        fillCompHand();
        isHumanTurn = true;
        compSolver = new Solver(dictionary, comp, this);
        if(isEOG()) System.exit(0);
        System.out.print("Made it to the end of computerPlay()\n");
    }

    //Fills the computer's rack
    public void fillCompHand() {
        while(comp.size() < 7) comp.add(bag.drawTile(true));
    }

    //Detects EOG conditions
    public boolean isEOG(){
        if(bag.getSize() == 0) return true;
        else if(getAllPositions().size() < 5) return true;
        else return false;
    }
}
