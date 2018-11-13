#!/usr/bin/env python
# -*- coding:utf-8 -*-

from pprint import pprint
import sys
import cv2
from image_helper import SplitedImage, ColorHuMoment

if __name__ == "__main__":
    args = sys.argv
    print(args[1:])
    images = (cv2.imread(fname) for fname in args[1:])
    for img in images:
        hu = ColorHuMoment(SplitedImage(img))
        print(hu)
