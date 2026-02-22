# DeoHealth üèÉüìç

DeoHealth is a comprehensive Android health and fitness application built with modern Android development practices. It helps users track their physical activity, manage health goals, and monitor progress toward a healthier lifestyle.

## üöÄ Features

- **Step Tracking**: Real-time monitoring of daily steps with historical data analysis.
- **Calorie Record**: Log and track daily calorie intake and expenditure.
- **Goal Management**: Set, track, and achieve personalized health and fitness goals.
- **Profile Customization**: Manage user health metrics (weight, height, age) and preferences.
- **Local Persistence**: All data is securely stored locally using Room database.
- **Modern UI**: Built entirely with Jetpack Compose for a smooth and responsive user experience.

## üîß Tech Stack

- **Languge**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Asynchronous Programming**: Coroutines & Flow
- **Data Persistence**: DataStore (Preferences)
- **Background Tasks**: WorkManager
- **Architecture**: MVVM (Model-View-ViewModel)

## üìí Getting Started

### Prerequisites

- Android Studio Flamingo or newer.
- Android SDK 30+.

### Building

1. Clone the repository:
   ```bash
   git clone https://github.com/srinithishs004/DeoHealth.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the app on an emulator or physical device.

## üîí Sensitive Content & Privacy

To maintain security and prevent the exposure of sensitive local configuration data, the following files are explicitly excluded from version control via `.gitignore`:

- `local.properties`: Contains local SDK paths and potential API keys.
- `build_log*.txt` & `build_output.txt`: Temporary build artifacts.
- `*.jks` & `*.keystore`: Private signing keys (for release builds).
- `google-services.json`: Firebase configuration (if integrated).

**Note**: Always ensure your local environment is secured and never commit private keys to the repository.

## üôñ License

This project is licensed under the **Creative Commons Attribution 4.0 International (CC BY 4.0)**. 

See the [LICENSE](LICENSE) file for the full text.

---
*Developed with  ÔøΩÔøΩ by Srinithish*
