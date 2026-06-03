package ru.otus.vinakov.metric.domain.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EntityLifecycleEvent extends ApplicationEvent {

    public enum Phase {PRE_PERSIST, POST_PERSIST, PRE_UPDATE, POST_UPDATE}

    private final Phase phase;

    public EntityLifecycleEvent(Object source, Phase phase) {
        super(source);
        this.phase = phase;
    }
}
