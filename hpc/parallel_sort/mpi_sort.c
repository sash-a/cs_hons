#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <inttypes.h>
#include <mpi.h>

int *read_values(int* arr, int size)
{
    arr = malloc(size * sizeof(int));
    for (int i = 0; i < size; ++i)
        arr[i] = rand() % 10;
    return arr;
}

void print_arr(int* arr, int size)
{
    for (int i = 0; i < size; ++i)
    {
        printf("%d, ", arr[i]);
    }
}

void merge(int* a, int* b, int len_a, int len_b)
{}

int main(int argc, char *argv[])
{
    int rank, num_procs;
    int n_vals = 12;

    int *global_arr;
    int *local_arr;

    int local_len;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &num_procs);

    if (rank == 0) // is master thread
    {
        global_arr = read_values(global_arr, n_vals);

        if (n_vals % num_procs != 0)
        {
            printf("Invalid array size!");
            return 0;
        }
        local_len = n_vals / num_procs;
    }

    MPI_Bcast(&local_len, 1, MPI_INT, 0, MPI_COMM_WORLD);
    local_arr = malloc(local_len * sizeof(int));
    MPI_Scatter(global_arr, local_len, MPI_INT, local_arr, local_len, MPI_INT, 0, MPI_COMM_WORLD);

    printf("rank: %d\n", rank);
    for (int i = 0; i < local_len; ++i)
    {
        printf("%d, ", local_arr[i]);
    }

    MPI_Finalize();
    return 0;
}
