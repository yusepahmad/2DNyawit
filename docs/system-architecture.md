# Arsitektur Sistem

## Struktur tingkat tinggi

Arsitektur ini merupakan desain sederhana berbasis game loop yang berpusat pada `GamePanel`. Tidak sepenuhnya mengikuti MVC, tetapi memiliki pemisahan yang mirip:

* Model: sistem farm, state game, entitas
* View: kode rendering dan overlay UI
* Controller: penanganan input dan loop update

Diagram komponen ASCII:

```
[KeyHandler/Mouse] -> [GamePanel] -> [Player, FarmSystem] -> [TileManager, UI]
```

## Komponen utama

* GamePanel: loop utama, orkestrasi rendering, transisi state.
* TileManager: memuat map dan menggambar layer tile.
* Player/Entity: pergerakan, pertarungan, dan perilaku dasar entitas.
* FarmSystem: simulasi grid farm, risiko, bencana, dan pesan UI.
* UserInterface: HUD, banner, dan layar game over.
* Sound: memuat dan memutar efek suara serta musik.
* FirefighterEventSystem: alur dialog modal untuk keputusan pemadam kebakaran.

## Alur data

* Event input memperbarui flag di KeyHandler.
* GamePanel membaca flag dan memanggil Player serta FarmSystem.
* FarmSystem memperbarui GameState dan mengirim pesan ke UI.
* UI membaca GameState dan FarmSystem untuk menampilkan status dan banner.

## Catatan desain

* Rendering entitas diurutkan berdasarkan worldY untuk mensimulasikan kedalaman.
* FarmSystem menggunakan subsistem pembantu (risiko, ekonomi, cuaca, bencana) untuk logika yang lebih terfokus.
