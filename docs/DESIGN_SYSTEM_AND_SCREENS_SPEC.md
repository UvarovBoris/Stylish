# Stylish - Design System & UI Specifications

> **Source:** Figma eCommerce App UI Kit (File Key: `QLfS37a0puFWZ15N1hpyOK`, Node: `1:16990`)  
> **Offline Snapshot Date:** October 2026  
> **Target Framework:** Jetpack Compose (Kotlin 2.x, Material 3)  
> **Base Screen Dimensions:** 375 x 812 dp (Standard Viewport) / 390 x 844 dp

---

## 1. Design System Foundations

### 1.1 Color Palette (`StylishColors`)

| Color Token Name | Hex / RGBA | Role / Usage in App |
|---|---|---|
| **Primary (Brand Red/Pink)** | `#F83758` | Primary action buttons, active tab indicators, promotional badges, links |
| **Secondary (Soft Pink)** | `#FA7189` | Secondary accents, tags, subtle highlight badges, gradient stops |
| **Primary Blue** | `#4392F9` | Informational highlights, link texts ("Details", "VIP"), blue badges |
| **Sale / Discount Coral** | `#FE735C` | "50% Off", discount tags, urgency badges |
| **Alert / Error Red** | `#EB3030` | Form validation errors, critical discount alerts ("upto 33% off") |
| **Dark Plum / Brand Dark** | `#21003D` | Dark headers, special banner background ("DARK BAG") |
| **Background Pure White** | `#FFFFFF` | Screen backgrounds (Auth, Splash, Success), card surfaces |
| **Background Off-White** | `#FDFDFD` | Main app content background (Home, Shop, Trending, Checkout) |
| **Surface Gray 100** | `#F2F2F2` | Input field background, card background, placeholder boxes |
| **Surface Gray 50** | `#F8F8F8` | Subtle card background, alternate section stripes |
| **Surface Gray 75** | `#F4F4F4` | Secondary card backgrounds, order item card background |
| **Border Gray 200** | `#D9D9D9` | Form input borders, dividers, card outlines |
| **Border Gray 250** | `#D2D2D2` | Secondary dividers, disabled outlines |
| **Border Gray 300** | `#C8C8C8` | Search bar border, inactive chips |
| **Muted Gray 400** | `#BBBBBB` | Inactive indicators, pagination inactive dots, placeholder icons |
| **Muted Gray 450** | `#C4C4C4` | Pagination "Prev" button, disabled states |
| **Placeholder Gray 500** | `#A4A9B3` | Input placeholder text, rating review counts (`344567`) |
| **Text Secondary 600** | `#808488` | Strikethrough original prices, secondary labels, variation labels |
| **Text Secondary Alt** | `#828282` | Secondary body text, timestamps, subtitle notes |
| **Text Muted 700** | `#6E7179` | Masked numbers ("*********2109"), card brand descriptions |
| **Text Body 800** | `#575757` | Terms & conditions text, form field labels |
| **Text Primary Dark** | `#232327` | Primary headings, product titles, dialog headers |
| **Text Pure Black** | `#000000` | High-contrast titles, primary bold prices, status bar text |

#### Special Overlays & Gradients
- **Promo Pink Gradient:** `linear-gradient(139deg, #FFC8D1 0%, #FFFFFF 100%)`
- **Blue Card Gradient (`l2`):** `linear-gradient(180deg, #3F92FF 0%, #0B3689 100%)`
- **Green Card Gradient (`l4`):** `linear-gradient(180deg, #71F9A9 0%, #31B769 100%)`
- **Glassmorphic Surface:** `linear-gradient(134deg, rgba(255, 255, 255, 1) 0%, rgba(255, 255, 255, 0.7) 69%)`
- **Gold Accent Gradient:** `linear-gradient(90deg, #EFAD18 25%, #F8D7B4 100%)`
- **Subtle Surface Tint (`s 1`):** `rgba(231, 231, 235, 0.30)`

---

### 1.2 Typography System

The primary typeface across all screens is **Montserrat**. Secondary accents use **Poppins** (special labels/headers), **Roboto** (navigation tags), and **Alegreya SC** (brand titles).

