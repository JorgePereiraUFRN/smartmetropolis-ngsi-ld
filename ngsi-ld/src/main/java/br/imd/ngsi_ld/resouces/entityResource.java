package br.imd.ngsi_ld.resouces;

import java.text.ParseException;
import java.util.List;

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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.geotools.ows.ServiceException;

import br.imd.ngsi_ld.exception.DaoException;
import br.imd.ngsi_ld.exception.EntityAlreadyExistException;
import br.imd.ngsi_ld.exception.EntityDoesNotExistException;
import br.imd.ngsi_ld.exception.InvalidEntityIdException;
import br.imd.ngsi_ld.exception.ObjectNullException;
import br.imd.ngsi_ld.exception.PropertyAlreadyExistException;
import br.imd.ngsi_ld.exception.PropertyDoesNotExistException;
import br.imd.ngsi_ld.exception.RelationashipDoesNotExistException;
import br.imd.ngsi_ld.exception.SgeolServiceException;
import br.imd.ngsi_ld.model.Entity;
import br.imd.ngsi_ld.model.Property;
import br.imd.ngsi_ld.model.Relationaship;
import br.imd.ngsi_ld.service.EntityService;
import br.imd.ngsi_ld.util.EntitySerializer;

@Path("/smartmetropolis")
public class entityResource {

