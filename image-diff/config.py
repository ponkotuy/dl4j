from pyhocon import ConfigFactory


class MyConfig:
    def __init__(self, fname):
        self.conf = ConfigFactory.parse_file('application.conf')
        self.path = Path(self.conf['path'])

    def __str__(self):
        return str({'path': str(self.path)})


class Path:
    def __init__(self, conf):
        self.conf = conf
        self.dl4jDir = conf.get('dl4j_dir')
        self.imagesDir = conf.get('images_dir')
        self.originalImagesDir = conf.get('original_images_dir')

    def __str__(self):
        return str({'dl4jDir': self.dl4jDir, 'imagesDir': self.imagesDir, 'originalImagesDir': self.originalImagesDir})
