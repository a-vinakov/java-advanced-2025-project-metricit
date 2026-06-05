package ru.otus.vinakov.metric.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Component
public class MetricTaskSynchronizer {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public void runMetricScheduler(Runnable runnable) {
        lock.writeLock().lock();
        try {
            runnable.run();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Transactional
    public void runMetricEventHandler(Runnable runnable) {
        lock.readLock().lock();
        try {
            runnable.run();
        } catch (Exception e) {
            log.error("Event processing failed", e);
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

}
