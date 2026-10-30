# Online Event Planning System

A web-based event planning and advertising system built as a 2nd year group
project. Registered users can create, edit and delete events, publish
advertisements for them, and visitors can browse and search everything without
an account. A separate administrator panel allows the whole system to be
managed.

The application is written with **Java Servlets, JSP, JDBC and MySQL** and is
packaged as a WAR file for **Apache Tomcat**.

---

## Features

### Public website

- Home page with upcoming events, recent advertisements and event categories
- Browse, search and filter events by keyword, category and time period
- Browse and search advertisements
- Detailed event and advertisement pages
- About Us and Contact Us pages (messages are stored in the database)
- Responsive layout for desktop, tablet and mobile screens

### Registered users

- Register and login with a username or email address
- Personal dashboard with event and advertisement statistics
- Create, edit and delete own events (with optional image upload)
- Create, edit and delete own advertisements (with optional image upload)
- Link an advertisement to one of the user's events and fill in its details
  automatically
- View and manage only their own records

### Administrator

- Separate administrator login
- Dashboard with system statistics and recent activity
- View all registered users
- Add, edit and delete any event
- View and delete any advertisement
- Add, rename and delete event categories
- Read and delete contact messages
- View the public website while logged in as an administrator

---

## Technologies

| Layer      | Technology                                  |
| ---------- | ------------------------------------------- |
| Language   | Java 11+                                    |
| Web        | Java Servlet 4.0, JSP, JSTL-free scriptlets |
| Database   | MySQL 8                                     |
| Access     | JDBC with `PreparedStatement`               |
| Build      | Apache Maven (WAR packaging)                |
| Server     | Apache Tomcat 9 (Java EE 8 / `javax.*`)     |
| Front end  | HTML5, CSS3, vanilla JavaScript             |

> **Important:** this project uses the `javax.servlet` namespace, so it must be
> deployed on **Tomcat 9** (or another Java EE 8 container). Tomcat 10+ uses
> `jakarta.servlet` and will not work without migration.

---

## Project structure

```
Online-Event-Planning-System/
├── database/
│   └── online_event_planning_system.sql   # schema + default admin + categories
├── src/main/java/com/eventplanner/
│   ├── dao/          # JDBC data access classes
│   ├── filter/       # encoding and authentication filters
│   ├── model/        # Java beans
│   ├── servlet/      # controllers
│   └── util/         # connection, validation, upload and password helpers
├── src/main/resources/db.properties       # database connection settings
├── src/main/webapp/
│   ├── css/style.css                      # main stylesheet
│   ├── js/main.js                         # form and UI enhancements
│   ├── images/                            # logo, banner and placeholders
│   ├── uploads/                           # uploaded images (created at runtime)
│   ├── WEB-INF/views/                     # JSP pages (public, user, admin, errors)
│   ├── WEB-INF/web.xml                    # welcome file, error pages, session config
│   └── index.jsp                          # redirects to /home
└── pom.xml
```

---

## Setup instructions

### 1. Create the database

Start MySQL and run the SQL script:

```bash
mysql -u root -p < database/online_event_planning_system.sql
```

The script creates the `online_event_planning_system` database, all tables,
eight default event categories and the default administrator account.

### 2. Configure the database connection

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/online_event_planning_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.username=root
db.password=root
```

The values can also be supplied through the `DB_URL`, `DB_USERNAME` and
`DB_PASSWORD` environment variables, which override the properties file.

### 3. Build the WAR file

```bash
mvn clean package
```

The WAR file is created at:

```
target/online-event-planning-system.war
```

### 4. Deploy on Apache Tomcat 9

Copy the WAR file into the Tomcat `webapps` folder and start Tomcat:

```bash
cp target/online-event-planning-system.war "$CATALINA_HOME/webapps/"
"$CATALINA_HOME/bin/startup.sh"
```

On Windows:

```powershell
Copy-Item target\online-event-planning-system.war "$env:CATALINA_HOME\webapps\"
& "$env:CATALINA_HOME\bin\startup.bat"
```

Then open:

```
http://localhost:8080/online-event-planning-system/
```

### 5. Run from an IDE (optional)

1. Import the project as a **Maven project**.
2. Add **Apache Tomcat 9** as the server.
3. Deploy the artifact `online-event-planning-system:war exploded`.
4. Open `http://localhost:8080/online-event-planning-system/`.

---

## Default login details

Administrator:

| Field    | Value      |
| -------- | ---------- |
| Username | `admin`    |
| Password | `admin123` |

There are no demo user accounts. Register a new account from the
**Register** page to test the user features.

> Change the administrator password before using the system outside a
> classroom/demo environment.

---

## Database tables

| Table               | Purpose                                                     |
| ------------------- | ----------------------------------------------------------- |
| `admins`            | Administrator login accounts                                |
| `users`             | Registered user accounts                                    |
| `categories`        | Event categories                                            |
| `events`            | Events created by users and administrators                  |
| `advertisements`    | Advertisements published by registered users                |
| `contact_messages`  | Messages submitted through the Contact Us page              |

Passwords are stored as salted SHA-256 hashes in the format `salt:hash`.
Uploaded images are stored in `src/main/webapp/uploads/` and the file name is
saved in the database.

---

## Useful URLs

| URL                    | Description                        |
| ---------------------- | ---------------------------------- |
| `/`                    | Redirects to the home page         |
| `/home`                | Public home page                   |
| `/events`              | Event list, search and filters     |
| `/events?id=1`         | Event details                      |
| `/advertisements`      | Advertisement list and search      |
| `/advertisements?id=1` | Advertisement details              |
| `/about`               | About Us                           |
| `/contact`             | Contact Us                         |
| `/login` `/register`   | User authentication pages          |
| `/user/dashboard`      | User dashboard (login required)    |
| `/admin/login`         | Administrator login                |
| `/admin/dashboard`     | Administrator dashboard            |

---

## Validation and security notes

- All SQL statements use `PreparedStatement` parameters.
- Output written into JSP pages is escaped with `WebUtil.escape`.
- User passwords are never stored in plain text.
- Session IDs are regenerated after login.
- Authentication filters protect `/user/*` and `/admin/*`.
- Users can only edit or delete their own events and advertisements.
- Uploaded files are limited to JPG, PNG and GIF, 5 MB maximum.
- CSRF protection is not implemented because this is a university project;
  add tokens before using the system for real public traffic.

---

## Troubleshooting

**`Database configuration file db.properties was not found`**
Make sure `src/main/resources/db.properties` is included in the build (run
`mvn clean package`).

**`Access denied for user 'root'@'localhost'`**
Update `db.username` / `db.password` in `db.properties` or set the
`DB_USERNAME` and `DB_PASSWORD` environment variables.

**`Unknown database 'online_event_planning_system'`**
Run `database/online_event_planning_system.sql` again.

**Uploaded images do not appear**
Check that the Tomcat process can write to the deployed `uploads` folder. When
the WAR is not exploded, the application automatically falls back to a folder
inside the system temporary directory and logs a warning.

**Pages look unstyled**
Confirm that `src/main/webapp/css/style.css` and
`src/main/webapp/js/main.js` are present in the deployed application and that
the context path is correct.

---

## License

This project was created for educational purposes as part of a 2nd year group
project. You may use and modify it for learning.
