# Countdown problem

## Questions

### Given these data types:

```data Expr = Val Int | App Op Expr Expr```

```data Op = Add | Mul```

#### Define these functions:

```eval :: Expr → Int```

```values :: Expr → [ Int ]```

```delete :: Int → [ Int ] → [ Int ]```

```perms :: [ Int ] → [ [ Int ] ]```

```split :: [ Int ] → [([Int], [Int])]```

```exprs :: [ Int ] → [ Expr ]```

```solve :: [ Int ] → Int → [ Expr ]```
