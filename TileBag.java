import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class TileBag {
    private final LinkedList<Tile> letterBag = new LinkedList<>();
    /*
    * Main constructor for both WordSolver and Scrabble:
    * Creates a total Tile Bag
    * */
    public TileBag(){
        addTile(9,'a');
        addTile(2,'b');
        addTile(2,'c');
        addTile(4,'d');
        addTile(12,'e');
        addTile(2,'f');
        addTile(3,'g');
        addTile(2,'h');
        addTile(9,'i');
        addTile(1,'j');
        addTile(1,'k');
        addTile(4,'l');
        addTile(2,'m');
        addTile(6,'n');
        addTile(8,'o');
        addTile(2,'p');
        addTile(1,'q');
        addTile(6,'r');
        addTile(4,'s');
        addTile(6,'t');
        addTile(4,'u');
        addTile(2,'v');
        addTile(2,'w');
        addTile(1,'x');
        addTile(2,'y');
        addTile(1,'z');
        addTile(2,'\0');
    }

    //Adds a Tile to the Tile Bag
    public void addTile(int num, char letter){
        for(int i = 0; i < num; i++){
            Tile newTile = new Tile(letter);
            letterBag.add(newTile);
        }
    }

    //Returns a randomized rack of Tiles
    public ArrayList<Tile> getHand(boolean includeBlanks){
        ArrayList<Tile> newHand = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            Random rand = new Random();
            int index = rand.nextInt(letterBag.size());
            if(letterBag.get(index).getLetter() == '*' && includeBlanks){
                newHand.add(letterBag.get(index));
                letterBag.remove(index);
            } else if(letterBag.get(index).getLetter() != '*'){
                newHand.add(letterBag.get(index));
                letterBag.remove(index);
            } else {
                while(letterBag.get(index).getLetter() == '*'){
                    index = rand.nextInt(letterBag.size());
                }
                newHand.add(letterBag.get(index));
                letterBag.remove(index);
            }
        }
        return newHand;
    }

    //Removes a letter from the Tile Bag
    public void removeLetter(Tile temp){
        letterBag.remove(temp);
    }

    //Draws a Tile from the Tile Bag
    public Tile drawTile(boolean includeBlanks){
        Random rand = new Random();
        Tile newTile = letterBag.get(rand.nextInt(letterBag.size()));
        if(newTile.getLetter() == '*' && includeBlanks){
            removeLetter(newTile);
            return newTile;
        } else if(newTile.getLetter() != '*'){
            removeLetter(newTile);
            return newTile;
        } else {
            while(newTile.getLetter() == '*'){
                newTile = letterBag.get(rand.nextInt(letterBag.size()));
            }
            removeLetter(newTile);
            return newTile;
        }
    }

    //Returns the current size of the Tile Bag
    public int getSize(){return letterBag.size();}
}

