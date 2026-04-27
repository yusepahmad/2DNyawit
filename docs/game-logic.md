# Logika Game

## State game

* TITLE: menu utama.
* PLAY: gameplay aktif.
* PAUSE: gameplay dihentikan sementara.
* GAME_OVER: menampilkan pesan dan kembali ke menu utama.

Transisi:

* TITLE -> PLAY: Enter
* PLAY -> PAUSE: P
* PAUSE -> PLAY: P
* GAME_OVER -> TITLE: Enter

## Aturan farm

* Menanam membutuhkan 30 gold per tile.
* Sekat api (firebreak) membutuhkan 20 gold per tile.
* Penanganan api manual membutuhkan 25 gold.
* Waktu tumbuh sawit adalah 3 hari.

## Siklus harian

* Jam bertambah selama update; hari berganti setelah jam 23.
* Transisi harian:

  * Pertumbuhan dan counter lahan tidak terpakai diperbarui.
  * Api menyebar jika tidak dikendalikan.
  * Risiko dievaluasi; hujan dapat mengurangi risiko dan memadamkan api.
  * Bencana dapat terjadi (berdasarkan risiko).

## Ekonomi dan inventaris

* Hasil panen ditambahkan ke inventaris.
* Penjualan mengubah inventaris menjadi gold (harga dasar 120, dengan pengaruh penalti hujan).
* Layanan tukang kebun dapat memanen atau menjual dengan biaya atau diskon tertentu.

## Aturan penyitaan lahan

* Jika tidak ada aktivitas menanam selama 14 hari berturut-turut, maka game over.

## Penanganan event

* KeyHandler mengatur flag input; GamePanel memprosesnya setiap update.
* Klik mouse digunakan untuk berinteraksi dengan tile farm atau sekat api.
* FirefighterEventSystem menampilkan dialog modal untuk memilih respons.