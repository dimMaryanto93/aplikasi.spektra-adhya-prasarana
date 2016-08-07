# PT Spektra Adhya Prasaran

![spektra_adhya_prasarana](/imgs/spektra-adhya-prasaran-home.png)

Berikut ini adalah beberapa hal yang harus dikonfigurasi diantaranya

* Instal Java Runtime Environment (JRE)
* Instal database PostgreSQL
* Membuat role atau user schema pada DBMS postgresql
* Membuat database
* [Download binary aplikasi ```spektra-adhya-prasarana-x.x.jar```](https://github.com/elkahanna/spektra-adhya-prasarana/releases)
* Menjalankan aplikasi

## Install PostgreSQL

* [Download disini](https://www.postgresql.org/download/)
* Install sesuai dengan platform (Windows, Linux, Mac)
* Konfigurasi password root dan default port

## Membuat keneksi database

* user : spektra_adhya_prasarana
* password : admin
* database_name : spektra_adhya_prasarana
* port : 5432

### SQL Statement membuat user database

* Login sebagai ```postgres```

```bash
psql -h localhost -U postgres
```

* Kemudian buat user dan database sebagai berikut:

```sql
CREATE USER spektra_adhya_prasarana WITH SUPERUSER LOGIN PASSWORD 'admin';
CREATE DATABASE spektra_adhya_prasarana WITH OWNER spektra_adhya_prasarana;
```

### Membuat user & database PgAdmin3

* Login sebagai ```postgres```

![PgAdmin3 Login as postgres](/imgs/pgadmin3.png)

* Klik kanan pada menu-item ```Login Role``` -> ```New Login Role```

* Pada Role name isi dengan ```spektra_adhya_prasarana```

![PgAdmin3 Create user - name](/imgs/userdb-name.png)

* Kemudian pada tab ```Definition``` masukan Password & Password again ```admin```

![PgAdmin3 Create user - password](/imgs/userdb-password.png)

* Setelah itu klik OK. Maka hasilnya seperti berikut:

![PgAdmin3 usercreated](/imgs/usercreated.png)

* tahap selanjutnya adalah membuat database dengan nama yang sama seperti nama user yaitu ```spektra-adhya-prasarana```

* Klik Menu item ```Databases``` -> ```New Database``` kemudian masukan namenya ```spektra-adhya-prasarana``` dan ownernya ```spektra-adhya-prasarana```

![PgAdmin3 database create](/imgs/createdb.png)

* Kemudian klik Ok, maka hasilnya seperti berikut:

![PgAdmin3 database created with owner spektra-adhya-prasarana](/imgs/dbcreated.png)

## Membuat launcher aplikasi

* [Download jar archive](https://github.com/elkahanna/spektra-adhya-prasarana/releases)
* Pindahkan lokasi file jar (Bebas dimana saja) rekomendasi
  * Windows (```C:\Program Files\spektra-adhya-prasaran```
  * Linux (```/opt/spektra-adhya-prasarana/```)
* Membuat shortcut
  * Windows (Klik kanan pada file jar -> Sent -> Sent to Desktop)
  * Linux (lihat [cara membuat .desktop](http://dimmaryanto-blog.github.io/blog/linux/tips/desktop-entity-linux/))

## Menjalankan aplikasi

### Jalankan via command line

* Perintah dasar

```bash
java -jar lokasi-jar.jar
```

#### Perintah di Windows

contoh di Windows saya simpan di ```C:\Users\dimmaryanto\Downloads\spektra-adhya-prasarana-1.0.jar```

```bash
cd C:\Users\dimmaryanto\Downloads\

java -jar spektra-adhya-prasarana-1.0.jar
```

#### Perintah di Linux

contoh di Linux saya simpan di ```/home/dimmaryanto/Downloads/```

```bash
cd /home/dimmaryanto/Downloads/

java -jar spektra-adhya-prasarana-1.0.jar
```

### Jalankan via shortcut

* Setting supaya file ```.jar``` berjalan menggunakan ```Java Execution```, caranya klik kanan pada file ```.jar``` kemudian pilih aplikasi ```Java Execution``` / ```Java Runtime```

* Setelah itu baru Klik 2x pada icon di desktop atau di dashboard (Linux)
