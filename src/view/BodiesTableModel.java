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

class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
	String[] _header = { "Id", "gId", "Mass", "Velocity", "Position", "Force" };
	List<Body> _bodies;
	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}
	
	@Override
	public String getColumnName(int col) {
		return _header[col];
	}
	
	@Override
	public int getRowCount() {
		return _bodies.size();
	}
	@Override
	public int getColumnCount() {
		return _header.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = this._bodies.get(rowIndex).getId();
			break;
		case 1:
			s = this._bodies.get(rowIndex).getgId();
			break;
		case 2:
			s = this._bodies.get(rowIndex).getMass();
			break;
		case 3:
			s= this._bodies.get(rowIndex).getVelocity();
			break;
		case 4:	
			s=this._bodies.get(rowIndex).getPosition();
			break;
		case 5:
			s=this._bodies.get(rowIndex).getForce();
			break;
		}
		return s;
	}
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		_bodies.clear();
		for(BodiesGroup b:groups.values()) {
			Iterator<Body> it=b.iterator();
			while(it.hasNext()) {
				_bodies.add(it.next());
			}
		}
		fireTableDataChanged();	
	}
	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		_bodies.clear();
		fireTableStructureChanged();
	
	}
	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		for(BodiesGroup b:groups.values()) {
			Iterator<Body> it=b.iterator();
			while(it.hasNext()) {
				_bodies.add(it.next());
			}
		}
		fireTableStructureChanged();
	}
	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		for(BodiesGroup b:groups.values()) {
			Iterator<Body> it=b.iterator();
			while(it.hasNext()) {
				if(_bodies.contains(it.next())) {
					_bodies.add(it.next());
				}
			}
		}
		fireTableStructureChanged();
	
	}
	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		_bodies.add(b);
		fireTableStructureChanged();
	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
	
	}
	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
	}
}
