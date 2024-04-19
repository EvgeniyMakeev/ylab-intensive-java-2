## Training Diary Console Application

This repository contains a Java console application for managing training diaries. Users can log in, add training entries, view their training history, and more. Additionally, administrators have access to additional options, such as viewing training history for all users and logs.

### Getting Started

To run the application, you need to have Java installed on your machine. Follow these steps:

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/EvgeniyMakeev/ylab-intensive-java-2


2. Navigate to the project directory:

   ```bash
   cd training-diary
   ```

3. Compile and run the application:

   ```bash
   javac App.java
   java App
   ```

### Usage

*For log in as demo user use the login:* **DemoUser** *and password:* **1234**

1. Upon running the application, you will be presented with the main menu:

   ```plaintext
   ======= MAIN MENU =======
   1. Log in.
   2. Registration.

   0. Exit
   ```

2. Choose an option by entering the corresponding number.

3. If you choose to log in or register, you will be prompted to enter your login and password.

4. Follow the on-screen instructions to navigate through the application and perform various actions.

5. To exit the application, select option 0.

### Features

- **User Management:**
    - Log in
    - Registration

- **User Options:**
    - Add new training
    - Edit training
    - Delete training
    - Show training history
    - Show statistics of trainings
    - Admin options (if user is an administrator)
    - Log out

*For log in as administrator use the login:* **admin** *and password:* **admin**

- **Admin Options:**
    - Show training history for all users
    - Show training history for a specific user
    - Show log for a specific user
    - Show log for all users
    - Back to User menu

### Project Structure

The project is organized into several packages:

- `service`: Contains the main application code and business logic.
- `dao`: Contains data access objects.
- `utils`: Contains utility.
- `exceptions`: Defines custom exceptions.
- `in`: Input interfaces and implementations for user input.
- `model`: Data model classes and records.
- `out`: Output interfaces and implementations for displaying messages.


### Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
