package br.imd.ngsi_ld.model;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.imd.ngsi_ld.util.Formats;

public class Geometry {
	
	private String entityId;
	private JSONObject geoJson;

	public Geometry() {
		// TODO Auto-generated constructor stub
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public JSONObject getGeoJson() {
		return geoJson;
	}

	public void setGeoJson(JSONObject geoJson) {
		this.geoJson = geoJson;
	}
	
	public String toPostGisGeoJson() throws IOException, JSONException{
		
		if(geoJson == null){
			return "{}";
		}else{
			return Formats.getGeoJSON(geoJson.toString()).toString();
		}
		
	}

}
