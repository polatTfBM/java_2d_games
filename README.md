# Görselli Banka Sistemi

Bu proje, Java Swing kullanarak geliştirilmiş basit bir banka yönetim arayüzü içerir. Uygulama ile yeni hesaplar açabilir, mevcut hesaplara para yatırabilir, hesaplardan para çekebilir ve hesaplar arasında havale gerçekleştirebilirsiniz.

## Özellikler
- Hesap listesi ve bakiye görüntüleme
- Yeni hesap oluşturma
- Para yatırma ve para çekme işlemleri
- Hesaplar arası havale
- İşlem durumunu gösteren bildirim alanı

## Derleme ve Çalıştırma
1. Proje klasöründe aşağıdaki komutu çalıştırarak kaynak dosyalarını derleyin:
   ```bash
   javac -d out $(find src -name "*.java")
   ```
2. Uygulamayı başlatmak için:
   ```bash
   java -cp out com.example.bank.ui.BankApp
   ```

Uygulama açıldığında listede hesaplar görünecektir. Yeni hesap eklemek için sağ paneldeki formu kullanın. Bir hesabı seçip işlem alanındaki tutarı girerek para yatırma, çekme veya havale işlemlerini gerçekleştirebilirsiniz. Havale için hedef hesap numarasını (örneğin `ACC-0002`) girmeniz yeterlidir.
