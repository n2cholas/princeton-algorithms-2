import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
  private static class Node {
    Node next;
    char val;
    
    public Node() {
      next = null;
    }
  }
  
  private static class LinkedList {
    Node head;
    public LinkedList() {
      head = new Node();
    }
    void fill(int start, int end) {
      Node x = head;
      x.val = (char) start;
      for (int i = start+1 ; i < end ; i++){
        x.next = new Node();
        x = x.next;
        x.val = (char) i;
      }
    }
    void push (char c) {
      Node x = head;
      head = new Node();
      head.val = c;
      head.next = x;
    }
    
    int indexAndRemove(char c){
      if (head.val == c) {
        head = head.next;
        return 0;
      }
      Node x = head;
      Node prev = head;
      int ind = 0;
      while (x.val != c){
        if (x.next == null) return -1;
        prev = x;
        x = x.next;
        ind++;
      }
      prev.next = x.next;
      return ind;   
    }
    char remove (int ind) {
      Node x = head;
      Node prev = head;
      while (ind-- > 0) {
        if (x.next == null) throw new java.lang.NullPointerException();
        prev = x;
        x = x.next;
      }
      char c = x.val;
      prev.next = x.next;
      return c;
    }
  }
  
  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    LinkedList alpha = new LinkedList();
    alpha.fill(0,256);

    while (!BinaryStdIn.isEmpty()){
      char next = BinaryStdIn.readChar();
      BinaryStdOut.write((char) alpha.indexAndRemove(next));
      alpha.push(next);
    }
    
    BinaryStdOut.close();
  }
  
  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    LinkedList alpha = new LinkedList();
    alpha.fill(0,256);
    
    while (!BinaryStdIn.isEmpty()) {
      char next = alpha.remove(BinaryStdIn.readChar());
      BinaryStdOut.write(next);
      alpha.push(next);
    }

    BinaryStdOut.close();
  }
  
  // if args[0] is '-', apply move-to-front encoding
  // if args[0] is '+', apply move-to-front decoding
  public static void main(String[] args){
    if (args[0].equals("-")) encode();
    else if (args[0].equals("+")) decode();
    else throw new java.lang.IllegalArgumentException();
  }
}

//CAAABCCCACCF
// java MoveToFront - < abra.txt | java edu.princeton.cs.algs4.HexDump 16