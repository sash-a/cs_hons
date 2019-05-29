#!/bin/bash
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=2 --ntasks=8
#SBATCH --job-name="qs timing"
#SBATCH --time=50:00
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=NONE
module load mpi/openmpi-4.0.1

# cd /home/hpc24/cs_hons/hpc/parallel_sort

cd ..

make mpi_qs

#parallel
# 1 mil - 10 mil
echo '2 cores 2 nodes'
for i in {1..10}; do
	for j in {1..20}; do
		nv=$(($i * 1000000))
		mpirun -n 8 --quiet build/mpi_qs $nv
	done;
done

# 1k to 1mil
for i in {0..5}; do
	for j in {1..20}; do
		nv=$((1000 * 10 ** $i))
		mpirun -n 8 --quiet build/mpi_qs $nv
	done;
done

