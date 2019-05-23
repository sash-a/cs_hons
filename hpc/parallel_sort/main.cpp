// Created by sasha on 2019/05/23.

#include "seq_sort.h"
#include "omp_sort.h"

using namespace std;

int main(int argc, char *argv[])
{
    srand(100);
    int n_vals = 100000;

    vector<int> v = utils::random_vec(n_vals);

    double start = omp_get_wtime();
    omp::quicksort(v, 7, 0, n_vals);
    double end = omp_get_wtime();

    printf("time taken %f\n", end - start);
    cout << "is sorted " << utils::is_sorted(v) << endl;

    for (int i : omp::cores)
        cout << i << endl;
    return 0;
}