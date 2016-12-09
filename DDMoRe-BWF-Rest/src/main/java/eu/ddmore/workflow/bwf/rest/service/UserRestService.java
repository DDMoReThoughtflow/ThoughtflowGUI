package eu.ddmore.workflow.bwf.rest.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.ddmore.workflow.bwf.client.model.ResultEntry;
import eu.ddmore.workflow.bwf.client.model.User;
import eu.ddmore.workflow.bwf.client.model.Users;
import eu.ddmore.workflow.bwf.client.service.UserService;
import eu.ddmore.workflow.bwf.client.util.Primitives;
import eu.ddmore.workflow.bwf.rest.spring.AppContext;

@Path("/rest/user")
public class UserRestService extends BaseRestService {

	private UserService userService;
	
	@GET
	@Path("/ping")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response ping() {
		return Response.status(Status.OK).entity(new ResultEntry(true)).build();
	}

	@GET
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response login(@QueryParam("username") String username, @QueryParam("password") String password) {
		/** Validate user input */
		if (Primitives.isEmpty(username)) {
			return error(Status.BAD_REQUEST, "Username must not be empty.");
		}
		if (Primitives.isEmpty(password)) {
			return error(Status.BAD_REQUEST, "Password must not be empty.");
		}
		
		/** Try to do login */
		User user = null;
		try {
			user = getUserService().login(username, password);
		} catch (Throwable ex) {
			throw exception("Error do login for '" + username + "'.", ex);
		}
			
		if (user != null) {
			return Response.status(Status.OK).entity(user).build();	
		}
		return error(Status.UNAUTHORIZED, "Invalid username or password.");
	}
	
	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response search(
			@DefaultValue("false") @QueryParam("loadAuthorities") boolean loadAuthorities, 
			@DefaultValue("false") @QueryParam("loadProjects") boolean loadProjects, 
			@DefaultValue("false") @QueryParam("loadMembers") boolean loadMembers) {
		
		try {
			List<User> users = getUserService().search(loadAuthorities, loadProjects, loadMembers);
			return Response.status(Status.OK).entity(new Users(users)).build();
		} catch (Throwable ex) {
			throw exception("Error get users.", ex);
		}
	}
	
	@GET
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getById(
			@PathParam("id") Long id,
			@DefaultValue("false") @QueryParam("loadAuthorities") boolean loadAuthorities, 
			@DefaultValue("false") @QueryParam("loadProjects") boolean loadProjects, 
			@DefaultValue("false") @QueryParam("loadMembers") boolean loadMembers) {
		
		/** Validate user input */
		if (id == null) {
			return error(Status.BAD_REQUEST, "Id must not be empty.");
		}
		
		try {
			User user = getUserService().getById(id, loadAuthorities, loadProjects, loadMembers);
			if (user != null) {
				return Response.status(Status.OK).entity(user).build();	
			}
			return error(Status.NOT_FOUND, "No user found with id '" + id + "'");
		} catch (Throwable ex) {
			throw exception("Error get user with id '" + id + "'.", ex);
		}
	}

	@GET
	@Path("/username/{username}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getByUsername(
			@PathParam("username") String username,
			@DefaultValue("false") @QueryParam("loadAuthorities") boolean loadAuthorities, 
			@DefaultValue("false") @QueryParam("loadProjects") boolean loadProjects, 
			@DefaultValue("false") @QueryParam("loadMembers") boolean loadMembers) {
		
		/** Validate user input */
		if (Primitives.isEmpty(username)) {
			return error(Status.BAD_REQUEST, "Username must not be empty.");
		}
		
		try {
			User user = getUserService().getByUsername(username, loadAuthorities, loadProjects, loadMembers);
			if (user != null) {
				return Response.status(Status.OK).entity(user).build();	
			}
			return error(Status.NOT_FOUND, "No user found with username '" + username + "'");
		} catch (Throwable ex) {
			throw exception("Error get user with username '" + username + "'.", ex);
		}
	}

	@POST
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response insertOrUpdate(User user) {
		/** Validate user input */
		if (user == null) {
			return error(Status.BAD_REQUEST, "User not be empty.");
		}
		
		try {
			User insertedOrUpdatedUser = getUserService().insertOrUpdate(user);
			return Response.status(Status.OK).entity(insertedOrUpdatedUser).build();
		} catch (Throwable ex) {
			throw exception("Error insert or update user '" + user + "'.", ex);
		}
	}
	
	@DELETE
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteById(@PathParam("id") Long id) {
		/** Validate user input */
		if (id == null) {
			return error(Status.BAD_REQUEST, "Id must not be empty.");
		}
		
		try {
			boolean b = getUserService().deleteById(id);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error delete user with id '" + id + "'.", ex);
		}
	}

	@DELETE
	@Path("/username/{username}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteById(@PathParam("username") String username) {
		/** Validate user input */
		if (Primitives.isEmpty(username)) {
			return error(Status.BAD_REQUEST, "Username must not be empty.");
		}
		
		try {
			boolean b = getUserService().deleteByUsername(username);
			return Response.status(Status.OK).entity(new ResultEntry(b)).build();
		} catch (Throwable ex) {
			throw exception("Error delete user with username '" + username + "'.", ex);
		}
	}

	@GET
	@Path("/members/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMembersById(
			@PathParam("id") Long id,
			@DefaultValue("false") @QueryParam("loadAuthorities") boolean loadAuthorities, 
			@DefaultValue("false") @QueryParam("loadProjects") boolean loadProjects, 
			@DefaultValue("false") @QueryParam("loadMembers") boolean loadMembers) {

		/** Validate user input */
		if (id == null) {
			return error(Status.BAD_REQUEST, "Id must not be empty.");
		}
		
		try {
			List<User> users = getUserService().getMembersById(id, loadAuthorities, loadProjects, loadMembers);
			return Response.status(Status.OK).entity(new Users(users)).build();
		} catch (Throwable ex) {
			throw exception("Error get members for user with id '" + id + "'.", ex);
		}
	}

	@GET
	@Path("/members/username/{username}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getMembersByUsername(
			@PathParam("username") String username,
			@DefaultValue("false") @QueryParam("loadAuthorities") boolean loadAuthorities, 
			@DefaultValue("false") @QueryParam("loadProjects") boolean loadProjects, 
			@DefaultValue("false") @QueryParam("loadMembers") boolean loadMembers) {

		/** Validate user input */
		if (Primitives.isEmpty(username)) {
			return error(Status.BAD_REQUEST, "Username must not be empty.");
		}
		
		try {
			List<User> users = getUserService().getMembersByUsername(username, loadAuthorities, loadProjects, loadMembers);
			return Response.status(Status.OK).entity(new Users(users)).build();
		} catch (Throwable ex) {
			throw exception("Error get members for user with username '" + username + "'.", ex);
		}
	}
	
	// --------------------------------------------------------- Getter, Setter
	
	public UserService getUserService() {
		if (this.userService == null) {
			this.userService = AppContext.getBean(UserService.class);
		}
		return this.userService;
	}
}
