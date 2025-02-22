package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup implements Iterable<Body> {
	private String id;//identificador
	private ForceLaws force;//Leyes de fuerzaç
	private List<Body> l;//Lista de bodies
	private List<Body> _bodiesRO;

	
	
	public BodiesGroup(String id, ForceLaws force) throws IllegalArgumentException {
		if(id==null)throw new IllegalArgumentException("Id cannot be null");
		if(id.trim().length()==0)throw new IllegalArgumentException("Id must have at least one char that is not white space");
		if(id.isBlank())throw new IllegalArgumentException("Id cannot be empty");
		if(force==null)throw new IllegalArgumentException("Force laws cannot be null");
		this.id=id.trim();
		this.id=id.trim();
		this.force=force;
		l=new ArrayList<>();
		_bodiesRO = Collections.unmodifiableList(l);

	}
	
	public String getId() {
		return this.id;
	}
	
	void setForceLaws(ForceLaws fl) throws IllegalArgumentException{
		if(fl==null)throw new IllegalArgumentException("Bodies in a group must have different ids");
		this.force=fl;
	}
	
	void addBody(Body b) throws IllegalArgumentException{
		if(b==null)throw new IllegalArgumentException("Bodies in a group must have different ids");
		if(l.contains(b))throw new IllegalArgumentException("Bodies in a group must have different ids");
		l.add(b);
	}
	
	void advance(double dt) throws IllegalArgumentException{
		if(dt<0||dt==0)throw new IllegalArgumentException("Delta time must be positive");
		for(Body body:l) {
			body.resetForce();
		}
		force.apply(l);
		for(Body body:l) {
			body.advance(dt);
		}
	}
	
	public JSONObject getState() {
		JSONObject jo1 = new JSONObject();
		jo1.put("id", id);
		JSONArray j=new JSONArray();
		for(Body b:l) {
			j.put(b.getState());
		}
		jo1.put("bodies", j);
		return jo1;
	}
	
	public String getForceLawsInfo() {
		return force.toString();
		}
	
	public String toString() {
		return getState().toString();
	}
	
	@Override

	public boolean equals(Object obj) {

		if (this == obj)

			return true;

		if (obj == null)

			return false;

		if (getClass() != obj.getClass())

			return false;

		BodiesGroup other = (BodiesGroup) obj;

		if (id == null) {

			if (other.id != null)

				return false;

		} else if (!id.equals(other.id))

			return false;

		return true;


	}

      @Override

	public int hashCode() {

		final int prime = 31;

		int result = 1;

		result = prime * result + ((id == null) ? 0 : id.hashCode());

		return result;


	}

	@Override
	public Iterator<Body> iterator() {
		return _bodiesRO.iterator();
	}
}
