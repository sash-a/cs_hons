product :: [Int] -> Int
product xs = foldl (*) 1 xs
-- implemented without foldl
product xs = prod xs ((length xs) - 1)
prod xs 0 = 1
prod xs i = prod xs (i-1) * xs !! i