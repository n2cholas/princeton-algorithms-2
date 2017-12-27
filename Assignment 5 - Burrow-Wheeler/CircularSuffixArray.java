import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
  private final int len;
  private int[] index;
  
  private static class CircularString {
    private char[] s;
    private int offset;
    
    public CircularString() {
      offset = 0;
    }
    
    public CircularString(String str) {
      if (str == null)
        throw new java.lang.IllegalArgumentException(); 
      int length = str.length();
      s = new char[length];
      offset = 0;
      for (int i = 0 ; i < length ; i++)
        s[i] = str.charAt(i);
    }
    
    public CircularString (String str, int set){
      this(str);
      
      if (set < 0 || set >= s.length)
        throw new java.lang.IllegalArgumentException(); 
      
      offset = set;
    }
    
    int charAt(int i){
      if (i < 0 || i >= s.length + offset)
        return -1;
      
      return s[(i+offset)%s.length];
    }
    
    CircularString substring(int start) {
      if (start < 0 || start >= s.length)
        throw new java.lang.IllegalArgumentException();
      CircularString sub = new CircularString();
      sub.s = s;
      sub.offset = start;
      return sub;
    }
    
    int getOffset() { return offset; }
  }
  
  public CircularSuffixArray(String s) {   // circular suffix array of s
    if (s == null)
      throw new java.lang.IllegalArgumentException();
    
    len = s.length();
    
    if (len != 0){
      CircularString[] str = new CircularString[len];
      str[0] = new CircularString(s);
      for (int i = 1 ; i < len ; i++) {
        str[i] = str[0].substring(i);
      }
      
      sort(str);
      
      index = new int[len];
      for (int i = 0 ; i < len ; i++)
        index[i] = str[i].getOffset();
    }
  }
  
  private static void sort (CircularString[] a) { sort(a, 0, a.length - 1, 0); }
  
  private static void sort (CircularString[] a, int lo, int hi, int d){
    if (hi <= lo) return;
    int lt = lo, gt = hi;
    int v = a[lo].charAt(d);
    int i = lo + 1;
    
    while (i <= gt) {
      int t = a[i].charAt(d);
      if (t < v) swap(a, lt++, i++);
      else if (t > v) swap(a, i, gt--);
      else i++;
    }
    
    sort (a, lo, lt-1, d);
    if (v >= 0) sort (a, lt, gt, d+1);
    sort(a, gt+1, hi, d);
  }
  
  private static void swap (CircularString[] a, int x, int y) {
    CircularString temp = a[x];
    a[x] = a[y];
    a[y] = temp;
  }
  
  
  public int length() {                    // length of s
    return len;
  }
  
  public int index(int i) {                // returns index of ith sorted suffix
    if (i < 0 || i >= len) 
      throw new java.lang.IllegalArgumentException();
    return index[i];
  }
  
  public static void main(String[] args) { // unit testing (required)     
    String test = "ABRACADABRA!";
    CircularSuffixArray tester = new CircularSuffixArray(test);
    
    for (int i = 0 ; i < tester.length(); i++) {
      StdOut.println(tester.index(i));
    }
  }
}