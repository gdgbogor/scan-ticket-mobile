@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "id.gdev.regist"
    compileSdk = 34

    defaultConfig {
        applicationId = "id.gdev.regist"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "0.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "APP_AUTHORITY", "\"id.gdev.regist.provider\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icon.extended)
    implementation(libs.open.csv)
    implementation(libs.constraint.layout)
    implementation(libs.navigation)
    implementation(libs.hilt)
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.0")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01")
    implementation("com.google.android.gms:play-services-code-scanner:16.1.0")
    implementation(libs.qr.genartor)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}
