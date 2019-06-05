package br.imd.ngsi_ld.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import br.imd.ngsi_ld.exception.EntitySerializerException;
import br.imd.ngsi_ld.exception.ObjectNullException;
import br.imd.ngsi_ld.model.Entity;
import br.imd.ngsi_ld.model.Property;
import br.imd.ngsi_ld.model.PropertyType;
import br.imd.ngsi_ld.model.Relationaship;

public class EntitySerializer extends JsonSerializer<Entity> {

	private static final long serialVersionUID = 1L;
	private static TimeZone tz = TimeZone.getTimeZone("UTC");
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

	static {
		df.setTimeZone(tz);
	}

	@Override
	public void serialize(Entity entity, JsonGenerator jgen, SerializerProvider privider) throws IOException {

		jgen.writeStartObject();

		jgen.writeObjectField("@context", entity.getContext());
		jgen.writeStringField("id", entity.getId());
		jgen.writeStringField("type", entity.getType());

		if (entity.getModifiedAt() != null) {
			jgen.writeStringField("modifiedAt", df.format(entity.getModifiedAt()));
		}
		if (entity.getCreatedAt() != null) {
			jgen.writeStringField("createdAt", df.format(entity.getCreatedAt()));
		}

		try {
			for (String prop : entity.getProperties().keySet()) {
				jgen.writeFieldName(prop);
				jgen.writeRawValue(JSONWriter.valueToString(entity.getProperties().get(prop).toJsonObject()));
			}
			
			for (String rel : entity.getRelationaships().keySet()) {
				jgen.writeFieldName(rel);
				jgen.writeRawValue(JSONWriter.valueToString(entity.getRelationaships().get(rel).toJson()));
			}
		} catch (JSONException e) {
			throw new IOException(e.getMessage());
		} catch (org.codehaus.jettison.json.JSONException e) {
			throw new IOException(e.getMessage());
		}

		jgen.writeEndObject();

	}

	public Entity jsonLDToEntity(String json_ld) throws JSONException, ParseException {
		JSONObject jsonObject = new JSONObject(json_ld);

		return jsonLDToEntity(jsonObject);
	}

	public Entity jsonLDToEntity(JSONObject json) throws JSONException, ParseException {

		Entity entity = new Entity();

		JSONArray context = json.getJSONArray("@context");

		for (int i = 0; i < context.length(); i++) {
			entity.addContext(context.getString(i));
		}
		json.remove("@context");

		entity.setId(json.getString("id"));
		json.remove("id");
		if (json.has("createdAt")) {
			entity.setCreatedAt(df.parse(json.getString("createdAt")));
			json.remove("createdAt");
		}
		if (json.has("modifiedAt")) {
			entity.setModifiedAt(df.parse(json.getString("modifiedAt")));
			json.remove("modifiedAt");
		}
		entity.setType(json.getString("type"));
		json.remove("type");

		for (String key : json.keySet()) {

			JSONObject obj = new JSONObject(json.get(key).toString());

			if (obj.has("type") && obj.getString("type").equalsIgnoreCase("Relationaship")) {

				Relationaship relationaship = jsonToRelationaship(obj);
				entity.addRelationaship(key, relationaship);

			} else if (obj.has("type") && (obj.getString("type").equalsIgnoreCase("Property")
					|| obj.getString("type").equalsIgnoreCase("GeoProperty"))) {

				Property property = jsonToProperty(obj);
				entity.addProperty(key, property);

			}

		}

		return entity;
	}

	public Entity simpleJsonToEntity(JSONObject simpleJson)
			throws JSONException, ParseException, EntitySerializerException {

		return simpleJsonToEntity(simpleJson.toString());
	}

	public Entity simpleJsonToEntity(String simpleJson)
			throws JSONException, ParseException, EntitySerializerException {

		try {
			Entity entity = new Entity();
			JSONObject json = new JSONObject(simpleJson);

			JSONArray context = json.getJSONArray("@context");

			for (int i = 0; i < context.length(); i++) {
				entity.addContext(context.getString(i));
			}
			json.remove("@context");

			if (json.has("id")) {
				entity.setId(json.getString("id"));
				json.remove("id");
			}
			if (json.has("_id")) {
				entity.setId(json.getString("_id"));
				json.remove("_id");
			}

			if (json.has("createdAt")) {
				entity.setCreatedAt(df.parse(json.getString("createdAt")));
				json.remove("createdAt");
			}
			if (json.has("modifiedAt")) {
				entity.setModifiedAt(df.parse(json.getString("modifiedAt")));
				json.remove("modifiedAt");
			}

			entity.setType(json.getString("type"));
			json.remove("type");

			JSONObject properties = json.getJSONObject("properties");

			for (String key_prop : properties.keySet()) {

				Property property = jsonToProperty(properties.getJSONObject(key_prop));

				entity.addProperty(key_prop, property);
			}

			JSONObject relationaships = json.getJSONObject("relationaships");

			for (String key_rel : relationaships.keySet()) {

				Relationaship rel = jsonToRelationaship(relationaships.getJSONObject(key_rel));

				entity.addRelationaship(key_rel, rel);
			}

			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EntitySerializerException("Erro ao serializar json para entidade: " + e.getMessage());
		}

	}

