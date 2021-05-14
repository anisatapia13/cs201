import java.util.*;

public class HashListAutocomplete implements Autocompletor {
    private int mySize;
    private static final int MAX_PREFIX = 10;
    private Map<String,List<Term>> myMap = new HashMap<>();

    public HashListAutocomplete(String[] terms, double[] weights){
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");}
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("terms and weights are not the same length");
        }
        initialize(terms, weights);

    }
    @Override
    public List<Term> topMatches(String prefix, int k) {

        if( k == 0 || prefix == null) return Collections.emptyList();
        List<Term> match = myMap.get(prefix);
        if( match == null ) return Collections.emptyList();
        List<Term> topMatches = match.subList(0, Math.min(k, match.size()));
        return topMatches;

    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<>();

        for(int i = 0; i < terms.length; i++){
            List<Term> list = new ArrayList<>();
            Term myTerm = new Term(terms[i], weights[i]);
            myMap.putIfAbsent("", list);
            myMap.get("").add(myTerm);

            for(int j = 0; j < terms[i].length(); j ++){
                String prefix = terms[i].substring(0, j+1);
                if(prefix.length() > MAX_PREFIX) continue;
                List<Term> nlist = new ArrayList<>();
                myMap.putIfAbsent(prefix,nlist);
                myMap.get(prefix).add(myTerm);
            }
        }
        for(String pref : myMap.keySet()){
            Collections.sort(myMap.get(pref),Comparator.comparing(Term::getWeight).reversed());
        }
    }

    @Override
    public int sizeInBytes() {
        if (mySize == 0) {

            for(String t : myMap.keySet()) {
                mySize += BYTES_PER_CHAR*t.length();
                for (Term term: myMap.get(t)) {
                    mySize += BYTES_PER_CHAR*term.getWord().length();
                    mySize += BYTES_PER_DOUBLE ;
                }
            }
        }
        return mySize;
    }
}
