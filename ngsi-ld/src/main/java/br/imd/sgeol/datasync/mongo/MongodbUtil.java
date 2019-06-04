package br.imd.sgeol.datasync.mongo;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import br.imd.sgeol.exception.DaoException;

public class MongodbUtil {

	public static void deleteCollection(String collectionName) throws DaoException {
		try {
			MongoCollection<Document> collection = MongoConnection.getInstance().getCollection(collectionName);

			collection.drop();
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}
	
	public static Boolean existCollection(String collectionName) throws DaoException {
		try {
			return MongoConnection.getInstance().existsCollection(collectionName);
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}

	public static void creatCollection(String collectionName) throws DaoException {
		try {
			MongoConnection.getInstance().CreateCollection(collectionName);
		} catch (Exception e) {
			throw new DaoException(e.getMessage());
		}
	}
	
}
