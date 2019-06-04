package br.imd.sgeol.util;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.geotools.geojson.geom.GeometryJSON;



public class Formats {

	
	
	public static  String checkGeoJSON(String value) throws IOException, JSONException {
		GeometryJSON ret = new GeometryJSON(12);
		return ret.toString(getGeoJSON(value));
	}
	
	public static com.vividsolutions.jts.geom.Geometry getGeoJSON(String value) throws IOException, JSONException {
		JSONObject json = new JSONObject(value);
		GeometryJSON ret = new GeometryJSON(12);
		StringReader reader = new StringReader(json.toString());
		return ret.read(reader);		
	}
	
	
	public static String getDate(Date d) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		return df.format(d);
	}
}
