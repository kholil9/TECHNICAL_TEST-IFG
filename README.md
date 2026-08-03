# Katalon Project - Test Code (REST API + Kafka Consumer)

Project ini berisi 2 skenario pengujian sesuai instruksi soal:
1. **REST API test** — Katalon berperan sebagai *producer* (mengirim request POST/PUT/DELETE) dan *consumer* (menerima & memvalidasi response GET).
2. **Kafka test** — Katalon bertindak sebagai *consumer* yang membaca pesan dari sebuah topic Kafka.

Karena file project Katalon (`.prj`, Object Repository, dsb) hanya valid bila dibuat langsung oleh Katalon Studio, ikuti langkah di bawah untuk membangun project-nya dari nol lalu **paste script yang sudah disiapkan** ke masing-masing test case. Ini jauh lebih aman daripada meng-import file `.prj` buatan tangan yang berisiko corrupt.

---

## 1. Buat Project Baru di Katalon Studio
1. Buka Katalon Studio → **File > New > Project** → beri nama, misal `KatalonProjectTest`.
2. Buat 2 folder di **Test Cases**: `API` dan `Kafka`.

## 2. Test Case REST API (Producer & Consumer)
1. Klik kanan folder `API` → **New > Test Case** → beri nama `TC_REST_API_CRUD`.
2. Buka tab **Script** (bukan Manual) pada test case tersebut.
3. Copy seluruh isi file `Scripts/API/TC_REST_API_CRUD/Script.groovy` dari paket ini, paste ke sana.
4. Jalankan test case — tidak perlu setup tambahan karena memakai API publik `jsonplaceholder.typicode.com`.

## 3. Test Case Kafka Consumer

Ada 2 opsi tergantung apakah kamu bisa menambahkan external library jar di Katalon atau tidak.

### OPSI 1 (butuh jar) — pakai Kafka Java client
#### a. Tambahkan library Kafka ke Katalon
1. Download jar berikut (via Maven Central) dan taruh di folder `Drivers` pada project Katalon:
   - `kafka-clients-3.6.1.jar`
   - `slf4j-api-2.0.9.jar`
2. Atau tambahkan lewat **Project > Settings > External Libraries**. Kalau ada tab "Search on Maven Central", cukup ketik `org.apache.kafka:kafka-clients:3.6.1` — Katalon download otomatis, tidak perlu cari jar manual.

#### b. Tambahkan Custom Keyword
1. Klik kanan **Keywords** → **New > Package**, beri nama `kafka`.
2. Buat file Groovy baru `KafkaHelper.groovy` di dalamnya.
3. Copy isi file `Keywords/kafka/KafkaHelper.groovy` dari paket ini.
4. Untuk test case-nya, pakai `Scripts/Kafka/TC_Kafka_Consumer/Script.groovy`.

### OPSI 2 (TANPA jar) — pakai command line (direkomendasikan kalau susah install jar)
Katalon menjalankan perintah `kafka-console-consumer` lewat `docker exec`, jadi tidak butuh library Java apapun. Hanya butuh Docker.

1. Klik kanan **Keywords** → **New > Package**, beri nama `kafka`.
2. Buat file Groovy baru `KafkaHelperCLI.groovy`, copy isi dari `Keywords/kafka/KafkaHelperCLI.groovy`.
3. Untuk test case-nya, pakai isi `Scripts/Kafka/TC_Kafka_Consumer/Script_CLI_Alternative.groovy`.
4. Setelah `docker compose up -d`, cek nama container yang benar dengan `docker ps`, lalu sesuaikan variabel `containerName` di script kalau berbeda dari `kafka-local-kafka-1`.

### c. Jalankan Kafka lokal (untuk keperluan testing/demo)
```bash
cd kafka-local
docker compose up -d
```

### d. Kirim (produce) pesan uji ke topic
```bash
docker exec -it kafka-local-kafka-1 kafka-console-producer \
  --broker-list localhost:9092 --topic test-topic
```
Ketik pesan bebas lalu Enter, misalnya:
```
Hello from producer
```

### e. Buat Test Case Kafka di Katalon
1. Klik kanan folder `Kafka` → **New > Test Case** → beri nama `TC_Kafka_Consumer`.
2. Buka tab **Script**, copy isi file `Scripts/Kafka/TC_Kafka_Consumer/Script.groovy`.
3. Jalankan test case — Katalon akan membaca pesan yang sudah dikirim di langkah (d).

> Tips: jalankan langkah (d) terlebih dahulu sebelum menjalankan test case, karena test case akan menunggu pesan selama 15 detik lalu gagal jika tidak ada pesan masuk.

## 4. Buat Test Suite (opsional tapi disarankan)
1. Klik kanan **Test Suites** → **New > Test Suite** → beri nama `TS_AllTests`.
2. Drag kedua test case (`TC_REST_API_CRUD`, `TC_Kafka_Consumer`) ke dalamnya.
3. Jalankan Test Suite untuk melihat hasil gabungan.

---

## 5. Push ke Repository Publik (GitHub/GitLab)
Dari root folder project Katalon (folder yang berisi file `.prj`):

```bash
git init
git add .
git commit -m "Katalon test project: REST API + Kafka consumer"
git branch -M main
git remote add origin https://github.com/<username>/<nama-repo>.git
git push -u origin main
```

Pastikan repository disetel **Public**, lalu kirimkan link repository tersebut pada kolom jawaban HackerRank.

### Rekomendasi `.gitignore` untuk project Katalon
```
Reports/
.git/
*.log
Drivers/*.jar
```
(Jangan ignore folder `Test Cases`, `Scripts`, `Keywords`, `Object Repository`, dan file `.prj` — itu inti dari project.)
