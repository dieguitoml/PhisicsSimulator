package simulator.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class GroupsTableModel extends AbstractTableModel implements SimulatorObserver {
	String[] _header = { "Id", "Force Laws", "Bodies" };
	List<BodiesGroup> _groups;
	GroupsTableModel(Controller ctrl) {
		_groups = new ArrayList<>();
		ctrl.addObserver(this);// TODO registrar this como observador;
	}
	// TODO el resto de m�todos van aqu� �
	
	@Override
	public String getColumnName(int col) {
		return _header[col];
	}	
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this._groups.size();
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this._header.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = this._groups.get(rowIndex).getId();
			break;
		case 1:
			s = this._groups.get(rowIndex).getForceLawsInfo();
			break;
		case 2:
			s="";
			Iterator<Body> it= this._groups.get(rowIndex).iterator();
			while(it.hasNext()) {
				s+=it.next().getId()+" ";
			}
			break;
		}
		return s;
	}
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		_groups.clear();
		fireTableStructureChanged();
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		_groups.addAll(groups.values());
		fireTableStructureChanged();
	
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		_groups.add(g);
		fireTableStructureChanged();
	
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {//NO estoy seguro, hay que revisarlo
		// TODO Auto-generated method stub
		_groups.clear();
		_groups.addAll(groups.values());
		fireTableStructureChanged();
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
	
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		fireTableStructureChanged();
		
	}
}
