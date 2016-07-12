import java.util.Timer;

public class TimerHandler extends Thread {
	Timer timer = null;
	SysTrayIcon trayIcon = null;
	TimedJob job;	// Timed job downloads and parses reddit data at specified time

	public TimerHandler() {
		
	}

	public void run() {
		// Create system tray icon
		trayIcon = new SysTrayIcon();
		
		// Create a job
		job = new TimedJob(trayIcon);
		
		// Create timer
		timer = new Timer();
	
		// Create task that repeats itself at fixed time
		timer.scheduleAtFixedRate(job, 0, 3600 * 1000);		// 3600 (1h) seconds to ms
		// Fix this so the user can choose the refresh time
	}

}
