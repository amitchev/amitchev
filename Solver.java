import java.util.*;

/*
* Finds the highest scoring play for the computer player and
* if the human player's move is valid. I used the algorithm described
* in the "World's Greatest Scrabble Program" paper provided by Professor
* Chenoweth
* Contains:
* - dictionary: Lexicon for the solver to traverse
* - hand: computer player's hand
* - solverBoard: board for the Solver to produce solutions off of
* - crossCheckMap: HashMap with BoardSquare as keys and list of possible letters that
* can be placed at each BoardSquare
* - direction: whether the Solver will look for horizontally or vertically for solutions
* - highestScore: current Highest Score
* - highestDir: direction of the current highest scoring play
* - highestWord: word of the current highest scoring play
* - rightLimReached: Used to help with traversing the board
* - isGUIGame: Whether the GUI version of the game is being played
* */
public class Solver {
    private final Lexicon dictionary;
    private final ArrayList<Tile> hand;
    private final Board solverBoard;
    private Map<BoardSquare, ArrayList<Character>> crossCheckMap;
    private int direction;
    private int highestScore = 0;
    private int[] highestCrd;
    private int highestDir;
    private String highestWord;
    private boolean rightLimReached = false;
    private ArrayList<Character> placeHolder = new ArrayList<>();
    private boolean isGUIGame;

    /*
    * Standard constructor used by both programs, assigns values to the
    * specified variables.
    * */
    public Solver(Lexicon dict, ArrayList<Tile> newHand, Board newBoard){
        dictionary = dict;
        hand = newHand;
        solverBoard = newBoard;
        direction = 1;
    }

    /*
    * Returns the position to the left of the current position,
    * depending on the current direction being searched for.
    * */
    public int[] getLeft(int[] pos){
        if (direction == 1 && pos[1] - 1 >= 0) {
            return new int[]{pos[0], pos[1]-1};
        } else if(direction == 2 && pos[0]-1 >= 0){
            return new int[]{pos[0]-1, pos[1]};
        } else return pos;

    }

    /*
    * Returns the position to the right of the current position,
    * depending on the current direction being searched for.
    * */
    public int[] getRight(int[] pos){
        if (direction == 1 && pos[1] + 1 < solverBoard.getSize()) {
            return new int[]{pos[0], pos[1]+1};
        } else if(direction == 2 && pos[0] + 1 < solverBoard.getSize()){
            return new int[]{pos[0]+1, pos[1]};
        } else {
            rightLimReached = true;
            return pos;
        }
    }

    /*
    * Returns the position above the current position, depending
    * on the current direction being searched for.
    * */
    public int[] getUp(int[] pos){
        if (direction == 1 && pos[0] - 1 >= 0) {
            return new int[]{pos[0]-1, pos[1]};
        } else if(direction == 2 && pos[1] - 1 >= 0){
            return new int[]{pos[0], pos[1]-1};
        } else return pos;
    }

    /*
    * Returns the position below the current position, depending
    * on the current direction being searched for.
    * */
    public int[] getDown(int[] pos){
        if (direction == 1 && pos[0] + 1 < solverBoard.getSize()) {
            return new int[]{pos[0]+1, pos[1]};
        } else if(direction == 2 && pos[1] + 1 < solverBoard.getSize()){
            return new int[]{pos[0], pos[1]+1};
        } else {
            return pos;
        }
    }

    /*
    * Returns a list of all BoardSquares on the board that are
    * possible anchor coordinates.
    * */
    public ArrayList<BoardSquare> findAnchors(){
        ArrayList<BoardSquare> anchors = new ArrayList<>();
        for(BoardSquare temp : solverBoard.getAllPositions()){
            int[] tempCrd = new int[]{temp.getX(), temp.getY()};
            if(solverBoard.validPosition(tempCrd)){
                if(!temp.isFilled() && solverBoard.getSquare(getLeft(tempCrd)).isFilled()){
                    anchors.add(temp);
                } else if(!temp.isFilled() && solverBoard.getSquare(getRight(tempCrd)).isFilled()){
                    anchors.add(temp);
                } else if(!temp.isFilled() && solverBoard.getSquare(getDown(tempCrd)).isFilled()){
                    anchors.add(temp);
                } else if(!temp.isFilled() && solverBoard.getSquare(getUp(tempCrd)).isFilled()){
                    anchors.add(temp);
                }
            }
        }
        return anchors;
    }

