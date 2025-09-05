plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "br.com.tcc.oauth2app"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.tcc.oauth2app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.coroutines)
    implementation(libs.retrofit)
    implementation(libs.gsonConverter)
    implementation(libs.gson)
    implementation(libs.rxAdapter)

    implementation(platform(libs.okHttpBom))
    implementation(libs.okHttp)
    implementation(libs.okHttpLoggingInterceptor)
    implementation(libs.urlConnection)

    implementation(libs.fragmentNav)
    implementation(libs.ui)
    implementation(libs.navUi)
    implementation(libs.navFragment)
    implementation(libs.fragment)
    implementation(libs.fragmentKtx)

    implementation(libs.lifecycleExtensions)
    implementation(libs.lifecycleViewmodel)
    implementation(libs.lifecycleCompiler)
    implementation(libs.lifecycleCommon)


    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)
    kapt (libs.androidxHiltCompiler)
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation(libs.timber)

    implementation(libs.multidex)
}