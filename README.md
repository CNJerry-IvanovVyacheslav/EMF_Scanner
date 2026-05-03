# EMF Scanner (Android)

A pet project focused on handling high-frequency hardware sensor data, implementing Clean Architecture, and rendering real-time graphics using Jetpack Compose Canvas.

## Tech Stack & Architecture
* **Kotlin** (Coroutines, Flow)
* **UI:** Jetpack Compose (using Canvas for 60fps rendering without full UI tree recompositions).
* **Architecture:** Clean Architecture (Data, Domain, Presentation) + MVI state management.
* **DI:** Hilt

## Implementation Details

### 1. Reactive Sensor Data Flow
The application reads raw data from the device's hardware magnetometer (`Sensor.TYPE_MAGNETIC_FIELD`). The legacy callback-based `SensorEventListener` is wrapped in a `callbackFlow` at the Data Source level. This creates a cold, reactive stream that automatically unregisters from the `SensorService` when the UI lifecycle stops, preventing battery drain.

### 2. 3D Vector Math
The Android Sensor API returns magnetic field strength along the X, Y, and Z axes. To obtain the absolute magnitude regardless of the device's orientation, the app calculates the 3D Pythagorean theorem: 
$Magnitude = \sqrt{x^2 + y^2 + z^2}$ 
The resulting value is measured in micro-Tesla (µT).

### 3. Handling Hardware Noise & Calibration
Smartphones generate significant internal magnetic noise due to components like speakers, vibration motors, and OIS coils.
* **Hardware Calibration (HAL):** The app relies on the Android Hardware Abstraction Layer (HAL) to filter out hard-iron bias (triggered natively when the user moves the device in a figure-8 pattern).
* **Software Calibration (Tare):** Added a baseline calibration feature in the Domain layer. It captures the current environmental background (usually 40-60 µT for Earth's magnetic field + room interference) and calculates the delta. This allows the UI to visualize only active local anomalies (e.g., a power adapter) rather than static environmental noise.
