import os
from Result import Result
from matplotlib import pyplot as plt


def get_data(path):
    data = {}

    for root, dirs, files in os.walk(path):
        print(root, dirs, files)
        for file in files:
            with open(os.path.join(root, file), 'r+') as f:
                data[file[:-4]] = [Result(line[:-1]) for line in f.readlines() if '--std' not in line and ',' in line]

    return data


def avg(results):
    final = {}
    count = {}

    for res in results:
        if res.nv in final:
            final[res.nv] += res.time
            count[res.nv] += 1
        else:
            final[res.nv] = res.time
            count[res.nv] = 1

    for k in final.keys():
        final[k] = final[k] / count[k]

    return final

def speedup(seq, para):
    d = {}
    for k in para.keys():
        d[k] = seq[k]/para[k]

    return d

data = get_data('../RawData')
seq = avg(data['seq'])

# mpi_psrs_4 = speedup(seq, avg(data['mpi_psrs_4ct']))
# mpi_qs_4 = speedup(seq, avg(data['mpi_qs_4ct']))
# omp_psrs_4 = speedup(seq, avg(data['omp_psrs_4ct']))
# omp_qs_4 = speedup(seq, avg(data['omp_qs_4ct']))
# omp_qs_4_bal = speedup(seq, avg(data['omp_qs_4ct_bal']))


# plt.title('Comparison of all 4 threaded sorts')
# plt.scatter(list(mpi_psrs_4.keys())[:-1], list(mpi_psrs_4.values())[:-1], alpha=0.5, label='MPI PSRS 4 threads', marker='^-')
# plt.scatter(list(mpi_qs_4.keys())[:-1], list(mpi_qs_4.values())[:-1], alpha=0.5, label='MPI QS 4 threads')
# plt.scatter(list(omp_psrs_4.keys())[:-1], list(omp_psrs_4.values())[:-1], alpha=0.5, label='OMP PSRS 4 threads')
# plt.scatter(list(omp_qs_4.keys())[:-1], list(omp_qs_4.values())[:-1], alpha=0.5, label='OMP QS 4 threads')
# plt.scatter(list(omp_qs_4_bal.keys())[:-1], list(omp_qs_4_bal.values())[:-1], alpha=0.5,
#            label='OMP QS 4 threads balanced')

# omp_qs_8 = avg(data['omp_qs_8ct'])
# omp_qs_4 = avg(data['omp_qs_4ct'])
# omp_qs_2 = avg(data['omp_qs_2ct'])
# omp_qs_8_bal = avg(data['omp_qs_8ct_bal'])
# omp_qs_4_bal = avg(data['omp_qs_4ct_bal'])
# omp_qs_2_bal = avg(data['omp_qs_2ct_bal'])
#
# plt.title('Time taken vs data size for varying thread counts')
# plt.scatter(list(omp_qs_8.keys())[:-1], list(omp_qs_8.values())[:-1], alpha=0.5, label='OMP 8 threads naive')
# plt.scatter(list(omp_qs_4.keys())[:-1], list(omp_qs_4.values())[:-1], alpha=0.5, label='OMP 4 threads naive')
# plt.scatter(list(omp_qs_2.keys())[:-1], list(omp_qs_2.values())[:-1], alpha=0.5, label='OMP 2 threads naive')
# plt.scatter(list(omp_qs_8_bal.keys())[:-1], list(omp_qs_8_bal.values())[:-1], alpha=0.5, label='OMP 8 threads balanced')
# plt.scatter(list(omp_qs_4_bal.keys())[:-1], list(omp_qs_4_bal.values())[:-1], alpha=0.5, label='OMP 4 threads balanced')
# plt.scatter(list(omp_qs_2_bal.keys())[:-1], list(omp_qs_2_bal.values())[:-1], alpha=0.5, label='OMP 2 threads balanced')

# mpi_psrs_8 = avg(data['mpi_psrs_8ct'])[100000000]
# mpi_psrs_4 = avg(data['mpi_psrs_4ct'])[100000000]
# mpi_psrs_2 = avg(data['mpi_psrs_2ct'])[100000000]

