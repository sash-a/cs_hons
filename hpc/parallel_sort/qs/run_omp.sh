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

for i in {1..20}; do
    ./main 10000000
done
