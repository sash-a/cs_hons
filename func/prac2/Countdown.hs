data Expr = Val Int | App Op Expr Expr deriving (Show)
data Op = Add | Mul deriving (Show)

eval :: Expr -> Int
eval (Val x) = x
eval (App Add x y) = (eval x) + (eval y)
eval (App Mul x y) = (eval x) * (eval y)

values :: Expr -> [Int]
values (Val x) = [x]
values (App _ x y) = values x ++ values y

delete :: Int -> [Int] -> [Int]
delete n (x:xs) | n == x = xs
                | otherwise = [x]++delete n xs
delete _ [] = []

perms :: [Int] -> [[Int]]
perms [] = [[]]
perms xs = [x:ls | x <- xs, let l = delete x xs, ls <- perms l]

split :: [Int] -> [([Int], [Int])]
split xs = [(a,b) | n <- [1..((length xs) - 1)], let a = take n xs, let b = drop n xs]

allops :: Expr -> Expr -> [Expr]
allops a b = [App Add a b, App Mul a b]

exprs :: [Int] -> [Expr]
exprs [x] = [Val x]
exprs xs = [e | (ls, rs) <- split xs, l <- exprs ls, r <- exprs rs, e <- allops l r]

solve :: [Int] -> Int -> [Expr]
solve xs t = [e | p <- perms xs, e <- exprs p, eval e == t]