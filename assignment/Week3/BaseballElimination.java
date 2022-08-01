import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
  private String[] teams;
  private Map<String, Integer> teamToIndex;
  private int[] wins;
  private int[] losses;
  private int[] remaining;
  private int[][] against;
  private boolean[] eliminated;
  private Map<String, ArrayList<String>> certificates;

  public BaseballElimination(String filename) {
    In in = new In(filename);

    String firstLine = in.readLine();
    int size = Integer.parseInt(firstLine);

    this.teams = new String[size];
    this.teamToIndex = new HashMap<String, Integer>();

    this.wins = new int[size];
    this.losses = new int[size];
    this.remaining = new int[size];
    this.against = new int[size][size];
    this.eliminated = new boolean[size];
    this.certificates = new HashMap<String, ArrayList<String>>();

    int count = 0;
    while (in.hasNextLine()) {
      String line = in.readLine();

      String[] words = line.trim().split("\\s+");

      String team = words[0];
      this.teams[count] = team;
      this.teamToIndex.put(team, count);

      this.wins[count] = Integer.parseInt(words[1]);
      this.losses[count] = Integer.parseInt(words[2]);
      this.remaining[count] = Integer.parseInt(words[3]);

      for (int i = 4; i < words.length; i++) {
        int tmp = Integer.parseInt(words[i]);
        this.against[count][i - 4] = tmp;
      }

      count++;
    }

    // Calculate elimination
    for (int teamId = 0; teamId < size; teamId++) {
      // ---- Trivial elimination ----

      boolean trivialEliminated = false;
      ArrayList<String> result = new ArrayList<String>();

      for (int i = 0; i < size; i++) {
        if (teamId != i && this.wins[teamId] + this.remaining[teamId] < this.wins[i]) {
          trivialEliminated = true;
          result.add(this.teams[i]);
        }
      }

      if (trivialEliminated) {
        this.eliminated[teamId] = true;
        this.certificates.put(this.teams[teamId], result);
        // Skip the non-trivial elimination
        continue;
      }

      // ---- Non-trivial elimination ----
      int totalV = 1 + (size * (size - 1) / 2) + size + 1;
      FlowNetwork g = new FlowNetwork(totalV);

      int s = 0;
      int t = 1;

      // Add edge from team vertices to `t`
      for (int i = 0; i < size; i++) {
        int v = i + 2;

        if (i == teamId) {
          // We are calculating if this team is eliminated, do not add it to the network.
          continue;
        }

        FlowEdge e = new FlowEdge(v, t, this.wins[teamId] + this.remaining[teamId] - this.wins[i]);
        g.addEdge(e);
      }

      int vid = size + 2;
      // Add edge from `s` to game vertices
      // Add edge from game vertices to team vertices
      for (int i = 0; i < size; i++) {
        for (int j = i + 1; j < size; j++) {

          if (i != teamId && j != teamId) {
            // Add edge from `s` to game vertices
            FlowEdge e = new FlowEdge(s, vid, this.against[i][j]);
            g.addEdge(e);

            // Add edge from game vertices to team vertices
            FlowEdge ei = new FlowEdge(vid, i + 2, Integer.MAX_VALUE);
            g.addEdge(ei);
            FlowEdge ej = new FlowEdge(vid, j + 2, Integer.MAX_VALUE);
            g.addEdge(ej);
          }

          vid++;
        }
      }

      var ff = new FordFulkerson(g, s, t);

      // Check if eliminated
      for (FlowEdge e : g.adj(s)) {
        if (e.flow() != e.capacity()) {
          this.eliminated[teamId] = true;

          // Record which team eliminate it
          for (int i = 0; i < size; i++) {
            int v = i + 2;

            if (ff.inCut(v) && i != teamId) {
              result.add(this.teams[i]);
            }
          }
          this.certificates.put(this.teams[teamId], result);

          break;
        }
      }

    }
  } // create a baseball division from given filename in format specified below

  public int numberOfTeams() {
    return this.teams.length;
  } // number of teams

  public Iterable<String> teams() {
    return Arrays.asList(this.teams);
  } // all teams

  public int wins(String team) {
    if (!this.teamToIndex.containsKey(team)) {
      throw new IllegalArgumentException();
    }

    return this.wins[this.teamToIndex.get(team)];
  } // number of wins for given team

  public int losses(String team) {
    if (!this.teamToIndex.containsKey(team)) {
      throw new IllegalArgumentException();
    }

    return this.losses[this.teamToIndex.get(team)];
  } // number of losses for given team

  public int remaining(String team) {
    if (!this.teamToIndex.containsKey(team)) {
      throw new IllegalArgumentException();
    }

    return this.remaining[this.teamToIndex.get(team)];
  } // number of remaining games for given team

  public int against(String team1, String team2) {
    if (!this.teamToIndex.containsKey(team1)) {
      throw new IllegalArgumentException();
    }
    if (!this.teamToIndex.containsKey(team2)) {
      throw new IllegalArgumentException();
    }

    return this.against[this.teamToIndex.get(team1)][this.teamToIndex.get(team2)];
  } // number of remaining games between team1 and team2

  public boolean isEliminated(String team) {
    if (!this.teamToIndex.containsKey(team)) {
      throw new IllegalArgumentException();
    }

    return this.eliminated[this.teamToIndex.get(team)];
  } // is given team eliminated?

  public Iterable<String> certificateOfElimination(String team) {
    if (!this.teamToIndex.containsKey(team)) {
      throw new IllegalArgumentException();
    }

    return this.certificates.get(team);
  } // subset R of teams that eliminates given team; null if not eliminated

  public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
      if (division.isEliminated(team)) {
        StdOut.print(team + " is eliminated by the subset R = { ");
        for (String t : division.certificateOfElimination(team)) {
          StdOut.print(t + " ");
        }
        StdOut.println("}");
      } else {
        StdOut.println(team + " is not eliminated");
      }
    }
  }
}
