package ru.otus.vinakov.analytic.function;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.otus.vinakov.analytic.repository.NumberEntityMetricRepository;
import ru.otus.vinakov.metric.domain.EntityMetric;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntitiesListByNumberMetricValueGEValueAF implements AnalyticFunction<EntitiesListByNumberMetricValueGEValueAF.Params, Set<String>> {

    @Getter
    @Setter
    public static class Params {
        private String schemaKey;
        private String metricKey;
        private Integer value;
    }

    private final NumberEntityMetricRepository repository;

    @Override
    public String getKey() {
        return "entities_with_prop_was_set_more_x_times";
    }

    @Override
    public String getName() {
        return "Entities with prop was set more X times ";
    }

    @Override
    public String getDescription() {
        return "Entities in which the value of an object's property has changed multiple times to a specific value";
    }

    @Override
    public Set<String> execute(Params params) {
        //вытащить все метрики с ключом, у которых на счетчике больше X значений
        return repository.findAllByMetricSchemaKeyAndMetricKeyAndActualValueGreaterThanEqual(params.getSchemaKey(), params.getMetricKey(), params.getValue().doubleValue())
                .stream().map(EntityMetric::getEntityKey).collect(Collectors.toSet());
    }

    @Override
    public Class<? extends Params> getParamsClass() {
        return Params.class;
    }



}
