# AI Agent Guidelines (Stylish)

Project guidelines and standards for AI coding assistants working on the **Stylish** Android app.

---

## 1. Product Overview
- **App Name:** Stylish
- **Domain:** Fashion E-Commerce Mobile App.
- **Core Features:** Product catalog, categories, search & filtering, product details, shopping cart, checkout flow, wishlist, and user account management.

---

## 2. Design System & Figma Reference
- **Design Source:** [Figma: eCommerce App UI Kit](https://www.figma.com/design/QLfS37a0puFWZ15N1hpyOK/eCommerce-App-UI-Kit---Case-Study-Ecommerce-Mobile-App-UI-kit--Community-?node-id=1-16990&p=f&t=e81cBV6fUcQgQo1R-0)
  - **File Key:** `QLfS37a0puFWZ15N1hpyOK`
  - **Starting Node ID:** `1:16990`
- **Figma MCP Integration:** Always use **Framelink MCP for Figma** (`get_figma_data`, `download_figma_images`) to inspect component dimensions, paddings, color codes, and typography, and to download required vector assets/icons before implementing UI.
- **Theming:** Translate Figma design tokens into Material 3 `ui/theme/` (Color, Typography, Shape). Never hardcode ad-hoc colors or font styles in composables.

---

## 3. Tech Stack
- **Language:** Kotlin 2.x
- **UI:** 100% Jetpack Compose + Material Design 3 (no XML, ViewBinding, or Fragments)
- **Architecture:** Clean Architecture + Unidirectional Data Flow (UDF)
- **Dependency Injection:** Dagger Hilt
- **Navigation:** Navigation 3 (`androidx.navigation3`) — do NOT use Navigation 2 or `NavController`
- **Async & State:** Kotlin Coroutines + `StateFlow` (never `LiveData` or RxJava)
- **Build System:** Gradle Kotlin DSL with Version Catalog (`gradle/libs.versions.toml`)

---

## 4. Architecture & State Management (UDF)
- **Single Source of Truth:** ViewModels expose a single immutable `StateFlow<UiState>`.
- **Atomic State Updates:** Always update state via `_uiState.update { it.copy(...) }`.
- **Collection in UI:** Collect state strictly with `collectAsStateWithLifecycle()`.
- **Layers:**
  - **UI Layer:** Composable screens + ViewModels + UiState.
  - **Domain Layer:** Pure Kotlin models and single-responsibility Use Cases (`operator fun invoke(...)`).
  - **Data Layer:** Repositories and DataSources (Room, Retrofit/Ktor), returning `Flow<T>` or `Result<T>`.

---

## 5. Jetpack Compose Standards

### Stateful Screen vs. Stateless Content
Split every screen into two composables:
1. **Stateful Screen:** Injects ViewModel (`hiltViewModel()`), collects `UiState`, handles navigation/events.
2. **Stateless Content:** Takes immutable `UiState` and event lambdas. Contains no ViewModel references; previewable with `@Preview`.

### Parameter Ordering
1. `modifier: Modifier = Modifier` (first optional parameter; applied to root layout)
2. Required data parameters
3. Optional configuration / styling
4. Event callbacks (`onItemClick: () -> Unit`)
5. Trailing composable lambda (`content: @Composable () -> Unit`)

### Performance
- Always provide unique `key`s in `LazyColumn`/`LazyRow` items (`key = { item.id }`).
- Use `derivedStateOf` for values derived from frequently changing state (e.g. scroll state).
- Wrap collections or custom models in `@Immutable` / `@Stable` or use `ImmutableList`.
- Never allocate heavy objects or coroutine dispatchers inside composables without `remember`.

---

## 6. Code Quality & Conventions
- **Dependencies:** All dependencies and versions MUST go into `gradle/libs.versions.toml`. Never hardcode library versions in `build.gradle.kts`.
- **Strings & Assets:** Never hardcode user-facing strings; use `res/values/strings.xml` and `stringResource(...)`.
- **Theming:** Use `MaterialTheme.colorScheme` and `MaterialTheme.typography` exclusively.
- **Imports:** Never use wildcard imports (`import foo.bar.*`).
- **Testing:** MockK + JUnit, **Turbine** for testing Flows, and Compose UI testing rules.
