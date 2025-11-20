package me.fullidle.ficore.ficore.common.comm;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.val;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class CommManager {
    private final Map<Class<? extends IMessage>, HashSet<? extends IHandler<? extends IMessage>>> MESSAGE_SUBSCRIPTIONS = new HashMap<>();

    public <T extends IMessage, E extends IHandler<T>> HashSet<E> getHandlers(final Class<T> type) {
        return (HashSet<E>) MESSAGE_SUBSCRIPTIONS.computeIfAbsent(type, k -> new HashSet<IHandler<T>>());
    }

    public <T extends IMessage, E extends IHandler<T>> SubscriptionObject<T> subscribe(final Class<T> clazz, final E handler) {
        val handlers = getHandlers(clazz);
        if (handlers.contains(handler)) throw new IllegalArgumentException("Handler already subscribed!");
        handlers.add(handler);
        return new SubscriptionObject<>(this, clazz, handler);
    }

    public <T extends IMessage> void unsubscribe(final SubscriptionObject<T> subscriptionObject) {
        getHandlers(subscriptionObject.getType()).remove(subscriptionObject.getHandler());
    }

    public <T extends IMessage> void publish(final T message) {
        getHandlers(((Class<T>) message.getClass())).forEach(handler -> handler.handle(message));
    }

    /**
     * 将该方法用在你要用来通讯得地方对接上
     */
    public <T extends IMessage> void receive(final ByteArrayDataInput byteIn) {
        try {
            publish(receive(((Class<T>) Class.forName(byteIn.readUTF())).newInstance(), byteIn));
        } catch (ClassCastException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException ignored) {
        }
    }

    public <T extends IMessage> T receive(final T message, final ByteArrayDataInput byteIn) {
        message.decode(byteIn);
        return message;
    }

    /**
     * 在你可以发送通讯的地方将格式化好后的数据发出
     */
    public ByteArrayDataOutput formatMessage(final IMessage message) {
        return formatMessage(ByteStreams.newDataOutput(), message);
    }

    public ByteArrayDataOutput formatMessage(ByteArrayDataOutput byteOut, IMessage message) {
        byteOut.writeUTF(message.getClass().getName());
        message.encode(byteOut);
        return byteOut;
    }
}
