package br.imd.sgeol.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
public class Relationaship extends ContextObejct {

	private Object objetc;
	private String observedAt;

	public Relationaship() {
		this.type = "Relationaship";
	}

	public Relationaship(Object objetc) {
		super();
		this.type = "Relationaship";
		this.objetc = objetc;
	}

	public Object getObjetc() {
		return objetc;
	}

	public void setObjetc(Object objetc) {
		this.objetc = objetc;
	}

	public String getObservedAt() {
		return observedAt;
	}

	public void setObservedAt(String observedAt) {
		this.observedAt = observedAt;
	}

	public JSONObject toJson() throws JSONException {

		JSONObject json = new JSONObject();

		json.put("type", type);

		try {

			json.put("object", new JSONArray(objetc.toString()));

		} catch (Exception e) {
			// caso o valor não seja um array ele sera inserido como um valor
			// simples
			json.put("object", objetc);
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
		return "Relationaship [objetc=" + objetc + ", observedAt=" + observedAt + "]";
	}

}
