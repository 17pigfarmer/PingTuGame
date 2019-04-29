package pingtu.viewer;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import pingtu.dao.HandleDB;
import pingtu.handle.HandleImage;

public class MainFrame extends JFrame implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnMusic) {
			musicSwitch(e);
		} else if (e.getSource() == btnStart) {
			if (isRun) {
				int n = JOptionPane.showConfirmDialog(this, "您真的要重新开始游戏吗？");
				if (n == 0) {
					init();
					startThread();
					lblLeftTime.setText(leftTime+"");
				}
			}else {
				init();
				startThread();
				isRun = true;
				btnStart.setText("重新开始");
				lblLeftTime.setText(leftTime+"");
			}
		} else if (e.getSource() == btnShow) {
			JOptionPane.showMessageDialog(this, "傻");
		} else if (e.getSource() == btnOrder) {
			new TimeOrderDialog(this, true);
		} else {
			switchImg(e);
		}
	}

	private JPanel contentPane;
	JLabel lblShow = new JLabel("");
	JPanel ImgPanel = new JPanel();
	private int lv = 3;
	HandleDB hdb = new HandleDB();

	private Random rand = new Random();
	private int[] numArr = null;

	private int blankR;
	private int blankC;
	

	private JButton btnShow = new JButton("\u6E38\u620F\u8BF4\u660E");
	private JButton btnStart = new JButton("\u5F00\u59CB");
	private JButton btnMusic = new JButton("开启音乐");
	private JButton btnOrder = new JButton("\u663E\u793A\u6392\u884C\u699C");

	JLabel lblNewLabel = new JLabel(" \u5269\u4F59\u65F6\u95F4");
	JLabel lblLeftTime = new JLabel("1200");

	private String imgName = "img_1.jpg";

	JComboBox cbGrade = null;
	JComboBox cbImg = null;

	private boolean isRun = false;

	ImgButton[][] btnArr;
	
	private int leftTime = 1200;
	Timer timer = null;
	
	private AudioClip sound = null;
	private final JComboBox comboBox = new JComboBox(new Object[]{});

	/**
	 * Launch the application.
	 */
	
	public void musicSwitch(ActionEvent e) {
		JButton btn = (JButton)e.getSource();
		if("开启音乐".equals(btn.getText().trim())) {
			sound.loop();
			btn.setText("关闭音乐");
		}else {
			sound.stop();
			btn.setText("开启音乐");
		}
	}
	
	public void startThread() {
		if(timer!=null)
			timer.cancel();
		leftTime = 1200;
		timer = new Timer();
		timer.schedule(new MyTask(), 1000, 1000);
		
	}
	
	public class MyTask extends TimerTask{

		@Override
		public void run() {
			leftTime--;
			lblLeftTime.setText(leftTime+"");
			if(leftTime == 0) {
				lblLeftTime.setText("0");
				JOptionPane.showMessageDialog(null, "游戏时间到");
				this.cancel();
			}
			
		}
			
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public boolean checkOrder() {
		boolean flag = true;
		int n = 1;
		for (int row = 0; row < lv; row++) {
			for (int col = 0; col < lv; col++) {
				if (btnArr[row][col].getNum() != n) {
					flag = false;
					break;
				}
				n++;
			}
		}
		return flag;
	}

	public void switchImg(ActionEvent e) {
		ImgButton btn = (ImgButton) e.getSource();
		int row = btn.getRow();
		int col = btn.getCol();

		if (Math.abs(row - blankR) + Math.abs(col - blankC) == 1) {
			int temp = btnArr[row][col].getNum();
			btnArr[row][col].setNum(btnArr[blankR][blankC].getNum());
			btnArr[blankR][blankC].setNum(temp);
			btnArr[blankR][blankC].updateImage(true);
			btnArr[row][col].updateImage(false);
			blankR = row;
			blankC = col;

			if (checkOrder()) {
				JOptionPane.showMessageDialog(this, "恭喜你，已经排好了");

				int leftTime = Integer.parseInt(lblLeftTime.getText());
				int time = 1200 - leftTime;
				String name = JOptionPane.showInputDialog(this, "请输入姓名");

				if (name == null || "".equals(name.trim())) {
					name = "匿名";
				}
				hdb.insertInfo(name, time);
			}
		}
	}

	public void initNum() {
		numArr = new int[lv * lv];
		for (int i = 0; i < lv * lv; i++) {
			numArr[i] = i + 1;
		}
		for (int i = 0; i < lv * lv - 2; i++) {
			int n = rand.nextInt(lv * lv - i - 1) + i;
			int temp = numArr[i];
			numArr[i] = numArr[n];
			numArr[n] = temp;
		}
	}

	public void init() {
		initGrade();
		initImage();
		initNum();
		
		HandleImage ih = new HandleImage();
		ih.deleteAll();
		
		long l = System.currentTimeMillis();
		String preName = String.valueOf(l);
		ih.cuttingImage(500/lv, lv, lv, preName, imgName);
		
		ImgPanel.removeAll();
		ImgPanel.setLayout(new GridLayout(lv, lv));
		btnArr = new ImgButton[lv][lv];
		int k = 0;
		for (int i = 0; i < lv; i++) {
			for (int j = 0; j < lv; j++) {
				// public ImgButton(int row, int col, int num, String preName) {
				ImgButton btn = new ImgButton(i, j, numArr[k], preName);
				k++;
				ImgPanel.add(btn);
				btnArr[i][j] = btn;
				btnArr[i][j].addActionListener(this);
			}
		}
		blankC = 2;
		blankR = 2;
		btnArr[blankR][blankC].updateImage(false);
	}

	public void initImage() {

		int index = cbImg.getSelectedIndex();

		if (index == 0)
			index = 1;

		imgName = "img_" + index + ".jpg";

		URL url = this.getClass().getResource("/img/" + imgName);
		ImageIcon icon = new ImageIcon(url);
		icon.setImage(icon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));
		lblShow.setIcon(icon);
	}

	public void initGrade() {
		int n = cbGrade.getSelectedIndex();
		if (n == 0 || n == 1)
			lv = 3;
		else if (n == 2)
			lv = 4;
		else if (n == 3)
			lv = 5;
	}

	public MainFrame() {
		this.setTitle("拼图游戏");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 770, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		ImgPanel.setBackground(Color.GRAY);
		ImgPanel.setBounds(15, 15, 500, 500);
		contentPane.add(ImgPanel);

		lblShow.setVerticalAlignment(SwingConstants.BOTTOM);
		lblShow.setBounds(535, 20, 210, 220);
		lblShow.setBorder(new TitledBorder("样图"));
		contentPane.add(lblShow);

		btnMusic.setBounds(590, 383, 110, 23);
		contentPane.add(btnMusic);
		btnMusic.addActionListener(this);

		btnShow.setBounds(590, 416, 110, 23);
		contentPane.add(btnShow);
		btnShow.addActionListener(this);

		btnStart.addActionListener(this);
		btnStart.setBounds(590, 449, 110, 23);
		contentPane.add(btnStart);

		btnOrder.setBounds(590, 350, 110, 23);
		contentPane.add(btnOrder);
		btnOrder.addActionListener(this);

		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 12));
		lblNewLabel.setBounds(573, 248, 64, 30);
		contentPane.add(lblNewLabel);

		lblLeftTime.setForeground(Color.RED);
		lblLeftTime.setFont(new Font("宋体", Font.BOLD, 12));
		lblLeftTime.setBounds(678, 248, 54, 30);
		contentPane.add(lblLeftTime);

		String[] strimg = { "选择图片", "1", "2" };
		cbImg = new JComboBox(strimg);
		cbImg.setBounds(590, 288, 110, 21);
		contentPane.add(cbImg);

		String[] strGrade = { "选择难度", "3行3列", "4行4列", "5行5列" };
		comboBox.setBounds(0, 0, 110, 21);
		
		contentPane.add(comboBox);
		cbGrade = new JComboBox(strGrade);
		cbGrade.setBackground(Color.WHITE);
		cbGrade.setBounds(590, 319, 110, 21);
		contentPane.add(cbGrade);

		URL urlSound = this.getClass().getResource("/music/The Chinese zither.wav");
		sound = Applet.newAudioClip(urlSound);
		this.setVisible(true);
	}
}
