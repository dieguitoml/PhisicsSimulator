package simulator.factories;
import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws>{
	public NewtonUniversalGravitationBuilder() {
		super("nlug", "Newton Universal Gravitation");
	}

	@Override
	protected NewtonUniversalGravitation createInstance(JSONObject data) {
		if(!data.has("G"))return new NewtonUniversalGravitation(6.67E-11);
		return new NewtonUniversalGravitation(data.getDouble("G"));

	}
	
	@Override
	public JSONObject getInfo() {
		JSONObject info=super.getInfo();
		JSONObject o2=new JSONObject();
		o2.put("G", "the gravitational constant (a number)");
		info.put("data", o2);
		return info;
		
	}
}
