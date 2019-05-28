cmake_minimum_required(VERSION 3.6)
project(parallel_sort)

#set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fopenmp")

add_executable(parallel_sort mpi_sort.c utils.h seq_sort.h main.cpp omp_sort.h psrs/mpi_psrs.c)