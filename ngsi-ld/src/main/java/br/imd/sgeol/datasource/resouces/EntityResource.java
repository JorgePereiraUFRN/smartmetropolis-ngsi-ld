package br.imd.sgeol.datasource.resouces;

import java.text.ParseException;
import java.util.List;

import javax.measure.unit.SystemOfUnits;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.json.JSONException;

import br.imd.sgeol.datasource.service.EntityService;
import br.imd.sgeol.exception.DaoException;
import br.imd.sgeol.exception.EntityAlreadyExistException;
import br.imd.sgeol.exception.EntityDoesNotExistException;
import br.imd.sgeol.exception.InvalidEntityIdException;
import br.imd.sgeol.exception.ObjectNullException;
import br.imd.sgeol.exception.SgeolServiceException;
import br.imd.sgeol.model.Entity;

@Path("/smartmetropolis")
public class EntityResource extends GenericResource {

	private static final EntityService SERVICE = EntityService.getInstance();

	public EntityResource() {
		super(SERVICE);

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save( String ngsi_ld) {

		try {
			Entity entity = ENTITY_SERIALIZER.jsonLDToEntity(ngsi_ld);
			SERVICE.save(entity);
			return Response.status(201).build();

		} catch (SgeolServiceException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}  catch (ObjectNullException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (InvalidEntityIdException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (EntityAlreadyExistException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (JSONException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (ParseException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(String ngsi_ld) {

		try {
			Entity entity = ENTITY_SERIALIZER.jsonLDToEntity(ngsi_ld);
			SERVICE.update(entity);
			return Response.ok().build();

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}catch (ObjectNullException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}  catch (InvalidEntityIdException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (EntityDoesNotExistException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (JSONException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (ParseException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} 

	}

	@GET
	@Path("find-by-id")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@QueryParam("entity-id") String id) {

		try {
			Entity entity = SERVICE.findById( id);
			if (entity != null) {
				return Response.ok(entity.toNGSI_LD(), MediaType.APPLICATION_JSON).build();
			} else {
				return Response.ok("{}").build();
			}

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@DELETE
	public Response delete(@QueryParam("entity-id") String id) {

		try {
			SERVICE.delete(id);

			return Response.ok().build();

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path("property")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProperty( @QueryParam("entity-id") String entityId,
			String property, @QueryParam("property-name") String propertyId) {

		return super.addProperty(entityId, property, propertyId);

	}

	@PUT
	@Path("property")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProperty(@QueryParam("entity-id") String entityId,
			String property, @QueryParam("property-name") String propertyId) {

		return super.updateProperty(entityId, property, propertyId);

	}

	@DELETE
	@Path("property")
	public Response removeProperty( @QueryParam("entity-id") String entityId,
			@QueryParam("property-name") String propertyName) {

		return super.removeProperty(entityId, propertyName);

	}

	@GET
	@Path("property")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findProperty(@QueryParam("entity-id") String entityId,
			@QueryParam("property-name") String propertyName) {

		return super.findProperty(entityId, propertyName);
	}

	@POST
	@Path("relationaship")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRelationaship(@QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName, String relationaship) {
		return super.addRelationaship(entityId, relationashipName, relationaship);

	}

	@PUT
	@Path("relationaship")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRelationaship(@QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName, String relationaship) {

		return super.updateRelationaship(entityId, relationashipName, relationaship);

	}

	@DELETE
	@Path("relationaship")
	public Response removeRelationaship(@QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName) {
		return super.removeRelationaship(entityId, relationashipName);

	}

	@GET
	@Path("relationaship")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findRelationaship( @QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName) {

		return super.findRelationaship(entityId, relationashipName);
	}

	@POST
	@Path("context")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response addContext(@QueryParam("entity-id") String entityId,
			String context) {

		return super.addContext(entityId, context);

	}

	@DELETE
	@Path("context")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response removeContext(@QueryParam("entity-id") String entityId,
			String context) {

		return super.removeContext(entityId, context);

	}

	@GET
	@Path("find-by-property-filter")
	public Response findByPropertyFilter(@QueryParam("field") String field,
			@QueryParam("operator") String operator, @QueryParam("value") String value,
			@QueryParam("limit") @DefaultValue("1024") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {
		return super.findByPropertyFilter(field, operator, value, limit, offset);
	}

	@GET
	@Path("find-by-relationaship-filter")
	public Response findByRelationashipByFilter(@QueryParam("field") String field, @QueryParam("operator") String operator,
			@QueryParam("value") String value, @QueryParam("limit") @DefaultValue("1024") int limit,
			@QueryParam("offset") @DefaultValue("0") int offset) {
		return super.findByRelationashipByFilter(field, operator, value, limit, offset);
	}

	@GET
	public Response findAll(@QueryParam("limit") @DefaultValue("1024") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {

		return super.findAll(limit, offset);
	}

	@GET
	@Path("find-by-query")
	public Response findByQuery( @QueryParam("query") String query,
			@QueryParam("limit") @DefaultValue("1024") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {

		return super.findByQuery(query, limit, offset);
	}

	

	@POST
	@Path("find-by-document")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findByDocumentQuery(@PathParam("layer") String layer, String document,
			@QueryParam("limit") @DefaultValue("1024") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {

		return super.findByDocumentQuery(document, limit, offset);
	}

	

}
