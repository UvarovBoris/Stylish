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
- **Theming:** Translate Figma design tokens into custom app theming in `ui/theme/` (`StylishColors`, `Color`, `Typography`, `Shape`). Use `StylishTheme.colors` for app styling and component colors across all app screens. Use `MaterialTheme` for base setup and scaffolding where it makes sense (e.g., `Scaffold`, basic surface/content color defaults, ripple effects). Never hardcode ad-hoc colors or font styles in composables.

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

## 4. Architecture & State Management (MVI / UDF)
- **Single Source of Truth:** ViewModels expose a single immutable `StateFlow<UiState>`.
- **Atomic State Updates:** Always update state via `_uiState.update { it.copy(...) }`.
- **Single Intent Entry Point:** ViewModels expose a single entry point method `fun onIntent(intent: FeatureIntent)` taking a `sealed interface FeatureIntent` for UI actions/events, rather than multiple loose `on...` methods.
- **One-Time Side Effects:** Transitory events (navigation, toast, snackbar) must NOT be stored in `UiState`. Instead, emit them via a buffered `Channel<FeatureSideEffect>` exposed as `Flow<FeatureSideEffect> = _sideEffect.receiveAsFlow()`. Collect side effects in the UI within `LaunchedEffect(viewModel)` or `LaunchedEffect(Unit)`.
- **Collection in UI:** Collect state strictly with `collectAsStateWithLifecycle()`.
- **Layers:**
  - **UI Layer:** Composable screens + ViewModels + UiState + Intents + Side Effects.
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
- **Theming:** Use `StylishTheme.colors` for app styling and custom components. Use `MaterialTheme` for base framework setup and `MaterialTheme.typography`. Never hardcode raw hex colors in composables.
- **Imports:** Never use wildcard imports (`import foo.bar.*`).
- **Testing:** MockK + JUnit, **Turbine** for testing Flows and Channels, `StandardTestDispatcher` for Coroutine tests (`Dispatchers.setMain(testDispatcher)` / `Dispatchers.resetMain()`, advancing virtual time with `testScheduler.advanceUntilIdle()`), and Compose UI testing rules.

---

## 7. Modularization & Use Case Structure
See detailed specification in `docs/ARCHITECTURE.md`.
- **Pattern:** Feature-first + shared core modules (`:app`, `:feature:*`, `:core:*`).
- **Feature Isolation:** Feature modules (`:feature:*`) must NEVER depend on each other.
- **Module `.gitignore`:** Every module (including all new `:feature:*` and `:core:*` modules) MUST contain its own `.gitignore` ignoring `/build`.
- **Use Case Placement:**
  - **`:core:domain`**: Shared cross-feature business logic (e.g. `AddToCartUseCase`, `ToggleWishlistUseCase`, `GetCartBadgeCountUseCase`).
  - **`:feature:<name>`**: Feature-private business logic (e.g. `ValidateShippingAddressUseCase`, `ApplyProductFilterUseCase`).
  - **Direct Repository**: ViewModels can inject repositories directly from `:core:data` when no business logic or multi-repo coordination is needed (no redundant pass-through use cases).
- **Core Modules:**
  - `:core:designsystem` (Figma tokens, `StylishColors`, atomic UI)
  - `:core:ui` (shared composables like `ProductCard`)
  - `:core:model` (pure Kotlin domain models)
  - `:core:domain` (shared cross-feature use cases)
  - `:core:data` (repositories & sync)
  - `:core:network` (API client & DTOs)
  - `:core:database` (Room DB, DAOs)
  - `:core:datastore` (preferences & session tokens)
  - `:core:common` (dispatchers, Result wrappers)

