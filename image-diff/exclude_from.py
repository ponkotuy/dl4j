#!/usr/bin/env python
# -*- coding:utf-8 -*-
from itertools import islice

from os import path

import numpy

import os

from config import MyConfig
from image_helper import SplitedImage, ColorHuMoment


def files(directory):
    for p, _, fs in os.walk(directory):
        for filePath in map(lambda x: os.path.join(p, x), fs):
            yield filePath


if __name__ == "__main__":
    config = MyConfig('application.conf')
    dl4jDir = path.join(config.path.dl4jDir, 'image/ero/1009125ec32910e0d')
    images = (SplitedImage(fname) for fname in islice(files(dl4jDir), 5))
    huMoments = [ColorHuMoment(img) for img in images]
    variances = [numpy.var(numpy.array([hu.ary[i] for hu in huMoments])) for i in range(21)]
    maxIndex = variances.index(max(variances))
