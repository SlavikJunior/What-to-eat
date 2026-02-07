import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

val spoonacularApiKey: String = localProperties.getProperty("SPOONACULAR_API_KEY", "\"\"")

android {
    namespace = "com.example.whattoeat"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.whattoeat"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SPOONACULAR_API_KEY", "\"$spoonacularApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.jsoup)
    implementation(libs.coil)
    implementation(libs.coilNet)
    implementation(libs.shimmer)
    implementation(libs.palette)
    implementation(libs.hilt.navigation)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.retrofit.serialization)
    implementation(libs.kotlin.reflect)
    implementation(libs.okhttp.logging)
    implementation(libs.mlkit.lang.id) {
        exclude(group = "com.google.mlkit", module = "language-id-common")
    }
    implementation(libs.mlkit.translate)
    implementation(libs.coroutines.play.services)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}