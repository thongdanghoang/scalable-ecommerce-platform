plugins {
  id("java-library")
  id("maven-publish")
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

publishing {
  publications {
    create<MavenPublication>("commons") {
      groupId = "vn.id.thongdanghoang.sep"
      artifactId = "commons"
      version = "1.0.2"

      from(components["java"])
    }
  }
}

repositories {
  mavenCentral()
}

dependencies {
  api(libs.commons.lang)
  api(libs.commons.text)
  api(libs.commons.collection)

  implementation(libs.hibernate.core)

  compileOnly(libs.lombok)
  annotationProcessor(libs.lombok)

  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

