#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

/*
 * Bu program, 0'ların geçilebilir, 1'lerin ise duvar olduğu 2 boyutlu bir
 * labirentte başlangıç noktasından hedef noktasına giden en kısa yolu bulur.
 * En kısa yolu bulmak için Breadth-First Search (BFS) algoritması kullanılır.
 * BFS, ağırlıksız graf (veya ızgara) üzerinde en kısa yolu bulma garantisi
 * veren seviyeler halinde dolaşma yaklaşımına dayanır.
 */

/* Labirentin boyutlarını sabit olarak tanımlıyoruz. */
#define ROWS 5
#define COLS 6

/*
 * Her hücreyi temsil etmek için bir konum (row, col) çifti saklamamız gerekiyor.
 * Bu amaçla küçük bir struct kullanmak, hem okunabilirliği arttırır hem de
 * kuyruk veri yapısında temiz bir şekilde saklamamızı sağlar.
 */
typedef struct {
    int row;
    int col;
} Position;

/*
 * BFS algoritması bir kuyruk (queue) veri yapısı üzerinde çalışır.
 * Kuyruğu statik bir dizi ve baş/son indeksleri ile uyguluyoruz.
 */
typedef struct {
    Position data[ROWS * COLS];
    int front;
    int rear;
} Queue;

/* Kuyruğu başlatmak için yardımcı fonksiyon. */
void initQueue(Queue *q) {
    q->front = 0;
    q->rear = 0;
}

/* Kuyruğun boş olup olmadığını kontrol eder. */
bool isEmpty(const Queue *q) {
    return q->front == q->rear;
}

/* Kuyruğa yeni bir konum ekler. */
void enqueue(Queue *q, Position pos) {
    q->data[q->rear++] = pos;
}

/* Kuyruktan en öndeki konumu çıkarır. */
Position dequeue(Queue *q) {
    return q->data[q->front++];
}

/*
 * BFS sırasında dört yönlü hareket (yukarı, aşağı, sol, sağ) kullanacağız.
 * Bu diziler, ilgili komşu hücrelere ulaşmak için satır/sütun değişimlerini
 * saklar. Indexler eşleşecek şekilde kullanılır.
 */
const int dRow[4] = {-1, 1, 0, 0};
const int dCol[4] = {0, 0, -1, 1};

