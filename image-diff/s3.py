import boto3


class MyS3:
    def __init__(self, bucket_name):
        self._s3 = boto3.resource('s3')
        self.bucket_name = bucket_name
        self._bucket = self._s3.Bucket(bucket_name)

    def upload_file(self, local_path, s3_path):
        return self._bucket.upload_file(local_path, s3_path)


if __name__ == "__main__":
    raise NotImplementedError
