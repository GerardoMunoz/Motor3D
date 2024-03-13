// Por_hacer
//Carro-pista
//Dinámica_de_cuerpo_rígido-Esfera


import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics;

public class Pista {
  public static void main(String[] args) {
    System.out.println("Pista");
    Pista pista = new Pista();
  }

  public Pista() {

    Scene scene1 = new Scene();
    Camera camera_xy = new CamerMatrtProj(scene1, new double[][]{{10,0,0},{0,10,0}},new double[] {200,100}  );
    Camera camera_xz = new CamerMatrtProj(scene1, new double[][]{{10,0,0},{0,0,10}},new double[] {200,100}  );
    Cube cube1 = new Cube(Color.red);
    cube1.setTransformation(Matrix.multiply(10, Matrix.id(3)));
    scene1.add(cube1);

    Matrix Rz1deg = Matrix.Rz((float) (1 * Math.PI / 180));
    double t_ant,t_act;
    t_ant =System.currentTimeMillis();
    
    while (true) {
      t_act=System.currentTimeMillis();
      //System.out.println((t_act - t_ant));
      t_ant = t_act;
      try {
		  
        Thread.sleep((long) (50.0));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      camera_xy.repaint();
      camera_xz.repaint();

      cube1.setTransformation(Matrix.multiply(Rz1deg, cube1.getTransformation()));
    }

  }

}

class Surface {
  public Color c;
  public Matrix points; // Each column is a point, and the rows are the axis x,y,z

  public Surface(double[][] points, Color c) {
    this(new Matrix(points), c);
  }

  public Surface(Matrix points, Color c) {
    this.points = points;
    this.c = c;
  }


  double distance(Matrix Q) {
    double total = 0;
    int n = points.getColumns();
    for (int i = 0; i < n; i++) {

      Matrix PQ = Matrix.sub(points.getColumn(i), Q);
      total += Math.sqrt(Math.pow(PQ.getElement(0, 0), 2) +
          Math.pow(PQ.getElement(1, 0), 2) +
          Math.pow(PQ.getElement(2, 0), 2));
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
    this.transformation = Matrix.id(3);
    this.children = new ArrayList<>();
    this.surfaces = new ArrayList<>();
  }

  public void add(Object3D child) {
    children.add(child);
  }

  public Object3D getObjectByName(String name) {
    for (Object3D child : children) {
      if (child.getName().equals(name)) {
        return child;
      }
    }
    return null;
  }

  public Object3D getObjectById(int id) {
    for (Object3D child : children) {
      if (child.getId() == id) {
        return child;
      }
    }
    return null;
  }

  public void remove(Object3D child) {
    children.remove(child);
  }

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


  public void graphAll2DPolig(Matrix transformation, Graphics g, Camera cam) {

    for (Surface surface : surfaces) {
      Matrix tranf_points = Matrix.multiply(transformation, Matrix.multiply(this.transformation, surface.points));
      int[][] points2D = cam.render(tranf_points);
      g.setColor(surface.c);
      g.fillPolygon(points2D[0], points2D[1], points2D[0].length);
    }

    for (Object3D child : getChildren()) {
      child.graphAll2DPolig(Matrix.multiply(transformation, this.transformation), g, cam);
    };
  }


}

class Scene extends Object3D {
}

class Cube extends Object3D {

  public Cube(Color c) {
    super();
    surfaces.add(new Surface(
    Matrix.transpose(new Matrix(new double[][] { { -1, -1, -1 }, { -1, -1, 1 }, {
    -1, 1, 1 }, { -1, 1, -1 } })),c));// x=-1
    surfaces.add(new Surface(
    Matrix.transpose(new Matrix(new double[][] { { 1, -1, -1 }, { 1, 1, -1 }, {
    1, 1, 1 }, { 1, -1, 1 } })), c));// x=1
    surfaces.add(new Surface(
    Matrix.transpose(new Matrix(new double[][] { { -1, 1, -1 }, { 1, 1, -1 }, {
    1, 1, 1 }, { -1, 1, 1 } })), c));// y=1
    surfaces.add(new Surface(
    Matrix.transpose(new Matrix(new double[][] { { -1, -1, -1 }, { 1, -1, -1 }, {
    1, -1, 1 }, { -1, -1, 1 } })),c));// y=-1
    surfaces.add(new Surface(
    Matrix.transpose(new Matrix(new double[][] { { -1, -1, -1 }, { 1, -1, -1 }, {
    1, 1, -1 }, { -1, 1, -1 } })),c));// z=-1
    surfaces.add(new Surface(
        Matrix.transpose(new Matrix(new double[][] { { -1, -1, 1 }, { 1, -1, 1 }, { 1, 1, 1 }, { -1, 1, 1 } })), c));// z=1

  }

}

abstract class Camera extends JPanel {
  private JFrame f;
  Scene scene;

 

  public  Camera(Scene scene){
     this.scene = scene;

    JFrame f = new JFrame();
    f.add(this);
    // F.setUndecorated(true);
    f.setSize(640, 480);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(new Color(140, 180, 180));
    // g.fillRect(0, 0, (int)ScreenSize.getWidth(),

    scene.graphAll2DPolig(Matrix.id(3), g, this);
  }

  public abstract int[][] render(Matrix matrix);
}


class CamerMatrtProj extends Camera {

  Matrix matrProj;
  double[] translation;
	
  public  CamerMatrtProj(Scene scene, double[][] matrProj, double[] translation){
	  super(scene);
	  this.matrProj=new Matrix(matrProj);
	  this.translation = translation;
  }
  public int[][] render(Matrix points3D) {
	Matrix points2D=Matrix.multiply( matrProj, points3D);
    int rows = 2;// points2D.getRows();
    int columns = points2D.getColumns();
    int[][] result = new int[rows][columns];
    for (int j = 0; j < columns; j++) {
      Matrix m2D =  points2D.getColumn(j);
      for (int i = 0; i < rows; i++) {
        result[i][j] = (int) (m2D.getElement(i, 0)  + translation[i]);
      }
    }
    return result;
  }

}

