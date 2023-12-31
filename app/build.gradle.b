apply plugin: 'com.android.application'


android {
    compileSdkVersion 26
    defaultConfig {
        applicationId "com.OP.speechrecognizer"
        minSdkVersion 14
        targetSdkVersion 26
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
        aaptOptions.cruncherEnabled = false
        aaptOptions.useNewCruncher = false
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.1'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.1'
    implementation files('libs/Msc.jar')
    implementation files('libs/Sunflower.jar')
    implementation 'org.litepal.android:core:1.3.2'
    implementation 'com.squareup.okhttp3:okhttp:3.4.1'
    implementation 'com.google.code.gson:gson:2.7'
    implementation 'com.github.bumptech.glide:glide:3.7.0'
}

