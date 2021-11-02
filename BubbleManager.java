

//First working version of Bubble Manager, minor adjustments to RNG needed

/**
This class manages all the bookkeeping for my BubblePopper game. It has 7
member variables that assist with the methods in tracking the board. Most are declared
here then initialized when necessary in different methods. It uses a List of Lists that
serves as the board, with the indexes of the board variable serving as the rows and
the indexes of each list in the container actings as the columns. I also created
a public nested class of BubbleCoord, with row and column member variables, which are initialized in
its constructor. These serve as an easy container for coordinates that need to be
altered or removed. There are 5 different types of bubble strings, they are randomly
generated using the fixed seed provided to the constructor which is passed to the rand variable.
*/
import java.util.*;
import java.awt.Color;
public class BubbleManager {
    
    private static Random rand;
    private static int rows;
    private static int cols;
    private static int numSame;
    private static int numBubTypes;
    private static List<List<String>> board;
    private static String[] bubbleTypes = {" ", "+", "@", "*", "$", "^", "#", "-", "&"};
    private static Color[] diffColors = {Color.WHITE, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.ORANGE, Color.CYAN};
    private static List<BubbleCoord> removeList;
    public static Map<String, Color> colorMap;

    /**
	* Nested Coordinate class to assist with altering and removing items on board
    */
    class BubbleCoord{
    	int cRow;
    	int cCol;
    	/**
		* Bubble Coordinate Constructor
		* @param int cRow
		* @param int cCol
    	*/
    	public BubbleCoord(int row, int col){
    		super();
    		cRow = row;
    		cCol = col;
    	}
    	/**
		*Returns row for this coordinate
		*@return cRow
    	*/
    	public int getRow(){return cRow;}
    	/**
		*Returns column for this coordinate
		*@return cCol
    	*/
    	public int getCol(){return cCol;}

    }
    
    /**
	*Bubble manager constructor
	*Creates a bubble manager object and generates a randomized board
	*@param int r: rows
	*@param int c: columns
	*@param int randNumBub: number of bubble types
    */
    public BubbleManager(int r, int c, int randNumBub){
        rows = r;
        cols = c;
        rand = new Random(randNumBub);
        numBubTypes = randNumBub;
        generateBoard(rows, cols, randNumBub);
        colorMap = new HashMap<>();
        for(int x = 0; x < bubbleTypes.length; x++){
                colorMap.put(bubbleTypes[x], diffColors[x]);
        }
    }

    /**
	*Method to Generate a random board based on board manager constructor
	*Creates a bubble manager object and generates a randomized board, doesn't return anything
	*@param int nRows: rows
	*@param int nCols: columns
	*@param int numTypes: number of bubble types
    */
    public void generateBoard(int nRows, int nCols, int numTypes){
        board = new LinkedList<>();
        LinkedList<String> newRow;
        for(int r = 0; r < 12; r++){
        	newRow = new LinkedList<>();
        	for(int c = 0; c < cols; c++){
        		newRow.add(bubbleTypes[0]);
        	}
        	board.add(newRow);
        }
        for(int r = 0; r < nRows; r++){
        	newRow = new LinkedList<>();
        	for(int c = 0; c < nCols; c++){
        		newRow.add(bubbleTypes[rand.nextInt(numTypes)]);
        	}
        	board.set(r, newRow);
        }
    }

