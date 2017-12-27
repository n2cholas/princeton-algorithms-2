import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.AcyclicSP;
import java.awt.Color;

public class SeamCarver {
  private Picture pic;
  private double[] energy;
  private int w, h;

  
  public SeamCarver(Picture picture) {               // create a seam carver object based on the given picture
    if (picture == null)
      throw new java.lang.IllegalArgumentException();
    
    pic = new Picture(picture);
    w = pic.width();
    h = pic.height();
    
    calcEnergy();
  }
  
  private int pos(int x, int y) { return x*h+y; }
  
  private void calcEnergy() {
    energy = new double[w*h];
    
    for (int x = 0; x < w; x++){
      for (int y = 0 ; y < h; y++){
        if (x == 0 || y == 0 || x == w - 1 || y == h - 1)
          energy[pos(x,y)] = 1000;
        else {
          // ---------- Calculate dual gradient energy -------------
          // Get necessary colours
          // Note: get takes col, row
          Color 
            above = pic.get(x, y-1),
            below = pic.get(x, y+1),
            left = pic.get (x-1, y),
            right = pic.get(x+1, y);
          
          //Calculate differences
          int rx = left.getRed() - right.getRed();
          int gx = left.getGreen() - right.getGreen();
          int bx = left.getBlue() - right.getBlue();
          
          int ry = above.getRed() - below.getRed();
          int gy = above.getGreen() - below.getGreen();
          int by = above.getBlue() - below.getBlue();
          
          //Calculate gradients
          int grad_x_sq = rx*rx + gx*gx + bx*bx;
          int grad_y_sq = ry*ry + gy*gy + by*by;
          
          energy[pos(x,y)] = Math.sqrt(grad_x_sq + grad_y_sq);
        }
      }
    }
  }
  
  public Picture picture() {  return new Picture(pic); }
  
  public     int width() {  return w; }
  
  public     int height() {  return h; }
  
  public  double energy(int x, int y) {              // energy of pixel at column x and row y
    if (x < 0 || x >= w || y < 0 || y >= h)
      throw new java.lang.IllegalArgumentException();
    
    return energy[pos(x,y)];
  }
  
  public   int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
    EdgeWeightedDigraph graph = new EdgeWeightedDigraph(w*h + 2);
    
    for (int x = 0; x < w-1; x++){
      for (int y = 0 ; y < h; y++){        
        graph.addEdge(              new DirectedEdge(pos(x,y), pos(x+1,y),   energy(x+1, y)));
        if (y != 0) graph.addEdge(  new DirectedEdge(pos(x,y), pos(x+1,y-1), energy(x+1, y-1)));
        if (y != h-1) graph.addEdge(new DirectedEdge(pos(x,y), pos(x+1,y+1), energy(x+1, y+1)));
      }
    }
    
    for (int i = 0 ; i < h ; i ++) {
      graph.addEdge(new DirectedEdge(w*h, pos(0, i), energy(0,0)));
      graph.addEdge(new DirectedEdge(pos(w-1,i), w*h+1, energy(0,0)));
    }
    
    int[] seam = new int[w];
    AcyclicSP path = new AcyclicSP(graph, w*h);
    Iterable<DirectedEdge> pathTo = path.pathTo(w*h+1);
    int index = -1;
    for (DirectedEdge e : pathTo) {
      if (index >= 0 && index < seam.length){
        seam[index] = e.from()%h;
      }
      index++;
    }
    
    return seam;
  }
  
  public   int[] findVerticalSeam() {                // sequence of indices for vertical seam
    EdgeWeightedDigraph graph = new EdgeWeightedDigraph(w*h + 2);
    
    for (int x = 0; x < w; x++){
      for (int y = 0 ; y < h-1; y++){        
        graph.addEdge(              new DirectedEdge(pos(x,y), pos(x, y+1),  energy(x, y+1)));
        if (x != 0) graph.addEdge(  new DirectedEdge(pos(x,y), pos(x-1,y+1), energy(x-1, y+1)));
        if (x != w-1) graph.addEdge(new DirectedEdge(pos(x,y), pos(x+1,y+1), energy(x+1, y+1)));
      }
    }
    
    for (int i = 0 ; i < w ; i ++) {
      graph.addEdge(new DirectedEdge(w*h, pos(i, 0), energy(0,0)));
      graph.addEdge(new DirectedEdge(pos(i, h-1), w*h+1, energy(0,0)));
    }
    
    int[] seam = new int[h];
    AcyclicSP path = new AcyclicSP(graph, w*h);
    Iterable<DirectedEdge> pathTo = path.pathTo(w*h+1);
    int index = -1;
    for (DirectedEdge e : pathTo) {
      if (index >= 0 && index < seam.length){
        seam[index] = e.from()/h;
      }
      index++;
    }
    
    return seam;
  }
  
  public    void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
    if (seam == null || seam.length != w || seam[w-1] < 0 || seam[w-1] >= h)
      throw new java.lang.IllegalArgumentException();
    for (int i = 0; i < w - 1; i++) {
      if (seam[i] < 0 || seam[i] >= h || Math.abs(seam[i] - seam[i+1]) > 1)
        throw new java.lang.IllegalArgumentException();
    }
    
    h--;
    Picture temp = new Picture(w, h);
    boolean passed = false;
    
    for (int x = 0 ; x < w ; x++) {
      for (int y = 0 ; y < h ; y++) {
        if (seam[x] == y)
          passed = true;
        
        if (!passed)
          temp.set(x, y, pic.get(x, y));
        else
          temp.set(x, y, pic.get(x, y+1));
      }
      passed = false;
    }
    
    pic = temp;
  }
  
  public    void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture
    if (seam == null || seam.length != h || seam[h-1] < 0 || seam[h-1] >= w)
      throw new java.lang.IllegalArgumentException();
    for (int i = 0; i < h - 1; i++) {
      if (seam[i] < 0 || seam[i] >= w || Math.abs(seam[i] - seam[i+1]) > 1)
        throw new java.lang.IllegalArgumentException();
    }
    
    w--;
    Picture temp = new Picture(w, h);
    boolean passed = false;
    
    for (int y = 0 ; y < h ; y++) {
      for (int x = 0; x < w ; x++) {
        if (seam[y] == x)
          passed = true;
        
        if (!passed)
          temp.set(x, y, pic.get(x, y));
        else
          temp.set(x, y, pic.get(x+1, y));
      }
      passed = false;
    }
    
    pic = temp;
  }  
}