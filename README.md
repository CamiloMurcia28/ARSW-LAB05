### Escuela Colombiana de Ingeniería

## Autores: Camilo Nicolas Murcia Espinosa y Tomas Suarez Piratova

### Arquitecturas de Software



#### API REST para la gestión de planos.

En este ejercicio se va a construír el componente BlueprintsRESTAPI, el cual permita gestionar los planos arquitectónicos de una prestigiosa compañia de diseño. La idea de este API es ofrecer un medio estandarizado e 'independiente de la plataforma' para que las herramientas que se desarrollen a futuro para la compañía puedan gestionar los planos de forma centralizada.
El siguiente, es el diagrama de componentes que corresponde a las decisiones arquitectónicas planteadas al inicio del proyecto:

![](img/CompDiag.png)

Donde se definió que:

* El componente BlueprintsRESTAPI debe resolver los servicios de su interfaz a través de un componente de servicios, el cual -a su vez- estará asociado con un componente que provea el esquema de persistencia. Es decir, se quiere un bajo acoplamiento entre el API, la implementación de los servicios, y el esquema de persistencia usado por los mismos.

Del anterior diagrama de componentes (de alto nivel), se desprendió el siguiente diseño detallado, cuando se decidió que el API estará implementado usando el esquema de inyección de dependencias de Spring (el cual requiere aplicar el principio de Inversión de Dependencias), la extensión SpringMVC para definir los servicios REST, y SpringBoot para la configurar la aplicación:


![](img/ClassDiagram.png)

### Parte I

1. Integre al proyecto base suministrado los Beans desarrollados en el ejercicio anterior. Sólo copie las clases, NO los archivos de configuración. Rectifique que se tenga correctamente configurado el esquema de inyección de dependencias con las anotaciones @Service y @Autowired.

