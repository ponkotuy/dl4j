#!/usr/bin/env python
# -*- coding:utf-8 -*-
import os

import multiprocessing as multi
from multiprocessing import Pool
from os import path

from common import calc_hu_moments, CalcDiff
from config import MyConfig


def files(directory):
    for p, ps, fs in os.walk(directory):
        for f in fs:
            yield path.join(p, f)


def output(local_path, s3_path, file_names, bucket):
    with open(local_path, 'w') as f:
        f.writelines(name + '\n' for name in file_names)
    bucket.upload_file(local_path, s3_path)


DIFF_THRESHOLD = 5.0e-6
PARALLEL_COUNT_MAX = 6

if __name__ == "__main__":
    parallel = Pool(min(PARALLEL_COUNT_MAX, multi.cpu_count()))
    config = MyConfig()
    dl4j_dir = path.join(config.path.images_dir, '')
    (hu_moments, failed) = calc_hu_moments(list(files(dl4j_dir)), parallel)
    enabled = parallel.map(CalcDiff(hu_moments, DIFF_THRESHOLD), enumerate(hu_moments))
    failed += [hu.fname for hu, b in zip(hu_moments, enabled) if b]
    erase_path = config.path.images_dir + '/'
    fnames = [name.replace(erase_path, '') for name in failed]
    output(config.path.model_file, config.path.exclude_file, fnames, config.aws.s3.client())
    print(fnames)
