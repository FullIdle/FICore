package me.fullidle.ficore.ficore.common.comm;

import lombok.Getter;

@Getter
public class SubscriptionObject<T extends IMessage> {
    private final Class<T> type;
    private final IHandler<T> handler;

    public SubscriptionObject(Class<T> type, IHandler<T> handler) {
        this.type = type;
        this.handler = handler;
    }

    public void unsubscribe() {
        CommManager.unsubscribe(this);
    }
}
