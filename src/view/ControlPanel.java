package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ControlPanel extends JPanel implements SimulatorObserver {
	private Controller _ctrl;
	private JToolBar _toolaBar;
	private JFileChooser _fc;
	private boolean _stopped = true; // utilizado en los botones de run/stop
	private JButton _quitButton;//Boton de cerrar programa
	private JButton _file;//Bot�n de seleccionar archivos
	private JButton _forceLaws;//Boton de cambiar la ley de fuerza
	private JButton _simulation;//Boton representaci�n simulaci�n
	private JButton _advance; //Boton de run 
	private JButton _stop;//Boton de parar la simulaci�n
	private JSpinner st;//Cambiar los pasos de simulacion con las flechas 
	private JTextField dt;
	private Map<String, BodiesGroup> _groupsRO;
	private ForceLawsDialog f;//Para seleccionar una nueva ley de fuerza por pantalla
	private JFrame lawsF=new JFrame();//Frame de las leyes de fuerza
	private JFrame simulation=new JFrame();//Frame de la simulacion del programa
	private ViewerWindow vw;
// TODO a�ade m�s atributos aqu� �
	
	public ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		_toolaBar = new JToolBar();
		this.add(_toolaBar, BorderLayout.PAGE_START);
		// TODO crear los diferentes botones/atributos y a�adirlos a _toolaBar.
		// Todos ellos han de tener su correspondiente tooltip. Puedes utilizar
		// _toolaBar.addSeparator() para a�adir la l�nea de separaci�n vertical
		// entre las componentes que lo necesiten
		_file=new JButton();
		_forceLaws=new JButton();
		_simulation=new JButton();
		_advance=new JButton();
		_stop=new JButton();
		
		//Bot�n de seleccionar ficheros
		_file.setToolTipText("Load bodies file into the editor");
		_file.setIcon(new ImageIcon("resources/icons/open.png"));
		_toolaBar.add(_file);
		_toolaBar.addSeparator();
		//Bot�n de cambiar las leyes de fuerza
		_forceLaws.setToolTipText("Change force laws");
		_forceLaws.setIcon(new ImageIcon("resources/icons/physics.png"));
		_toolaBar.add(_forceLaws);
		//Bot�n de ver la simulaci�n
		_simulation.setToolTipText("View the simulation of the groups");
		_simulation.setIcon(new ImageIcon("resources/icons/viewer.png"));
		_toolaBar.add(_simulation);
		_toolaBar.addSeparator();
		//Bot�n de avanzar en la simulaci�n
		_advance.setToolTipText("Run the simulation");
		_advance.setIcon(new ImageIcon("resources/icons/run.png"));
		_toolaBar.add(_advance);
		//Bot�n de parar la simulaci�n
		_stop.setToolTipText("Stop the simulation");
		_stop.setIcon(new ImageIcon("resources/icons/stop.png"));
		_toolaBar.add(_stop);
		
		//JSpinner y JTextField
		
		SpinnerNumberModel stModel=new SpinnerNumberModel(0,0,10000,1);
		st=new JSpinner(stModel);
		st.setMaximumSize(new Dimension(1000,1000));
		st.setMinimumSize(new Dimension(1000,1000));
		dt=new JTextField();
		dt.setMaximumSize(new Dimension(2000,2000));
		dt.setMinimumSize(new Dimension(2000,2000));
		JLabel n1=new JLabel("Steps:");
		JLabel n2=new JLabel("Delta-time:");
		dt.setToolTipText("Set the delta-time");
		_toolaBar.add(n1);
		_toolaBar.add(st);
		_toolaBar.add(n2);
		_toolaBar.add(dt);
		
		// Quit Button
		_toolaBar.add(Box.createGlue()); // this aligns the button to the right
		_toolaBar.addSeparator();//Duda, donde esta el separador?????????
		_quitButton = new JButton();
		_quitButton.setToolTipText("Quit");
		_quitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		_toolaBar.add(_quitButton);
		_quitButton.addActionListener((e) -> Utils.quit(this));	
		
		//Crear el selector de ficheros
		
		_fc=new JFileChooser();
		_fc.setFileFilter(new FileNameExtensionFilter("Archivos de texto json", "json"));
		
		//Listeners de los diferentes botones
		
		
		_file.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int option=_fc.showOpenDialog(Utils.getWindow(ControlPanel.this));//Esto no se si est� bien, hay que preguntar a la profe
				if(option==JFileChooser.APPROVE_OPTION) {
					try {
						File f=_fc.getSelectedFile();
						FileInputStream Fi = new FileInputStream(f);
						_ctrl.reset();
						_ctrl.loadData(Fi);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						Utils.showErrorMsg(e1.getMessage());
					}catch (IllegalArgumentException ex) {
						Utils.showErrorMsg(ex.getMessage());
					}
				}
			}
		});
		
		_forceLaws.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(f==null) {
					f= new ForceLawsDialog(lawsF,_ctrl);
				}
				f.open();
			}	
		});
		
		_simulation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				vw = new ViewerWindow(simulation, _ctrl);
			}
		});
		
		_advance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped=false;	
				_file.setEnabled(false);
				_forceLaws.setEnabled(false);
				_simulation.setEnabled(false);
				_advance.setEnabled(false);
				try {
				_ctrl.setDeltaTime(Double.parseDouble(dt.getText()));
				run_sim(Integer.parseInt(st.getValue().toString()));
				}catch(NumberFormatException ex) {
					Utils.showErrorMsg(ex.getMessage());
					_stopped=true;	
					_file.setEnabled(true);
					_forceLaws.setEnabled(true);
					_simulation.setEnabled(true);
					_advance.setEnabled(true);
				}catch(IllegalArgumentException exp) {
					Utils.showErrorMsg(exp.getMessage());
					_stopped=true;	
					_file.setEnabled(true);
					_forceLaws.setEnabled(true);
					_simulation.setEnabled(true);
					_advance.setEnabled(true);
				}
			}
		});
		_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped=true;
			}
			
		});
		
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
		try {
		_ctrl.run(1);
		} catch (Exception e) {
		// Llama Utils.showErrorMsg con el mensaje de error
		Utils.showErrorMsg(e.getMessage());
		// Pon enable todos los botones
		_file.setEnabled(true);
		_forceLaws.setEnabled(true);
		_simulation.setEnabled(true);
		_advance.setEnabled(true);
		_stopped = true;
		return;
		}
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				run_sim(n-1);
		} });
		} else {
		_stopped = true;
		// Llama Utils.showErrorMsg con el mensaje de error
		if(n<0) {//Qu� mensage de Error habr�a que mostrar aqui?????
			Utils.showErrorMsg("Number of steps is negative");
		}
		_file.setEnabled(true);
		_forceLaws.setEnabled(true);
		_simulation.setEnabled(true);
		_advance.setEnabled(true);
		// Pon enable todos los botones 
		}
	}
	
	
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {	
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		this.dt.setText(String.valueOf(dt));
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
	}
}

