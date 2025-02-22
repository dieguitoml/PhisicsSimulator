package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class StatusBar extends JPanel implements SimulatorObserver {
	// TODO A�adir los atributos necesarios, si hace falta �
	private JLabel groupsValue;
	private JLabel timeValue;
	
	
	StatusBar(Controller ctrl) {
		initGUI();
	    ctrl.addObserver(this); // se registra como observador
	}
	
	/*
	 * No se como se debe acceder a la informacion desde aqui(time y ngroups), ya que no veo como hacerlo desde controller 
	 * 
	 * Falta meter un espacio entre la info y la barrita
	 * 
	 */
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		// TODO Crear una etiqueta de tiempo y a�adirla al panel
		timeValue = new JLabel(String.valueOf("Time: ")); //lleva el numero equivalente al tiempo
		this.add(timeValue);
		
		JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
		s1.setPreferredSize(new Dimension(10, 20));
		this.add(s1);
		
		// TODO Crear la etiqueta de n�mero de grupos y a�adirla al panel
		groupsValue = new JLabel("Groups:");
		//groupsValue = new JLabel(String.valueOf(ngroups));// lleva el numero equivalente al numero de grupos
		this.add(groupsValue);
		
		
		// TODO Utilizar el siguiente c�digo para a�adir un separador vertical
		JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
		s2.setPreferredSize(new Dimension(10, 20));
		this.add(s2);
	}
	
	
	// TODO el resto de m�todos van aqu�
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		this.timeValue.setText("Time: "+time);
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		this.groupsValue.setText("Groups: " + groups.size());
		this.timeValue.setText("Time: "+time);
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		this.groupsValue.setText("Groups: " + groups.size());
		this.timeValue.setText("Time: "+time);
		
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		this.groupsValue.setText("Groups: " + groups.size());
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		this.timeValue.setText("Time: "+dt);
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
		
	}
	}

