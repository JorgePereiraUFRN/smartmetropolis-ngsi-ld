# smartmetropolis-ngsi-ld


### Entidades


| Numero | Método HTTP | Path  | Descrição  | Body
| :---:| :------------: | :------------ | :------------ | :------------ |
| 1 |POST | /ngsi-ld/ws/smartmetropolis | Insere uma nova entidade |  representação em json-ld da entidade|
| 2 |PUT | /ngsi-ld/ws/smartmetropolis | Atualiza uma entidade pertencente|  representação em json-ld da entidade|
| 3 |GET | /ngsi-ld/ws/smartmetropolis/find-by-id?entity-id=`{id da entidade}` | Consulta uma entidade com id igual a `{id da entidade}` | |
| 4 |GET | /ngsi-ld/ws/smartmetropolis?limit=`{limit}`&offset=`{offset}` | Consulta entidades. Os valores de `limit` e `offset` são opcionais , por padrao limit é 1024 e offset 0.| |
| 5 |DELETE | /ngsi-ld/ws/smartmetropolis/?entity-id=`{id da entidade}` | Deleta uma entidade com id igual a `{id da entidade}` | |
| 6 |POST| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{id da entidade}`&property-name=`{nome da nova propriedade}`| Insere uma nova propriedade em uma entidade| Representação em json da propriedade.|
| 7 |PUT| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{id da entidade}`&property-name=`{nome da propriedade}` | Atualiza uma propriedade de uma entidade | Representação em Json da Propriedade|
| 8 |GET| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{id da netidade}`&property-name=`{nome da propriedade}` |Consulta uma propriedade pertencente a uma entidade ||
| 9 |DELETE| /ngsi-ld/ws/smartmetropolis/property?entity-id=`{id da entidade}`&property-name=`{nome da propriedade}` | Remove uma propriedade pertencente a uma entidade||
| 10 |GET| /ngsi-ld/ws/smartmetropolis/find-by-property-filter?field=`{elemento da propriedade}`&operator=`{operator}`&value=`{value}` |Consulta uma entidade com base em uma propriedade. operadores suportados: eq (igual); gt (maior); gte (maior ou igual); lt (menor); lte (menor ou igual), in (esta contido) ||
| 11 |POST| /ngsi-ld/ws/smartmetropolis/relationaship?entity-id=`{id da entidade}`relationaship-name=`{nome do novo relacionamento}` | Insere um novo relacionamento em uma entidade | representação em Json do novo relacionamento|
| 12 |PUT| /ngsi-ld/ws/smartmetropolis/relationaship?entity-id=`{id da entidade}`&relationaship-name=`{nome do relacionamento}`| Atualiza um relacionamento de uma entidade| Representação em Json do relacionamento|
| 13 |DELETE| /ngsi-ld/ws/smartmetropolis/relationaship?entity-id=`{id da entidade}`&relationaship-name=`{nome do relacionamento}` | Deleta um relacioanmento de uma entidade | |
| 14 |GET| /ngsi-ld/ws/smartmetropolis/relationaship?entity-id=`{id da entidade}`&relationaship-name=`{nome do relacionamento}` | Consulta um relacioanmento de uma entidade| |
| 15 |GET| /ngsi-ld/ws/smartmetropolis/find-by-relationaship-filter?field=`{elemento do relacionamento}`&operator=`{operador}`&value=`{value}` | Consulta entidades com base em um relacionamento. Operadores suportados: eq (igual), in (esta contido) ||
| 16 |GET| /ngsi-ld/ws/smartmetropolis/find-by-query?query=`{query}`&limit=`{limit}`&offset=`{offset}`| Consulta entidades com base em uma query | |
| 17 |POST| /ngsi-ld/ws/smartmetropolis/find-by-document?limit=`{limit}`&offset=`{offset}   `| Consulta entidades com base em um docuemnto json, que segue as regras de consultas do mongodb| Documento  json a ser usado como base para a pesquisa|



#### 1 - Salvar uma nova entidade

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


#### 2 - Atualizar uma entidade

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

#### 3 - Consultar uma entidade com base em seu id

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-id?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485

#### 4 - Consultar uma lista de entidades
`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis?limit=500&offset=0

#### 5 - Deletar uma entidade

`DELETE` http://localhost:8080/ngsi-ld/ws/smartmetropolis?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485

#### 6 - Adicionar uma nova propriedade a uma entidade

`POST` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia

{"type":"Property","value":"7.5"}

#### 7 - Atualizar o valor de uma propriedade pertencente a uma entidade

`PUT` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia

{"type":"Property","value":"8.5"}

#### 8 - Consultar uma propriedade pertencente a uma entidade

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia

#### 9 - Deletar uma propriedade pertencente a uma entidade

`DELETE` http://localhost:8080/ngsi-ld/ws/smartmetropolis/property?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&property-name=nota_geografia


#### 10 - Consultar entidades com base em suas propriedades

Essa consulta retorna todos os alunos com nota em matemática superior a 9  

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-property-filter?field=nota_matematica.value&operator=gt&value=9


#### 11 -  Adicionar um  novo relacionamento a uma entidade

`POST` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationaship-name=escola_anterior

{"type":"Relationaship","object":"urn:ngsi-ld:escola:123455321"} 


####  12 - Atualizar um relacionamento de uma entidade

`PUT` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationaship-name=escola_anterior

{"type":"Relationaship","object":"urn:ngsi-ld:escola:123459634"} 


#### 13 - Deletar um relacionamento de uma entidade

`DELETE` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationaship-name=escola_anterior

#### 14 -Consultar um relacionamento de uma entidade

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/relationaship?entity-id=urn:ngsi-ld:aluno:d8c1f8aa-6f12-4c3a-b318-593c13c61485&relationaship-name=escola

#### 15 -Consultar entidades com base em seus relacionamentos

Essa consulta retorna os alunos que estudam na escola `urn:ngsi-ld:escola:12345567`  

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-relationaship-filter?field=escola.object&operator=eq&value=urn:ngsi-ld:escola:12345567


#### 16 -Consultar entidades com base em uma query

Essa consulta retorna os alunos cuja nota em matematica é superior a 7 e que estudam na escola `urn:ngsi-ld:escola:12345567`

`GET` http://localhost:8080/ngsi-ld/ws/smartmetropolis/find-by-query?query=p*.nota_matematica.value$gte$7;r*.escola.object$eq$urn:ngsi-ld:escola:12345567


#### 17 - Consulta entidades com base em um documento json

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

