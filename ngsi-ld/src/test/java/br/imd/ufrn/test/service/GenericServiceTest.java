package br.imd.ufrn.test.datasource.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import br.imd.sgeol.datasource.service.GenericService;
import br.imd.sgeol.exception.DaoException;
import br.imd.sgeol.exception.EntityDoesNotExistException;
import br.imd.sgeol.exception.ObjectNullException;
import br.imd.sgeol.exception.PropertyAlreadyExistException;
import br.imd.sgeol.exception.PropertyDoesNotExistException;
import br.imd.sgeol.exception.RelationashipDoesNotExistException;
import br.imd.sgeol.exception.SgeolServiceException;
import br.imd.sgeol.model.Entity;
import br.imd.sgeol.model.Property;
import br.imd.sgeol.model.PropertyType;
import br.imd.sgeol.model.Relationaship;

public abstract class GenericServiceTest {

	private final Entity entity;
	private final GenericService service;
	

	private static TimeZone tz = TimeZone.getTimeZone("UTC");
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");

	static {
		df.setTimeZone(tz);
	}

	public GenericServiceTest(final Entity entity, final GenericService service) {
		this.entity = entity;
		this.service = service;
	}

	@Test
	public void testAddProperty()
			throws SgeolServiceException, DaoException, ObjectNullException, PropertyAlreadyExistException {

		Property property = new Property(PropertyType.Property, "new_prop");
		String propertyId = UUID.randomUUID().toString();

		service.addProperty(entity.getId(), property, propertyId);

		Property retriviedProp = service.findProperty(entity.getId(), propertyId);

		Assert.assertEquals(property.getValue().toString(), retriviedProp.getValue().toString());
	}

	@Test
	public void testUpdateProperty() throws SgeolServiceException, DaoException, ObjectNullException,
			PropertyAlreadyExistException, PropertyDoesNotExistException {

		Property property = new Property(PropertyType.Property, "new_prop");
		String propertyId = UUID.randomUUID().toString();

		service.addProperty( entity.getId(), property, propertyId);

		property.setValue("new value");

		service.updateProperty(entity.getId(), property, propertyId);

		Property retriviedProp = service.findProperty(entity.getId(), propertyId);

		Assert.assertEquals(property.getValue().toString(), retriviedProp.getValue().toString());

	}

	@Test
	public void testRemoveProperty()
			throws SgeolServiceException, DaoException, ObjectNullException, PropertyAlreadyExistException {
		Property property = new Property(PropertyType.Property, "new_prop");
		String propertyId = UUID.randomUUID().toString();

		service.addProperty(entity.getId(), property, propertyId);

		service.removeProperty(entity.getId(), propertyId);

		Property retriviedProp = service.findProperty(entity.getId(), propertyId);

		Assert.assertNull(retriviedProp);

	}

	@Test
	public void testFindProperty()
			throws SgeolServiceException, DaoException, ObjectNullException, PropertyAlreadyExistException {
		Property property = new Property(PropertyType.Property, "new_prop");
		String propertyId = UUID.randomUUID().toString();

		service.addProperty(entity.getId(), property, propertyId);

		Property retriviedProp = service.findProperty(entity.getId(), propertyId);

		Assert.assertEquals(property.toString(), retriviedProp.toString());
	}

	@Test
	public void testAddRelationaship()
			throws SgeolServiceException, ObjectNullException, DaoException, EntityDoesNotExistException {

		Relationaship relationaship = new Relationaship();

		relationaship.setObjetc("urn:ngsi-ld:test:contextEntity1");
		String relationashipName = UUID.randomUUID().toString();

		service.addRelationaship(entity.getId(), relationashipName, relationaship);

		Relationaship finded = service.findRelationaship(entity.getId(), relationashipName);

		Assert.assertEquals(relationaship.toString(), finded.toString());
	}

	@Test
	public void testUpdateRelationaship() throws SgeolServiceException, ObjectNullException, DaoException,
			EntityDoesNotExistException, RelationashipDoesNotExistException {
		Relationaship relationaship = new Relationaship();

		relationaship.setObjetc("urn:ngsi-ld:test:contextEntity1");
		String relationashipName = UUID.randomUUID().toString();

		service.addRelationaship(entity.getId(), relationashipName, relationaship);

		relationaship.setObjetc("urn:ngsi-ld:test:contextentity2");

		service.updateRelationaship(entity.getId(), relationashipName, relationaship);

		Relationaship finded = service.findRelationaship(entity.getId(), relationashipName);

		Assert.assertEquals(relationaship.toString(), finded.toString());

	}

	@Test
	public void testRemoveRelationaship()
			throws SgeolServiceException, ObjectNullException, DaoException, EntityDoesNotExistException {

		Relationaship relationaship = new Relationaship();
		relationaship.setObjetc("urn:ngsi-ld:test:contextEntity1");
		String relationashipName = UUID.randomUUID().toString();

		service.addRelationaship(entity.getId(), relationashipName, relationaship);

		service.removeRelationaship(entity.getId(), relationashipName);

		Relationaship finded = service.findRelationaship(entity.getId(), relationashipName);

		Assert.assertNull(finded);

	}

	@Test
	public void testFindRelationaship()
			throws SgeolServiceException, ObjectNullException, DaoException, EntityDoesNotExistException {
		Relationaship relationaship = new Relationaship();
		relationaship.setObjetc("urn:ngsi-ld:test:contextEntity1");
		String relationashipName = UUID.randomUUID().toString();

		service.addRelationaship(entity.getId(), relationashipName, relationaship);

		Relationaship finded = service.findRelationaship(entity.getId(), relationashipName);

		Assert.assertEquals(relationaship.toString(), finded.toString());
	}

	@Test
	public void testFindAll() throws DaoException, SgeolServiceException {

		List<Entity> entities = service.findAll(10, 0);

		Assert.assertTrue(entities.size() > 0);
	}

	

}
