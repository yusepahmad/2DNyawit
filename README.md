# Sawit2D (2D Nyawit)

**Sawit2D** adalah sebuah game petualangan 2D berbasis Java yang dibangun menggunakan Java Swing dan AWT. Game ini mengusung tema perkebunan kelapa sawit ("Nyawit"), di mana pemain dapat menjelajahi dunia berbasis tile (kotak-kotak) dengan sudut pandang top-down.

## 🌟 Fitur Utama

- **Eksplorasi Dunia:** Peta dunia berukuran 50x50 tile dengan berbagai jenis permukaan seperti rumput, jalan, tanah, dan air.
- **Sistem Pergerakan:** Kontrol karakter yang responsif menggunakan keyboard (WASD).
- **Collision Detection:** Sistem deteksi tabrakan yang solid untuk mencegah pemain melewati objek padat seperti pohon, dinding, atau air.
- **Sistem Objek & Item:** Mendukung interaksi dengan objek seperti kapak (axe) dan item koleksi (koin perunggu).
- **Grafik Tile-Based:** Menggunakan sistem rendering tile yang efisien dengan dukungan untuk elemen interaktif (pohon kering, batang kayu, dll).

## 🛠️ Teknologi yang Digunakan

- **Bahasa Pemrograman:** Java
- **Library Grafis:** Java Swing & AWT (Native Java)
- **Build Tool:** Maven
- **Testing:** JUnit

## 🚀 Cara Menjalankan

### Prasyarat
- Java Development Kit (JDK) 8 atau versi yang lebih baru.
- Apache Maven terinstal di sistem Anda.

### Langkah-langkah
1. **Clone Repositori:**
   ```bash
   git clone https://github.com/username/sawit2d.git
   cd sawit2d
   ```

2. **Build Proyek:**
   Gunakan Maven untuk mengompilasi kode sumber:
   ```bash
   mvn clean compile
   ```

3. **Jalankan Game:**
   ```bash
   mvn exec:java -Dexec.mainClass="com.uph_lpjk.sawit2d.App"
   ```

## 📂 Struktur Folder Utama

- `src/main/java/.../controller`: Logika utama game, penanganan input, dan game loop (`GamePanel`, `KeyHandler`).
- `src/main/java/.../entity`: Definisi karakter dan entitas dalam game (`Player`, `Entity`).
- `src/main/java/.../tile`: Pengaturan sistem map dan rendering tile (`TileManager`).
- `src/main/java/.../object`: Definisi objek interaktif yang ada di dunia game.
- `src/main/resources`: Aset visual (PNG), file peta (TXT), dan font.

---

## 🤝 Panduan Kontribusi

Kami sangat terbuka bagi siapa saja yang ingin membantu mengembangkan **Sawit2D**! Berikut adalah arahan untuk berkontribusi:

### 1. Alur Kontribusi
1. **Fork** repositori ini.
2. Buat **Branch** baru untuk fitur atau perbaikan bug Anda: `git checkout -b fitur/nama-fitur`.
3. Lakukan **Commit** perubahan Anda dengan pesan yang jelas: `git commit -m 'Menambahkan fitur sistem inventory'`.
4. **Push** ke branch Anda: `git push origin fitur/nama-fitur`.
5. Buat **Pull Request** ke repositori utama.

### 2. Area Pengembangan yang Dibutuhkan
- **Grafik & Aset:** Penambahan sprite karakter baru, variasi pohon sawit, atau animasi lingkungan.
- **Mekanik Gameplay:** Implementasi sistem inventaris, mekanisme memanen sawit, atau sistem misi/quest.
- **Optimasi:** Meningkatkan efisiensi rendering pada peta yang lebih besar.
- **Audio:** Penambahan efek suara (langkah kaki, suara alam) dan musik latar.

### 3. Standar Kode
- Kode mengikuti standar **[Google Java Format](https://github.com/google/google-java-format)**.
- Jalankan perintah berikut sebelum melakukan commit untuk merapikan kode:
  ```bash
  mvn fmt:format
  ```
- Pastikan kode mengikuti konvensi penamaan Java (CamelCase).
- Tambahkan komentar pada metode atau logika yang kompleks.
- Perbarui unit test jika Anda melakukan perubahan pada logika inti.

---

## 👥 Tim Pengembangan

**Sawit2D** dikembangkan oleh tim kolaboratif dengan pembagian peran yang terstruktur untuk memastikan kualitas dan skalabilitas proyek.

### 🎯 Project Management

* **DMS (Dimas)** — *Project Manager*

  * Menentukan roadmap & milestone proyek
  * Mengatur timeline dan distribusi tugas
  * Koordinasi antar tim (dev, asset, design)
  * Review progres & quality control

---

### 💻 Game Development

#### 🧠 Core System & Architecture

* **eunza (Yusep)** — *Lead Game Developer*

  * Perancangan arsitektur game (MVC / modular structure)
  * Implementasi game loop & state management
  * Integrasi antar sistem (entity, tile, object)
  * Optimasi performa rendering & logic

#### 🎮 Gameplay & Mechanics

* **mffr (Falih)** — *Gameplay Developer*

   * Creating Base Project / Boilerplate
  * Implementasi sistem movement (WASD)
  * Collision detection & physics sederhana
  * Sistem interaksi (cut tree, collect item)
  * Future: inventory, crafting, quest system

---

### 🌍 World & Level Design

* **mffr (Falih)** — *Map Designer*
* **Sya (Sasha)** — *Level Designer*

  * Desain layout map (tile-based 50x50+)
  * Penempatan object (tree, water, path)
  * Balancing eksplorasi & navigasi player
  * Pembuatan file map (TXT / data-driven)

---

### 🎨 Art & Asset

#### 🧩 Visual Asset Creation

* **Nay (Hanaya)** — *Lead Artist*
* **Sya (Sasha)** — *2D Artist*

  * Pembuatan sprite karakter
  * Desain tile set
  * Object asset
  * UI element (icon, HUD - future)

#### ✨ Animation (Future Scope)

* Idle, walking, interaction animation

---

### 📖 Narrative & Game Design

* **Nay (Hanaya)** — *Game Designer / Story Writer*

  * Konsep dunia “Nyawit”
  * Storyline & lore sederhana
  * Desain quest & objective
  * Player progression & reward system

---
Dikembangkan dengan ❤️ oleh tim **UPH LPJK & Team 2**.