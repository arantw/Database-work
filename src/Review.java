public class Review {
    private int reviewId;
    private int gameId;
    private String playerName;
    private int rating;
    private String comment;

    public Review() {}

    public Review(int reviewId, int gameId, String playerName, int rating, String comment) {
        this.reviewId = reviewId;
        this.gameId = gameId;
        this.playerName = playerName;
        this.rating = rating;
        this.comment = comment;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getGameId() { return gameId; }
    public void setGameId(int gameId) { this.gameId = gameId; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}