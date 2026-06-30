plugins {
    alias(libs.plugins.android.application)
}
android {
    // Namespace unik untuk aplikasi
    namespace = "com.app.tiketin.v1"
    compileSdk {
        // Menentukan versi compile SDK
        version = release(36) {
            // Minor API level tambahan
            minorApiLevel = 1
        }
    }
    defaultConfig {
        // ID unik aplikasi
        applicationId = "com.app.tiketin.v1"
        // Minimum versi Android yang bisa menjalankan aplikasi
        minSdk = 24
        // Target versi Android yang dioptimalkan
        targetSdk = 36
        // Nomor versi internal (untuk Play Store)
        versionCode = 1
        // Nama versi aplikasi yang ditampilkan ke pengguna
        versionName = "1.0"
        // Runner untuk instrumented test
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        // Mengaktifkan ViewBinding untuk mempermudah akses view
        viewBinding = true
    }
    buildTypes {
        release {
            // Menentukan apakah ProGuard/R8 akan digunakan untuk minifikasi
            isMinifyEnabled = false
            // File konfigurasi ProGuard yang digunakan
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Menentukan versi Java yang digunakan untuk kompilasi
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
dependencies {
    // Library AndroidX Core KTX untuk fitur Kotlin
    implementation("androidx.core:core-ktx:1.13.1")
    // Library AppCompat untuk kompatibilitas UI
    implementation("androidx.appcompat:appcompat:1.7.0")
    // Material Design Components
    implementation("com.google.android.material:material:1.12.0")
    // ConstraintLayout untuk layout fleksibel
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // SplashScreen API untuk layar pembuka
    implementation("androidx.core:core-splashscreen:1.0.1")
    // Dependensi yang diambil dari katalog libs (versi dikelola di gradle/libs.versions.toml)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))

    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")

    // Dependensi untuk unit testing
    testImplementation(libs.junit)
    // Dependensi untuk instrumented testing (JUnit dan Espresso)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}