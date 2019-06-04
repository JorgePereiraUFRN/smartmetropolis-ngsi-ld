package br.imd.sgeol.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationValues {

	public static String Mongo_Url;
	public static String Mongo_DB;

	static {
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initialize() throws IOException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream source = classLoader.getResourceAsStream("properties.cfg");
		Properties conf = new Properties();
		conf.load(source);

		Mongo_Url = conf.getProperty("mongo.url");
		Mongo_DB = conf.getProperty("mongo.db");

	}
}