| Style Token | Font Family | Weight | Size (sp/pt) | Line Height | Letter Spacing | Alignment | Primary Usage |
|---|---|---|---|---|---|---|---|
| **Display Large** | Montserrat | Bold (700) | 80sp | 122sp | 0.02em | Left | Onboarding hero text, large campaign banners |
| **Display Medium** | Montserrat | Bold (700) | 36sp | 43sp | - | Left | "Welcome Back!", "Create an account", "Forgot password?" |
| **Display Small** | Montserrat | Medium (500) | 32sp | 52sp | - | Center | Splash promo headline, Hero highlights |
| **Headline Large** | Montserrat | ExtraBold (800) | 24sp | Auto | - | Center | Splash onboarding title, prominent headings |
| **Headline Medium** | Montserrat | Bold (700) | 22sp | 22sp | -0.018em | Center/Left | "Continue" button, "Get Started" CTA button |
| **Headline Small** | Montserrat | SemiBold (600) | 20sp | 22sp | - | Left | Section headers ("Deal of the Day", "Special Offers") |
| **Title Large** | Montserrat | SemiBold (600) | 18sp | 22sp | - | Left/Center | Screen TopBar titles ("Checkout", "Profile", "Shop") |
| **Title Medium** | Montserrat | Medium (500) | 18sp | 24sp | - | Left | Order summary titles ("Order", "Shipping", "Total") |
| **Title Small** | Montserrat | SemiBold (600) | 16sp | 22sp | - | Left | Product card titles, prices in cart ("$ 34.00") |
| **Body Large** | Montserrat | Medium (500) | 16sp | 20sp | - | Left | "NIke Sneakers", Banner subtitle "Flat and Heels" |
| **Body Large Regular**| Montserrat | Regular (400) | 16sp | Auto | -0.044em | Left | Large placeholder text, input fields |
| **Body Medium Semi** | Montserrat | SemiBold (600) | 14sp | 24sp | 0.02em | Center | Onboarding description text ("Amet minim...") |
| **Body Medium Semi 2**| Montserrat | SemiBold (600) | 14sp | 16sp | - | Left | Product card headers, "Delivery Address" |
| **Body Medium** | Montserrat | Medium (500) | 14sp | 16sp | - | Left | Secondary form labels, account settings |
| **Body Medium Reg** | Montserrat | Regular (400) | 14sp | 20sp | - | Left | Search placeholder ("Search any Product..") |
| **Body Small Semi** | Montserrat | SemiBold (600) | 12sp | 16sp | - | Center | "View all", "Shop Now", Button text |
| **Body Small Med** | Montserrat | Medium (500) | 12sp | 16sp | - | Left | Product prices ("₹1,900"), star rating numbers ("4.8")|
| **Body Small Reg** | Montserrat | Regular (400) | 12sp | 16sp | - | Left | Multi-line description, variation details, address |
| **Caption Semi** | Montserrat | SemiBold (600) | 12sp | 22sp | - | Right | "Total Order (1) :", order item subtotals |
| **Caption Med** | Montserrat | Medium (500) | 10sp | 16sp | - | Left | Variation chips ("Black", "Red", "Size: 7UK") |
| **Caption Reg** | Montserrat | Regular (400) | 10sp | 16sp | - | Left | Product snippet subtitle, review count |
| **Label Small** | Montserrat | Medium (500) | 8sp | 22sp | - | Left | Urgent badges ("upto 33% off", "50%Off") |
| **Poppins Medium** | Poppins | Medium (500) | 15sp | 20sp | - | Center | Top navigation tabs, specialty buttons |
| **Roboto Nav** | Roboto | Regular (400) | 12sp | 16sp | 0.033em | Center | Bottom navigation labels ("Home", "Wishlist", "Setting")|
| **Brand Alegreya**| Alegreya SC | ExtraBold (800) | 28sp | 1.17em | - | Center | "Stylish" brand logo typography |

---

### 1.3 Shapes, Borders, Shadows & Elevation

- **Button Corner Radius:** `4dp` to `6dp` (standard primary & secondary buttons), `24dp` / Pill (chips, "Shop Now", "Visit now")
- **Card Corner Radius:** `6dp` to `8dp` (Product cards, promo banners), `10dp` (Address card, order item card)
- **Input Field Corner Radius:** `8dp` to `10dp` (with `1dp` border in `#D9D9D9`)
- **Circular Elements:** `50% / Circle` (Category icons `56x56dp`, User avatar `96x96dp`, Social login circle buttons `54x54dp`)
- **Shadow Tokens:**
  - **Card Elevation 1 (`effect_f45ab841`):** `0px 1px 16px rgba(0, 0, 0, 0.08)` (Product cards, bottom bar)
  - **Button / Chip Shadow (`effect_9a350530`):** `0px 2px 2px rgba(0, 0, 0, 0.15)`
  - **Soft Elevation (`effect_24fb9266`):** `0px 6px 14px -8px rgba(0, 0, 0, 0.25)`

---

## 2. Reusable Component Specifications

