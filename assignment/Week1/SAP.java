import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
  private final Digraph graph;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    this.graph = new Digraph(G);
    return;
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    if (v < 0 || v >= this.graph.V() || w < 0 || w >= this.graph.V()) {
      throw new IllegalArgumentException();
    }

    if (v == w) {
      return 0;
    }

    var vBFS = new BreadthFirstDirectedPaths(this.graph, v);
    var wBFS = new BreadthFirstDirectedPaths(this.graph, w);

    // Default set the path not found
    var shortest = -1;

    for (var i = 0; i < this.graph.V(); i++) {

      if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
        var currentLength = vBFS.distTo(i) + wBFS.distTo(i);

        if (shortest == -1 || currentLength < shortest) {
          shortest = currentLength;
        }
      }
    }

    return shortest;
  }

  // a common ancestor of v and w that participates in a shortest ancestral path;
  // -1 if no such path
  public int ancestor(int v, int w) {
    if (v < 0 || v >= this.graph.V() || w < 0 || w >= this.graph.V()) {
      throw new IllegalArgumentException();
    }

    if (v == w) {
      return v;
    }

    var vBFS = new BreadthFirstDirectedPaths(this.graph, v);
    var wBFS = new BreadthFirstDirectedPaths(this.graph, w);

    // Default set the path not found
    var shortest = -1;
    var ancestor = -1;

    for (var i = 0; i < this.graph.V(); i++) {

      if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
        var currentLength = vBFS.distTo(i) + wBFS.distTo(i);

        if (shortest == -1 || currentLength < shortest) {
          shortest = currentLength;
          ancestor = i;
        }
      }
    }

    return ancestor;
  }

  // length of shortest ancestral path between any vertex in v and any vertex in
  // w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException();
    }

    for (Integer vIdx : v) {
      if (vIdx == null || vIdx >= this.graph.V()) {
        throw new IllegalArgumentException();
      }
    }

    for (Integer wIdx : w) {
      if (wIdx == null || wIdx >= this.graph.V()) {
        throw new IllegalArgumentException();
      }
    }

    var shortest = -1;

    // Empty iterable
    if (!v.iterator().hasNext()) {
      return -1;
    }
    if (!w.iterator().hasNext()) {
      return -1;
    }

    var vBFS = new BreadthFirstDirectedPaths(this.graph, v);
    var wBFS = new BreadthFirstDirectedPaths(this.graph, w);

    for (var i = 0; i < this.graph.V(); i++) {

      if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
        var currentLength = vBFS.distTo(i) + wBFS.distTo(i);

        if (shortest == -1 || currentLength < shortest) {
          shortest = currentLength;
        }
      }
    }

    return shortest;

  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such
  // path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException();
    }

    for (Integer vIdx : v) {
      if (vIdx == null || vIdx >= this.graph.V()) {
        throw new IllegalArgumentException();
      }
    }

    for (Integer wIdx : w) {
      if (wIdx == null || wIdx >= this.graph.V()) {
        throw new IllegalArgumentException();
      }
    }

    // Empty iterable
    if (!v.iterator().hasNext()) {
      return -1;
    }
    if (!w.iterator().hasNext()) {
      return -1;
    }

    var shortest = -1;
    var ancestor = -1;

    var vBFS = new BreadthFirstDirectedPaths(this.graph, v);
    var wBFS = new BreadthFirstDirectedPaths(this.graph, w);

    for (var i = 0; i < this.graph.V(); i++) {

      if (vBFS.hasPathTo(i) && wBFS.hasPathTo(i)) {
        var currentLength = vBFS.distTo(i) + wBFS.distTo(i);

        if (shortest == -1 || currentLength < shortest) {
          shortest = currentLength;
          ancestor = i;
        }
      }
    }

    return ancestor;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph G = new Digraph(in);
    SAP sap = new SAP(G);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}
