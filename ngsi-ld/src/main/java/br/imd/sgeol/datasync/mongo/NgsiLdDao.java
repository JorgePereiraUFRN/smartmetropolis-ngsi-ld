package br.imd.sgeol.datasync.mongo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import br.imd.sgeol.exception.DaoException;
import br.imd.sgeol.exception.EntitySerializerException;
import br.imd.sgeol.exception.ObjectNullException;
import br.imd.sgeol.model.Entity;
import br.imd.sgeol.model.Property;
import br.imd.sgeol.model.PropertyType;
import br.imd.sgeol.model.Relationaship;
import br.imd.sgeol.util.DataTypeUtil;
import br.imd.sgeol.util.DateFormatUtil;
import br.imd.sgeol.util.EntitySerializer;
//import groovy.transform.builder.InitializerStrategy.SET;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class NgsiLdDao implements NgsiLdDaoInterface {
	
	private static final String collectionName  = "ngsi_ld";

	public NgsiLdDao() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#save(java.lang.String,
	 * ngsi_ld.model.Entity)
	 */
	public void save(Entity entity) throws DaoException {
		try {
			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			Document document = Document.parse(entity.toJson());
			document.put("createdAt", new Date());
			document.remove("modifiedAt");
			document.put("_id", entity.getId());
			// document.remove("id");

			collection.insertOne(document);

		} catch (JsonProcessingException e) {
			throw new DaoException(e.getMessage());
		} catch (JSONException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#update(java.lang.String,
	 * ngsi_ld.model.Entity)
	 */
	public void update(Entity entity) throws DaoException {

		try {
			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			Document document = Document.parse(entity.toJson());

			document.put("createdAt", findById(entity.getId()).getCreatedAt());

			document.remove("modifiedAt");
			document.put("modifiedAt", new Date());

			collection.replaceOne(new Document("_id", entity.getId()), document, new UpdateOptions().upsert(true));

		} catch (JsonProcessingException e) {
			throw new DaoException(e.getMessage());
		} catch (JSONException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#findById(java.lang.
	 * String, java.lang.String)
	 */
	public Entity findById(String id) throws DaoException {
		try {
			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			Document document = collection.find(Filters.eq("_id", id)).first();

			if (document != null) {

				Entity entity = new EntitySerializer().documentToEntity(document);

				return entity;
			} else {
				return null;
			}

		} catch (ParseException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		} catch (EntitySerializerException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		} catch (ObjectNullException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		}

	}

	
	public List<Entity> findByPropertyFilter(String field, String operator, String value,
			int limit, int offset) throws DaoException {

		List<Entity> ret = new ArrayList<Entity>();
		try {
			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			Document queryDoc = new Document();

			if (DataTypeUtil.isBoolean(value)) {
				queryDoc.append("properties." + field, new Document("$" + operator, Boolean.parseBoolean(value)));
			} else if (DataTypeUtil.isDate(value)) {
				queryDoc.append("properties." + field, new Document("$" + operator, DateFormatUtil.parse(value)));
			} else if (DataTypeUtil.isInteger(value)) {
				queryDoc.append("properties." + field, new Document("$" + operator, Integer.parseInt(value)));
			} else if (DataTypeUtil.isDouble(value)) {
				queryDoc.append("properties." + field, new Document("$" + operator, Double.parseDouble(value)));
			} else {
				queryDoc.append("properties." + field, new Document("$" + operator, value));
			}

			FindIterable<Document> entities = collection.find(queryDoc).skip(offset).limit(limit);

			MongoCursor<Document> cursor = entities.iterator();

			EntitySerializer serializer = new EntitySerializer();

			try {
				while (cursor.hasNext()) {

					ret.add(serializer.documentToEntity(cursor.next()));

				}
			} finally {
				cursor.close();
			}
		} catch (ParseException e) {
			throw new DaoException(e.getMessage());
		} catch (EntitySerializerException e) {
			throw new DaoException(e.getMessage());
		} catch (ObjectNullException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		}

		return ret;
	}

	public List<Entity> findByRelationashipByFilter( String field, String operator, String value,
			int limit, int offset) throws DaoException {

		List<Entity> ret = new ArrayList<Entity>();
		try {
			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			Document queryDoc = new Document();
			if (DataTypeUtil.isBoolean(value)) {
				queryDoc.append("relationaships." + field, new Document("$" + operator, Boolean.parseBoolean(value)));
			} else if (DataTypeUtil.isDate(value)) {
				queryDoc.append("relationaships." + field, new Document("$" + operator, DateFormatUtil.parse(value)));
			} else if (DataTypeUtil.isDouble(value)) {
				queryDoc.append("relationaships." + field, new Document("$" + operator, Double.parseDouble(value)));
			} else if (DataTypeUtil.isInteger(value)) {
				queryDoc.append(field, new Document("$" + operator, Integer.parseInt(value)));
			} else {
				queryDoc.append("relationaships." + field, new Document("$" + operator, value));
			}

			FindIterable<Document> entities = collection.find(queryDoc).skip(offset).limit(limit);

			MongoCursor<Document> cursor = entities.iterator();

			EntitySerializer serializer = new EntitySerializer();

			try {
				while (cursor.hasNext()) {

					ret.add(serializer.documentToEntity(cursor.next()));

				}
			} finally {
				cursor.close();
			}
		} catch (ParseException e) {
			throw new DaoException(e.getMessage());
		} catch (EntitySerializerException e) {
			throw new DaoException(e.getMessage());
		} catch (ObjectNullException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		}

		return ret;
	}

	public List<Entity> findAll(int limit, int offset) throws DaoException {

		List<Entity> ret = new ArrayList<Entity>();
		try {
			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			FindIterable<Document> entities = collection.find().limit(limit).skip(offset);

			MongoCursor<Document> cursor = entities.iterator();

			EntitySerializer serializer = new EntitySerializer();

			try {
				while (cursor.hasNext()) {
					Document document = cursor.next();

					Entity entiity = serializer.documentToEntity(document);

					ret.add(entiity);

				}
			} finally {
				cursor.close();
			}
		} catch (ParseException e) {
			throw new DaoException(e.getMessage());
		} catch (EntitySerializerException e) {
			throw new DaoException(e.getMessage());
		} catch (ObjectNullException e) {
			e.printStackTrace();
			throw new DaoException(e.getMessage());
		}

		return ret;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#delete(java.lang.String,
	 * java.lang.String)
	 */
	public void delete( String id) throws DaoException {

		MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

		collection.deleteOne(new Document("_id", id));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#addProperty(java.lang.
	 * String, java.lang.String, ngsi_ld.model.Property, java.lang.String)
	 */
	public void addProperty(String entityId, Property property, String propertyId)
			throws DaoException {

		MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

		try {
			Bson filter = new Document("_id", entityId);
			Bson newValue;

			newValue = new Document("properties." + propertyId, Document.parse(property.toJsonObject().toString()));

			Bson updateOperationDocument = new Document("$set", newValue);

			collection.updateOne(filter, updateOperationDocument);
		} catch (JSONException e) {
			throw new DaoException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#updateProperty(java.lang
	 * .String, java.lang.String, ngsi_ld.model.Property, java.lang.String)
	 */
	public void updateProperty(String entityId, Property property, String propertyId)
			throws DaoException {

		MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

		try {
			Bson filter = new Document("_id", entityId);
			Bson newValue = new Document("properties." + propertyId,
					Document.parse(property.toJsonObject().toString()));

			Bson updateOperationDocument = new Document("$set", newValue);

			collection.updateOne(filter, updateOperationDocument, new UpdateOptions().upsert(false));

		} catch (JSONException e) {
			throw new DaoException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#removeProperty(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public void removeProperty(String entityId, String propertyName) throws DaoException {
		/*
		 * Entity entity = findById(collectionName, entityId);
		 * 
		 * entity.removeProperty(propertyName); update(collectionName, entity);
		 */

		MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

		Bson filter = new Document("_id", entityId);
		Bson removedProp = new Document("properties." + propertyName, "");
		Bson updateOperationDocument = new Document("$unset", removedProp);

		collection.updateOne(filter, updateOperationDocument);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#addRelationaship(java.
	 * lang.String, java.lang.String, java.lang.String,
	 * ngsi_ld.model.Relationaship)
	 */
	public void addRelationaship( String entityId, String relationashipName,
			Relationaship relationaship) throws DaoException {

		Entity entity = findById(entityId);

		entity.addRelationaship(relationashipName, relationaship);

		update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#updateRelationaship(java
	 * .lang.String, java.lang.String, java.lang.String,
	 * ngsi_ld.model.Relationaship)
	 */
	public void updateRelationaship(String entityId, String relationashipName,
			Relationaship relationaship) throws DaoException {

		Entity entity = findById(entityId);

		entity.removeRelationaship(relationashipName);
		entity.addRelationaship(relationashipName, relationaship);

		update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#removeRelationaship(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	public void removeRelationaship(String entityId, String relationashipId)
			throws DaoException {
		Entity entity = findById(entityId);

		entity.removeRelationaship(relationashipId);

		update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#addContext(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	public void addContext( String entityId, String context) throws DaoException {
		Entity entity = findById(entityId);

		entity.addContext(context);

		update(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.imd.sgeol.datasource.mongo.NgsiLdDaoInterface#removeContext(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	public void removeContext(String entityId, String context) throws DaoException {
		Entity entity = findById( entityId);

		entity.removeContext(context);

		update( entity);
	}

	public Property findProperty( String entityId, String propertyName) throws DaoException {

		Entity entity = findById(entityId);

		if (entity != null) {
			return entity.getProperty(propertyName);
		} else {
			return null;
		}

	}

	public Relationaship findRelationaship( String entityId, String relationashipName)
			throws DaoException {

		Entity entity = findById(entityId);

		if (entity != null) {
			return entity.getRelationaship(relationashipName);
		} else {
			return null;
		}

	}

	public List<Entity> findByDocumentQuery(JSONObject query, int limit, int offset)
			throws DaoException {
		List<Entity> entities = new ArrayList<Entity>();
		try {

			Document queryDoc = Document.parse(query.toString());

			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			FindIterable<Document> documents = collection.find(queryDoc).limit(limit).skip(offset);

			for (Document doc : documents) {

				Entity entity = new EntitySerializer().documentToEntity(doc);

				entities.add(entity);

			}
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}

		return entities;
	}

	public List<Entity> findByQuery(String query, int limit, int offset) throws DaoException {

		List<Entity> entities = new ArrayList<Entity>();

		try {
			query = query.replace("p*", "properties");
			query = query.replace("r*", "relationaships");

			String[] queries = query.split(";");

			Document queryDoc = new Document();

			for (String q : queries) {

				String[] queryTerms = q.split("\\$", 3);

				String field = queryTerms[0];
				String operator = queryTerms[1];
				String value = queryTerms[2];

				if (DataTypeUtil.isBoolean(value)) {
					if (queryDoc.get(field) == null) {
						queryDoc.append(field, new Document("$" + operator, Boolean.parseBoolean(value)));
					} else {
						((Document) queryDoc.get(field)).append("$" + operator, Boolean.parseBoolean(value));
					}
				} else if (DataTypeUtil.isDate(value)) {
					if (queryDoc.get(field) == null) {
						queryDoc.append(field, new Document("$" + operator, DateFormatUtil.parse(value)));
					} else {
						((Document) queryDoc.get(field)).append("$" + operator, DateFormatUtil.parse(value));
					}
				} else if (DataTypeUtil.isDouble(value)) {
					if (queryDoc.get(field) == null) {
						queryDoc.append(field, new Document("$" + operator, Double.parseDouble(value)));
					} else {
						((Document) queryDoc.get(field)).append("$" + operator, Double.parseDouble(value));
					}
				} else if (DataTypeUtil.isInteger(value)) {
					if (queryDoc.get(field) == null) {
						queryDoc.append(field, new Document("$" + operator, Integer.parseInt(value)));
					} else {
						((Document) queryDoc.get(field)).append("$" + operator, Integer.parseInt(value));
					}
				} else {
					if (queryDoc.get(field) == null) {
						queryDoc.append(field, new Document("$" + operator, value));
					} else {
						((Document) queryDoc.get(field)).append("$" + operator, value);
					}
				}

			}

			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			FindIterable<Document> documents = collection.find(queryDoc).limit(limit).skip(offset);

			for (Document doc : documents) {

				Entity entity = new EntitySerializer().documentToEntity(doc);

				entities.add(entity);

			}
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}

		return entities;

	}

	

}
