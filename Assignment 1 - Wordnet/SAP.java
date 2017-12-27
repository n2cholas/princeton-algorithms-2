import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class SAP  {
  final private Digraph graph;
  private int shortLength = -1;
  private int shortAncestor = -1;
  
  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    if (G == null) 
      throw new java.lang.IllegalArgumentException();
    
    graph = new Digraph(G);
  }
  
  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    if (v == w)
      shortLength = 0;
    else
      findShort(v, w);
    
    return shortLength;
  }
  
  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    if (v == w)
      shortAncestor = v;
    else
      findShort(v, w);
    
    return shortAncestor;
  }
  
  private void findShort (int v, int w)  { //sets the global shortAncestor and Length variables
    if (v >= graph.V() || v < 0 || w >= graph.V() || w < 0)
      throw new java.lang.IllegalArgumentException();
    
    // initialization of BFS
    Queue<Integer> vq = new Queue<Integer>();
    Queue<Integer> wq = new Queue<Integer>();
    boolean[] vmarked = new boolean[graph.V()];
    boolean[] wmarked = new boolean[graph.V()];
    int[] vDistTo = new int[graph.V()];
    int[] wDistTo = new int[graph.V()];
    vmarked[v] = true;
    wmarked[w] = true;
    vq.enqueue(v);
    wq.enqueue(w);
    
    setShort(vq, wq, vmarked, wmarked, vDistTo, wDistTo);
  }
  
  private void findShort (Iterable<Integer> v, Iterable<Integer> w)  { //sets the global shortAncestor and Length variables  
    if (v == null || w == null)
      throw new java.lang.IllegalArgumentException();
    
    for (int vcur : v) {
      for (int wcur : w) {
        if (vcur == wcur) {
          shortLength = 0;
          shortAncestor = vcur;
          return;
        }
      }
    }
    
    // initialization of BFS
    Queue<Integer> vq = new Queue<Integer>();
    Queue<Integer> wq = new Queue<Integer>();
    boolean[] vmarked = new boolean[graph.V()];
    boolean[] wmarked = new boolean[graph.V()];
    int[] vDistTo = new int[graph.V()];
    int[] wDistTo = new int[graph.V()];
    Iterator<Integer> vi = v.iterator();
    Iterator<Integer> wi = w.iterator();
    
    while (vi.hasNext()) {
      int cur = vi.next();
      if (cur >= graph.V() || cur < 0)
        throw new java.lang.IllegalArgumentException();
      
      vmarked[cur] = true;
      vq.enqueue(cur);
    }
    
    while (wi.hasNext()) {
      int cur = wi.next();
      if (cur >= graph.V() || cur < 0)
        throw new java.lang.IllegalArgumentException();
      
      wmarked[cur] = true;
      wq.enqueue(cur);
    }
    
    setShort(vq, wq, vmarked, wmarked, vDistTo, wDistTo);
  }
    
  private void setShort (Queue<Integer> vq, Queue<Integer> wq, boolean[] vmarked, boolean[] wmarked, int[] vDistTo, int[] wDistTo)  {
    shortLength = -1;
    shortAncestor = -1;
      
    while ((!vq.isEmpty() || !wq.isEmpty())) {
      if (!vq.isEmpty())  {
        int v1 = vq.dequeue();
        for (int v2 : graph.adj(v1)) {
          if (!vmarked[v2]) {
            vq.enqueue(v2);
            vmarked[v2] = true;
            vDistTo[v2] = vDistTo[v1]+1;
          }
          if (wmarked[v2] && (vDistTo[v2] + wDistTo[v2] < shortLength || shortLength == -1)) {
            shortLength = vDistTo[v2] + wDistTo[v2];
            shortAncestor = v2;
          } 
        }
      }
      if (!wq.isEmpty())  {
        int w1 = wq.dequeue();
        for (int w2 : graph.adj(w1))  {
          if (!wmarked[w2]) {
            wq.enqueue(w2);
            wmarked[w2] = true;
            wDistTo[w2] = wDistTo[w1]+1;
          }
          if (vmarked[w2] && (wDistTo[w2] + vDistTo[w2] < shortLength || shortLength == -1))  {
            shortLength = wDistTo[w2] + vDistTo[w2];
            shortAncestor = w2;
          }
        }
      }
    }  
    
  }
  
  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    findShort(v, w);
    return shortLength;
  }
  
  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    findShort(v, w);
    return shortAncestor;
  }
  
  // do unit testing of this class
  public static void main(String[] args)  {
    In in = new In("wordnet/digraph1.txt");
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty())  {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length   = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}