    /*
    * Main loop for the Solver class:
    * Loops through all the possible anchor coordinates given by
    * findAnchors(), trying both possible directions for all of said
    * anchors.
    * */
    public void findAllPlays(boolean isGUIGame){
        this.isGUIGame = isGUIGame;
        for(int dir = 1; dir < 3; dir++){
            direction = dir;
            ArrayList<BoardSquare> anchors = findAnchors();
            crossCheckMap = crossCheck();
            for(BoardSquare temp : anchors){
                if(solverBoard.getSquare(getLeft(new int[]{temp.getX(), temp.getY()})).isFilled()){
                    int[] leftScan = getLeft(new int[]{temp.getX(), temp.getY()});
                    String partial = solverBoard.getSquare(leftScan).getTile().getLetter() + "";
                    while(solverBoard.getSquare(getLeft(leftScan)).isFilled()){
                        if(getLeft(leftScan) != leftScan) leftScan = getLeft(leftScan);
                        else break;
                        partial = solverBoard.getSquare(leftScan).getTile().getLetter() + partial;
                    }
                    LexiconNode partialNode = dictionary.find(partial);
                    if(partialNode != null){
                        extendWordRight(partial,
                                partialNode,
                                new int[]{temp.getX(), temp.getY()},
                                false, 0);

                    }
                } else {
                    int limit = 0;
                    int[] leftScanPos = new int[]{temp.getX(), temp.getY()};
                    while(!solverBoard.getSquare(getLeft(leftScanPos)).isFilled()
                            && solverBoard.validPosition(getLeft(leftScanPos))
                            && !anchors.contains(solverBoard.getSquare(getLeft(leftScanPos)))){ //todo look into this line
                        limit++;
                        if(getLeft(leftScanPos) !=  leftScanPos) leftScanPos = getLeft(leftScanPos);
                        else break;
                    }
                    getLeftPart("",
                            dictionary.getRoot(),
                            new int[]{temp.getX(), temp.getY()}, limit, 0);
                }
            }
        }
        int playIndex = highestWord.length()-1;
        int[] playPos = highestCrd;
        direction = highestDir;
        if(isGUIGame){
            while(playIndex >= 0){
                solverBoard.setSquareTile(playPos, highestWord.charAt(playIndex));
                solverBoard.playTile(solverBoard.getSquare(playPos).getTile(), solverBoard.getSquare(playPos), 2);
                playIndex-=1;
                if(playIndex >= 0) playPos = getLeft(playPos);
            }
        } else {
            while(playIndex >= 0){
                solverBoard.setSquareTile(playPos, highestWord.charAt(playIndex));
                if(Character.isUpperCase(highestWord.charAt(playIndex))) removeFromHand('*');
                else if(!solverBoard.getSquare(playPos).isFilled()) removeFromHand(highestWord.charAt(playIndex));
                playIndex-=1;
                if(playIndex >= 0) playPos = getLeft(playPos);
            }
            solverBoard.printBoard();
        }


    }

    /*
    * Determines if the move passed in the parameters is a legal move.
    * It scores the play, and if it has a higher score than the current
    * highest scoring play, it replaces the values of the appropriate variables.
    * */
    public void legalMove(String word, int[] pos, int numTilesUsed){
        Board temp = solverBoard.getBoardCopy();
        int[] tempPos = pos;
        int index = word.length()-1;
        int total = 0;
        int wMulti = 0;
        int lMulti = 0;
        int downScore = 0;
        int upScore = 0;
        ArrayList<Integer> additionalWords = new ArrayList<>();
        while(index >= 0){
            if(solverBoard.getSquare(tempPos).isFilled()){
                lMulti += solverBoard.getSquare(tempPos).getTile().getPoints();
            } else {
                wMulti += solverBoard.getSquare(tempPos).getWordMulti();
                if(solverBoard.getSquare(getUp(tempPos)).isFilled()){

                    if(solverBoard.getSquare(tempPos).getScoreMulti() == 2){
                        upScore = (solverBoard.getSquare(tempPos).getLetterMulti()*new Tile(word.charAt(index)).getPoints()) + getUpScore(getUp(tempPos));
                        additionalWords.add(upScore);
                    } else {
                        upScore = getUpScore(getUp(tempPos)) + new Tile(word.charAt(index)).getPoints();
                        additionalWords.add(upScore);
                    }
                }
                if(solverBoard.getSquare(getDown(tempPos)).isFilled()){
                    if(solverBoard.getSquare(tempPos).getScoreMulti() == 2){
                        downScore = (solverBoard.getSquare(tempPos).getLetterMulti()*new Tile(word.charAt(index)).getPoints()) + getDownScore(getDown(tempPos));
                        additionalWords.add(downScore);
                    } else {
                        downScore = getDownScore(getDown(tempPos)) + new Tile(word.charAt(index)).getPoints();
                        additionalWords.add(downScore);
                    }
                }
                if(solverBoard.getSquare(tempPos).getScoreMulti() == 2){
                    temp.setSquareTile(tempPos, word.charAt(index));
                    lMulti += (solverBoard.getSquare(tempPos).getLetterMulti())*(temp.getSquare(tempPos).getTile().getPoints());
                } else {
                    temp.setSquareTile(tempPos, word.charAt(index));
                    lMulti += temp.getSquare(tempPos).getTile().getPoints();
                }

            }
            index-=1;
            if(index >= 0) tempPos = getLeft(tempPos);
        }
        if(wMulti > 0){
            total = (wMulti * lMulti);
            if(additionalWords.size() > 0){
                for(Integer tempInt : additionalWords){
                    total += tempInt;
                }
            }
        } else {
            total = lMulti;
            if(additionalWords.size() > 0){
                for(Integer tempInt : additionalWords){
                    total += tempInt;
                }
            }
        }
        if(numTilesUsed == 7) total += 50;
        if(total > highestScore){
            highestScore = total;
            highestWord = word;
            highestCrd = pos;
            highestDir = direction;
        }

    }

