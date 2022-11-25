plugins {
    id("lserv.common")
    id("application")
}

dependencies {
    implementation(project(":lserv"))
}

application {
    // Define the main class for the application.
    mainClass.set("lserv.app.AppKt")
}
