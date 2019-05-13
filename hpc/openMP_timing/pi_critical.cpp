#include <iostream>
#include <cmath>
#include <omp.h>

using namespace std;

int main (int argc, char* argv[])
{
	double niter= 10000000;
	if(argc>1) 
		niter = atof(argv[1]);

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
	double time = end-start;
	
	double pi = ((double)count/(double)(niter*numthreads))*4.0;
	printf("%f, %d, %f, %f\n", pi, numthreads, time, niter);
}
