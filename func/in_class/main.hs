add :: Int -> (Int -> Int) -- apply to first arg recieve func then second arg then get final result
add x y = x + y

mx x y = if x > y then x else y

absx n | n >= 0    = n
       | otherwise = -n -- not working?

