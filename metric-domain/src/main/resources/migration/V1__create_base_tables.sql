-- 1. Базовые таблицы без зависимостей
CREATE TABLE IF NOT EXISTS metric_schema (
                               id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                               key VARCHAR(255) NOT NULL,
                               name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS time_metric_schedule (
                                      id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                      key VARCHAR(255) NOT NULL,
                                      name VARCHAR(255) NOT NULL,
                                      timezone VARCHAR(255) NOT NULL,
                                      sleep_date TIMESTAMP(0) NULL,
                                      wake_date TIMESTAMP(0) NULL,
                                      calendar_key VARCHAR(255) NULL,
                                      periods JSONB NOT NULL
);

-- 2. Родительская таблица наследования
CREATE TABLE IF NOT EXISTS metric (
                        id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        key VARCHAR(255) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        idx INTEGER NOT NULL DEFAULT 1,
                        type VARCHAR(255) NOT NULL,
                        schema_id BIGINT NOT NULL REFERENCES metric_schema(id)
);

-- 3. Дочерние таблицы (наследуются от metric)
CREATE TABLE IF NOT EXISTS time_metric (
                             id BIGINT NOT NULL PRIMARY KEY REFERENCES metric(id),
                             target_time_default BIGINT NULL,
                             target_date_default TIMESTAMP(0) NULL,
                             schedule_id BIGINT NULL REFERENCES time_metric_schedule(id)
);

CREATE TABLE IF NOT EXISTS number_metric (
                               id BIGINT NOT NULL PRIMARY KEY REFERENCES metric(id),
                               target_value_default FLOAT(53) NULL,
                               actual_value_default BIGINT NULL
);

CREATE TABLE IF NOT EXISTS entity_metric (
                               id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                               metric_id BIGINT NOT NULL REFERENCES metric(id),
                               entity_key VARCHAR(255) NOT NULL
);

-- 4. Сущности, привязанные к конкретным типам метрик
CREATE TABLE IF NOT EXISTS time_entity_metric (
                                    id BIGINT NOT NULL PRIMARY KEY REFERENCES entity_metric(id),
                                    status VARCHAR(255) NOT NULL,
                                    sleeping BOOLEAN NOT NULL DEFAULT FALSE,
                                    activation_date TIMESTAMP(0),
                                    actual_time BIGINT,
                                    target_time BIGINT,
                                    actual_date TIMESTAMP(0),
                                    target_date TIMESTAMP(0)
);

CREATE TABLE IF NOT EXISTS number_entity_metric (
                                      id BIGINT NOT NULL PRIMARY KEY REFERENCES entity_metric(id),
                                      actual_value FLOAT(53) NULL,
                                      target_value FLOAT(53) NULL
);

CREATE TABLE IF NOT EXISTS entity_event (
                                     id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                     entity_key VARCHAR(255) NOT NULL,
                                     attributes JSONB NOT NULL,
                                     created TIMESTAMP(0) NOT NULL
);
-- 5. История изменений и условия
CREATE TABLE IF NOT EXISTS metric_history (
                                     id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                     metric_id BIGINT NOT NULL REFERENCES entity_metric(id),
                                     event_id BIGINT NOT NULL REFERENCES entity_event(id),
                                     created TIMESTAMP(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS time_metric_history (
                                     id BIGINT NOT NULL PRIMARY KEY REFERENCES metric_history(id),
                                     status_new VARCHAR(255) NULL,
                                     sleeping_new BOOLEAN NULL,
                                     activation_date_new TIMESTAMP(0) NULL,
                                     actual_time_new BIGINT NULL,
                                     target_time_new BIGINT NULL,
                                     actual_date_new TIMESTAMP(0) NULL,
                                     target_date_new TIMESTAMP(0) NULL
);

CREATE TABLE IF NOT EXISTS number_metric_history (
                                       id BIGINT NOT NULL PRIMARY KEY REFERENCES metric_history(id),
                                       actual_value_new FLOAT(53) NULL,
                                       target_value_new FLOAT(53) NULL
);

CREATE TABLE IF NOT EXISTS metric_condition (
                                  id BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                  idx INTEGER NOT NULL,
                                  condition_script VARCHAR(255) NOT NULL,
                                  result_script VARCHAR(255) NOT NULL,
                                  script_type VARCHAR(255) NOT NULL,
                                  metric_id BIGINT NOT NULL REFERENCES metric(id)
);