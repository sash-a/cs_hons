//
// Created by sasha on 2019/05/20.
//

#ifndef PARALLEL_SORT_UTILS_H
#define PARALLEL_SORT_UTILS_H

#include <iostream>
#include <vector>


namespace utils
{
    void print_vec(const std::vector<int> &v)
    {
        for (int i : v)
            std::cout << i << std::endl;
    }

    std::vector<int> random_vec(const int &size)
    {
        std::vector<int> v;
        for (int i = 0; i < size; ++i)
            v.push_back(int(rand() % 100));

        return v;
    }

    bool is_sorted(const std::vector<int> &v)
    {
        for (int j = 1; j < v.size(); ++j)
        {
            if (v[j - 1] > v[j])
                return false;
        }

        return true;
    }

}

#endif //PARALLEL_SORT_UTILS_H
