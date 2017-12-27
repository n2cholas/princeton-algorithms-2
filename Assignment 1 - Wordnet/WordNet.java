import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Bag;
import java.util.HashMap;
import java.util.Iterator;

public class WordNet  {
  private HashMap<String, Bag<Integer>> map;
  private SAP sap;
  private int len;
  
  // constructor takes the name of the two input files
  // n log n, n = # nouns
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null)
      throw new java.lang.IllegalArgumentException();
    
    parseSynsets(synsets);
    makeSAP(hypernyms);
  }
  
  private void parseSynsets(String syns)  { //puts all the synsets into the arraylist
    In in = new In(syns);
    map = new HashMap<String, Bag<Integer>>();
    
    while (in.hasNextLine()) {
      String[] fields = in.readLine().split(",");
      int id = Integer.parseInt(fields[0]);
      String[] nouns = fields[1].split(" ");
      
      for (int i = 0 ; i < nouns.length ; i++) {
        Bag<Integer> bag;
        if (map.containsKey(nouns[i])) bag = map.get(nouns[i]);
        else bag = new Bag<Integer>();
        bag.add(id);
        map.put(nouns[i].trim(), bag);
      }
      
      len = id + 1;
    }
  }
  
  private void makeSAP (String hyps)  {
    In in = new In(hyps);
    Digraph graph = new Digraph(len);
    
    while (in.hasNextLine()) {
      String[] connections = in.readLine().split(",");
      for (int i = 1 ; i < connections.length ; i ++)
        graph.addEdge(Integer.parseInt(connections[0]), Integer.parseInt(connections[i]));
    }
    
    sap = new SAP(graph);
  }
  
  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return map.keySet();
  }
  
  // is the word a WordNet noun?
  // log n
  public boolean isNoun(String word)  {
    if (word == null) throw new java.lang.IllegalArgumentException();
    
    return map.containsKey(word);
  }
  
  // distance between nounA and nounB (defined below)
  // distance(A, B) = distance is the minimum length of any ancestral path between any synset v of A and any synset w of B.
  public int distance(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) 
      throw new java.lang.IllegalArgumentException();
    
    return sap.length(map.get(nounA), map.get(nounB));
  }
  
  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) 
      throw new java.lang.IllegalArgumentException();
    
    int ancestor = sap.ancestor(map.get(nounA), map.get(nounB));

    StringBuilder synset = new StringBuilder();
    Iterator<String> keys = map.keySet().iterator();   
    
    while (keys.hasNext()) {
      String next = keys.next();
      for (int val : map.get(next)) {
        if (val == ancestor) {
          synset.append(next);
          synset.append(" ");
        }
      }
    }
    
    return synset.toString();
  }
}