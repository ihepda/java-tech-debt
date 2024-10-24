# java-tech-debt
Annotations and reporting tool in order to mark technical debt issues.

Technical depts are difficult to register. There are a lot of methods to register tech debs:
* ticket systems like jira or clickup
* TODO comment
* external document like Excel
* ecc...

The purpose of this project is to permit to mark technical debts directly in the code and to have a report to extract information by the code in order to analyze and to plan the resolution.

## Mark the code
java-tech-debt library supplies an annotation **io.github.ihepda.techdebt.TechDebt** used to register some information about the debt.

	comment
	author
	date
	severity
	type
	effort
	
```
	@TechDebt(severity = Severity.MAJOR, comment = "metodo")
	public void test(@TechDebt(severity = Severity.MINOR, comment = "parametro") String d) {
		int x = 0;
		System.out.println(x);
	}
```

You can use a single annotation or multiple annotation

```
@TechDebt(severity = Severity.MAJOR, comment = "classe")
@TechDebt(severity = Severity.MINOR, comment = "minor class", effort = Effort.MASSIVE, type = Type.PERFORMANCE)
public class MiaProva2 {
```


## Reports
Currently are available only 2 type of report:
* sysout: dump the report in the standard output stream (System.out)
* simple: dump the report in a file

### Custom report
You can use a custom report writing a class that implements **io.github.ihepda.techdebt.report.TechDebtReport** or extends **io.github.ihepda.techdebt.report.AbstractReport** overriding the methods init and report.
Add the report in the classpath of the javac and set the property techdebt.report.class with the fully qualified name of the class.
ie:

```
\Java\jdk-17.0.11\bin\javac -Atechdebt.report.class=my.processor.CustomReport -classpath techdebt-processor-0.0.8-SNAPSHOT.jar;my-processor.jar  test\MyAnnotatedClass.java
```

## How to generate the report
The report processor uses the javax.annotation.processing.Processor API. You can use it directly by the javac compiler adding the jar at the classpath or you can configure the maven-compiler-plugin, below some example:

### Using the processor by the javac

```
\Java\jdk-17.0.11\bin\javac -classpath techdebt-processor-0.0.8-SNAPSHOT.jar test\MyAnnotatedClass.java
```

### Using the processor with maven
Add the techdebt-processor as dependency in the pom

```
  	<dependency>
		<groupId>io.github.ihepda</groupId>
		<artifactId>techdebt-processor</artifactId>
  		<version>0.0.8-SNAPSHOT</version>
  		<scope>compile</scope>
  		<optional>true</optional>
  	</dependency>
```
Remember to set the scope to **compile** and the **optional** tag to true. This configuration will use this library only in compilation phase and it won't be added as runtime dependency.

```
  <build>
  	<plugins>
  		<plugin>
  			<groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <encoding>UTF-8</encoding>
                <annotationProcessorPaths>
                	<annotationProcessorPath>
						<groupId>io.github.ihepda</groupId>
						<artifactId>techdebt-processor</artifactId>
				  		<version>0.0.8-SNAPSHOT</version>
  						  
                	</annotationProcessorPath>
                	
                </annotationProcessorPaths>
                <compilerArgs>
                	<compilerArg>-Atechdebt.report.severity.order=true</compilerArg>
                	<compilerArg>-Atechdebt.report.output.dir=c:\Projects\tmp</compilerArg>
                	<compilerArg>-Atechdebt.report.class=simple</compilerArg>
                </compilerArgs>
            </configuration>
  		</plugin>
  	</plugins>
  </build>
```


### Configuration


| property  | description | default value |
|-----------|-------------|---------------|
| techdebt.report.class  | class to use to create the report  | sysout  |
| techdebt.report.severity.order  | boolean parameter, specifies the report has to order for severity  | false |
| techdebt.report.output.dir  | folder where to save the report file. Used only with techdebt.report.class=simple |  System.getProperty("java.io.tmpdir") |
| techdebt.report.output.name | name of the report file. Used only with techdebt.report.class=simple |  TechDebt.report |


