package ru.otus.vinakov.metric.script;

import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.script.ScriptType;

import java.util.Optional;

@Component
public class JSScriptProcessor implements ScriptProcessor {

    private static class JSEvaluableScript extends GraalVMEvaluableScript {

        public JSEvaluableScript(String key, String script) {
            super(key, script);
        }

        @Override
        protected String getLanguageId() {
            return "js";
        }
    }

    @Override
    public Optional<EvaluableScript> getScript(String scriptKey) {
        //todo кеширование
        return Optional.empty();
    }

    @Override
    public EvaluableScript buildScript(String scriptKey, String script) {
        return new JSEvaluableScript(scriptKey, script);
    }

    @Override
    public ScriptType getType() {
        return ScriptType.JS;
    }
}
