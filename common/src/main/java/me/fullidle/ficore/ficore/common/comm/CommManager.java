package me.fullidle.ficore.ficore.common.comm;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class CommManager {
    private final Map<Class<? extends IMessage>, HashSet<? extends IHandler<? extends IMessage>>> MESSAGE_SUBSCRIPTIONS = new HashMap<>();
    private final BiMap<Integer, Class<? extends IMessage>> MESSAGE_TYPES = HashBiMap.create();

    public <T extends IMessage, E extends IHandler<T>> HashSet<E> getHandlers(final Class<T> type) {
        return (HashSet<E>) MESSAGE_SUBSCRIPTIONS.computeIfAbsent(type, k -> new HashSet<IHandler<T>>());
    }

    public void registerMessage(final Class<? extends IMessage> type, final int id) {
        if (this.MESSAGE_TYPES.containsValue(type))
            throw new IllegalArgumentException(type.getName() + "Message type already registered!");
        if (this.MESSAGE_TYPES.containsKey(id)) throw new IllegalArgumentException(id + "Message id already registered!");
        this.MESSAGE_TYPES.put(id, type);
    }

    /**
     * 订阅信息
     * @param clazz 信息类型
     * @param handler 处理器
     * @return 订阅对象
     */
    public <T extends IMessage, E extends IHandler<T>> SubscriptionObject<T> subscribe(final Class<T> clazz, final E handler) {
        if (!this.MESSAGE_TYPES.containsValue(clazz))
            throw new IllegalArgumentException("Message type not registered!");
        val handlers = getHandlers(clazz);
        if (handlers.contains(handler)) throw new IllegalArgumentException("Handler already subscribed!");
        handlers.add(handler);
        return new SubscriptionObject<>(this, clazz, handler);
    }

    /**
     * 取消订阅
     * @param subscriptionObject 订阅对象
     */
    public <T extends IMessage> void unsubscribe(final SubscriptionObject<T> subscriptionObject) {
        getHandlers(subscriptionObject.getType()).remove(subscriptionObject.getHandler());
    }

    /**
     * 编码
     * @param data 编码前数据
     * @param message 信息
     * @return 编码后数据
     */
    public @NotNull ByteArrayDataOutput encode(@NotNull ByteArrayDataOutput data, IMessage message) {
        val id = this.MESSAGE_TYPES.inverse().get(message.getClass());
        if (id == null) throw new IllegalArgumentException("Message type not registered!");
        data.writeInt(id);
        message.encode(data);
        return data;
    }

    /**
     * 解码
     * @param data 数据
     * @return 数据信息 解码失败和不存在注册的id返回null
     */
    public @Nullable IMessage decode(ByteArrayDataInput data) {
        val id = data.readInt();
        val clazz = this.MESSAGE_TYPES.get(id);
        if (clazz == null) return null;
        try {
            val message = clazz.getConstructor().newInstance();
            message.decode(data);
            return message;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通知发布
     * @param message 信息
     */
    public void publish(IMessage message) {
        publish(encode(ByteStreams.newDataOutput(), message));
    }

    /**
     * 发布
     * @param data 数据
     */
    public abstract void publish(ByteArrayDataOutput data);

    public void receive(ByteArrayDataInput data) {
        val decode = decode(data);
        if (decode == null) return;
        receive(decode);
    }

    public void receive(IMessage message) {
        val handlers = this.MESSAGE_SUBSCRIPTIONS.get(message.getClass());
        if (handlers == null) return;
        for (IHandler<?> handler : handlers) ((IHandler<IMessage>) handler).handle(message);
    }
}
