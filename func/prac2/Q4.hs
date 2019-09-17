split :: [Int] -> [([Int], [Int])]
split xs = [(a,b) | n <- [1..((length xs) - 1)], let a = take n xs, let b = drop n xs]