// Created by sasha on 2019/05/23.

#include "seq_sort.h"
#include "omp_sort.h"

using namespace std;

int main(int argc, char *argv[])
{
    srand(100);
    unsigned long long n_vals = 1000000; // default
    if (argc == 2)
        n_vals = stoull(argv[1]);
    printf("num vals, %llu\n", n_vals);
    printf("type, is_sorted, time\n");

    int *orig = new int[n_vals]; // original array so that all tests use the same
    int *v = new int[n_vals];
    utils::rand_arr(orig, n_vals);

    copy(orig, orig + n_vals, v);

    for (int i = 0; i < n_vals; ++i)
    {
        if (orig[i] != v[i]) printf("not coppied properly! %d\n", i);
    }
    omp_set_nested(1);

    //-------------------------------------------------
    // parallel recursive using tasks

    double start = omp_get_wtime();
    #pragma omp parallel default(none) shared(v, n_vals)
    {
        #pragma omp single nowait
        omp::qs_rec_tasks(v, 10, 0, n_vals - 1);
    }
    double end = omp_get_wtime();

    printf("tasks, %d, %f\n", utils::is_sorted(v, n_vals), end - start);

    //-------------------------------------------------
    // parallel recursive using sections

    copy(orig, orig + n_vals, v);

    start = omp_get_wtime();
    omp::qs_rec_tasks(v, 7, 0, n_vals - 1);
    end = omp_get_wtime();

    printf("sections, %d, %f\n", utils::is_sorted(v, n_vals), end - start);

    //-------------------------------------------------
    // sequential recursive

    copy(orig, orig + n_vals, v);

    start = omp_get_wtime();
    seq::qs(v, 7, 0, n_vals-1);
    end = omp_get_wtime();

    printf("sequential, %d, %f\n", utils::is_sorted(v, n_vals), end - start);

    delete[] v;
    delete[] orig;
    return 0;
}