#!/bin/sh
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks=4
#SBATCH --job-name="mpi qs timing"
#SBATCH --time=50:00
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=NONE
module load mpi/openmpi-4.0.1

cd /home/hpc24/cs_hons/hpc/parallel_sort/psrs

mpic++ -Wall -o psrs_mpi psrs_mpi_test.cpp --std=c++11

mpirun -n 4 psrs_mpi -DR 1000 -SR 100
