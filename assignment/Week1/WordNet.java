import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class WordNet {

  private final RedBlackBST<String, ArrayList<Integer>> nounToIdStore;
  private final ArrayList<String> idToSynsetStore;
  private final SAP sap;
  private int size; // Number of total vertex

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) {
      throw new IllegalArgumentException();
    }

    // Build two stores (maps)
    this.nounToIdStore = new RedBlackBST<String, ArrayList<Integer>>();
    this.idToSynsetStore = new ArrayList<String>();

    // Read in synsets
    In in = new In(synsets);
    while (in.hasNextLine()) {
      String line = in.readLine();

      String[] parts = line.split(",");

      int id = Integer.parseInt(parts[0]);

      this.idToSynsetStore.add(parts[1]);

      String[] nouns = parts[1].split(" ");
      for (String noun : nouns) {
        if (this.nounToIdStore.contains(noun)) {

          // NOT the first time read this noun
          var existedList = this.nounToIdStore.get(noun);
          existedList.add(id);

        } else {
          
          // First time read this noun
          var newList = new ArrayList<Integer>();
          newList.add(id);
          this.nounToIdStore.put(noun, newList);

        }
      }

      // Count how many vertex in total
      this.size++;
    }
    
    // Build the digraph with hypernyms file
    Digraph digraph = new Digraph(this.size);

    // Read in hypernyms
    in = new In(hypernyms);
    while (in.hasNextLine()) {
      String line = in.readLine();

      String[] parts = line.split(",");

      int id = Integer.parseInt(parts[0]);

      for (var i = 1; i < parts.length; i++) {
        int hypernymId = Integer.parseInt(parts[i]);

        digraph.addEdge(id, hypernymId);
      }
    }

    // Check if the Digraph has no cycle
    Topological topological = new Topological(digraph);
    if (!topological.hasOrder()) {
      throw new IllegalArgumentException();
    }
    // Check if it only has single root
    int rootCount = 0;
    for (var i =0; i< digraph.V(); i++) {
      if (digraph.outdegree(i) == 0) {
        rootCount++;
      }
    }
    if (rootCount > 1) {
      throw new IllegalArgumentException();
    }

    this.sap = new SAP(digraph);

    return;
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return this.nounToIdStore.keys();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException();
    }

    return this.nounToIdStore.contains(word);
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException();
    }

    if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    ArrayList<Integer> idA = this.nounToIdStore.get(nounA);
    ArrayList<Integer> idB = this.nounToIdStore.get(nounB);

    return this.sap.length(idA, idB);
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA
  // and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException();
    }

    if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
      throw new IllegalArgumentException();
    }

    ArrayList<Integer> idA = this.nounToIdStore.get(nounA);
    ArrayList<Integer> idB = this.nounToIdStore.get(nounB);
    int ancestor = this.sap.ancestor(idA, idB);

    return this.idToSynsetStore.get(ancestor);
  }

  // do unit testing of this class
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);

    StdOut.printf("distance(): %d\n", wordnet.distance("kinescope", "Gb"));

    return;
  }
}