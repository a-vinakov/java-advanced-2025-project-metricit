package ru.otus.vinakov.jira.plugin.context;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.vinakov.jira.plugin.dto.EntityMetricDTO;
import ru.otus.vinakov.jira.plugin.dto.EntityMetricList;
import ru.otus.vinakov.jira.plugin.dto.MetricDTO;
import ru.otus.vinakov.jira.plugin.listener.JiraHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class EntityMetricIssuePanelContextProvider extends AbstractJiraContextProvider {

    protected final JiraHttpClient httpClient;

    private static final Logger log = LoggerFactory.getLogger(EntityMetricIssuePanelContextProvider.class);

    public EntityMetricIssuePanelContextProvider() {
        Map<String, String> defaultHeaders = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
        //todo constant
        this.httpClient = new JiraHttpClient("http://localhost:8080", defaultHeaders);
    }

    @Override
    public Map getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Issue issue = (Issue) jiraHelper.getContextParams().get("issue");
        Map context = new HashMap();
        context.put("durationUtils", ComponentAccessor.getJiraDurationUtils());
        if (issue != null) {
            try {
                List<? extends EntityMetricDTO> metrics = getMetrics(issue.getKey());
                context.put("metrics", metrics
                        .stream()
                        .filter(m -> m.getMetric().getCategory() == getMetricCategory())
                        .collect(Collectors.toList()));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            context.put("metrics", Collections.emptyList());
            log.warn("Issue context is null for right panel");
        }
        return context;
    }

    protected abstract MetricDTO.Category getMetricCategory();

    protected abstract List<? extends EntityMetricDTO> getMetrics(String issueKey) throws IOException, InterruptedException;

}