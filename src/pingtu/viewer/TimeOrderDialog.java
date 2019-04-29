package pingtu.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import pingtu.dao.HandleDB;
import pingtu.entity.TimeOrder;

public class TimeOrderDialog extends JDialog {
	

	private final JPanel contentPanel = new JPanel();
	/**
	 * Launch the application.
	 */

	public TimeOrderDialog(MainFrame parent,boolean isShow) {
		super(parent,isShow);
		this.setTitle("���а�");
		this.setResizable(false);
		setBounds(100, 100, 360, 300);
		this.setLocationRelativeTo(parent);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("\u6392\u884C\u699C");
		label.setForeground(Color.RED);
		label.setFont(new Font("����", Font.BOLD, 18));
		label.setBounds(140, 10, 78, 46);
		contentPanel.add(label);
		
		JTextArea taResult = new JTextArea();
		taResult.setBackground(Color.LIGHT_GRAY);
		taResult.setEditable(false);
		taResult.setBounds(31, 59, 280, 145);
		contentPanel.add(taResult);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);

			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
			
			HandleDB hdb = new HandleDB();
			ArrayList<TimeOrder> al = hdb.selectInfo();
			if(al.size()==0) {
				taResult.setText("��ʱû����Ϸ��¼");
			}else {
				taResult.append("����\t����\t����ʱ��\r\n\r\n");
				for(int i=0;i<al.size();i++) {
					TimeOrder to = al.get(i);
					taResult.append("��"+(i+1)+"��\t"+to.getName()+"\t"+to.getTime()+"\r\n");
				}
			}

			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			this.setVisible(true);
		}
	}
}
