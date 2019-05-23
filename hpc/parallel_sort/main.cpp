// Created by sasha on 2019/05/23.

#include "seq_sort.h"
#include "omp_sort.h"

using namespace std;

int main(int argc, char *argv[])
{
    srand(100);
    int n_vals = 1000;
    int v[n_vals];
    utils::rand_arr(v, n_vals);

    printf("initial:\n");
    utils::print_vec(v, n_vals);
    printf("\n");

    double start = omp_get_wtime();
    #pragma omp parallel default(none) shared(v, n_vals)
    {
        #pragma omp single nowait
        omp::qs_rec(v, 7, 0, n_vals-1);
    }
//    seq::qs(v, 7, 0, n_vals-1);
    double end = omp_get_wtime();

    printf("\ntime taken %f\n", end - start);
    printf("is sorted: %d\n\n", utils::is_sorted(v,n_vals));
    printf("new arr: \n");
    utils::print_vec(v, n_vals);

    return 0;
}