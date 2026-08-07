# IMDBApp


## Custom Lint Rules

This project includes custom Android Lint checks maintained under `com.example.lint` (`IMDBIssueRegistry`) to enforce Jetpack Compose parameter standards.

### Rules Enforced

* **`MissingComposableModifier` (Warning):** Requires public, non-preview `@Composable` functions to accept a `modifier: Modifier = Modifier` parameter so callers can customize layout and behavior.
* **`ModifierPosition` (Warning):** Enforces that the `modifier` parameter is positioned as the **first optional parameter** (the first parameter with a default value) in the function signature, maintaining consistency with Compose guidelines.

---

### Running Lint

To check your code against these custom rules, run the following Gradle commands from your project root:

**Check the entire project:**
```bash
./gradlew lint