import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
// import static Matrix.crossProduct;
// import static Matrix.add;
// import static Matrix.sub;
// import static Matrix.dot;
// import static Matrix.multiply;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;



public class Pista  {
    public static void main(String[] args){
        System.out.println("Hola90");
        Pista pista = new Pista();
    }
    
    //Point3D P1 = Point3D(2,0,0)
    //Point3D P2 = Point3D(0,2,0)
    
    public  Pista(){
        Scene scene1 = new Scene();
        Camera camera1 = new Camera(scene1, 45,3.0/4,0.1,100);
        Camera camera2 = new Camera(scene1, 45,3.0/4,0.1,100);
        //                                                float FPS=20;
        //SimpleRender render = new SimpleRender();//scene1,camera1,FPS,
		Cube cube1 = new Cube(Color.red);
		scene1.add(cube1);
		
        //SimpleRender render2 = new SimpleRender(scene1,camera2);//scene1,camera1,FPS,
        
        //camera1.moveTo(P1).lookAt(0,0,0)
        
        //camera1

        //Texture texture_yellow = new SolidColor(Color.yellow)
        //Cube chasis1 = new Cube(1,0.3,1.5, Color.yellow);
        
        System.out.println("Hola1");
        //animation();
		// mainLoop
        
        while (true){
            System.out.println(System.currentTimeMillis());
			try {
				Thread.sleep((long) (1000.0));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			camera1.repaint();
			camera2.repaint();
        }


        //render.render()

    
     
    }

       
}

class Surface{
	public Color c;
	public Matrix points; // Each column is a point, and the rows are the axis x,y,z
	
	public Surface(double[][] points, Color c)
	{
		this(new Matrix(points),  c);
	}
	
	public Surface(Matrix points, Color c)
	{
		this.points = points;
		this.c = c;
		//points.print();
	}
	
	double distance(Matrix Q)
	{
		double total = 0;
		int n=points.getColumns();
		for(int i=0; i<n; i++){
			
			Matrix PQ = Matrix.sub(points.getColumn(i),Q);
			total += Math.sqrt(	Math.pow( PQ.getElement(0,0),2) + 
								Math.pow( PQ.getElement(1,0),2) + 	
								Math.pow( PQ.getElement(2,0),2) ) ;
		}
		return total / points.getColumns();
	}
	

}



class Object3D {
	private static int lastAssignedId = 0;
	
     List<Object3D> children;
     List<Surface> surfaces;
     String name;
     int id;
    private Matrix transformation; 

    public Object3D() {
        this.name = "";
        this.id = ++lastAssignedId;
        this.transformation = new Matrix(new double[][]{{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}});
        //this.rotation = new Matrix(new double[]{0,0,0});
        //this.scale = new Matrix(new double[]{0,0,0});
        this.children = new ArrayList<>();
        this.surfaces = new ArrayList<>();
    }

    // Método para agregar un hijo al objeto
    public void add(Object3D child) {
        children.add(child);
    }

