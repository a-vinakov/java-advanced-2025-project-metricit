package ru.otus.vinakov.metric.script;

import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.script.ScriptType;

import java.util.Optional;

@Component
public class GraalVMScriptProcessor implements ScriptProcessor {

    @Override
    public Optional<EvaluableScript> getScript(String scriptKey) {
        return Optional.empty();
    }

    @Override
    public EvaluableScript buildScript(String scriptKey, String script) {
        return null;
    }

    @Override
    public ScriptType getType() {
        return null;
    }
}