	public Entity documentToEntity(Document document)
			throws JSONException, ParseException, ObjectNullException, EntitySerializerException {

		if (document == null) {
			throw new ObjectNullException("the json document is null");
		}

		JSONObject json = new JSONObject(document.toJson());

		json.put("createdAt", df.format(document.getDate("createdAt")));

		if (document.containsKey("modifiedAt")) {
			json.put("modifiedAt", df.format(document.getDate("modifiedAt")));
		}

		return simpleJsonToEntity(json);

		
	}

	public Property documentToProperty(Document doc) {
		Property property = new Property();

		if (doc.containsKey("observedAt")) {
			property.setObservedAt(doc.getString("observedAt"));
		}

		String type = doc.getString("type");

		if (type.equalsIgnoreCase(PropertyType.GeoProperty.name())) {
			property.setType(PropertyType.GeoProperty);
		} else if (type.equalsIgnoreCase(PropertyType.Property.name())) {
			property.setType(PropertyType.Property);
		}

		if (doc.get("value") instanceof Document) {
			// o document.value é um documento, nesse caso o valor da
			// propriedade é o valor json do documento
			property.setValue(((Document) (doc.get("value"))).toJson());

		} else {
			property.setValue(doc.get("value"));

		}

		if (doc.containsValue("properties")) {

			Map<String, Document> properties = (Map<String, Document>) doc.get("properties");

			for (String key : properties.keySet()) {

				Document docProp = properties.get(key);

				property.addProperty(key, documentToProperty(docProp));
			}

		}

		if (doc.containsValue("relationaships")) {

			Map<String, Document> relationaships = (Map<String, Document>) doc.get("relationaships");

			for (String key : relationaships.keySet()) {

				Document docRel = relationaships.get(key);

				property.addRelationaship(key, documentToRelationaship(docRel));
			}

		}

		return property;
	}

	public Relationaship documentToRelationaship(Document doc) {

		Relationaship rel = new Relationaship();

		rel.setObjetc(doc.get("object"));

		if (doc.containsKey("observedAt")) {
			rel.setObservedAt("observedAt");
		}

		if (doc.containsValue("properties")) {

			Map<String, Document> properties = (Map<String, Document>) doc.get("properties");

			for (String key : properties.keySet()) {

				Document docProp = properties.get(key);

				rel.addProperty(key, documentToProperty(docProp));
			}

		}

		if (doc.containsValue("relationaships")) {

			Map<String, Document> relationaships = (Map<String, Document>) doc.get("relationaships");

			for (String key : relationaships.keySet()) {

				Document docRel = relationaships.get(key);

				rel.addRelationaship(key, documentToRelationaship(docRel));
			}

		}

		return rel;
	}

	public Relationaship jsonToRelationaship(String json) {

		JSONObject rel = new JSONObject(json);
		return jsonToRelationaship(rel);
	}

	public Relationaship jsonToRelationaship(JSONObject json) {

		Relationaship rel = new Relationaship();

		rel.setObjetc(json.get("object").toString());
		json.remove("object");
		json.remove("type");

		if (json.has("observedAt")) {
			rel.setObservedAt(json.getString("observedAt"));
			json.remove("observedAt");
		}

		for (String key : json.keySet()) {

			JSONObject obj = new JSONObject(json.get(key).toString());

			if (obj.has("type") && obj.getString("type").equalsIgnoreCase("Relationaship")) {

				Relationaship relationaship = jsonToRelationaship(obj);
				rel.addRelationaship(key, relationaship);

			} else if (obj.has("type") && (obj.getString("type").equalsIgnoreCase("Property")
					|| obj.getString("type").equalsIgnoreCase("GeoProperty"))) {

				Property property = jsonToProperty(obj);
				rel.addProperty(key, property);
			}

		}

		return rel;

	}

	public Property jsonToProperty(String json) {
		JSONObject property = new JSONObject(json);

		return jsonToProperty(property);
	}

	public Property jsonToProperty(JSONObject json) {

		Property property = new Property();

		if (json.has("observedAt")) {
			property.setObservedAt(json.getString("observedAt"));
			json.remove("observedAt");
		}

		String type = json.getString("type");

		if (type.equalsIgnoreCase(PropertyType.GeoProperty.name())) {
			property.setType(PropertyType.GeoProperty);
		} else if (type.equalsIgnoreCase(PropertyType.Property.name())) {
			property.setType(PropertyType.Property);
		}

		json.remove("type");

		property.setValue(json.get("value"));
		json.remove("value");

		for (String key : json.keySet()) {

			JSONObject obj = new JSONObject(json.get(key).toString());

			if (obj.has("type") && obj.getString("type").equalsIgnoreCase("Relationaship")) {

				Relationaship relationaship = jsonToRelationaship(obj);
				property.addRelationaship(key, relationaship);

			} else if (obj.has("type") && (obj.getString("type").equalsIgnoreCase("Property")
					|| obj.getString("type").equalsIgnoreCase("GeoProperty"))) {

				Property prop = jsonToProperty(obj);
				property.addProperty(key, prop);
			}

		}

		return property;
	}

}
