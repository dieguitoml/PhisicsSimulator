package simulator.view;

import simulator.control.*;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainWindow extends JFrame {
	private Controller _ctrl;
	private ControlPanel cp;
	private StatusBar sb;
	public MainWindow(Controller c) {
		super("Physics Simulator");
		_ctrl = c;
		initGUI();
	}	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		// Crea ControlPanel y añadelo a PAGE_START de mainPanel
		cp=new ControlPanel(this._ctrl);
		mainPanel.add(this.cp,BorderLayout.PAGE_START);
		// Crea StatusBar and y añadelo a PAGE_END de mainPanel
		sb=new StatusBar(_ctrl);
		mainPanel.add(sb,BorderLayout.PAGE_END);
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		// Crea tabla de grupos y añadela a contentPanel.
		InfoTable Groups=new InfoTable("Groups", new GroupsTableModel(_ctrl));
		contentPanel.add(Groups);
		// Usa setPreferredSize(new Dimension(500, 250)) para su tamaño
		Groups.setPreferredSize(new Dimension(500,250));
		// Crea la tabla de cuerpos y añadela a contentPanel.
		InfoTable Bodies =	new InfoTable("Bodies", new BodiesTableModel(_ctrl));
		contentPanel.add(Bodies);
		// Usa setPreferredSize(new Dimension(500, 250)) para su tamaño
		Bodies.setPreferredSize(new Dimension(500,250));
		// Llama a Utils.quit(MainWindow.this) in method windowClosing
		addWindowListener(new WindowListener(){
			@Override
			public void windowClosing(WindowEvent e) {
				Utils.quit(MainWindow.this);
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		}
		);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
		}
	}