### 2.1 Top App Bar / Toolbar
- **Height:** `56dp` (plus status bar `44dp`)
- **Background:** `#FDFDFD` / Transparent
- **Variants:**
  1. **Home / Main Top Bar:**
     - Left: Hamburger Menu icon (`24x24dp`, `#232327`)
     - Center: "Stylish" Logo (Brand logo mark in `#F83758` + "Stylish" text in Alegreya SC)
     - Right: Profile Avatar circle (`32x32dp`, bordered)
  2. **Screen Header with Back Navigation (Checkout, Profile, Shop, Shipping):**
     - Left: Back Arrow button (`24x24dp`, circular click area `40x40dp`)
     - Center: Screen Title (e.g. "Checkout", "Profile", Montserrat SemiBold 18sp, `#000000`)
     - Right: Optional Action (e.g. Heart / Cart icon, `24x24dp`)

### 2.2 Search Bar
- **Dimensions:** Width `343dp` (full width with `16dp` side margins), Height `40dp`
- **Background:** `#FFFFFF`
- **Border:** `1dp` solid `#BBBBBB` or `#D9D9D9`, Corner Radius `10dp`
- **Content:**
  - Left: Search icon (`18x18dp`, `#BBBBBB`), `12dp` left padding
  - Center: Placeholder text: `"Search any Product.."` (Montserrat Regular 14sp, `#BBBBBB`)
  - Right: Voice Search / Microphone icon (`18x18dp`, `#BBBBBB`), `12dp` right padding

### 2.3 Bottom Navigation Bar
- **Dimensions:** Width `375dp`, Height `64dp` (plus navigation bar insets)
- **Background:** `#FFFFFF`, Elevation `8dp` (`boxShadow: 0px -2px 10px rgba(0,0,0,0.05)`)
- **Items (5 tabs, equally spaced `75dp` each):**
  1. **Home:** Icon (`24x24dp`), Label: "Home" (Roboto Regular 12sp, Active: `#F83758`, Inactive: `#000000`)
  2. **Wishlist:** Icon (`24x24dp`), Label: "Wishlist"
  3. **Cart:** Icon (`24x24dp`), Center highlighted / Badge support
  4. **Search:** Icon (`24x24dp`), Label: "Search"
  5. **Setting:** Icon (`24x24dp`), Label: "Setting"

### 2.4 Product Card (Standard Grid & Carousel)
- **Dimensions:** Width `164dp` to `170dp`, Height `240dp` to `260dp`
- **Background:** `#FFFFFF`, Corner Radius `6dp` to `8dp`, Elevation `2dp`
- **Card Hierarchy:**
  1. **Product Image:** Height `124dp` to `136dp`, `ContentScale.Crop`, Corner Radius `4dp` top
  2. **Product Title:** Montserrat Medium 12sp, `#000000`, max 1-2 lines with ellipsis (e.g. "HRX by Hrithik Roshan")
  3. **Description / Snippet:** Montserrat Regular 10sp, `#000000` or `#808488`, max 2 lines
  4. **Pricing Row:**
     - Current Price: Montserrat Medium 12sp, `#000000` (e.g. "₹2499")
     - Strikethrough Price: Montserrat Light 12sp, `#808488` (e.g. "₹4999") with strikethrough line
     - Discount Tag: Montserrat Regular 10sp, `#FE735C` (e.g. "50%Off")
  5. **Rating Row:**
     - 5 Gold/Yellow Star icons (`12x12dp` each, `#EDB310`)
     - Review Count: Montserrat Regular 10sp, `#A4A9B3` (e.g. "344567")

### 2.5 Promotional Banners & Carousels
- **Main Home Promo Banner:**
  - Width `343dp`, Height `189dp`, Corner Radius `12dp`
  - Background: Pink gradient or promotional background graphic
  - Headline: "50-40% OFF" (Montserrat Bold 20sp, `#FFFFFF`)
  - Subtitle: "Now in products\nAll colours" (Montserrat Regular 12sp, `#FFFFFF`)
  - Button: "Shop Now" (Pill shape, Background `#F83758`, White text with right arrow)
  - Dot Indicators: 3 dots at bottom center (Active `#F83758`, Inactive `#BBBBBB`)
- **Deal of the Day Banner:**
  - Height `60dp`, Background `#4392F9`, Corner Radius `8dp`
  - Left: "Deal of the Day" (Montserrat SemiBold 16sp, `#FFFFFF`), Clock icon + "22h 55m 20s remaining" (Montserrat Regular 12sp, `#FFFFFF`)
  - Right: "View all" button (Outlined / White pill button with right arrow)
- **Special Offers / "Flat and Heels" Banner:**
  - Width `343dp`, Height `172dp`, Background White with Gold border/accent gradient
  - Image: Left/Center high-resolution shoe graphic
  - Right: Title "Flat and Heels" (Montserrat Medium 16sp, `#232327`), Subtitle "Stand a chance to get rewarded" (Montserrat Regular 10sp), "Visit now" button (Pill `#F83758`, White text)

