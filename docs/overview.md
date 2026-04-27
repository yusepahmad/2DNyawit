# Sawit2D Gameplay Teknis

## Ringkasan proyek

Sawit2D adalah game petualangan dan farming 2D top-down yang dibangun menggunakan Java Swing/AWT. Basis kode menggabungkan eksplorasi dunia berbasis tile dengan subsistem manajemen pertanian (menanam, memanen, bencana).

## Tujuan proyek

* Menyediakan game loop ringan berbasis tile dan pipeline rendering.
* Mensimulasikan pertumbuhan pertanian, risiko, dan kejadian bencana dalam grid yang terkelola.
* Mendukung interaksi keyboard dan mouse untuk pergerakan dan aksi pertanian.

## Teknologi yang digunakan

* Java (JDK 8+)
* Swing/AWT untuk rendering dan input
* Maven untuk build dan manajemen dependensi

## Titik masuk

Aplikasi dimulai dari `com.uph_lpjk.sawit2d.App` yang membuat `JFrame` Swing dan menghubungkan `GamePanel`.

Perintah untuk menjalankan:

```bash
mvn exec:java -Dexec.mainClass="com.uph_lpjk.sawit2d.App"
```

## Struktur folder utama

* `src/main/java/com/uph_lpjk/sawit2d`

  * `controller`: main loop, input, UI, audio, collision, penanganan event
  * `entity`: pemain dan logika dasar entitas
  * `farm`: grid pertanian, ekonomi, risiko, bencana, cuaca, dan layanan helper
  * `tile`: loading dan rendering tile map
  * `interactive/tile`: tile interaktif yang bisa dihancurkan (misalnya pohon kering)
  * `object`: objek dunia dan item
  * `utility`: loading aset dan utilitas gambar
* `src/main/resources`

  * `maps`: tile map (txt)
  * `tile`, `tiles_interactive`: tile dan sprite interaktif
  * `objects`, `player`: sprite game
  * `sounds`, `fonts`: audio dan font
  * `sawit`, `fire`: aset pertumbuhan sawit dan animasi api
* `docs`: dokumentasi developer (folder ini)
