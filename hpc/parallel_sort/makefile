main: main.cpp seq_sort.h omp_sort.h utils.h
	g++ -o main -fopenmp main.cpp --std=c++11

mpi: mpi_sort.c
	mpicc -o mpi mpi_sort.c --std=c11

run_mpi_4: mpi
	mpirun -n 4 mpi

run_mpi_8: mpi
	mpiexec -n 8 mpi

clean:
	rm -rf main
