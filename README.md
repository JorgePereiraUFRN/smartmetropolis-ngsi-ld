# smartmetropolis-ngsi-ld


### Entidades


| Number | HTTP Verb | Path  | Description  | Body
| :---:| :------------: | :------------ | :------------ | :------------ |
| 1 |POST | /ngsi-ld/ws/smartmetropolis | Save a new entity |  json-ld representation of the entity|
| 2 |PUT | /ngsi-ld/ws/smartmetropolis | Update a entity|  json-ld representation of the entity|
| 3 |GET | /ngsi-ld/ws/smartmetropolis/find-by-id?entity-id=`{entity-id}` | Query a entity with id `{id da entidade}` | |
| 4 |GET | /ngsi-ld/ws/smartmetropolis?limit=`{limit}`&offset=`{offset}` | Query multiple entities. The values for limit and offset are optionals.| |
| 5 |DELETE | /ngsi-ld/ws/smartmetropolis/?entity-id=`{entity id}` | Delete a entity with id`{id da entidade}` | |
| 6 |POST| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{entity id}`&property-name=`{property name}`| Insert a new property in an entity| Json representations of the property.|
| 7 |PUT| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{entity id}`&property-name=`{property name}` | Update an property of an entity | Json representation os the property|
| 8 |GET| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{entity id}`&property-name=`{property name}` | Query an propterty ||
| 9 |DELETE| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{entity id}`&property-name=`{property name}` | Delete an property||
| 10 |GET| /ngsi-ld/ws/smartmetropolis/find-by-property-filter?field=`{elmenent of the property}`&operator=`{operator}`&value=`{value}` |Query entities based on your properties. Suported operators: eq (equal); gt (greather); gte (greather or equal); lt (less); lte (less or equal) ||
| 11 |POST| /ngsi-ld/ws/smartmetropolis/relationship?entity-id=`{entity id}`relationship-name=`{relationship name}` | Insert a new relationaship | Json representation of the relationaship|
| 12 |PUT| /ngsi-ld/ws/smartmetropolis/relationaship?entity-id=`{entity id}`&relationship-name=`{relationship name}`| Update a relationanship| Json representation of the relationship|
| 13 |DELETE| /ngsi-ld/ws/smartmetropolis/relationship?entity-id=`{entity id}`&relationship-name=`{relationnship name}` | Delete a relationship | |
| 14 |GET| /ngsi-ld/ws/smartmetropolis/relationaship?entity-id=`{entity id}`&relationship-name=`{relationship name}` | Query a relationnship| |
| 15 |GET| /ngsi-ld/ws/smartmetropolis/find-by-relationship-filter?field=`{element of relationaship}`&operator=`{operador}`&value=`{value}` | Query entities based in your relationships. Suported operators: eq (equal) ||
| 16 |GET| /ngsi-ld/ws/smartmetropolis/find-by-query?query=`{query}`&limit=`{limit}`&offset=`{offset}`| Query entities based on query | |
| 17 |POST| /ngsi-ld/ws/smartmetropolis/find-by-document?limit=`{limit}`&offset=`{offset}   `| Query entities based on json document| Json document|



#### 1 - Save a new entity

`POST` http://localhost:8080/ngsi-ld/ws/smartmetropolis

{  
  "@context" : [ "https://forge.etsi.org/gitlab/NGSI-LD/NGSI-LD/raw/master/coreContext/ngsi-ld-core-context.json", "https://github.com/JorgePereiraUFRN/SGEOL-LD/blob/master/ngsi-ld/education/student/Student_Context.jsonld" ],  
  "id" : "urn:ngsi-ld:aluno:32a72d25-8378-4921-a318-87db41968d73",  
  "type" : "aluno_test",  
  "nota_portugues" : {"type":"Property","value":"8"},  
  "nota_matematica" : {"type":"Property","value":"9"},  
  "location" : {"type":"GeoProperty","value":{"coordinates":["10","30"],"type":"Point"}},  
  "escola" : {"type":"Relationaship","object":"urn:ngsi-ld:escola:12345567"}  
}


#### 2 - Update a entity

`PUT` http://localhost:8080/ngsi-ld/ws/smartmetropolis

{  
  "@context" : [ "https://forge.etsi.org/gitlab/NGSI-LD/NGSI-LD/raw/master/coreContext/ngsi-ld-core-context.json", "https://github.com/JorgePereiraUFRN/SGEOL-LD/blob/master/ngsi-ld/education/student/Student_Context.jsonld" ],  
  "id" : "urn:ngsi-ld:aluno:32a72d25-8378-4921-a318-87db41968d73",  
  "type" : "aluno_test",  
  "nota_portugues" : {"type":"Property","value":"8"},  
  "nota_matematica" : {"type":"Property","value":"value1"},  
  `"frequencia_de_aula" : {"type":"Property","value":"95"},`    
  "location" : {"type":"GeoProperty","value":{"coordinates":["10","30"],"type":"Point"}},  
  "escola" : {"type":"Relationaship","object":"urn:ngsi-ld:escola:12345567"}  
}

#### 3 - Query a entity by id

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-id?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485

#### 4 - Query entities
`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis?limit=500&offset=0

#### 5 -Delete a entity

`DELETE` http://localhost:8080/ngsi-ld/ws/smartmetropolis?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485

#### 6 - Save a new property

`POST` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia

{"type":"Property","value":"7.5"}

#### 7 - Update a property

`PUT` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia

{"type":"Property","value":"8.5"}

#### 8 - Query a property

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia

#### 9 - Deletar uma propriedade pertencente a uma entidade

`DELETE` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia


#### 10 - Query entities based on your properties

This query returns all students with math scores higher than 9  

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-property-filter?field=nota_matematica.value&operator=gt&value=9


#### 11 -  Add a new relationanship

`POST` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationship-name=escola_anterior

{"type":"Relationship","object":"urn:ngsi-ld:escola:123455321"} 


####  12 - Update a relationnship

`PUT` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationaship-name=escola_anterior

{"type":"Relationship","object":"urn:ngsi-ld:escola:123459634"} 


#### 13 - Delete a relationship

`DELETE` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationship-name=escola_anterior

#### 14 -Query a relationship

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationship-name=escola

#### 15 - Query entities based in your relationships
This query returns students who study at school `urn:ngsi-ld:escola:12345567`  

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-relationship-filter?field=escola.object&operator=eq&value=urn:ngsi-ld:escola:12345567


#### 16 - Query entities based on query

This query returns students whose math scores are higher than 7 and who study at school `urn:ngsi-ld:escola:12345567`

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-query?query=p*.nota_matematica.value$gte$7;r*.escola.object$eq$urn:ngsi-ld:escola:12345567


#### 17 - Query entities based on json documento

`POST` http://localhost:8080/sgeol-dm/v2/aluno/find-by-document?limit=2

{
	"$or": [{
		"properties.attendance.value": {
			"$gte": 99
		}
	}, {
		"properties.attendance.value": {
		
			"$lte": 100
		}
	}]
}

