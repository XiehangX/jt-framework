package io.github.hylexus.jt.utils;

import io.github.hylexus.jt.config.JtProtocolConstant;
import io.github.hylexus.jt.exception.MsgEscapeException;
import io.github.hylexus.oaks.utils.Bytes;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author hylexus
 * createdAt 2019/1/28
 **/
@Slf4j
public class ProtocolUtils {

    public static byte calculateCheckSum4Jt808(byte[] bs, int start, int end) {
        byte sum = bs[start];
        for (int i = start + 1; i < end; i++) {
            sum ^= bs[i];
        }
        return sum;
    }

    public static byte[] doEscape4ReceiveJt808Msg(byte[] bs, int start, int end) throws Exception {
        if (start < 0 || end > bs.length) {
            throw new ArrayIndexOutOfBoundsException("doEscape4Receive error : index out of bounds(start=" + start
                    + ",end=" + end + ",bytes length=" + bs.length + ")");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            int i = 0;
            for (; i < start; i++) {
                outputStream.write(bs[i]);
            }
            // 最后一个字节不应该是7d，否则就是编码错误
            for (; i < end - 1; i++) {
                if (bs[i] == 0x7d && bs[i + 1] == 0x01) {
                    outputStream.write(0x7d);
                    i++;
                } else if (bs[i] == 0x7d && bs[i + 1] == 0x02) {
                    outputStream.write(0x7e);
                    i++;
                } else {
                    outputStream.write(bs[i]);
                }
            }

            // https://github.com/hylexus/jt-framework/issues/17
            for (; i < bs.length; i++) {
                outputStream.write(bs[i]);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new MsgEscapeException(e);
        }
    }

    public static byte[] doEscape4SendJt808Msg(byte[] bs, int start, int end) throws IOException {
        if (start < 0 || end > bs.length) {
            throw new ArrayIndexOutOfBoundsException("doEscape4Send error : index out of bounds(start=" + start
                    + ",end=" + end + ",bytes length=" + bs.length + ")");
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (int i = 0; i < start; i++) {
                outputStream.write(bs[i]);
            }
            for (int i = start; i <= end; i++) {
                if (bs[i] == 0x7e) {
                    outputStream.write(0x7d);
                    outputStream.write(0x02);
                } else if (bs[i] == 0x7d) {
                    outputStream.write(0x7d);
                    outputStream.write(0x01);
                } else {
                    outputStream.write(bs[i]);
                }
            }
            // https://github.com/hylexus/jt-framework/issues/17
            for (int i = end + 1; i < bs.length; i++) {
                outputStream.write(bs[i]);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new MsgEscapeException(e);
        }
    }

    public static String bytes2String(byte[] bytes, int start, int length) {
        return bytes2String(bytes, start, length, null);
    }

    public static String bytes2String(byte[] bytes, int start, int length, String defaultVal) {
        try {
            return new String(Bytes.subSequence(bytes, start, length), JtProtocolConstant.JT_808_STRING_ENCODING);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return defaultVal;
        }
    }

    public static byte[] byteBufToByteArray(ByteBuf buf, int length) {
        byte[] tmp = new byte[length];
        buf.readBytes(tmp);
        return tmp;
    }
}
