#!/bin/sh
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks=4
#SBATCH --job-name="qs timing"
#SBATCH --time=50:00
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=NONE

cd /home/hpc24/cs_hons/hpc/parallel_sort

export OMP_NUM_THREADS=8

make omp_qs
for i in {1..5}; do
    ./build/omp_qs 10000000 1
done

for i in {1..5}; do
    ./build/omp_qs 10000000 2
done

for i in {1..5}; do
    ./build/omp_qs 10000000 3
done