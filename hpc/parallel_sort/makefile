main: main.cpp seq_sort.h omp_sort.h utils.h
	g++ -o main -fopenmp main.cpp --std=c++11

mpi: mpi_sort.c
	mpicc -o mpi mpi_sort.c --std=c11

psrs_mpi: psrs_mpi_test.cpp
	mpic++ -o psrs_mpi psrs_mpi_test.cpp --std=c++11

clean:
	rm -rf main
