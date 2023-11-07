import java.util.ArrayList;

/*
* Implementation of a Trie:
* Serves as my dictionary/word list for both programs
* Works with LexiconNode, which helps to traverse the complete Trie
* - root: root LexiconNode of the complete Trie
* */
public class Lexicon {
    private final LexiconNode root;

    //Constructor to simply initialize the Trie
    public Lexicon(){
        root = new LexiconNode();
    }

    /*
    * Main Constructor that builds the Trie:
    * Takes in an ArrayList of Strings that is built in either WordSolver
    * or Scrabble. Used a variation of the algorithm to build to a binary tree
    * too traverse the different nodes.
    * */
    public Lexicon(ArrayList<String> words){
        root = new LexiconNode();
        LexiconNode current;
        for(String c : words){
            current = root;
            for(int i = 0; i < c.length(); i++){
                if(!current.getChildren().containsKey(c.charAt(i))){
                    current.getChildren().put(c.charAt(i), new LexiconNode());
                }
                current = current.getChildren().get(c.charAt(i));
            }
            current.setIsWord(true);
        }
    }
    /*
    * Traverses the Trie to locate the LexiconNode that String (word)
    * leads to within the Trie. Used within solver to incrementally step
    * through the trie.
    * */
    public LexiconNode find(String word){
        LexiconNode current = root;
        for(int i = 0; i < word.length(); i++){
            if(!current.getChildren().containsKey(word.charAt(i))) return null;
            else current = current.getChildren().get(word.charAt(i));
        }
        return current;
    }

    /*
    * Traverses the Trie to determine if the specified  String (word)
    * is a word stored within the Trie.
    * */
    public boolean isWord(String word){
        LexiconNode temp = find(word);
        if(temp == null) return false;
        return temp.isWord();
    }

    //Returns the root node of the Lexicon
    public LexiconNode getRoot() {
        return root;
    }
}