    /**
	*Determines if there are bubbles adjacent to the provided coordinate
	*Used to determine if a bubble addition is valid or if bubbles are floating
	*@param int r: specifc row
	*@param int c: specific colum
	*@return boolean: returns true if there are adjacent bubbles and vice versa
    */
    public boolean hasAdjacent(int r, int c){
        boolean hasAdj = false;
        System.out.println("hasAdjacent was called");
    	if(r%2 == 0 ){
            System.out.println("hasAdjacent (E) was stepped into");
            if(r<12 && c+1<cols && c-1>-1 || c==0){
                System.out.println("Horizontal Check (R) stepped into");
                if(c==0){
                    if(!board.get(r).get(c+1).equals(bubbleTypes[0])){
                        System.out.println("Horizontal Check performed 1");
                        hasAdj = true;
                    }
                } else {
                    if(!board.get(r).get(c+1).equals(bubbleTypes[0])){
                        System.out.println("Horizontal Check performed 1");
                        hasAdj = true;
                    }
                    if(!board.get(r).get(c-1).equals(bubbleTypes[0])){
                        System.out.println("Horizontal Check performed 2");
                        hasAdj = true;
                    }
                }
                //System.out.println("Horizontal Check (L) stepped into");
                //System.out.println("Horizontal Check (R) stepped out of");
                System.out.println("r value is: " + r);
                System.out.println("c value is: " + c);
            //} else if(r<12 && c-1>-1 || c == 1){
                
            } 
            if(r-1>-1 && c-1>-1){
                if(!board.get(r-1).get(c-1).equals(bubbleTypes[0])){
                    System.out.println("Top Check performed 1");
                    hasAdj = true;
                } else if (!board.get(r-1).get(c).equals(bubbleTypes[0])){
                    System.out.println("Top Check performed 2");
                    hasAdj = true;
                }
            } 
            if(r+1<12 && c-1>-1){
                if(!board.get(r+1).get(c-1).equals(bubbleTypes[0])){
                    System.out.println("Bottom Check performed 1");
                    hasAdj = true;
                } else if(!board.get(r+1).get(c).equals(bubbleTypes[0])){
                    System.out.println("Bottom Check performed 1");
                    hasAdj = true;
                }
            }
        } else if(r%2 != 0){
            System.out.println("hasAdjacent (O) was stepped into");
            if(r<12 && c+1<cols){
                if(!board.get(r).get(c+1).equals(bubbleTypes[0])){
                    System.out.println("Horizontal Check performed 1o");
                    hasAdj = true;
                }
            }
            if(r<12 && c-1>-1){
                if(!board.get(r).get(c-1).equals(bubbleTypes[0])){
                    System.out.println("Horizontal Check performed 2o");
                    hasAdj = true;
                }
            }
            if(r-1>-1 && c+1<cols){
                if(!board.get(r-1).get(c+1).equals(bubbleTypes[0])){
                    System.out.println("Top Check performed 1o");
                    hasAdj = true;
                } else if(!board.get(r-1).get(c).equals(bubbleTypes[0])){
                    System.out.println("Top Check performed 2o");
                    hasAdj = true;
                }
            }
            if(r+1<12 && c+1<cols){
                if(!board.get(r+1).get(c+1).equals(bubbleTypes[0])){
                    System.out.println("Bottom Check performed 1o");
                    hasAdj = true;
                } else if(!board.get(r+1).get(c).equals(bubbleTypes[0])){
                    System.out.println("Bottom Check performed 2o");
                    hasAdj = true;
                }
            }
        }
    	return hasAdj;
    }

    /**
	*Adds a bubble to the board if position is valid
	*Counts number of same color bubbles using hasSame()
	*@param int thisRow: specific row
	*@param int thisCol: specific column
	*@return int: number of same colored bubbles
    */
    public int addBubble(int thisRow, int thisCol){
        numSame = 0;
        //LinkedList<List<String>> boardClone =  new LinkedList<>(board);
        if(board.get(thisRow).get(thisCol).equals(bubbleTypes[0]) && hasAdjacent(thisRow, thisCol)){
            System.out.println("Bubble was added");
        	board.get(thisRow).set(thisCol, bubbleTypes[rand.nextInt(numBubTypes)]);
            removeList = new LinkedList<>();
            removeList.add(new BubbleCoord(thisRow, thisCol));
        	numSame = hasSame(thisRow, thisCol);
        	System.out.println("There are " + (numSame - 1) + " of the same Bubbles adjacent to this  one!");
        	//if(numSame > 2){
        		//removeSame();
        	//}
        }
        return numSame;
    }

