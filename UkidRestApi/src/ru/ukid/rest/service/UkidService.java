package ru.ukid.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource (exposed at "api" path)
 */
@Path("")
public class UkidService {

	public UkidService() {
	}
	
	/**
     * Retrieves ping command
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getPing() {
        return "{\"ping\":\"ok\"}";
    }
}
