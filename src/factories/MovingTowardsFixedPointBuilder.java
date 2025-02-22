package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws>{
	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "Moving Towards Fixed Point Builder");
	}

	@Override
	protected MovingTowardsFixedPoint createInstance(JSONObject data) {
		double g = 9.81;
		Vector2D c = new Vector2D();
		
		if(data.has("c")) {
			Vector2D aux = new Vector2D(data.getJSONArray("c").getDouble(0),data.getJSONArray("c").getDouble(1));
			c=aux;
		}
		if(data.has("g"))g=data.getDouble("g");
		return new MovingTowardsFixedPoint(c,g);

	}
	
	@Override
	public JSONObject getInfo() {
		JSONObject info=super.getInfo();
		JSONObject o2=new JSONObject();
		o2.put("c","the point towards which bodies move (e.g., [100.0,50.0])");
		o2.put("g", "the length of the acceleration vector (a number)");
		info.put("data", o2);
		return info;
		
	}
}
