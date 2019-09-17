delete :: Int -> [Int] -> [Int]
delete n (x:xs) | n == x = xs
                | otherwise = [x]++delete n xs
delete _ [] = []