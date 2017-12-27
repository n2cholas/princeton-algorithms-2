import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
  private static final int R = 256;
  
  // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
  public static void transform() {
    String str = BinaryStdIn.readString();
    CircularSuffixArray arr = new CircularSuffixArray(str);
    int len = arr.length();
    for (int i = 0 ; i < len ; i++)
      if (arr.index(i) == 0) BinaryStdOut.write(i);
    for (int i = 0 ; i < len ; i++) 
       BinaryStdOut.write(str.charAt( (arr.index(i)+len-1)%len ));
    
    BinaryStdOut.close();
  }
  
  // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
  public static void inverseTransform(){
    int first = BinaryStdIn.readInt();
    String t = BinaryStdIn.readString();
    
    int len = t.length();
    int[] sorter = new int[R];
    for (int i = 0 ; i < len ; i++) 
      sorter[t.charAt(i)]++; //records frequency of each character
    
    char[] sorted = new char[len];
    //the loop below records the starting position of each character in the sorder suffixes
    //also creates the sorted array
    for (int i = 0, prev = 0, temp = 0, start = 0; i < R ; i++) { 
      if (sorter[i] != 0) {
        for (int k = start ; k < sorter[i]+start ; k++)
          sorted[k] = (char) i;
        
        start += sorter[i]; //moves start to right position in sorted array for next iteration
        temp = prev;
        prev += sorter[i];
        sorter[i] = temp;
      }
    }
    
    int[] next = new int[len];
    for (int i = 0 ; i < len ; i++)  //creates next array
      next[sorter[t.charAt(i)]++] = i;
    
    for (int i = first, j = 0; j < len; i = next[i], j++)
      BinaryStdOut.write(sorted[i]);
    
    BinaryStdOut.close();
  }
  
  // if args[0] is '-', apply Burrows-Wheeler transform
  // if args[0] is '+', apply Burrows-Wheeler inverse transform
  public static void main(String[] args){
    if (args[0].equals("-")) transform();
    else if (args[0].equals("+")) inverseTransform();
    else throw new IllegalArgumentException();
  }
}