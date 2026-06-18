import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDAO {

    // 1. 新增遊戲功能
    public boolean insertGame(Game game) {
        String sql = "INSERT INTO Games (title, developer, price, release_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getDeveloper());
            ps.setInt(3, game.getPrice());
            ps.setDate(4, new java.sql.Date(game.getReleaseDate().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. 修改遊戲價格與名稱功能
    public boolean updateGame(Game game) {
        String sql = "UPDATE Games SET title = ?, developer = ?, price = ? WHERE game_id = ?";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getDeveloper());
            ps.setInt(3, game.getPrice());
            ps.setInt(4, game.getGameId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. 刪除遊戲功能
    public boolean deleteGame(int gameId) {
        String sql = "DELETE FROM Games WHERE game_id = ?";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 關鍵字搜尋遊戲
    public List<Game> searchGamesByKey(String keyword) {
        List<Game> list = new ArrayList<>();
        String sql = "SELECT * FROM Games WHERE title LIKE ? OR developer LIKE ?";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Game(rs.getInt("game_id"), rs.getString("title"),
                            rs.getString("developer"), rs.getInt("price"), rs.getDate("release_date")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    //依據標籤篩選
    public List<Game> getGamesByTag(int tagId) {
        List<Game> list = new ArrayList<>();
        String sql = "SELECT g.* FROM Games g " +
                "JOIN Game_Tags gt ON g.game_id = gt.game_id " +
                "WHERE gt.tag_id = ?";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tagId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Game(rs.getInt("game_id"), rs.getString("title"),
                            rs.getString("developer"), rs.getInt("price"), rs.getDate("release_date")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    // 遊戲 ID 與標籤 ID 進行綁定
    public boolean bindTagToGame(int gameId, int tagId) {
        String sql = "INSERT INTO Game_Tags (game_id, tag_id) VALUES (?, ?)";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gameId);
            ps.setInt(2, tagId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //刪除指定 ID 的標籤
    public boolean deleteTag(int tagId) {
        String sql = "DELETE FROM Tags WHERE tag_id = ?";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tagId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 指定價格內遊戲
    public List<Game> getGamesByPriceRange(int maxPrice) {
        List<Game> list = new ArrayList<>();
        String sql = "SELECT * FROM Games WHERE price <= ? ORDER BY price ASC";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maxPrice);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Game(rs.getInt("game_id"), rs.getString("title"),
                            rs.getString("developer"), rs.getInt("price"), rs.getDate("release_date")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Tag> getAllTags() {
        List<Tag> list = new ArrayList<>();
        String sql = "SELECT * FROM Tags ORDER BY tag_id ASC";

        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Tag(rs.getInt("tag_id"), rs.getString("tag_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //熱門統計
    public List<Map<String, Object>> getTopRatedGames() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT g.game_id, g.title, AVG(r.rating) AS avg_rating, COUNT(r.review_id) AS total_reviews " +
                "FROM Games g JOIN Reviews r ON g.game_id = r.game_id " +
                "GROUP BY g.game_id, g.title ORDER BY avg_rating DESC";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("gameId", rs.getInt("game_id"));
                map.put("title", rs.getString("title"));
                map.put("avgRating", rs.getDouble("avg_rating"));
                map.put("totalReviews", rs.getInt("total_reviews"));
                list.add(map);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 冷門庫存(小於兩則評論)
    public List<Game> getColdGames() {
        List<Game> list = new ArrayList<>();
        String sql = "SELECT g.* FROM Games g " +
                "LEFT JOIN Reviews r ON g.game_id = r.game_id " +
                "GROUP BY g.game_id, g.title " +
                "HAVING COUNT(r.review_id) <= 2";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Game(rs.getInt("game_id"), rs.getString("title"),
                        rs.getString("developer"), rs.getInt("price"), rs.getDate("release_date")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    //各開發商遊戲統計
    public Map<String, Integer> getDeveloperStats() {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT developer, COUNT(game_id) AS game_count FROM Games GROUP BY developer";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getString("developer"), rs.getInt("game_count"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
    //首頁(發行日期排序)
    public List<Game> getAllGamesOrderByDate() {
        List<Game> list = new ArrayList<>();
        String sql = "SELECT * FROM Games ORDER BY release_date DESC";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Game(rs.getInt("game_id"), rs.getString("title"),
                        rs.getString("developer"), rs.getInt("price"), rs.getDate("release_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Game> getAllId() {//ID排序
        List<Game> list = new ArrayList<>();
        String sql = "SELECT * FROM Games ORDER BY game_id ASC";

        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Game game = new Game();
                game.setGameId(rs.getInt("game_id"));
                game.setTitle(rs.getString("title"));
                game.setDeveloper(rs.getString("developer"));
                game.setPrice(rs.getInt("price"));
                game.setReleaseDate(rs.getDate("release_date"));
                list.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    //新增標籤
    public boolean insertTag(Tag tag) {
        String sql = "INSERT INTO Tags (tag_name) VALUES (?)";
        try (Connection conn = DatabaseDemo.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tag.getTagName());
            return ps.executeUpdate() > 0; // 影響行數 > 0 代表新增標籤成功
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //新增遊戲|綁定標籤
    public boolean insertGameWithTags(Game game, List<Integer> tagIds) {
        String insertGameSql = "INSERT INTO Games (title, developer, price, release_date) VALUES (?, ?, ?, ?)";
        String insertTagSql = "INSERT INTO Game_Tags (game_id, tag_id) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseDemo.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psGame = conn.prepareStatement(insertGameSql, Statement.RETURN_GENERATED_KEYS)) {
                psGame.setString(1, game.getTitle());
                psGame.setString(2, game.getDeveloper());
                psGame.setInt(3, game.getPrice());
                psGame.setDate(4, new java.sql.Date(game.getReleaseDate().getTime()));
                psGame.executeUpdate();

                // 取得AUTO_INCREMENT生成的 game_id
                try (ResultSet generatedKeys = psGame.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newGameId = generatedKeys.getInt(1);
                        try (PreparedStatement psTag = conn.prepareStatement(insertTagSql)) {
                            for (int tagId : tagIds) {
                                psTag.setInt(1, newGameId);
                                psTag.setInt(2, tagId);
                                psTag.addBatch();
                            }
                            psTag.executeBatch();
                        }
                    }
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }
}