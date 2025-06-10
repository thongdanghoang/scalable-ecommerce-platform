dependencies {
//    implementation(enforcedPlatform(libs.quarkus.blaze.persistence.bom))
//    implementation(libs.blaze.persistence.quarkus)
//    runtimeOnly(libs.blaze.persistence.hibernate)

    implementation(libs.quarkus.hibernate.reactive.panache)
    implementation(libs.quarkus.reactive.pg.client)

    implementation(libs.quarkus.rest)
    implementation(libs.quarkus.rest.jsonb)
    implementation(libs.quarkus.hibernate.validator)

    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.config.yaml)
    implementation(libs.quarkus.micrometer)
    implementation(libs.quarkus.smallrye.openapi)
    implementation(libs.quarkus.smallrye.health)

    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.rest.assured)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)

    implementation(libs.commons.lang)
    implementation(libs.commons.collection)
    testImplementation("org.mockito:mockito-core:5.18.0")
}