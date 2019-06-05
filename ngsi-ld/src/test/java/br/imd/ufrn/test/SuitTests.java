package br.imd.ufrn.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.imd.ufrn.test.rest.api.EntityResourceTest;
import br.imd.ufrn.test.service.EntityServiceTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({ EntityResourceTest.class, EntityServiceTest.class })
public class SuitTests {

}
