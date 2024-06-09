plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dontblinkappx"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dontblinkappx"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.vision.common)
    implementation(libs.play.services.vision)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    implementation(libs.glide)
    implementation(libs.glide.annotations)
    annotationProcessor(libs.glide.compiler)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.firebase:firebase-database:21.0.0")
}