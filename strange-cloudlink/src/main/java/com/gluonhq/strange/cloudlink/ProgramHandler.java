package com.gluonhq.strange.cloudlink;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("program")
public class ProgramHandler {

    @GET
    @Path("execute")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Result executeProgram(Program program) {
        return new Result();
    }
}
