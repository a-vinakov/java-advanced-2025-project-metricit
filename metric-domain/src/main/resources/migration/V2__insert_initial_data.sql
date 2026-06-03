BEGIN;
-- 1. Базовые таблицы (без зависимостей)
INSERT INTO metric_schema (key, name)
VALUES
    ( 'TEST', 'Default Schema');

INSERT INTO time_metric_schedule (key, name, timezone, sleep_date, wake_date, calendar_key, periods)
VALUES
    ( 'SCHEDULE1', 'Schedule 9-13:14-18', 'GMT+03:00', '2026-06-01 18:00:00', '2026-06-02 09:00:00', 'RU_2026', '[{"start":"09:00","end":"13:00"}, {"start":"14:00","end":"18:00"}]'::jsonb),
    ( 'SCHEDULE2', 'Schedule 00-24', 'GMT+03:00', '2026-06-01 19:00:00', '2026-06-02 10:00:00', 'RU_2026', '[{"start":"00:00","end":"23:59"}]'::jsonb);
INSERT INTO metric (key, name, idx, type, schema_id)
VALUES
    ('m_time_1', 'Счетчик рабочего времени', 1, 'TIMER', (select id from metric_schema where key = 'TEST')),
    ('m_time_2', 'Круглосуточный счетчик', 1, 'TIMER', (select id from metric_schema where key = 'TEST')),
    ('m_num_1', 'Number Metric 1', 3, 'COUNTER', (select id from metric_schema where key = 'TEST'));

-- 3. Специфичные таблицы метрик (наследуются/ссылаются на metric)
INSERT INTO time_metric (id, target_time_default, target_date_default, schedule_id)
VALUES
     ((select id from metric where key = 'm_time_1'), 7200, NULL, (select id from time_metric_schedule where key = 'SCHEDULE1')),
     ((select id from metric where key = 'm_time_2'), 638400, NULL, (select id from time_metric_schedule where key = 'SCHEDULE2'));

INSERT INTO number_metric (id, target_value_default, actual_value_default)
VALUES
    ((select id from metric where key = 'm_num_1'), 3, 0);

INSERT INTO metric_condition (idx, condition_script, result_script, script_type, metric_id)
VALUES
    (1, '(last == null || last.status.toUpperCase() == "TODO") && now.status.toUpperCase() == "IN PROGRESS"', '({"status" : "START", "actualTime" : 0, "targetTime" : 7200})', 'JS', (select id from metric where key ='m_time_1')),
    (2, 'now.status.toUpperCase() == "DONE"', '({"status" : "STOP"})', 'JS', (select id from metric where key ='m_time_1')),

    (3, '(last == null || last.status.toUpperCase() == "TODO") && now.status.toUpperCase() == "IN PROGRESS"', '({"status" : "START", "actualTime" : 0})', 'JS', (select id from metric where key ='m_time_2')),
    (4, 'now.status.toUpperCase() == "DONE"', '({"status" : "STOP"})', 'JS', (select id from metric where key ='m_time_2')),

    (1, '(last == null || last.priority != "High") && now.priority == "High"', '({"actualValue" : metric.getActualValue() + 1})', 'JS', (select id from metric where key ='m_num_1'));

COMMIT;