    /*
    * Based on the algorithm described in the "World's Greatest Scrabble Program":
    * Essentially creates the right part of the word for the play.
    * - partial: partial word being formed
    * - currNode: current node in the Trie currently being accessed
    * - nextPos: next position (to the right) to be processed
    * - anchorFilled: whether the anchor of this play has been filled
    * - numTilesUsed: number of tiles used to form the word (used for the BINGO condition)
    * */
    public void extendWordRight(String partial, LexiconNode currNode, int[] nextPos, boolean anchorFilled , int numTilesUsed){
        if(!solverBoard.getSquare(nextPos).isFilled() && dictionary.isWord(partial.toLowerCase()) && anchorFilled){
            if(rightLimReached){
                if(finalCrossCheck(partial, nextPos)) legalMove(partial, nextPos, numTilesUsed);
                else if(partial.equals("troolie") && direction == 2) System.out.print("troolie failed finalCrossCheck()\n");
            }
            else{
                if(finalCrossCheck(partial, getLeft(nextPos))) legalMove(partial, getLeft(nextPos), numTilesUsed);
                else if(partial.equals("troolie") && direction == 2) System.out.print("troolie failed finalCrossCheck()\n");
            }
            rightLimReached = false;

        }
        if(solverBoard.validPosition(nextPos)){
            ArrayList<Character> temp = crossCheckMap.get(solverBoard.getSquare(nextPos));
            if(!solverBoard.getSquare(nextPos).isFilled()){
                for(Character tempC : currNode.getChildren().keySet()){
                    if(nextPos[0] == 2 && nextPos[1] == 4){
                        placeHolder = temp;
                    }
                    if(checkHand(tempC) && temp.contains(tempC)){
                        if(partial.equals("m") && nextPos[0] == 2 && nextPos[1] == 9){
                            placeHolder = temp;
                        }
                        removeFromHand(tempC);
                        extendWordRight(partial + tempC,
                                currNode.getChildren().get(tempC),
                                getRight(nextPos),
                                true, numTilesUsed+1);
                        hand.add(new Tile(tempC));
                    }
                    else if(checkHandForBlank() && temp.contains(tempC)){
                        removeFromHand('*');
                        if(partial.equals("m") && nextPos[0] == 2 && nextPos[1] == 9){
                            placeHolder = temp;
                        }
                        extendWordRight(partial + Character.toUpperCase(tempC),
                                currNode.getChildren().get(tempC),
                                getRight(nextPos),
                                true, numTilesUsed+1);
                        hand.add(new Tile('*'));
                    }
                }
            } else {
                Character tempChar = solverBoard.getSquare(nextPos).getTile().getLetter();
                if(currNode.getChildren().containsKey(tempChar)){
                    extendWordRight(partial + tempChar,
                            currNode.getChildren().get(tempChar),
                            getRight(nextPos),
                            true, numTilesUsed);
                }
            }

        }

    }