int main(void) {
    /*
     * Labirenti sabit bir 2D dizi olarak tanımlıyoruz.
     * 0 = geçilebilir hücre, 1 = duvar.
     */
    int maze[ROWS][COLS] = {
        {0, 1, 0, 0, 0, 0},
        {0, 1, 0, 1, 1, 0},
        {0, 0, 0, 1, 0, 0},
        {1, 1, 0, 0, 0, 1},
        {0, 0, 0, 1, 0, 0}
    };

    /*
     * Başlangıç ve hedef konumları. Bu örnekte (0,0) başlangıç,
     * (ROWS-1, COLS-1) hedef olarak alınmıştır.
     */
    Position start = {0, 0};
    Position goal = {ROWS - 1, COLS - 1};

    /*
     * BFS için ziyaret durumu tutan 2D bool dizi.
     * Bir hücre ziyaret edildiğinde yeniden kuyruğa eklenmesini önlemek için kullanılır.
     */
    bool visited[ROWS][COLS] = {false};

    /*
     * En kısa yolu geri takip etmek için her hücre için önceki hücreyi saklayacağız.
     * prev[row][col] = o hücreye gelmeden önceki hücre.
     */
    Position prev[ROWS][COLS];

    /* Kuyruğu başlatıyoruz ve başlangıç hücresini ekliyoruz. */
    Queue queue;
    initQueue(&queue);
    enqueue(&queue, start);
    visited[start.row][start.col] = true;

    /* BFS döngüsünün hedefi bulup bulmadığını takip eden bayrak. */
    bool foundGoal = false;

    /*
     * BFS'in ana döngüsü:
     * 1. Kuyruktan bir hücre çıkar.
     * 2. Eğer bu hücre hedefse aramayı bitir.
     * 3. Değilse komşu hücreleri dolaş.
     */
    while (!isEmpty(&queue)) {
        Position current = dequeue(&queue);

        if (current.row == goal.row && current.col == goal.col) {
            foundGoal = true;
            break;  /* Hedefe ulaşıldı, arama tamamlanabilir. */
        }

        /*
         * Dört komşu hücreyi kontrol et. Her biri için:
         * - Sınırlar içinde mi?
         * - Duvar değil mi?
         * - Daha önce ziyaret edildi mi?
         * Şartlar sağlanıyorsa kuyruğa ekle ve ziyaret edildi olarak işaretle.
         */
        for (int i = 0; i < 4; ++i) {
            int newRow = current.row + dRow[i];
            int newCol = current.col + dCol[i];

            /* Sınır kontrolü */
            if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS) {
                continue;
            }

            /* Duvar kontrolü */
            if (maze[newRow][newCol] == 1) {
                continue;
            }

            /* Ziyaret kontrolü */
            if (visited[newRow][newCol]) {
                continue;
            }

            visited[newRow][newCol] = true;
            prev[newRow][newCol] = current;  /* Yolu geri takip edebilmek için kaydet. */
            Position neighbor = {newRow, newCol};
            enqueue(&queue, neighbor);
        }
    }

    if (!foundGoal) {
        printf("Hedefe giden bir yol bulunamadı.\n");
        return 0;
    }

    /*
     * Hedef bulunduysa, prev dizisini kullanarak yolu sondan başa doğru toparlarız.
     * Maksimum yol uzunluğu ROWS*COLS olabileceğinden bu boyutta bir dizi kullanıyoruz.
     */
    Position path[ROWS * COLS];
    int length = 0;

    Position current = goal;
    while (!(current.row == start.row && current.col == start.col)) {
        path[length++] = current;
        current = prev[current.row][current.col];
    }
    path[length++] = start;  /* Başlangıç hücresini de ekliyoruz. */

    /* Yolu ters çevrilmiş halde topladığımız için geriden başlayarak yazdırıyoruz. */
    printf("En k\u0131sa yol uzunlu\u011fu: %d ad\u0131m\n", length - 1);
    printf("Yol: ");
    for (int i = length - 1; i >= 0; --i) {
        printf("(%d,%d)", path[i].row, path[i].col);
        if (i != 0) {
            printf(" -> ");
        }
    }
    printf("\n");

    return 0;
}

/*
Algoritma Analizi:

- Zaman karma\u015f\u0131kl\u0131\u011f\u0131: BFS, her hücreyi en fazla bir kez kuyru\u011fa
  ekledi\u011fi ve her kenar\u0131 (kom\u015fuluk ili\u015fkisi) en fazla bir kez kontrol etti\u011fi
  için O(V + E) zaman karma\u015f\u0131kl\u0131\u011f\u0131na sahiptir. Burada V, labirentteki
  hücre say\u0131s\u0131; E ise geçi\u015f yap\u0131labilecek kom\u015fuluklar\u0131n say\u0131s\u0131d\u0131r.
- Uzay karma\u015f\u0131kl\u0131\u011f\u0131: Ziyaret edilen hücreleri, prev dizisini ve kuyru\u011fu
  tutmak için O(V) ek alan kullan\u0131r.

Avantajlar:
- A\u011f\u0131rl\u0131ks\u0131z graflarda ve ızgaralarda en kısa yolu garanti eder.
- Uygulamas\u0131 nispeten basittir ve sadece FIFO kuyru\u011fu gerektirir.

Dezavantajlar:
- Çok büyük labirentlerde haf\u0131za kullan\u0131m\u0131 artabilir, çünkü tüm seviyeleri
  aynı anda bellekte tutar.
- A\u011f\u0131rl\u0131kl\u0131 (kenar maliyetli) graflarda en k\u0131sa yolu bulmak için uygun de\u011fildir.
*/
