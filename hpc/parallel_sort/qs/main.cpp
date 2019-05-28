// Created by sasha on 2019/05/23.

#include "seq_sort.h"
#include "omp_sort.h"


using namespace std;

int main(int argc, char *argv[])
{
    srand(100);
    long n_vals = 1000000; // default
    if (argc > 1) n_vals = stoull(argv[1]);

    /*
     * Determines what will be run
     * 0 = all
     * 1 = tasks
     * 2 = sections
     * 3 = sequential
     */
    int type = 0;
    if (argc > 2) type = atoi(argv[2]);


    double start, end;

    int *orig = new int[n_vals]; // original array so that all tests use the same
    int *v = new int[n_vals];
    utils::rand_arr(orig, n_vals);
    copy(orig, orig + n_vals, v);

    omp_set_nested(1);

    //-------------------------------------------------
    // parallel recursive using tasks

    if (type == 1 || type == 0)
    {
	start = omp_get_wtime();
#pragma omp parallel //default(none) shared(v, n_vals)
        {
#pragma omp single nowait
            omp::qs_rec_tasks(v, 100, 0, n_vals - 1);
        }
        end = omp_get_wtime();

        printf("%ld, %d, %d %f\n", n_vals, omp_get_max_threads(), utils::is_sorted(v, n_vals), end - start);
    }
    //-------------------------------------------------
    // parallel recursive using sections

    copy(orig, orig + n_vals, v);

    if (type == 2 || type == 0)
    {
        start = omp_get_wtime();
        omp::qs_rec_section(v, 100, 0, n_vals - 1);
        end = omp_get_wtime();

        printf("%ld, %d, %d %f\n", n_vals, omp_get_max_threads(), utils::is_sorted(v, n_vals), end - start);
    }
    //-------------------------------------------------
    // sequential recursive

    copy(orig, orig + n_vals, v);

    if (type == 3 || type == 0)
    {
        start = omp_get_wtime();
        seq::qs(v, 7, 0, n_vals - 1);
        end = omp_get_wtime();

        printf("%ld, %d, %d %f\n", n_vals, omp_get_max_threads(), utils::is_sorted(v, n_vals), end - start);
    }

    delete[] v;
    delete[] orig;
    return 0;
}
