package ru.otus.vinakov.jira.plugin.condition;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.conditions.AbstractIssueWebCondition;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;

public class NumberMetricIssuePanelCondition extends AbstractIssueWebCondition {

    @Override
    public boolean shouldDisplay(ApplicationUser applicationUser, Issue issue, JiraHelper jiraHelper) {
        return applicationUser != null;
    }

}
