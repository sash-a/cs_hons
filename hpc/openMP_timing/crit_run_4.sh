#!/bin/bash
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks=4
#SBATCH --time=120:00
#SBATCH --mem-per-cpu=4000
#SBATCH --job-name="OMPCriticalPerformance"
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=BEGIN,END,FAIL

export OMP_NUM_THREADS=4

cd /home/hpc24/hpchons_OMP_timing
for i in {1..50}; do
    srun -n1 pi_critical $i 4
done

export OMP_NUM_THREADS=16

for i in {1..50}; do
    srun -n1 pi_critical $i 4
done
echo "Done"
