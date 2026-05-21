# UpgradeNote — Tugas Praktikum Minggu 10
**Nama:** Fanisa Aulia Safitri | **NIM:** 123140121  
**Branch:** `week-10` | **Mata Kuliah:** Pengembangan Aplikasi Mobile — ITERA

---

## Test Coverage Report

> Screenshot hasil coverage report dari Android Studio:

<img width="959" height="599" alt="image" src="https://github.com/user-attachments/assets/6a7debe1-c54a-48ab-be8c-d78e4425cbb2" />

> Link youtube video demo
https://youtu.be/g4rE5GDM6rk


---

## Daftar Test Cases

### 1. KoinModuleTest — Koin DI (3 test cases)
| No | Test Case |
|---|---|
| 1 | `dataModule resolves GeminiService correctly` |
| 2 | `viewModelModule loads without errors` |
| 3 | `AIRepository uses same GeminiService singleton` |

---

### 2. NoteRepositoryTest — Unit Test Repository (10 test cases)
| No | Test Case |
|---|---|
| 1 | `getAllNotes returns correct list of notes` |
| 2 | `getAllNotes returns empty list when no notes exist` |
| 3 | `insertNote calls repository with correct parameters` |
| 4 | `updateNote calls repository with correct id and content` |
| 5 | `deleteNote calls repository delete with correct id` |
| 6 | `deleteNote does not delete other notes` |
| 7 | `searchNotes returns matching notes` |
| 8 | `searchNotes returns empty when no match found` |
| 9 | `note model holds correct data` |
| 10 | `note default isSynced is false` |

---

### 3. NotesViewModelTest — Unit Test ViewModel (10 test cases)
| No | Test Case |
|---|---|
| 1 | `notes flow emits data from repository` |
| 2 | `addNote calls repository insertNote with correct parameters` |
| 3 | `deleteNote calls repository deleteNote with correct id` |
| 4 | `updateNote calls repository updateNote with correct parameters` |
| 5 | `onSearchQueryChanged updates searchQuery state` |
| 6 | `initial searchQuery is empty` |
| 7 | `changeTheme updates theme state correctly` |
| 8 | `changeSortOrder updates isSortDescending state` |
| 9 | `deleteNote only deletes specified note` |
| 10 | `initial theme matches settingsManager` |

---

### 4. NoteFlowTest — Flow Test dengan Turbine (6 test cases)
| No | Test Case |
|---|---|
| 1 | `getAllNotes flow emits correct notes list` |
| 2 | `getAllNotes flow emits empty list when no data` |
| 3 | `searchNotes flow emits only matching notes` |
| 4 | `searchNotes flow emits empty when no match` |
| 5 | `flow completes after emitting single item` |
| 6 | `searchNotes is called with exact query string` |

---

### 5. NoteScreenUITest — UI Test Compose (7 test cases)
| No | Test Case |
|---|---|
| 1 | `nutritionScreen_analyzeButton_isDisplayed` |
| 2 | `nutritionScreen_foodInput_acceptsText` |
| 3 | `errorCard_displaysCorrectMessage` |
| 4 | `noteItem_titleIsDisplayed` |
| 5 | `emptyState_isDisplayedWhenNoNotes` |
| 6 | `addButton_isClickable` |
| 7 | `searchBar_acceptsTextInput` |

---

**Total: 29 unit test cases** (+ 7 UI instrumented test) | Build: `BUILD SUCCESSFUL`