---

## 3. Detailed Specifications for All 16 Screens

### Screen 1: Splash Screen (Brand Splash)
- **Frame ID:** `#1:18734`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FFFFFF`
- **Content:**
  - Center: Brand Logo Graphic (`120 x 100 dp`)
  - Brand Name: `"Stylish"` (Alegreya SC ExtraBold, 40sp, `#F83758`)
- **Flow:** Automatically displays for ~2 seconds, transitions to Onboarding / Splash 1.

---

### Screen 2: Splash screen-1 (Onboarding 1 - "Choose Products")
- **Frame ID:** `#1:17865`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FFFFFF`
- **Status Bar:** `9:41` (Montserrat Thin Italic 15sp, `#000000`)
- **Top Row:**
  - Left: Step indicator: `"1/3"` (Current step 1 in `#000000`, "/3" in `#A0A0A1`, Montserrat SemiBold 18sp)
  - Right: `"Skip"` text button (Montserrat SemiBold 18sp, `#000000`, top `44dp`, right `16dp`)
- **Center Hero Illustration:**
  - Illustration depicting shopping & product selection (`width 300dp, height 300dp`)
- **Content Block:**
  - Title: `"Choose Products"` (Montserrat ExtraBold 24sp, `#000000`, centered)
  - Description: `"Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. Velit officia consequat duis enim velit mollit."` (Montserrat SemiBold 14sp, lineHeight 24sp, `#A8A8A9`, centered)
- **Bottom Navigation Controls:**
  - Center: 3-dot page indicator (Dot 1: Active capsule `32x8dp` in `#180804`, Dots 2 & 3: Inactive circles `8x8dp` in `#D9D9D9`)
  - Right: `"Next"` button (Montserrat SemiBold 18sp, `#F83758`)

---

### Screen 3: Splash screen-2 (Onboarding 2 - "Make Payment")
- **Frame ID:** `#1:18135`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FFFFFF`
- **Status Bar:** `9:41`
- **Top Row:**
  - Left: `"2/3"` (Step 2 in `#000000`, "/3" in `#A0A0A1`)
  - Right: `"Skip"` button (Montserrat SemiBold 18sp, `#000000`)
- **Center Hero Illustration:**
  - Payment & online checkout illustration (`width 300dp, height 300dp`)
- **Content Block:**
  - Title: `"Make Payment"` (Montserrat ExtraBold 24sp, `#000000`, centered)
  - Description: Same onboarding copy (Montserrat SemiBold 14sp, `#A8A8A9`, centered)
- **Bottom Navigation Controls:**
  - Left: `"Prev"` button (Montserrat SemiBold 18sp, `#C4C4C4`)
  - Center: 3-dot page indicator (Dot 2: Active capsule `32x8dp` in `#180804`)
  - Right: `"Next"` button (Montserrat SemiBold 18sp, `#F83758`)

---

### Screen 4: Splash screen-3 (Onboarding 3 - "Get Your Order")
- **Frame ID:** `#1:18392`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FFFFFF`
- **Top Row:**
  - Left: `"3/3"`
  - Right: `"Skip"` button
- **Center Hero Illustration:**
  - Delivery package & doorstep fulfillment illustration (`300 x 300 dp`)
- **Content Block:**
  - Title: `"Get Your Order"` (Montserrat ExtraBold 24sp, `#000000`, centered)
  - Description: Onboarding copy (Montserrat SemiBold 14sp, `#A8A8A9`, centered)
- **Bottom Navigation Controls:**
  - Left: `"Prev"` button (Montserrat SemiBold 18sp, `#C4C4C4`)
  - Center: 3-dot page indicator (Dot 3: Active capsule `32x8dp` in `#180804`)
  - Right: `"Get Started"` action button (Montserrat SemiBold 18sp, `#F83758`)

---

### Screen 5: Get Started Screen
- **Frame ID:** `#1:16994`, **Dimensions:** `390 x 844 dp`
- **Background:** Full-bleed background fashion photography with dark gradient overlay at bottom
- **Content Placement (Bottom Aligned):**
  - Headline: `"You want\nAuthentic, here\nyou go!"` (Montserrat SemiBold 34sp, lineHeight 40sp, `#FFFFFF`)
  - Subtitle: `"Find it here, buy it now"` (Montserrat Regular 14sp, `#F2F2F2`)
  - Primary CTA Button:
    - Dimensions: Width `343dp`, Height `55dp`, Corner Radius `4dp`
    - Background: `#F83758`
    - Text: `"Get Started"` (Montserrat Bold 22sp, `#FFFFFF`, centered)
- **Flow:** Tap "Get Started" -> Navigates to Sign In / Sign Up.

---