    /*
    * Forms the left part of any potential words to play
    * - partial: partial word being formed
    * - currNode: current node of the Trie being accessed
    * - anchorPos: anchor position the word is being built off of
    * - limit: number of non-anchor squares to the left of the one being processed (decremented with each call)
    * - numTilesUsed: number of tiles used to form the possible word (used for BINGO condition)
    * */
    public void getLeftPart(String partial, LexiconNode currNode, int[] anchorPos, int limit, int numTilesUsed){
        extendWordRight(partial, currNode, anchorPos, false, numTilesUsed);
        if(limit > 0){
            ArrayList<Character> temp = crossCheckMap.get(solverBoard.getSquare(anchorPos));
            for(Character tempChar : currNode.getChildren().keySet()){
                if(checkHand(tempChar) && temp.contains(tempChar)){
                    removeFromHand(tempChar);
                    getLeftPart(partial + tempChar,
                            currNode.getChildren().get(tempChar),
                            anchorPos, limit-1, numTilesUsed+1);
                    hand.add(new Tile(tempChar));
                }
                else if(checkHandForBlank() && temp.contains(tempChar)){
                    removeFromHand('*');
                    getLeftPart(partial + Character.toUpperCase(tempChar),
                            currNode.getChildren().get(tempChar),
                            anchorPos, limit-1, numTilesUsed+1);
                    hand.add(new Tile('*'));
                }
            }
        }

    }

    /*
    * Checks the hand to see if it contains the Tile specified in
    * the parameters.
    * */
    public boolean checkHand(char temp){
        for(Tile tempTile : hand){
            if(tempTile.getLetter() == temp) return true;
        }
        return false;
    }

