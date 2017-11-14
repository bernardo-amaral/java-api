package com.bernardo.japi.services.v1;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.bernardo.japi.TokensBusinessManager;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@Path("/v1/tokens")
@Api(value = "/tokens", description = "Manage Tokens")

public class TokensResource {

    private static final Logger log = Logger.getLogger(TokensResource.class.getName());

    @GET
    @Path("/{tokenId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find Token",
            notes = "Check if the Token is valid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success: { true}"),
            @ApiResponse(code = 400, message = "Failed: {\"error\": \"error description\", \"status\":\"FAIL\"}")
    })

    public Response getTokenById(@ApiParam(value = "userId", required = true, defaultValue = "23456", allowableValues = "", allowMultiple = false)
                                @PathParam("tokenId") String tokenId) {

        log.info("UserResource::getUserById started userId=" + tokenId);

        if (tokenId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Empty userId\", \"status\":\"FAIL\"}")
                    .build();
        }

        try {
            Token token = TokensBusinessManager.getInstance().findToken(tokenId);
            return Response.status(Response.Status.OK).entity(token).build();
        } catch(Exception e) {

        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Cold Not Find User\", \"status\":\"FAIL\"}")
                .build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get All Tokens",
            notes = "This API retrieves all tokens")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success: { tokens: []}"),
            @ApiResponse(code = 400, message = "Failed: {\"error\": \"error description\", \"status\":\"FAIL\"}")
    })
    public Response getToken() {

        try {
            List<Token> tokens = TokensBusinessManager.getInstance().findTokens();

            TokensHolder tokenHolder = new TokensHolder();
            tokenHolder.setTokens(tokens);

            return Response.status(Response.Status.OK).entity(tokenHolder).build();
        } catch (Exception e) {

        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Cold Not Find User\", \"status\":\"FAIL\"}")
                .build();
    }


    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a new Token",
            notes = "This API creates a new token if the uid does not exist" +
                    "<p><u>Input Parameters</u><ul><li><b>new token object</b> is required</li></ul>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success: { token: details}"),
            @ApiResponse(code = 400, message = "Failed: {\"error\": \"error description\", \"status\":\"FAIL\"}")
    })
    public Response createToken(
            @ApiParam(value = "New Token", required = true, defaultValue = "\"{\"name\": \"Garage Door\"}\"", allowableValues = "", allowMultiple = false)
                    Token token) {

        try {
            Token newToken = TokensBusinessManager.getInstance().addToken(token);

            return Response.status(Response.Status.OK).entity(newToken).build();
        } catch (Exception e) {

        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Cold Not Create Token\", \"status\":\"FAIL\"}")
                .build();
    }




}
