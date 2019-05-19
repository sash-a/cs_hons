cmake_minimum_required(VERSION 3.14)
project(parallel_sort)

set(CMAKE_CXX_STANDARD 14)

add_executable(parallel_sort omp_sort.cpp omp_sort.h seq_sort.cpp mpi_sort.cpp mpi_sort.h)