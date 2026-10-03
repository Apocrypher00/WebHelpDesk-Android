# WebHelpDesk for Android

An unofficial Android client for SolarWinds Web Help Desk.
This is a personal project being revived after several years, and is under active development.
It is not affiliated with or supported by SolarWinds.

## Current status

The app is being adapted to an early-access version of the WHD NextGen API.
Compatibility with other WHD versions has not been established.

- Technician username/password login and bearer-token requests work on a physical Android phone.
- The app saves the token and checks it on startup to reuse a valid login.
- Existing ticket search, list, and detail screens still need validation against the new API.
- Assets and settings screens are placeholders.
- A client-to-technician token-switch helper exists but is not called automatically.
  Linked technician ID discovery remains unresolved.

For now, use credentials that authenticate directly as a technician.
Client credentials may return a client token, even when the web interface lets that account
switch to a linked technician.

## Build and run

1. Open the repository in Android Studio.
2. Install Android SDK Platform 33 and configure the Gradle JDK in Android Studio.
   The current setup has been built with the bundled JDK 25; the build files retain
   Java/Kotlin bytecode target 1.8.
3. Sync the project and run the `app` configuration on a device running Android 7.0
   (API 24) or newer.

The repository pins Gradle 9.8.0, Android Gradle Plugin 8.7.3, and Kotlin 2.0.21.
Use the checked-in Gradle wrapper rather than a separately installed Gradle.
From PowerShell, with `JAVA_HOME` set and a local Android SDK configured:

```powershell
.\gradlew.bat assembleDebug
```

Enter only the server hostname on the login screen, for example `helpdesk.example.com`.
The app adds HTTPS and `/api/v1/ra`; do not enter a scheme or API path.
The device must be able to reach the server and trust its TLS certificate.

## Development notes

- `Api.kt` contains token creation, request headers, and the unused account-switch helper.
- `LoginActivity.kt` handles login and saved-token validation.
- The app currently persists the hostname and bearer token in SharedPreferences.
- API collection response handling, token expiry, and error handling still need review.
- The included tests are starter tests, not a comprehensive API integration suite.

Keep credentials, tokens, server data, signing keys, and local build configuration out of commits.
Android Studio settings and generated caches are local; shared code styles remain tracked.

## License

Project-authored code is released under the [Unlicense](UNLICENSE).
Dependencies and third-party materials retain their own terms; the Unlicense does not
grant rights to SolarWinds branding or other third-party materials.
