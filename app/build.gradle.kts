plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.keywordnewsreader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.keywordnewsreader"
        minSdk = 27
        targetSdk = 33
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
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.0.0-alpha1")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.4")
    implementation("androidx.navigation:navigation-testing:2.7.4")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    kapt ("com.android.databinding:compiler:3.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.jsoup:jsoup:1.11.3")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    implementation(kotlin("script-runtime"))
}