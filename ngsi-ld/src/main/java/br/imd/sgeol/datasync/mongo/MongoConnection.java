package br.imd.sgeol.datasync.mongo;

import java.io.IOException;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.operation.CreateCollectionOperation;

import br.imd.sgeol.util.ConfigurationValues;

public class MongoConnection{
	
	private MongoDatabase db;
	private MongoClient cli;
	private static MongoConnection con;
	
	
	public boolean existsCollection(String name){	
		return  db.listCollectionNames()
			    .into(new ArrayList<String>()).contains(name);	
	}
	
	
	private MongoConnection(){
		try {
			ConfigurationValues.initialize();
			this.cli = new MongoClient(ConfigurationValues.Mongo_Url);
			this.db = this.cli.getDatabase(ConfigurationValues.Mongo_DB);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static MongoConnection getInstance(){		
		if( con == null){
			con = new MongoConnection();
		}		
		return con;		
	}
	
	public MongoCollection<Document> getCollection(String name){
		if(!existsCollection(name)) this.db.createCollection(name);
		return this.db.getCollection(name);
	}
	
	public void CreateCollection(String name){
		this.db.createCollection(name);
	}
	

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.db = null;
		this.cli.close();
		this.cli = null;
	}
	
	
}
