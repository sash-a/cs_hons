//
// Created by sasha on 2019/05/23.
//

#ifndef PARALLEL_SORT_OMP_SORT_H
#define PARALLEL_SORT_OMP_SORT_H

#include <iostream>
#include <omp.h>
#include <cmath>
#include "utils.h"
#include "seq_sort.h"

using namespace std;

namespace omp
{
    void qs_rec_balanced(int *v, int thresh, long len)
    {
        long local_size = len / omp_get_max_threads();
        for (int i = 0; i < omp_get_max_threads(); ++i)
        {
            #pragma omp task
            seq::qs(v, thresh, i * local_size, i * local_size + local_size - 1);
        }
        #pragma omp taskwait
        v = utils::merge_all(v, len / omp_get_max_threads(), omp_get_max_threads());
    }


    void qs_rec_tasks(int *v, int thresh, long l, long h)
    {
        if (h - l < thresh)
            utils::insertionsort(v, l, h);
        else
        {
            int pivot = utils::partition(v, l, h);

            if (true)
                //if (omp_get_num_threads() <= omp_get_max_threads())
            {
                #pragma omp task
                qs_rec_tasks(v, thresh, l, pivot - 1);
                #pragma omp task
                qs_rec_tasks(v, thresh, pivot + 1, h);
            } else
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
