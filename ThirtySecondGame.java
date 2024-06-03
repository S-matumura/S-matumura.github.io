# S-matumura.github.io
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ThirtySecondGame {
    // JFrameの初期設定
    private JFrame frame;
    private JLabel messageLabel;
    private long startTime;
    private boolean gameStarted;
    private List<Double> gameRecords;

    private static final String RECORDS_FILE = "game_records.txt";

    public ThirtySecondGame() {
        frame = new JFrame("30秒カウントゲーム");
        messageLabel = new JLabel("スペースキーを押してゲームを開始します。", SwingConstants.CENTER);

        // JFrameの終了動作とサイズ設定
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.add(messageLabel);
        frame.setVisible(true);

        gameStarted = false;
        gameRecords = new ArrayList<>();

        // 過去の記録を読み込む
        loadRecords();

        // キーボード入力リスナーを追加
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (!gameStarted) {
                        startGame(); // ゲーム開始処理
                    } else {
                        endGame(); // ゲーム終了処理
                    }
                }
            }
        });
    }

    // ゲーム開始メソッド
    private void startGame() {
        gameStarted = true;
        startTime = System.currentTimeMillis();
        messageLabel.setText("タイミングを測定中...");

        // 経過時間を更新するタイマーを設定する（最初の10秒のみ表示）
        Timer elapsedTimer = new Timer(1000, new ActionListener() {
            int elapsedSeconds = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                long currentTime = System.currentTimeMillis();
                double elapsedTime = (currentTime - startTime) / 1000.0;

                if (elapsedSeconds < 10) {
                    messageLabel.setText(String.format("経過時間: %.2f秒", elapsedTime));
                    elapsedSeconds++;
                } else {
                    ((Timer) e.getSource()).stop();
                    messageLabel.setText("タイミングを測定中...");
                }
            }
        });
        elapsedTimer.start();
    }

    // ゲーム終了メソッド
    private void endGame() {
        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;

        messageLabel.setText(String.format("経過時間: %.2f秒", elapsedTime));

        // 結果判定とメッセージ表示
        if (elapsedTime > 30.00 && elapsedTime < 30.59) {
            JOptionPane.showMessageDialog(frame, "おめでとう!ほぼ30秒です!");
        } else {
            JOptionPane.showMessageDialog(frame, "残念!30秒ではありません。");
        }

        // 記録を保存
        saveRecord(elapsedTime);

        gameStarted = false;
        messageLabel.setText("スペースキーを押してゲームを開始します。");
    }

    // 記録をファイルに保存するメソッド
    private void saveRecord(double elapsedTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECORDS_FILE, true))) {
            writer.write(String.valueOf(elapsedTime));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ファイルから記録を読み込むメソッド
    private void loadRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RECORDS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                gameRecords.add(Double.parseDouble(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 読み込んだ記録を表示
        if (!gameRecords.isEmpty()) {
            StringBuilder recordsText = new StringBuilder("<html>過去の記録:<br>");
            for (double record : gameRecords) {
                recordsText.append(String.format("%.2f秒<br>", record));
            }
            recordsText.append("</html>");
            messageLabel.setText(recordsText.toString());
        }
    }
}

import javax.swing.SwingUtilities;

//メインメソッド
public class Main extends Object {
    public static void main(String[]args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThirtySecondGame();
            }
        });
    }
    
}
