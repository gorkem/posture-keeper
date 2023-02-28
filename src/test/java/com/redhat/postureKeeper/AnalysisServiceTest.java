package com.redhat.postureKeeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static com.redhat.postureKeeper.TestDataHelper.SIMPLE_SARIF;
import static com.redhat.postureKeeper.TestDataHelper.VALID_REPO;
import static com.redhat.postureKeeper.TestDataHelper.TEST_WORKSPACE;
import static com.redhat.postureKeeper.TestDataHelper.createRunObject;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import com.redhat.postureKeeper.service.AnalysisService;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AnalysisServiceTest {

    @Inject AnalysisService service;

    @Test
    public void addTest(){
        String sarifId = service.add(TEST_WORKSPACE, "{}", VALID_REPO, "commithash");
        assertNotNull(sarifId);
        assertNotEquals("", sarifId);
    }

    @Test
    public void addNFindTest(){
        String hash = Long.toString(System.currentTimeMillis());
        String sarifId = service.add(TEST_WORKSPACE, SIMPLE_SARIF,VALID_REPO, hash);
        assertNotNull(sarifId);
        String sarif = service.find(TEST_WORKSPACE, sarifId);
        assertEquals(SIMPLE_SARIF,sarif);
    }

    @Test
    public void addNFindWithCommitTest(){
        String hash = Long.toString(System.currentTimeMillis());
        String sarifId = service.add(TEST_WORKSPACE, SIMPLE_SARIF,VALID_REPO, hash);
        assertNotNull(sarifId);
        String sarifIdFound = service.findId(TEST_WORKSPACE, VALID_REPO, hash);
        assertEquals(sarifId, sarifIdFound); 
    }

    @Test
    public void addFindNotFoundBySarif(){
        String hash = Long.toString(System.currentTimeMillis());
        String sarifId = service.add(TEST_WORKSPACE, SIMPLE_SARIF, VALID_REPO, hash);
        assertNotNull(sarifId);
        String sarif = service.find(TEST_WORKSPACE, "65f79a5983206303bfcd8d63");
        assertNull(sarif);
    }

    @Test
    public void addFindNotFoundByCommit(){
        String hash = Long.toString(System.currentTimeMillis());
        String sarifId = service.add(TEST_WORKSPACE, SIMPLE_SARIF,VALID_REPO, hash);
        assertNotNull(sarifId);
        String sarif = service.findId(TEST_WORKSPACE, VALID_REPO, hash+"salt");
        assertNull(sarif);
    }
    @Test
    public void findNoCollection(){
        String randomWS = Long.toString(System.currentTimeMillis());
        String sarif = service.find(randomWS, "65f79a5983206303bfcd8d63");
        assertNull(sarif);
    }

    @Test
    public void testAppendRuns(){
        String hash = Long.toString(System.currentTimeMillis());
        String sarifId = service.add(TEST_WORKSPACE, SIMPLE_SARIF,VALID_REPO, hash);
        assertNotNull(sarifId);
        String updatedSarifId = service.appendRuns(TEST_WORKSPACE, sarifId, Arrays.asList(createRunObject("test tool")));
        assertNotNull(updatedSarifId);
        assertEquals(sarifId, updatedSarifId);
    }
}