    // Método para obtener un objeto por nombre
    public Object3D getObjectByName(String name) {
        for (Object3D child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    // Método para obtener un objeto por ID
    public Object3D getObjectById(int id) {
        for (Object3D child : children) {
            if (child.getId() == id) {
                return child;
            }
        }
        return null;
    }

    // Método para eliminar un objeto hijo
    public void remove(Object3D child) {
        children.remove(child);
    }

    // Getters y Setters para las propiedades
    public List<Object3D> getChildren() {
		
        return children;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Matrix getTransformation() {
        return transformation;
    }

    public void setTransformation(Matrix transformation) {
        this.transformation = transformation;
    }
	
	
    // Método para obtener todas las superficies de la escena y sus hijos recursivamente
    public List<Surface> getAllSurfaces() {
        List<Surface> allSurfaces = new ArrayList<>();

        // Agregar las superficies de la escena actual
        allSurfaces.addAll(this.surfaces);

        // Recorrer los hijos recursivamente y agregar sus superficies
        for (Object3D child : getChildren()) {
            //if (child instanceof Scene) {
                // Si el hijo es otra escena, llamar al método recursivamente
                //Scene childScene = (Scene) child;
                allSurfaces.addAll(child.getAllSurfaces());
            //} else {
                // Si el hijo no es una escena, simplemente agregar sus superficies
            //    allSurfaces.addAll(child.getSurfaces());
            //}
        }

        return allSurfaces;
    }

}

	
	

class Scene extends Object3D{
	//public Scene(){
	//	super('scene', int id, double[] position)
	//}
}
    

class Cube extends Object3D {
	
	public Cube(Color c){
		super();
		surfaces.add(new Surface(Matrix.transpose(new Matrix(new double[][]{{-1,-1,-1},{-1,-1,1},{-1,1,1},{-1,1,-1}})),c));//x=-1
		surfaces.add(new Surface(Matrix.transpose(new Matrix(new double[][]{{1,-1,-1},{1,1,-1},{1,1,1},{1,-1,1}})),c));//x=1
		surfaces.add(new Surface(Matrix.transpose(new Matrix(new double[][]{{-1,1,-1},{1,1,-1},{1,1,1},{-1,1,1}})),c));//y=1
		surfaces.add(new Surface(Matrix.transpose(new Matrix(new double[][]{{-1,-1,-1},{1,-1,-1},{1,-1,1},{-1,-1,1}})),c));//y=-1
		surfaces.add(new Surface(Matrix.transpose(new Matrix(new double[][]{{-1,-1,-1},{1,-1,-1},{1,1,-1},{-1,1,-1}})),c));//z=-1
		surfaces.add(new Surface(Matrix.transpose(new Matrix(new double[][]{{-1,-1,1},{1,-1,1},{1,1,1},{-1,1,1}})),c));//z=1
		


	}
	

    // private double[][] vertices;
    // private int[][] aristas;

    // public Cube(double d) {
        // // Coordenadas de los vértices del cubo
        // vertices = new double[][] {
                // {d/2, d/2, d/2},
                // {-d/2, d/2, d/2},
                // {-d/2, -d/2, d/2},
                // {d/2, -d/2, d/2},
                // {d/2, d/2, -d/2},
                // {-d/2, d/2, -d/2},
                // {-d/2, -d/2, -d/2},
                // {d/2, -d/2, -d/2}
        // };

        // // Definir las aristas del cubo (conexiones entre vértices)
        // aristas = new int[][] {
                // {0, 1}, {1, 2}, {2, 3}, {3, 0},
                // {4, 5}, {5, 6}, {6, 7}, {7, 4},
                // {0, 4}, {1, 5}, {2, 6}, {3, 7}
        // };
    // }

    // @Override
    // protected void paintComponent(Graphics g) {
        // super.paintComponent(g);

        // int centerX = getWidth() / 2;
        // int centerY = getHeight() / 2;

        // // Proyectar y graficar las aristas
        // for (int[] arista : aristas) {
            // int x1 = (int) (vertices[arista[0]][0] + centerX);
            // int y1 = (int) (-vertices[arista[0]][1] + centerY);

            // int x2 = (int) (vertices[arista[1]][0] + centerX);
            // int y2 = (int) (-vertices[arista[1]][1] + centerY);

            // g.drawLine(x1, y1, x2, y2);
        // }
    // }

    // public static void main(String[] args) {
        // double d = 100.0; // Longitud de la arista
        // CuboAristas panel = new CuboAristas(d);

        // JFrame frame = new JFrame("Aristas de un Cubo");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(400, 400);
        // frame.setContentPane(panel);
        // frame.setVisible(true);
    // }
}

class Camera extends JPanel {
    private double fov;
    private double aspectRatio;
    private double near;
    private double far;
	private JFrame f;
	private Matrix ViewFrom;
	private Matrix ViewTo;
	static double zoom = 1000;
	private static double t ;
	//static Matrix W1, W2, ViewVector, RotationVector, PlaneVector1, PlaneVector2;
	//static Plane P;
	private static Matrix CalcFocusPos; //= new double[2];
	private final static Matrix DirectionVector = new Matrix(new double[]{1, 1, 1});
	static Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
	private Matrix screenSize = new Matrix( new double[] {screenDim.getWidth()/2, screenDim.getHeight()/2});
	Scene scene;


	public Matrix calcPos(Matrix ViewFrom, Matrix ViewTo, Matrix X){
		Matrix ViewVector = Matrix.sub(ViewTo, ViewFrom);
		Matrix PlaneVector1 = Matrix.crossProduct(ViewVector,DirectionVector);
		Matrix PlaneVector2 = Matrix.crossProduct(ViewVector,PlaneVector1);
		//P = new Plane(PlaneVector1, PlaneVector2, Screen.ViewTo);
		Matrix RotationVector = getRotationVector(ViewFrom, ViewTo);
		Matrix W1 = Matrix.crossProduct(ViewVector, RotationVector);
		Matrix W2 = Matrix.crossProduct(ViewVector, W1);
	//	CalcFocusPos = Calculator.CalculatePositionP(Screen.ViewFrom, Screen.ViewTo, Screen.ViewTo[0], Screen.ViewTo[1], Screen.ViewTo[2]);
		Matrix ViewToPoint = Matrix.sub(X, ViewFrom);
		Matrix V1 = PlaneVector1;
		Matrix V2 = PlaneVector2;
		Matrix NV = Matrix.crossProduct(V1,V2);
		Matrix P = ViewTo;
		
		double t =  (Matrix.dot(NV,P) - Matrix.dot(NV,ViewFrom))/Matrix.dot(NV,ViewToPoint);
		double proy2D_0 = zoom * Matrix.dot(Matrix.add(ViewFrom, Matrix.multiply(t, ViewToPoint)),W2);
		double proy2D_1 = zoom * Matrix.dot(Matrix.add(ViewFrom, Matrix.multiply(t, ViewToPoint)),W1);
		return new Matrix(new double[]{proy2D_0,proy2D_1});
	}

	public Matrix calcProy( Matrix X){//Matrix ViewFrom, Matrix ViewTo,

		Matrix CalcFocusPos = calcPos(ViewFrom, ViewTo, ViewTo);	
		Matrix CalcPos = calcPos(ViewFrom, ViewTo, X);
		Matrix X_proy = Matrix.add(Matrix.sub(screenSize,  CalcFocusPos),Matrix.multiply(zoom,CalcPos));
		return X_proy;
	}

	static Matrix getRotationVector(Matrix ViewFrom, Matrix ViewTo)
	{
		Matrix dif = Matrix.sub(ViewFrom, ViewTo);
		double dx = Math.abs(dif.getElement(0,0));
		double dy = Math.abs(dif.getElement(1,0));
		double xRot, yRot;
		xRot=dy/(dx+dy);		
		yRot=dx/(dx+dy);

		if(ViewFrom.getElement(1,0)>ViewTo.getElement(1,0))
			xRot = -xRot;
		if(ViewFrom.getElement(0,0)<ViewTo.getElement(0,0))
			yRot = -yRot;

			Matrix V = new Matrix(new double[]{xRot, yRot, 0});
		return V;
	}

	public Camera(Scene scene, double fov, double aspect, double near, double far)
	{
		this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
		this.scene = scene;
 		
		
		JFrame f = new JFrame();
		f.add(this);
		//F.setUndecorated(true);
	    f.setSize(640,480);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
		public void paintComponent(Graphics g)
	{
		//Clear screen and draw background color
		g.setColor(new Color(140, 180, 180));
		//g.fillRect(0, 0, (int)DDDTutorial.ScreenSize.getWidth(), (int)DDDTutorial.ScreenSize.getHeight());

		//CameraMovement();
		
		//Calculated all that is general for this camera position
		//Calculator.SetPrederterminedInfo();



		List<Surface> surfaces = scene.getAllSurfaces();
		//System.out.println("Paint");
		
		for (Surface surface : surfaces){
			//System.out.println(surface.c);
			int[][] arrays=r3toPixels(surface.points);
			g.setColor(surface.c);
			g.fillPolygon(arrays[0],arrays[1],surface.points.getColumns()); 
			






	// void drawPolygon(Graphics g)
	// {
		// if(draw && visible)
		// {
			// g.setColor(new Color((int)(c.getRed() * lighting), (int)(c.getGreen() * lighting), (int)(c.getBlue() * lighting)));
			// if(seeThrough)
				// g.drawPolygon(P);
			// else
				// g.fillPolygon(P);
			// if(Screen.OutLines)
			// {
				// g.setColor(new Color(0, 0, 0));
				// g.drawPolygon(P);
			// }

			// if(Screen.PolygonOver == this)
			// {
				// g.setColor(new Color(255, 255, 255, 100));
				// g.fillPolygon(P);
			// }
		// }
	// }
			
		}
		




		// //Updates each polygon for this camera position
		// for(int i = 0; i < DPolygons.size(); i++)
			// DPolygons.get(i).updatePolygon();
		
		// //rotate and update shape examples
		// Cubes.get(0).rotation+=.01;
		// Cubes.get(0).updatePoly();

		// Prisms.get(0).rotation+=.01;
		// Prisms.get(0).updatePoly();
		
		// Pyramids.get(0).rotation+=.01;
		// Pyramids.get(0).updatePoly();

		// //Set drawing order so closest polygons gets drawn last
		// setOrder();
			
		// //Set the polygon that the mouse is currently over
		// setPolygonOver();
			
		// //draw polygons in the Order that is set by the 'setOrder' function
		// for(int i = 0; i < NewOrder.length; i++)
			// DPolygons.get(NewOrder[i]).DrawablePolygon.drawPolygon(g);
			
		// //draw the cross in the center of the screen
		// drawMouseAim(g);			
		
		// //FPS display
		// g.drawString("FPS: " + (int)drawFPS + " (Benchmark)", 40, 40);
		
// //		repaintTime = System.currentTimeMillis() - repaintTime; 
// //		System.out.println(repaintTime);
		// SleepAndRefresh();
	}
	
//	Matrix elim_z(Matrix v){
//		return new Matrix(double[] {v.getElement(0,0), v.getElement(1,0)})
//	}


		public int[][] r3toPixels(Matrix matrix){
			matrix.print();
			System.out.println();
			int rows = 2;//matrix.getRows();
			int columns = matrix.getColumns();
			int[][] result=new int[rows][columns] ;
			for (int j = 0; j <columns; j++) {
				Matrix m2D= matrix.getColumn(j);//calcProy( matrix.getColumn(j));
				for (int i = 0; i < rows; i++) {
					result[i][j] = (int)(m2D.getElement(i,0)*10+100);
				}
				//System.out.println();
			}
			return result;
		}

	
}

// class PerspectiveCamera extends Camera {
	// public PerspectiveCamera(double fov, double aspect, double near, double far){
		// super( fov,  aspect,  near,  far);
	// }
// }

// class SimpleRender{
	// static void render(Scene scene, Camera camera){
		
	// }
// }


 