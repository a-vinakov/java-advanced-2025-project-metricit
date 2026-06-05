package ru.otus.vinakov.metric.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.vinakov.metric.dto.domain.MetricHistoryWithMetricKeyDTO;
import ru.otus.vinakov.metric.repository.MetricHistoryRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricHistoryService {

    private final MetricHistoryRepository metricHistoryRepository;

    public List<MetricHistoryWithMetricKeyDTO> getEntityMetricHistory(String entityKey) {
        List<MetricHistoryWithMetricKeyDTO> list = metricHistoryRepository.findAllLatestByEntityKey(entityKey);
        return list;
    }
}
