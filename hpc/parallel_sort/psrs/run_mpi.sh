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
mpicc -o mpi_psrs mpi_psrs.c -std=c11

mpirun -n 4 --quiet mpi_psrs

