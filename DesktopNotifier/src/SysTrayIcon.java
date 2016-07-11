import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JOptionPane;

public class SysTrayIcon {
	private TrayIcon trayIcon = null;
	private SystemTray systemTray = null;
	private Image image = null;
	private ItemListener notificationsItemListener;
	private ActionListener aboutItemListener = null;
	private ActionListener exitItemListener = null;
	private PopupMenu popupMenu = null;
	private CheckboxMenuItem menuItem0 = null;
	private MenuItem menuItem1 = null;
	private MenuItem menuItem2 = null;

	public SysTrayIcon() {
		// Constructor
		if (SystemTray.isSupported()) {
			System.out.println("System tray is supported");
			systemTray = SystemTray.getSystemTray();
			image = Toolkit.getDefaultToolkit().getImage("tray_icon.png");

			// Create the pop up menu
			popupMenu = new PopupMenu();
			// Create menu items
			menuItem0 = new CheckboxMenuItem("Enable/Disable", true);
			menuItem1 = new MenuItem("About");
			menuItem2 = new MenuItem("Exit");

			// Add menu items to the pop up menu
			popupMenu.add(menuItem0);
			popupMenu.add(menuItem1);
			popupMenu.add(menuItem2);

			// Create system tray icon
			trayIcon = new TrayIcon(image, "Notifier", popupMenu);

			// Add image to tray
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}

			// Action listeners tells what happens when user clicks a button on
			// the popup menu
			
			notificationsItemListener = new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if(menuItem0.getState()){
						SendBaloonMessageToDesktop("You have enabled notifications");
					}else{
						SendBaloonMessageToDesktop("You have disabled notifications");
					}
					
				}
			};
			
			
			aboutItemListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ShowAboutDialog();
				}
			};

			exitItemListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ExitApplication();

				}
			};

			// Set up listeners for the menu items
			
			menuItem0.addItemListener(notificationsItemListener);
			menuItem1.addActionListener(aboutItemListener);
			menuItem2.addActionListener(exitItemListener);

		} else {
			System.out.println("System tray is not supported");
		}

	}
	
	
	private void ShowAboutDialog(){
		String message = "Code: Filip Radic\n\nReddit icon by Martz90 under\nCC Attribution-Noncommercial-No Derivate 4.0\n\nApps preferences desktop notification Icon by Oxygen Team under\nGNU Lesser General Public License";
		JOptionPane.showMessageDialog(new Frame(), message, "Desktop Notifier for /r/GameDeals", 1);
	}
	
	private void ExitApplication(){
		System.exit(0);
	}
	
	public void SendBaloonMessageToDesktop(String message){
		trayIcon.displayMessage("New game deals on /r/GameDeals", message, TrayIcon.MessageType.INFO);
	}
	
}
