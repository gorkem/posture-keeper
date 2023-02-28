package com.redhat.postureKeeper.service;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.postureKeeper.sarif.Run;
import com.redhat.postureKeeper.sarif.SarifSchema210;

@Path("{workspace}/analysis")
public class AnalysisResource {
   
    @Inject AnalysisService sarifService;
    @Inject ObjectMapper mapper;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@RestPath String workspace, SarifPayload payload){
        try{
            String id = sarifService.findId(workspace, payload.getGitRepoUrl(), payload.getCommit_sha());
            String clearSarif = payload.getSarifAsJsonString();
            if( id == null ){
                id = sarifService.add(workspace, 
                    clearSarif, 
                    payload.getGitRepoUrl(), 
                    payload.getCommit_sha());
                    return Response.accepted().entity(id).build();
            }else {           
                SarifSchema210 sarifObj = mapper.readValue(clearSarif, SarifSchema210.class);
                List<Run> runs = sarifObj.getRuns();
                if(runs != null && !runs.isEmpty()){
                    sarifService.appendRuns(workspace, id, runs);
                }
                return Response.accepted().entity(id).build(); 
            }
        } catch (IOException ioe) {
            return Response.status(Status.BAD_REQUEST).entity(ErrorUtilities.getErrorJson("Error decompressing Sarif")).build();
        }
        catch (IllegalArgumentException iae) {
            return Response.status(Status.BAD_REQUEST)
            .entity(ErrorUtilities.getErrorJson("Sarif is not correctly encoded: "+ iae.getMessage())).build();
        }
    }

    @GET
    @Path("{sarifId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSarif(@RestPath String workspace, @RestPath String sarifId ){
        String sarif = sarifService.find(workspace, sarifId);
        if(sarif == null ){
            return Response.status(Status.NOT_FOUND)
            .entity(ErrorUtilities.getErrorJson(
                String.format("No SARIF found with id %s ",sarifId))).build();
        }
        return Response.ok(sarif).build();
    }
    
    @GET
    @Path("{repo}/{commit}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveSarifByCommitHash( @RestPath String workspace, @RestPath String repo, @RestPath String commit) {
        String sarifId = sarifService.findId(workspace, repo, commit);
        if(sarifId == null ){
            return Response.status(Status.NOT_FOUND)
                .entity(ErrorUtilities.getErrorJson(
                String.format("No SARIF found for commit sha %s on %s repository ", commit, repo)))
                .build();
        } 
        String sarif = sarifService.find(workspace, sarifId);
        if(sarif == null ){
            return Response.status(Status.NOT_FOUND)
                .entity(ErrorUtilities.getErrorJson(
                String.format("No SARIF found for commit sha %s on %s repository ", commit, repo)))
                .build();
        }  
        return Response.ok(sarif).build();
    }

}
