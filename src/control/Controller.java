package simulator.control;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	private PhysicsSimulator f;
	private Factory<Body> b;//Factoria generica de cuerpos
	private Factory<ForceLaws> l;//Factoria generica de leyes de fuerza
	
	public Controller(PhysicsSimulator f,Factory<Body> b,Factory<ForceLaws> l) {
		this.f=f;
		this.b=b;
		this.l=l;
	}
	
	public void loadData(InputStream in) {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray j1=jsonInput.getJSONArray("groups");
		for(int i=0;i<j1.length();i++) {
			f.addGroup(j1.get(i).toString());
		}
		if(jsonInput.has("laws")) {
			JSONArray j2=jsonInput.getJSONArray("laws");
			for(int i=0;i<j2.length();i++) {
				ForceLaws fl= l.createInstance(j2.getJSONObject(i).getJSONObject("laws"));
				f.setForceLaws(j2.getJSONObject(i).getString("id"), fl);
			}
		}
		JSONArray j3=jsonInput.getJSONArray("bodies");
		for(int i=0;i<j3.length();i++) {
			Body bb=b.createInstance(j3.getJSONObject(i));
			f.addBody(bb);
		}
	}
	
	public void run(int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");
		p.println(f.getState().toString());
		for(int i=0;i<n;i++) {
			f.advance();
			p.print(",");
			p.println(f.getState().toString());
		}
		p.println("]");
		p.println("}"); 
	}
	
	public void run(int n) {
		for(int i=0; i<n; i++) f.advance();
	}

	public List<JSONObject> getForceLawsInfo() {
		return l.getInfo();
	}
	
	public void reset() {
		f.reset();
	}
	
	public void setDeltaTime(double dt) {
		f.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o) {
		f.addObserver(o);
	}
	
	public void removeObserver(SimulatorObserver o) {
		f.removeObserver(o);
	}
	
	public void setForceLaws(String gId, JSONObject info) {//Para cambiar las leyes de fuerza
		ForceLaws nuevaLaw = l.createInstance(info);
		f.setForceLaws(gId, nuevaLaw);
	}
	
}
