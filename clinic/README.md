# MediBook - Medical Appointment Scheduling System
SE1020 OOP Project - Group 26-MTR-Y1S2-WD-003 (Java Servlets + JSP, Bootstrap 5, `.txt` file storage)

## Run
1. Needs JDK 11+, Maven, Tomcat 9 (javax.servlet). For Tomcat 10+: use `jakarta.servlet-api` in `pom.xml` and replace `javax.servlet` with `jakarta.servlet` in the Java files.
2. `mvn clean package` then copy `target/clinic.war` into Tomcat's `webapps/` (or import as a Maven project in Eclipse/IntelliJ and run on Tomcat).
3. Open `http://localhost:8080/clinic/`
4. Default admin: **admin / admin123**. Patients register themselves.
5. Data files are created in `~/clinic-data/` (`patients.txt`, `doctors.txt`, `appointments.txt`, `admins.txt`, `payments.txt`, `reviews.txt`, `adminlog.txt`). Passwords are stored as SHA-256 hashes.

## Work split (one component per member)
Each component is self-contained: its own Java package under `src/main/java/com/clinic/<pkg>/` and JSP folder under `src/main/webapp/<pkg>/`. The shared code in `common/` and `includes/` is written once and used by everyone.

| # | Component | Package / JSP folder | Data file | CRUD | OOP concepts |
|---|-----------|----------------------|-----------|------|--------------|
| 1 | Patient Management | `patient` | patients.txt | register, search, update profile, delete | Encapsulation (`Patient`), Inheritance (`RegularPatient`, `InsuredPatient`), Polymorphism (`discountRate()`, `cancellationFee()`, `authenticate()`) |
| 2 | Doctor Management | `doctor` | doctors.txt | add, search, edit, delete | Encapsulation (`Doctor`), Inheritance (`GeneralPractitioner`, `Specialist`), Polymorphism (`consultationFee()`, `display()`) |
| 3 | Appointment Booking | `appointment` | appointments.txt | book, view, reschedule/complete/cancel, delete | Encapsulation (`Appointment`), Abstraction (`AppointmentService` slot checks), Polymorphism (cancellation fee by patient type) |
| 4 | Admin Management | `admin` | admins.txt, adminlog.txt | register, view + activity log, edit permissions, delete | Encapsulation (`Admin`), Inheritance (`Admin extends User`), Abstraction (permission-gated admin-only actions) |
| 5 | Billing & Payment | `billing` | payments.txt | generate bill, view history, pay/discount, delete settled | Encapsulation (`Payment`), Inheritance (`CashPayment`, `CardPayment`, `PendingPayment`), Polymorphism (`surcharge()`, `process()`) |
| 6 | Feedback & Reviews | `review` | reviews.txt | submit, view per doctor, edit, delete/moderate | Encapsulation (`Review`), Inheritance (`PublicReview`, `VerifiedReview`), Polymorphism (`displayFor(admin)`) |

Shared: `common/User` (base class), `FileHandler` (txt read/write), `Util`, `LoginServlet`, `LogoutServlet`, `AppListener`, `includes/header.jsp`, `includes/footer.jsp`.

## Integration points (where components meet)
- Login (`common`) checks `AdminDAO` and `PatientDAO`.
- Appointment uses Doctor (availability, fee) and Patient (cancellation fee).
- Billing uses Appointment (completed/cancelled) + Doctor fee + Patient discount.
- Review marks a review VERIFIED when the patient has a COMPLETED appointment with that doctor.
- Admin dashboard counts records from all other components.
