import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;


public class AlarmInterface {

	protected Shell shell;

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
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		Label lblPracticAlarmSystem = new Label(shell, SWT.NONE);
		lblPracticAlarmSystem.setAlignment(SWT.CENTER);
		lblPracticAlarmSystem.setFont(SWTResourceManager.getFont("Trajan Pro", 20, SWT.BOLD));
		lblPracticAlarmSystem.setBounds(88, 10, 252, 33);
		lblPracticAlarmSystem.setText("Practic Alarm System");
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 30, 430, 2);
		
		Group grpSensorsEnabled = new Group(shell, SWT.NONE);
		grpSensorsEnabled.setText("Sensors Enabled");
		grpSensorsEnabled.setBounds(10, 49, 430, 78);
		
		Group grpLogs = new Group(shell, SWT.NONE);
		grpLogs.setText("Logs");
		grpLogs.setBounds(10, 147, 430, 78);
		
		Button btnDisable = new Button(shell, SWT.NONE);
		btnDisable.setBounds(172, 240, 95, 28);
		btnDisable.setText("Disable");

	}
}
