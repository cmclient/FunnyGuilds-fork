dependencies {
    implementation(project(":nms:api"))
    implementation(project(":nms:v1_8R3"))

    shadow("org.spigotmc:spigot:1.11-R0.1-SNAPSHOT") // 1.11.2 is latest
}