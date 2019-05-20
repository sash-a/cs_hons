cmake_minimum_required(VERSION 3.14)
project(parallel_sort)

#set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fopenmp")

add_executable(parallel_sort omp_sort.cpp seq_sort.cpp mpi_sort.cpp)