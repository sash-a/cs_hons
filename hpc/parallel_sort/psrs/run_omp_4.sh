#!/bin/bash
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks=4
#SBATCH --job-name="qs timing"
#SBATCH --time=50:00
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=NONE

# cd /home/hpc24/cs_hons/hpc/parallel_sort
cd ..
export OMP_NUM_THREADS=4

make omp_psrs

#parallel
echo 'parallel tasks 4 cores' 
# 1 mil - 10 mil
for i in {1..10}; do
	for j in {1..20}; do
		nv=$(($i * 1000000))
		./build/omp_psrs $nv
	done;
done
# 1k to 1mil
for i in {0..5}; do
	for j in {1..20}; do
		nv=$((1000 * 10 ** $i))
		./build/omp_psrs $nv
	done;
done

