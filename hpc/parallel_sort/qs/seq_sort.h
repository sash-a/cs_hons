// Created by sasha on 2019/05/23.

#ifndef PARALLEL_SORT_SEQ_SORT_H
#define PARALLEL_SORT_SEQ_SORT_H

#include <iostream>
#include "utils.h"

using namespace std;
namespace seq
{
    void qs(int v[], const int &thresh, long l, long h)
    {
        if (h - l < thresh)
            utils::insertionsort(v, l, h);
        else
        {
            int pivot = utils::partition(v, l, h);
            qs(v, thresh, l, pivot - 1);
            qs(v, thresh, pivot + 1, h);
        }
    }

    void qs_itter(int v[], const int &thresh, long l, long h)
    {
        pair stack[10];
        int top = -1;
        stack[++top] = pair(l, h);

        while (top >= 0)
        {
            tie(l, h) = stack[top--];

            if (h - l < thresh)
                utils::insertionsort(v, l, h);
            else
            {
                int pivot = utils::partition(v, l, h);
                stack[++top] = pair(l, pivot - 1);
                stack[++top] = pair(pivot + 1, h);
            }
        }
    }
}
#endif //PARALLEL_SORT_SEQ_SORT_H
