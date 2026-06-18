import java.util.Date;

public class Game {
    private int gameId;
    private String title;
    private String developer;
    private int price;
    private Date releaseDate;

    public Game() {}

    public Game(int gameId, String title, String developer, int price, Date releaseDate) {
        this.gameId = gameId;
        this.title = title;
        this.developer = developer;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }
}