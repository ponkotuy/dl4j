#!/usr/bin/env python
# -*- coding:utf-8 -*-

import cv2
import numpy


class InvalidImageError(Exception):
    """Raised invalid color image"""


class ColorImage:
    def __init__(self, b, g, r):
        self.b = b
        self.g = g
        self.r = r

    def bgr(self):
        return self.b, self.g, self.r


class SplitedImage(ColorImage):
    def __init__(self, fname):
        img = cv2.imread(fname)
        if img is None:
            raise InvalidImageError("Faild imread image(not image?): %s" % fname)
        if img.shape[2] != 3:
            raise InvalidImageError("Required colorful of image: %s" % fname)
        self.fname = fname
        super().__init__(*cv2.split(img))


class ColorHuMoment(ColorImage):
    def __init__(self, splited):
        self.fname = splited.fname
        super().__init__(*(self._calcHuMoment(c) for c in splited.bgr()))
        self.ary = sum((c.tolist() for c in self.bgr()), [])

    def __str__(self):
        return str(
            {"fname": self.fname,
             "elems": {"r": str(self.r.ravel()), "g": str(self.g.ravel()), "b": str(self.b.ravel())}})

    def diff(self, hu):
        return sum(numpy.linalg.norm(x - y) for x, y in zip(self.bgr(), hu.bgr()))

    @staticmethod
    def _calcHuMoment(mono):
        m = cv2.moments(mono)
        return cv2.HuMoments(m)


if __name__ == "__main__":
    raise NotImplementedError
