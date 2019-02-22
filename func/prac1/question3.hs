halve :: [a] -> ([a], [a])
halve xs = splitAt (quot (length xs) 2) xs