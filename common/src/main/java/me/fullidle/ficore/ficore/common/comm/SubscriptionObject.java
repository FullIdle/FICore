package me.fullidle.ficore.ficore.common.comm;

import lombok.Getter;

@Getter
public class SubscriptionObject<T extends IMessage> {
    private final CommManager parent;
    private final Class<T> type;
    private final IHandler<T> handler;

    public SubscriptionObject(CommManager parent, Class<T> type, IHandler<T> handler) {
        this.parent = parent;
        this.type = type;
        this.handler = handler;
    }

    public void unsubscribe() {
        this.parent.unsubscribe(this);
    }
}
