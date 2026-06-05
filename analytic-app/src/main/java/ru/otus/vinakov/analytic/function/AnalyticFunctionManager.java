package ru.otus.vinakov.analytic.function;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AnalyticFunctionManager {

    private final List<AnalyticFunction> functions;

    public Optional<AnalyticFunction> getFunction(String functionKey) {
        return functions.stream().filter(f -> f.getKey().equals(functionKey)).findFirst();
    }
}
