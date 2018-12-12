#!/usr/bin/env python
from os import path

import os

from multiprocessing import Pool
import multiprocessing as multi
from send2trash import send2trash

from config import MyConfig
from common import calc_hu_moments, CalcDiff
from leveldb import LevelDB

PARALLEL_COUNT_MAX = 2
DIFF_THRESHOLD = 5.0e-7
DOUBLE_FMT = 'd'

if __name__ == "__main__":
    parallel = Pool(min(PARALLEL_COUNT_MAX, multi.cpu_count()))
    config = MyConfig()
    images_dir = config.path.original_images_dir
    db = LevelDB(config.path.end_nearly_dirs_file, DOUBLE_FMT)
    for p, _, fs in os.walk(images_dir):
        if fs and DIFF_THRESHOLD < (db.get(p) or 1.0):
            print(p)
            (hu_moments, _) = calc_hu_moments([path.join(p, f) for f in fs], parallel)
            filtered_diff = parallel.map(CalcDiff(hu_moments, DIFF_THRESHOLD), enumerate(hu_moments))
            failed = [hu.fname for hu, b in zip(hu_moments, filtered_diff) if b]
            for fname in failed:
                print("  Trash: %s" % fname)
                send2trash(fname)
            db.put(p, DIFF_THRESHOLD)
