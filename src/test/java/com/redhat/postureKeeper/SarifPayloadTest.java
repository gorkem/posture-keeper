package com.redhat.postureKeeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static com.redhat.postureKeeper.TestDataHelper.SIMPLE_SARIF;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;
import org.junit.jupiter.api.Test;
import com.redhat.postureKeeper.service.SarifPayload;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class SarifPayloadTest {

    @Test
    public void testConstructor(){
     SarifPayload payload = new SarifPayload("sarif", "gitRepoUrl", "commit_sha");
     assertEquals("sarif", payload.getSarif());  
     assertEquals("gitRepoUrl", payload.getGitRepoUrl());
     assertEquals("commit_sha", payload.getCommit_sha());

    }
  
    @Test
    public void testGetSarifAsJsonString() throws IOException{
        byte[] compressed = compress(SIMPLE_SARIF);
        byte[] encoded = Base64.getEncoder().encode(compressed);
        SarifPayload payload = new SarifPayload(new String(encoded), "gitrepourl", "commit_sha");
        String sarifJson = payload.getSarifAsJsonString();
        assertEquals(SIMPLE_SARIF, sarifJson);
    }

    @Test
    public void testGetSarifAsJsonStringForNull() throws IOException{
        SarifPayload payload = new SarifPayload(null, "gitrepourl", "commit_sha");
        assertNull(payload.getSarifAsJsonString());
        SarifPayload payloadEmpty = new SarifPayload("", "gitrepourl", "commit_sha"); 
        assertEquals("", payloadEmpty.getSarifAsJsonString());
    }

    private static byte[] compress(String data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data.getBytes());
        gzip.close();
        byte[] compressedData = bos.toByteArray();
        bos.close();
        return compressedData;
    }
}
