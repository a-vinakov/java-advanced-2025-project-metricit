package ru.otus.vinakov.metric.script;

import org.graalvm.polyglot.Value;
import ru.otus.vinakov.metric.domain.script.ScriptType;

import java.util.Map;
import java.util.Optional;

public interface ScriptProcessor {

    interface EvaluableScript extends AutoCloseable {
        String getKey();

        Value eval(Map<String, Object> variables) throws Exception;

    }

    Optional<EvaluableScript> getScript(String scriptKey);

    EvaluableScript buildScript(String scriptKey, String script);

    ScriptType getType();

}
