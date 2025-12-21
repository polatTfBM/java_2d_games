package model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private String description;
    private int totalCopies;
    private int availableCopies;
    private byte[] bookImage;

    public Book() {
    }

    public Book(int id, String title, String author, String category, String description, int totalCopies, int availableCopies, byte[] bookImage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.bookImage = bookImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public byte[] getBookImage() {
        return bookImage;
    }

    public void setBookImage(byte[] bookImage) {
        this.bookImage = bookImage;
    }
}
