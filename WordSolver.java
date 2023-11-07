import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordSolver {
    public static void main(String[] args) throws IOException{
        Scanner dictReader = new Scanner(new File(args[0]));
        Scanner boardReader = new Scanner(new File(args[1]));
        String input;
        ArrayList<String> words = new ArrayList<>();
        while(dictReader.hasNextLine()){
            input = dictReader.nextLine();
            if(!input.contains(" ")) words.add(input);
        }
        Lexicon dictionary = new Lexicon(words);
        while(boardReader.hasNextLine()){
            ArrayList<String> strBoard = new ArrayList<>();
            ArrayList<Tile> compHand = new ArrayList<>();
            int boardSize = boardReader.nextInt();
            int i = 0;
            while(i <= boardSize){
                String input2 = boardReader.nextLine();
                if(!input2.isBlank()){
                    strBoard.add(input2);
                }
                i++;
            }
            input = boardReader.nextLine();
            for(int j = 0; j < input.length(); j++){
                compHand.add(new Tile(input.charAt(j)));
            }
            Board game = new Board(strBoard, boardSize, compHand, dictionary);
            game.printBoard();
            game.printCompHand();
            Solver newSolver = new Solver(dictionary, compHand, game);
            newSolver.findAllPlays(false);
        }
    }
}

