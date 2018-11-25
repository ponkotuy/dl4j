from image_helper import SplitedImage, InvalidImageError, ColorHuMoment


def load_image(fname):
    try:
        image = SplitedImage(fname)
    except InvalidImageError:
        return fname  # 失敗はファイル名
    else:
        return ColorHuMoment(image)


def calc_hu_moments(files: [], parallel=None):
    results = parallel.map(load_image, files)
    hu_moments = [x for x in results if isinstance(x, ColorHuMoment)]
    failed = [x for x in results if isinstance(x, str)]
    return hu_moments, failed


class CalcDiff:
    CHECK_RANGE = 9

    def __init__(self, hu_moments, threshold):
        self.hu_moments = hu_moments
        self.threshold = threshold

    def __call__(self, xs):
        (idx, hu) = xs
        end = min(idx + CalcDiff.CHECK_RANGE, len(self.hu_moments))
        return any(hu.diff(that) <= self.threshold for that in self.hu_moments[(idx + 1):end])
