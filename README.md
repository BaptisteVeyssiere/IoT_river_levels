co808 assessment 3

MariaDB [co838]> describe monitoring_stations;
+------------+--------------+------+-----+---------+-------+
| Field      | Type         | Null | Key | Default | Extra |
+------------+--------------+------+-----+---------+-------+
| station_id | varchar(12)  | NO   | PRI | NULL    |       |
| latitude   | decimal(8,6) | YES  |     | NULL    |       |
| longitude  | decimal(8,6) | YES  |     | NULL    |       |
+------------+--------------+------+-----+---------+-------+

MariaDB [co838]> describe subscriber;
+--------------+--------------+------+-----+---------+-------+
| Field        | Type         | Null | Key | Default | Extra |
+--------------+--------------+------+-----+---------+-------+
| phone_number | int(11)      | NO   | PRI | NULL    |       |
| latitude     | decimal(8,6) | YES  |     | NULL    |       |
| longitude    | decimal(8,6) | YES  |     | NULL    |       |
+--------------+--------------+------+-----+---------+-------+

username@129.12.44.32

pom.xml for maven deploying from eclipse and intellij (edit button to see text properly)

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>mycode.feedthepenguin.org</groupId>
  <artifactId>mycode.feedthepenguin.org</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <profiles>
    <profile>
        <id>tomcat-localhost</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <tomcat-server>tomcatServer</tomcat-server>
            <tomcat-url>http://mycode.feedthepenguin.org:8080/manager/text</tomcat-url>
        </properties>
    </profile>
</profiles>
<dependencies>
</dependencies>
<distributionManagement>
		<repository>
			<id>co838</id>
			<url>http://129.12.44.32</url>
		</repository>
		
	</distributionManagement>
<packaging>war</packaging>
  <build>
  
    <sourceDirectory>src</sourceDirectory>
    <plugins>
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.7.0</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
      <plugin>
        <artifactId>maven-war-plugin</artifactId>
        <version>3.0.0</version>
        <configuration>
          <warSourceDirectory>WebContent</warSourceDirectory>
        </configuration>
      </plugin>
<plugin>
    <!-- enable deploying to tomcat -->
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>tomcat-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <server>${tomcat-server}</server>
        <url>${tomcat-url}</url>
    </configuration>
</plugin>
      <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.0</version>
        </plugin>
  


<!-- Deploy wars to Tomcat 7 with mvn tomcat7:deploy or tomcat7:redeploy -->
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.1</version>
    <configuration>
        <url>http://129.12.44.32:8080/manager/text</url>
        <username>rob</username>
        <password>portishead</password>
        <path>/rob</path>
    </configuration>
</plugin>
  </plugins>
  </build>
	<repositories>
      <repository>
    <id>sonatype-oss-public</id>
    <url>https://oss.sonatype.org/content/groups/public/</url>
    <releases>
        <enabled>true</enabled>
    </releases>
</repository>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	
</repositories>

</project>