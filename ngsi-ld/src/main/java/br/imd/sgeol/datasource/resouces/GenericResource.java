package br.imd.sgeol.datasource.resouces;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.geotools.ows.ServiceException;

import br.imd.sgeol.datasource.service.GenericService;
import br.imd.sgeol.exception.DaoException;
import br.imd.sgeol.exception.EntityDoesNotExistException;
import br.imd.sgeol.exception.ObjectNullException;
import br.imd.sgeol.exception.PropertyAlreadyExistException;
import br.imd.sgeol.exception.PropertyDoesNotExistException;
import br.imd.sgeol.exception.RelationashipDoesNotExistException;
import br.imd.sgeol.exception.SgeolServiceException;
import br.imd.sgeol.model.Entity;
import br.imd.sgeol.model.Property;
import br.imd.sgeol.model.Relationaship;
import br.imd.sgeol.util.EntitySerializer;

public abstract class GenericResource {

	protected final GenericService genericService;
	protected static final EntitySerializer ENTITY_SERIALIZER = new EntitySerializer();

	public GenericResource(final GenericService genericService) {
		this.genericService = genericService;
	}

	protected Response addProperty(String entityId, String property, String propertyId) {

		try {
			genericService.addProperty(entityId, ENTITY_SERIALIZER.jsonToProperty(property),
					propertyId);

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

	protected Response updateProperty(String entityId, String property, String propertyId) {

		try {
			genericService.updateProperty(entityId, ENTITY_SERIALIZER.jsonToProperty(property),
					propertyId);
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

	protected Response removeProperty(String entityId, String propertyName) {

		try {
			genericService.removeProperty(entityId, propertyName);
			return Response.ok().build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	protected Response findProperty(String entityId, String propertyName) {

		try {

			Property property = genericService.findProperty(entityId, propertyName);

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

	protected Response addRelationaship(String entityId, String relationashipName,
			String relationaship) {

		try {
			genericService.addRelationaship(entityId, relationashipName,
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

	protected Response updateRelationaship( String entityId, String relationashipName,
			String relationaship) {

		try {
			genericService.updateRelationaship(entityId, relationashipName,
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

	protected Response removeRelationaship(String entityId, String relationashipName) {

		try {
			genericService.removeRelationaship(entityId, relationashipName);
			return Response.ok().build();

		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	protected Response findRelationaship(String entityId, String relationashipName) {

		try {
			Relationaship relationaship = genericService.findRelationaship(entityId, relationashipName);

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

	protected Response addContext(String entityId, String context) {

		try {
			genericService.addContext(entityId, context);

			return Response.ok().build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (DaoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		} catch (EntityDoesNotExistException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	protected Response removeContext(String entityId, String context) {

		try {
			genericService.removeContext(entityId, context);
			return Response.ok().build();
		} catch (SgeolServiceException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	protected Response findByPropertyFilter(String field, String operator, String value,
			int limit, int offset) {

		try {
			List<Entity> entities = genericService.findByPropertyFilter(field, operator, value, limit,
					offset);

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

	protected Response findByRelationashipByFilter(String field, String operator, String value,
			int limit, int offset) {

		try {
			List<Entity> entities = genericService.findByRelationashipByFilter(field, operator, value,
					limit, offset);

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

	protected Response findAll(int limit, int offset) {

		try {
			List<Entity> entities = genericService.findAll(limit, offset);

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

	public Response findByQuery( String query, int limit, int offset) {
		try {
			List<Entity> entities = genericService.findByQuery(query, limit, offset);

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
	
	

	public Response findByDocumentQuery(String document, int limit, int offset) {

		try {
			List<Entity> entities = genericService.findByDocumentQuery(document, limit, offset);

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
