// Created by sasha on 2019/05/19.

#include <iostream>
#include <vector>
#include <algorithm>
#include <tuple>
#include <omp.h>

#define pair pair<int, int>

using namespace std;


int cores[8] = {0, 0, 0, 0, 0, 0, 0, 0};

void print_vec(const vector<int> &v)
{
    for (int i : v)
        cout << i << endl;
}

vector<int> random_vec(const int &size)
{
    vector<int> v;
    for (int i = 0; i < size; ++i)
        v.push_back(int(rand() % 100));

    return v;
}

bool is_sorted(const vector<int> &v)
{
    for (int j = 1; j < v.size(); ++j)
    {
        if (v[j - 1] > v[j])
            return false;
    }

    return true;
}

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

    int pivot = partition(v, l, h);
#pragma omp parallel
    {
#pragma omp single nowait
        {
            cores[omp_get_thread_num()]++;
            if (pivot - l > thresh)
                quicksort(v, thresh, l, pivot - 1);
            else
                insertionsort(v, l, pivot);
        }

#pragma omp single nowait
        {
            cores[omp_get_thread_num()]++;
            if (h - pivot > thresh)
                quicksort(v, thresh, pivot + 1, h);
            else
                insertionsort(v, pivot + 1, h);
        }
    }
}

int main(int argc, char *argv[])
{
    srand(100);
    int n_vals = 100000;

    vector<int> v = random_vec(n_vals);

    double start = omp_get_wtime();

    quicksort(v, 7, 0, n_vals);

    double end = omp_get_wtime();
    printf("time taken %f", end - start);
    cout << is_sorted(v) << endl;

    for (int i : cores)
        cout << i << endl;
    return 0;
}