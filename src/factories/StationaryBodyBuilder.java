package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body> {
	public StationaryBodyBuilder() {
		super("st_body", "Stationary Body");
	}

	@Override
	protected StationaryBody createInstance(JSONObject data) throws IllegalArgumentException{
		if(!data.has("id")) throw new IllegalArgumentException("Must have id");
		if(!data.has("gid"))throw new IllegalArgumentException("Must have gid");
		if(!data.has("p")) throw new IllegalArgumentException("Must have p");
		if(!data.has("m")) throw new IllegalArgumentException("ddd");	
		if(data.getJSONArray("p").length()!=2)throw new IllegalArgumentException("p must be 2D");
		JSONArray aux=data.getJSONArray("p");
		Vector2D p=new Vector2D(aux.getDouble(0),aux.getDouble(1));
		return new StationaryBody(data.getString("id"),data.getString("gid"),p,data.getDouble("m"));	

	}
}
