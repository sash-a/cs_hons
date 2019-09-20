#!/usr/bin/python
import copy
import sys
from collections import deque


def main(file):
    stack = deque()
    stack.append({})

    with open(file, 'r+') as f:
        for line in f:
            symbol_table = stack[-1]
            line = line[:-1] if line.endswith('\n') else line

            if line.startswith('define'):
                _, var, val = line.split(' ')
                symbol_table[var] = val
            elif line.startswith('use'):
                _, var = line.split(' ')
                val = 'undefined'
                if var in symbol_table:
                    val = symbol_table[var]

                print('use %s = %s' % (var, val))
                continue
            elif line.startswith('beginscope'):
                stack.append(copy.deepcopy(symbol_table))
            elif line.startswith('endscope'):
                stack.pop()

            print(line)


if __name__ == '__main__':
    main(sys.argv[1])
