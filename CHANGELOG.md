# 0.1.0 (2016-10-10)
This version represents the MVP.

## Features
**Application main class**: `de.communicode.communikey.CommunikeyApplication`
**Application YAML configuration**: `src/main/resources/application.yaml`

### Controllers
  - [CCKEY-23][jira-cckey-23] Implemented the `de.communicode.communikey.controller.PasswordController` class which provides the following endpoints:

| Endpoint | Usage |
| --- | --- |
| `passwords` | A list of all saved password entries. |
| `passwords/<ID>/edit` | A simple form to edit a password with the given `ID`. |
| `passwords/new` | A simple form to create a new password. |

### Entities
  - [CCKEY-15][jira-cckey-15] Implemented the `de.communicode.communikey.domain.Password` class which consists of the password value and a creation timestamp

### Repositories
  - [CCKEY-16][jira-cckey-16] Designed the `de.communicode.communikey.repository.PasswordRepository` interface

### Services
  - [CCKEY-17][jira-cckey-17] Designed the `de.communicode.communikey.service.PasswordService` interface and implemented the `de.communicode.communikey.service.PasswordServiceImpl` class

| Method Signature | Description |
| --- | --- |
| `+ getAllPasswords() : Iterable<Password>` | Gets all `Password` entities of the `PasswordRepository`. |
| `+ getPasswordById(long) : Password` | Gets the `Password` with the given `id`. |
| `+ getPasswordByCreationDate(Timestamp) : Password` | Gets the first `Password` found with the given creation `Timestamp`. |
| `+ deletePassword(Password) : void` | Deletes the given `Password`. |
| `+ modifyPasswordValue(Password, String) : void` | Modifies the value of the given `Password`. |
| `+ savePassword(Password) : Password` | Saves the given `Password` in the `PasswordRepository`. |

### UI
  - [CCKEY-24][jira-cckey-24] Implemented the prototype web UI Thymeleaf templates and CSS files

[jira-cckey-15]: https://jira.communicode.de/browse/CCKEY-15
[jira-cckey-16]: https://jira.communicode.de/browse/CCKEY-16
[jira-cckey-17]: https://jira.communicode.de/browse/CCKEY-17
[jira-cckey-23]: https://jira.communicode.de/browse/CCKEY-23
[jira-cckey-24]: https://jira.communicode.de/browse/CCKEY-24
