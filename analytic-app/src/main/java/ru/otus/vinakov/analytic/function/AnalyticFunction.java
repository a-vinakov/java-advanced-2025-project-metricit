package ru.otus.vinakov.analytic.function;

public interface AnalyticFunction<PARAMS, RESULT> {

    String getKey();

    String getName();

    String getDescription();

    RESULT execute(PARAMS params);

    Class<? extends PARAMS> getParamsClass();
}
