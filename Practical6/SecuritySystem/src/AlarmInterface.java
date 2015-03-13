/*
 * COS 332 - Practical 6
 * Contributors to this project:
 * 	-Jason Richard Evans u13032608
 *  -Vivian Laura-Lee Venter u13238435
 *  
 */

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class AlarmInterface {

	protected Shell shell;
	private	String alarmMessage = "";

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AlarmInterface window = new AlarmInterface();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 382);
		shell.setText("Practic Alarms");
		
		Label lblPracticAlarmSystem = new Label(shell, SWT.NONE);
		lblPracticAlarmSystem.setAlignment(SWT.CENTER);
		lblPracticAlarmSystem.setFont(SWTResourceManager.getFont("Trajan Pro", 20, SWT.BOLD));
		lblPracticAlarmSystem.setBounds(88, 10, 252, 33);
		lblPracticAlarmSystem.setText("Practic Alarm System");
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 30, 430, 2);
		
		Group grpSensorsEnabled = new Group(shell, SWT.NONE);
		grpSensorsEnabled.setText("Sensors Enabled");
		grpSensorsEnabled.setBounds(10, 49, 430, 92);
		
		Label lblDDoor = new Label(grpSensorsEnabled, SWT.NONE);
		lblDDoor.setBounds(57, 10, 90, 14);
		lblDDoor.setText("F: Front Door");
		
		Label lblWWindows = new Label(grpSensorsEnabled, SWT.NONE);
		lblWWindows.setBounds(57, 30, 90, 14);
		lblWWindows.setText("W: Windows");
		
		Label lblOpOutsidePerimeter = new Label(grpSensorsEnabled, SWT.NONE);
		lblOpOutsidePerimeter.setBounds(57, 51, 133, 14);
		lblOpOutsidePerimeter.setText("O: Outside Perimeter");
		
		Label lblBdBackDoor = new Label(grpSensorsEnabled, SWT.NONE);
		lblBdBackDoor.setBounds(261, 10, 133, 14);
		lblBdBackDoor.setText("B: Back Door");
		
		Label lblIInside = new Label(grpSensorsEnabled, SWT.NONE);
		lblIInside.setBounds(261, 30, 60, 14);
		lblIInside.setText("I: Inside");
		
		Label lblPPanic = new Label(grpSensorsEnabled, SWT.NONE);
		lblPPanic.setBounds(261, 51, 60, 14);
		lblPPanic.setText("P: Panic");
		
		Group grpLogs = new Group(shell, SWT.NONE);
		grpLogs.setText("Logs");
		grpLogs.setBounds(10, 147, 430, 157);
		
		List list = new List(grpLogs, SWT.BORDER);
		list.setBounds(10, 10, 406, 120);
		
		Button btnDisable = new Button(shell, SWT.NONE);
		btnDisable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose(); //Close the application
			}
		});
		btnDisable.setBounds(174, 310, 95, 28);
		btnDisable.setText("Disable");
		
		list.addKeyListener(new KeyAdapter() {
			//@Override
			public void keyPressed(KeyEvent e) {
				/*
				 * When a key is pressed, on the list, an alarm is activated.
				 * There are just a few pre-determined alams specified in this instance.
				 * 
				 */
				char theKey = e.character;
				theKey = Character.toUpperCase(theKey);
				switch(theKey){
					case 'F': 	alarmMessage = "Front Door Breached.";
								list.add(alarmMessage);
								completeAlarm();
								break;
					case 'W':	alarmMessage = "Windows have been Breached.";
								list.add(alarmMessage);
								completeAlarm();
								break;
					case 'O':	alarmMessage = "Outside Perimeter has been Breached";
								list.add(alarmMessage);
								completeAlarm();
								break;
					case 'B':	alarmMessage = "Back Door has been Breached.";
								list.add(alarmMessage);
								completeAlarm();
								break;
					case 'I':	alarmMessage = "Inside Perimeter has been Breached.";
								list.add(alarmMessage);
								completeAlarm();
								break;
					case 'P':	alarmMessage = "A panic alarm has been raised.";
								list.add(alarmMessage);
								completeAlarm();
								break;
				};
			}
		});

	}
	
	private void completeAlarm(){
		emailSystem mail = new emailSystem();
		mail.sendEmail(alarmMessage);
	}
}
