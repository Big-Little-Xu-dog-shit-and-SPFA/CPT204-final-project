package ataxx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class ScorePanel extends JPanel {

    private JLabel scoreLabel;

    ScorePanel() {
        setPreferredSize(new Dimension(200, 100));
        setBackground(Color.DARK_GRAY);

        //Score分数记得加接口
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));

        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(scoreLabel);
    }
}



class GUI implements View, CommandSource, Reporter {

    private JFrame frame;

    private JLabel titleLabel;
    private JButton beginButton;
    private JButton quitButton;
    private JLabel modeLabel;
    private JButton aiButton;
    private JButton multiplayerButton;
    private JPanel mainPanel;
    private JPanel boardPanel;
    private JButton[][] gridButtons;

    private JButton button1;
    private JButton button2;

    //private JLabel scoreLabel;
    private ScorePanel scorePanel;

    GUI(String ataxx) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                start();
            }
        });
    }

    private void createAndShowGUI(String ataxx) {
        frame.getContentPane().removeAll();
        frame = new JFrame(ataxx);
        mainPanel = new JPanel(new BorderLayout());
        boardPanel = new JPanel(new GridLayout(7, 7));
        gridButtons = new JButton[7][7];

        // 创建棋盘格子按钮
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                gridButtons[row][col] = new JButton();
                gridButtons[row][col].setPreferredSize(new Dimension(50, 50));
                boardPanel.add(gridButtons[row][col]);
            }
        }
        button1 = new JButton("Button 1");
        button2 = new JButton("Button 2");
        //scoreLabel = new JLabel("Score: 0");
        scorePanel = new ScorePanel();


        // 创建游戏控制面板
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(button1);
        controlPanel.add(button2);
        // 创建顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(scorePanel, BorderLayout.CENTER);

//        JButton newGameButton = new JButton("New Game");
//        JButton quitButton = new JButton("Quit");

        // 添加按钮点击事件监听器
//        newGameButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 处理开始新游戏的逻辑
//                // 在这里编写代码...
//            }
//        });
//
//        quitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // 处理退出游戏的逻辑
//                // 在这里编写代码...
//            }
//        });

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
        titleLabel = new JLabel("Ataxx Game");
        titleLabel.setBounds(90, 30, 400, 100);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));


        beginButton = new JButton("Begin");
        beginButton.setFont(new Font("Arial", Font.BOLD, 25));
        beginButton.setBounds(125, 250, 150, 70);
        quitButton = new JButton("Quit");
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
                System.exit(0);//直接终止程序
            }
        });


        // 添加组件到 JFrame
        frame.getContentPane().add(titleLabel);
        frame.getContentPane().add(beginButton);
        frame.getContentPane().add(quitButton);
        // 显示首页
        frame.setVisible(true);
    }
    private void showModePage() {
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

        // 添加按钮点击事件监听器 //跳转的颜色选择页面
        //！！！需要添加功能
        ///
        ////
        aiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showColorPage();
            }
        });
        multiplayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showColorPage();
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
        ///
        ////
        RedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAndShowGUI("ataxx");
            }
        });
        BlueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAndShowGUI("ataxx");
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
    }

    @Override
    public String getCommand(String prompt) {
        return JOptionPane.showInputDialog(frame, prompt);
    }

    @Override
    public void announceWinner(PieceState state) {
        JOptionPane.showMessageDialog(frame, "Winner: " + state);
    }

    @Override
    public void announceMove(Move move, PieceState player) {
        JOptionPane.showMessageDialog(frame, "Player " + player + " moved: " + move);
    }

    @Override
    public void message(String format, Object... args) {
        JOptionPane.showMessageDialog(frame, String.format(format, args));
    }

    @Override
    public void error(String format, Object... args) {
        JOptionPane.showMessageDialog(frame, "Error: " + String.format(format, args), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }

    public void pack() {
        frame.pack();
    }
}


