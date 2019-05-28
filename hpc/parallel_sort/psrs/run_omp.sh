#!/bin/sh
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks=4
#SBATCH --job-name="qs timing"
#SBATCH --time=50:00
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=NONE

cd /home/hpc24/cs_hons/hpc/parallel_sort/psrs

export OMP_NUM_THREADS=4

gcc -fopenmp -o omp_psrs omp_psrs.c -lm -std=c11
for i in {1..20}; do
    ./omp_psrs 1000000
done

