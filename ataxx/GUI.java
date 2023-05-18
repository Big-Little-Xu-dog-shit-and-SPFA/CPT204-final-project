package ataxx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


class ScorePanel extends JPanel {
    private JLabel scoreLabel;

    ScorePanel(Board board) {
        // Score分数记得加接口
        // 收到！
        scoreLabel = new JLabel(board.getScore());
        genPanel();
    }

    ScorePanel(PieceState winner) {
        if (winner == PieceState.RED) {
            scoreLabel = new JLabel("red win!");
        } else {
            scoreLabel = new JLabel("blue win!");
        }
        genPanel();
    }

    private void genPanel() {
        setPreferredSize(new Dimension(200, 100));
        setBackground(Color.DARK_GRAY);

        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));

        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(scoreLabel);
    }
}



class GUI implements View, CommandSource, Reporter {
    private JFrame frame;
    private String moveCmd;
    private PieceState winner;
    private Board nowBoard;
    private boolean isBlocked;
    private boolean isBlocking;
    private final Color darkPurple = new Color(83, 26, 109);

    private ArrayList<String> commands;

    private static Map<String, JFrame> frameMap = new HashMap<>();

    GUI(String ataxx) {
        commands = new ArrayList<>();
        moveCmd = "";
        winner = PieceState.EMPTY;
        nowBoard = new Board();
        nowBoard.clear();
        isBlocked = false;
        isBlocking = false;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                start();
            }
        });
    }

    private JPanel genBoard() {
        JPanel boardPanel = new JPanel(new GridLayout(7, 7));

        // 创建棋盘格子按钮
        for (int row = 6; row >= 0; row--) {
            for (int col = 0; col < 7; col++) {
                char c = (char) (col + 'a');
                char r = (char) (row + '1');

                final String index = String.valueOf(c) + String.valueOf(r);
                JButton btn = new JButton();
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color btnColor = btn.getBackground();
                        if (isBlocking == false) {
                            if (moveCmd.equals("")) {
                                if ((btnColor == Color.red && nowBoard.nextMove() == PieceState.RED) ||
                                        (btnColor == Color.blue && nowBoard.nextMove() == PieceState.BLUE)) {

                                    moveCmd = index;
                                }
                            } else {
                                if (btnColor != Color.red &&
                                        btnColor != Color.blue &&
                                        btnColor != darkPurple) {

                                    if (Math.abs(moveCmd.charAt(0) - index.charAt(0)) <= 2 &&
                                            Math.abs(moveCmd.charAt(1) - index.charAt(1)) <= 2 &&
                                            (moveCmd.charAt(0) != index.charAt(0) || moveCmd.charAt(1) != index.charAt(1))) {
                                        commands.add(moveCmd + "-" + index);
                                        isBlocked = true;
                                    }
                                }
                                moveCmd = "";
                            }
                        } else {
                            if (btnColor != Color.red &&
                                    btnColor != Color.blue &&
                                    btnColor != darkPurple) {

                                commands.add("block " + index);
                            }
                        }
                    }
                });

                btn.setPreferredSize(new Dimension(50, 50));

                if (nowBoard.getContent(c, r) == PieceState.RED) {
                    btn.setBackground(Color.red);
                } else if (nowBoard.getContent(c, r) == PieceState.BLUE) {
                    btn.setBackground(Color.blue);
                } else if (nowBoard.getContent(c, r) == PieceState.BLOCKED) {
                    btn.setBackground(darkPurple);
                }
                boardPanel.add(btn);
            }
        }
        return boardPanel;
    }

    private void delDupWin(String ataxx) {
        if (frameMap.containsKey(ataxx)) {
            // 如果存在，则关闭之前的窗口
            JFrame oldFrame = frameMap.get(ataxx);
            oldFrame.dispose();
        }
    }

    private void createAndShowGUI(String ataxx) {
        if (frame == null) return;
        delDupWin(ataxx);

        ScorePanel scorePanel;
        frame = new JFrame(ataxx);
        JPanel mainPanel = new JPanel(new BorderLayout());
        frameMap.put(ataxx, frame);

        JPanel boardPanel = genBoard();
        if (winner == PieceState.EMPTY) {
            scorePanel = new ScorePanel(nowBoard);
        } else {
            scorePanel = new ScorePanel(winner);
        }

        // 创建游戏控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        // 创建顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(scorePanel, BorderLayout.CENTER);

        JButton newGameButton = new JButton("New Game");
        JButton quitButton = new JButton("Quit");
        JButton blockButton = new JButton("Add Block");
        JButton finBlkButton = new JButton("Finish Block");
        JButton passButton = new JButton("Pass");

        // 添加按钮点击事件监听器
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理开始新游戏的逻辑
                // 在这里编写代码...
                commands.add("new");
                isBlocked = false;
                winner = PieceState.EMPTY;
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 处理退出游戏的逻辑
                // 在这里编写代码...
                commands.add("quit");
            }
        });

        blockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isBlocked == false) {
                    isBlocking = true;
                    delDupWin(ataxx);
                    createAndShowGUI(ataxx);
                }
            }
        });

        finBlkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBlocking = false;
                isBlocked = true;
                createAndShowGUI("Ataxx");
            }
        });

        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nowBoard.couldMove(nowBoard.nextMove())) {
                    commands.add("-");
                }
            }
        });

        controlPanel.add(newGameButton);
        controlPanel.add(quitButton);
        controlPanel.add(passButton);
        if (isBlocking == false) controlPanel.add(blockButton);
        else controlPanel.add(finBlkButton);

        // 将棋盘面板添加到中央面板并设置间距
        JPanel boardContainer = new JPanel();
        boardContainer.setLayout(new BorderLayout());
        boardContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        boardContainer.add(boardPanel, BorderLayout.CENTER);

        // 创建左侧面板和右侧面板，并设置间距
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(50, 0));
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(50, 0));

        // 创建主面板
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(boardContainer, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // 将主面板添加到主窗口的内容面板中
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void start() {
        // 创建 JFrame
        frame = new JFrame("Ataxx Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 400;
        int height = 600;
        frame.setSize(width, height);
        frame.setLayout(null);

        // 创建首页组件
        JLabel titleLabel = new JLabel("Ataxx Game");
        titleLabel.setBounds(90, 30, 400, 100);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));

        JButton beginButton = new JButton("Begin");
        beginButton.setFont(new Font("Arial", Font.BOLD, 25));
        beginButton.setBounds(125, 250, 150, 70);
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 25));
        quitButton.setBounds(125, 370, 150, 70);

        // 添加按钮点击事件监听器  //点击Begin或者Quit按钮将实现跳转
        beginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showModePage();
            }
        });
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commands.add("quit");
            }
        });

        // 添加组件到 JFrame
        frame.getContentPane().add(titleLabel);
        frame.getContentPane().add(beginButton);
        frame.getContentPane().add(quitButton);
        // 显示首页
        setVisible(true);
    }
    private void showModePage() {
        JLabel modeLabel;
        JButton aiButton;
        JButton multiplayerButton;

        // 清除首页组件
        frame.getContentPane().removeAll();

        // 创建模式选择页面组件
        modeLabel = new JLabel("Please choose versus mode");
        modeLabel.setBounds(20, 10, 400, 100);
        modeLabel.setFont(new Font("Arial", Font.BOLD, 25));

        aiButton = new JButton("Challenge AI");
        aiButton.setBounds(100, 250, 200, 70);//位置
        aiButton.setFont(new Font("Arial", Font.BOLD, 20));//字体大小

        multiplayerButton = new JButton("Two-player battle");
        multiplayerButton.setBounds(100, 370, 200, 70);
        multiplayerButton.setFont(new Font("Arial", Font.BOLD, 20));

        // 添加按钮点击事件监听器 跳转到颜色选择页面
        //！！！需要添加功能
        aiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showColorPage();
            }
        });
        multiplayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commands.add("manual red");
                commands.add("manual blue");
                commands.add("new");
                frame.dispose();
            }
        });

        // 添加组件到 JFrame
        frame.getContentPane().add(modeLabel);
        frame.getContentPane().add(aiButton);
        frame.getContentPane().add(multiplayerButton);

        // 重新绘制 JFrame
        frame.revalidate();
        frame.repaint();
    }

    private void showColorPage() {
        // 清除组件
        frame.getContentPane().removeAll();

        // 创建颜色选择页面组件
        JLabel ColorLabel = new JLabel("Please choose your Color");
        ColorLabel.setBounds(20, 10, 400, 100);
        ColorLabel.setFont(new Font("Arial", Font.BOLD, 25));

        JButton RedButton = new JButton("RED");
        RedButton.setFont(new Font("Arial", Font.BOLD, 25));
        RedButton.setBounds(125, 250, 150, 70);
        JButton BlueButton = new JButton("BLUE");
        BlueButton.setFont(new Font("Arial", Font.BOLD, 25));
        BlueButton.setBounds(125, 370, 150, 70);

        // 添加按钮点击事件监听器
        //！！！需要添加功能
        RedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commands.add("manual red");
                commands.add("ai blue");
                commands.add("new");
                frame.dispose();
            }
        });
        BlueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                commands.add("manual blue");
                commands.add("ai red");
                commands.add("new");
                frame.dispose();
            }
        });

        // 添加组件到 JFrame
        frame.getContentPane().add(ColorLabel);
        frame.getContentPane().add(RedButton);
        frame.getContentPane().add(BlueButton);

        // 重新绘制 JFrame
        frame.revalidate();
        frame.repaint();
    }


    @Override
    public void update(Board board) {
        // 实现更新棋盘的逻辑
        // 在这里编写代码...
        nowBoard = board;
        createAndShowGUI("Ataxx");
    }

    @Override
    public String getCommand(String prompt) {
        if (commands.isEmpty()) return "board_on";
        return commands.remove(0);
    }

    @Override
    public void announceWinner(PieceState state) {
        winner = state;
        createAndShowGUI("Ataxx");
    }

    @Override
    public void announceMove(Move move, PieceState player) {
    }

    @Override
    public void message(String format, Object... args) {
    }

    @Override
    public void error(String format, Object... args) {
    }

    public void setVisible(boolean b) {
        if (frame == null) return;
        frame.setVisible(b);
    }

    public void pack() {
        if (frame == null) return;
        frame.pack();
    }
}


