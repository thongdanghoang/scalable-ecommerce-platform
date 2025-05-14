version = "1.0.0-SNAPSHOT"
group = "vn.id.thongdanghoang"

plugins {
    java
    id("io.quarkus") version libs.versions.quarkus.get()
}

allprojects {

    repositories {
        mavenCentral()
        mavenLocal()
    }

    apply(plugin = "java")
    apply(plugin = "io.quarkus")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    dependencies {
        implementation(enforcedPlatform(rootProject.libs.quarkus.bom))
    }

    tasks.withType<Test> {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")

        testLogging {
            events("passed", "skipped", "failed")
            showCauses = true
            showExceptions = true
            showStackTraces = true
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }
}
