plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lethimcook"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lethimcook"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    // Navigation Component (Fragment + NavHost)
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation ("androidx.navigation:navigation-ui:2.5.3")
    // Retrofit + Gson converter
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp (for logging)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    // SQLite no need implementation, sudah di anroidnya
    // Material Design components (for MaterialButton, etc.)
    implementation ("com.google.android.material:material:1.8.0")
    // Lifecycle + ViewModel
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

}