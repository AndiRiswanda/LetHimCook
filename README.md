# 🍳 LetHimCook
by andiriswanda

**LetHimCook** adalah aplikasi resep dan memasak berbasis Android yang dirancang untuk membantu laki laki yang masih kesuliatan dalam belajar cara memasak dan ilmu ilmu dapur dasar untuk belajar dengan mudah dan simple di aplikasi ini

---

## ✨ Fitur

* 🔍 **Penemuan Resep**: Jelajahi ratusan resep yang mudah diikuti bahkan untuk bro pemula
* 📋 **Detail Resep**: Lihat bahan-bahan, langkah-langkah memasak, dan tonton video tutorial
* ❤️ **Favorit**: Simpan dan kelola resep favorit untuk akses cepat bahkan secara offline!!
* ⚠️ **Kesalahan Umum Memasak**: Pelajari kesalahan umum bro saat memasak dan cara menghindarinya
* 🧂 **Informasi Bahan**: Dapatkan penjelasan rinci dan kegunaan berbagai bahan masakan

---

## 🛠 implementasi teknis

### 🧱 Arsitektur

Aplikasi ini menggunakan pola desain **Model-View-Controller (MVC)**:

* **Model**: Kelas Java seperti `Meal`, `CookingMistake`, dan `Ingredient`
* **View**: Layout XML untuk aktivitas dan fragmen
* **Controller**: Aktivitas dan fragmen yang menangani interaksi pengguna dan logika

### 🔑 Komponen Utama

* **Integrasi API**: Menggunakan **Retrofit** untuk mengambil data resep dari [TheMealDB API](https://www.themealdb.com/)
* **Penyimpanan Lokal**: Menggunakan **SQLite** untuk menyimpan data seperti resep favorit dan kesalahan memasak
* **Desain UI**: Berdasarkan prinsip **Material Design** untuk tampilan yang modern dan konsisten
* **Pemrosesan Gambar**: Menggunakan **Glide** untuk memuat dan menyimpan gambar dengan efisien
* **Navigasi**: Menggunakan **Navigation Component** berbasis fragmen

---

## 🗄 Struktur Basis Data

Aplikasi menggunakan beberapa database SQLite:

* `FavoritesDbHelper`: Menyimpan resep favorit pengguna
* `MistakesDbHelper`: Menyimpan kesalahan memasak umum beserta solusinya
* ada juga ingredient db dan mistake db

---

## 📱 Cara Menggunakan

1. **Recepi list**: Lihat resep-resep acak yang ditampilkan di list
2. **Filter Berdasarkan Kategori**: Gunakan chip kategori untuk menjelajahi jenis hidangan tertentu
3. **Detail Resep**: Ketuk kartu resep untuk melihat bahan, petunjuk langkah demi langkah, dan video tutorial
4. **Simpan Favorit**: Ketuk ikon ❤️ untuk menyimpan resep ke daftar favorit
5. **Belajar dari Kesalahan**: Buka bagian "Kesalahan Umum/common mistakes" untuk meningkatkan keterampilan memasak

---

## 🖼 Tangkapan Layar

Berikut adalah beberapa tangkapan layar dari aplikasi:

| Beranda                              | list recep                               | Favorit                                |
| ------------------------------------ | ---------------------------------------- | -------------------------------------- |
| ![home](https://github.com/AndiRiswanda/LethimCook/blob/master/app/src/main/res/drawable/ScreenShot%20App/Screenshot_20250610_234817_LetHimCook.jpg) | ![list](https://github.com/AndiRiswanda/LethimCook/blob/master/app/src/main/res/drawable/ScreenShot%20App/Screenshot_20250610_234823_LetHimCook.jpg?raw=true) | ![favorite](https://github.com/AndiRiswanda/LethimCook/blob/master/app/src/main/res/drawable/ScreenShot%20App/Screenshot_20250610_234832_LetHimCook.jpg?raw=true) |



---
By Andi Riswanda | H071231008
