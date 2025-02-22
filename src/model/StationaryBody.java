package simulator.model;

import simulator.misc.Vector2D;

public class StationaryBody extends Body{

	public StationaryBody(String idd, String gid, Vector2D p, double m) throws IllegalArgumentException {
		super(idd, gid, p,new Vector2D(), m);
		// TODO Auto-generated constructor stub
	}

	@Override
	void advance(double dt) {	
	}

}
