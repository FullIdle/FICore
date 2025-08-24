package me.fullidle.ficore.ficore.common.comm;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

/**
 * 信息类接口
 */
public interface IMessage {
    /**
     * 编码
     * @param byteOut 可将数据写入该字节数组
     */
    void encode(ByteArrayDataOutput byteOut);
    /**
     * 解码
     * @param byteIn 用于读取数据
     */
    void decode(ByteArrayDataInput byteIn);
}
