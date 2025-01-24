plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.walmart.assessment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.walmart.assessment"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        sourceSets {
            getByName("main") {
                kotlin.srcDir("src/main/kotlin")
            }
            getByName("test") {
                resources.srcDir("src/resources")
            }
            getByName("androidTest") {
                assets.srcDir("src/resources")
            }
        }
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }

    // Dependencies licenses issues fix
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }

    // Mockk ART Agent issue fix
    @Suppress("UnstableApiUsage")
    testOptions {
        packaging {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
}

// Hamcrest matchers fix
configurations.all {
    resolutionStrategy {
        force("org.hamcrest:hamcrest-library:1.3")
    }
}

dependencies {
    // Androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Material-3
    implementation(libs.material)

    // Widgets
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)

    // OkHttp
    implementation(libs.okhttp.android.support)
    implementation(libs.logging.interceptor)

    // Retrofit
    implementation(platform(libs.retrofit.bom))
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.retrofit2.converter)

    // Unit-tests
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    // Debug-testing
    debugImplementation(libs.androidx.test.core)
    debugImplementation(libs.androidx.test.runner)
    debugImplementation(libs.androidx.test.rules)
    debugImplementation(libs.androidx.fragment.testing)

    // UI-tests
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.mockk.android)
}