# Struktur Kode

## Package

* `controller`
* `entity`
* `farm`
* `tile`
* `interactive/tile`
* `object`
* `utility`

## Kelas inti

* `App`: titik masuk aplikasi dan setup jendela Swing.
* `GamePanel`: game loop, pipeline rendering, dan state machine.
* `KeyHandler`: penangkap input dan flag aksi.
* `CollisionChecker`: pengecekan collision untuk tile, objek, dan entitas.
* `TileManager` dan `Tile`: loading map dan rendering tile.
* `UserInterface`: HUD, banner, dan layar game over.
* `Player` dan `Entity`: pergerakan, logika serangan, dan atribut dasar entitas.
* `FarmSystem`, `FarmGrid`, `FarmTile`: simulasi dan rendering farm.
* `EconomySystem`, `RiskSystem`, `DisasterSystem`, `WeatherSystem`: subsistem farm.
* `FirefighterEventSystem`: dialog pilihan modal.
* `Sound`: pemutaran audio.

## Struktur resource

* `maps`: tile map berbasis teks
* `tile`, `tiles_interactive`: terrain dan sprite interaktif
* `objects`, `player`: sprite game
* `sounds`, `fonts`: audio dan font
* `sawit`, `fire`: pertumbuhan sawit dan animasi api
