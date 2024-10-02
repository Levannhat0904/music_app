plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.room)
}

android {
    namespace = "net.lenhat.musicapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.lenhat.musicapplication"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    room {
        schemaDirectory("$projectDir/schemas")
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

    buildFeatures {
        viewBinding = true

    }
}

dependencies {
    implementation(libs.gson.di)
    implementation(libs.glide.di)
    implementation(libs.retrofit.di)
    implementation(libs.gson.converter.di)
    implementation(libs.media3.xoplayer.di)
    implementation(libs.media3.common.di)
    implementation(libs.media3.media.session.di)
    implementation(libs.media3.ui.di)
    implementation(libs.room.runtime.di)
    implementation(libs.activity)
    // todo: update like this:
    annotationProcessor(libs.room.compiler.di)

    implementation(libs.room.rxjava3.di)
    implementation(libs.rxjava3.di)
    implementation(libs.rxandroid.di)
    implementation(libs.preference.di)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}