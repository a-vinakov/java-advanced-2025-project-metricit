package ru.otus.vinakov.jira.plugin.context;

import org.codehaus.jackson.type.TypeReference;
import ru.otus.vinakov.jira.plugin.dto.EntityMetricDTO;
import ru.otus.vinakov.jira.plugin.dto.MetricDTO;
import ru.otus.vinakov.jira.plugin.dto.TimeEntityMetricDTO;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TimeMetricIssuePanelContextProvider extends EntityMetricIssuePanelContextProvider {

    @Override
    protected MetricDTO.Category getMetricCategory() {
        return MetricDTO.Category.TIME;
    }

    @Override
    protected List<? extends EntityMetricDTO> getMetrics(String issueKey) throws IOException, InterruptedException {
        List<TimeEntityMetricDTO> metrics = httpClient.get(String.format("/rest/api/metric/entity/%s/%s", issueKey,
                getMetricCategory().name().toLowerCase()), Collections.emptyMap(), new TypeReference<>() {
        });
        return metrics;
    }
}
