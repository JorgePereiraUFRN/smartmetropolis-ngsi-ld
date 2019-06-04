package br.imd.ufrn.test.datasource.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.geotools.ows.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.imd.sgeol.datasource.service.EntityService;
import br.imd.sgeol.exception.DaoException;
import br.imd.sgeol.exception.EntityAlreadyExistException;
import br.imd.sgeol.exception.EntityDoesNotExistException;
import br.imd.sgeol.exception.InvalidEntityIdException;
import br.imd.sgeol.exception.ObjectNullException;
import br.imd.sgeol.exception.SgeolServiceException;
import br.imd.sgeol.model.Entity;
import br.imd.sgeol.model.Geometry;
import br.imd.sgeol.model.PropertyType;

public class EntityServiceTest extends GenericServiceTest {

	private static final Entity entity1 = new Entity();
	private static final EntityService ENTITY_SERVICE = EntityService.getInstance();
	private static final Geometry geometry;

	static {

		entity1.setId(UUID.randomUUID().toString());
		entity1.setType("test");
		entity1.addProperty("prop1", PropertyType.Property, "value1");
		entity1.addProperty("prop2", PropertyType.Property, "20");
		entity1.addRelationaship("relationaship1", "urn:ngsi-ld:layer:layer1");

		geometry = new Geometry();
		geometry.setEntityId(entity1.getId());

		try {
			JSONObject point = new JSONObject();
			point.put("type", "Point");
			point.put("coordinates", new JSONArray(Arrays.asList(new String[] { "10", "30" })));

			geometry.setGeoJson(point);

			entity1.addGeometry(geometry);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public EntityServiceTest() {
		super(entity1, ENTITY_SERVICE);
	}

	@Before
	public void setUp() throws Exception {
		ENTITY_SERVICE.save(entity1);
	}

	@After
	public void tearDown() throws Exception {
		ENTITY_SERVICE.delete(entity1.getId());
	}

	@Test
	public void testSave() throws IOException, JSONException, SgeolServiceException, 
			ObjectNullException, InvalidEntityIdException, EntityAlreadyExistException,
			DaoException {

		Entity entity = new Entity();
		entity.setType("test");
		entity.setId(UUID.randomUUID().toString());
		entity.setType("test");
		entity.addContext(
				"https://forge.etsi.org/gitlab/NGSI-LD/NGSI-LD/raw/master/coreContext/ngsi-ld-core-context.json");
		entity.addProperty("prop1", PropertyType.Property, "value1");
		entity.addGeometry(geometry);

		ENTITY_SERVICE.save(entity);

		Entity finded = ENTITY_SERVICE.findById(entity.getId());

		assertEquals(entity.getProperty("prop1").toJsonObject().toString(),
				finded.getProperty("prop1").toJsonObject().toString());
		assertEquals(entity.getProperty("location").toJsonObject().getJSONObject("value").get("coordinates").toString(),
				finded.getProperty("location").toJsonObject().getJSONObject("value").get("coordinates").toString());

		ENTITY_SERVICE.delete(entity.getId());
	}

	@Test
	public void testUpdate()
			throws SgeolServiceException, ObjectNullException, 
			InvalidEntityIdException, DaoException, EntityDoesNotExistException, JSONException{

		entity1.removeProperty("prop1");
		entity1.addProperty("prop1", PropertyType.Property, "new_value");

		ENTITY_SERVICE.update(entity1);

		Entity finded = ENTITY_SERVICE.findById(entity1.getId());

		assertEquals(entity1.getProperty("prop1").toJsonObject().toString(),
				finded.getProperty("prop1").toJsonObject().toString());
	}

	@Test
	public void testFindById() throws SgeolServiceException, DaoException {

		Entity finded = ENTITY_SERVICE.findById(entity1.getId());

		assertNotNull(finded);

	}

	@Test
	public void testDelete() throws SgeolServiceException, DaoException {
		ENTITY_SERVICE.delete(entity1.getId());

		Entity finded = ENTITY_SERVICE.findById(entity1.getId());

		assertNull(finded);
	}

	@Test
	public void testFindByPropertyFilter() throws IOException, JSONException, SgeolServiceException,
			 ObjectNullException, InvalidEntityIdException,
			EntityAlreadyExistException, DaoException {

		Entity entity = new Entity();
		entity.setType("test");
		entity.setId(UUID.randomUUID().toString());
		entity.addProperty("prop1", PropertyType.Property, "value1");
		entity.addProperty("prop2", PropertyType.Property, "25");
		entity.addRelationaship("relationaship1", "urn:ngsi-ld:layer:layer2");
		entity.addGeometry(geometry);

		ENTITY_SERVICE.save(entity);

		List<Entity> entities = ENTITY_SERVICE.findByPropertyFilter("prop2.value", "gt", "23", 10, 0);

		assertEquals(1, entities.size());

		entities = ENTITY_SERVICE.findByPropertyFilter("prop2.value", "lt", "26", 10, 0);

		assertEquals(2, entities.size());

		ENTITY_SERVICE.delete(entity.getId());

	}

	@Test
	public void testFindByRelationashipByFilter() throws IOException, JSONException, SgeolServiceException,
			EntityAlreadyExistException, DaoException, ObjectNullException, InvalidEntityIdException {
		
		Entity entity = new Entity();
		entity.setType("test");
		entity.setId(UUID.randomUUID().toString());
		entity.addProperty("prop1", PropertyType.Property, "value1");
		entity.addProperty("prop2", PropertyType.Property, "25");
		entity.addRelationaship("relationaship1", "urn:ngsi-ld:layer:layer2");
		entity.addGeometry(geometry);

		ENTITY_SERVICE.save(entity);

		List<Entity> entities = ENTITY_SERVICE.findByRelationashipByFilter("relationaship1.object", "eq",
				"urn:ngsi-ld:layer:layer1", 10, 0);
		assertEquals(1, entities.size());

		entities = ENTITY_SERVICE.findByRelationashipByFilter("relationaship1.object", "eq",
				"urn:ngsi-ld:layer:layer2", 10, 0);
		assertEquals(1, entities.size());

		ENTITY_SERVICE.delete(entity.getId());
	}

	@Test
	public void testFindByQuery() throws IOException, JSONException, SgeolServiceException, InvalidEntityIdException, EntityAlreadyExistException,
			DaoException, ObjectNullException {

		String query = "p*.prop2.value$gt$24;r*.relationaship1.object$eq$urn:ngsi-ld:layer:layer2";

		Entity entity = new Entity();
		entity.setType("test");
		entity.setId(UUID.randomUUID().toString());
		entity.addProperty("prop1", PropertyType.Property, "value1");
		entity.addProperty("prop2", PropertyType.Property, "25");
		entity.addRelationaship("relationaship1", "urn:ngsi-ld:layer:layer2");
		entity.addGeometry(geometry);

		ENTITY_SERVICE.save(entity);

		List<Entity> entities = ENTITY_SERVICE.findByQuery(query, 10, 0);

		assertEquals(1, entities.size());
		assertEquals(entity.getId(), entities.get(0).getId());

		ENTITY_SERVICE.delete(entity.getId());
	}

	

	@Test
	public void testFindDocumentByQuery() throws IOException, JSONException, SgeolServiceException,
			 ObjectNullException,  InvalidEntityIdException,
			EntityAlreadyExistException, DaoException, ServiceException {

		Entity entity = new Entity();
		entity.setType("test");
		entity.setId(UUID.randomUUID().toString());
		entity.addProperty("prop1", PropertyType.Property, "value1");
		entity.addProperty("prop2", PropertyType.Property, "25");
		entity.addRelationaship("relationaship1", "urn:ngsi-ld:layer:layer2");
		entity.addGeometry(geometry);

		ENTITY_SERVICE.save(entity);

		List<Document> consulta = new ArrayList<Document>();
		consulta.add(new Document("properties.prop2.value", new Document("$lte", 20)));
		consulta.add(new Document("properties.prop2.value", new Document("$gte", 25)));

		Document query = new Document();
		query.append("$or", consulta);

		List<Entity> entities = ENTITY_SERVICE.findByDocumentQuery(query.toJson(), 10, 0);

		assertEquals(2, entities.size());

		ENTITY_SERVICE.delete(entity.getId());
	}

}
