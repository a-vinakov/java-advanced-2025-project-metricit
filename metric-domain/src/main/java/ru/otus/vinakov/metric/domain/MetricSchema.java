package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "metric_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetricSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "name", nullable = false)
    private String name;
}
