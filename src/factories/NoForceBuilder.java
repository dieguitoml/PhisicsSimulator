package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws>{
	public NoForceBuilder() {
		super("nf", "No Force Builder");
	}

	@Override
	protected NoForce createInstance(JSONObject data) {
		return new NoForce();
	}
	
	@Override
	public JSONObject getInfo() {
		JSONObject info=super.getInfo();
		JSONObject nf=new JSONObject();
		info.put("data", nf);
		return info;
	}
}
