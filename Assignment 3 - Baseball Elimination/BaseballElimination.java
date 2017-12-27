import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import java.util.HashMap;
import java.util.ArrayList;

public class BaseballElimination {
  private final int num;
  private final int[] win, loss, left;
  private final int[][] games;
  private final HashMap<String, Integer> teams;
  private final String[] teamArray;
  private ArrayList<Bag<String>> certificate;
  
  public BaseballElimination(String filename) {                   // create a baseball division from given filename in format specified below
    if (filename == null)
      throw new java.lang.IllegalArgumentException();
    
    In in = new In(filename);
    num = in.readInt();
    win = new int[num];
    loss = new int[num];
    left = new int[num];
    games = new int[num][num];
    teams = new HashMap<String, Integer>();
    teamArray = new String[num];
    certificate = new ArrayList<Bag<String>>(num);
    
    for (int i = 0 ; i < num ; i++) {
      String teamString = in.readString();
      teams.put(teamString, i);
      teamArray[i] = teamString;
      win[i] = in.readInt();
      loss[i] = in.readInt();
      left[i] = in.readInt();
      certificate.add(new Bag<String>());
      for (int j = 0 ; j < num ; j++){
        games[i][j] = in.readInt();
      }
    }
  }
  
  public              int numberOfTeams() { return num;  }
  
  public Iterable<String> teams() { return teams.keySet(); }
  
  public              int wins(String team) {                     // number of wins for given team
    if (team == null || !teams.containsKey(team)) 
      throw new java.lang.IllegalArgumentException(); 
    return win[teams.get(team)];
  }
  
  public              int losses(String team) {                   // number of losses for given team
    if (team == null || !teams.containsKey(team)) 
      throw new java.lang.IllegalArgumentException(); 
    return loss[teams.get(team)];
  }
  
  public              int remaining(String team) {                // number of remaining games for given team
    if (team == null || !teams.containsKey(team)) 
      throw new java.lang.IllegalArgumentException(); 
    return left[teams.get(team)];
  }
  
  public              int against(String team1, String team2) {   // number of remaining games between team1 and team2
    if (team1 == null || team2 == null || !teams.containsKey(team1) || !teams.containsKey(team2)) 
      throw new java.lang.IllegalArgumentException(); 
    return games[teams.get(team1)][teams.get(team2)];
  }
  
  public          boolean isEliminated(String team) {             // is given team eliminated?
    if (team == null || !teams.containsKey(team)) 
      throw new java.lang.IllegalArgumentException(); 
    else if (!certificate.get(teams.get(team)).isEmpty())
      return true;

    int x = teams.get(team);
 
    //Check trivial elimination
    for (int i = 0 ; i < num ; i++) {
      if (i!=x && win[x] + left[x] < win[i]){
        certificate.get(x).add(teamArray[i]);
        return true;
      }
    }
    
    //Non trivial elimination
    int choose2 = choose(num, 2); //number of game vertices
    int s = choose2 + num;
    int t = choose2 + num + 1;
    int total = 0;
    FlowNetwork network = new FlowNetwork(2 + choose2 + num);
    for (int i = 0 ; i < num ; i++) {
      if (i!=x) {
        network.addEdge(new FlowEdge(i, t, win[x]+left[x]-win[i]));
      }
    }
    
    for (int i = 0, y = num; i < num; i++) {
      for (int j = i+1; j < num ; j++) {
        if (i != x && j != x){
          network.addEdge(new FlowEdge(s, y, games[i][j]));
          network.addEdge(new FlowEdge(y, j, Double.POSITIVE_INFINITY));
          network.addEdge(new FlowEdge(y, i, Double.POSITIVE_INFINITY));
          y++;
          total += games[i][j];
        }
      }
    }
    
    FordFulkerson ford = new FordFulkerson(network, s, t);    
    
    //System.out.println("team: " + x + "\n" + "total: " + total + "\n" + "ford.val: " + ford.value() ); //+ "\n"+ network
    
    if ( ((int) ford.value()) == total)
      return false;
   
    for (int i = 0 ; i < num ; i++) {
      if (i != x && ford.inCut(i))
        certificate.get(x).add(teamArray[i]);
    }
    
    return true;
  }
  
  private int choose (int n, int k) {
    int num = 1;
    for (int i = 1; i <= k ; i++)
      num *= (n-(k-i))/i;
    return num;
  }
  
  public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated
    if (team == null || !teams.containsKey(team)) 
      throw new java.lang.IllegalArgumentException(); 
    else if (isEliminated(team))
      return certificate.get(teams.get(team));
    else
      return null;
  }
}