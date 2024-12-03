plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.quizziesclient"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quizziesclient"
        minSdk = 25
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

val ktor_version = "3.0.1"
val stomp_protocol_android = "1.6.6"

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    //implementation("org.hildan.krossbow:krossbow-websocket-builtin:7.0.0") // Java 11 , Not supported in android
    //implementation("org.hildan.krossbow:krossbow-stomp-core:7.0.0")
    //implementation("org.hildan.krossbow:krossbow-websocket-builtin:7.0.0")
    //implementation("org.hildan.krossbow:krossbow-websocket-okhttp:7.0.0")
    implementation("io.ktor:ktor-client-android:1.6.4")
    implementation("io.ktor:ktor-client-core:${ktor_version}")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:1.6.4")
    // Prueba 4
    implementation("com.github.NaikSoftware:StompProtocolAndroid:${stomp_protocol_android}") // STOMP client library
    implementation("org.java-websocket:Java-WebSocket:1.5.2") // WebSocket library
    // Prueba 5
    //implementation("com.github.NaikSoftware:StompProtocolAndroid:${stomp_protocol_android}")

    // RxAndroid
    //implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    //implementation("io.reactivex.rxjava3:rxjava:3.1.10")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.8")
    // Gson
    implementation("com.google.code.gson:gson:2.11.0")
    //
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}