import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Scrabble extends Application {
    @Override
    public void start(Stage mainStage) throws IOException {
        mainStage.setTitle("Scrabble");
        mainStage.setMinHeight(750);
        mainStage.setMinWidth(1150);
        File boardFile = new File("empty_scrabble_board.txt");
        URL site = new URL("https://www.wordgamedictionary.com/sowpods/download/sowpods.txt");
        BufferedReader bfr = new BufferedReader(new InputStreamReader(site.openStream()));
        Scanner boardReader = new Scanner(boardFile);
        String input;
        ArrayList<String> words = new ArrayList<>();
        while((input = bfr.readLine()) != null){
            if(!input.contains(" ")) words.add(input);
        }
        Lexicon dictionary = new Lexicon(words);
        ArrayList<String> strBoard = new ArrayList<>();
        String input2;
        while(boardReader.hasNextLine()){
            input2 = boardReader.nextLine();
            if(!input2.isBlank()){
                strBoard.add(input2);
                System.out.print(input2 + '\n');
            }
        }
        Board game = new Board(strBoard, dictionary);
        Solver humanSolver = new Solver(dictionary, game.getHuman(), game);
        Solver compSolver = new Solver(dictionary, game.getComp(), game);
        game.setSolvers(humanSolver, compSolver);
        mainStage.setScene(new Scene(game, 1000, 750, Color.TAN));
        mainStage.show();
    }
}
