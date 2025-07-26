import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("vn.id.thongdanghoang.sep:commons:1.0.1")

    implementation(enforcedPlatform(rootProject.libs.quarkus.bom))
    implementation(libs.quarkus.hibernate.orm)
    implementation(libs.quarkus.hibernate.orm.panache)
    implementation(libs.quarkus.jdbc.postgresql)
    implementation(libs.quarkus.liquibase)
    implementation(libs.quarkus.rest)
    implementation(libs.quarkus.hibernate.validator)
    implementation(libs.quarkus.rest.jackson)
    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.config.yaml)
    implementation(libs.quarkus.micrometer)
    implementation(libs.quarkus.smallrye.openapi)
    implementation(libs.quarkus.smallrye.health)
    implementation(libs.quarkus.oidc)
    implementation(libs.quarkus.smallrye.context.propagation)
    implementation(libs.mapstruct)

    compileOnly(libs.lombok)
    compileOnly(libs.mapstruct.processor)

    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.processor)

    testCompileOnly(libs.lombok)

    testAnnotationProcessor(libs.lombok)

    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.rest.assured)
}

group = "vn.id.thongdanghoang"
version = "1.0.1"

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
