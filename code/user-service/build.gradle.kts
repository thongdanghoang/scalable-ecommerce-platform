plugins {
    id("java")
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.11.0"
    id("com.diffplug.spotless") version "8.1.0"
}

group = "vn.id.thongdanghoang.sep"
version = "1.0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("com.microsoft.playwright:playwright:1.57.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

spotless {
    // optional: limit format enforcement to just the files changed by this feature branch
    // ratchetFrom("origin/main")

    java {
        target("src/*/java/**/*.java")

        importOrderFile("../eclipse.importorder")

        removeUnusedImports()
        forbidWildcardImports()
        forbidModuleImports()

        eclipse("4.26")
            .configFile("../eclipse-format.xml")
            .sortMembersEnabled(true)
            .sortMembersOrder("SF,SI,SM,F,I,C,M,T")
            .sortMembersDoNotSortFields(false)
            .sortMembersVisibilityOrderEnabled(true)
            .sortMembersVisibilityOrder("B,R,D,V")

        formatAnnotations()
    }
}
