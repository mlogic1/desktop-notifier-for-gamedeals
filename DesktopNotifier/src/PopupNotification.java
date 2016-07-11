import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


public class PopupNotification extends Thread implements ActionListener {
	
	private JFrame frame;
	private JPanel panelTop;
	private JSeparator separator;		// Horizontal line between head and body of the notification
	private JPanel panelBottom;
	private JScrollPane scroll;			// Scroll that enables scrolling if there are too many deals in the notification window
	
	private BorderLayout borderLayout;	// Layout for the frame
	private FlowLayout flowLayout;		// Layout for the top panel
	
	private Font entriesFont;			// Font for entries
	private Color entriesColor;			// Color for text
	private Color titleColor;			// Color for text
	
	private JLabel topLeftIcon;
	private JLabel topMiddleText;
	private JButton btn2;
	
	
	
	// TODO
	// Make the notification bar close itself on timer
	// Put the frame in top right corner of the screen
	
	// Replace this with proper Arraylist
	String[] dealNames;
	
	public PopupNotification(String[] DealNames){
		dealNames = DealNames;
		frame = new JFrame("Notification");
		//frame.setSize(400, 200);
		borderLayout = new BorderLayout();
		frame.getContentPane().setLayout(borderLayout);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(405, 260));
		
		
		panelTop = new JPanel();
		panelBottom = new JPanel();
		
		//panelTop.setBackground(new Color(255,0,0));
		//panelBottom.setBackground(new Color(0,255,0));
		
		entriesFont = new Font("Trebuchet MS", Font.BOLD, 16);
		entriesColor = new Color(75, 75, 75);
		titleColor = new Color(67, 91, 247);
		
		frame.getContentPane().add(panelTop, BorderLayout.PAGE_START);
		frame.getContentPane().add(panelBottom, BorderLayout.CENTER);
		
		scroll = new JScrollPane(panelBottom, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(scroll, BorderLayout.CENTER);
		
		flowLayout = new FlowLayout(FlowLayout.LEFT);
		panelTop.setLayout(flowLayout);
		panelTop.setBackground(new Color(250, 250, 250));
		
		panelBottom.setLayout(new BoxLayout(panelBottom, BoxLayout.Y_AXIS));
		panelBottom.setBackground(new Color(250, 250, 250));
		
		
		
		
		topLeftIcon = new JLabel();
		ImageIcon icon = new ImageIcon("desktop_notification.png");
		topLeftIcon.setIcon(icon);
		topMiddleText = new JLabel("New deals are posted on /r/GameDeals");
		topMiddleText.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		topMiddleText.setForeground(titleColor);
		ImageIcon iconClose = new ImageIcon("Actions_delete.png");
		btn2 = new JButton(iconClose);
		btn2.setPreferredSize(new Dimension(32, 32));
		btn2.setBorderPainted(false);
		btn2.setContentAreaFilled(false);
		btn2.setFocusPainted(false);
		btn2.setOpaque(false);
		
		//btn2.setIcon(iconClose);
		
		
		panelTop.add(topLeftIcon);
		panelTop.add(topMiddleText);
		panelTop.add(btn2);
		
		separator = new JSeparator();
		panelBottom.add(separator, Component.CENTER_ALIGNMENT);
		
		btn2.addActionListener(this);
		
		frame.setType(javax.swing.JFrame.Type.UTILITY);	// This removes the title from taskbar
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);				// Center notif, fix this
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, titleColor));	// Create border
	}
	
	@Override
	public void run(){
		ShowPopup();
	}
	
	
	
	public void ShowPopup(){
		
		for(int i=0;i<dealNames.length;i++){
			JLabel entryLabel = new JLabel("<html>" + dealNames[i] + "</html>", SwingConstants.LEFT);
			Border margin = new EmptyBorder(10, 10, 0, 0);	// Top and left margin
			entryLabel.setBorder(margin);
			entryLabel.setFont(entriesFont);
			entryLabel.setForeground(entriesColor);
			panelBottom.add( entryLabel, BorderLayout.CENTER);
		}
		
		int height = panelBottom.getHeight();
		
		frame.setVisible(true);
		frame.pack();
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		frame.dispose();
	}
}
