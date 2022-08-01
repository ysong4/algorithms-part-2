# DirectedGraphs

Notes:

1. Detect cycle in DAG: https://www.geeksforgeeks.org/detect-cycle-in-a-graph/ 

## Shortest directed cycle

Given a digraph G, design an efficient algorithm to find a directed cycle with the minimum number of edges (or report that the graph is acyclic). The running time of your algorithm should be at most proportional to `V(E + V)` and use space proportional to `E + V`, where V is the number of vertices and E is the number of edges.

### My answer

Loop through all V. On each vertex X, do BFS to find if all shortest path. If the end vertex contains a edge to X, then the cycle is found.

Record the shortest cycle found for each vertex.

And return the mimnimum one.

## Hamiltonian path in a DAG

Given a directed acyclic graph, design a linear-time algorithm to determine whether it has a Hamiltonian path (a simple path that visits every vertex), and if so, find one.

### Official Answer

> Hint: topological sort.

https://stackoverflow.com/questions/16124844/algorithm-for-finding-a-hamiltonian-path-in-a-dag

Notes: Topological sort doesn't need a source vertex. Its input is just the DAG itself. And then the algo will do the rest.

## Reachable vertex

DAG: Design a linear-time algorithm to determine whether a DAG has a vertex that is reachable from every other vertex, and if so, find one.

Digraph: Design a linear-time algorithm to determine whether a digraph has a vertex that is reachable from every other vertex, and if so, find one.

### My answer

DAG: (not in linear time)

Loop every vertex X:
  Do DFS on X, create a set of reachable vertices for X

Now we have a list of Set, say SET[].
Result = SetX.intersect(SetY)
Loop remaining Sets:
  Result = Result.intersect(SetZ).

Digraph: 

Similar procedure, but using BFS.

### Official Answer

> Hint (DAG): compute the outdegree of each vertex.

https://stackoverflow.com/questions/15716318/linear-time-algorithm-to-determine-whether-a-dag-has-a-vertex-reachable-from-eve

Notes: Check the outdegree for every vertex. IIF only exists 1 vertex outdegree is 0, then the result is excatly this vertex.

> Hint (digraph): compute the strong components and look at the kernel DAG (the digraph that results when you contract each strong component to a single vertex).

Notes: After running strong-component algorithms, will have a Kernal DAG and the starting DAG will contains the TARGET vertex.
