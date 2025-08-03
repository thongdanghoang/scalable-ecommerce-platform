dependencies {
    implementation(libs.quarkus.liquibase)
    implementation(libs.quarkus.jdbc.postgresql)
    implementation(libs.quarkus.config.yaml)

    testImplementation(libs.quarkus.junit5)
}
