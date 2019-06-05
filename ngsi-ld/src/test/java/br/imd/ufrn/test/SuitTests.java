package br.imd.ufrn.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.imd.ufrn.test.datasource.service.EntityServiceTest;
import br.imd.ufrn.test.rest.api.EntityResourceTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({ EntityResourceTest.class, EntityServiceTest.class })
public class SuitTests {

}
