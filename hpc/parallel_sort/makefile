main: main.cpp seq_sort.h omp_sort.h
	g++ -o main -fopenmp main.cpp

mpi: mpi_sort.c
	mpicc -o mpi mpi_sort.c

run_mpi_4: mpi
	mpiexec -n 4 mpi

run_mpi_8: mpi
	mpiexec -n 8 mpi

clean:
	rm -rf main