![image](https://github.com/user-attachments/assets/909415af-3a2e-46cd-b891-48a93e94e2a2)


2. Modifique el bean de persistecia 'InMemoryBlueprintPersistence' para que por defecto se inicialice con al menos otros tres planos, y con dos asociados a un mismo autor.

![image](https://github.com/user-attachments/assets/505cfc90-351b-48c5-bc66-b3ab74fe3085)


3. Configure su aplicación para que ofrezca el recurso "/blueprints", de manera que cuando se le haga una petición GET, retorne -en formato jSON- el conjunto de todos los planos. Para esto:

	* Modifique la clase BlueprintAPIController teniendo en cuenta el siguiente ejemplo de controlador REST hecho con SpringMVC/SpringBoot:

	```java
	@RestController
	@RequestMapping(value = "/url-raiz-recurso")
	public class XXController {
    
        
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoXX(){
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }        
	}

	```
	* Haga que en esta misma clase se inyecte el bean de tipo BlueprintServices (al cual, a su vez, se le inyectarán sus dependencias de persisntecia y de filtrado de puntos).

	![image](https://github.com/user-attachments/assets/ca4b4dea-5c62-46ca-89f6-508043eff9a2)


4. Verifique el funcionamiento de a aplicación lanzando la aplicación con maven:

	```bash
	$ mvn compile
	$ mvn spring-boot:run
	
	```
	Y luego enviando una petición GET a: http://localhost:8080/blueprints. Rectifique que, como respuesta, se obtenga un objeto jSON con una lista que contenga el detalle de los planos suministados por defecto, y que 	se haya aplicado el filtrado de puntos correspondiente.

	![image](https://github.com/user-attachments/assets/50f3dd92-6a5a-4f25-8acd-b520353c4162)

	![image](https://github.com/user-attachments/assets/fc1a1a4c-489a-45e6-bcba-94ded7e222fe)


5. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}, el cual retorne usando una representación jSON todos los planos realizados por el autor cuyo nombre sea {author}. Si no existe dicho autor, se debe responder con el código de error HTTP 404. Para esto, revise en [la documentación de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html), sección 22.3.2, el uso de @PathVariable. De nuevo, verifique que al hacer una petición GET -por ejemplo- a recurso http://localhost:8080/blueprints/juan, se obtenga en formato jSON el conjunto de planos asociados al autor 'juan' (ajuste esto a los nombres de autor usados en el punto 2).

   	![image](https://github.com/user-attachments/assets/c2810e81-47a0-4ad1-9208-15bc532daeea)

	![image](https://github.com/user-attachments/assets/c8596a8a-9539-4991-a60c-03a233cd67bf)
	![image](https://github.com/user-attachments/assets/85bf3ebd-828e-4baf-a7df-b82003efc8b7)
	![image](https://github.com/user-attachments/assets/4b4daa12-a1fb-4777-a63a-c948aaf02b03)



7. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}/{bpname}, el cual retorne usando una representación jSON sólo UN plano, en este caso el realizado por {author} y cuyo nombre sea {bpname}. De nuevo, si no existe dicho autor, se debe responder con el código de error HTTP 404. 

	![image](https://github.com/user-attachments/assets/9381332c-4304-4e39-88b9-1c164496bd92)

	![image](https://github.com/user-attachments/assets/6ba344d9-4bd2-4dc8-b75e-eaecde139fc8)
	![image](https://github.com/user-attachments/assets/dc28d8b6-f3ed-4793-b445-0b9e6ab2dc1d)
	![image](https://github.com/user-attachments/assets/79a1ae8e-e2a7-4dd1-912e-cb43efe2b2cd)
	![image](https://github.com/user-attachments/assets/07670e8d-d687-4b3e-b068-43b941bb29f8)





### Parte II

1.  Agregue el manejo de peticiones POST (creación de nuevos planos), de manera que un cliente http pueda registrar una nueva orden haciendo una petición POST al recurso ‘planos’, y enviando como contenido de la petición todo el detalle de dicho recurso a través de un documento jSON. Para esto, tenga en cuenta el siguiente ejemplo, que considera -por consistencia con el protocolo HTTP- el manejo de códigos de estados HTTP (en caso de éxito o error):

	```	java
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<?> manejadorPostRecursoXX(@RequestBody TipoXX o){
        try {
            //registrar dato
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.FORBIDDEN);            
        }        
 	
	}
	```

 ![image](https://github.com/user-attachments/assets/6ea32d00-5c7a-4867-b0f0-f52a42a3a8d2)


2.  Para probar que el recurso ‘planos’ acepta e interpreta
    correctamente las peticiones POST, use el comando curl de Unix. Este
    comando tiene como parámetro el tipo de contenido manejado (en este
    caso jSON), y el ‘cuerpo del mensaje’ que irá con la petición, lo
    cual en este caso debe ser un documento jSON equivalente a la clase
    Cliente (donde en lugar de {ObjetoJSON}, se usará un objeto jSON correspondiente a una nueva orden:

	```	
	$ curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://URL_del_recurso_ordenes -d '{ObjetoJSON}'
	```	

	Con lo anterior, registre un nuevo plano (para 'diseñar' un objeto jSON, puede usar [esta herramienta](http://www.jsoneditoronline.org/)):
	

	Nota: puede basarse en el formato jSON mostrado en el navegador al consultar una orden con el método GET.

![image](https://github.com/user-attachments/assets/0237379b-a853-4b4e-986b-e953ad1c4845)



4. Teniendo en cuenta el autor y numbre del plano registrado, verifique que el mismo se pueda obtener mediante una petición GET al recurso '/blueprints/{author}/{bpname}' correspondiente.

![image](https://github.com/user-attachments/assets/9b105453-b4ad-4de9-a4f0-bcb2a648a573)


5. Agregue soporte al verbo PUT para los recursos de la forma '/blueprints/{author}/{bpname}', de manera que sea posible actualizar un plano determinado.
![image](https://github.com/user-attachments/assets/68f3b9f3-2d7c-4caa-ac87-cccd85868e97)


![image](https://github.com/user-attachments/assets/d5918d37-59d3-4e91-a5fe-36a732af9360)

Actualizamos el plano1 de tomas

![image](https://github.com/user-attachments/assets/247125a6-602e-41db-8374-3be832010e0a)



### Parte III

El componente BlueprintsRESTAPI funcionará en un entorno concurrente. Es decir, atederá múltiples peticiones simultáneamente (con el stack de aplicaciones usado, dichas peticiones se atenderán por defecto a través múltiples de hilos). Dado lo anterior, debe hacer una revisión de su API (una vez funcione), e identificar:

* Qué condiciones de carrera se podrían presentar?

Cuando se necesite consultar o agregar nuevos planos, se puede experimentar un escenario donde se acceda al HashMap y donde se alamcenen los planos.

  ![image](https://github.com/user-attachments/assets/8ba873c1-1934-4aa0-a1cd-998b1c016070)

* Cuales son las respectivas regiones críticas?

Sabiendo que el almacenamiento persistente de los planos no es seguro para los hilos(thread-safe) surge una condicion carrera cuando 2 solicitudes intentan agregar un plano al mismo tiempo con el mismo nombre y autor.

![image](https://github.com/user-attachments/assets/69436a20-4aa2-4405-b402-9ae32908618b)


Para asegurar el funcionamiento thread-safe, vamos a cambiar el tipo de HashMap a un ConcurrentHashMap, ya que es una coleccion que ya implementa la concurrecia y que ademas garantiza un funcionamiento thread-safe. En cuanto a la condicion carrera, vamos a sustituir el metodo put() por el metodo putIfAbsent(), que es usado para mapear una llave y valor especifico, solo si esta llave no existe o ha sido mapeada como null, de esta forma se previena la condicion carrera.

![image](https://github.com/user-attachments/assets/d6013b89-4a3b-443d-beaf-36b09b052929)

![image](https://github.com/user-attachments/assets/eef00bdf-9ea5-425f-8452-cad11f118279)


Ajuste el código para suprimir las condiciones de carrera. Tengan en cuenta que simplemente sincronizar el acceso a las operaciones de persistencia/consulta DEGRADARÁ SIGNIFICATIVAMENTE el desempeño de API, por lo cual se deben buscar estrategias alternativas.

Escriba su análisis y la solución aplicada en el archivo ANALISIS_CONCURRENCIA.txt

#### Criterios de evaluación

1. Diseño.
	* Al controlador REST implementado se le inyectan los servicios implementados en el laboratorio anterior.
	* Todos los recursos asociados a '/blueprint' están en un mismo Bean.
	* Los métodos que atienden las peticiones a recursos REST retornan un código HTTP 202 si se procesaron adecuadamente, y el respectivo código de error HTTP si el recurso solicitado NO existe, o si se generó una excepción en el proceso (dicha excepción NO DEBE SER de tipo 'Exception', sino una concreta)	
2. Funcionalidad.
	* El API REST ofrece los recursos, y soporta sus respectivos verbos, de acuerdo con lo indicado en el enunciado.
3. Análisis de concurrencia.
	* En el código, y en las respuestas del archivo de texto, se tuvo en cuenta:
		* La colección usada en InMemoryBlueprintPersistence no es Thread-safe (se debió cambiar a una con esta condición).
		* El método que agrega un nuevo plano está sujeta a una condición de carrera, pues la consulta y posterior agregación (condicionada a la anterior) no se realizan de forma atómica. Si como solución usa un bloque sincronizado, se evalúa como R. Si como solución se usaron los métodos de agregación condicional atómicos (por ejemplo putIfAbsent()) de la colección 'Thread-Safe' usada, se evalúa como B.
