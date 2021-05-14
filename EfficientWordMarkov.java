import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
/* similar to Efficient Markov but uses WordGrams instead of String myText to generate random text */
public class EfficientWordMarkov extends BaseWordMarkov {
    private Map<WordGram, ArrayList<String>> myMap;

    //construct an EfficientMarkov object
    public EfficientWordMarkov(int order){
        super(order);
        //myMap is initialised to a HashMap
        myMap =  new HashMap<>();
    }
    //default constructor has size 2
    public EfficientWordMarkov(){
        this(2);
    }
    //setTraining builds the map and adds to myOrder wordGrams
    @Override
    public void setTraining(String text) {
        myWords = text.split("\\s+");
        myMap.clear();

        for(int k = 0; k < myWords.length- myOrder+1; k ++) {
            ArrayList<String> arr = new ArrayList<>();
            WordGram wg = new WordGram(myWords, k , myOrder);
            myMap.putIfAbsent(wg, arr);

            if (myOrder + k == myWords.length) {
                myMap.get(wg).add(PSEUDO_EOS);
            }
            else {
                myMap.get(wg).add(myWords[k + myOrder]);
            }
        }
    }
    // getFollows checks to see if k WordGram is present in the map. If its not we throw an exception, if it is we return an array list of words that follow the WordGram
    @Override
    public ArrayList<String> getFollows(WordGram kGram) {
        if(! myMap.containsKey(kGram)){
            throw new NoSuchElementException(kGram + "not in map");
        }
        return myMap.get(kGram);
    }
}
