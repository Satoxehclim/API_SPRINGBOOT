# API con Springboot
## Acciones previas
Esta api requiere de acciones previas para poder ser ejecutada. 
### 1. Preparar la DB en MySQL
Debemos crear la base de datos vacia que utilizaremos en el proyecto. Es importante que se encuentre completamente vacia, ya que la api se encargara de realizar la creacion de las tablas y relaciones de las tablas con ``spring.jpa.hibernate.ddl-auto=update``. En el caso personal de prueba se utilizo el nombre ``PruebaTecnica``
### 2. Preparar las variables de entorno
En el caso de las variables de entorno las podemos ubicar en el archivo **application.properties** con la siguiente estructura:
```ini
spring.application.name=[NOMBRE_DE_LA_APP]
## Configuración de la base de datos MySQL
spring.datasource.url=jdbc:mysql://[IP_SERVIDOR]:[PUERTO]/[NOMBRE_DE_LA_DB]
spring.datasource.username=[USUARIO_CON_ACCESO_A_LA_DB]
spring.datasource.password=[CONTRASEÑA_DEL_USUARIO]
spring.jpa.hibernate.ddl-auto=update
## Configuraciones para JWT
jwt.secret=[CONTRASEÑA_PARA_JWT]
jwt.expiration.time=3600000
## Encryption keys para AES-256
encryption.key=[CADENA_DE_32_CARACTERES_PARA_ENCRIPTADO]
encryption.salt=[CADENA_DE_32_CARACTERES_PARA_ENCRIPTADO]
```
### **NOTA:** las cadenas pueden ser generadas con [la herramienta de Norton](https://us.norton.com/feature/password-generator)
---
## Endpoints
Dentro de los endpoints tenemos los siguientes:
### Regresa los usuarios.
```apacheconf
GET /users
```
---
### Regresa los usuarios ordenados por el parametro ``sortedBy`` de manera ascendente.
```apacheconf
GET /users?sortedBy=[email|id|name|phone|tax_id|created_at] 
```
**Ejemplo:**
- Si queremos ordenar los usuarios por correo usamos:
```apacheconf
GET /users?filter=email+co+gmail
```
---
### Regresa los usuarios filtrados por el parametro ``filter``.
```apacheconf
GET /users?filter=[email|id|name|phone|tax_id|created_at]+[co|eq|sw|ew]+[value]
```
**EJEMPLOS:**
- Si queremos filtrar los usuarios que contengan ``5709`` en su tax_id usamos:
```apacheconf
GET /users?filter=tax_id+co+5709
```
- Si queremos filtrar los usuarios que tengan exactamente su id usamos:
```apacheconf
GET /users?filter=id+eq+[ID_A_BUSCAR]
```
- Si queremos filtrar los usuarios que contengan ``Abra`` al inicio de su nombre:
```apacheconf
GET /users?filter=name+sw+Abra
```
- Si queremos filtrar los usuarios que contengan ``gmail.com`` al final de su correo usamos:
```apacheconf
GET /users?filter=email+ew+gmail.com
```
---
### Guarda un nuevo usuario en la DB
```apacheconf
POST /users 
```
Este debe ser acompañado de un body de tipo ``JSON``
```JSON
{
    "email": "[CORREO_DEL_USUARIO]",
    "name": "[NOMBRE_DEL_USUARIO]",
    "phone": "[NUMERO_DE_TELEFONO_EN_FORMATO_{+1 55 555 555 55}]",
    "password": "[CONTRASEÑA_DEL_USUARIO]",
    "tax_id": "[RFC_DEL_USUARIO_DE_FORMATO_UNICO]",
    "addresses": [
        {
            "name": "[NOMBRE_DE_DIRECCION]",
            "street": "[CALLE_DE_DIRECCION]",
            "country_code": "[CODIGO_DE_PAIS_{ej. MX}]"
        },
        {
            "name": "[NOMBRE_DE_DIRECCION]",
            "street": "[CALLE_DE_DIRECCION]",
            "country_code": "[CODIGO_DE_PAIS_{ej. MX}]"
        }
  ]
}
```
Es importante saber que puede tener todas las direcciones necesarias para completar los datos.
### **NOTA:** LOS CAMPOS PUEDEN SER NULOS EXCEPTO POR EL RFC

