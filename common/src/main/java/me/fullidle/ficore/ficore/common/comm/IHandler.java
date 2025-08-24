package me.fullidle.ficore.ficore.common.comm;

/**
 * 通讯使用的处理
 */
public interface IHandler<T extends IMessage> {
    /**
     * 处理指定的信息类
     * @param message 处理的信息类
     */
    void handle(T message);
}
