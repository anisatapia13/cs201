import java.util.PriorityQueue;

/**
 * Although this class has a history of several years,
 * it is starting from a blank-slate, new and clean implementation
 * as of Fall 2018.
 * <P>
 * Changes include relying solely on a tree for header information
 * and including debug and bits read/written information
 * 
 * @author Owen Astrachan
 */
//Larry Chen & Anisa Tapia
public class HuffProcessor {

	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD); 
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	public static final int HUFF_TREE  = HUFF_NUMBER | 1;

	private final int myDebugLevel;
	
	public static final int DEBUG_HIGH = 4;
	public static final int DEBUG_LOW = 1;
	
	public HuffProcessor() {
		this(0);
	}
	
	public HuffProcessor(int debug) {
		myDebugLevel = debug;
	}

	/**
	 * Compresses a file. Process must be reversible and loss-less.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be compressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void compress(BitInputStream in, BitOutputStream out){

		// remove all this code when implementing compress
		int[] counts = readForCounts(in);
		HuffNode root = makeTreeFromCounts(counts);
		String[] codings = makeCodingsFromTree(root);
		out.writeBits(BITS_PER_INT, HUFF_TREE);
		writeHeader(root,out);
		in.reset();
		writeCompressedBits(codings, in , out);
		out.close();
	}
	private int[] readForCounts(BitInputStream in){
		int[] freq = new int[ALPH_SIZE + 1];
		while(true){
			int myCount = in.readBits(BITS_PER_WORD);
			if(myCount == -1){
				break;
			}
			int secCount = myCount;
			freq[secCount]+=1;
		}
		freq[PSEUDO_EOF]=1;
		return freq;
	}
	private HuffNode makeTreeFromCounts(int[] counts){
		PriorityQueue<HuffNode> pq = new PriorityQueue<>();
		for(int x = 0; x < counts.length; x++){
			if(counts[x] > 0){
				pq.add(new HuffNode(x, counts[x], null, null));
			}
		}
		while(pq.size() > 1){
			HuffNode left = pq.remove();
			HuffNode right = pq.remove();
			HuffNode t = new HuffNode(0, right.myWeight + left.myWeight, left, right);
			pq.add(t);
		}
		HuffNode root = pq.remove();
		return root;
	}
	private String[] makeCodingsFromTree(HuffNode root){
		String[] encodings = new String[1 + ALPH_SIZE];
		codingHelper(root, "", encodings);
		return encodings;
	}
	private void codingHelper(HuffNode root, String part, String[] encodings){
		if(root.myRight == null && root.myLeft == null){
			encodings[root.myValue] = part;
			return;
		}
		codingHelper(root.myRight,  part + "1", encodings);
		codingHelper(root.myLeft,  part + "0", encodings);
	}
	private void writeHeader(HuffNode root, BitOutputStream out){
		if(root.myRight == null && root.myLeft == null){
			out.writeBits(1, 1);
			out.writeBits(1 + BITS_PER_WORD, root.myValue);
		}
		else{
			out.writeBits(1, 0);
			writeHeader(root.myLeft, out);
			writeHeader(root.myRight, out);
		}
	}
	private void writeCompressedBits(String[] codings, BitInputStream in, BitOutputStream out){
		while(true){
			int one = in.readBits(BITS_PER_WORD);
			if(one == -1) break;
			String code = codings[one];
			out.writeBits(code.length(), Integer.parseInt(code, 2));
		}
		String end = codings[PSEUDO_EOF];
		out.writeBits(end.length(), Integer.parseInt(end, 2));
	}
	/**
	 * Decompresses a file. Output file must be identical bit-by-bit to the
	 * original.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be decompressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void decompress(BitInputStream in, BitOutputStream out){

		int magic = in.readBits(BITS_PER_INT);
		if (magic != HUFF_TREE) {
			throw new HuffException("invalid magic number " + magic);
		}
		// remove all code below this point for P7
		HuffNode root = readTree(in);
		HuffNode current = root;
		while (true) {
			int bits = in.readBits(1);
			if (bits == -1) {
				throw new HuffException("bad input, no PSEUDO_EOF");
			}
			else {
				if (bits == 0) current = current.myLeft;
				else current = current.myRight;
				if (current.myRight == null && current.myLeft == null){
					if (current.myValue == PSEUDO_EOF)
						break;   // out of loop
					else {
						out.writeBits(BITS_PER_WORD, current.myValue);
						current = root; // start back after leaf
					}
				}
			}
		}
		out.close();
	}

	private HuffNode readTree(BitInputStream in) {
		int bit = in.readBits(1);
		if (bit == -1) throw new HuffException("-1");
		if (bit == 0) {
			HuffNode left = readTree(in);
			HuffNode right = readTree(in);
			return new HuffNode(0,0,left,right);
		}
		else {
			int value = in.readBits(BITS_PER_WORD + 1);
			return new HuffNode(value,0,null,null);
		}
	}
}