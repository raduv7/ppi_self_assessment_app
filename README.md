<h1>On how to run</h1>

<h2>0. Create a Postgresql database named self_assessment_tool</h2><br>

<h2>1. Create an ```application.properties``` file in ```src/main/resources```</h2><br>

<h2>2. Inside the file, add the following lines:</h2>
```
spring.profiles.active=dev
# suppress inspection "SpellCheckingInspection" for whole file

spring.datasource.url=jdbc:postgresql://localhost:5432/self_assessment_tool
spring.datasource.username=<<db_username>>
spring.datasource.password=<<db_password>>


spring.jpa.hibernate.ddl-auto=update

# suppress inspection "SpringBootApplicationProperties"
# spring.config.import=classpath:/profiles/application-${spring.profiles.active}.properties


spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=true
spring.jpa.open-in-view=false

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=32MB
spring.servlet.multipart.max-request-size=32MB

passwordPepper=extraUltraMegaBen10PlasmaUltraSecretPepper
jwtSecretKey=<<jwt_secret_key, needs to be 128 chars long>>
ai_model.path=ai_model/<path/to/dir/>/main.py
user_data.path=src/main/resources/userData/
project_root.path=<<project_root_path>>
```
<br>

<h2>3. Replace the four ```<<values>>``` above</h2>
The project_root.path is the path to the root of the java spring project
<br>

<h2>4. Run the application</h2>

<h2>5. You can add new models to run from the ```AssessmentService``` class 
from ```src/main/java/sas/business/service/assess``` package</h2>