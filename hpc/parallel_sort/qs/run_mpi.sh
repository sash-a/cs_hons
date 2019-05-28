#!/bin/sh
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks=4
#SBATCH --job-name="mpi qs timing"
#SBATCH --time=50:00
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=NONE
module load mpi/openmpi-4.0.1

cd /home/hpc24/cs_hons/hpc/parallel_sort

make mpi

for i in {1..20}; do
	mpirun -n 4 --quiet ./mpi 1000000
done

for i in {1..20}; do
        mpirun -n 4 --quiet ./mpi 100000
done

for i in {1..20}; do
        mpirun -n 4 --quiet ./mpi 10000
done

for i in {1..20}; do
        mpirun -n 4 --quiet ./mpi 1000
done

