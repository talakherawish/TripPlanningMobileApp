# TripPlanningMobileApp

An Android application for planning and managing personal trips. The app allows users to create, edit, delete, and store trip entries locally through a clean and simple interface. Built with Java and Android Studio, it uses RecyclerView, lifecycle-aware components, and SharedPreferences to ensure smooth performance and persistent storage.
Running Projject Demo: https://drive.google.com/file/d/12yE4Kuwb6qV74TWaWHS13OVSyNwy8W5Z/view?usp=sharing

## Features

* Create new trip entries with custom details.
* Display all saved trips in a scrollable list using RecyclerView and a custom adapter.
* Edit, delete, and search for existing trips.
* Select trip dates using an integrated calendar component.
* Persist data locally using SharedPreferences.
* Simple and flexible UI built with standard Android widgets.
* Modular structure with reusable Trip objects and adapters, supporting clean architecture.

## Prerequisites

* Android Studio (latest version recommended)
* Java 11+
* Android SDK installed

## Run Instructions

1. Clone the repository:

   bash
   git clone https://github.com/talakherawish/TripPlanningMobileApp.git
   
2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Run the app on an emulator or a physical Android device.

## Usage

* Open the app to view the list of stored trips.
* Add a new trip by entering the required details and selecting an image.
* View stored trips presented with title, description, date, and image.
* Edit or delete entries as needed.
* All trip data is stored locally using SharedPreferences and remains available after restarting the app.

## Programmer

*Tala Kherawish*
This was my first experience working with Android Studio and mobile development in general. After running into typical emulator issues, I continued development using an external Android device. This project was built as part of a university course and focuses on creating practical, user-centric Android applications with clean architecture and customizable features.
