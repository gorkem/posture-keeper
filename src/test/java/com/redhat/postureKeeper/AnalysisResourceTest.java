package com.redhat.postureKeeper;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static com.redhat.postureKeeper.TestDataHelper.SIMPLE_SARIF;
import static com.redhat.postureKeeper.TestDataHelper.VALID_REPO;
import static com.redhat.postureKeeper.TestDataHelper.TEST_WORKSPACE;
import static com.redhat.postureKeeper.TestDataHelper.createRunObject;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.redhat.postureKeeper.service.AnalysisResource;
import com.redhat.postureKeeper.service.AnalysisService;

@QuarkusTest
@TestHTTPEndpoint(AnalysisResource.class)
public class AnalysisResourceTest {
    
    private static final String VALID_SARIF_ID = "abcd-1234-defg-5678";
    private static final String VALID_COMMIT_ID = "randohash23456";
    private static final String MINIMUM_PAYLOAD="""
        {
            \"sarif\":\"H4sIAAAAAAAA/6vmUgACpbLUouLM/DwlKwUlIz1DPQMlHYh4UWleMVAwWiGWq5YLAH99WIgsAAAA\",
            \"gitRepoUrl\":\"https://some.git.repo/myrepo/\",
            \"commit_sha\":\"hafadsnai1dfgjanhf\"
        }   
        """;

    @InjectMock AnalysisService sarifService;


    @BeforeEach
    void setUp(){
        when(sarifService.find(TEST_WORKSPACE, "unknownID")).thenReturn(null);
        when(sarifService.find(TEST_WORKSPACE, VALID_SARIF_ID)).thenReturn(SIMPLE_SARIF);
        when(sarifService.add(TEST_WORKSPACE, SIMPLE_SARIF, "https://some.git.repo/myrepo/","hafadsnai1dfgjanhf"))
        .thenReturn(VALID_SARIF_ID);
        when(sarifService.findId(TEST_WORKSPACE, VALID_REPO, VALID_COMMIT_ID)).thenReturn(VALID_SARIF_ID);
        when(sarifService.findId(TEST_WORKSPACE, VALID_REPO, "empty")).thenReturn(null);
        when(sarifService.appendRuns(TEST_WORKSPACE, VALID_COMMIT_ID, Arrays.asList(createRunObject("MyLinter"))))
        .thenReturn(VALID_SARIF_ID);

    }

    @Test
    public void testSaveSarif() {
        given().pathParam("workspace", TEST_WORKSPACE)
            .when().contentType(ContentType.JSON)
            .body(MINIMUM_PAYLOAD)
            .post()
            .then().statusCode(202);
    }

    @Test
    public void testMergeRuns() {
        String body = "{\"sarif\":\"H4sIAAAAAAAAA6vmUgACpbLUouLM/DwlKwUlIz1DPQMlHYh4UWleMVAwGsxTUKiG0kCZkvz8HKAMQgQollKUCTQITRQonpeYmwoyuiS1uEQBrBFJvpYLnQWhY4FkLQA+AH7NnwAAAA==\"," +
        "\"gitRepoUrl\":\""+VALID_REPO+"\","+
        "\"commit_sha\":\""+VALID_COMMIT_ID+"\"}";
        given().pathParam("workspace", TEST_WORKSPACE)
            .when().contentType(ContentType.JSON)
            .body(body)
            .post()
            .then().statusCode(202);
    }

    @Test
    public void testGetSarifNotFound() {
        given().pathParams("sarifId", "unknownID", "workspace", TEST_WORKSPACE)
            .when().contentType(ContentType.JSON)
            .get("{sarifId}")
            .then().statusCode(404)
            .body(is("{ \"message\": \"No SARIF found with id unknownID \" }"))
            .contentType(containsString(ContentType.JSON.toString()));
    }

    @Test
    public void testGetSarif(){
        given().pathParams("sarifId", VALID_SARIF_ID, "workspace", TEST_WORKSPACE)
            .when().contentType(ContentType.JSON)
            .get("{sarifId}")
            .then().statusCode(200)
            .body(is(SIMPLE_SARIF));
    }

    @Test
    public void testGetSarifByCommit(){
        given().pathParams("workspace",TEST_WORKSPACE, "repo", VALID_REPO, "commit", VALID_COMMIT_ID)
            .when().contentType(ContentType.JSON)
            .get("{repo}/{commit}")
            .then().statusCode(200)
            .body(is(SIMPLE_SARIF));
    }

    @Test
    public void testGetSarifByCommitNotFound(){
        given().pathParams("workspace",TEST_WORKSPACE, "repo", VALID_REPO, "commit", "empty")
            .when().contentType(ContentType.JSON)
            .get("{repo}/{commit}")
            .then().statusCode(404)
            .body(is("{ \"message\": \"No SARIF found for commit sha empty on "+VALID_REPO+" repository \" }"))
            .contentType(containsString(ContentType.JSON.toString()));
    }

}
