import org.jetbrains.kotlin.utils.addToStdlib.ifTrue

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.riego"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.riego"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding{
        enable = true
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //implementation ("androidx.core:core-splashscreen:1.0.0")
    implementation("com.google.android.material:material:1.1.0")

    //listrecicle
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    //http
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    //location
    implementation("com.google.android.gms:play-services-location:19.0.1")
    //Room
    implementation ("androidx.room:room-ktx:2.6.1")
    //implementation ("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //corrutinas
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    //implementation("com.android.tools.build:gradle:8.4.1")

    //maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")
}