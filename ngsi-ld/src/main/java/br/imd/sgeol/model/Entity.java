package br.imd.sgeol.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import br.imd.sgeol.util.DateFormatUtil;
import br.imd.sgeol.util.EntitySerializer;

@JsonPropertyOrder({"@context", "id", "type", "createdAt", "modifiedAt", "properties", "relationaships" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Entity extends ContextObejct {

	private List<String> contexties = new ArrayList<String>();

	private String id;
	private Date createdAt;
	private Date modifiedAt;

	

	public Entity() {
		// TODO Auto-generated constructor stub
	}

	@JsonProperty("@context")
	public List<String> getContext() {
		return contexties;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addContext(String context) {
		this.contexties.add(context);
	}

	public void removeContext(String contex) {

		for (int i = 0; i < contexties.size(); i++) {
			if (contexties.get(i).equals(contex)) {
				contexties.remove(i);
				break;
			}
		}
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public void addGeometry(Geometry geometry) throws IOException, JSONException {

		addProperty("location", PropertyType.GeoProperty, geometry.getGeoJson().toString());
	}

	public Geometry getGeometry() throws JSONException {

		if (properties.get("location") == null) {
			return null;
		} else {

			Geometry geometry = new Geometry();
			geometry.setEntityId(id);

			Property property = getProperty("location");

			geometry.setGeoJson(new JSONObject(property.getValue().toString()));

			return geometry;
		}
	}

	public JSONObject toJsonObject() throws JSONException {

		JSONObject json = new JSONObject();

		json.put("@context", contexties);
		json.put("_id", id);
		json.put("type", type);
		if (modifiedAt != null) {
			json.put("modifiedAt", (modifiedAt));
		}
		if (createdAt != null) {
			json.put("createdAt", DateFormatUtil.format(createdAt));
		}

		JSONObject propObjs = new JSONObject();

		for (String prop : properties.keySet()) {

			propObjs.put(prop, properties.get(prop).toJsonObject());

		}

		json.put("properties", propObjs);

		JSONObject relObjs = new JSONObject();

		for (String rel : relationaships.keySet()) {
			relObjs.put(rel, relationaships.get(rel).toJson());
		}

		json.put("relationaships", relObjs);

		return json;
	}

	public String toJson() throws JsonProcessingException, JSONException {

		return toJsonObject().toString();
	}

	public String toNGSI_LD() /* throws JsonProcessingException */{

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		SimpleModule module = new SimpleModule();
		module.addSerializer(Entity.class, new EntitySerializer());
		mapper.registerModule(module);

		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}
	


	@Override
	public String toString() {
		return "Entity [contexties=" + contexties + ", id=" + id + ", createdAt=" + createdAt + ", modifiedAt="
				+ modifiedAt + ", type=" + type + ", properties=" + properties + ", relationaships=" + relationaships
				+ "]";
	}

}
