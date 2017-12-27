
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Outcast  {
  final private WordNet wordnet;
  
  public Outcast(WordNet wordnet)  {        // constructor takes a WordNet object
    if (wordnet == null)
      throw new java.lang.IllegalArgumentException();
    
    this.wordnet = wordnet;
  }
  
  public String outcast(String[] nouns)  {   // given an array of WordNet nouns, return an outcast
    if (nouns == null)
      throw new java.lang.IllegalArgumentException();
    
    int max = -1;
    String outcast = "";
    
    for (String nounA : nouns) {
      int sum = 0;
      for (String nounB : nouns) {
        sum += wordnet.distance(nounA, nounB);
      }
      if (sum > max) {
        max = sum;
        outcast = nounA;
      }
      
    }
    
    return outcast;
  }
  
  public static void main(String[] args)  {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++)  {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}