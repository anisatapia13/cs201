import java.util.Comparator;

/**
 * Factor pattern for obtaining PrefixComparator objects
 * without calling new. Users simply use
 *
 *     Comparator<Term> comp = PrefixComparator.getComparator(size)
 *
 * @author owen astrachan
 * @date October 8, 2020
 */
public class PrefixComparator implements Comparator<Term> {

    private int myPrefixSize; // size of prefix

    /**
     * private constructor, called by getComparator
     *
     * @param prefix is prefix used in compare method
     */
    private PrefixComparator(int prefix) {
        myPrefixSize = prefix;
    }


    /**
     * Factory method to return a PrefixComparator object
     *
     * @param prefix is the size of the prefix to compare with
     * @return PrefixComparator that uses prefix
     */
    public static PrefixComparator getComparator(int prefix) {
        return new PrefixComparator(prefix);
    }


    @Override
    public int compare(Term v, Term w) {
        int r = myPrefixSize;
        String vword = v.getWord();
        String wword = w.getWord();
        int min = Math.min(vword.length(), wword.length());
        int numIter = Math.min(r, min);

        for (int k = 0; k < numIter; k++) {
            if (vword.charAt(k) - wword.charAt(k) > 0) return 1;
            if (vword.charAt(k) < wword.charAt(k) ) return -1;
        }
        for (int i = 0; i < numIter; i++ ){
            if (vword.length() >= r && wword.length() >= r ) return 0;
            if (vword.length() == wword.length()) return 0;
            if (vword.length() > wword.length()) return 1;
            if (vword.length() < wword.length()) return -1;
        }
        return 0;
        //return vword.length()-wword.length();
        //return v.getWord().compareTo(w.getWord());
    }
}