### Screen 6: Sign In Screen
- **Frame ID:** `#1:18612`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FFFFFF`
- **Header:**
  - Title: `"Welcome \nBack!"` (Montserrat Bold 36sp, lineHeight 43sp, `#000000`, left aligned, top `80dp`, left `32dp`)
- **Form Fields (Gap 16dp):**
  1. **Username or Email:**
     - Height `55dp`, Corner Radius `10dp`, Background `#F3F3F3`, Border `1dp` solid `#A8A8A9`
     - Left Icon: User / Profile icon (`20x20dp`, `#626262`)
     - Placeholder: `"Username or Email"` (Montserrat Medium 12sp, `#676767`)
  2. **Password:**
     - Height `55dp`, Corner Radius `10dp`, Background `#F3F3F3`, Border `1dp` solid `#A8A8A9`
     - Left Icon: Lock icon (`20x20dp`, `#626262`)
     - Right Icon: Eye / Password visibility toggle icon (`20x20dp`, `#626262`)
     - Placeholder: `"Password"` (Montserrat Medium 12sp, `#676767`)
- **Forgot Password Row:**
  - Right aligned link: `"Forgot Password?"` (Montserrat Regular 12sp, `#F83758`)
- **Primary CTA Button:**
  - Height `55dp`, Background `#F83758`, Corner Radius `4dp`
  - Text: `"Login"` (Montserrat SemiBold 20sp, `#FFFFFF`, centered)
- **Social Login Divider:**
  - Text: `"- OR Continue with -"` (Montserrat Medium 12sp, `#575757`, centered, margin top `40dp`)
- **Social Buttons Row (3 circles, gap 16dp):**
  - Google, Apple, Facebook circular icon buttons (`54x54dp`, Background `#FCF3F6`, Border `1dp` `#F83758`)
- **Footer Text:**
  - Centered: `"Create An Account "` (Montserrat Regular 14sp, `#575757`) + `"Sign Up"` (Montserrat SemiBold 14sp, underline, `#F83758`)

---

### Screen 7: Sign Up Screen
- **Frame ID:** `#1:18668`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FFFFFF`
- **Header:**
  - Title: `"Create an \naccount"` (Montserrat Bold 36sp, lineHeight 43sp, `#000000`, left `32dp`)
- **Form Fields (Height 55dp, Corner Radius 10dp):**
  1. Username or Email (User icon, placeholder: "Username or Email")
  2. Password (Lock icon + Eye icon, placeholder: "Password")
  3. Confirm Password (Lock icon + Eye icon, placeholder: "ConfirmPassword")
- **Terms & Legal Notice:**
  - `"By clicking the "` + `"Register"` (`#F83758`) + `" button, you agree to the public offer"` (Montserrat Regular 12sp, `#676767`)
- **Primary CTA Button:**
  - `"Create Account"` (Montserrat SemiBold 20sp, `#FFFFFF`, Background `#F83758`, Height `55dp`)
- **Social Login:**
  - `"- OR Continue with -"` + Google / Apple / Facebook circular buttons
- **Footer:**
  - `"I Already Have an Account "` + `"Login"` (Montserrat SemiBold 14sp, `#F83758`)

---

### Screen 8: Forgot Password Screen
- **Frame ID:** `#1:18585`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FFFFFF`
- **Header:**
  - Title: `"Forgot\npassword?"` (Montserrat Bold 36sp, lineHeight 43sp, `#000000`)
- **Form Field:**
  - Email input (`Height 55dp`, Corner Radius `10dp`, Mail icon, placeholder: `"Enter your email address"`)
- **Informative Note:**
  - `"* We will send you a message to set or reset your new password"` (Asterisk in `#F83758`, body in Montserrat Regular 12sp, `#676767`)
- **Primary Button:**
  - `"Submit"` (Montserrat SemiBold 20sp, `#FFFFFF`, Background `#F83758`, Height `55dp`)

---

### Screen 9: Home Page Screen
- **Frame ID:** `#1:17021`, **Dimensions:** `375 x 2209 dp` (Scrollable LazyColumn)
- **Background:** `#FDFDFD`
- **Top Bar (Sticky):** Hamburger Menu (`24dp`), "Stylish" brand logo, User Avatar (`32dp`)
- **Search Bar:** `343 x 40 dp`, "Search any Product..", Mic icon
- **Filter & Sort Header Row:**
  - Left: `"All Featured"` (Montserrat SemiBold 18sp, `#000000`)
  - Right: `"Sort"` button (Icon + "Sort", Montserrat Regular 12sp) & `"Filter"` button (Icon + "Filter")
- **Category Circular List (Horizontal Scroll):**
  - 5 circular categories: Beauty, Fashion, Kids, Mens, Womens
  - Each item: Circle image (`56 x 56 dp`) + Label (Montserrat Medium 10sp, `#232327`)
