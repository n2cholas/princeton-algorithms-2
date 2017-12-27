import java.util.LinkedHashSet;
import edu.princeton.cs.algs4.Queue;

public class BoggleSolver
{  
  private LinkedHashSet<String> validWords;
  private boolean[][] marked;
  private int r, c;
  private BoggleBoard board;
  private Node dict;
  
  private static class Node {
    private boolean val = false;
    private Node[] next = new Node[26];
  }
  
  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    if (dictionary == null)
      throw new java.lang.IllegalArgumentException();
    
    dict = new Node();
    
    for (String word : dictionary)
      add(dict, word);
  }
  
  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    if (board == null)
      throw new java.lang.IllegalArgumentException();
    
    validWords = new LinkedHashSet<String>();
    r = board.rows();
    c = board.cols();
    marked = new boolean[r][c];
    this.board = board;
    for (int i = 0 ; i < r ; i++) {
      for (int j = 0 ; j < c ; j++) {
        addWords("", i, j, dict);
      }
    }
    
    return validWords;
  }
  
  private void addWords (String soFar, int i, int j, Node root) {
    if (i < 0 || i >= r || j < 0 || j >= c || marked[i][j]|| !hasPrefix(root, String.valueOf(board.getLetter(i, j)))) // keysWithPrefix(root, String.valueOf(board.getLetter(i, j))).isEmpty())
      return;
    
    char cur = board.getLetter(i, j);
    
    if (cur == 'Q') {
      soFar = soFar.concat("QU");
      if (soFar.length() > 2 && contains(root, "QU"))
        validWords.add(soFar);
    }
    else {
      soFar = soFar.concat(String.valueOf(cur));
      if (soFar.length() > 2 && contains(root, String.valueOf(cur)))
        validWords.add(soFar);
    }
    
    marked[i][j] = true;
    for (int x = -1 ; x <= 1 ; x++){
      for (int y = -1 ; y <= 1 ; y++) {
        if (cur == 'Q')
          addWords(soFar, i+x, j+y, root.next[cur-'A'].next['U'-'A']);
        else
          addWords(soFar, i+x, j+y, root.next[cur-'A']);
      }
    }
    marked[i][j] = false;
  }
  
  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (word == null)
      throw new java.lang.IllegalArgumentException();
    else if (!contains(dict, word) || word.length() <= 2) 
      return 0;
    else if (word.length() <= 4)
      return 1;
    else if (word.length() == 5)
      return 2;
    else if (word.length() == 6)
      return 3;
    else if (word.length() == 7)
      return 5;
    else
      return 11;
  }
  
  private void add(Node root, String key) {
    root = add(root, key, 0);
  }
  
  private Node add(Node x, String key, int d){
    if (x == null) x = new Node();
    if (d == key.length()) {
      x.val = true;
      return x;
    }
    
    int c = key.charAt(d) - 'A';
    x.next[c] = add(x.next[c], key, d+1);
    return x;
  }
  
  private boolean contains(Node root, String key){
    Node x = get(root, key, 0);
    if (x == null) return false;
    return x.val;
  }
  
  private Node get(Node x, String key, int d){ 
    if (x == null) return null;
    if (d == key.length()) return x;
    int c = key.charAt(d) -'A';
    return get(x.next[c], key, d+1);
  }
  
  private boolean hasPrefix (Node root, String prefix) {
    Node x = get(root, prefix, 0);
    if (x == null) return false;
    else return true;
  }
}
