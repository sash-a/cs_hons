#include <iostream> 
#include <omp.h>
#include <cmath>

using namespace std;

int main(int argc, char* argv[])
{
	double niter= 10000000;
	if(argc>1) 
		niter = atof(argv[1]);
	double x, y, z;
	int i;
	int numthreads=0;
	int count=0;

	#pragma ompparallel private(x, y, z, i) shared(count, numthreads){
		srandom((int)time(NULL) ^ omp_get_thread_num());//Give random() a seed value
		for(i=0; i<niter; ++i)//main loop
		{
			x = (double)random()/RAND_MAX;//gets a random x coordinate 
			y = (double)random()/RAND_MAX;//gets a random y coordinate
			z = sqrt((x*x)+(y*y));//number inside unit circle
			if(z<=1)
			{
				#pragma omp critical
				{++count;}//a valid random point
			}
		}
		#pragma ompmaster numthreads=omp_get_num_threads();
	}

	double pi = ((double)count/(double)(niter*numthreads))*4.0;
	printf("Pi: %f (#threads=%d)\n", pi,numthreads);