- **Main Hero Promo Carousel:**
  - Banner (`343 x 189 dp`): "50-40% OFF", "Now in products", "All colours", "Shop Now" CTA pill button
  - Page indicator dots (3 dots)
- **"Deal of the Day" Section:**
  - Banner header (`343 x 60 dp`, Background `#4392F9`): Clock icon + "22h 55m 20s remaining", "View all" button
  - Horizontal Product Row: 4 Product Cards (`170 x 240 dp`):
    1. "Women Printed Kurta", ₹1500 (was ₹2499, 40% Off), 56,890 reviews
    2. "HRX by Hrithik Roshan", ₹2499 (was ₹4999, 50% Off), 344,567 reviews
    3. "Philips BHH880/10", ₹999 (was ₹1999, 50% Off), 646,776 reviews
    4. "TITAN Men Watch- 1806N", ₹1500 (was ₹3500, 60% Off), 15,007 reviews
- **Special Offer Card:**
  - "Special Offers 😱", "We make sure you get the offer you need at best prices"
- **"Flat and Heels" Banner:**
  - Banner (`343 x 172 dp`): Shoe artwork, "Flat and Heels", "Stand a chance to get rewarded", "Visit now" pill button
- **"Trending Products" Section:**
  - Header: "Trending Products", "Last Date 29/02/22", "View all"
  - Horizontal Product Row: Watches, Bags, Footwear cards
- **Sponsored Banner:**
  - "Sponserd", "up to 50% Off"
- **Bottom Navigation Bar (Fixed):** 5 tabs (Home, Wishlist, Cart, Search, Setting)

---

### Screen 10: Trending Products Screen
- **Frame ID:** `#1:17150`, **Dimensions:** `375 x 2074 dp`
- **Background:** `#FDFDFD`
- **Top Bar & Search Bar:** Same as Home
- **Results Count Row:**
  - Left: `"52,082+ Iteams "` (Montserrat SemiBold 18sp, `#000000`)
  - Right: `"Sort"` and `"Filter"` buttons
- **2-Column Product Grid:**
  - Grid of `164 x 260 dp` cards:
    - "Autumn And Winter Casual cotton-padded jacket...", ₹499
    - "Antheaa Black & Rust Orange Floral Print Tiered Midi F...", ₹1,990
    - "Sony PS4 Console, 1TB Slim with 3 Games", ₹1,999
    - "Mens Starry Sky Printed Shirt", ₹399
    - "Blue cotton denim dress", ₹999
    - "Solid Black Dress for Women", ₹2,000
    - "George Walker Derby Brown Formal Shoes", ₹999
    - "Realme 7 (6 GB RAM | 64 GB ROM)", ₹3,499
    - "Air Jordan 12 Stay", ₹4,999
- **Bottom Navigation Bar:** Fixed at bottom

---

### Screen 11: Shop Page (Product Details Screen)
- **Frame ID:** `#1:17220`, **Dimensions:** `375 x 1285 dp`
- **Background:** `#FDFDFD`
- **Top Bar:** Back button (`24dp`), Cart icon (`24dp`)
- **Product Image Gallery:**
  - Large hero product image (`343 x 213 dp`) with 5 carousel indicator dots
- **Size Selector:**
  - Label: `"Size: 7UK"` (Montserrat SemiBold 14sp)
  - Horizontal chips: `6 UK`, `7 UK` (Selected: `#FA7189` border/fill), `8 UK`, `9 UK`, `10 UK`
- **Product Title & Details:**
  - Title: `"NIke Sneakers"` (Montserrat SemiBold 20sp)
  - Subtitle: `"Vision Alta Men’s Shoes Size (All Colours)"` (Montserrat Regular 14sp)
  - Rating: 5 gold stars + `"56,890"` review count
  - Pricing: `"₹1,500"` (Current, Montserrat SemiBold 16sp), `"₹2,999"` (Strikethrough, `#808488`), `"50% Off"` (`#FE735C`)
- **Description Expandable Section:**
  - Header: `"Product Details"` (Montserrat Medium 14sp)
  - Text: `"Perhaps the most iconic sneaker of all-time, this original "Chicago" colorway is the cornerstone to any sneaker collection..."` + `"More"` link in `#FA7189`
- **Feature Badges Row:**
  - `"Nearest Store"`, `"VIP"`, `"Return policy"`
- **Delivery Availability:**
  - `"Delivery in 1 within Hour"`
- **Action Buttons Row:**
  - Left: `"Go to cart"` (Outlined / Light button, Height `44dp`)
  - Right: `"Buy Now"` (Primary button, Background `#31B769` Green or `#F83758`, Height `44dp`)
