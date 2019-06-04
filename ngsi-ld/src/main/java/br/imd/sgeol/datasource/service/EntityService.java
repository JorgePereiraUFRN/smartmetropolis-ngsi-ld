package br.imd.sgeol.datasource.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;

import br.imd.sgeol.datasync.mongo.MongodbUtil;
import br.imd.sgeol.datasync.mongo.NgsiLdDao;
import br.imd.sgeol.datasync.mongo.NgsiLdDaoInterface;

import br.imd.sgeol.exception.DaoException;
import br.imd.sgeol.exception.EntityAlreadyExistException;
import br.imd.sgeol.exception.EntityDoesNotExistException;
import br.imd.sgeol.exception.InvalidEntityIdException;
import br.imd.sgeol.exception.ObjectNullException;
import br.imd.sgeol.exception.SgeolServiceException;
import br.imd.sgeol.model.Entity;

public class EntityService extends GenericService {

	private static final EntityService ENTITY_SERVICE = new EntityService();

	private EntityService() {

	}

	public static EntityService getInstance() {
		return ENTITY_SERVICE;
	}

	public void save(Entity entity) throws SgeolServiceException, ObjectNullException,
			InvalidEntityIdException, EntityAlreadyExistException, DaoException {

		validateEntity(entity);

		if (entityExist(entity.getId())) {
			throw new EntityAlreadyExistException("The entity whit id: " + entity.getId() + " already exist.");
		}

		try {
			ngsiLdDao.save(entity);

		} catch (Exception e) {
			e.printStackTrace();

			ngsiLdDao.delete(entity.getId());

			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public void update(Entity entity) throws SgeolServiceException, ObjectNullException,
			InvalidEntityIdException, DaoException, EntityDoesNotExistException {

		validateEntity(entity);

		if (!entityExist(entity.getId())) {
			throw new EntityDoesNotExistException("The entity whit id " + entity.getId() + " does not exist");
		}
		try {

			ngsiLdDao.update(entity);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

	public Entity findById(String id) throws SgeolServiceException, DaoException {
		try {
			return ngsiLdDao.findById(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}
	}

	public void delete(String id) throws SgeolServiceException, DaoException {
		try {
			ngsiLdDao.delete(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SgeolServiceException("Unespected error: " + e.getMessage());
		}

	}

}
