package com.emobile.springtodo.util;

import com.emobile.springtodo.repository.TodoRepository;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoMetrics {

    private final MeterRegistry meterRegistry;
    private final TodoRepository repository;

    @PostConstruct
    public void initMetrics() {
        meterRegistry.gauge("completed_todos", repository, TodoRepository::countCompleted);
        meterRegistry.gauge("pending_todos", repository, TodoRepository::countPending);

    }
}
