package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import simulator.misc.Vector2D;
import simulator.model.BodiesGroup;
import simulator.model.Body;

@SuppressWarnings("serial")
class Viewer extends SimulationViewer {

	private static final int _WIDTH = 500;
	private static final int _HEIGHT = 500;

	// (_centerX,_centerY) is used as the origin when drawing
	// the bodies
	private int _centerX;
	private int _centerY;

	// values used to shift the actual origin (the middle of
	// the window), when calculating (_centerX,_centerY)
	private int _originX = 0;
	private int _originY = 0;

	// the scale factor, used to reduce the bodies coordinates
	// to the size of the component
	private double _scale = 1.0;

	// indicates if the help message should bae shown
	private boolean _showHelp = true;

	// indicates if the position/velocity vectors should be shown
	private boolean _showVectors = true;

	// the list bodies and groups
	private List<Body> _bodies;
	private List<BodiesGroup> _groups;

	// a color generator, and a map that assigns colors to groups
	private ColorsGenerator _colorGen;
	private Map<String, Color> _gColor;

	// the index and Id of the selected group, -1 and null means all groups
	private int _selectedGroupIdx = -1;
	private String _selectedGroup = null;

	Viewer() {
		initGUI();
	}

	private void initGUI() {

		// add a border
		setBorder(BorderFactory.createLineBorder(Color.black, 2));

		// initialize the color generator, and the map, that we use
		// assign colors to groups
		_colorGen = new ColorsGenerator();
		_gColor = new HashMap<>();

		// initialize the lists of bodies and groups
		_bodies = new ArrayList<>();
		_groups = new ArrayList<>();

		// The preferred and minimum size of the components
		setMinimumSize(new Dimension(_WIDTH, _HEIGHT));
		setPreferredSize(new Dimension(_WIDTH, _HEIGHT));

		// add a key listener to handle the user actions
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			/*
			 * 
			 * TODO
			 * 
			 * EN: handle keys 'j','l','i','m' to add 10/-10 to _originX/_originY, and then
			 * call repaint(). This will make the origin point moves left/right/up/down. See
			 * how the values of _centerX and _centerY are calculated in method
			 * paintComponent
			 * 
			 * ES: Gestiona las teclas 'j','l','i','m' para sumar 10/-10 a
			 * _originX/_originY, y luego llame a repaint(). Esto hará que el punto de
			 * origen se mueva hacia la izquierda/derecha/arriba/abajo. Vea cómo se calculan
			 * los valores de _centerX y _centerY en el método paintComponent
			 * 
			 * TODO
			 * 
			 * EN: handle key 'k' to set _originX and _originY to 0, and then call
			 * repaint(). This will return the origin point to the center of the window.
			 * 
			 * ES: Gestiona la tecla 'k' para poner _originX y _originY en 0, y luego llame
			 * a repaint(). Esto hace que el punto de origen sea el centro de la ventana.
			 * 
			 * TODO
			 * 
			 * EN: handle key 'h' to change the value of _showHelp to !_showHelp, and then
			 * call repaint(). This will make show/hide the help text - see method
			 * paintComponent
			 * 
			 * ES: gestiona la tecla 'h' para cambiar el valor de _showHelp a !_showHelp, y
			 * luego llame a repaint(). Esto hará que se muestre/oculte el texto de ayuda -
			 * ver método paintComponent
			 * 
			 * TODO
			 * 
			 * EN: handle key 'v' to change the value of _showVectors to !_showVectors, and
			 * then call repaint(). You will use this variable in drawBodies to decide if to
			 * show or hide the velocity/force vectors.
			 * 
			 * ES: gestiona la tecla 'v' para cambiar el valor de _showVectors a
			 * !_showVectors, y luego llame a repaint(). Tienes que usar esta variable en
			 * drawBodies para decidir si mostrar u ocultar los vectores de
			 * velocidad/fuerza.
			 * 
			 * TODO
			 * 
			 * EN: handle key 'g' such that it makes the next group visible. Note that after
			 * the last group all bodies are shown again. This should be done by modifying
			 * _selectedGroupIdx from -1 (all groups) to _groups.size()-1 in a circular way.
			 * When its value is -1 you should set _selectedGroup to null, otherwise to the
			 * id of the corresponding group. Then in method showBodies you will draw only
			 * those that belong to the selected group.
			 * 
			 * ES: gestionar la tecla 'g' de manera que haga visible el siguiente grupo.
			 * Tenga en cuenta que después del último grupo, se muestran todos los cuerpos.
			 * Esto se puede hacer modificando _selectedGroupIdx de -1 (todos los grupos) a
			 * _groups.size()-1 de forma circular. Cuando su valor es -1, _selectedGroup
			 * sería nulo, de lo contrario, sería el id del grupo correspondiente. En el
			 * método showBodies, solo dibujarás los que pertenecen al grupo seleccionado.
			 * 
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'g':
					if(_selectedGroupIdx==_groups.size()-1) {
						_selectedGroupIdx=-1;
						_selectedGroup=null;
					}else {
						_selectedGroupIdx+=1;
						_selectedGroup=_groups.get(_selectedGroupIdx).getId();
					}
					repaint();
					break;
				case 'j':
					_originX-=10;
					repaint();
					break;
				case 'l':
					_originX+=10;
					repaint();
					break;
				case 'i':
					_originY+=10;
					repaint();
					break;
				case 'm':
					_originY-=10;
					repaint();
					break;
				case 'k':
					_originX=0;
					_originY=0;
					repaint();
					break;
				case 'v':
					_showVectors=!_showVectors;
					repaint();
					break;
				case 'h':
					_showHelp=!_showHelp;
					repaint();
					break;
				case '-':
					_scale = _scale * 1.1;
					repaint();
					break;
				case '+':
					_scale = Math.max(1000.0, _scale / 1.1);
					repaint();
					break;
				case '=':
					autoScale();
					repaint();
					break;

				default:
				}
			}
		});

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// a better graphics object
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// calculate the center
		_centerX = getWidth() / 2 - _originX;
		_centerY = getHeight() / 2 - _originY;

		// TODO draw red cross at (_centerX,_centerY)
		
		gr.setColor(Color.RED);
		gr.drawLine(_centerX, _centerY, _centerX+5, _centerY);
		gr.drawLine(_centerX, _centerY, _centerX-5, _centerY);
		gr.drawLine(_centerX, _centerY, _centerX, _centerY+5);
		gr.drawLine(_centerX, _centerY, _centerX, _centerY-5);

		// draw bodies
		drawBodies(gr);

		// show help if needed
		if (_showHelp) {
			showHelp(gr);
		}
	}

	private void showHelp(Graphics2D g) {//PREGUNTAR SI ESTE MÉTODO ESTÁ BIEN
		/*
		 * TODO
		 * 
		 * EN: complete to show the following text on the top-left corner:
		 * 
		 * h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit //
		 * l: move right, j: move left, i: move up, m: move down: k: reset 
		 * g: show next group
		 * Scaling ratio: ... 
		 * Selected Group: ...
		 * 
		 * ES: completa el método para que muestre el siguiente texto en la esquina
		 * superior izquierda:
		 * 
		 * h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit 
		 * l: move right, j: move left, i: move up, m: move down: k: reset 
		 * g: show next group
		 * Scaling ratio: ... 
		 * Selected Group: ...
		 * 
		 */
		g.setColor(Color.RED);
		String h="h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit";
		g.drawString(h,10,15); 
		String gx="g: show next group";
		g.drawString(gx, 10, 30);
		String l="l: move right, j: move left, i: move up, m: move down: k: reset ";
		g.drawString(l, 10, 45);
		String sc="Scaling ratio: " + String.valueOf(_scale);
		g.drawString(sc, 10, 60);
		g.setColor(Color.BLUE);
		String gp="Selected Group: ";
		if(_selectedGroup==null) {
			gp+="all";
		}else {
			gp+=_selectedGroup;
		}
		g.drawString(gp,10,75);
	}

