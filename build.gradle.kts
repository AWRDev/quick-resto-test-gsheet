plugins {
	java
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.awrdev"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17


repositories {
	mavenCentral()
	maven { url = uri("https://www.jitpack.io/" ) }
}



dependencies {
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly ("org.springframework.boot:spring-boot-devtools")
	implementation("com.fathzer:javaluator:3.0.3")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register("listrepos") {
	doLast {
		println("Repositories:")
		project.repositories.map{it as MavenArtifactRepository}
			.forEach{
				println("Name: ${it.name}; url: ${it.url}")
			}
	}
}

