package com.keepyuppy.KeepyUppy;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepyuppy.KeepyUppy.content.communication.request.IssueRequest;
import com.keepyuppy.KeepyUppy.content.communication.response.IssueResponse;
import com.keepyuppy.KeepyUppy.content.domain.entity.Issue;
import com.keepyuppy.KeepyUppy.content.domain.enums.ContentType;
import com.keepyuppy.KeepyUppy.content.domain.enums.IssueStatus;
import com.keepyuppy.KeepyUppy.content.service.PostService;
import com.keepyuppy.KeepyUppy.member.domain.entity.Member;
import com.keepyuppy.KeepyUppy.member.domain.enums.Grade;
import com.keepyuppy.KeepyUppy.member.domain.enums.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class IssueTest {

    @Autowired
    PostService postService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testCreateIssueRequest() throws JsonProcessingException {
        String json = "{\n" +
                "  \"title\": \"Issue title\",\n" +
                "  \"content\": \"Issue content\",\n" +
                "  \"dueDate\": \"2024-03-20 12:00:00\",\n" +
                "  \"status\": \"TODO\",\n" +
                "  \"assignedMembers\": [\"JohnDoe\", \"JaneSmith\"]\n" +
                "}";

        LocalDateTime expectedDateTime = LocalDateTime.of(2024, 3, 20, 12, 0, 0);


        IssueRequest request = objectMapper.readValue(json, IssueRequest.class);

        assertEquals("Issue title", request.getTitle());
        assertEquals("Issue content", request.getContent());
        assertEquals(expectedDateTime, request.getDueDate());
        assertEquals(IssueStatus.TODO, request.getStatus());
    }

    @Test
    void testIssueResponse() throws JsonProcessingException {
        LocalDateTime dueDate = LocalDateTime.of(2024, 3, 20, 12, 0, 0);

        Member member = new Member(null, null, Grade.TEAM_MEMBER, Status.ACCEPTED);
        Issue issue = Issue.issueBuilder()
                .team(null)
                .title("Issue title")
                .author(member)
                .content("Issue content")
                .dueDate(dueDate)
                .status(IssueStatus.TODO)
                .issueAssignments(Collections.emptySet())
                .type(ContentType.ISSUE)
                .build();

        String expectedJson = "{\"id\":null," +
                "\"title\":\"Issue title\"," +
                "\"author\":{\"name\":null,\"role\":null,\"grade\":\"TEAM_MEMBER\"}," +
                "\"content\":\"Issue content\"," +
                "\"assignedMembers\":[]," +
                "\"dueDate\":\"2024-03-20 12:00:00\"," +
                "\"status\":\"TODO\"}";

        IssueResponse response = IssueResponse.of(issue, Collections.emptySet());
        String actualJson = objectMapper.writeValueAsString(response);

        Assertions.assertThat(actualJson).isEqualTo(expectedJson);
    }



}
