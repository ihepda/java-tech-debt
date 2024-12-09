# java-tech-debt

| Resource | Status |
| ---- | ------ |
|Parent project|[![Maven Central parent](https://maven-badges.herokuapp.com/maven-central/io.github.ihepda/tech-debt-parent/badge.svg?style=flat)](https://central.sonatype.com/artifact/io.github.ihepda/tech-debt-parent)|
|Annotation project|[![Maven Central parent](https://maven-badges.herokuapp.com/maven-central/io.github.ihepda/tech-debt-annotation/badge.svg?style=flat)](https://central.sonatype.com/artifact/io.github.ihepda/tech-debt-annotation)|
|Processor project|[![Maven Central parent](https://maven-badges.herokuapp.com/maven-central/io.github.ihepda/tech-debt-processor/badge.svg?style=flat)](https://central.sonatype.com/artifact/io.github.ihepda/tech-debt-processor)|
|Maven plugin project|[![Maven Central parent](https://maven-badges.herokuapp.com/maven-central/io.github.ihepda/tech-debt-maven-plugin/badge.svg?style=flat)](https://central.sonatype.com/artifact/io.github.ihepda/tech-debt-maven-plugin)|


The *java-tech-debt* project is an annotation and reporting tool designed to identify and manage technical debts in code. Technical debts are an inevitable reality in software development, often overlooked or forgotten over time.

## Why Technical Debts Matter 
Technical debts represent compromises in the code that, if unmanaged, can lead to maintenance and scalability issues. Often, these debts are noted in external tools like Jira or ClickUp, but other times they are completely forgotten, accumulating and further complicating the development process.

## Project Features

* **Annotations**: Allows marking parts of the code that contain technical debts.
* **Reporting**: Generates detailed reports to monitor and manage technical debts over time.


## Conclusion 

The java-tech-debt project offers an effective solution to keep code clean and manageable, helping development teams track technical debts and plan refactoring efforts in a more structured and informed way.

## Marking Technical Debts with java-tech-debt

### Introduction

The **java-tech-debt** project allows you to annotate and manage technical debts directly in your code. This tutorial will guide you through the steps needed to mark technical debts using the annotations provided by the project.

### Step 1: Add the Maven Dependency

Ensure you have the **java-tech-debt** dependency in your *pom.xml* file:

```
<dependency>
    <groupId>io.github.ihepda</groupId>
    <artifactId>tech-debt-annotation</artifactId>
    <version>1.0.1</version>
    <scope>compile</scope>
  	 <optional>true</optional>
</dependency>
```
Remember to set the scope to **compile** and the **optional** tag to true. This configuration will use this library only in compilation phase and it won't be added as runtime dependency.

### Step 2: Import the Annotation
In your Java file, import the necessary annotation:

	import io.github.ihepda.techdebt.annotations.TechDebt;
	
### Step 3: Annotate the Code with Technical Debts

Use the annotation to mark technical debts in your code. Here is an example:

```
@TechDebt(
    description = "Refactor this method to improve readability",
    date = "2024-10-28",
    owner = "developer@example.com",
    severity= Severity.MAJOR
)
public void someMethod() {
    // Code that needs refactoring
}
```

You can use a single annotation or multiple annotation

```
@TechDebt(severity = Severity.MAJOR, comment = "classe")
@TechDebt(severity = Severity.MINOR, comment = "minor class", effort = Effort.MASSIVE, type = Type.PERFORMANCE)
public class MyClass {
```
You can mark issues in statement blocks, unfortunately you can't use annotation in statement but **java-tech-debs** supply a method to mark some statement blocks using the *refComment* attribute.
With the *refComment* you can mark a comment as a technical debt comment that the system will use to identify the location of the block to mark, in the example the *refComment="AX"* indicates a block that starts with a comment @TD-AX and ends with a line comment #TD-AX (AX is the code).

Important is: the start block is a multi-line java comment (not javadoc comment) with the first line that contains the block code @TD-{code}, all comment below this row will be used to set the *comment* attribute in the tecnical debt information.
The end of the block must be a single line comment starts with #TD-{code}

```
	@TechDebt(
			comment = "strange method2", 
			author = "CDA", 
			severity = TechDebt.Severity.TRIVIAL, 
			type = Type.MAINTAINABILITY, 
			effort = Effort.MASSIVE,
			refComment = "AX")
	public void execute2(Map<String, Object> params) {
		@TechDebt(comment = "bad variable", author = "me", severity = TechDebt.Severity.MAJOR)
		var a = 10;
		var b = 20;

		/*
		 * @TD-AX 
		 * This is a test comment for AX
		 * 
		 * to check
		 */
		this.execute(null);
		// #TD-AX
		this.execute(null);

	}

```



### Step 4: Generate Reports

Once you have annotated the code, you can generate reports to monitor and manage technical debts. Run the Maven command to generate the report:

	mvn tech-debt:report

or

	mvn site

This command will analyze the annotations in your code and generate a detailed report of the technical debts.

Or, you can add the plugin in the *reporting* section of your *pom.xml*

```
	<reporting>
		<plugins>
			<plugin>
		        <groupId>io.github.ihepda</groupId>
		        <artifactId>tech-debt-maven-plugin</artifactId>
		  			<version>1.0.1</version>
				
			</plugin>
		</plugins>
	</reporting>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-site-plugin</artifactId>
				<version>3.12.1</version>

			</plugin>
		</plugins>
	</build>
```

#### Filtering
issue: https://github.com/ihepda/java-tech-debt/issues/6

The report permits to set a filter in order to match only interested technical debts, for example technical debts sith a severity MAJOR or CRITICAL.

The filter is an 'SQL like filter', this is, you can use:
* common operators : =, !=, <=, <, >=, >
* like operator with or without the not keyword
* in operator with or without the not keyword
* and or conjunctions
* grouping expression with '(' and ')'

Below an example
```
comment like '%test%' and severity >= 'MAJOR' or author in ('CDA', 'LA')

comment not like '%test%' and (severity = 'MAJOR' or author not in ('CDA', 'LA'))
```
In order to activate the filter you have to add the *filter* configuration in the plugin
```
	<reporting>
		<plugins>
			<plugin>
		        <groupId>io.github.ihepda</groupId>
		        <artifactId>tech-debt-maven-plugin</artifactId>
		  		<version>1.0.1</version>
				<configuration>
                    			<filter>comment like '%test%'</filter>
				</configuration>
			</plugin>
		</plugins>
	</reporting>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-site-plugin</artifactId>
				<version>3.12.1</version>

			</plugin>
		</plugins>
	</build>
```


### Conclusion
Annotating technical debts directly in the code with **java-tech-debt** helps keep the code clean and manageable. Following these steps will allow you to track technical debts and plan refactoring efforts in a more structured way.

## Examples

Please see the example in **example** folder
