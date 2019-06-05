package br.imd.ngsi_ld.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.imd.ngsi_ld.util.DateFormatUtil;

@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class Property extends ContextObejct {

	protected Object value;
	protected String observedAt;

	
	public Property() {
		// TODO Auto-generated constructor stub
	}

	public Property(PropertyType type, Object value) {
		super();
		this.type = type.toString();
		this.value = value;
	}

	public void setType(PropertyType type) {
		this.type = type.toString();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getObservedAt() {
		return observedAt;
	}

	public void setObservedAt(String observedAt) {
		this.observedAt = observedAt;
	}

	public JSONObject toJsonObject() throws JSONException {

		JSONObject json = new JSONObject();

		json.put("type", type);

		try {
			
			if (this.type.equals(PropertyType.GeoProperty.toString())) {
				json.put("value", new JSONObject(value.toString()));
			} else if (isJsonObect(value.toString())) {
				json.put("value", new JSONObject(value.toString()));
			} else if (isJsonArray(value.toString())) {
				json.put("value", new JSONArray(value.toString()));
			} else if (isBoolean(value.toString())) {
				json.put("value", Boolean.parseBoolean(value.toString()));
			} else if (isDate(value.toString())) {
				json.put("value", new JSONObject().put("$date", value.toString()));
			} else if (isDouble(value.toString())) {
				json.put("value", Double.parseDouble(value.toString()));
			} else if (isInteger(value.toString())) {
				json.put("value", Integer.parseInt(value.toString()));
			} else {
				json.put("value", value.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.put("value", value.toString());
		}

		if (observedAt != null && !observedAt.isEmpty()) {
			json.put("observedAt", observedAt);
		}

		for (String prop : properties.keySet()) {
			json.put(prop, properties.get(prop));
		}

		for (String rel : relationaships.keySet()) {
			json.put(rel, relationaships.get(rel));
		}

		return json;
	}

	@Override
	public String toString() {
		return "Property [value=" + value + ", observedAt=" + observedAt + "]";
	}

	private boolean isJsonObect(String value) {
		try {
			JSONObject jsonObject = new JSONObject(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isJsonArray(String value) {
		try {
			JSONArray jsonArray = new JSONArray(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isInteger(String value) {
		try {
			Integer integer = Integer.parseInt(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isDouble(String value) {
		try {
			Double integer = Double.parseDouble(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isBoolean(String value) {

		if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
			return true;
		}
		return false;

	}
	
	private boolean isDate(String value) {
		try {

			Date date = DateFormatUtil.parse(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
