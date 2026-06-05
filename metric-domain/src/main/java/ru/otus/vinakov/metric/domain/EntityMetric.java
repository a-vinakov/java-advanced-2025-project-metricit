package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entity_metric")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EntityMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;

    @Column(name = "entity_key", nullable = false)
    private String entityKey;

    @OneToMany(mappedBy = "metric", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<MetricHistory> history = new ArrayList<>();

    @Transient
    private EntityEvent event;

    public EntityMetric(Metric metric, String entityKey, EntityEvent event) {
        this.metric = metric;
        this.entityKey = entityKey;
        this.event = event;
    }

    @PrePersist
    protected void prePersist() {
        this.history.add(buildHistory());
    }

    @PreUpdate
    protected void preUpdate() {
        this.history.add(buildHistory());
    }

    public abstract Metric.Category getCategory();

    protected abstract MetricHistory buildHistory();

}
