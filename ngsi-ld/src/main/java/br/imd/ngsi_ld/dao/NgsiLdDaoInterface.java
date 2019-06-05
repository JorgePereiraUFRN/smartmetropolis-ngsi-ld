package br.imd.ngsi_ld.dao;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import br.imd.ngsi_ld.exception.DaoException;
import br.imd.ngsi_ld.model.Entity;
import br.imd.ngsi_ld.model.Property;
import br.imd.ngsi_ld.model.Relationaship;

public interface NgsiLdDaoInterface {

	void save(Entity entity) throws DaoException;

	void update(Entity entity) throws DaoException;

	Entity findById(String id) throws DaoException;

	void delete(String id) throws DaoException;

	void addProperty(String entityId, Property property, String propertyId) throws DaoException;

	void updateProperty(String entityId, Property property, String propertyId) throws DaoException;

	void removeProperty(String entityId, String propertyName) throws DaoException;
	
	Property findProperty(String entityId, String propertyName) throws DaoException;

	void addRelationaship(String entityId, String relationashipName, Relationaship relationaship)
			throws DaoException;

	void updateRelationaship(String entityId, String relationashipName, Relationaship relationaship)
			throws DaoException;

	void removeRelationaship(String entityId, String relationashipName) throws DaoException;
	
	Relationaship findRelationaship(String entityId, String relationashipName) throws DaoException;

	void addContext(String entityId, String context) throws DaoException;

	void removeContext(String entityId, String context) throws DaoException;

	List<Entity> findByDocumentQuery(JSONObject query, int limit, int offset) throws DaoException;
	
	List<Entity> findByPropertyFilter(String field, String operator, String value,
			int limit, int offset) throws DaoException;
	
	List<Entity> findByRelationashipByFilter(String field, String operator, String value,
			int limit, int offset) throws DaoException;
	
	List<Entity> findAll(int limit, int offset) throws DaoException;
	
	public List<Entity> findByQuery(String query,  int limit, int offset) throws DaoException;
	
		
}