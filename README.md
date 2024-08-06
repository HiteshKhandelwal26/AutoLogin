# Auto Login App
## About
*A simple android app that auto logins after saving the authentication token received in the login API response and saves the session.
Thus allowing the user to navigate to the dashboard screen directly when the app re-launches.

## Built With
- Kotlin - First class and official programming language for Android development.
- Coroutines - For composing asynchronous and event-based programs by using observable sequences.
- MVVM clean architecture with repository pattern
- Example to demonstrate how to call the api from repository class using Flow Builder
- Android Architecture Components - Collection of libraries that help you design robust, testable, and maintainable apps.
- ViewModel - Stores UI-related data that isn't destroyed on UI changes.
- Hilt - Dependency Injection Framework
- Retrofit - A type-safe HTTPS client for Android and Java.
- Gson - used to convert Java Objects into their JSON representation and vice versa.
- Ktlint -Ktlint is a static code analysis tool maintain by Pinterest. Linter and formatter for Kotlin code.

## Steps followed to implement the project
- Added the internet permissions in the manifest file
- Add dependency in the build.Gradle(:app)
- Create the app folder structure - Followed the clean architecture repository pattern
- Add the Hilt 

## Future Plans
- Unit & Instrumentation test cases to be added.
- Merge all code coverage reports at single place.
- Reach code coverage atleast 80%.(TDD & BDD)
- Dark Mode theme