- **Similar Products Carousel:**
  - Header: `"Similar To"` + `"282+ Iteams "`, Sort & Filter chips
  - Horizontal row of similar sneaker cards

---

### Screen 12: Profile Screen
- **Frame ID:** `#1:17411`, **Dimensions:** `375 x 1657 dp`
- **Background:** `#FDFDFD`
- **Top Bar:** Back arrow, Title: `"Checkout"` / `"Profile"` (Montserrat SemiBold 18sp)
- **Profile Header:**
  - Avatar image: Circle (`96 x 96 dp`) with Edit Pencil badge icon (`24x24dp` at bottom-right of avatar)
- **Section 1: Personal Details:**
  - Email Address: `"aashifa@gmail.com"`
  - Password: `"***********"` + `"Change Password"` link (`#F83758`)
- **Section 2: Business / Delivery Address Details:**
  - Pincode: `"450116"`
  - Address: `"216 St Paul's Rd, London"`
  - City: `"London"`
  - State: `"N1 2LL,"`
  - Country: `"United Kingdom"`
- **Section 3: Bank Account Details:**
  - Bank Account Number: `"204356XXXXXXX"`
  - Account Holder’s Name: `"Abhiraj Sisodiya"`
  - IFSC Code: `"SBIN00428"`
- **Bottom Action Button:**
  - `"Save"` button (Height `55dp`, Background `#F83758`, Corner Radius `4dp`, Text: Montserrat Bold 20sp White)

---

### Screen 13: Checkout Screen (Shopping Bag / Cart)
- **Frame ID:** `#1:17482`, **Dimensions:** `375 x 812 dp`
- **Background:** `#FDFDFD`
- **Top Bar:** Back button, Title: `"Checkout"`
- **Delivery Address Card:**
  - Dimensions: `331 x 79 dp`, Background `#FFFFFF`, Corner Radius `8dp`, Shadow
  - Left: Location Pin icon (`24x24dp`)
  - Content: Title `"Address :"`, Body: `"216 St Paul's Rd, London N1 2LL, UK\nContact :  +44-784232"`
  - Right: Edit Pencil icon (`24x24dp`)
- **Shopping List Items:**
  - Section Header: `"Shopping List"`
  - **Item Card 1: "Women’s Casual Wear"**
    - Thumbnail: `120 x 120 dp`, Corner Radius `4dp`
    - Title: `"Women’s Casual Wear"` (Montserrat SemiBold 14sp)
    - Variations: Size & Color chips (`"Black"`, `"Red"`)
    - Rating: `"4.8"` (5 stars)
    - Price: `"$ 34.00"` (Current), `"$ 64.00"` (Original), `"upto 33% off  "` (Alert red)
    - Total Order (1): `"$ 34.00"`
  - **Item Card 2: "Men’s Jacket"**
    - Thumbnail: `120 x 120 dp`
    - Title: `"Men’s Jacket"` (Montserrat SemiBold 14sp)
    - Variations: `"Green"`, `"Grey"`
    - Rating: `"4.7"`
    - Price: `"$ 45.00"` (was `"$ 67.00"`, `"upto 28% off  "`)
    - Total Order (1): `"$ 45.00"`
- **Bottom Total & Action:**
  - Total Order Price: `"$ 79.00"`
  - `"Continue"` button (Height `55dp`, Background `#F83758`, White text)

---

### Screen 14: Place Order Screen (Order Summary & Coupons)
- **Frame ID:** `#1:17606`, **Dimensions:** `393 x 852 dp`
- **Background:** `#FDFDFD`
- **Top Bar:** Back button, Title: `"Shopping Bag"`
- **Delivery Estimate Card:**
  - `"Delivery by 10 May 2XXX"`
- **Order Item Preview:**
  - Thumbnail + `"Women’s Casual Wear"`, `"Checked Single-Breasted Blazer"`
  - Size: `"42"`, Qty: `"1"`
- **Coupons Section:**
  - `"Apply Coupons"` field + `"Select"` button
- **Order Payment Details Breakdown:**
  - Header: `"Order Payment Details"`
  - Order Amounts: `"7,000.00"`
  - Convenience Fee: `"Know More"`
  - Delivery Fee: `"Free"`
  - Order Total: `"7,000.00"` (`"EMI Available"` + `"Details"` link)
- **Sticky Bottom Checkout Bar:**
  - Left: Total amount `"7,000.00"` + `"View Details"` link
  - Right: `"Proceed to Payment"` button (Height `48dp`, Background `#F83758`, Corner Radius `4dp`)

---

### Screen 15: Shipping Screen (Payment Selection)
- **Frame ID:** `#1:17703`, **Dimensions:** `375 x 852 dp`
- **Background:** `#FDFDFD`
- **Top Bar:** Back button, Title: `"Checkout"`
- **Summary Header:**
  - Order: `"7,000"`
  - Shipping: `"30"`
  - Total: `"7,030"`