    /**
	*Determines if there are bubbles adjacent to the provided coordinate are the same color
	*Recursive to work from new position
	*@param int r: specifc row
	*@param int c: specific colum
	*@return int: number of same colored bubbles from call
    */
    public int hasSame(int r, int c){
        System.out.println("hasSame was called");
        int sameBubbles = removeList.size();
       String thisBubble = board.get(r).get(c);
       System.out.println("This is the current bubble:" + thisBubble);
       if(r%2 == 0){
        System.out.println("Checked even row");
            if(r<12 && c+1<cols){
                if(board.get(r).get(c+1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r, c+1));
                        sameBubbles++;
                        System.out.println("Horizontal Check performed s1");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r, c+1);}
                        return sameBubbles;
                    }
                }
            }
            if(r<12 && c-1>-1){
                if(board.get(r).get(c-1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r, c-1));
                        sameBubbles++;
                        System.out.println("Horizontal Check performed s2");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r, c-1);}
                        return sameBubbles;
                    }
                }
            }
            if(r-1>-1 && c-1>-1){
                if(board.get(r-1).get(c-1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r-1, c-1));
                        sameBubbles++;
                        System.out.println("Top Check performed s1");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r-1, c-1);}
                        return sameBubbles;
                    }
                }
                if (board.get(r-1).get(c).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r-1, c));
                        sameBubbles++;
                        System.out.println("Top Check performed s2");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r-1, c);}
                        return sameBubbles;
                    }
                }
            }
            if(r+1<12 && c-1>-1){
                if(board.get(r+1).get(c-1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r+1, c-1));
                        sameBubbles++;
                        System.out.println("Bottom Check performed s1");
                        System.out.println(sameBubbles);
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r+1, c-1);}
                        return sameBubbles;
                    }
                }
                if(board.get(r+1).get(c).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r+1, c));
                        sameBubbles++;
                        System.out.println("Bottom Check performed s2");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r+1, c);}
                        return sameBubbles;
                    }
                }
            }
        } else if(r%2 != 0){
            System.out.println("Checked odd row");
            if(r<12 && c-1>-1){
                if(board.get(r).get(c-1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r, c-1));
                        sameBubbles++;
                        System.out.println("Horizontal Check performed so1");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r, c-1);}
                        return sameBubbles;
                    }
                }
            }
            if(r<12 && c+1<cols){
                if(board.get(r).get(c+1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r, c+1));
                        sameBubbles++;
                        System.out.println("Horizontal Check performed so2");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r, c+1);}
                        return sameBubbles;
                    }
                }
            }
            if(r-1>-1 && c+1<cols){
                if(board.get(r-1).get(c+1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r-1, c+1));
                        sameBubbles++;
                        System.out.println("Top Check performed so1");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r-1, c+1);}
                        return sameBubbles;
                    }
                if(board.get(r-1).get(c).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r-1, c));
                        sameBubbles++;
                        System.out.println("Top Check performed so2");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r-1, c);}
                        return sameBubbles;
                    }
                }
            }
            if(r+1<12 && c+1<cols){
                if(board.get(r+1).get(c+1).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r+1, c+1));
                        sameBubbles++;
                        System.out.println("Bottom Check performed so1");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r+1, c+1);}
                        return sameBubbles;
                    }
                if(board.get(r+1).get(c).equals(thisBubble)){
                    sameBubbles = removeList.size();
                    if(sameBubbles + 1< 4){
                        removeList.add(new BubbleCoord(r+1, c));
                        sameBubbles++;
                        System.out.println("Bottom Check performed so2");
                        if(sameBubbles + 1< 4){sameBubbles= hasSame(r+1, c);}
                        return sameBubbles;
                    }
                }
            }
        }
    }
    }
    return sameBubbles;
    }
    

    /**
	* Removes bubbles added to removeList whenever called
    */
    public void removeSame(){
        for(int x = 0; x < removeList.size(); x++){
        	BubbleCoord thisCoord = new BubbleCoord(removeList.get(x).getRow(), removeList.get(x).getCol());
        	board.get(thisCoord.getRow()).set(thisCoord.getCol(), bubbleTypes[0]);
        }
    }

    public Map<String, Color> getColorMap(){return colorMap;}

    /**
	* Finds and removes any floating bubbles whenever called, returns how many were removed
	* @return int: number of floaters founds and removed
    */
    public int findFloaters(){
    	int numFloaters = 0;
        LinkedList<List<String>> floaterClone = new LinkedList<>(board);
        for(int r = 0; r < 12; r++){
        	for(int c = 0; c < cols; c++){
        		if(!floaterClone.get(r).get(c).equals(bubbleTypes[0]) && !hasAdjacent(r, c)){
        			board.get(r).set(c, bubbleTypes[0]);
        			numFloaters++;
        		}
        	}
        }
        return numFloaters;
    }

    /**
	* Converts the board to a hex grid string representation
	* @return String: string representation of the current board.
    */
    public String toString(){
    	String boardString = "";
    	for(int r = 0; r < 12; r++){
    		for(int c = 0; c < cols; c++){
    			if(r%2 != 0){
    				boardString += " " + board.get(r).get(c);
    			}else{
    				boardString += board.get(r).get(c) + " ";
    			}
    		}
    		boardString += "\n";
    	}
    	return boardString;
    }


}

