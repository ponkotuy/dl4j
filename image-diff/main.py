#!/usr/bin/env python
# -*- coding:utf-8 -*-

import sys

from os import path

from image_helper import SplitedImage, ColorHuMoment

if __name__ == "__main__":
    args = sys.argv
    print(args[1:])
    images = (SplitedImage(fname) for fname in args[1:])
    huMoments = [ColorHuMoment(img) for img in images]
    for x, y in zip(huMoments, huMoments[1:]):
        print(path.basename(x.fname), path.basename(y.fname), x.diff(y))
