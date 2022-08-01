# Shortest Paths

## Monotonic shortest path

Given an edge-weighted digraph `G`, design an `ElogE` algorithm to find a monotonic shortest path from `s` to every other vertex. A path is monotonic if the sequence of edge weights along the path are either strictly increasing or strictly decreasing.

### My Answer

1. `edgeTo[V] -> Edge[]`, `distTo[V] -> float[]`, `ascending[V] -> boolean[]`.
2. Dijkstra's Algo, pick next V by distTo (IndexMinPQ).
3. Relax Edge: `E` times in max.

> My solution may not work

### Official Answer

> Hint: relax edges in ascending order to find a best monotonically increasing path; relax edges in descending order to find a best monotonically decreasing path.

The question has hidden assumption actually (https://algs4.cs.princeton.edu/44sp/):
- have no negative weight.

Use Dijkstra's Algo twice should work. Modified the `edge relaxation` logic.

## Second shortest path

Given an edge-weighted digraph and let `P` be a shortest path from vertex `s` to vertex `t`. Design an `ElogV` algorithm to find a path (not necessarily simple) other than `P` from `s` to `t` that is as short as possible. Assume all of the edge weights are strictly positive.

### My Answer

1. Run Dijkstra's algo, find the `P`.
2. Remove the edge with smallest weight in `P`.
3. Run Dijkstra's algo again.

> My solution may not work

### Official Answer

> Hint: compute the shortest path distances from `s` to every vertex and the shortest path distances from every vertex to `t`.

1. Do Dijkstra from `s` first.
2. Reverse the graph, do the Dijkstra from `t` again.
3. Find the second shortest.

## Shortest path with one skippable edge

Given an edge-weighted digraph, design an `ElogV` algorithm to find a shortest path from `s` to `t` where you can change the weight of any one edge to zero. Assume the edge weights are nonnegative.

### My Answer

1. Run Dijkstra's algo. Find the shortest path, say P0.
2. Loop all incoming edge on `t`, find other paths that could reach `t`. Say these paths are P1, P2... PN.
3. Calculate D0 = P0.dist - maxEdge in P0, D1 = P1.dist - maxEdge in P1... DN = PN.dist - maxEdge in PN.
4. Sort all the D0 to DN, find the minimum D. Its corresponding P should be the shortest path.

> My solution may not work

### Official Answer

> Hint: compute the shortest path distances from `s` to every vertex and the shortest path distances from every vertex to `t`.

1. Do Dijkstra from `s` first.
2. Reverse the graph, do the Dijkstra from `t` again.
3. Loop through all the path, .

