package ru.otus.vinakov.metric.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.otus.vinakov.metric.domain.converter.MapStringObjectConverter;

import java.sql.Timestamp;
import java.util.Map;

@Entity
@Table(name = "entity_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_key", nullable = false)
    private String entityKey;

    @Column(name = "created", nullable = false)
    private Timestamp created;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> attributes;

    public EntityEvent(String entityKey, Timestamp created, Map<String, Object> attributes) {
        this.entityKey = entityKey;
        this.created = created;
        this.attributes = attributes;
    }
}