# omp_psrs_8 = avg(data['omp_psrs_8ct'])[100000000]
# omp_psrs_4 = avg(data['omp_psrs_4ct'])[100000000]
# omp_psrs_2 = avg(data['omp_psrs_2ct'])[100000000]

# mpi_qs_8 = avg(data['mpi_qs_8ct'])[100000000]
# mpi_qs_4 = avg(data['mpi_qs_4ct'])[100000000]
# mpi_qs_2 = avg(data['mpi_qs_2ct'])[100000000]

# omp_qs_8 = avg(data['omp_qs_8ct'])[100000000]
# omp_qs_4 = avg(data['omp_qs_4ct'])[100000000]
# omp_qs_2 = avg(data['omp_qs_2ct'])[100000000]

# seq10m = seq[100000000]

# threads = [2, 4, 8]
# mpi_psrs = [seq10m / mpi_psrs_2, seq10m / mpi_psrs_4, seq10m / mpi_psrs_8]
# omp_psrs = [seq10m / omp_psrs_2, seq10m / omp_psrs_4, seq10m / omp_psrs_8]
# mpi_qs = [seq10m / mpi_qs_2, seq10m / mpi_qs_4, seq10m / mpi_qs_8]
# omp_qs = [seq10m / omp_qs_2, seq10m / omp_qs_4, seq10m / omp_qs_8]


# plt.title('Speedup vs core count for array size of 10 million')
# plt.scatter(threads, omp_psrs, alpha=0.5, label='OMP psrs speedup')
# plt.scatter(threads, mpi_psrs, alpha=0.5, label='MPI psrs speedup')
# plt.scatter(threads, mpi_qs, alpha=0.5, label='MPI qs speedup')
# plt.scatter(threads, omp_qs, alpha=0.5, label='OMP qs speedup')


# mpi_psrs_4_2 = speedup(seq, avg(data['mpi_psrs_4_2']))
# mpi_psrs_4 = speedup(seq, avg(data['mpi_psrs_4ct']))

# mpi_qs_4_2 = speedup(seq, avg(data['mpi_qs_4_2']))
# mpi_qs_4 = speedup(seq, avg(data['mpi_qs_4ct']))
# plt.title('Comparison of 4 threaded performance on 1 vs 2 nodes')
# plt.scatter(list(mpi_psrs_4_2.keys())[:-1], list(mpi_psrs_4_2.values())[:-1], alpha=0.5, label='psrs 2 nodes', marker=4)
# plt.scatter(list(mpi_psrs_4.keys())[:-1], list(mpi_psrs_4.values())[:-1], alpha=0.5, label='psrs 1 node', marker =4)
# plt.scatter(list(mpi_qs_4_2.keys())[:-1], list(mpi_qs_4_2.values())[:-1], alpha=0.5, label='qs 2 nodes')
# plt.scatter(list(mpi_qs_4.keys())[:-1], list(mpi_qs_4.values())[:-1], alpha=0.5, label='qs 1 node')

mpi_psrs_8 = speedup(seq, avg(data['mpi_psrs_8ct']))[10000000]
mpi_psrs_8_2 = speedup(seq, avg(data['mpi_psrs_8_2']))[10000000]
mpi_psrs_8_4 = speedup(seq, avg(data['mpi_psrs_8_4']))[10000000]

mpi_qs_8 = speedup(seq, avg(data['mpi_qs_8ct']))[10000000]
mpi_qs_8_2 = speedup(seq, avg(data['mpi_qs_8_2']))[10000000]
mpi_qs_8_4 = speedup(seq, avg(data['mpi_qs_8_4']))[10000000]

plt.plot([1,2,4], [mpi_psrs_8, mpi_psrs_8_2, mpi_psrs_8_4], 'o-', alpha=0.5, label='RSPS')
plt.plot([1,2,4], [mpi_qs_8, mpi_qs_8_2, mpi_qs_8_4], '^-', alpha=0.5, label='QS')
plt.title('Nodes vs speedup for array size of 10 million and 8 threads')

plt.xlabel('Nodes')
plt.ylabel('Speedup')

plt.legend()
plt.grid(True)
plt.show()
# plt.savefig('Speedup.png')
