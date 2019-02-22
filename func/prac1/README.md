## Questions

1. Define a function product that produces the product of a list of numbers. For example: product [2,3,4], should produce the solution: 24. (20%)

2. The library function last selects the last element of a non-empty list; for example: last [1,2,3,4,5] = 5. Write another definition for the last function in terms of the other library functions. (20%)

3. Using library functions, define a function halve :: [a] − > ([a],[a]) that splits an even length list into two halves. For example: halve [1,2,3,4,5,6], should produce: ([1,2,3],[4,5,6]). (20%)

4. Consider a function safetail :: [a] − > [a] that behaves in the same way as the tail function except that it maps the empty list to itself rather than producing an error. Using tail and the function null :: [a] − > Bool that decides if a list is empty or not, define safetail using only: (1) a conditional expression, (2) guarded equations, and (3) pattern matching. Defining three different functions for solving these three different problems is an acceptable approach. (40%)
