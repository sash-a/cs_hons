#!/bin/bash
#SBATCH --account=icts
#SBATCH --partition=curie
#SBATCH --nodes=1 --ntasks-per-node=1
#SBATCH --cpus-per-task=3 --ntasks-per-core=1
#SBATCH --time=60:00
#SBATCH --mem-per-cpu=4000
#SBATCH --job-name="OMPCriticalPerformance"
#SBATCH --mail-user=ABRSAS002@myuct.ac.za
#SBATCH --mail-type=BEGIN,END,FAIL

export OMP_NUM_THREADS=3

cd /home/hpc24/openMP_timing
for i in {1..20}; do
    srun pi_critical $i 3
done

