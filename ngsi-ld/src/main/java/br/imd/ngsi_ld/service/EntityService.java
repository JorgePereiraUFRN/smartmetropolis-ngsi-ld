package br.imd.ngsi_ld.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.geotools.ows.ServiceException;

import br.imd.ngsi_ld.dao.MongodbUtil;
import br.imd.ngsi_ld.dao.NgsiLdDao;
import br.imd.ngsi_ld.dao.NgsiLdDaoInterface;
import br.imd.ngsi_ld.exception.DaoException;
import br.imd.ngsi_ld.exception.EntityAlreadyExistException;
import br.imd.ngsi_ld.exception.EntityDoesNotExistException;
import br.imd.ngsi_ld.exception.InvalidEntityIdException;
import br.imd.ngsi_ld.exception.ObjectNullException;
import br.imd.ngsi_ld.exception.PropertyAlreadyExistException;
import br.imd.ngsi_ld.exception.PropertyDoesNotExistException;
import br.imd.ngsi_ld.exception.RelationashipAlreadyExistException;
import br.imd.ngsi_ld.exception.RelationashipDoesNotExistException;
import br.imd.ngsi_ld.exception.SgeolServiceException;
import br.imd.ngsi_ld.model.Entity;
import br.imd.ngsi_ld.model.Property;
import br.imd.ngsi_ld.model.Relationaship;

public class EntityService {

	private static final EntityService ENTITY_SERVICE = new EntityService();
	private NgsiLdDaoInterface ngsiLdDao = new NgsiLdDao();

	private EntityService() {

	}

	public static EntityService getInstance() {
		return ENTITY_SERVICE;
	}
	
	protected void validateEntity(Entity entity)
			throws ObjectNullException, InvalidEntityIdException {

		if (entity == null) {
			throw new ObjectNullException("The entity is null.");
		}

		if (entity.getId() == null || entity.getId().isEmpty()) {
			throw new InvalidEntityIdException("The entity name is inavalid");
		}

	}

	protected void validateProperty(Property prop) throws ObjectNullException {

		if (prop == null || prop.getValue() == null) {
			throw new ObjectNullException("The property value is null");
		}

		if (prop.getType() == null || prop.getType().isEmpty()) {
			throw new ObjectNullException("The property type value is null");
		}
	}

	protected boolean propertyExist(String entityId, String propertyName)
			throws SgeolServiceException, DaoException {

		Property property = findProperty(entityId, propertyName);

		if (property != null) {
			return true;
		} else {
			return false;
		}

	}

	protected void validateRelationaship(Relationaship rel) throws ObjectNullException {

		if (rel == null || rel.getObjetc() == null) {
			throw new ObjectNullException("The relationaship value is null");
		}
	}

