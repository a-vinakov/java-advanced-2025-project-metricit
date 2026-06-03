package ru.otus.vinakov.metric.script;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.io.IOException;
import java.util.Map;

public abstract class GraalVMEvaluableScript implements ScriptProcessor.EvaluableScript {

    private final String key;
    private final String script;
    private final Context context;

    public GraalVMEvaluableScript(String key, String script) {
        this.key = key;
        this.script = script;
        this.context = Context.newBuilder(getLanguageId())
                .allowAllAccess(true)
                .build();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Value eval(Map<String, Object> variables) {
        variables.forEach((k,v) -> context.getBindings(getLanguageId()).putMember(k, Value.asValue(v)));
        return context.eval(getLanguageId(), script);
    }

    @Override
    public void close() throws IOException {
//        context.close();
    }

    protected abstract String getLanguageId();
}
