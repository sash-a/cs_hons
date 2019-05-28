omp_qs: qs/main.cpp qs/seq_sort.h qs/omp_sort.h qs/utils.h
	g++ -o build/omp_qs -fopenmp qs/main.cpp --std=c++11

mpi_qs: qs/mpi_sort.c
	mpicc -o build/mpi qs/mpi_sort.c --std=c11

mpi_psrs: psrs/mpi_psrs.c
	mpicc -o build/mpi_psrs psrs/mpi_psrs.c --std=c11

omp_psrs: psrs/omp_psrs.c
	gcc -o build/omp_psrs -fopenmp psrs/omp_psrs.c --std=c11 -lm

clean:
	rm -rf build/*
