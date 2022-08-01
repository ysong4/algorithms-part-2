# Maximum Flow

## Fattest path

Given an edge-weighted digraph and two vertices `s` and `t`, design an `ElogE` algorithm to find a fattest path from `s` to `t`. The bottleneck capacity of a path is the minimum weight of an edge on the path. A fattest path is a path such that no other path has a higher bottleneck capacity.

### My Answer

When finding the first augmenting path (BFS), use a priority queue to stores all the edges (instead of store in a normal queue).

Say using the `IndexMaxPQ`.

Each element in PQ is the `i` vertex, sorted by `CapacityTo[i]`.

Maintained:

- `edgeTo[]`: which edge connect to this vertex.
- `capacityTo[]`: max capacity to this vertex from the connected edge.

After running the algorithm, will find a fatest path from `s` to `t`, and also find the fatest path from `s` to all other vertex.

```java
    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        marked = new boolean[G.V()];
        edgeTo = new FlowEdge[G.V()];
        capacityTo = new int[G.V()];

        // breadth-first search
        IndexMaxPQ<Integer> queue = new IndexMaxPQ<Integer>();
        queue.enqueue(s);
        marked[s] = true
        while (!queue.isEmpty()) {
            int v = queue.dequeue();

            for (FlowEdge e : G.adj(v)) {
                int w = e.other(v);

                // if residual capacity from v to w
                if (e.residualCapacityTo(w) > 0) {
                    if (!marked[w]) {
                        marked[w] = true
                        edgeTo[w] = e;
                        capacityTo[w] = e.residualCapacityTo(w);
                        queue.insert(w, capacityTo[w]);
                    } else {
                        // already marked, need to relax
                        if (e.residualCapacityTo(w) > capacityTo[w]) {
                          edgeTo[w] = e;
                          capacityTo[w] = e.residualCapacityTo(w);
                          queue.increaseKey(w, capacityTo[w]);
                        }
                    }
                }
            }
        }

        // is there an augmenting path?
        return marked[t];
    }
```

## Perfect matchings in k-regular bipartite graphs

Suppose that there are `n` men and `n` women at a dance and that each man knows exactly `k` women and each woman knows exactly `k` men (and relationships are mutual). Show that it is always possible to arrange a dance so that each man and woman are matched with someone they know.

### My Answer

No idea.

### Official Answer

> formulate the bipartite matching problem as a maxflow problem; find a (fractional) feasible flow of value `n`; conclude that there is a perfect matching.

Convert it to a MaxFlow or MinCut problem, see if there is `n` man on the solution.

## Maximum weight closure problem

A subset of vertices `S` in a digraph is closed if there are no edges pointing from `S` to a vertex outside `S`. Given a digraph with weights (positive or negative) on the vertices, find a closed subset of vertices of maximum total weight.

### My Answer

BFS find the connected component.

Calculate the total weight for each CC.

Sort all CC and find the max.

### Official Answer

> formulate as a mincut problem; assign edge `(v, w)` a weight of infinity if there is an edge from `v` to `w` in the original digraph.

Reference:
- https://en.wikipedia.org/wiki/Closure_problem
- http://gagguy.is-programmer.com/posts/17737

1. Connect all + vertex to `s` (edge weight is the vertex weight)
2. Connect all - vertex to `t` (edge weight is the vertex weigbt ABS)
3. All previous edge connect with infinity edge weight.
4. Calculate Max flow.
5. Maximum weight = (All + vertex weight) - MaxFlow
6. The subset of vertices: Get the final residual network, find the vertices that are reachable from `s`. Those are the vertices in the result subset.

Reference of `Given MaxFlow, find the MinCut`: https://www.geeksforgeeks.org/minimum-cut-in-a-directed-graph/