	private void drawBodies(Graphics2D g) {
		/*
		 * TODO
		 * 
		 * EN: draw all bodies for which isVisible(b) return 'true' (see isVisible
		 * below, it returns true if the body belongs to the selected group). For each
		 * body, you should draw the velocity and force vectors if _showVectors is true.
		 * Use method drawLineWithArrow to draw the vectors. The color of body 'b'
		 * should be _gColor.get(b.getgId()) -- see method addGroup below. You should
		 * assume that the origin point is (_centerX,_centerY), and recall to divide the
		 * coordinates of the body by the value of _scale.
		 * 
		 * 
		 * ES: Dibuja todos los cuerpos para los que isVisible(b) devuelve 'true' (ver
		 * isVisible abajo, devuelve 'true' si el cuerpo pertenece al grupo
		 * seleccionado). Para cada cuerpo, debes dibujar los vectores de velocidad y
		 * fuerza si _showVectors es 'true'. Usa el método drawLineWithArrow para
		 * dibujar los vectores. El color del cuerpo 'b' debe ser
		 * _gColor.get(b.getgId()) -- ver el método addGroup. Como punto de origen usar
		 * (_centerX,_centerY), y recordar dividir las coordenadas del cuerpo por el
		 * valor de _scale.
		 * 
		 */
		for(Body b: _bodies) {//HAY QUE PREGUNTARLE A LA PROFE POR ESTE MÉTODO EN EL LABORATORIO
			int x=(int) (_centerX + (b.getPosition().getX())/_scale);
			int y= (int)(_centerY - (b.getPosition().getY())/_scale);
			if(isVisible(b)) {
				if(_showVectors==true) {
					int xx=(int) (x + (b.getVelocity().direction().scale(15).getX()));
					int yy=(int) (y - (b.getVelocity().direction().scale(15).getY()));
					drawLineWithArrow(g,x,y,xx,yy, 6,4,Color.GREEN,Color.GREEN);
					xx=(int)(x+(b.getForce().direction().scale(15).getX()));
					yy=(int)(y-(b.getForce().direction().scale(15).getY()));
					drawLineWithArrow(g,x,y,xx,yy, 5,4,Color.RED,Color.RED);
				}
				g.setColor(_gColor.get(b.getgId()));
				String id=b.getId();
				g.fillOval(x - 5, y - 5, 2 * 5, 2 * 5);
				g.setColor(Color.BLACK);
				g.drawString(id,x,y-10);
			}
		}
		
	}

