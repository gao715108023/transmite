package node.view;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;


import rpc.NodeServer;

public class MainFrame extends JFrame {

    private JButton bCollection = null;

    private JButton bServerStart = null;

    private int rows[] = null;

    private int cols[] = null;

    private Object data[][] = null;

    private String columnName[] = {"hostname", "updateTime"};

    private JTable mainTable = null;

    private int num = 0;

    private JPanel north;

    private JPanel south;

    private String hostName;

    private String updateTime;

    NodeServer nodeServer;

    public MainFrame() {

        north = new JPanel();
        south = new JPanel();
        bCollection = new JButton("收集静态信息");
        bServerStart = new JButton("服务节点开启");
        data = new Object[100][3];
        mainTable = new JTable(data, columnName);
        mainTable.setForeground(Color.BLUE);
        mainTable.setFillsViewportHeight(true);
        mainTable.setCellSelectionEnabled(true);
        JScrollPane scrollpane = new JScrollPane(mainTable);
        addMethodListener();
        south.add(scrollpane);
        north.add(bCollection);
        north.add(bServerStart);

        this.add(north, BorderLayout.NORTH);
        this.add(south, BorderLayout.SOUTH);

        this.setResizable(false);
        this.setSize(700, 700);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public int getNum() {
        return num;
    }


    public void setNum(int num) {
        this.num = num;
    }


    public int[] getRows() {
        return rows;
    }

    public int[] getCols() {
        return cols;
    }

    public Object[][] getData() {
        return data;
    }

    public String[] getColumnName() {
        return columnName;
    }

    public JTable getMainTable() {
        return mainTable;
    }

    public void setRows(int[] rows) {
        this.rows = rows;
    }

    public void setCols(int[] cols) {
        this.cols = cols;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public void setColumnName(String[] columnName) {
        this.columnName = columnName;
    }

    public void setMainTable(JTable mainTable) {
        this.mainTable = mainTable;
    }

    private void addMethodListener() {
        this.mainTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                rows = mainTable.getSelectedRows();
                cols = mainTable.getSelectedColumns();
            }


        });
        this.bCollection.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TableModel model = mainTable.getModel();
                if (rows != null && cols != null) {
                    hostName = (String) model.getValueAt(rows[0], 0);
                    updateTime = (String) model.getValueAt(rows[0], 1);


                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "请选择要更新的节点名");
                }


            }
        });
        this.bServerStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                startNodeServerThread.start();

            }
        });


    }

    private Thread startNodeServerThread =
            new Thread(new Runnable() {
                public void run() {
                    nodeServer.start();
                }
            }, "startNodeServer");

    public void addItem(String hostName, String update) {
        if (num <= 100) {
            TableModel model = mainTable.getModel();

            model.setValueAt(hostName, num, 0);
            model.setValueAt(update, num, 1);
            num++;
        }


    }

    public void moveItem(String hostName, int n) {

        TableModel model = mainTable.getModel();
        for (int i = 0; i <= num; i++) {
            if (i == n) {
                model.setValueAt("", num, 0);
                model.setValueAt("", num, 1);

            }

        }


    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
