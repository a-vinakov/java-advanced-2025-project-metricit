package ru.otus.vinakov.metric.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.MetricCondition;
import ru.otus.vinakov.metric.domain.script.ScriptType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricConditionDTO {

    private Long id;
    private Integer index;
    private String conditionScript;
    private String resultScript;
    private ScriptType scriptType;
    private MetricDTO metric;

    public MetricConditionDTO(Long id, Integer index, String conditionScript, String resultScript, ScriptType scriptType) {
        this.id = id;
        this.index = index;
        this.conditionScript = conditionScript;
        this.resultScript = resultScript;
        this.scriptType = scriptType;
    }

    public static MetricConditionDTO from(MetricCondition condition, boolean withMetric) {
        MetricConditionDTO dto = new MetricConditionDTO(condition.getId(), condition.getIndex(),
                condition.getConditionScript(), condition.getResultScript(), condition.getScriptType());
        if (withMetric) {
            dto.setMetric(MetricDTO.from(condition.getMetric(), false, false));
        }
        return dto;
    }

}