	private boolean isVisible(Body b) {
		/*
		 * TODO 
		 * 
		 * EN: return true if _selectedGroup is null or equal to b.getgId() 
		 * 
		 * ES: devuelve true si _selectedGroup es null o igual a b.getgId()
		 *
		 */
		if(_selectedGroup==null||_selectedGroup.equals(b.getgId())) {
			return true;
		}
		return false;
	}

	// calculates a value for scale such that all visible bodies fit in the window
	private void autoScale() {

		double max = 1.0;

		for (Body b : _bodies) {
			Vector2D p = b.getPosition();
			max = Math.max(max, Math.abs(p.getX()));
			max = Math.max(max, Math.abs(p.getY()));
		}

		double size = Math.max(1.0, Math.min(getWidth(), getHeight()));

		_scale = max > size ? 4.0 * max / size : 1.0;
	}

	@Override
	public void addGroup(BodiesGroup g) {
		/*
		 * TODO
		 * 
		 * EN: add g to _groups and its bodies to _bodies
		 *
		 * ES: añadir g a _groups y sus cuerpos a _bodies
		 * 
		 */
		_groups.add(g);
		Iterator<Body> it=g.iterator();
		while(it.hasNext()) {
			_bodies.add(it.next());
		}
		_gColor.put(g.getId(), _colorGen.nextColor()); // assign color to group
		autoScale();
		update();
	}

	@Override
	public void addBody(Body b) {
		/*
		 * TODO
		 * 
		 *  EN: add b to _bodies
		 *  
		 *  ES: añadir b a _bodies
		 *  
		 */
		_bodies.add(b);
		autoScale();
		update();
	}

	@Override
	public void reset() {
		/*
		 * TODO
		 * 
		 * EN: clear the group list, bodies list, and the colors map
		 * 
		 * ES: borrar (usando el método clear) la lista de grupos, la lista de cuerpos y
		 * el mapa de colores
		 * 
		 */
		_bodies.clear();
		_groups.clear();
		_gColor.clear();
		_colorGen.reset(); // reset the color generator
		_selectedGroupIdx = -1;
		_selectedGroup = null;
		update();
	}

	@Override
	void update() {
		repaint();
	}

	// This method draws a line from (x1,y1) to (x2,y2) with an arrow.
	// The arrow is of height h and width w.
	// The last two arguments are the colors of the arrow and the line
	private void drawLineWithArrow(//
			Graphics g, //
			int x1, int y1, //
			int x2, int y2, //
			int w, int h, //
			Color lineColor, Color arrowColor) {

		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(arrowColor);
		g.fillPolygon(xpoints, ypoints, 3);
	}

}