    /*
    * Removes the specified Tile from the current hand/rack
    * */
    public void removeFromHand(char temp){
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i).getLetter() == temp){
                hand.remove(i);
                return;
            }
        }
    }

    /*
    * Checks the hand to see if it contains a blank Tile
    * */
    private boolean checkHandForBlank(){
        for(Tile tile : hand){
            if(tile.getLetter() == '*') return true;
        }
        return false;
    }

    /*
    * Generates the crossCheckMap based on the current direction. Maps
    * lists of all letters that can be legally placed at each BoardSquare.
    * */
    public Map<BoardSquare, ArrayList<Character>> crossCheck(){
        Map<BoardSquare, ArrayList<Character>> availableLetters = new HashMap<>();
        ArrayList<Character> legalLetters;
        for(BoardSquare temp : solverBoard.getAllPositions()){
            if(temp.isFilled()){
                continue;
            }
            StringBuilder upLetters = new StringBuilder();
            int[] scanPos = new int[]{temp.getX(), temp.getY()};
            while(solverBoard.getSquare(getUp(scanPos)).isFilled()){
                if(getUp(scanPos)[0] != scanPos[0] && getUp(scanPos)[1] != scanPos[1]) scanPos = getUp(scanPos);
                else break;
                upLetters.insert(0, solverBoard.getSquare(scanPos).getTile().getLetter());
            }
            StringBuilder downLetters = new StringBuilder();
            int[] scanPosDown = new int[]{temp.getX(), temp.getY()};
            while(solverBoard.getSquare(getDown(scanPosDown)).isFilled()){
                if(getDown(scanPosDown)[0] != scanPosDown[0] && getDown(scanPosDown)[1] != scanPosDown[1]) scanPosDown = getDown(scanPosDown);
                else{
                    break;
                }
                downLetters.append(solverBoard.getSquare(scanPosDown).getTile().getLetter());
            }
            if(upLetters.length() == 0 && downLetters.length() == 0){
                char[] allLetters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                legalLetters = new ArrayList<>();
                for(Character tempAL : allLetters){
                    legalLetters.add(tempAL);
                }
            } else {
                char[] allLetters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                legalLetters = new ArrayList<>();
                for(Character letter : allLetters){
                    String formedWord;
                    formedWord = upLetters.toString() + letter + downLetters;
                    if(dictionary.isWord(formedWord)){
                        if(temp.getX() == 2 && temp.getY() == 4) System.out.print("Formed word is: " + formedWord + "\n");
                        legalLetters.add(letter);
                    }
                }
            }
            availableLetters.put(temp, legalLetters);
        }
        return availableLetters;
    }

    //Gets the score of any words formed above the current position
    public int getUpScore(int[] pos){
        int total = 0;
        while(solverBoard.getSquare(pos).isFilled()){
            total += solverBoard.getSquare(pos).getTile().getPoints();
            if(getUp(pos)[0] != pos[0] && getUp(pos)[1] != pos[1]) pos = getUp(pos);
            else break;
        }
        return total;
    }

    //Gets the score of any words formed below the current position
    public int getDownScore(int[] pos){
        int total = 0;
        while(solverBoard.getSquare(pos).isFilled()){
            total += solverBoard.getSquare(pos).getTile().getPoints();
            if(getDown(pos)[0] != pos[0] && getDown(pos)[1] != pos[1]) pos = getDown(pos);
            else break;
        }
        return total;
    }


    //Used in concert with finalCrossCheck() before a play is sent to legalMove()
    public boolean testCrossCheck(int[] coordinate, char testChar){
        StringBuilder upLetters = new StringBuilder();
        int[] scanPos = new int[]{coordinate[0], coordinate[1]};
        int upIndex = 0;
        while(solverBoard.getSquare(getUp(scanPos)).isFilled() && upIndex < solverBoard.getSize()){
            scanPos = getUp(scanPos);
            upLetters.insert(0, solverBoard.getSquare(scanPos).getTile().getLetter());
            upIndex++;
        }
        StringBuilder downLetters = new StringBuilder();
        int[] scanPosDown = new int[]{coordinate[0], coordinate[1]};
        int downIndex = 0;
        while(solverBoard.getSquare(getDown(scanPosDown)).isFilled() && downIndex < solverBoard.getSize()){
            scanPosDown = getDown(scanPosDown);
            downLetters.append(solverBoard.getSquare(scanPosDown).getTile().getLetter());
            downIndex++;
        }
        if(upLetters.length() == 0 & downLetters.length() == 0) return true;
        else{
            String formedWord = upLetters.toString() + testChar + downLetters;
            if(dictionary.isWord(formedWord)) return true;
        }
        return false;
    }

    //Double checks a play to ensure it is a legal play before being sent to legalMove
    public boolean finalCrossCheck(String word, int[] crd){
        int index = word.length()-1;
        int[] pos = crd;
        while(index >= 0){
            if(!testCrossCheck(pos, word.charAt(index))){
                return false;
            }
            else {
                index-=1;
                if(index >= 0) pos = getLeft(pos);
            }
        }
        return true;
    }

    //Used in Scrabble to determine if the play passed in the parameters is legal based on the current state of the board.
    public boolean validPlay(String word, int[] coordinate, int direction, int numTilesUsed){
        this.direction = direction;
        crossCheckMap = crossCheck();
        int index = word.length()-1;
        int[] scanPos = coordinate;
        int lMulti = 0;
        int wMulti = 0;
        int upScore = 0;
        int downScore = 0;
        int total;
        while(index >= 0){
            ArrayList<Character> ccMapList = crossCheckMap.get(solverBoard.getSquare(scanPos));
            if(ccMapList != null && !ccMapList.contains(word.charAt(index))) return false;
            if(solverBoard.getSquare(scanPos).isFilled()){
                lMulti += solverBoard.getSquare(scanPos).getTile().getPoints();
            } else {
                wMulti += solverBoard.getSquare(scanPos).getWordMulti();
                if(solverBoard.getSquare(getUp(scanPos)).isFilled()){

                    if(solverBoard.getSquare(scanPos).getScoreMulti() == 2){
                        upScore = (solverBoard.getSquare(scanPos).getLetterMulti()*new Tile(word.charAt(index)).getPoints()) + getUpScore(getUp(scanPos));
                    } else {
                        upScore = getUpScore(getUp(scanPos)) + new Tile(word.charAt(index)).getPoints();
                    }
                }
                if(solverBoard.getSquare(getDown(scanPos)).isFilled()){
                    if(solverBoard.getSquare(scanPos).getScoreMulti() == 2){
                        downScore = (solverBoard.getSquare(scanPos).getLetterMulti()*new Tile(word.charAt(index)).getPoints()) + getDownScore(getDown(scanPos));
                    } else {
                        downScore = getDownScore(getDown(scanPos)) + new Tile(word.charAt(index)).getPoints();
                    }
                }
                if(solverBoard.getSquare(scanPos).getScoreMulti() == 2){
                    lMulti += (solverBoard.getSquare(scanPos).getLetterMulti())*(new Tile(word.charAt(index)).getPoints());
                } else {
                    lMulti += new Tile(word.charAt(index)).getPoints();
                }
            }
            index-=1;
            if(index >= 0) scanPos = getRight(scanPos);
        }
        if(wMulti > 0){
            total = (wMulti * lMulti) + upScore + downScore;
        } else total = lMulti + upScore + downScore;

        if(numTilesUsed == 7) total += 50;
        if(dictionary.isWord(word)){
            highestScore = total;
            return true;
        } else return false;
    }

    //Returns the current highest score
    public int getHighestScore(){return highestScore;}

}
