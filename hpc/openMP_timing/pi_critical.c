#include <omp.h>
#include <time.h>
#include <stdlib.h>
#include <math.h>
#include <stdio.h>

using namespace std;

int main (int argc, char* argv[])
{
	double niter= 10000000;

	int iteration = atoi(argv[1]);
	int numcores = atoi(argv[2]);

	if(argc>3) 
		niter = atof(argv[3]);

	double x, y, z;
	int i;
	int numthreads=0;
	int count=0;

	double start = omp_get_wtime();

	#pragma omp parallel private(x, y, z, i) shared(count,numthreads)
	{
		srandom((int)time(NULL) ^ omp_get_thread_num());
		for(i=0; i<niter; ++i)
		{
			x = (double)random()/RAND_MAX;
			y = (double)random()/RAND_MAX;
			z = sqrt((x*x)+(y*y));
			if (z<=1)
			{
				#pragma omp critical
				++count;
			}

			#pragma omp master
			numthreads = omp_get_num_threads();
		}
	}

	double end = omp_get_wtime();
	
	double pi = ((double)count/(double)(niter*numthreads))*4.0;
    printf("%d,%.0f,%d,%d,%f,%f\n", iteration, niter, numcores, numthreads, end-start, pi);
}
