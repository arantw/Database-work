import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameStoreGUI extends JFrame {
    private GameDAO gameDAO = new GameDAO();
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> tagComboBox;

    public GameStoreGUI() {
        setTitle("遊戲商品管理系統");
        setSize(1150, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();

        renderGameTable(gameDAO.getAllGamesOrderByDate());
        refreshTagComboBox();
    }

    //下拉選單
    private void refreshTagComboBox() {
        tagComboBox.removeAllItems();
        tagComboBox.addItem("請選擇遊戲標籤...");
        try {
            List<Tag> allTags = gameDAO.getAllTags();
            for (Tag t : allTags) {
                tagComboBox.addItem(t.getTagId() + "-" + t.getTagName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("查詢功能區"));

        searchField = new JTextField(10);
        JButton btnhome = new JButton("首頁");
        JButton btnSearch = new JButton("關鍵字搜尋");
        tagComboBox = new JComboBox<>();
        JButton btnTagFilter = new JButton("標籤篩選");
        JButton btnPriceFilter = new JButton("500元以內");
        JButton btnTopRated = new JButton("熱門好評");
        JButton btnColdGames = new JButton("冷門庫存");
        JButton sort =new JButton("ID排序");

        topPanel.add(btnhome);
        topPanel.add(new JLabel("關鍵字:"));
        topPanel.add(searchField);
        topPanel.add(btnSearch);
        topPanel.add(new JLabel(" 標籤:"));
        topPanel.add(tagComboBox);
        topPanel.add(btnTagFilter);
        topPanel.add(btnPriceFilter);
        topPanel.add(btnTopRated);
        topPanel.add(btnColdGames);
        topPanel.add(sort);

        JPanel leftPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        leftPanel.setBorder(BorderFactory.createTitledBorder("資料管理區"));


        JButton btnAdd = new JButton("進貨新遊戲");
        JButton btnUpdate = new JButton("修改遊戲資料");
        JButton btnBindTag = new JButton("遊戲貼新標籤");
        JButton btnDelete = new JButton("下架刪除遊戲");
        JButton btnAddTag = new JButton("新增遊戲標籤");
        JButton btnDeleteTag = new JButton("刪除遊戲標籤");

        Dimension btnSize = new Dimension(140, 40);
        btnAdd.setPreferredSize(btnSize);
        btnUpdate.setPreferredSize(btnSize);
        btnBindTag.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        btnAddTag.setPreferredSize(btnSize);
        btnDeleteTag.setPreferredSize(btnSize);


        leftPanel.add(btnAdd);
        leftPanel.add(btnUpdate);
        leftPanel.add(btnBindTag);
        leftPanel.add(btnDelete);
        leftPanel.add(btnAddTag);
        leftPanel.add(btnDeleteTag);


        tableModel = new DefaultTableModel(new String[]{"遊戲ID", "遊戲名稱", "開發廠商", "販售價格", "發行日期"}, 0);
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        // 排版
        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        //按鈕點擊
        btnSearch.addActionListener(this::executeSearch);
        btnTagFilter.addActionListener(this::executeTagFilter);
        btnPriceFilter.addActionListener(e -> renderGameTable(gameDAO.getGamesByPriceRange(500)));
        btnColdGames.addActionListener(e -> renderGameTable(gameDAO.getColdGames()));
        btnTopRated.addActionListener(this::executeTopRatedAnalysis);

        btnhome.addActionListener(e -> renderGameTable(gameDAO.getAllGamesOrderByDate()));
        btnAdd.addActionListener(this::executeAddGame);
        btnUpdate.addActionListener(this::executeUpdateGame);
        btnDelete.addActionListener(this::executeDeleteGame);
        btnAddTag.addActionListener(this::executeAddTag);
        btnDeleteTag.addActionListener(this::executeDeleteTag);
        btnBindTag.addActionListener(this::executeBindTagToGame);
        sort.addActionListener(e -> renderGameTable (gameDAO.getAllId()));
    }

    private void renderGameTable(List<Game> games) {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"遊戲ID", "遊戲名稱", "開發廠商", "販售價格", "發行日期"});
        for (Game g : games) {
            tableModel.addRow(new Object[]{g.getGameId(), g.getTitle(), g.getDeveloper(), g.getPrice(), g.getReleaseDate()});
        }
    }

    //關鍵字搜尋
    private void executeSearch(ActionEvent e) {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請輸入搜尋關鍵字！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        renderGameTable(gameDAO.searchGamesByKey(keyword));
    }

    //標籤篩選
    private void executeTagFilter(ActionEvent e) {
        int selectedIndex = tagComboBox.getSelectedIndex();
        if (selectedIndex == 0) {
            JOptionPane.showMessageDialog(this, "請選擇一個正確的標籤！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int tagId = Integer.parseInt(tagComboBox.getSelectedItem().toString().split("-")[0]);
        renderGameTable(gameDAO.getGamesByTag(tagId));
    }
    private void executeBindTagToGame(java.awt.event.ActionEvent e) {
        try {
            //詢問遊戲
            String gameIdStr = JOptionPane.showInputDialog(this, "請輸入【既有遊戲】的 ID (例如: 1):");
            if (gameIdStr == null || gameIdStr.trim().isEmpty()) return;
            int gameId = Integer.parseInt(gameIdStr.trim());

            //詢問標籤
            String tagIdStr = JOptionPane.showInputDialog(this, "請輸入要綁定的【遊戲標籤】ID (例如: 103):");
            if (tagIdStr == null || tagIdStr.trim().isEmpty()) return;
            int tagId = Integer.parseInt(tagIdStr.trim());

            //呼叫Game_Tags
            if (gameDAO.bindTagToGame(gameId, tagId)) {
                JOptionPane.showMessageDialog(this, "成功幫遊戲 ID " + gameId + " 貼上標籤 ID " + tagId + "！");

                renderGameTable(gameDAO.getAllGamesOrderByDate());
            } else {
                JOptionPane.showMessageDialog(this, "綁定失敗！請確認該遊戲 ID 與標籤 ID 是否都存在於資料庫中，或該遊戲是否早已貼過此標籤。", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "輸入格式錯誤！ID 必須為數字。", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    //熱門統計
    private void executeTopRatedAnalysis(ActionEvent e) {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"排名", "遊戲名稱", "平均玩家評分", "總評論筆數"});
        List<Map<String, Object>> data = gameDAO.getTopRatedGames();
        int rank = 1;
        for (Map<String, Object> map : data) {
            tableModel.addRow(new Object[]{
                    rank++,
                    map.get("title"),
                    String.format("%.1f ", (Double) map.get("avgRating")),
                    map.get("totalReviews") + " 則"
            });
        }
    }

    //新增遊戲
    private void executeAddGame(ActionEvent e) {
        try {
            String title = JOptionPane.showInputDialog(this, "【進貨】請輸入遊戲名稱:");
            if (title == null || title.trim().isEmpty()) return;
            String developer = JOptionPane.showInputDialog(this, "【進貨】請輸入開發商:");
            String priceStr = JOptionPane.showInputDialog(this, "【進貨】請輸入價格:");
            int price = Integer.parseInt(priceStr);

            String dateStr = JOptionPane.showInputDialog(this, "請輸入發行日期 (格式: YYYY-MM-DD):");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date releaseDate = sdf.parse(dateStr.trim());

            String tagsStr = JOptionPane.showInputDialog(this, "請輸入要綁定的標籤 ID (多個請用逗號隔開):");
            List<Integer> tagIds = new ArrayList<>();
            if (tagsStr != null && !tagsStr.trim().isEmpty()) {
                String[] splitTags = tagsStr.split(",");
                for (String tag : splitTags) { tagIds.add(Integer.parseInt(tag.trim())); }
            }

            Game newGame = new Game();
            newGame.setTitle(title);
            newGame.setDeveloper(developer);
            newGame.setPrice(price);
            newGame.setReleaseDate(releaseDate);

            if (gameDAO.insertGameWithTags(newGame, tagIds)) {
                JOptionPane.showMessageDialog(this, "遊戲進貨成功，並已成功綁定 " + tagIds.size() + " 個標籤！");
                renderGameTable(gameDAO.getAllGamesOrderByDate()); // 即時刷新大表格！
            } else {
                JOptionPane.showMessageDialog(this, "進貨失敗，請確認標籤 ID 是否存在。", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        } catch (java.text.ParseException pEx) {
            JOptionPane.showMessageDialog(this, "日期格式錯誤！請務必輸入 YYYY-MM-DD", "錯誤", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "輸入格式錯誤！", "警示", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 執行修改遊戲
    private void executeUpdateGame(ActionEvent e) {
        try {
            String idStr = JOptionPane.showInputDialog(this, "請輸入要修改的遊戲 ID:");
            if (idStr == null) return;
            int gameId = Integer.parseInt(idStr);

            String newTitle = JOptionPane.showInputDialog(this, "請輸入新的遊戲名稱:");
            String newDeveloper = JOptionPane.showInputDialog(this, "請輸入新的開發商:");
            String newPriceStr = JOptionPane.showInputDialog(this, "請輸入新的價格:");
            int newPrice = Integer.parseInt(newPriceStr);

            Game updatedGame = new Game();
            updatedGame.setGameId(gameId);
            updatedGame.setTitle(newTitle);
            updatedGame.setDeveloper(newDeveloper);
            updatedGame.setPrice(newPrice);

            if (gameDAO.updateGame(updatedGame)) {
                JOptionPane.showMessageDialog(this, "資料修改成功！");
                renderGameTable(gameDAO.getAllGamesOrderByDate()); // 即時刷新大表格！
            } else {
                JOptionPane.showMessageDialog(this, "修改失敗，找不到該遊戲 ID。", "提示", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "輸入格式錯誤！", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 執行下架刪除遊戲
    private void executeDeleteGame(ActionEvent e) {
        try {
            String idStr = JOptionPane.showInputDialog(this, "請輸入要下架刪除的遊戲 ID:");
            if (idStr == null) return;
            int gameId = Integer.parseInt(idStr);

            int confirm = JOptionPane.showConfirmDialog(this, "確定要刪除遊戲 ID: " + gameId + " 嗎？這將連同評論一起刪除！", "確認刪除", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (gameDAO.deleteGame(gameId)) {
                    JOptionPane.showMessageDialog(this, "遊戲已成功移出庫存！");
                    renderGameTable(gameDAO.getAllGamesOrderByDate());
                } else {
                    JOptionPane.showMessageDialog(this, "刪除失敗，找不到該遊戲 ID。", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "輸入格式錯誤！", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 執行新增標籤
    private void executeAddTag(ActionEvent e) {
        String tagName = JOptionPane.showInputDialog(this, "請輸入要新增的藝術遊戲標籤名稱 (例如: 像素風、肉鴿):");
        if (tagName == null || tagName.trim().isEmpty()) return;

        Tag newTag = new Tag();
        newTag.setTagName(tagName.trim());

        if (gameDAO.insertTag(newTag)) {
            JOptionPane.showMessageDialog(this, "「" + tagName + "」標籤新增成功！");
            refreshTagComboBox();
        } else {
            JOptionPane.showMessageDialog(this, "標籤新增失敗！可能名稱重複了。", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 執行刪除標籤 (含自動更新下拉選單)
    private void executeDeleteTag(ActionEvent e) {
        try {
            String idStr = JOptionPane.showInputDialog(this, "請輸入要刪除的遊戲標籤 ID:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            int tagId = Integer.parseInt(idStr.trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                    "確定要刪除標籤 ID: " + tagId + " 嗎？\n這將會解除所有遊戲與該標籤的綁定關係！",
                    "確認刪除標籤", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (gameDAO.deleteTag(tagId)) {
                    JOptionPane.showMessageDialog(this, "標籤 ID " + tagId + " 已成功刪除！");
                    refreshTagComboBox();
                } else {
                    JOptionPane.showMessageDialog(this, "刪除失敗，找不到該標籤 ID。", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "輸入格式錯誤！ID 必須為數字。", "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameStoreGUI().setVisible(true);
        });
    }
}