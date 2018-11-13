#!/usr/bin/env python
# -*- coding:utf-8 -*-

import cv2

class SplitedImage:
    def __init__(self, img):
        self.image = img
        self.rgb = cv2.split(img)
        self.r = self.rgb[0]
        self.g = self.rgb[1]
        self.b = self.rgb[2]

class ColorHuMoment:
    def __init__(self, splited):
        self.splited = splited
        self.r = self._calcHuMoment(splited.r)
        self.g = self._calcHuMoment(splited.g)
        self.b = self._calcHuMoment(splited.b)

    def _calcHuMoment(self, mono):
        m = cv2.moments(mono)
        return cv2.HuMoments(m)

    def __str__(self):
        return repr({"r": str(self.r.ravel()), "g": str(self.g.ravel()), "b": str(self.b.ravel())})

if __name__ == "__main__":
    raise NotImplementedError
