package ru.ukid.rest.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ru.ukid.model.ds.DataRepository;

/**
 * Root resource (exposed at "" path)
 */
@Path("")
public class UkidService {

	private static final Logger _logger = Logger.getLogger(UkidService.class
			.getName());

	private final static String SUCCESS = "success";
	private final static String FAILED = "failed";
	private final static String ERROR = "err";
	
	private final static String JSON_UTF8 = MediaType.APPLICATION_JSON + ";charset=utf-8";
	
	private final static String PATH_PING = "ping";
	private final static String PATH_PINGDB = "pingdb";
	private final static String PATH_AUTH = "auth";
	
	@EJB
	private DataRepository _rep;

	public UkidService() {
	}

	/**
	 * Retrieves API info
	 * 
	 * @return an instance of java.lang.String
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getApi() {

		JsonArray arr = Json.createArrayBuilder().add("/" + PATH_PING).add("/" + PATH_PINGDB).add("/" + PATH_AUTH)
				.build();

		return Json.createObjectBuilder().add("paths", arr).build().toString();
	}

	/**
	 * Retrieves ping command
	 * 
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path(PATH_PING)
	@Produces(MediaType.APPLICATION_JSON)
	public String getPing() {
		return Json.createObjectBuilder().add(PATH_PING, SUCCESS).build()
				.toString();
	}

	/**
	 * Retrieves ping database command
	 * 
	 * @return an instance of java.lang.String
	 */
	@GET
	@Path(PATH_PINGDB)
	@Produces(JSON_UTF8)
	public String getPingDB() {

		String result = FAILED;

		try
		{
			if(_rep.pingDataSource()) result = SUCCESS;
			
		} catch (Exception e) {
			e.printStackTrace();

			return Json.createObjectBuilder().add(PATH_PINGDB, FAILED)
					.add(ERROR, "" + e.getMessage()).build().toString();
		}

		return Json.createObjectBuilder().add(PATH_PINGDB, result).build()
				.toString();
	}

	/**
	 * User authentication
	 * 
	 * @return an instance of java.lang.String
	 * @throws IOException
	 */
	@GET
	@Path(PATH_AUTH)
	@Produces(JSON_UTF8)
	public String checkAuthentication(@QueryParam("u") String user,
			@QueryParam("p") String password,
			@Context final HttpServletResponse response) throws IOException {

		if (user == null || password == null) {
			response.sendError(Response.Status.BAD_REQUEST.getStatusCode());
			return null;
		}

		JsonObjectBuilder resultObject = Json.createObjectBuilder();

		boolean auth = false;
		try {
			auth = _rep.authenticateUser(user, password);
		} catch (Exception e) {
			resultObject.add(PATH_AUTH, FAILED);
			resultObject.add(ERROR, e.getMessage());
			e.printStackTrace();
		}

		if (auth) {
			resultObject.add(PATH_AUTH, SUCCESS);
		}

		return resultObject.build().toString();
	}

}
