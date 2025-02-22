package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body>{

	public MovingBodyBuilder() {
		super("mv_body", "Moving Body");
	}

	@Override
	protected MovingBody createInstance(JSONObject data) throws IllegalArgumentException{
		if(!data.has("id")) throw new IllegalArgumentException("Must have id");
		if(!data.has("gid"))throw new IllegalArgumentException("Must have gid");
		if(!data.has("p")) throw new IllegalArgumentException("Must have p");
		if(!data.has("v")) throw new IllegalArgumentException("Must have v");
		if(!data.has("m")) throw new IllegalArgumentException("ddd");	
		if(data.getJSONArray("p").length()!=2)throw new IllegalArgumentException("p must be 2D");
		if(data.getJSONArray("v").length()!=2)throw new IllegalArgumentException("v must be 2D");
		JSONArray aux=data.getJSONArray("p");
		Vector2D p=new Vector2D(aux.getDouble(0),aux.getDouble(1));
		aux=data.getJSONArray("v");
		Vector2D v=new Vector2D(aux.getDouble(0),aux.getDouble(1));
		return new MovingBody(data.getString("id"),data.getString("gid"),p,v,data.getDouble("m"));	

	}

}
