import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete

plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    compileOnly("org.projectlombok:lombok:1.18.42")

    implementation("io.quarkus:quarkus-apicurio-registry-avro")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-avro")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-messaging-kafka")
    implementation("io.quarkus:quarkus-oidc")
    implementation("io.quarkus:quarkus-quartz")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-smallrye-context-propagation")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))

    testAnnotationProcessor("org.projectlombok:lombok:1.18.42")

    testCompileOnly("org.projectlombok:lombok:1.18.42")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "vn.id.thongdanghoang.sep"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

quarkus {
    quarkusBuildProperties.put("quarkus.hibernate-orm.query.fail-on-pagination-over-collection-fetch", "true")
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

val avroTempDir = file("src/main/avro")
val copyAvroSchemas by tasks.registering(Copy::class) {
    from("../schemas/avro")
    into(avroTempDir)
    include("**/*.avsc")
}
val deleteAvroSchemas by tasks.registering(Delete::class) {
    delete(avroTempDir)
}
tasks.named("quarkusGenerateCode") {
    dependsOn(copyAvroSchemas)
    finalizedBy(deleteAvroSchemas)
}
