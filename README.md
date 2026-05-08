# LaptopMart

A full-stack E-Commerce Android application for buying and selling laptops, built with modern architecture and cloud-based services.

## Overview

LaptopMart is a comprehensive mobile e-commerce platform that enables users to browse, search, and purchase laptops with a seamless user experience. The app features real-time inventory management, secure transactions, and an intuitive user interface.

## Features

- **Browse & Search**: Explore a wide range of laptops with advanced filtering and search capabilities
- **Real-time Inventory**: Live product availability synced across all users
- **User Authentication**: Secure login and account management
- **Shopping Cart**: Add items to cart and manage quantities
- **Order Management**: Track orders and view order history
- **Image Hosting**: High-quality product images powered by Cloudinary
- **Responsive Design**: Optimized for various Android devices

## Tech Stack

- **Language**: Java
- **Architecture**: MVVM (Model-View-ViewModel)
- **Backend Services**:
  - Firebase Firestore for real-time database
  - Firebase Authentication
  - Cloudinary for image hosting
- **Platform**: Android

## Architecture

The app follows the **MVVM pattern** for clean, maintainable code:
- **Model**: Handles data layer and business logic
- **View**: Android UI components (Activities, Fragments)
- **ViewModel**: Manages UI-related data and state

## Key Technologies

- **Firestore Batch Writes**: Efficient batch operations for inventory updates
- **Real-time Sync**: Firebase Firestore listeners for live data updates
- **Cloudinary Integration**: Optimized image delivery and management

## Getting Started

### Prerequisites

- Android Studio (latest version)
- Java 8 or higher
- Firebase project setup
- Cloudinary account

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/varrver/LaptopMart.git
   cd LaptopMart
   ```

2. Open the project in Android Studio

3. Configure Firebase:
   - Add your `google-services.json` file to the `app/` directory
   - Set up Firestore database and Authentication in Firebase Console

4. Configure Cloudinary:
   - Update your Cloudinary credentials in the configuration file

5. Build and run the app

## Project Structure

```
LaptopMart/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── models/
│   │   │   │   ├── views/
│   │   │   │   └── viewmodels/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle
├── README.md
└── .gitignore
```

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for any bugs or feature requests.

## License

This project is open source and available under the MIT License.

## Author

**varrver** - [GitHub Profile](https://github.com/varrver)

## Contact

For questions or support, please open an issue on the GitHub repository.

---

**Last Updated**: May 2026
