import AndroidConfig.roomVersion


object AndroidConfig {
    const val kotlinVersion = "1.4.31"
    const val minSdk = 21
    const val compileSdk = 31
    const val buildTools = "30.0.2"
    const val roomVersion = "2.3.0-beta01"
    const val targetSdk = 31
}

object Dependencies {
    const val kotlinStandardLib = "org.jetbrains.kotlin:kotlin-stdlib:${AndroidConfig.kotlinVersion}"
    const val kotlinCoreKtx = "androidx.core:core-ktx:1.6.0"
    const val legacySupportV4 = "androidx.legacy:legacy-support-v4:1.0.0"

    // ui essential
    const val uiEssentialAppCompat = "androidx.appcompat:appcompat:1.4.1"
    const val uiEssentialConstraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val uiEssentialRecycleView = "androidx.recyclerview:recyclerview:1.1.0"

    // ui material
    const val uiMaterialSupport = "com.google.android.material:material:1.2.1"
    const val uiGlide = "com.github.bumptech.glide:glide:4.11.0"
    const val materialDesign = "com.google.android.material:material:1.3.0"

    // paging3
    private const val paging_version = "3.1.0"
    const val paging3 = "androidx.paging:paging-runtime:$paging_version"

    // live data and view model
    private const val lifecycleVersion = "2.4.0"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    const val lifecycleViewCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
    const val lifecycleAnnotation = "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"
    const val lifecycleSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion"

    // navigation
    private const val navigationVersion = "2.3.5"
    const val uiNavigationFragment = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    const val uiNavigationUI = "androidx.navigation:navigation-ui-ktx:$navigationVersion"

    // hilt
    private const val hiltAndroidVersion = "2.38.1"
    private const val hiltVersion = "1.0.0-alpha01"
    const val diHiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$hiltAndroidVersion"
    const val diHiltAndroid = "com.google.dagger:hilt-android:$hiltAndroidVersion"
    const val diHiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$hiltAndroidVersion"
    const val diHiltLifecycle = "androidx.hilt:hilt-lifecycle-viewmodel:$hiltVersion"
    const val diHiltCompiler = "androidx.hilt:hilt-compiler:$hiltVersion"

    // networking
    private const val retrofitAndroidVersion = "2.9.0"
    private const val okhttpLoggingVersion = "4.9.0"
    private const val gsonVersion = "2.8.7"
    const val networkRetrofit = "com.squareup.retrofit2:retrofit:$retrofitAndroidVersion"
    const val networkRetrofitGsonConverter = "com.squareup.retrofit2:converter-gson:$retrofitAndroidVersion"
    const val networkRetrofitLogging = "com.squareup.okhttp3:logging-interceptor:$okhttpLoggingVersion"
    const val jsonSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"
    const val gson = "com.google.code.gson:gson:$gsonVersion"

    // security
    const val androidxSecurityCrypto = "androidx.security:security-crypto:1.1.0-alpha03"

    // room database
    const val roomDatabase = "androidx.room:room-runtime:$roomVersion"
    const val roomKtxDatabase = "androidx.room:room-ktx:$roomVersion"
    const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    const val roomRxJava = "androidx.room:room-rxjava3:$roomVersion"

    // testing
    const val testArchCore = "androidx.arch.core:core-testing:2.1.0"
    const val testPowerMockCore = "org.powermock:powermock-core:2.0.9"
    const val testPowerMockJunit = "org.powermock:powermock-module-junit4:2.0.9"
    const val testPowerMockMockito = "org.powermock:powermock-api-mockito2:2.0.9"
    const val testJUnit4 = "androidx.test.ext:junit-ktx:1.1.3"

    // database debugging
    const val databaseDebugger = "com.amitshekhar.android:debug-db:1.0.6"

    // glide
    const val glide = "com.github.bumptech.glide:glide:4.13.0"
    const val glideAnnotation = "com.github.bumptech.glide:compiler:4.13.0"

    // chart view
    const val anyChart = "com.github.AnyChart:AnyChart-Android:1.1.2"
}
