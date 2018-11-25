from os import path

from pyhocon import ConfigFactory
from s3 import MyS3


class MyConfig:
    def __init__(self):
        self.conf = ConfigFactory.parse_file('application.conf')
        self.path = Path(self.conf['path'])
        self.aws = Aws(self.conf['aws'])

    def __str__(self):
        return str({'path': str(self.path)})


class Path:
    def __init__(self, conf):
        self.conf = conf
        self.dl4j_dir = conf.get('dl4j_dir')
        self.images_dir = path.join(self.dl4j_dir, conf.get('images_dir'))
        self.exclude_file = conf.get('exclude_file')
        self.model_file = path.join(self.dl4j_dir, self.exclude_file)
        self.end_nearly_dirs = conf.get('end_nearly_dirs')
        self.end_nearly_dirs_file = path.join(self.dl4j_dir, conf.get('end_nearly_dirs'))
        self.original_images_dir = conf.get('original_images_dir')

    def __str__(self):
        return str({'dl4jDir': self.dl4j_dir, 'imagesDir': self.images_dir, 'originalImagesDir': self.original_images_dir})


class Aws:
    def __init__(self, conf):
        self.conf = conf
        self.s3 = S3Config(conf.get('s3'))


class S3Config:
    def __init__(self, conf):
        self.conf = conf
        self.bucket = conf.get("bucket")

    def client(self):
        return MyS3(self.bucket)
