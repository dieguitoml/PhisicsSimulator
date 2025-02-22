package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable <SimulatorObserver>{

	private double dt;//Tiempo real por paso
	private double ActualTime;//Tiempo actual
	private ForceLaws force;
	private Map<String,BodiesGroup> m;
	private List<String> s;
	private List<SimulatorObserver> observers;
	private Map<String, BodiesGroup> _groupsRO;
	
	public PhysicsSimulator(ForceLaws force,double dt) throws IllegalArgumentException{
		if(dt<=0)throw new IllegalArgumentException("Delta-time must be positive");
		if(force==null)throw new IllegalArgumentException("Force laws cannot be null");
		this.dt=dt;
		this.force=force;
		this.m=new HashMap<>();
		this.s=new ArrayList<>();
		this.ActualTime=0;
		this.observers=new ArrayList<>();
		_groupsRO=Collections.unmodifiableMap(m);
	}
	
	public void advance() {
		for(BodiesGroup b:m.values()) {
			b.advance(dt);
		}
		this.ActualTime+=dt;
		
		notifyAdvance();
	}
	
	public void addGroup(String id) throws IllegalArgumentException{
		if(m.size()>0 && m.containsKey(id))throw new IllegalArgumentException("Cannot add a group twice");
		BodiesGroup b=new BodiesGroup(id,force);
		m.put(id,b);
		s.add(id);
		notifyAddGroup(b);
	}
	
	public void addBody(Body b) throws IllegalArgumentException{
		BodiesGroup i=m.get(b.getgId());
		if(i==null)throw new IllegalArgumentException("Group must exists");
		i.addBody(b);
		notifyAddBody(b);
	}
	
	public void setForceLaws(String id, ForceLaws f) throws IllegalArgumentException{
		if(!m.containsKey(id))throw new IllegalArgumentException("Group must exists");
		else {
			m.get(id).setForceLaws(f);
			notifySetForceLaws(m.get(id));
		}
	}
	
	public JSONObject getState() {
		JSONObject o1=new JSONObject();
		JSONArray o2=new JSONArray();
		o1.put("time", ActualTime);
		for(String st:s) {	
			o2.put(m.get(st).getState());
		}
		o1.put("groups", o2);
		return o1;
	}
	
	public String toString() {
		return getState().toString();
		
	}
	
	public void reset() {
		m.clear();
		s.clear();
		ActualTime=0;
		notifyReset();
	}
	
	public void setDeltaTime(Double dt) throws IllegalArgumentException{
		if(dt<0)throw new IllegalArgumentException("Dt must be positive");
		this.dt=dt;

		notifySetDeltaTime();
	}

	@Override
	public void addObserver(SimulatorObserver o) {
		if(!observers.contains(o)) {
			observers.add(o);
			notifyAddObserver(o);
		}
	}

	@Override
	public void removeObserver(SimulatorObserver o) {
		observers.remove(o);
		
	}
	
	private void notifySetForceLaws(BodiesGroup bg) {
		for(SimulatorObserver o : observers) {
			o.onForceLawsChanged(bg);
		}
	}
	
	private void notifySetDeltaTime() {
		for(SimulatorObserver o : observers) {
			o.onDeltaTimeChanged(dt);
		}
	}
	
	private void notifyReset() {
		for(SimulatorObserver o : observers) {
			o.onReset(_groupsRO, ActualTime, dt);
		}
	}
	
	private void notifyAddBody(Body b) {
		for(SimulatorObserver o : observers) {
			o.onBodyAdded(_groupsRO, b);
		}
	}
	
	private void notifyAddGroup(BodiesGroup bg) {
		for(SimulatorObserver o : observers) {
			o.onGroupAdded(_groupsRO, bg);
		}
	}
	
	private void notifyAdvance() {
		for(SimulatorObserver o : observers) {
			o.onAdvance(_groupsRO, ActualTime);
		}
	}
	
	private void notifyAddObserver(SimulatorObserver o) {
		o.onRegister(_groupsRO, ActualTime, dt);
	}
}
