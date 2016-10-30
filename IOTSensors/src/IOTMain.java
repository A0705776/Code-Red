import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class IOTMain extends JFrame implements Runnable{
	

	/**
	 * 
	 */
	private JPanel contentPane;
	private JTextField IPtextField;
	private JTextField porttextField;
	private JTextArea outputtextArea;
	private JToggleButton tglbtnLed;
	public TCPClient tcpclient;
	public boolean newInputFromTCP = false;
	public boolean isBusy = false;
	public boolean isStarted = false;
	
	
	public void beginTho()
	{
	
		try {
			
			this.setVisible(true);
			new Thread(this).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		IOTMain i = new IOTMain();
		i.beginTho();
		 
         
			
	}

	/**
	 * Create the frame.
	 */
	public IOTMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblEnterTheIp = new JLabel("Enter the IP Address");
		
		IPtextField = new JTextField();
		IPtextField.setText("172.25.4.37");
		IPtextField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		
		porttextField = new JTextField();
		porttextField.setText("23");
		porttextField.setColumns(10);
		
		JButton btnNewButton = new JButton("Enter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tcpclient = new TCPClient(IPtextField.getText(),Integer.parseInt(porttextField.getText()));
				try {
					tcpclient.setup();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				isStarted = true;
			}
		});
		
		outputtextArea = new JTextArea();
		outputtextArea.setLineWrap(true);
		
		tglbtnLed = new JToggleButton("LED OFF");
		tglbtnLed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = 0;
				if(tglbtnLed.isSelected()){
					tglbtnLed.setText("LED ON");
					tglbtnLed.setEnabled(false);
					isBusy = true;
					tcpclient.write("LED1");
					//tcpclient.waitForTCPToReceive();
					while(isBusy && i < 1000){i++;try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}
					tglbtnLed.setEnabled(true);

				}else{
					tglbtnLed.setText("LED OFF");
					tglbtnLed.setEnabled(false);
					isBusy = true;
					tcpclient.write("LED0");
					//tcpclient.waitForTCPToReceive();
					while(isBusy && i < 1000){i++;try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}
					tglbtnLed.setEnabled(true);

				}
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(outputtextArea, GroupLayout.PREFERRED_SIZE, 389, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblEnterTheIp, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(porttextField, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addContainerGap()
											.addComponent(lblPort)))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
								.addComponent(IPtextField, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
							.addGap(102)
							.addComponent(tglbtnLed, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(25, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblEnterTheIp)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(IPtextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblPort)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(porttextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(tglbtnLed, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(outputtextArea, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	@Override
	public void run() {
		
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			if(isStarted && !(tcpclient.newInputFromTCP)){
				while(tcpclient.inFromServer.hasNext()){
					outputtextArea.append(tcpclient.inFromServer.nextLine());
					isBusy = false;
					
				}
				
			}

		}
	}
}