---
### Actualiza un usuario de la DB
```apacheconf
PATCH /users/{id} 
```
Este debe ser acompañado de un body de tipo ``JSON``. Es importante decir que estos campos pueden omitirse si no se desea cambiar algo del usuario en cuestion
```JSON
{
    "email": "[CORREO_DEL_USUARIO]",
    "name": "[NOMBRE_DEL_USUARIO]",
    "phone": "[NUMERO_DE_TELEFONO_EN_FORMATO_{+1 55 555 555 55}]",
    "password": "[CONTRASEÑA_DEL_USUARIO]",
    "tax_id": "[RFC_DEL_USUARIO_DE_FORMATO_UNICO]",
    "addresses": [
        {
            "name": "[NOMBRE_DE_DIRECCION]",
            "street": "[CALLE_DE_DIRECCION]",
            "country_code": "[CODIGO_DE_PAIS_{ej. MX}]"
        },
        {
            "name": "[NOMBRE_DE_DIRECCION]",
            "street": "[CALLE_DE_DIRECCION]",
            "country_code": "[CODIGO_DE_PAIS_{ej. MX}]"
        }
    ]
}
```
---
### Borra un usuario de la DB
Solo es necesario colocar el id en la URL para borrar al usuario
```apacheconf
DELETE /users/{id}
```
---
### Inicio de sesión
```apacheconf
POST /users/login
```
El inicio de sesion debe ser acompañado con el ``JSON`` siguiente
```JSON
{
    "tax_id": "[RFC_DEL_USUARIO_DE_FORMATO_UNICO]",
    "password": "[CONTRASEÑA_DEL_USUARIO]",
}
```
Al iniciar sesion nos regresara un `JSON` con un Token de autenticacion, el usuario que inicio sesion y un mensaje de confirmacion.
```JSON
{
	"message": "login successful",
	"user": {
		"addresses": [
			{
				"name": "[NOMBRE_DE_DIRECCION]",
                "id": [ID_DE_LA_DIRECCION],
                "street": "[CALLE_DE_DIRECCION]",
                "country_code": "[CODIGO_DE_PAIS_{ej. MX}]"
			},
			{
				"name": "[NOMBRE_DE_DIRECCION]",
                "id": [ID_DE_LA_DIRECCION],
                "street": "[CALLE_DE_DIRECCION]",
                "country_code": "[CODIGO_DE_PAIS_{ej. MX}]"
			}
		],
		"created_at": "[FECHA_DE_CREACION_DEL_USUARIO]",
		"email": "[CORREO_DEL_USUARIO]",
		"id": [ID_DEL_USUARIO],
		"name": "[NOMBRE_DEL_USUARIO]",
		"password": null,
		"phone": "[NUMERO_DE_TELEFONO_EN_FORMATO_{+1 55 555 555 55}]",
		"tax_id": "[RFC_DEL_USUARIO_DE_FORMATO_UNICO]",
	},
	"token": "[TOKEN_PERSONALIZADO_POR_SESION]"
}   
```
---
## Notas importantes
### 1. Las contraseñas se cifran para su ingreso en la DB con el algoritmo AES256 y no son devueltas en **NINGUNA DE LAS PETICIONES HTTP**
### 2. El campo `created_at` estaconfigurado en la zona horaria `GMT+3` corespondiente a **Madagascar** y en formato `dd-MM-yyyy HH:mm`
### 3. El campo `tax_id` esta validado para ser **unico** y para tener el formato de RFC con la expresion regular 
```re
^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$
```
### 4. El campo `phone` esta validado para tener el formato de 10 digitos con codigo de pais y tambien sin condigo de pais para tener el formato `+1 55 555 555 55` o el formato `+12 55 555 555 55` o el formato `+123 55 555 555 55` o el formato `55 555 555 55` con la expresion regular 
```re
^(\+[0-9]{1,3} )?[0-9]{2} [0-9]{3} [0-9]{3} [0-9]{2}$
```
### 5. Se incluye una coleccion de peticiones `HTTP` para el programa [Insomnia](https://insomnia.rest/) con compatibilidad para ser usadas en [Postman](https://www.postman.com/) llamado `Insomnia_HTTP_Colection_2026-03-22.yaml`
### 6. Este proyecto fue realizado con las siguientes tecnologias:
- **Java** `JDK 21.0.10`
- **Base de Datos:** `MySQL 8.0.45`
- **Framework:** [`Springboot 4.0.4`](https://start.spring.io/)
- `Maven 3.9.14`
- `Java JWT 4.4.0`
- `JPA 7.2.7.Final`
### 7. ESTE PROYECTO FUE REALIZADO POR ABRAHAM ROMAN RAMIREZ Y PUEDE SER ENCONTRADO EN [GITHUB](https://github.com/Satoxehclim/Prueba-Tecnica/tree/main) PARA VERIFICAR TODOS LOS CAMBIOS Y VERSIONES DE DESARROLLO