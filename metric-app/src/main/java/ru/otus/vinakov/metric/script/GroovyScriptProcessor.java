package ru.otus.vinakov.metric.script;

import org.springframework.stereotype.Component;
import ru.otus.vinakov.metric.domain.script.ScriptType;

import java.util.Optional;

@Component
public class GroovyScriptProcessor implements ScriptProcessor {

    private static class GroovyEvaluableScript extends GraalVMEvaluableScript {

        private static final String GROOVY_WRAPPER = "return new groovy.lang.GroovyShell().evaluate(\"%s\")";

        public GroovyEvaluableScript(String key, String script) {
            super(key, String.format(GROOVY_WRAPPER, script));
        }

        @Override
        protected String getLanguageId() {
            return "java";
        }
    }

    @Override
    public Optional<EvaluableScript> getScript(String scriptKey) {
        return Optional.empty();
    }

    @Override
    public EvaluableScript buildScript(String scriptKey, String script) {
        return new GroovyEvaluableScript(scriptKey, script);
    }

    @Override
    public ScriptType getType() {
        return ScriptType.GROOVY;
    }
}
