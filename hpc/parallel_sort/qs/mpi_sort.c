#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <inttypes.h>
#include <limits.h>
#include <mpi.h>

int *read_values(int *arr, long size)
{
    arr = malloc(size * sizeof(int));
    for (int i = 0; i < size; ++i)
        arr[i] = rand();
    return arr;
}

void print_arr(int *arr, long size)
{
    for (int i = 0; i < size; ++i)
    {
        printf("%d, ", arr[i]);
    }
    printf("\n");
}

int is_sorted(int *arr, long size)
{
    for (int i = 1; i < size; ++i)
    {
        if (arr[i - 1] > arr[i]) return 0;
    }
    return 1;
}

int *merge_all(int *arr, int *n_arr, long local_len, int n_procs)
{
    int arr_counter[n_procs];
    for (int i = 0; i < n_procs; ++i) arr_counter[i] = 0;

    for (int i = 0; i < local_len * n_procs; ++i)
    {
        int mn = INT_MAX;
        int mn_proc = -1;

        for (int j = 0; j < n_procs; ++j)
        {
            if (arr_counter[j] < local_len && arr[arr_counter[j] + (local_len * j)] < mn)
            {
                mn = arr[arr_counter[j] + (local_len * j)];
                mn_proc = j;
            }
        }

        n_arr[i] = mn;
        ++arr_counter[mn_proc];
    }

    return n_arr;
}

int *merge_arr(int *new_arr, int *arr_a, int *arr_b, long alen, long blen)
{
    new_arr = malloc((alen + blen) * sizeof(int));
    int acount = 0;
    int bcount = 0;

    for (int i = 0; i < (alen + blen); ++i)
    {
        if (bcount >= blen || (acount < alen && arr_a[acount] < arr_b[bcount]))
        {
            new_arr[i] = arr_a[acount];
            ++acount;
        } else
        {
            new_arr[i] = arr_b[bcount];
            ++bcount;
        }
    }
    return new_arr;
}

void swap(int arr[], int a, int b)
{
    int t = arr[a];
    arr[a] = arr[b];
    arr[b] = t;
}

int partition(int v[], int l, long h)
{

    int lt_pos = l; // position one past the last element smaller than pivot

    // putting the pivot in the last position
    swap(v, l, h);
    int pivot = v[h];

    for (int i = l; i < h; ++i)
    {
        if (v[i] < pivot)
        {
            if (i > lt_pos)
                swap(v, i, lt_pos); // swap vs iter_swap?

            ++lt_pos;
        }
    }
    swap(v, h, lt_pos); // final swap to put pivot in correct position
    return lt_pos;
}

void insertionsort(int v[], int l, int h)
{
    for (int i = l + 1; i <= h; ++i)
    {
        int j = i;
        while (j > 0 && v[j - 1] > v[j])
        {
            swap(v, j - 1, j);
            --j;
        }
    }
}

void qs(int v[], int thresh, int l, long h)
{
    if (h - l < thresh)
        insertionsort(v, l, h);
    else
    {
        int pivot = partition(v, l, h);
        qs(v, thresh, l, pivot - 1);
        qs(v, thresh, pivot + 1, h);
    }
}

int main(int argc, char *argv[])
{
    int rank, num_procs;
    long n_vals = 1000; // default

    int *global_arr;
    int *local_arr;

    long local_len;

    srand(100);

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &num_procs);

    if (rank == 0) // is master thread
    {
        if (argc == 2)
            n_vals = atoi(argv[1]);

        global_arr = read_values(global_arr, n_vals);
        if (n_vals % num_procs != 0)
        {
            printf("Invalid array size!");
            return 0;
        }
        local_len = n_vals / num_procs;
    }

    double start = MPI_Wtime();

    MPI_Bcast(&local_len, 1, MPI_INT, 0, MPI_COMM_WORLD);
    local_arr = malloc(local_len * sizeof(int));
    MPI_Scatter(global_arr, local_len, MPI_INT, local_arr, local_len, MPI_INT, 0, MPI_COMM_WORLD);

    //qs
    qs(local_arr, 7, 0, local_len - 1);

    //MPI_Gather(local_arr, local_len, MPI_INT, global_arr, local_len, MPI_INT, 0, MPI_COMM_WORLD);
    if (rank != 0)
    {
        MPI_Send(local_arr, local_len, MPI_INT, 0, 0, MPI_COMM_WORLD);
    }

    if (rank == 0)
    {
        int n_arr[local_len * num_procs];

        global_arr = local_arr;
        for (int i = 1; i < num_procs; ++i)
        {
            int recvd[local_len];
            MPI_Status status;
            MPI_Recv(recvd, local_len, MPI_INT, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            global_arr = merge_arr(n_arr, recvd, global_arr, local_len, local_len * i);
        }
        // merge_all(n_arr, global_arr, local_len, num_procs);
        // qs(global_arr, 7, 0, n_vals - 1);
        double end = MPI_Wtime();

        printf("%ld, %d, %d %f\n", n_vals, num_procs, is_sorted(global_arr, n_vals), end - start);
//        free(global_arr);

    }
    free(local_arr);

    MPI_Finalize();
    return 0;
}
