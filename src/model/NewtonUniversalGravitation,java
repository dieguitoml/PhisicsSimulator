package simulator.model;

import java.util.List;

public class NewtonUniversalGravitation implements ForceLaws{
	
	private double G;
	
	public NewtonUniversalGravitation(double G) throws IllegalArgumentException{
		if(G<0)throw new IllegalArgumentException("G cannot be negative");
		if(G==0)throw new IllegalArgumentException("G cannot be zero");
		this.G=G;
	}

	@Override
	public void apply(List<Body> bs) {
		// TODO Auto-generated method stub
		for(Body b: bs) {
			for(Body r:bs) {
				if(b!=r) {
					if((r.getPosition().minus(b.getPosition())).magnitude()>0) {
						double m=r.getPosition().minus(b.getPosition()).magnitude();
						double f=(G*b.getMass()*r.getMass()/(m*m));
						b.addForce((r.getPosition().minus(b.getPosition())).direction().scale(f));
					}
				}
			}
		}
	}
	
	public String toString() {
		return "Newton’s Universal Gravitation with G="+ G;
	}

}
