delete :: Int -> [Int] -> [Int]
delete n (x:xs) | n == x = xs
                | otherwise = [x]++delete n xs
delete _ [] = []

perms :: [Int] -> [[Int]]
perms [] = [[]]
perms xs = [x:ls | x <- xs, let l = delete x xs, ls <- perms l]
