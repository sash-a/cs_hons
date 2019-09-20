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
