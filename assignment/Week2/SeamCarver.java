import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver {
  private Picture picture;
  private int height;
  private int width;
  private double[][] energyStore;

  // create a seam carver object based on the given picture
  public SeamCarver(Picture picture) {
    if (picture == null) {
      throw new IllegalArgumentException();
    }

    this.picture = new Picture(picture);
    this.height = picture.height();
    this.width = picture.width();

    this.energyStore = new double[this.width][this.height];
    
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        this.energyStore[x][y] = calculateEnergyOnPixel(x, y, this.picture);
      }
    }
  }

  private double calculateEnergyOnPixel(int x, int y, Picture pic) {
    // On the boundary
    if (x == 0 || x == pic.width() - 1 || y == 0 || y == pic.height() - 1) {
      return 1000;
    }

    // NOT on the boundary
    Color top = pic.get(x, y - 1);
    Color bottom = pic.get(x, y + 1);
    Color left = pic.get(x - 1, y);
    Color right = pic.get(x + 1, y);

    double rx = Math.abs(left.getRed() - right.getRed());
    double gx = Math.abs(left.getGreen() - right.getGreen());
    double bx = Math.abs(left.getBlue() - right.getBlue());

    double ry = Math.abs(top.getRed() - bottom.getRed());
    double gy = Math.abs(top.getGreen() - bottom.getGreen());
    double by = Math.abs(top.getBlue() - bottom.getBlue());

    double deltaX = Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2);
    double deltaY = Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2);

    return Math.sqrt(deltaX + deltaY);
  }

  // current picture
  public Picture picture() {
    return new Picture(this.picture);
  }

  // width of current picture
  public int width() {
    return this.width;
  }

  // height of current picture
  public int height() {
    return this.height;
  }

  // energy of pixel at column x and row y
  public double energy(int x, int y) {
    if (x < 0 || x > this.width - 1) {
      throw new IllegalArgumentException();
    }
    if (y < 0 || y > this.height - 1) {
      throw new IllegalArgumentException();
    }

    return this.energyStore[x][y];
  }

  // sequence of indices for horizontal seam
  public int[] findHorizontalSeam() {
    int vCount = 2 + this.width * this.height;

    EdgeWeightedDigraph g = new EdgeWeightedDigraph(vCount);

    int start = vCount - 2;
    int end = vCount - 1;

    // Add start and end edges
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {

        int current = x + y * this.width;

        if (x == 0) {
          DirectedEdge edge = new DirectedEdge(start, current, this.energy(x, y));
          g.addEdge(edge);
        }

        if (x == this.width - 1) {
          DirectedEdge edge = new DirectedEdge(current, end, 0);
          g.addEdge(edge);
        }

      }
    }

    // Add inner edges
    for (int x = 0; x < this.width - 1; x++) {
      for (int y = 0; y < this.height; y++) {

        int current = x + y * this.width;

        if (y != 0) {
          int toX = x + 1;
          int toY = y - 1;
          int to = toX + toY * this.width;

          DirectedEdge edge = new DirectedEdge(current, to, this.energy(toX, toY));
          g.addEdge(edge);
        }

        if (y != this.height - 1) {
          int toX = x + 1;
          int toY = y + 1;
          int to = toX + toY * this.width;

          DirectedEdge edge = new DirectedEdge(current, to, this.energy(toX, toY));
          g.addEdge(edge);
        }

        int toX = x + 1;
        int toY = y;
        int to = toX + toY * this.width;

        DirectedEdge edge = new DirectedEdge(current, to, this.energy(toX, toY));
        g.addEdge(edge);

      }
    }

    DijkstraSP sp = new DijkstraSP(g, start);

    Iterable<DirectedEdge> edges = sp.pathTo(end);

    int[] result = new int[this.width];
    for (DirectedEdge edge : edges) {
      if (edge.to() == end) {
        continue;
      }

      int x = edge.to() % this.width;
      int y = edge.to() / this.width;
      result[x] = y;
    }

    return result;
  }

  // sequence of indices for vertical seam
  public int[] findVerticalSeam() {
    int vCount = 2 + this.width * this.height;

    EdgeWeightedDigraph g = new EdgeWeightedDigraph(vCount);

    int start = vCount - 2;
    int end = vCount - 1;

    // Add start and end edges
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {

        int current = x + y * this.width;

        if (y == 0) {
          DirectedEdge edge = new DirectedEdge(start, current, this.energy(x, y));
          g.addEdge(edge);
        }

        if (y == this.height - 1) {
          DirectedEdge edge = new DirectedEdge(current, end, 0);
          g.addEdge(edge);
        }

      }
    }

    // Add inner edges
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height - 1; y++) {

        int current = x + y * this.width;

        if (x != 0) {
          int toX = x - 1;
          int toY = y + 1;
          int to = toX + toY * this.width;

          DirectedEdge edge = new DirectedEdge(current, to, this.energy(toX, toY));
          g.addEdge(edge);
        }

        if (x != this.width - 1) {
          int toX = x + 1;
          int toY = y + 1;
          int to = toX + toY * this.width;

          DirectedEdge edge = new DirectedEdge(current, to, this.energy(toX, toY));
          g.addEdge(edge);
        }

        int toX = x;
        int toY = y + 1;
        int to = toX + toY * this.width;

        DirectedEdge edge = new DirectedEdge(current, to, this.energy(toX, toY));
        g.addEdge(edge);

      }
    }

    DijkstraSP sp = new DijkstraSP(g, start);

    Iterable<DirectedEdge> edges = sp.pathTo(end);

    int[] result = new int[this.height];
    for (DirectedEdge edge : edges) {
      if (edge.to() == end) {
        continue;
      }

      int x = edge.to() % this.width;
      int y = edge.to() / this.width;
      result[y] = x;
    }

    return result;
  }

  // remove horizontal seam from current picture
  public void removeHorizontalSeam(int[] seam) {
    if (seam == null) {
      throw new IllegalArgumentException();
    }

    if (seam.length != this.width) {
      throw new IllegalArgumentException();
    }

    for (int i = 1; i < seam.length; i++) {
      if (Math.abs(seam[i] - seam[i - 1]) > 1) {
        throw new IllegalArgumentException();
      }

      if (seam[i - 1] < 0 || seam[i - 1] > this.height - 1) {
        throw new IllegalArgumentException();
      }
    }

    if (this.height <= 1) {
      throw new IllegalArgumentException();
    }

    Picture newPicture = new Picture(this.width, this.height - 1);

    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {

        if (y == seam[x]) {
          continue;
        }

        if (y > seam[x]) {
          newPicture.setRGB(x, y - 1, this.picture.getRGB(x, y));
        } else {
          newPicture.setRGB(x, y, this.picture.getRGB(x, y));
        }
      }
    }

    double[][] newEnergyStore = new double[this.width][this.height - 1];

    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height - 1; y++) {

        if (y + 1 == seam[x] || y == seam[x]) {
          newEnergyStore[x][y] = calculateEnergyOnPixel(x, y, newPicture);
        } else if (y < seam[x] - 1) {
          newEnergyStore[x][y] = this.energyStore[x][y];
        } else if (y > seam[x]) {
          newEnergyStore[x][y] = this.energyStore[x][y + 1];
        }

      }
    }

    this.picture = newPicture;
    this.width = newPicture.width();
    this.height = newPicture.height();
    this.energyStore = newEnergyStore;
  }

  // remove vertical seam from current picture
  public void removeVerticalSeam(int[] seam) {
    if (seam == null) {
      throw new IllegalArgumentException();
    }

    if (seam.length != this.height) {
      throw new IllegalArgumentException();
    }

    for (int i = 1; i < seam.length; i++) {
      if (Math.abs(seam[i] - seam[i - 1]) > 1) {
        throw new IllegalArgumentException();
      }

      if (seam[i - 1] < 0 || seam[i - 1] > this.width - 1) {
        throw new IllegalArgumentException();
      }
    }

    if (this.width <= 1) {
      throw new IllegalArgumentException();
    }

    Picture newPicture = new Picture(this.width - 1, this.height);

    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {

        if (x == seam[y]) {
          continue;
        }

        if (x > seam[y]) {
          newPicture.setRGB(x - 1, y, this.picture.getRGB(x, y));
        } else {
          newPicture.setRGB(x, y, this.picture.getRGB(x, y));
        }
      }
    }

    double[][] newEnergyStore = new double[this.width - 1][this.height];

    for (int x = 0; x < this.width - 1; x++) {
      for (int y = 0; y < this.height; y++) {

        if (x + 1 == seam[y] || x == seam[y]) {
          newEnergyStore[x][y] = calculateEnergyOnPixel(x, y, newPicture);
        } else if (x < seam[y] - 1) {
          newEnergyStore[x][y] = this.energyStore[x][y];
        } else if (x > seam[y]) {
          newEnergyStore[x][y] = this.energyStore[x + 1][y];
        }

      }
    }

    this.picture = newPicture;
    this.width = newPicture.width();
    this.height = newPicture.height();
    this.energyStore = newEnergyStore;
  }

  // unit testing (optional)
  public static void main(String[] args) {
    var picture = new Picture("./3x4.png");

    var seamCarver = new SeamCarver(picture);

    var seam = seamCarver.findVerticalSeam();
    for (int i = 0; i < seam.length; i++) {
      StdOut.printf("%d\n", seam[i]);
    }
    seamCarver.removeVerticalSeam(seam);

    StdOut.println(seamCarver.width());
    StdOut.println(seamCarver.height());

    seamCarver.picture().save("./hihi.png");
  }

}