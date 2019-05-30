# HPC sorting comparison

## How to run

### Complilation:

```make <parallel_framework>_<algorithm>```

Where parallel framework is either omp or mpi and parallel framework is either qs or psrs. To make OpenMP quicksort one would use: ```make omp_qs```

### Running:

#### OMP

```export OMP_SET_NUM_THREADS=<desired thread count>```

```./build/omp_<algorithm> <array size>```

Specifically for ```omp_qs``` there is a second optional command line arguement so it would be compiled as: ```./build/omp_<algorithm> <array size> <type>```. 

##### Type options:
0: run all

1: run omp tasks naive

2: run omp sections (this does not work for large array sizes)

3: run sequential

4: run omp tasks balanced

#### MPI

```mpirun -n  <desired thread count>  /build/mpi_<algorithm> <array size>```
