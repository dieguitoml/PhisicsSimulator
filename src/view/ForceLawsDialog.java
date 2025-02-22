package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class ForceLawsDialog extends JDialog implements SimulatorObserver {
	private DefaultComboBoxModel<String> _lawsModel;
	private DefaultComboBoxModel<String> _groupsModel;
	private DefaultTableModel _dataTableModel;
	private Controller _ctrl;
	private List<JSONObject> _forceLawsInfo;
	private String[] _headers = { "Key", "Value", "Description" };
	private int _selectedLawsIndex;
// TODO en caso de ser necesario, añadir los atributos aquí
	ForceLawsDialog(Frame parent, Controller ctrl) {
		super(parent, true);
		_ctrl = ctrl;
		initGUI();
		// TODO registrar this como observer;
		_ctrl.addObserver(this);
	}
private void initGUI() {
	setTitle("Force Laws Selection");
	setModal(true);
	JPanel mainPanel = new JPanel();
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	setContentPane(mainPanel);
	//Help
	JLabel help = new JLabel("<html><p>Select a force law and "
			+ "provide values for the parametes in the <b>Value column</b> (default values "
			+ "are used for parametes with no value).</p></html>");
	help.setAlignmentX(CENTER_ALIGNMENT);
	mainPanel.add(help);
	mainPanel.add(Box.createRigidArea(new Dimension(0,20)));
	// _forceLawsInfo se usará para establecer la información en la tabla
	_forceLawsInfo = _ctrl.getForceLawsInfo();
	// TODO crear un JTable que use _dataTableModel, y añadirla al panel
	_dataTableModel = new DefaultTableModel() {
		@Override
		public boolean isCellEditable(int row, int column) {
			return column!=0;
		}
	};
	_dataTableModel.setColumnIdentifiers(_headers);
	JTable t=new JTable(_dataTableModel) {
		private static final long serialVersionUID = 1L;

		// we override prepareRenderer to resize columns to fit to content
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			Component component = super.prepareRenderer(renderer, row, column);
			int rendererWidth = component.getPreferredSize().width;
			TableColumn tableColumn = getColumnModel().getColumn(column);
			tableColumn.setPreferredWidth(
					Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
			return component;
		}
	};
	t.add(new JScrollBar());
	mainPanel.add(t);
	mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
	_lawsModel = new DefaultComboBoxModel<>();
	_groupsModel = new DefaultComboBoxModel<>();
	// TODO añadir la descripción de todas las leyes de fuerza a _lawsModel
	for(int i=0;i<_forceLawsInfo.size();i++) {
		_lawsModel.addElement(_forceLawsInfo.get(i).getString("desc"));
	}
	
	// TODO crear un combobox que use _lawsModel y añadirlo al panel
	JComboBox bl=new JComboBox(_lawsModel);
	JLabel name1=new JLabel("Force Law: ");
	JPanel lm=new JPanel();
	lm.add(name1);
	lm.add(bl);
	JComboBox bg=new JComboBox<String>(_groupsModel);
	JLabel name2=new JLabel("  Group: ");
	lm.add(name2);
	lm.add(bg);
	lm.setAlignmentX(CENTER_ALIGNMENT);
	mainPanel.add(lm);
	bl.addActionListener((e) -> {
		_selectedLawsIndex=bl.getSelectedIndex();
		updateTableModel(bl.getSelectedIndex());
	});
	updateTableModel(bl.getSelectedIndex());
	// TODO crear los botones OK y Cancel y añadirlos al panel
	JButton Cancel=new JButton("Cancel");
	Cancel.addActionListener( new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ForceLawsDialog.this.setVisible(false);
		}
	});
	JButton Ok=new JButton("Ok");
	Ok.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {//Hacer un metodo que devuelva true o false en funcion de si se ha podido hacer el cambio
			if(changeForceLaws(bl,bg)) {
				ForceLawsDialog.this.setVisible(false);
			}		
		}
	});
	JPanel button=new JPanel();
	button.add(Cancel);
	button.add(Ok);
	mainPanel.add(button);
	setPreferredSize(new Dimension(700, 400));
	pack();
	setResizable(false);
	setVisible(false);
}
	public void open() {
		if (_groupsModel.getSize() == 0)
			//return _status;
		// TODO Establecer la posición de la ventana de diálogo de tal manera que se
		// abra en el centro de la ventana principal
			//return _status;
			if (getParent() != null)
			setLocation(getParent().getLocation().x+50, getParent().getLocation().y+100);
		pack();
		setVisible(true);
	}
	
	
	
	// TODO el resto de métodos van aquí…
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		_groupsModel.removeAllElements();
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		_groupsModel.addAll(groups.keySet());
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		_groupsModel.addAll(groups.keySet());
		
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
		
	}
	private void updateTableModel(int index) {
		JSONObject o=_forceLawsInfo.get(index);
		JSONObject d= o.getJSONObject("data");
		_dataTableModel.setNumRows(d.length()+1);
		for (int i = 0; i < _headers.length; i++) {
			_dataTableModel.setValueAt(_headers[i], 0, i);
		}
		
		if(!d.isEmpty()) {
			Iterator it = d.keySet().iterator();
			int j = 1;
		    while(it.hasNext()) {
		    	String a = (String) it.next();
		    	_dataTableModel.setValueAt(a, j, 0);
		    	_dataTableModel.setValueAt(o.getJSONObject("data").get(a).toString(), j, 2);
		    	j++;
		    }
		}
	}
	
	public String getData() {
		StringBuilder s = new StringBuilder();
		s.append('{');
		for (int i = 0; i < _dataTableModel.getRowCount(); i++) {
		String k = _dataTableModel.getValueAt(i, 0).toString();
		String v = _dataTableModel.getValueAt(i, 1).toString();
		if (!v.isEmpty()) {
			s.append('"');
			s.append(k);
			s.append('"');
			s.append(':');
			s.append(v);
			s.append(',');
		}
		}

		if (s.length() > 1) 
		s.deleteCharAt(s.length() - 1);
		s.append('}');

		return s.toString();
	}
	
	private boolean changeForceLaws(JComboBox bl, JComboBox bg) {//Intenta cambiar las leyes de fuerza, devolviendo true o false
		JSONObject fl = new JSONObject("{\"type\":" + _forceLawsInfo.get(_selectedLawsIndex).getString("type")
				+ ", \"data\":" + getData() + "}");
		
		try {
		_ctrl.setForceLaws(bg.getSelectedItem().toString(),fl);
		}catch(IllegalArgumentException e) {
			Utils.showErrorMsg(e.getMessage());
			return false;
		}
		return true;
	}
	
}

