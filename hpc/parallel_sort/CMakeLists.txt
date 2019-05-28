cmake_minimum_required(VERSION 3.6)
project(parallel_sort)

#set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fopenmp")

add_executable(parallel_sort qs/mpi_sort.c qs/utils.h qs/seq_sort.h qs/main.cpp qs/omp_sort.h psrs/mpi_psrs.c psrs/omp_psrs.c)