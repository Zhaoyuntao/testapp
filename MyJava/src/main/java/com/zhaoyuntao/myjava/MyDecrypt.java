package com.zhaoyuntao.myjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * created by zhaoyuntao
 * on 24/11/2021
 * description:
 */
class MyDecrypt {


//    int MAGIC_NO_COMPRESS_START = 0x03;
//    int MAGIC_NO_COMPRESS_START1 = 0x06;
//    int MAGIC_NO_COMPRESS_NO_CRYPT_START = 0x08;
//    int MAGIC_COMPRESS_START = 0x04;
//    int MAGIC_COMPRESS_START1 = 0x05;
//    int MAGIC_COMPRESS_START2 = 0x07;
//    int MAGIC_COMPRESS_NO_CRYPT_START = 0x09;
//
//    int MAGIC_SYNC_ZSTD_START = 0x0A;
//    int MAGIC_SYNC_NO_CRYPT_ZSTD_START = 0x0B;
//    int MAGIC_ASYNC_ZSTD_START = 0x0C;
//    int MAGIC_ASYNC_NO_CRYPT_ZSTD_START = 0x0D;
//
//    int MAGIC_END = 0x00;
//
//    int lastseq = 0;
//
//    String PRIV_KEY = "145aa7717bf9745b91e9569b80bbf1eedaa6cc6cd0e26317d810e35710f44cf8";
//    String PUB_KEY = "572d1e2710ae5fbca54c76a382fdd44050b3a675cb2bf39feebe85ef63d947aff0fa4943f1112e8b6af34bebebbaefa1a0aae055d9259b89a1858f7cc9af9df1";
//
//    public String tea_decipher(byte[] v, byte[] k) {
//        long op = 0xffffffffL;
//        long v0, v1 = struct.unpack('=LL', v[0:8]);
//        long k1, k2, k3, k4 = struct.unpack('=LLLL', k[0:16]);
//        long delta = 0x9E3779B9L;
//        long s = (delta << 4) & op;
//        for (int i = 0; i < 16; i++) {
//            v1 = (v1 - (((v0 << 4) + k3) ^ (v0 + s) ^ ((v0 >> 5) + k4))) & op;
//            v0 = (v0 - (((v1 << 4) + k1) ^ (v1 + s) ^ ((v1 >> 5) + k2))) & op;
//            s = (s - delta) & op;
//        }
//        return struct.pack('=LL', v0, v1);
//    }
//
//    public String tea_decrypt(byte[] v, byte[] k) {
//        int num = len(v) / 8 * 8;
//        String ret = "";
//        for (int i = 0; i < num; i += 8) {
//            x = tea_decipher(v[i:i + 8],k);
//            ret += x;
//        }
//
//        ret += v[num:];
//        return ret;
//    }
//
//    public boolean IsGoodLogBuffer(byte[] _buffer, int _offset, int count) {
//        int crypt_key_len;
//        if (_offset == len(_buffer)) return true;
//
//        int magic_start = _buffer[_offset];
//        if (MAGIC_NO_COMPRESS_START == magic_start ||
//                MAGIC_COMPRESS_START == magic_start ||
//                MAGIC_COMPRESS_START1 == magic_start) {
//            crypt_key_len = 4;
//        } else if (MAGIC_COMPRESS_START2 == magic_start ||
//                MAGIC_NO_COMPRESS_START1 == magic_start ||
//                MAGIC_NO_COMPRESS_NO_CRYPT_START == magic_start ||
//                MAGIC_COMPRESS_NO_CRYPT_START == magic_start ||
//                MAGIC_SYNC_ZSTD_START == magic_start ||
//                MAGIC_SYNC_NO_CRYPT_ZSTD_START == magic_start ||
//                MAGIC_ASYNC_ZSTD_START == magic_start ||
//                MAGIC_ASYNC_NO_CRYPT_ZSTD_START == magic_start) {
//            crypt_key_len = 64;
//        } else {
//            return false;//(False,'_buffer[%d]:%d != MAGIC_NUM_START' % (_offset, _buffer[_offset]));
//        }
//
//        int headerLen = 1 + 2 + 1 + 1 + 4 + crypt_key_len;
//
//        if ((_offset + headerLen + 1 + 1) > len(_buffer))
//            return false;//(False,'offset:%d > len(buffer):%d' % (_offset,len(_buffer)))
//        int length = struct.unpack_from("I", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len, 4))[0];
//        if ((_offset + headerLen + length + 1) > len(_buffer))
//            return false;//(False,'log length:%d, end pos %d > len(buffer):%d' % (length, _offset + headerLen + length + 1, len(_buffer)));
//        if (MAGIC_END != _buffer[_offset + headerLen + length])
//            return false;//(False,'log length:%d, buffer[%d]:%d != MAGIC_END' % (length, _offset + headerLen + length, _buffer[_offset + headerLen + length]))
//
//
//        if (1 >= count) {
//            return true;//(True,'')
//        } else {
//            return IsGoodLogBuffer(_buffer, _offset + headerLen + length + 1, count - 1);
//        }
//    }
//
//    public int GetLogStartPos(byte[] _buffer, int _count) {
//        int offset = 0;
//        while (true) {
//            if (offset >= len(_buffer)) break;
//
//            if (MAGIC_NO_COMPRESS_START == _buffer[offset]
//                    || MAGIC_NO_COMPRESS_START1 == _buffer[offset]
//                    || MAGIC_COMPRESS_START == _buffer[offset]
//                    || MAGIC_COMPRESS_START1 == _buffer[offset]
//                    || MAGIC_COMPRESS_START2 == _buffer[offset]
//                    || MAGIC_COMPRESS_NO_CRYPT_START == _buffer[offset]
//                    || MAGIC_NO_COMPRESS_NO_CRYPT_START == _buffer[offset]
//                    || MAGIC_SYNC_ZSTD_START == _buffer[offset]
//                    || MAGIC_SYNC_NO_CRYPT_ZSTD_START == _buffer[offset]
//                    || MAGIC_ASYNC_ZSTD_START == _buffer[offset]
//                    || MAGIC_ASYNC_NO_CRYPT_ZSTD_START == _buffer[offset]) {
//                if (IsGoodLogBuffer(_buffer, offset, _count)) {
//                    return offset;
//                }
//            }
//            offset += 1;
//        }
//        return -1;
//    }
//
//    public int len(byte[] a) {
//        return a.length;
//    }
//
//    public int DecodeBuffer(byte[] _buffer, int _offset, byte[] _outbuffer) {
//
//        if (_offset >= len(_buffer)) {
//            return -1;
//        }
//        boolean ret = IsGoodLogBuffer(_buffer, _offset, 1);
//        if (!ret) {
//            int fixpos = GetLogStartPos(_buffer[_offset:],1);
//            if (-1 == fixpos) {
//                return -1;
//            } else {
//                _outbuffer.extend("[F]decode_log_file.py decode error len=%d, result:%s \n" % (fixpos, ret[1]))
//                ;
//                _offset += fixpos;
//            }
//        }
//        byte magic_start = _buffer[_offset];
//        int crypt_key_len = 0;
//        if (MAGIC_NO_COMPRESS_START == magic_start ||
//                MAGIC_COMPRESS_START == magic_start ||
//                MAGIC_COMPRESS_START1 == magic_start) {
//            crypt_key_len = 4;
//        } else if (MAGIC_COMPRESS_START2 == magic_start ||
//                MAGIC_NO_COMPRESS_START1 == magic_start ||
//                MAGIC_NO_COMPRESS_NO_CRYPT_START == magic_start ||
//                MAGIC_COMPRESS_NO_CRYPT_START == magic_start ||
//                MAGIC_SYNC_ZSTD_START == magic_start ||
//                MAGIC_SYNC_NO_CRYPT_ZSTD_START == magic_start ||
//                MAGIC_ASYNC_ZSTD_START == magic_start ||
//                MAGIC_ASYNC_NO_CRYPT_ZSTD_START == magic_start) {
//            crypt_key_len = 64;
//        } else {
//            _outbuffer.extend('in DecodeBuffer _buffer[%d]:%d != MAGIC_NUM_START' % (_offset, magic_start))
//            ;
//            return -1;
//        }
//        int headerLen = 1 + 2 + 1 + 1 + 4 + crypt_key_len;
//        int length = struct.unpack_from("I", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len, 4))[0];
//        byte[] tmpbuffer = new byte[length];
//
//        int seq = struct.unpack_from("H", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len - 2 - 2, 2))[0];
//        int begin_hour = struct.unpack_from("c", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len - 1 - 1, 1))[0];
//        int end_hour = struct.unpack_from("c", buffer(_buffer, _offset + headerLen - 4 - crypt_key_len - 1, 1))[0];
//
//
//        if (seq != 0
//                && seq != 1
//                && lastseq != 0
//                && seq != (lastseq + 1)) {
//            _outbuffer.extend("[F]decode_log_file.py log seq:%d-%d is missing\n" % (lastseq + 1, seq - 1))
//            ;
//        }
//
//        if (seq != 0) {
//            lastseq = seq;
//        }
//
//        tmpbuffer[:]=_buffer[_offset + headerLen:_offset + headerLen + length];
//
//        try {
//
//
//            if (MAGIC_NO_COMPRESS_START1 == _buffer[_offset] || MAGIC_SYNC_ZSTD_START == _buffer[_offset]) {
//
//            } else if (MAGIC_COMPRESS_START2 == _buffer[_offset] || MAGIC_ASYNC_ZSTD_START == _buffer[_offset]) {
//                svr = pyelliptic.ECC(curve = 'secp256k1');
//                client = pyelliptic.ECC(curve = 'secp256k1');
//                client.pubkey_x = str(buffer(_buffer, _offset + headerLen - crypt_key_len, crypt_key_len / 2));
//                client.pubkey_y = str(buffer(_buffer, _offset + headerLen - crypt_key_len / 2, crypt_key_len / 2));
//
//                svr.privkey = binascii.unhexlify(PRIV_KEY);
//                tea_key = svr.get_ecdh_key(client.get_pubkey());
//
//                tmpbuffer = tea_decrypt(tmpbuffer, tea_key);
//                if (MAGIC_COMPRESS_START2 == _buffer[_offset]) {
//                    decompressor = zlib.decompressobj(-zlib.MAX_WBITS);
//                    tmpbuffer = decompressor.decompress(str(tmpbuffer));
//                } else {
//                    decompressor = zstd.ZstdDecompressor();
//                    tmpbuffer = next(decompressor.read_from(ZstdDecompressReader(str(tmpbuffer)), 100000, 1000000));
//                }
//            } else if (MAGIC_ASYNC_NO_CRYPT_ZSTD_START == _buffer[_offset]) {
//                decompressor = zstd.ZstdDecompressor();
//                tmpbuffer = next(decompressor.read_from(ZstdDecompressReader(str(tmpbuffer)), 100000, 1000000));
//            } else if (MAGIC_COMPRESS_START == _buffer[_offset]
//                    || MAGIC_COMPRESS_NO_CRYPT_START == _buffer[_offset]) {
//                decompressor = zlib.decompressobj(-zlib.MAX_WBITS);
//                tmpbuffer = decompressor.decompress(str(tmpbuffer))
//            } else if (MAGIC_COMPRESS_START1 == _buffer[_offset]) {
//                decompress_data = new byte[];
//                while (len(tmpbuffer) > 0) {
//                    single_log_len = struct.unpack_from("H", buffer(tmpbuffer, 0, 2))[0];
//                    decompress_data.extend(tmpbuffer[2:single_log_len + 2]);
//                    tmpbuffer[:]=tmpbuffer[single_log_len + 2:
//                    len(tmpbuffer)];
//                }
//
//                decompressor = zlib.decompressobj(-zlib.MAX_WBITS);
//                tmpbuffer = decompressor.decompress(str(decompress_data));
//            } else {
//            }
//        } catch (
//                Exception e) {
////        traceback.print_exc()
//            _outbuffer.extend("[F]decode_log_file.py decompress err, " + str(e) + "\n");
//            return _offset + headerLen + length + 1;
//        }
//
//        _outbuffer.extend(tmpbuffer);
//
//        return _offset + headerLen + length + 1;
//
//    }
//
//    public void ParseFile(File _file, File _outfile) {
//        byte[] _buffer = null;
//        try {
//            FileInputStream fileInputStream = new FileInputStream(_file);
//            _buffer = new byte[fileInputStream.available()];
//            fileInputStream.read(_buffer);
//            fileInputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int startpos = GetLogStartPos(_buffer, 2);
//        if (-1 == startpos) {
//
//            return;
//
//
//        }
//        byte[] outbuffer = new byte[1024];
//        while (true) {
//            startpos = DecodeBuffer(_buffer, startpos, outbuffer);
//            if (-1 == startpos) break;
//        }
//        //todo
//        if (outbuffer.length == 0) return;
//
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(_outfile);
//            fileOutputStream.write(outbuffer);
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }
//
//    public static void main(String[] args) {
//    }
}
