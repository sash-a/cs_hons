// Created by sasha on 2019/05/23.

#ifndef PARALLEL_SORT_SEQ_SORT_H
#define PARALLEL_SORT_SEQ_SORT_H

#include <iostream>
#include "utils.h"

using namespace std;
namespace seq
{
    void qs(int v[], const int &thresh, int l, int h)
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
}
#endif //PARALLEL_SORT_SEQ_SORT_H
