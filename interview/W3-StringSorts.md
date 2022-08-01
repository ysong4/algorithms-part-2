# String Sorts

## 2-sum

Given an array `a` of `n` 64-bit integers and a target value `T`, determine whether there are two distinct integers `i` and `j` such that `a[i] + a[j] = T`. Your algorithm should run in linear time in the worst case.

### My Answer

1. As it is fixed length, use LSD (least-significant-digit-first) sort the array
2. During LSD sort, will generate the `count[]`. For each `a[i]`, calculate the `a[j] = T - a[i]`. Instantly check if the `count[a[j]]` exist in the sorted `a[]`.

## American flag sort

Given an array of `n` objects with integer keys between `0` and `R-1`, design a linear-time algorithm to rearrange them in ascending order. Use extra space at most proportional to `R`.

### My Answer

No idea...

### Official Answer

(Just my understanding after reading Wiki)

Reference: 
- https://en.wikipedia.org/wiki/American_flag_sort
- https://stackoverflow.com/questions/8262797/american-flag-sort-optimization

1. Loop through the array, count each key's appearance -> `count int[] size R`.
2. From `count int[] size R`, generate `range Range[] size R`, each `Range` contains two field `hi` and `lo`, which describe each index's range `{lo, hi}`. 
3. Clone the `count int[]` to `progress int[]`, which keep records of next available slot for swap for a index.
3. Loop through the original array. See if the element is in the place.
  1. If not in the right place, swap the element to the right slot according to `progress int[]`. After swap, do `progress[i]++` accordingly.
    - After swap, if the new element does not suit this slot, repeat `3.1` again. 
  2. If in the right place, just check the next one.

## Cyclic rotations

Two strings `s` and `t` are cyclic rotations of one another if they have the same length and `s` consists of a suffix of `t` followed by a prefix of `t`. For example, "suffixsort" and "sortsuffix" are cyclic rotations.

Given `n` distinct strings, each of length `L`, design an algorithm to determine whether there exists a pair of distinct strings that are cyclic rotations of one another. For example, the following list of `n = 12` strings of length `L = 10` contains exactly one pair of strings ("suffixsort" and "sortsuffix") that are cyclic rotations of one another.

```
algorithms polynomial sortsuffix boyermoore
structures minimumcut suffixsort stackstack
binaryheap digraphdfs stringsort digraphbfs
```

The order of growth of the running time should be `n * L^2`(or better) in the worst case. Assume that the alphabet size `R` is a small constant. Signing bonus. Do it in `NnL` time in the worst case.

### My Answer

> Suffix array

1. Split each string `s`, split it by `L` times. From it generate `L` Substring.
```go
type struct SubString {
  ToSort string // a[0:i] - a[i:L], the second one
  Remaining string // a[0:i] - a[i:L], the first one 
  index int // index to retrieve the full string back from original array
}
```
2. In total, will generate `(L-1) * n` SubString. Sort them using MSD, will take `(L-1) ^ 2 * n` time in garantee.
3. Go through the sorted list, if finding same SubStrings (`ToSort` part equals), then check if their `Remaining` is also same and they are with different `index` value. If yes, then we found one cyclic rotations.

### Bonus

1. When generating SubString, also generate a SymbolTable accordingly. 
2. For each string, store two keys in the map.
  1. Key: `${ToSort}_${Remaining}`, Value: SubString struct.
  2. Key: `${Remaining}_${ToSort}`, Value: SubString struct.
3. If we encounter a string that, when update its SubString to the Symbol table, we find its keys already in the map, then we find a cyclic rotation.