plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // REST Assured core
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:json-path:5.4.0")
    testImplementation("io.rest-assured:xml-path:5.4.0")

    implementation("io.rest-assured:rest-assured:5.4.0")

    // TestNG
    testImplementation("org.testng:testng:7.10.0")

    // Jackson untuk JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}

tasks.test {
    useTestNG() {
        suites("src/test/resources/testng.xml")
    }
}