	private static final EntityService entityService = EntityService.getInstance();
	private static final EntitySerializer ENTITY_SERIALIZER = new EntitySerializer();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(String ngsi_ld) {

		try {
			Entity entity = ENTITY_SERIALIZER.jsonLDToEntity(ngsi_ld);
			entityService.save(entity);
			return Response.status(201).build();

		} catch (SgeolServiceException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (ObjectNullException e) {
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
			entityService.update(entity);
			return Response.ok().build();

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (ObjectNullException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (InvalidEntityIdException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (EntityDoesNotExistException e) {
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
			Entity entity = entityService.findById(id);
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
			entityService.delete(id);

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
	public Response addProperty(@QueryParam("entity-id") String entityId, String property,
			@QueryParam("property-name") String propertyId) {

		try {
			entityService.addProperty(entityId, ENTITY_SERIALIZER.jsonToProperty(property), propertyId);

			return Response.ok().build();

		} catch (SgeolServiceException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (ObjectNullException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (PropertyAlreadyExistException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@PUT
	@Path("property")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProperty(@QueryParam("entity-id") String entityId, String property,
			@QueryParam("property-name") String propertyId) {

		try {
			entityService.updateProperty(entityId, ENTITY_SERIALIZER.jsonToProperty(property), propertyId);
			return Response.ok().build();

		} catch (SgeolServiceException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (ObjectNullException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (PropertyDoesNotExistException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (DaoException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@DELETE
	@Path("property")
	public Response removeProperty(@QueryParam("entity-id") String entityId,
			@QueryParam("property-name") String propertyName) {

		try {
			entityService.removeProperty(entityId, propertyName);
			return Response.ok().build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path("property")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findProperty(@QueryParam("entity-id") String entityId,
			@QueryParam("property-name") String propertyName) {

		try {

			Property property = entityService.findProperty(entityId, propertyName);

			if (property != null) {
				return Response.ok(property.toJsonObject().toString(), MediaType.APPLICATION_JSON).build();
			} else {
				return Response.ok("{}", MediaType.APPLICATION_JSON).build();
			}

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (JSONException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path("relationaship")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addRelationaship(@QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName, String relationaship) {

		try {
			entityService.addRelationaship(entityId, relationashipName,
					ENTITY_SERIALIZER.jsonToRelationaship(relationaship));

			return Response.ok().build();

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (ObjectNullException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (EntityDoesNotExistException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@PUT
	@Path("relationaship")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateRelationaship(@QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName, String relationaship) {
		try {
			entityService.updateRelationaship(entityId, relationashipName,
					ENTITY_SERIALIZER.jsonToRelationaship(relationaship));
			return Response.ok().build();

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (ObjectNullException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (RelationashipDoesNotExistException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@DELETE
	@Path("relationaship")
	public Response removeRelationaship(@QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName) {
		try {
			entityService.removeRelationaship(entityId, relationashipName);
			return Response.ok().build();

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path("relationaship")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findRelationaship(@QueryParam("entity-id") String entityId,
			@QueryParam("relationaship-name") String relationashipName) {
		try {
			Relationaship relationaship = entityService.findRelationaship(entityId, relationashipName);

			if (relationaship != null) {
				return Response.ok(relationaship.toJson().toString(), MediaType.APPLICATION_JSON).build();
			} else {
				return Response.ok("{}", MediaType.APPLICATION_JSON).build();
			}
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (JSONException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path("context")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response addContext(@QueryParam("entity-id") String entityId, String context) {
		try {
			entityService.addContext(entityId, context);

			return Response.ok().build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (EntityDoesNotExistException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@DELETE
	@Path("context")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response removeContext(@QueryParam("entity-id") String entityId, String context) {
		try {
			entityService.removeContext(entityId, context);
			return Response.ok().build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path("find-by-property-filter")
	public Response findByPropertyFilter(@QueryParam("field") String field, @QueryParam("operator") String operator,
			@QueryParam("value") String value, @QueryParam("limit") @DefaultValue("1024") int limit,
			@QueryParam("offset") @DefaultValue("0") int offset) {

		try {
			List<Entity> entities = entityService.findByPropertyFilter(field, operator, value, limit, offset);

			JSONArray array = new JSONArray();

			for (Entity entity : entities) {

				array.put(new JSONObject(entity.toNGSI_LD()));
			}

			return Response.ok(array.toString(), MediaType.APPLICATION_JSON).build();

		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	@GET
	@Path("find-by-relationaship-filter")
	public Response findByRelationashipByFilter(@QueryParam("field") String field,
			@QueryParam("operator") String operator, @QueryParam("value") String value,
			@QueryParam("limit") @DefaultValue("1024") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {
		try {
			List<Entity> entities = entityService.findByRelationashipByFilter(field, operator, value, limit, offset);

			JSONArray array = new JSONArray();

			for (Entity entity : entities) {

				array.put(new JSONObject(entity.toNGSI_LD()));
			}

			return Response.ok(array.toString(), MediaType.APPLICATION_JSON).build();

		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (JSONException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	public Response findAll(@QueryParam("limit") @DefaultValue("1024") int limit,
			@QueryParam("offset") @DefaultValue("0") int offset) {

		try {
			List<Entity> entities = entityService.findAll(limit, offset);

			JSONArray array = new JSONArray();

			for (Entity entity : entities) {

				array.put(new JSONObject(entity.toNGSI_LD()));
			}

			if (array.length() > 0) {
				return Response.ok(array.toString(), MediaType.APPLICATION_JSON).build();
			} else {
				return Response.ok("{}", MediaType.APPLICATION_JSON).build();
			}

		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (JSONException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();

		}
	}

	@GET
	@Path("find-by-query")
	public Response findByQuery( @QueryParam("query") String query,
			@QueryParam("limit") @DefaultValue("1024") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {
		try {
			List<Entity> entities = entityService.findByQuery(query, limit, offset);

			JSONArray array = new JSONArray();

			for (Entity entity : entities) {

				array.put(new JSONObject(entity.toNGSI_LD()));
			}

			if (array.length() > 0) {
				return Response.ok(array.toString(), MediaType.APPLICATION_JSON).build();
			} else {

				return Response.ok("{}", MediaType.APPLICATION_JSON).build();
			}

		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (JSONException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("find-by-document")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findByDocumentQuery(@PathParam("layer") String layer, String document,
			@QueryParam("limit") @DefaultValue("1024") int limit, @QueryParam("offset") @DefaultValue("0") int offset) {

		try {
			List<Entity> entities = entityService.findByDocumentQuery(document, limit, offset);

			JSONArray array = new JSONArray();

			for (Entity entity : entities) {

				array.put(new JSONObject(entity.toNGSI_LD()));
			}

			return Response.ok(array.toString(), MediaType.APPLICATION_JSON).build();

		} catch (ServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (JSONException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

}
