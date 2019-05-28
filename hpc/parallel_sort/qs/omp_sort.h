//
// Created by sasha on 2019/05/23.
//

#ifndef PARALLEL_SORT_OMP_SORT_H
#define PARALLEL_SORT_OMP_SORT_H

#include <iostream>
#include <vector>
#include <algorithm>
#include <tuple>
#include <omp.h>
#include <cmath>
#include "utils.h"

#define pair pair<int, int>

using namespace std;

namespace omp
{
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

    void qs_rec_tasks(int *v, const int &thresh, int l, int h)
    {
        if (h - l < thresh)
            utils::insertionsort(v, l, h);
        else
        {
            int pivot = utils::partition(v, l, h);

            if (true)
            // if (omp_get_num_threads() < omp_get_max_threads())
            {
                #pragma omp task
                qs_rec_tasks(v, thresh, l, pivot - 1);
                #pragma omp task
                qs_rec_tasks(v, thresh, pivot + 1, h);
            }
            else
            {
                qs_rec_tasks(v, thresh, l, pivot - 1);
                qs_rec_tasks(v, thresh, pivot + 1, h);

            }
        }
    }

    void qs_rec_section(int *v, const int &thresh, long l, long h)
    {
        if (h - l < thresh)
            utils::insertionsort(v, l, h);
        else
        {
            int pivot = utils::partition(v, l, h);
#pragma omp parallel sections default(none) shared(thresh) firstprivate(v, l, h, pivot)
            {
#pragma omp section
                qs_rec_section(v, thresh, l, pivot - 1);

#pragma omp section
                qs_rec_section(v, thresh, pivot + 1, h);

            }
        }
    }
}
#endif //PARALLEL_SORT_OMP_SORT_H
