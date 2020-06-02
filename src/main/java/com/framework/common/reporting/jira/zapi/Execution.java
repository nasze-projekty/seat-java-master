package com.framework.common.reporting.jira.zapi;

import com.framework.common.reporting.jira.JiraConfig;
import com.framework.common.properties.Property;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestResult;

import java.io.File;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Execution {

    private static final Logger logger = LogManager.getLogger();

    private final String version;
    private final String issue;
    private List<Integer> idList;
    private int currentStatus;

    /**
     * Constructor that links an execution to an issue.
     */
    public Execution(String issue) {
        this.version = Property.RESULT_VERSION.getValue();
        this.issue = issue;
        initExecutionIdsAndCurrentStatus();
    }

    private void initExecutionIdsAndCurrentStatus() {
        if (isBlank(version) || isBlank(issue)) {
            return;
        }
        String query = String.format(
                "issue='%s' and fixVersion='%s'", issue, version);

        SearchExecutions search = new SearchExecutions(query);
        idList = search.getExecutionIds();

        List<Integer> statusList = search.getExecutionStatuses();
        if (!statusList.isEmpty()) {
            currentStatus = statusList.get(0);
        }
    }

    /**
     * Gets the ZAPI status of the TestNG status.
     *
     * @return ZAPI execution status from the ITestResult status
     */
    public static int getZAPIStatus(int status) {
        switch (status) {
            case ITestResult.SUCCESS:
                return JiraConfig.ZapiStatus.ZAPI_STATUS_PASS;
            case ITestResult.FAILURE:
                return JiraConfig.ZapiStatus.ZAPI_STATUS_FAIL;
            case ITestResult.SKIP:
                return JiraConfig.ZapiStatus.ZAPI_STATUS_BLOCKED;
            default:
                return JiraConfig.ZapiStatus.ZAPI_STATUS_FAIL;
        }
    }

    public int getExecutionStatus() {
        return currentStatus;
    }

    /**
     * Update issue with a comment and attachments.
     */
    public void update(int status, String comment, String... attachments) {
        if (idList == null) {
            return;
        }
        for (Integer executionId : idList) {
            updateStatusAndComment(executionId, status, comment);
            replaceExistingAttachments(executionId, attachments);

            logger.debug("ZAPI Updater - Updated {} to status {}", issue, status);
        }
    }

    private void updateStatusAndComment(Integer executionId, int status, String comment) {

        try {
            JSONObject obj = new JSONObject();
            obj.put("status", String.valueOf(status));
            int commentMaxLen = 750;
            obj.put("comment", StringUtils.abbreviate(comment, commentMaxLen));

            JiraConfig.getJIRARequestSpec()
                    .contentType("application/json")
                    .body(obj.toString())
                    .when()
                    .put(JiraConfig.REST_ZAPI_PATH + "execution/" + executionId + "/execute");

        } catch (JSONException e) {
            logger.error("Update status and comment failed", e);
        }
    }

    private void replaceExistingAttachments(Integer executionId, String... attachments) {
        if (attachments != null) {
            deleteExistingAttachments(executionId);
            addAttachments(executionId, attachments);
        }
    }

    private void deleteExistingAttachments(Integer executionId) {

        String path = "attachment/attachmentsByEntity?entityType=EXECUTION&entityId=" + executionId;

        JiraConfig.getJIRARequestSpec()
                .get(JiraConfig.REST_ZAPI_PATH + path).thenReturn().jsonPath()
                .getList("data.fileId", String.class)
                .stream()
                .map(fileId -> JiraConfig.REST_ZAPI_PATH + "attachment/" + fileId)
                .forEach(JiraConfig.getJIRARequestSpec()::delete);
    }

    private void addAttachments(Integer executionId, String... attachments) {

        String path = JiraConfig.REST_ZAPI_PATH
                + "attachment?entityType=EXECUTION&entityId=" + executionId;

        Arrays.stream(attachments)
                .filter(Objects::nonNull)
                .map(File::new)
                .forEach(attachment ->
                        JiraConfig.getJIRARequestSpec()
                                .header("X-Atlassian-Token", "nocheck")
                                .multiPart(attachment)
                                .when()
                                .post(path));
    }
}
