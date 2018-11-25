import plyvel
import struct
from typing import Union

FmtType = Union[bytes, str]
DEFAULT_ENCODE = 'utf-8'


class LevelDB:
    def __init__(self, fpath, value_fmt: FmtType):
        self._db = plyvel.DB(fpath, create_if_missing=True)
        self.value_fmt = value_fmt

    def put(self, k: str, v):
        self._db.put(bs(k), struct.pack(self.value_fmt, v))

    def get(self, k: str):
        data = self._db.get(bs(k))
        return decode_value(data, self.value_fmt)

    def delete(self, k: str):
        self._db.delete(bs(k))

    def close(self):
        self._db.close()

    def __iter__(self):
        return ((k.decode(DEFAULT_ENCODE), decode_value(v, self.value_fmt)) for k, v in self._db)


def decode_value(value, fmt: FmtType):
    return value and struct.unpack(fmt, value)[0]


# TODO cache
def bs(string: str):
    return string.encode(DEFAULT_ENCODE)
