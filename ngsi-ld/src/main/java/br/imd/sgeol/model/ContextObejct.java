package br.imd.sgeol.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
@XmlRootElement
public abstract class ContextObejct {

	protected String type;

	protected Map<String, Property> properties = new HashMap<String, Property>();
	protected Map<String, Relationaship> relationaships = new HashMap<String, Relationaship>();

	public ContextObejct() {
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}

	public Map<String, Relationaship> getRelationaships() {
		return relationaships;
	}

	public void setRelationaships(Map<String, Relationaship> relationaships) {
		this.relationaships = relationaships;
	}

	public void addProperty(String id, Property pro) {
		properties.put(id, pro);
	}

	public void addProperty(String id, PropertyType type, String value) {
		properties.put(id, new Property(type, value));
	}

	public void addProperty(String id, PropertyType type, JSONObject value) throws JsonProcessingException {

		properties.put(id, new Property(type, value));
	}

	public void addProperty(String id, PropertyType type, JSONArray jsonArray) {
		properties.put(id, new Property(type, jsonArray));
	}

	public void removeProperty(String id) {
		properties.remove(id);
	}

	public void addRelationaship(String id, Relationaship rel) {
		relationaships.put(id, rel);
	}

	public void addRelationaship(String id, String object) {
		relationaships.put(id, new Relationaship(object));
	}

	public void addRelationaship(String id, JSONArray object) {
		relationaships.put(id, new Relationaship(object));
	}

	public void removeRelationaship(String id) {
		relationaships.remove(id);
	}

	public Property getProperty(String propertyName){
		
		return properties.get(propertyName);
	}
	
	public Relationaship getRelationaship(String relationashipName){
		return relationaships.get(relationashipName);
	}

}
