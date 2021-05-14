// Larry Chen and Anisa Tapia

public class LinkStrand implements IDnaStrand {

    //defining the private inner class
    private class Node{
        String info;
        Node next;

        public Node(String s){
            info = s;
            next = null;
        }
    }

    @Override
    //All quantities initialized at the start of the cutting splicing and replacement


    public void initialize(String source) {
        myFirst = new Node(source);
        myLast = myFirst;
        mySize = source.length();
        myAppends = 0;
        myIndex = 0;
        myLocalIndex = 0;
        myCurrent = myFirst;
    }
    //initialising the variables required in the class
    private Node myFirst, myLast, myCurrent;
    private long mySize;
    private int myAppends, myIndex, myLocalIndex;

    public LinkStrand(){
        this("");
    }

    public LinkStrand(String s){
        initialize(s);
    }
//Overridden method for size. Returns the size of the DNA strand
    @Override
    public long size() {
        return mySize;
    }
//converts the DNA contained in the node of a linkedlist to a string
    //appends each nodes info to a string builder and then returns the string builder by converting it to a string
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        Node n = myFirst;
        while(n != null){
            sb.append(n.info);
            n = n.next;
        }
        return sb.toString();
    }

    @Override
    public IDnaStrand getInstance(String source) {
        return new LinkStrand(source);
    }

    //Append is a mutator, adds the required DNA to the LinkedStrand object and increases teh size variable and the number of appends when called
    //Creates a new node with the defined DNA sequence and add it to the end
    @Override
    public IDnaStrand append(String dna) {
        myLast.next = new Node(dna);
        myLast = myLast.next;
        mySize = mySize + dna.length();
        myAppends++;
        return this;
    }
//Flips the LinkStrand by reversing the LinkedList and reversing the string
    //The new reversed list is stored in a StringBuilder which is returned
    @Override
    public IDnaStrand reverse() {
        LinkStrand reverse = new LinkStrand();
        Node n = this.myFirst;
        while(n != null){
            String str = new StringBuilder(n.info).reverse().toString();
            Node back = new Node(str);
            back.next = reverse.myFirst;
            reverse.myFirst = back;
            reverse.mySize = reverse.mySize + reverse.myFirst.info.length();
            n = n.next;
        }

        return reverse;
    }
//returns the number of times .append is called
    //myAppends is modified and increased by 1 everytime .append is called
    @Override
    public int getAppendCount() {
        return myAppends;
    }
//Finds the character at a defined index of the LinkStrand by iterating through
    //the DNA present in each node of the LinkedList
    @Override
    public char charAt(int index) {
        if (index < 0 || index >= this.mySize){
            throw new IndexOutOfBoundsException("Out of Bounds");
        }
        if(myIndex > index || index == 0){
            myIndex = 0;
            myLocalIndex = 0;
            myCurrent = myFirst;
        }
        while(index != myIndex){
            myIndex++;
            myLocalIndex++;
            if(myLocalIndex >= myCurrent.info.length()){
                myLocalIndex = 0;
                myCurrent = myCurrent.next;
            }
        }
        return myCurrent.info.charAt(myLocalIndex);
    }
}
