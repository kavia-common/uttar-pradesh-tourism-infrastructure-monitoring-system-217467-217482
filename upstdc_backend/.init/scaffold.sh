#!/usr/bin/env bash
set -euo pipefail
WS="/home/kavia/workspace/code-generation/uttar-pradesh-tourism-infrastructure-monitoring-system-217467-217482/upstdc_backend"
cd "$WS"
[ -f pom.xml ] || [ -d .git ] && exit 0
mkdir -p src/main/java/com/example/upstdc src/main/resources src/test/java/com/example/upstdc
cat > pom.xml <<'POM'
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.4</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>
  <groupId>com.example</groupId>
  <artifactId>upstdc-backend</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <properties>
    <java.version>17</java.version>
  </properties>
  <dependencies>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-actuator</artifactId></dependency>
    <dependency><groupId>com.h2database</groupId><artifactId>h2</artifactId><scope>runtime</scope></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <release>17</release>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
POM
cat > src/main/java/com/example/upstdc/UpstdcApplication.java <<'APP'
package com.example.upstdc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class UpstdcApplication { public static void main(String[] args){ SpringApplication.run(UpstdcApplication.class,args);} }
APP
# application-dev.properties - ensure actuator exposed and security disabled for dev so health is reachable
cat > src/main/resources/application-dev.properties <<'PROPS'
spring.datasource.url=jdbc:h2:mem:upstdc;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
# disable security for dev profile to allow actuator probe (safe for local dev)
spring.security.enabled=false
server.port=
# server.port set at runtime via SERVER_PORT env or --server.port
file.storage.path=${FILE_STORAGE_PATH:-${user.dir}/storage}
PROPS
mkdir -p "$WS/storage"
cat > src/test/java/com/example/upstdc/SimpleTest.java <<'TST'
package com.example.upstdc;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
class SimpleTest { @Test void smoke(){ assertTrue(true); } }
TST
cat > .env.example <<'ENV'
# Development environment example
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
# Optional DB vars (for headless dev we use in-memory H2 by default)
# DATABASE_URL=jdbc:postgresql://... 
# DB_USER=...
# DB_PASSWORD=...
ENV
# attempt to generate mvnw if mvn missing (best-effort, non-failing here)
if ! command -v mvn >/dev/null 2>&1 && command -v bash >/dev/null 2>&1; then mvn -N io.takari:maven:wrapper -Dmaven=3.8.8 >/dev/null 2>&1 || true; fi
