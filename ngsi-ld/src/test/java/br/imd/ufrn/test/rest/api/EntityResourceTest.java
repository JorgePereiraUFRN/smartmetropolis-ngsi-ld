package br.imd.ufrn.test.rest.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.bson.Document;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.imd.ngsi_ld.model.Entity;
import br.imd.ngsi_ld.model.Geometry;
import br.imd.ngsi_ld.model.Property;
import br.imd.ngsi_ld.model.PropertyType;
import br.imd.ngsi_ld.model.Relationaship;
import io.restassured.RestAssured;

public class EntityResourceTest {

	private static final Entity entity;

	private static TimeZone tz = TimeZone.getTimeZone("UTC");
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");

	static {
		entity = new Entity();

		entity.setId("urn:ngsi-ld:entity_test:" + UUID.randomUUID().toString());
		entity.setType("entity_test");
		entity.addContext(
				"https://forge.etsi.org/gitlab/NGSI-LD/NGSI-LD/raw/master/coreContext/ngsi-ld-core-context.json");
		entity.addProperty("prop1", PropertyType.Property, "value1");
		entity.addProperty("prop2", PropertyType.Property, "20");
		entity.addRelationaship("escola", "urn:ngsi-ld:escola:12345567");

		Geometry geometry = new Geometry();
		geometry.setEntityId(entity.getId());

		try {
			JSONObject point = new JSONObject();
			point.put("type", "Point");
			point.put("coordinates", new JSONArray(Arrays.asList(new String[] { "10", "30" })));

			geometry.setGeoJson(point);

			df.setTimeZone(tz);

			entity.addGeometry(geometry);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Before
	public void setUp() throws Exception {

		given().contentType(MediaType.APPLICATION_JSON).body(entity.toNGSI_LD()).when()
				.post("/ngsi-ld/ws/smartmetropolis");
	}

	@After
	public void tearDown() throws Exception {
		given().when().delete("/ngsi-ld/ws/smartmetropolis?entity-id=" + entity.getId());
	}

	@Test
	public void testAddProperty() throws JSONException {
		Property property = new Property();

		property.setType(PropertyType.Property);
		property.setValue("new value");

		given().contentType(MediaType.APPLICATION_JSON).body(property.toJsonObject().toString()).when()
				.post("/ngsi-ld/ws/smartmetropolis/property?entity-id=" + entity.getId() + "&property-name=propx")
				.then().assertThat().statusCode(200);

	}

	@Test
	public void testUpdateProperty() throws JSONException {
		Property property = new Property();

		property.setType(PropertyType.Property);
		property.setValue("new value");

		given().contentType(MediaType.APPLICATION_JSON).body(property.toJsonObject().toString()).when()
				.put("/ngsi-ld/ws/smartmetropolis/property?entity-id=" + entity.getId() + "&property-name=prop1")
				.then().assertThat().statusCode(200);

	}

	@Test
	public void testRemoveProperty() {
		given().when()
				.delete("/ngsi-ld/ws/smartmetropolis/property?entity-id=" + entity.getId() + "&property-name=prop1")
				.then().assertThat().statusCode(200);
	}

	@Test
	public void testFindProperty() {
		given().when()
				.get("/ngsi-ld/ws/smartmetropolis/property?entity-id=" + entity.getId() + "&property-name=prop1")
				.then().contentType(MediaType.APPLICATION_JSON).body("value", equalTo("value1"));

	}

	@Test
	public void testAddRelationaship() throws JSONException {
		Relationaship relationaship = new Relationaship();
		relationaship.setObjetc("urn:ngsi-ld:layer:layerx");

		given().contentType(MediaType.APPLICATION_JSON).body(relationaship.toJson().toString()).when().post(
				"/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=" + entity.getId() + "&relationaship-name=rel1")
				.then().assertThat().statusCode(200);

	}

	@Test
	public void testUpdateRelationaship() throws JSONException {
		Relationaship relationaship = new Relationaship();
		relationaship.setObjetc("urn:ngsi-ld:layer:layer_z");

		given().contentType(MediaType.APPLICATION_JSON).body(relationaship.toJson().toString()).when()
				.put("/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=" + entity.getId()
						+ "&relationaship-name=escola")
				.then().assertThat().statusCode(200);

	}

	@Test
	public void testRemoveRelationaship() {
		given().when().delete("/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=" + entity.getId()
				+ "&relationaship-name=escola1").then().assertThat().statusCode(200);

	}

	@Test
	public void testFindRelationaship() {
		given().when()
				.get("/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=" + entity.getId()
						+ "&relationaship-name=escola")
				.then().contentType(MediaType.APPLICATION_JSON).body("object", equalTo("urn:ngsi-ld:escola:12345567"));

	}

	@Test
	public void testAddContext() {
		given().contentType(MediaType.TEXT_PLAIN)
				.body("https://forge.etsi.org/gitlab/NGSI-LD/NGSI-LD/raw/master/coreContext/ngsi-ld-core-context.json")
				.when().post("/ngsi-ld/ws/smartmetropolis/context?entity-id=" + entity.getId()).then().assertThat()
				.statusCode(200);
	}

	@Test
	public void testRemoveContext() {
		given().contentType(MediaType.TEXT_PLAIN)
				.body("https://forge.etsi.org/gitlab/NGSI-LD/NGSI-LD/raw/master/coreContext/ngsi-ld-core-context.json")
				.when().delete("/ngsi-ld/ws/smartmetropolis/context?entity-id=" + entity.getId()).then().assertThat()
				.statusCode(200);

	}

	@Test
	public void testFindByPropertyFilter() {
		given().when()
				.get("/ngsi-ld/ws/smartmetropolis/find-by-property-filter?field=prop2.value&operator=gt&value=10")
				.then().contentType(MediaType.APPLICATION_JSON).body("id", hasItems(entity.getId()));

	}

	@Test
	public void testFindByRelationashipByFilter() {
		given().when()
				.get("/ngsi-ld/ws/smartmetropolis/find-by-relationaship-filter?field=escola.object&operator=eq&value=urn:ngsi-ld:escola:12345567")
				.then().contentType(MediaType.APPLICATION_JSON).body("id", hasItems(entity.getId()));

	}

	@Test
	public void testFindByQuery() {
		String query = "p*.prop2.value$gte$20;r*.escola.object$eq$urn:ngsi-ld:escola:12345567";
		given().when().get("/ngsi-ld/ws/smartmetropolis/find-by-query?query=" + query).then()
				.contentType(MediaType.APPLICATION_JSON).body("id", hasItems(entity.getId()));
	}

	@Test
	public void testFindAll() {
		given().when().get("/ngsi-ld/ws/smartmetropolis").then().contentType(MediaType.APPLICATION_JSON).assertThat()
				.statusCode(200).body("id", hasItems(entity.getId()));

	}

	@Test
	public void testSave() throws IOException, JSONException {
		Entity entity_test = new Entity();

		entity_test.setId("urn:ngsi-ld:aluno_junit_test:" + UUID.randomUUID().toString());
		entity_test.setType("aluno_junit_test");

		entity_test.addContext(
				"https://forge.etsi.org/gitlab/NGSI-LD/NGSI-LD/raw/master/coreContext/ngsi-ld-core-context.json");
		entity_test.addContext(
				"https://github.com/JorgePereiraUFRN/SGEOL-LD/blob/master/ngsi-ld/ws/education/student/Student_Context.jsonld");

		entity_test.addProperty("prop1", PropertyType.Property, "value1");
		entity_test.addProperty("prop2", PropertyType.Property, "20");
		entity_test.addRelationaship("escola", "urn:ngsi-ld:escola:12345567");

		Geometry geometry = new Geometry();
		geometry.setEntityId(entity_test.getId());

		JSONObject point = new JSONObject();
		point.put("type", "Point");
		point.put("coordinates", new JSONArray(Arrays.asList(new String[] { "10", "30" })));

		geometry.setGeoJson(point);
		entity_test.addGeometry(geometry);

		given().contentType(MediaType.APPLICATION_JSON).body(entity_test.toNGSI_LD()).when()
				.post("/ngsi-ld/ws/smartmetropolis").then().assertThat().statusCode(201);

		given().when().delete("/ngsi-ld/ws/smartmetropolis?entity-id=" + entity_test.getId()).then().assertThat()
				.statusCode(200);

	}

	@Test
	public void testUpdate() throws JsonProcessingException {
		entity.addProperty("new_prop", PropertyType.Property, "new_prop");

		given().contentType(MediaType.APPLICATION_JSON).body(entity.toNGSI_LD()).when()
				.put("/ngsi-ld/ws/smartmetropolis").then().assertThat().statusCode(200);
	}

	@Test
	public void testFindById() {
		given().when().get("/ngsi-ld/ws/smartmetropolis/find-by-id?entity-id=" + entity.getId()).then()
				.contentType(MediaType.APPLICATION_JSON).body("id", equalTo(entity.getId()));
	}

	@Test
	public void testDelete() {
		given().when().delete("/ngsi-ld/ws/smartmetropolis?entity-id=" + entity.getId()).then().assertThat()
				.statusCode(200);

	}

	@Test
	public void testFindByDocumentQuery() {

		List<Document> consulta = new ArrayList<Document>();
		consulta.add(new Document("properties.prop1.value", "value1"));
		consulta.add(new Document("properties.prop2.value", new Document("$lte", 20)));

		Document query = new Document();
		query.append("$and", consulta);

		given().contentType(MediaType.APPLICATION_JSON).body(query.toJson()).when()
				.post("/ngsi-ld/ws/smartmetropolis/find-by-document").then().body("id", hasItems(entity.getId()));

	}

}
