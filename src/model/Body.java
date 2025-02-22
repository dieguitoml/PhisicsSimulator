package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Body {
	
	protected String id;//Identificador
	protected String gid;//Identidficador de su grupo
	protected Vector2D v;//Vector de velocidad
	protected Vector2D f;//Vector de fuerza
	protected Vector2D p;//Vector de posicion
	protected double m;//Masa
	
	public Body(String id, String gid, Vector2D p, Vector2D v,  double m) throws IllegalArgumentException{	
		checkValidArgs(id,gid,p,v,m);
		this.id=id.trim();		
		this.gid=gid.trim();		
		this.v=v;	
		this.p=p;
		this.m=m;
		this.f= new Vector2D();
	}
	
	private void checkValidArgs(String id, String gid, Vector2D p, Vector2D v,  double m)throws IllegalArgumentException{
		if(id==null)throw new IllegalArgumentException("Id cannot be null");
		if(id.isEmpty())throw new IllegalArgumentException("Id cannot be empty");
		if(id.trim().length()==0)throw new IllegalArgumentException("Id must have at least one char that is not white space");
		if(gid==null )throw new IllegalArgumentException("gId cannot be null");
		if(gid.isEmpty())throw new IllegalArgumentException("gId cannot be empty");
		if(gid.trim().length()==0)throw new IllegalArgumentException("gId must have at least one char that is not white space");
		if(v==null)throw new IllegalArgumentException("velocity vector cannot be null");
		if(p==null)throw new IllegalArgumentException("position vector cannot be null");
		if(m==0)throw new IllegalArgumentException("Mass cannot be zero");
		if(m<0)throw new IllegalArgumentException("mass cannot be negative");
	}
	
	public String getId() {// devuelve el identificador del cuerpo.
		return this.id;
	}

	public String getgId() {//devuelve el identificador del grupo al que	pertenece el cuerpo. 
		return this.gid;
	}
	
	public Vector2D getVelocity() {// devuelve el vector de velocidad.
		return this.v;
	}
	
	public Vector2D getForce() {// devuelve el vector de fuerza.
		return this.f;
	}
	
	public Vector2D getPosition() {// devuelve el vector de posición.
		return this.p;
	}
	
	public double getMass() {// devuelve la masa del cuerpo.
		return this.m;
	}
	void addForce(Vector2D force) {//añade la fuerza f al vector de fuerzadel cuerpo
		this.f=f.plus(force);
	}
	void resetForce() {
		this.f=new Vector2D();
	}
	
	abstract void advance(double dt);
	
	public JSONObject getState() {
		JSONObject jo1 = new JSONObject();
		jo1.put("id", this.id);
		jo1.put("m", this.m);
		jo1.put("p", p.asJSONArray());
		jo1.put("v", v.asJSONArray());
		jo1.put("f",f.asJSONArray());
		return jo1;		
	}
	
	public String toString() {// devuelve getState().toString()
		return getState().toString();
	}
	
	@Override

	public boolean equals(Object obj) {

		if (this == obj)

			return true;

		if (obj == null)

			return false;

		if (!(obj instanceof Body ))

			return false;

		Body other = (Body) obj;

		if (id == null || other.id == null) {

				return false;

		} else if (!id.equals(other.id))

			return false;

		return true;

	}

}
