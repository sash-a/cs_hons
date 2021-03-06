data Expr = Val Int | App Op Expr Expr
data Op = Add | Mul

instance Show Expr where
    show (Val i) = (show i)
    show (App o l r) = (show l) ++ " " ++ (show o) ++ " " ++ (show r)

instance Show Op where
    show (Add) = "+"
    show (Mul) = "*"

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
exprs xs = [e | (ls, rs) <- split xs, l_expr <- exprs ls, r_expr <- exprs rs, e <- allops l_expr r_expr]
 
solve :: [Int] -> Int -> [Expr]
solve xs t = [e | p <- perms xs, e <- exprs p, (eval e) == t]