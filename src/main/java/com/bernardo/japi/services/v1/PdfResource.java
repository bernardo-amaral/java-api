package com.bernardo.japi.services.v1;

import com.bernardo.japi.TokensBusinessManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

@Path("/v1/pdf")
@Api(value = "/pdf", description = "Manage Tokens")

public class PdfResource {

    //private static final Logger log = Logger.getLogger(TokensResource.class.getName());


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "PDF Test",
            notes = "Just test")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success: { tokens: []}"),
            @ApiResponse(code = 400, message = "Failed: {\"error\": \"error description\", \"status\":\"FAIL\"}")
    })
    public Response getPdf() {

        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("pdf.pdf"));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream("test.jsp"));
            document.close();
            return Response.status(Response.Status.OK).entity("pdf created!").build();
        } catch (Exception e) {

        }

        //http://hmkcode.com/itext-html-to-pdf-using-java/

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Cold Not Generate PDF\", \"status\":\"FAIL\"}")
                .build();
    }

}
