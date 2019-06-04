package br.imd.sgeol.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataTypeUtil {
	
	
	public static boolean isJsonObect(String value) {
		try {
			JSONObject jsonObject = new JSONObject(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isJsonArray(String value) {
		try {
			JSONArray jsonArray = new JSONArray(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isInteger(String value) {
		try {
			Integer integer = Integer.parseInt(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDouble(String value) {
		try {
			Double integer = Double.parseDouble(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isBoolean(String value) {

		if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
			return true;
		}
		return false;

	}
	
	public static boolean isDate(String value) {
		try {

			Date date = DateFormatUtil.parse(value);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
