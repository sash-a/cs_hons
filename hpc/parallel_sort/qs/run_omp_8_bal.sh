#!/bin/bash
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks=8
#SBATCH --job-name="qs timing"
#SBATCH --time=50:00
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=NONE

# cd /home/hpc24/cs_hons/hpc/parallel_sort
cd ..
export OMP_NUM_THREADS=8

make omp_qs

# 1 mil - 10 mil
for i in {1..10}; do
	for j in {1..20}; do
		nv=$(($i * 1000000))
		./build/omp_qs $nv 4
	done;
done

