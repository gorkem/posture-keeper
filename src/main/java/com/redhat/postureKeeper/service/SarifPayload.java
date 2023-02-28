package com.redhat.postureKeeper.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

public class SarifPayload {

    private String sarif;

    private String gitRepoUrl; 
    
    private String commit_sha;
    
    public SarifPayload(String sarif, String gitRepoUrl, String commit_sha) {
        this.sarif = sarif;
        this.gitRepoUrl = gitRepoUrl;
        this.commit_sha = commit_sha;
    }

    public String getSarif() {
        return sarif;
    }

    public void setSarif(String sarif) {
        this.sarif = sarif;
    }

    public String getGitRepoUrl() {
        return gitRepoUrl;
    }

    public void setGitRepoUrl(String gitRepoUrl) {
        this.gitRepoUrl = gitRepoUrl;
    }

    public String getCommit_sha() {
        return commit_sha;
    }

    public void setCommit_sha(String commit_sha) {
        this.commit_sha = commit_sha;
    }

    /**
     * Returns the GZIP compressed and Base64 encoded sarif field 
     * as JSON String.
     * 
     * @return JSON formatted SARIF string
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public String getSarifAsJsonString() throws IOException{
        if(this.sarif == null || this.sarif.isEmpty())
        {
            return this.sarif;
        }
        byte[] decoded = Base64.getDecoder().decode(this.sarif);
        byte[] jsonString = this.decompress(decoded);
        return new String(jsonString);
    }

    private byte[] decompress(byte[] compressed) throws IOException {
        GZIPInputStream gzipIn = new GZIPInputStream(new ByteArrayInputStream(compressed));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gzipIn.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        gzipIn.close();
        baos.close();
        return baos.toByteArray();
    }

}