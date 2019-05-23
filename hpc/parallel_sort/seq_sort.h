// Created by sasha on 2019/05/23.

#ifndef PARALLEL_SORT_SEQ_SORT_H
#define PARALLEL_SORT_SEQ_SORT_H

#include <iostream>
#include <vector>
#include <algorithm>
#include <tuple>
#include <omp.h>
#include "utils.h"

#define pair pair<int, int>

using namespace std;
namespace seq
{
    int selectPivot(vector<int> &v, const int &l, const int &h)
    {
        return v[l];
    }

    int partition(vector<int> &v, const int &l, const int &h)
    {
        // probably not needed
        int lt_pos = l; // position one past the last element smaller than pivot
        int pivot = v[h];

        for (int i = l; i < h; ++i)
        {
            if (v[i] < pivot)
            {
                if (i > lt_pos)
                    swap(v[i], v[lt_pos]); // swap vs iter_swap?

                ++lt_pos;
            }
        }
        swap(v[h], v[lt_pos]); // final swap to put pivot in correct position
        return lt_pos;
    }

    void insertionsort(vector<int> &v, const int &l, const int &h)
    {
        for (int i = l + 1; i < h; ++i)
        {
            int j = i;
            while (j > 0 && v[j - 1] > v[j])
            {
                swap(v[j - 1], v[j]);
                --j;
            }
        }
    }

    void quicksort(vector<int> &v, const int &thresh, int l, int h)
    {
        if (h - l < thresh)
            insertionsort(v, l, h);
        else
        {
            int pivot = partition(v, l, h);
            quicksort(v, thresh, l, pivot - 1);
            quicksort(v, thresh, pivot + 1, h);
        }
    }
}
#endif //PARALLEL_SORT_SEQ_SORT_H