	protected boolean relationashipExist(String entityId, String relationashipName)
			throws SgeolServiceException, DaoException {

		Relationaship relationaship = findRelationaship(entityId, relationashipName);

		if (relationaship != null) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean entityExist(String entityId) throws DaoException {

		Entity entityMongo = ngsiLdDao.findById(entityId);

		if (entityMongo == null) {
			return false;
		}

		return true;
	}

	public void save(Entity entity) throws SgeolServiceException, ObjectNullException,
			InvalidEntityIdException, EntityAlreadyExistException, DaoException {

		validateEntity(entity);

		if (entityExist(entity.getId())) {
			throw new EntityAlreadyExistException("The entity whit id: " + entity.getId() + " already exist.");
		}

		try {
			ngsiLdDao.save(entity);

		} catch (Exception e) {
			e.printStackTrace();

			ngsiLdDao.delete(entity.getId());

			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public void update(Entity entity) throws SgeolServiceException, ObjectNullException,
			InvalidEntityIdException, DaoException, EntityDoesNotExistException {

		validateEntity(entity);

		if (!entityExist(entity.getId())) {
			throw new EntityDoesNotExistException("The entity whit id " + entity.getId() + " does not exist");
		}
		try {

			ngsiLdDao.update(entity);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public Entity findById(String id) throws SgeolServiceException, DaoException {
		try {
			return ngsiLdDao.findById(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}
	}

	public void delete(String id) throws SgeolServiceException, DaoException {
		try {
			ngsiLdDao.delete(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}
	

	public void addProperty(String entityId, Property property, String propertyId)
			throws SgeolServiceException, DaoException, ObjectNullException, PropertyAlreadyExistException {

		validateProperty(property);

		if (propertyExist(entityId, propertyId)) {
			throw new PropertyAlreadyExistException(
					"the property with name " + propertyId + " in entity " + entityId + " already exist.");
		}

		try {
			if (entityExist(entityId)) {

				ngsiLdDao.addProperty(entityId, property, propertyId);

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public void updateProperty(String entityId, Property property, String propertyId)
			throws SgeolServiceException, ObjectNullException, DaoException, PropertyDoesNotExistException {

		validateProperty(property);
		try {
			ngsiLdDao.updateProperty(entityId, property, propertyId);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}
	}

	public void removeProperty(String entityId, String propertyName) throws SgeolServiceException, DaoException {

		try {
			ngsiLdDao.removeProperty(entityId, propertyName);

		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public Property findProperty(String entityId, String propertyName) throws SgeolServiceException, DaoException {

		try {
			return ngsiLdDao.findProperty(entityId, propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}
	}

	public void addRelationaship(String entityId, String relationashipName, Relationaship relationaship)
			throws SgeolServiceException, ObjectNullException, DaoException, EntityDoesNotExistException {
		try {
			validateRelationaship(relationaship);

			if (relationashipExist(entityId, relationashipName)) {
				throw new RelationashipAlreadyExistException("This relationasip already exist");
			}

			if (!entityExist(entityId)) {

				throw new EntityDoesNotExistException("The entity whit id " + entityId + " does not exist");
			}

			ngsiLdDao.addRelationaship(entityId, relationashipName, relationaship);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public void updateRelationaship(String entityId, String relationashipName, Relationaship relationaship)
			throws SgeolServiceException, DaoException, ObjectNullException, RelationashipDoesNotExistException {

		try {
			validateRelationaship(relationaship);

			if (relationashipExist(entityId, relationashipName)) {
				ngsiLdDao.updateRelationaship(entityId, relationashipName, relationaship);
			} else {
				throw new RelationashipDoesNotExistException(
						"The relationaship " + relationashipName + " in entity " + entityId + " does not exist.");
			}
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public void removeRelationaship(String entityId, String relationashipName)
			throws SgeolServiceException, DaoException {
		try {
			ngsiLdDao.removeRelationaship(entityId, relationashipName);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public Relationaship findRelationaship(String entityId, String relationashipName)
			throws SgeolServiceException, DaoException {
		try {
			return ngsiLdDao.findRelationaship(entityId, relationashipName);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}
	}

	public void addContext(String entityId, String context)
			throws SgeolServiceException, DaoException, EntityDoesNotExistException {
		try {
			if (entityExist(entityId)) {
				ngsiLdDao.addContext(entityId, context);
			} else {
				throw new EntityDoesNotExistException("This entity does not exist");
			}
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}
	}

	public void removeContext(String entityId, String context) throws SgeolServiceException {
		try {
			ngsiLdDao.removeContext(entityId, context);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public List<Entity> findByPropertyFilter(String field, String operator, String value, int limit, int offset)
			throws DaoException, SgeolServiceException {
		try {
			return ngsiLdDao.findByPropertyFilter(field, operator, value, limit, offset);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public List<Entity> findByRelationashipByFilter(String field, String operator, String value, int limit, int offset)
			throws DaoException, SgeolServiceException {
		try {
			return ngsiLdDao.findByRelationashipByFilter(field, operator, value, limit, offset);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public List<Entity> findAll(int limit, int offset) throws DaoException, SgeolServiceException {
		try {
			return ngsiLdDao.findAll(limit, offset);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public List<Entity> findByQuery(String query, int limit, int offset) throws DaoException, SgeolServiceException {
		try {
			return ngsiLdDao.findByQuery(query, limit, offset);
		} catch (Exception e) {
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}
	}

	public List<Entity> findByDocumentQuery(String query, int limit, int offset) throws DaoException, ServiceException {

		try {
			JSONObject JsonQuery = new JSONObject(query);

			return ngsiLdDao.findByDocumentQuery(JsonQuery, limit, offset);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