- **Payment Method Selection List:**
  - Header: `"Payment"`
  - Option 1: **VISA** card (`"*********2109"`) with radio button
  - Option 2: **MasterCard** (`"*********2109"`)
  - Option 3: **Maestro** (`"*********2109"`)
  - Option 4: **PayPal** (`"*********2109"`)
  - Option 5: **Cash on Delivery**
- **Bottom CTA:**
  - `"Continue"` button (Height `55dp`, Background `#F83758`, Corner Radius `4dp`)

---

### Screen 16: Sucessfully Screen (Order Confirmation)
- **Frame ID:** `#1:17780`, **Dimensions:** `375 x 852 dp`
- **Background:** `#FFFFFF`
- **Content:**
  - Large Success Celebration Illustration / Animated Checkmark (`width 180dp, height 180dp`)
  - Headline: `"Payment done successfully."` (Montserrat Bold 22sp, `#232327`, centered)
  - Order Details summary (`Order: 7,000`, `Shipping: 30`, `Total: 7,030`)
  - Payment method used preview (`"*********2109"`)
  - Primary CTA Button:
    - `"Continue Shopping"` (Height `55dp`, Background `#F83758`, White text)
- **Flow:** Returns user to Home screen and clears the active cart.

---

## 4. Complete Asset & Icon Checklist

### 4.1 Vector Icons Required
- **Navigation:**
  - `ic_menu` (Hamburger drawer menu, `24x24dp`)
  - `ic_back` (Left arrow, `24x24dp`)
  - `ic_close` (Dismiss 'X', `20x20dp`)
  - `ic_arrow_right` (CTA next arrow, `16x16dp`)
- **Search & Filter:**
  - `ic_search` (Magnifying glass, `18x18dp`)
  - `ic_mic` (Microphone voice search, `18x18dp`)
  - `ic_sort` (Up/Down arrow sort, `16x16dp`)
  - `ic_filter` (Funnel filter icon, `16x16dp`)
- **Bottom Navigation:**
  - `ic_home` (`24x24dp`)
  - `ic_wishlist` (`24x24dp`)
  - `ic_cart` (`24x24dp`)
  - `ic_search_nav` (`24x24dp`)
  - `ic_settings` (`24x24dp`)
- **E-Commerce & Actions:**
  - `ic_star_filled` (Gold star rating, `12x12dp`, `#EDB310`)
  - `ic_star_half` / `ic_star_outline`
  - `ic_heart_outline` / `ic_heart_filled` (Wishlist toggle)
  - `ic_pin_location` (Delivery address pin, `18x18dp`)
  - `ic_edit` (Pencil edit icon, `18x18dp`)
  - `ic_trash` (Delete cart item, `18x18dp`)
  - `ic_plus` & `ic_minus` (Quantity stepper, `16x16dp`)
  - `ic_clock` (Deal of the day countdown, `16x16dp`)
  - `ic_success_check` (Large order confirmed checkmark)
- **Auth & Form:**
  - `ic_user` (Profile avatar / user icon, `20x20dp`)
  - `ic_lock` (Password lock, `20x20dp`)
  - `ic_eye` / `ic_eye_off` (Password visibility toggle, `20x20dp`)
  - `ic_mail` (Email field icon, `20x20dp`)
- **Payment Providers:**
  - `ic_visa`, `ic_mastercard`, `ic_maestro`, `ic_paypal`, `ic_cod`
- **Social Logins:**
  - `ic_google`, `ic_apple`, `ic_facebook` (`24x24dp`)

### 4.2 Image Slots / Graphic Assets
- **Logo:** Stylish Brand Logo (`ic_stylish_logo`)
- **Onboarding Illustrations:**
  - `img_onboarding_1` (Choose Products)
  - `img_onboarding_2` (Make Payment)
  - `img_onboarding_3` (Get Your Order)
- **Hero & Promotional Banners:**
  - `img_get_started_bg` (Fashion background model)
  - `img_banner_promo_main` ("50-40% OFF" banner)
  - `img_banner_flat_heels` ("Flat and Heels" banner)
  - `img_banner_sponsored` ("Sponsored" banner)
- **Categories:**
  - `img_cat_beauty`, `img_cat_fashion`, `img_cat_kids`, `img_cat_mens`, `img_cat_womens`
- **Product Photos:**
  - Mock catalog photos for Clothing, Footwear, Electronics, Cosmetics, Accessories
- **Profile:**
  - User avatar placeholder (`img_avatar_placeholder`)
- **Success:**
  - Order success celebration illustration (`img_order_success`)
