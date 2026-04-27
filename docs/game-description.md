# Deskripsi Game

Sawit2D adalah game top-down berbasis tile di mana pemain menjelajahi dunia dan mengelola kebun sawit. Area kebun berupa grid di mana tile dapat ditanami, dipanen, atau dilindungi dengan sekat api (firebreak).

## Mekanisme inti

* Pergerakan dan menebang: pemain bergerak dengan WASD dan dapat menebang menggunakan alat.
* Bertani: menanam sawit, menunggu pertumbuhan, memanen, dan menjual.
* Risiko dan bencana: risiko yang lebih tinggi dapat memicu kebakaran; pemain dapat memanggil tim pemadam kebakaran gajah.
* Ekonomi: emas digunakan untuk menanam, membuat sekat api, dan layanan; menjual hasil panen menghasilkan emas.

## Tujuan

* Menjaga kebun tetap produktif dan menghindari penyitaan lahan akibat tidak ada aktivitas dalam waktu lama.
* Mengelola risiko dan merespons kebakaran.
* Menjaga keseimbangan emas agar simulasi tetap berjalan.

## Interaksi pemain

Keyboard:

* `WASD` bergerak
* `E` atau `Enter` berinteraksi dengan tile kebun
* `F` menebang
* `B` memasang atau menghapus sekat api
* `H` tanam otomatis, `K` panen otomatis, `J` jual otomatis
* `N` lanjut ke hari berikutnya (hanya untuk development dan akan di hapus fitur nya ketika sudah production)
* `Q` jual inventaris
* `P` jeda

Mouse:

* Klik kiri: berinteraksi dengan tile kebun
* Klik kanan: mengaktifkan atau menonaktifkan sekat api pada tile kebun

## Kondisi game over

* Emas menjadi negatif (terjadi ketika pengeluaran melebihi jumlah emas).
* Lahan disita setelah 14 hari berturut-turut tanpa adanya aktivitas penanaman.
