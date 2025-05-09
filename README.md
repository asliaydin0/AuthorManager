📚 Java Swing + JDBC Author Manager

Bu proje, Java Swing kütüphanesi ve JDBC teknolojisi kullanılarak geliştirilmiş basit bir masaüstü uygulamasıdır. Uygulamanın amacı bir **yazar (author) veritabanını** görsel olarak yönetebilmektir.

🚀 Özellikler

- `Authors` tablosundaki verileri bir `JTable` üzerinde listeleme
- Tekli yazar ekleme (Ad, Soyad girilerek)
- Batch (toplu) yazar ekleme: pencere kapatıldığında toplu kayıt işlemi yapılır
- `JTable` üzerinden seçilen satırı silme
- Swing bileşenleriyle kullanıcı dostu arayüz: `JTextField`, `JButton`, `JTable`, `JFrame` vb.
- Güvenli veritabanı bağlantısı (`try-with-resources` ile)

🧱 Kullanılan Teknolojiler

- Java 8+
- Java Swing
- JDBC
- MySQL Veritabanı
- Eclipse IDE (veya benzeri)

🗃️ Veritabanı Yapısı (MySQL)

```sql
CREATE TABLE Authors (
  AuthorID INT AUTO_INCREMENT PRIMARY KEY,
  FirstName VARCHAR(50),
  LastName VARCHAR(50)
);
