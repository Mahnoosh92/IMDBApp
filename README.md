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
```

---

## Code Formatting with Spotless

We use [Spotless](https://github.com/diffplug/spotless) with [Ktlint](https://pinterest.github.io/ktlint/) to maintain consistent code style across the project.

### Apply Formatting

To automatically fix formatting issues (like wildcard imports, indentation, or naming conventions), run:

```bash
./gradlew spotlessApply --rerun-tasks
```

### Check Formatting

To check if the code complies with the style guide without applying changes:

```bash
./gradlew spotlessCheck
```
