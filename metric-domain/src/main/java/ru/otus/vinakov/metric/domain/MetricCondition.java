package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.vinakov.metric.domain.script.ScriptType;

@Entity
@Table(name = "metric_condition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idx", nullable = false)
    private Integer index;

    @Column(name = "condition_script", nullable = false)
    private String conditionScript;

    @Column(name = "result_script", nullable = false)
    private String resultScript;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;

    @Column(name = "scriptType")
    @Enumerated(EnumType.STRING)
    private ScriptType scriptType;

}
