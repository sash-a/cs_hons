safetail :: [a] -> [a]
-- conditional
safetail xs = if null xs then xs else tail xs
-- guarded eq
safetail xs | null xs = xs
            | otherwise = tail xs
-- pattern matching
safetail [] = []
safetail xs = tail xs