plugins {
    id("com.android.application")
}

android {
    namespace = "com.exam.exammodwithx"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.exam.exammodwithx"
        minSdk = 21
        targetSdk = 34
        versionCode = 5
        versionName = "1.4"
        vectorDrawables.useSupportLibrary = true
        // enable native lib
        ndk.abiFilters += listOf("arm64-v8a", "armeabi-v7a")
    }

    signingConfigs {
    create("release") {
        keyAlias = "abc"
        keyPassword = "abcdef"
        storeFile = file("abc.keystore")
        storePassword = "abcdef"
        enableV1Signing = true
        enableV2Signing = true
        enableV3Signing = true
    }
}


    buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
        signingConfig = signingConfigs.getByName("release")
        buildConfigField("boolean", "DEBUG", "false")
    }
}
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true 
    }
}

dependencies {
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.github.GrenderG:Toasty:1.5.2")
    // biometric
    implementation("androidx.biometric:biometric:1.1.0")
    // root detection
    implementation("com.scottyab:rootbeer-lib:0.1.0")
    // security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}
