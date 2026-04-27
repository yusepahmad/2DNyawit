# Game Loop

Loop diimplementasikan secara manual di `GamePanel` menggunakan thread khusus dan fixed time step. Setiap siklus memperbarui state game, melakukan render ke buffer off-screen, lalu menggambar ke layar.

Diagram ASCII:

```
Input -> Update -> Render (to temp buffer) -> Blit to screen
```

## Fase update

* Update pemain: pergerakan, pengecekan collision, status penebangan.
* Update sistem farm: aksi otomatis, progres hari dan jam, animasi api.
* Update tile interaktif: timer invincibility.
* Update partikel: memperbarui dan menghapus partikel yang sudah tidak aktif.

## Fase render

* Background tile map
* Overlay grid farm (pertumbuhan, api, hujan)
* Layer objek tile
* Tile interaktif dan entitas (diurutkan berdasarkan worldY)
* Overlay UI (status, banner, petunjuk)

## Integrasi dengan Swing

GamePanel menggunakan thread kustom dan menggambar ke dalam `BufferedImage` (`tempScreen`) untuk mengurangi flicker. `paintComponent()` menggunakan kembali buffer ini saat Swing melakukan repaint.

Pseudocode:

```
while (running) {
  accumulateDelta();
  if (delta >= 1) {
    update();
    drawToTempScreen();
    drawToScreen();
    delta--;
  }
}
```