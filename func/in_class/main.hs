add :: Int -> (Int -> Int) -- apply to first arg recieve func then second arg then get final result
add x y = x + y

mx x y = if x > y then x else y

absx n | n >= 0 = n
       | otherwise = -n -- not working?

-- list stuff
-- generating lists
factors :: Int -> [Int]
factors n = [x | x <- [1..n], n `mod` x == 0]

prime :: Int -> Bool
prime n = factors n == [1,n]

-- concat
concatenate xs = concat [[1,2,3], [4,5,6], [7,8,9]]

reverse' :: [a] -> [a]
reverse' [] = []
reverse' (x:xs) = reverse xs ++ [x]

-- min
min' :: [Int] -> Int
min (x:xs) = min ([a | a <- xs, a <= x])