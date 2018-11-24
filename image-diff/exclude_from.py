#!/usr/bin/env python
# -*- coding:utf-8 -*-

import os
from os import path

from config import MyConfig
from image_helper import SplitedImage, ColorHuMoment, InvalidImageError
from multiprocessing import Pool
import multiprocessing as multi


def files(directory):
    for p, ps, fs in os.walk(directory):
        for f in fs:
            yield path.join(p, f)


def load_image(fname):
    try:
        image = SplitedImage(fname)
    except InvalidImageError:
        return fname  # 失敗はファイル名
    else:
        return ColorHuMoment(image)


def output(local_path, s3_path, file_names, bucket):
    with open(local_path, 'w') as f:
        f.writelines(name + '\n' for name in file_names)
    bucket.upload_file(local_path, s3_path)


class CalcDiff:
    def __init__(self, hu_moments):
        self.hu_moments = hu_moments

    def __call__(self, xs):
        (idx, hu) = xs
        end = min(idx + CHECK_RANGE, len(self.hu_moments))
        return any(hu.diff(that) <= DIFF_THRESHOLD for that in self.hu_moments[(idx + 1):end])


CHECK_RANGE = 9
DIFF_THRESHOLD = 1.0e-7

if __name__ == "__main__":
    parallel = Pool(min(4, multi.cpu_count()))
    config = MyConfig()
    dl4j_dir = path.join(config.path.images_dir, '')
    results = parallel.map(load_image, list(files(dl4j_dir)))
    hu_moments = [x for x in results if isinstance(x, ColorHuMoment)]
    failed = [x for x in results if isinstance(x, str)]
    enabled = parallel.map(CalcDiff(hu_moments), enumerate(hu_moments))
    failed += [hu.fname for hu, b in zip(hu_moments, enabled) if b]
    erase_path = config.path.images_dir + '/'
    fnames = [name.replace(erase_path, '') for name in failed]
    output(config.path.model_file, config.path.exclude_file, fnames, config.aws.s3.client())
    print(fnames)
