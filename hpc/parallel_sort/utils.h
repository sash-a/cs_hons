//
// Created by sasha on 2019/05/20.
//

#ifndef PARALLEL_SORT_UTILS_H
#define PARALLEL_SORT_UTILS_H


#include <iostream>
#include <vector>
#include <algorithm>
#include <tuple>
#include <omp.h>

using namespace std;
namespace utils
{
    void print_vec(int v[], const int size)
    {
        for (int i = 0; i < size; ++i)
            printf("%d, ", v[i]);
    }

    int* rand_arr(int v[], const int &size)
    {
        for (int i = 0; i < size; ++i)
            v[i] = int(rand() % 100);

    }

    bool is_sorted(int v[], const int &size)
    {
        for (int j = 1; j < size; ++j)
        {
            if (v[j - 1] > v[j])
                return false;
        }

        return true;
    }

    int selectPivot(int v[], const int &l, const int &h)
    {
        return l;
    }

    int partition(int v[], const int &l, const int &h, bool debug = false)
    {
        if (debug)
        {
            printf("\n");
            print_vec(v, 10);
            printf("\n");
        }

        int lt_pos = l; // position one past the last element smaller than pivot

        // putting the pivot in the last position
        swap(v[selectPivot(v, l, h)], v[h]);
        int pivot = v[h];

        if (debug) printf("%d\n", pivot);

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

    void insertionsort(int v[], const int &l, const int &h)
    {
        for (int i = l + 1; i <= h; ++i)
        {
            int j = i;
            while (j > 0 && v[j - 1] > v[j])
            {
                std::swap(v[j - 1], v[j]);
                --j;
            }
        }
    }
}

#endif //PARALLEL_SORT_UTILS_H
