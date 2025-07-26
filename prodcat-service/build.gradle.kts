import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
  java
  id("io.quarkus") version libs.versions.quarkus.get()
}

repositories {
  mavenCentral()
  mavenLocal()
}

dependencies {
  implementation(project(":commons"))

  implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${libs.versions.quarkus.get()}"))
  implementation("io.quarkus:quarkus-rest")
  implementation("io.quarkus:quarkus-hibernate-validator")
  implementation("io.quarkus:quarkus-smallrye-openapi")
  implementation("io.quarkus:quarkus-rest-jackson")
  implementation("io.quarkus:quarkus-oidc")
  implementation("io.quarkus:quarkus-config-yaml")
  implementation("io.quarkus:quarkus-hibernate-orm-panache")
  implementation("io.quarkus:quarkus-liquibase")
  implementation("io.quarkus:quarkus-smallrye-health")
  implementation("io.quarkus:quarkus-smallrye-context-propagation")
  implementation("io.quarkus:quarkus-jdbc-postgresql")
  implementation("io.quarkus:quarkus-arc")
  implementation("io.quarkus:quarkus-hibernate-orm")

  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.rest-assured:rest-assured")
}

group = "vn.id.thongdanghoang"
version = "1.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
  systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")

  testLogging {
    events("passed", "skipped", "failed")
    showCauses = true
    showExceptions = true
    exceptionFormat = TestExceptionFormat.FULL
    showStackTraces = true
  }
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-parameters")
}
