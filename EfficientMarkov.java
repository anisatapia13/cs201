import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class EfficientMarkov extends BaseMarkov {
	private Map<String,ArrayList<String>> myMap;
	
	public EfficientMarkov() {
		super();
		myMap = new HashMap<>();
	}
	public EfficientMarkov(int order) {
		super(order);
		myMap = new HashMap<>();
	}
	@Override
	public void setTraining(String text) {
		myText = text;
		myMap.clear();
		String key = myText.substring(0, myOrder);
		for(int k = 0; k <= myText.length(); k ++) {
			key = myText.substring(k, k + myOrder);
			myMap.putIfAbsent(key, new ArrayList<String>());

			int start = myText.indexOf(key, k);
			if (start == -1) break;
			if (start + myOrder >= myText.length()) {
				myMap.get(key).add(PSEUDO_EOS);
				break;
			}
			String nextChar = myText.substring(start + myOrder, start + myOrder + 1);
			myMap.get(key).add(nextChar);
		}
	}
	@Override
	public ArrayList<String> getFollows(String key) {
		if(! myMap.containsKey(key)){
			throw new NoSuchElementException(key + "not in map");
		}
		return myMap.get(key);
	}
}	
