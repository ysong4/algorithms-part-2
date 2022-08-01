# Undirect Graphs

## Notes

Simple: use vertex exactly once.


## Nonrecursive depth-first search. 

Implement depth-first search in an undirected graph without using recursion.

### My Answer

Use a stack.

## Diameter and center of a tree. 

Given a connected graph with no cycles

- Diameter: design a linear-time algorithm to find the longest simple path in the graph.

- Center: design a linear-time algorithm to find a vertex such that its maximum distance from any other vertex is minimized.

### My Answer

- Diameter:

- Center: 

  1. Do BFS on each vertex. After BFS, we will know distance from that vertex to all other vertexs. And for each vertex we have a maximum.
  2. Loop through these maximums, find the smallest and its corresponding vertex. That's the center.

### Official Answer

> Hint (diameter): to compute the diameter, pick a vertex ss; run BFS from ss; then run BFS again from the vertex that is furthest from ss.
> Hint (center): consider vertices on the longest path.

## Euler cycle. 

An Euler cycle in a graph is a cycle (not necessarily simple) that uses every edge in the graph exactly one.

- Show that a connected graph has an Euler cycle if and only if every vertex has even degree.

- Design a linear-time algorithm to determine whether a graph has an Euler cycle, and if so, find one.

### My Answer

1. Show that a connected graph has an Euler cycle if and only if every vertex has even degree. 

->
Vertex need to be in even degreee, so that the path could come to this vertex and then leave this vertex. If a vertex has odd vertex, then a path will eventually stuck at this vertex, making it impossible to form a cycle.

<-
Assume it does not has a Euler cycle, then a path must has an end vertex, and the end vertex must be in odd degree. This contradicts with the even degree assumption.

2. Iterate the Graph, find the number of edges in each vertex. If all vertex having even degree, then it has an Euler cycle.

- Solution:
  1. Find the vertex with the most edges as the starting point.
  2. Next vertex chosen strategy:
    1. DesVertex[i].unvisited == 2: highest priority
    2. DesVertex[i].unvisited == 3: second high...
    ...
    100. DesVertex[i].unvisitedEdge == 1: Lowest priority 

### Official Answer

> Hint: use depth-first search and piece together the cycles you discover.

An online resource explain and demostrate this: https://math.unm.edu/~loring/links/discrete_f05/euler.pdf

### Solution from Internet

https://www.geeksforgeeks.org/fleurys-algorithm-for-printing-eulerian-path/

```
We first find the starting point which must be an odd vertex (if there are odd vertices) and store it in variable ‘u’. If there are zero odd vertices, we start from vertex ‘0’. We call printEulerUtil() to print Euler tour starting with u. We traverse all adjacent vertices of u, if there is only one adjacent vertex, we immediately consider it. If there are more than one adjacent vertices, we consider an adjacent v only if edge u-v is not a bridge. How to find if a given edge is a bridge? We count several vertices reachable from u. We remove edge u-v and again count the number of reachable vertices from u. If the number of reachable vertices is reduced, then edge u-v is a bridge. To count reachable vertices, we can either use BFS or DFS, we have used DFS in the above code. The function DFSCount(u) returns several vertices reachable from u. 
```
