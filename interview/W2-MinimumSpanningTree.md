# Minimum Spanning Trees

## Bottleneck minimum spanning tree

Given a connected edge-weighted graph, design an efficient algorithm to find a minimum bottleneck spanning tree. The bottleneck capacity of a spanning tree is the weights of its largest edge. A minimum bottleneck spanning tree is a spanning tree of minimum bottleneck capacity.

### My answer

Kruskal's algorithm or Prim's algorithm should do the job.

### Official

> Hint: prove that an MST is a minimum bottleneck spanning tree.

reference: contradiction proof: https://www.geeksforgeeks.org/minimum-bottleneck-spanning-treembst/

> Extra challenge: Compute a minimum bottleneck spanning tree in linear time in the worst case. Assume that you can compute the median of nn keys in linear time in the worst case.

reference: https://stackoverflow.com/questions/22875799/how-to-compute-a-minimum-bottleneck-spanning-tree-in-linear-time

## Is an edge in a MST

Given an edge-weighted graph `G` and an edge `e`, design a linear-time algorithm to determine whether `e` appears in some MST of `G`.

Note: Since your algorithm must take linear time in the worst case, you cannot afford to compute the MST itself.

### My answer

Say the two vertices on edge `e` is `v` and `w`.

Create a cut, where `w` itself is on one side, `v` and all other vertices are on the other side.

Sort all the edges on `w` by weight in ascending order, if `e` is the edge with minimum weight, then `e` appears in some MST of `G`.

### Official answer

> Hint: consider the subgraph `G'` of `G` containing only those edges whose weight is strictly less than that of `e`.

reference: https://stackoverflow.com/questions/15049864/check-if-edge-is-included-in-some-mst-in-linear-time-non-distinct-values

## Minimum-weight feedback edge set

A feedback edge set of a graph is a subset of edges that contains at least one edge from every cycle in the graph. If the edges of a feedback edge set are removed, the resulting graph is acyclic. Given an edge-weighted graph, design an efficient algorithm to find a feedback edge set of minimum weight. Assume the edge weights are positive.

### My answer

Using the Kruskal's algorithm to calculate the MST. But need some changes.

1. Not stopping at V-1 edge found, loop through all edges.

2. Create a queue `result` to store all found edges.

3. Replace the UF with a tree to serve similar purposes.

At the same time, maintain a tree for the current MST.
The tree could help in:
  1. detecting whether the new edge create a cycle.
  2. if there is a cycle, return all the edges in the cycle.
  3. find the edge with minimum weight in the cycle, if the edge not in `result`, enqueue it. Otherwise, discard it.

4. Return the `result`.

### Official answer

This is actually to find a maximum spanning tree, and then find its corresponding complement set.

> For simplicity, let's just assume that G is connected (The algorithm to be described below can be easily extended for the case when G is not connected).

> First, for any feedback-edge set F, it must be true that the graph G' = (V, E-F) doesn't have any cycle. This follows directly from the definition of feedback-edge sets.

> Since we are looking for the set F that has minimum total weight, G' should be a tree and the edges in G' must have the maximum total weight possible (why ?)
> → G' must be a maximum spanning tree.

reference: https://softwareengineering.stackexchange.com/questions/261785/min-weight-feedback-edge-set-with-dfs/291621#291621

https://stackoverflow.com/questions/4992664/how-to-find-maximum-spanning-tree