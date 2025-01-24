# Walmart Android Mobile app Take-home Assessment

### System Requirements:

* Android Studio Ladybug Feature Drop | 2024.2.2, and Higher
* Android SDK - Max API 35, Min API 24

### Configuration:

* Gradle: 8.12
* AGP: 8.8
* Kotlin: 2.1.0
* Java-version: 11

### Demo

Visit [Demo](demo) folder to view quick-demos.

### Design considerations:

* Model-View-ViewModel ( MVVM ) clean architecture, with Unidirectional Data-Flow
* "Push, don't pull" Kotlin-flow
* Unit-tests and Espresso-test available.

  ````./gradlew testDebugUnitTest````
  ````./gradlew connectedDebugAndroidTest````

Following Enterprise best-practices guidelines and design-decisions are avoided in-favor of
minimalist app-design and functionality.

* "Domain-layer"
* "Jetpack Navigation"
* "Code-coverage"
* "Static-Analysis linters"
* "Code-style" checkers and formatters