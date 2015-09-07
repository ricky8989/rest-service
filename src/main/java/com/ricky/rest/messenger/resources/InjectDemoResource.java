package com.ricky.rest.messenger.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;


@Path("/injectdemo")
@Consumes(MediaType.TEXT_PLAIN) 
@Produces(MediaType.TEXT_PLAIN)
public class InjectDemoResource {
	
	
	/**
	 * Demo usage of other params:
	 * 1. Matrix param (param separated by semi-colon; ex: /injectdemo/annotations;param=value)
	 * 2. Header param (can be use to get session id or any header parameters)
	 * 3. Cookie param (use to get cookie values)
	 * @return
	 */
	
	@GET
	@Path("/annotations")
	public String getParamsUsingAnnotations(@MatrixParam("param") String matrixParam,
										    @HeaderParam("authSessionID") String header,
										    @CookieParam("myCookie") String cookie){
		
		
		return "Matrix param: = ["+ matrixParam +"] Header param: = ["+ header + "] Cookie param: = ["+cookie +"]";
	}

	/**
	 * Demo using the Context to get URI and header information instead of above params
	 * @return
	 */
	@GET
	@Path("/context")
	public String getParamsUsingContext(@Context UriInfo uriInfo, @Context HttpHeaders headers){
		String path = uriInfo.getAbsolutePathBuilder().toString();
		String cookies = headers.getCookies().toString();
		return "Path =["+path+"] Cookies=["+cookies+"]";
	}
	
}
