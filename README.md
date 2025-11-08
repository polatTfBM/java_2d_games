# Basit Banka Sistemi

Bu proje, konsol üzerinden çalışan ve gerçekçi iş kurallarına sahip basit bir banka sistemi örneğidir. Uygulama, hesap açma, para yatırma/çekme, hesaplar arasında para transferi yapma ve hesap hareketlerini görüntüleme gibi temel işlemleri destekler.

## Gereksinimler
- Java 17 veya üzeri
- Maven 3.9+

## Çalıştırma
```bash
mvn clean package
java -cp target/banking-app-1.0-SNAPSHOT.jar com.example.bank.BankApplication
```

Uygulama başlangıçta iki örnek müşteri ve bazı işlem geçmişleri ile başlar. Menüdeki ilgili seçenekleri kullanarak yeni hesaplar oluşturabilir veya mevcut hesaplar üzerinde işlem yapabilirsiniz.

## Testler
```bash
mvn test
```
