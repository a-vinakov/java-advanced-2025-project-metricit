package ru.otus.vinakov.jira.plugin.listener;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class IssueEventListener implements InitializingBean, DisposableBean {

    @ComponentImport
    private final EventPublisher eventPublisher;
    private final JiraHttpClient httpClient;
    private final List<Long> eventTypes = List.of(
            EventType.ISSUE_CREATED_ID,
            EventType.ISSUE_UPDATED_ID,
            EventType.ISSUE_GENERICEVENT_ID
    );

    private static final Logger logger = LoggerFactory.getLogger(IssueEventListener.class);

    public IssueEventListener(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        Map<String, String> defaultHeaders = Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"
        );
        this.httpClient = new JiraHttpClient("http://localhost:8080", defaultHeaders);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }

    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onIssueEvent(IssueEvent event) {
        if (eventTypes.contains(event.getEventTypeId())) {
            try {
                Issue issue = event.getIssue();
                httpClient.post("/rest/api/metric/event",
                        new IssueMetricEventRequest(issue.getProjectObject().getKey(),
                                issue.getKey(),
                                Map.of(
                                        "status", event.getIssue().getStatus().getName(),
                                        "priority", event.getIssue().getPriority().getName()
                                )), Collections.emptyMap(), new TypeReference<IssueMetricEventResponse>() {});
            } catch (Exception e) {
                logger.error("Issue event processing failed", e);
            }
        }
    }
}
