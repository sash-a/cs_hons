all: omp seq mpi

omp: omp_sort.cpp utils.h
	g++ -o omp_sort -fopenmp omp_sort.cpp utils.h -std=c++11

seq: seq_sort.cpp utils.h
	g++ -o seq_sort -fopenmp seq_sort.cpp utils.h -std=c++11

mpi: mpi_sort.c utils.h
	g++ -o mpi_sort -fopenmp mpi_sort.c utils.h -std=c++11

clean:
	rm -rf seq_sort opm_sort mpi_sort