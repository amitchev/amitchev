import java.util.HashMap;
import java.util.Map;

/*
* Acts as the nodes of my Trie implementation.
* - isWord: Whether the node is the end of a word, assigned Lexicon
* - children: HashMap that serves as the child nodes of a parent node
* - Quick little update check comment
* */
public class LexiconNode {
    private boolean isWord;
    private final Map<Character, LexiconNode> children = new HashMap<>();

    //Returns children
    public Map<Character, LexiconNode> getChildren(){ return children;}

    //Returns value of isWord
    public boolean isWord(){return isWord;}

    //Sets the value of isWord to that specified in the parameter (newIsWord)
    public void setIsWord(boolean newIsWord){ isWord = newIsWord;}
}
