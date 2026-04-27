# Algoritma

## Pergerakan dan animasi

* Flag input menentukan arah.
* Pengecekan collision dilakukan sebelum pergerakan diterapkan.
* Animasi sprite berganti antar frame menggunakan counter.

## Deteksi collision (tile)

1. Hitung bounding box entitas dalam world space.
2. Konversi ke koordinat grid tile.
3. Periksa dua tile di sisi arah pergerakan.
4. Blokir pergerakan jika ada tile yang memiliki collision.

## Deteksi collision (objek dan entitas)

* Sesuaikan rectangle entitas dan target berdasarkan posisi world.
* Tambahkan offset sesuai kecepatan pada arah gerak.
* Gunakan intersection rectangle untuk mendeteksi overlap.
* Kembalikan posisi rectangle ke kondisi semula setelah pengecekan.

## Pertumbuhan dan panen farm

* Setiap tile yang ditanam menyimpan `remainingGrowDays`.
* Progres harian mengurangi `remainingGrowDays` hingga 0.
* Saat mencapai 0, tile siap untuk dipanen.

## Risiko dan bencana

* Risiko dihitung dari jumlah tanaman, jumlah sekat api, dan ukuran cluster.
* Peluang bencana meningkat seiring risiko dan dapat memicu kebakaran pada tile yang ditanam secara acak.
* Penyebaran api memeriksa tile yang bersebelahan dengan tile yang terbakar.

## Urutan rendering (kedalaman)

* Semua entitas dikumpulkan dalam sebuah list.
* List diurutkan berdasarkan worldY.
* Entitas digambar sesuai urutan untuk mensimulasikan kedalaman.
