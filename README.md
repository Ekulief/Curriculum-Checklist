# My SLU Curriculum Checklist

A Java Swing desktop application that helps a **BSIT or BSCS student of Saint Louis University** track progress through their curriculum. Students sign in with their ID number, see their subjects term by term, record grades, add courses, take electives, view their GPA, and shift between programs. Each student's progress is saved between sessions.

---

## Features

**Accounts**
- Sign up with a 7-digit ID number, a password, and a program (**BSCS** or **BSIT**)
- Log in, log out, and return to the login screen without closing the app

**Checklist menu**

| Option | What it does |
|---|---|
| 1 | Show the subjects for each school term, page by page |
| 2 | Show the subjects with grades and remarks for each term |
| 3 | Enter grades for subjects recently finished |
| 4 | Edit a course (grade, units, or name) or remove it |
| 5 | Add other courses taken (year, semester, course number, name, units, grade) |
| 6 | Display courses with the grade point average of each term |
| 7 | Display courses in alphabetical order |
| 8 | Display courses with grades from highest to lowest |
| 9 | Take an elective course |
| 10 | Shift program (BSIT to BSCS, or BSCS to BSIT) |
| 11 | Log out |
| 12 | Quit |

**Rules the app enforces**
- A course can only be graded or edited if its **prerequisites** are met
- Grades are percentages from 0 to 99, and a grade of **75 or higher is PASSING**; lower non-zero grades are FAILING, and ungraded courses show "Not Taken Yet"
- Students may take at most **2 electives in Year 3** and **3 electives in Year 4**
- Shifting programs renames the student's save file to the new program and converts course codes (IT to CS, or CS to IT)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | Java Swing (`JFrame`, `JOptionPane`, dialogs and text areas) |
| Persistence | Java object serialization (`.ser` files) for student progress, plain text files for accounts and course data |
| Concepts used | Abstract classes, inheritance, interfaces and lambdas (`Search`, `CourseProperty`), collections and streams, exception handling |
| IDE | IntelliJ IDEA |

---

## Project Structure

| Path | Contents |
|---|---|
| `src/prog2/fingrp/Main.java` | Entry point |
| `src/prog2/fingrp/CheckList.java` | Main window, login, sign-up, and the options menu |
| `src/prog2/fingrp/Curriculum.java` | Loading and saving a student's curriculum, all menu operations, prerequisite checks, program shifting |
| `src/prog2/fingrp/AbstractCourse.java`, `Course.java`, `Elective.java` | Course model classes |
| `src/prog2/fingrp/CourseProperty.java`, `Search.java` | Functional interfaces |
| `src/ToDoList.txt` | Team to-do notes and data file formats |
| `Courses/` | Curriculum reference data (see below) |
| `Data/` | Serialized student progress files |
| `Logo.png`, `LogoBackground.png`, `LogoIcon.png` | Images used by the windows |

### Course data files (`Courses/`)

| File | Format | Purpose |
|---|---|---|
| `bscs.txt`, `bsit.txt` | `year,term,courseNo,courseName,units` | The full program curriculum |
| `bscsprerequisites.txt`, `bsitprerequisites.txt` | `courseNo,prerequisite1,prerequisite2` | Prerequisites for each course |
| `bscselectives.txt`, `bsitelectives.txt` | `courseNo,courseName,units,labUnits,selected,canBeTaken` | Available electives |
| `cselectives.txt`, `itelectives.txt`, `prerequisites.txt` | Older formats | Earlier versions of the electives and prerequisite lists (not read by the current code) |

### Saved data

- **`accounts.txt`** is created in the working directory and stores one account per line as `ID:Password:Program`.
- **`Data/<id><program>.ser`** holds one student's courses, grades, and elective choices (for example `1234567bsit.ser`).

---

## Getting Started

### Prerequisites

- **JDK 8 or newer**
- **IntelliJ IDEA** (or any IDE that can run a Java Swing application)
- **Windows.** The code builds file paths with backslashes (for example `\Data\` and `\Courses\`), so it is written for Windows.

### Run

1. Open the project folder in IntelliJ IDEA (source root: `src`).
2. Set the **working directory** of the run configuration to the project root, so `Courses/`, `Data/`, and the logo images are found.
3. Run `prog2.fingrp.Main`.
4. On the login screen, click **Sign up** to create an account, then log in.

### Important: fix the prerequisites path

`Curriculum.readPrerequisites()` loads the prerequisites file from a hardcoded location on the original developer's computer:

```
C:\Users\admin\Downloads\AgcaoiliBarryBibit-CheeBilalCarbonellCumtiEzperagoza9401FinProj1\Courses\
```

On any other machine the file is not found, the error is only printed, and every course is treated as having **no prerequisites**. Change that path to use the project's `Courses` folder (the `COURSE_CURRICULUM_REFERENCE_LOCATION` constant already defined in `Curriculum`) so prerequisite checks work.

---

## Known Limitations

- Passwords are stored in plain text in `accounts.txt`.
- File paths are Windows-specific, and the prerequisites path is hardcoded (see above).
- Prerequisite checks are silently skipped when the prerequisites file cannot be read.
- Some option 3 and option 4 dialogs validate grades with different ranges (0 to 99 in one place, 65 to 99 in another).
- Several `Data/*.ser` files in the repository belong to old test accounts named after people rather than ID numbers, and are not used by the current ID-based login.
- Older course and prerequisite files remain in `Courses/` alongside the ones the code reads.
- The team's to-do list still notes missing JavaDoc comments and exception handling in places.

---

## Team

Final project, Programming 2 (Class 9401)

- Agcaoili, Adriel
- Barry, John Joeffrey
- Bibit-Chee, Lieflander Luke
- Bilal, Majd
- Carbonel, Theron
- Cumti, _TODO: add first name_
- Esperagoza, Genel

## Screenshots

_TODO: add screenshots of the login screen, options menu, curriculum view, and elective selection._

## License

_TODO: choose a license, or remove this section if the project is for coursework only._
