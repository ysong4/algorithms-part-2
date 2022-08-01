import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

  private final WordNet net;

  public Outcast(WordNet wordnet) {
    this.net = wordnet;
  } // constructor takes a WordNet object

  public String outcast(String[] nouns) {
    int farest = -1;
    String result = "";

    for (var noun: nouns ) {
      int distanceSum = 0;
      for (var inner: nouns) {
        distanceSum += this.net.distance(noun, inner);
      }

      if (farest == -1 || distanceSum > farest ) {
        farest = distanceSum;
        result = noun;
      }
    }

    return result;
  } // given an array of WordNet nouns, return an outcast

  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  } // see test client below
}