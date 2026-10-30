/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.circuitosimulador;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.awt.Graphics2D;
import java.awt.BasicStroke;


public class lienzocircuito extends JPanel {

    private String herramienta = "";
    private final List<Point> resistencias = new ArrayList<>();
    private final List<Double> valoresresistencias = new ArrayList();
    private final List<Boolean> resistenciasvertical = new ArrayList();
    private final List<String> nombresresistencias = new ArrayList<>();
    private final List<Point[]> cables = new ArrayList<>();
    private final List<Point> baterias = new ArrayList<>();
    private final List<Point> nodos = new ArrayList<>();
    private Point  mouseActual = null;
    private Point inicioCable = null;
    private int resistenciaseleccionada = -1;
    private Point desplazamiento = null;
    private final List<Boolean> bateriavertical = new ArrayList<>();

    public lienzocircuito() {
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
  public void mousePressed(MouseEvent e) {
 resistenciaseleccionada = -1;
    desplazamiento = null;

    // MODO BORRAR
    if (herramienta.equals("Borrar")) {

        // Borrar resistencia
        for (int i = resistencias.size() - 1; i >= 0; i--) {
            if (resistencias.get(i).distance(e.getPoint()) <= 35) {
                resistencias.remove(i);
                valoresresistencias.remove(i);
                resistenciasvertical.remove(i);
                nombresresistencias.remove(i);
                repaint();
                return;
            }
        }

        // Borrar cable
        for (int i = cables.size() - 1; i >= 0; i--) {
            Point[] cable = cables.get(i);

            if (distancia(e.getPoint(), cable[0], cable[1]) <= 10) {
                cables.remove(i);
                repaint();
                return;
            }
        }

        // Borrar batería
        for (int i = baterias.size() - 1; i >= 0; i--) {
            if (baterias.get(i).distance(e.getPoint()) <= 30) {
                baterias.remove(i);
                bateriavertical.remove(i);
                repaint();
                return;
            }
        }

        return;
    }

    // MODO DIBUJAR CABLE
    if (herramienta.equals("Cable")) {
        return;
    }

    // SELECCIONAR Y MOVER RESISTENCIA
    for (int i = resistencias.size() - 1; i >= 0; i--) {
        Point p = resistencias.get(i);

        if (p.distance(e.getPoint()) <= 35) {
            resistenciaseleccionada = i;

            desplazamiento = new Point(
                e.getX() - p.x,
                e.getY() - p.y
            );

            return;
        }
    }
    }

            
            @Override
            public void mouseReleased(MouseEvent e) {
          resistenciaseleccionada = -1;
    desplazamiento = null;
    repaint();
}   

            @Override
            public void mouseClicked(MouseEvent e) {         
    if (herramienta.equals("Cable")) {
    Point terminal = terminalmascercano(e.getPoint());

    if (terminal == null) {
        return;
    }

    if (inicioCable == null) {
        inicioCable = terminal;
        repaint();
        return;
    }

    Point inicio = inicioCable;
    Point fin = terminal;

   if (!inicio.equals(fin)) {
    int margen = 50;

    if (inicio.x != fin.x && inicio.y != fin.y) {
        // Recorrido vertical y después horizontal
        Point esquina = new Point(inicio.x, fin.y);

        cables.add(new Point[]{inicio, esquina});
        cables.add(new Point[]{esquina, fin});
    } else {
        // Si están alineados, una línea recta
        cables.add(new Point[]{inicio, fin});
    }
}
    inicioCable = null;
    repaint();
    return;
}
   for (int i = 0; i < resistencias.size(); i++) {
    Point p = resistencias.get(i);

    if (p.distance(e.getPoint()) <= 30) {
        resistenciasvertical.set(
            i, !resistenciasvertical.get(i)
        );
        repaint();
        return;
    }
}
   for (int i = 0; i < baterias.size(); i++) {
    Point p = baterias.get(i);

    if (p.distance(e.getPoint()) <= 30) {
        bateriavertical.set(i, !bateriavertical.get(i));
        repaint();
        return;
    }
}
      if (herramienta.equals("Resistencia")){
        String entrada = javax.swing.JOptionPane.showInputDialog(null, "Ingresa el valor de la resistencia:");
                    
        if (entrada != null ){
        try{
        double valor = Double.parseDouble(entrada.trim().replace(',', '.'));
                        
        if (!Double.isFinite(valor) || valor <= 0){
        javax.swing.JOptionPane.showMessageDialog(null, "La resistencia debe ser mayor a 0");
        return;
        }               
                resistencias.add(e.getPoint());
                resistenciasvertical.add(false);
                valoresresistencias.add(valor);
                nombresresistencias.add("R" + resistencias.size());
                repaint();
                               
                    } catch (NumberFormatException ex){
                        javax.swing.JOptionPane.showMessageDialog(null, "Ingresa un numero valido ");                        
                    }
                }
               } else if (herramienta.equals("Bateria")) {
    baterias.add(e.getPoint());
    bateriavertical.add(false);
    repaint();
} else if (herramienta.equals("Nodo")) {
    Point punto = e.getPoint();

    // Evitar nodos duplicados en el mismo lugar
    boolean existe = false;

    for (Point nodo : nodos) {
        if (nodo.distance(punto) < 10) {
            existe = true;
            break;
        }
    }

    if (!existe) {
        nodos.add(punto);
    }

    repaint();
}
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                    if (herramienta.equals("Cable")) {
        return;
    }

    if (resistenciaseleccionada != -1
            && desplazamiento != null) {

        Point nuevaPosicion = new Point(
            e.getX() - desplazamiento.x,
            e.getY() - desplazamiento.y
        );

        resistencias.set(
            resistenciaseleccionada,
            nuevaPosicion
        );

        repaint();
    }
            }
        });
    }

    public void activarResistencia() {
        herramienta = "Resistencia";
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void activarCable() {
    herramienta = "Cable";
    inicioCable = null;
    mouseActual = null;
    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
}

    public void activarBateria() {
        herramienta = "Bateria";
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    public void activarNodo() {
    herramienta = "Nodo";
    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
}

    @Override
    protected void paintComponent(Graphics g) {
         super.paintComponent(g);

Graphics2D g2 = (Graphics2D) g.create();

g2.setRenderingHint(
    java.awt.RenderingHints.KEY_ANTIALIASING,
    java.awt.RenderingHints.VALUE_ANTIALIAS_ON
);

// Cuadrícula suave
g2.setColor(new Color(232, 238, 245));

for (int x = 0; x < getWidth(); x += 20) {
    g2.drawLine(x, 0, x, getHeight());
}

for (int y = 0; y < getHeight(); y += 20) {
    g2.drawLine(0, y, getWidth(), y);
}

// Cables más definidos
g2.setColor(new Color(30, 41, 59));
g2.setStroke(new BasicStroke(
    3.0f,
    BasicStroke.CAP_ROUND,
    BasicStroke.JOIN_ROUND
));

for (Point[] cable : cables) {
    g2.drawLine(
        cable[0].x, cable[0].y,
        cable[1].x, cable[1].y
    );
}

g2.dispose();

       for (int i = 0; i < resistencias.size(); i++) {
    Point p = resistencias.get(i);

    Graphics2D r2 = (Graphics2D) g.create();
    r2.setRenderingHint(
        java.awt.RenderingHints.KEY_ANTIALIASING,
        java.awt.RenderingHints.VALUE_ANTIALIAS_ON
    );

    // Terminales y cuerpo de la resistencia
    r2.setStroke(new BasicStroke(
        2.2f,
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND
    ));

    r2.setColor(new Color(55, 65, 81));

    if (resistenciasvertical.get(i)) {
        // Terminal superior e inferior
        r2.drawLine(p.x, p.y - 40, p.x, p.y - 25);
        r2.drawLine(p.x, p.y + 25, p.x, p.y + 40);

        // Cuerpo vertical
        r2.setColor(new Color(219, 234, 254));
        r2.fillRoundRect(p.x - 12, p.y - 25, 24, 50, 8, 8);

        r2.setColor(new Color(37, 99, 235));
        r2.drawRoundRect(p.x - 12, p.y - 25, 24, 50, 8, 8);

    } else {
        // Terminal izquierdo y derecho
        r2.drawLine(p.x - 40, p.y, p.x - 25, p.y);
        r2.drawLine(p.x + 25, p.y, p.x + 40, p.y);

        // Cuerpo horizontal
        r2.setColor(new Color(219, 234, 254));
        r2.fillRoundRect(p.x - 25, p.y - 12, 50, 24, 8, 8);

        r2.setColor(new Color(37, 99, 235));
        r2.drawRoundRect(p.x - 25, p.y - 12, 50, 24, 8, 8);
    }

    // Nombre y valor
    r2.setColor(new Color(30, 41, 59));
    r2.setFont(new java.awt.Font(
        "Segoe UI", java.awt.Font.BOLD, 12
    ));

    String etiqueta = "R" + (i + 1) + " (" 
        + valoresresistencias.get(i) + " Ω)";

    if (resistenciasvertical.get(i)) {
        r2.drawString(etiqueta, p.x + 18, p.y + 4);
    } else {
        r2.drawString(etiqueta, p.x - 25, p.y - 19);
    }

    r2.dispose();
}


for (int i = 0; i < baterias.size(); i++) {
    Point p = baterias.get(i);

    g.setColor(new Color(37, 99, 235));

    if (!bateriavertical.get(i)) {
        // Batería horizontal
        g.drawLine(p.x - 25, p.y, p.x - 8, p.y);

        g.setColor(new Color(219, 234, 254));
        g.fillRect(p.x - 8, p.y - 15, 5, 30);
        g.setColor(new Color(37, 99, 235));
        g.drawRect(p.x - 8, p.y - 15, 5, 30);

        g.setColor(new Color(219, 234, 254));
        g.fillRect(p.x + 2, p.y - 8, 5, 16);
        g.setColor(new Color(37, 99, 235));
        g.drawRect(p.x + 2, p.y - 8, 5, 16);

        g.drawLine(p.x + 7, p.y, p.x + 25, p.y);
    } else {
        // Batería vertical
        g.drawLine(p.x, p.y - 25, p.x, p.y - 8);

        g.setColor(new Color(219, 234, 254));
        g.fillRect(p.x - 15, p.y - 8, 30, 5);
        g.setColor(new Color(37, 99, 235));
        g.drawRect(p.x - 15, p.y - 8, 30, 5);

        g.setColor(new Color(219, 234, 254));
        g.fillRect(p.x - 8, p.y + 2, 16, 5);
        g.setColor(new Color(37, 99, 235));
        g.drawRect(p.x - 8, p.y + 2, 16, 5);

        g.drawLine(p.x, p.y + 7, p.x, p.y + 25);
    }

    g.setColor(Color.BLACK);
    g.drawString("V" + (i + 1), p.x - 8, p.y + 30);
}
        

    }
    
    public List<Double> obtenervaloresresistencias(){ //obtener los valores de las resistencias
        return new ArrayList<>(valoresresistencias);
    }
    
public List<Point[]> obtenerTerminalesResistencias() { //obtener la terminal de la resistencia mas cercana 
    List<Point[]> terminales = new ArrayList<>();

    for (int i = 0; i < resistencias.size(); i++) {
        Point r = resistencias.get(i);
        Point a;
        Point b;

        if (resistenciasvertical.get(i)) {
            a = new Point(r.x, r.y - 40);
            b = new Point(r.x, r.y + 40);
        } else {
            a = new Point(r.x - 40, r.y);
            b = new Point(r.x + 40, r.y);
        }

        terminales.add(new Point[]{a, b});
    }

    return terminales;
}
public String resolverMixtoAutomatico(double voltaje) {
    List<Point[]> terminales = obtenerTerminalesResistencias();
    List<Double> valores = obtenervaloresresistencias();

    if (terminales.size() != valores.size() || valores.isEmpty()) {
        return "Error: no hay suficientes datos de las resistencias.";
    }

    List<Point[]> cablesActuales = obtenerCables();

    List<Point> puntos = new ArrayList<>();

    for (Point[] terminal : terminales) {
        puntos.add(terminal[0]);
        puntos.add(terminal[1]);
    }

    Point positivo = baterias.get(0);
    Point negativo = baterias.get(0);

    if (bateriavertical.get(0)) {
        positivo = new Point(positivo.x, positivo.y - 25);
        negativo = new Point(negativo.x, negativo.y + 25);
    } else {
        positivo = new Point(positivo.x - 25, positivo.y);
        negativo = new Point(negativo.x + 25, negativo.y);
    }

    puntos.add(positivo);
    puntos.add(negativo);

    int total = puntos.size();
    int[] padre = new int[total];

    for (int i = 0; i < total; i++) {
        padre[i] = i;
    }

    for (int i = 0; i < total; i++) {
        for (int j = i + 1; j < total; j++) {
            if (conectados(puntos.get(i), puntos.get(j))) {
                int a = i;
                while (padre[a] != a) {
                    a = padre[a];
                }

                int b = j;
                while (padre[b] != b) {
                    b = padre[b];
                }

                if (a != b) {
                    padre[b] = a;
                }
            }
        }
    }

    int[] nodos = new int[total];
    int cantidadNodos = 0;

    for (int i = 0; i < total; i++) {
        int raiz = i;
        while (padre[raiz] != raiz) {
            raiz = padre[raiz];
        }

        int encontrado = -1;

        for (int j = 0; j < i; j++) {
            int otraRaiz = j;
            while (padre[otraRaiz] != otraRaiz) {
                otraRaiz = padre[otraRaiz];
            }

            if (otraRaiz == raiz) {
                encontrado = nodos[j];
                break;
            }
        }

        if (encontrado == -1) {
            nodos[i] = cantidadNodos++;
        } else {
            nodos[i] = encontrado;
        }
    }

    int nodoPositivo = nodos[total - 2];
    int nodoNegativo = nodos[total - 1];

    double[][] red = new double[valores.size()][3];

    for (int i = 0; i < valores.size(); i++) {
        red[i][0] = nodos[i * 2];
        red[i][1] = nodos[i * 2 + 1];
        red[i][2] = valores.get(i);
    }

    if (nodoPositivo == nodoNegativo) {
        return "Error: la batería está en cortocircuito.";
    }

    return calculadoracircuito.resolvermixto(
        voltaje, red, cantidadNodos, nodoPositivo, nodoNegativo
    );
}

private Point terminalmascercano(Point punto) {
    Point mejor = null;
    double distanciaMinima = 100.0;

    for (int i = 0; i < resistencias.size(); i++) {
        Point r = resistencias.get(i);
        Point a;
        Point b;

        if (resistenciasvertical.get(i)) {
            // Terminales de resistencia vertical
            a = new Point(r.x, r.y - 40);
            b = new Point(r.x, r.y + 40);
        } else {
            // Terminales de resistencia horizontal
            a = new Point(r.x - 40, r.y);
            b = new Point(r.x + 40, r.y);
        }

        double da = punto.distance(a);
        double db = punto.distance(b);

        if (da < distanciaMinima) {
            distanciaMinima = da;
            mejor = a;
        }

        if (db < distanciaMinima) {
            distanciaMinima = db;
            mejor = b;
        }
    }

    for (int i = 0; i < baterias.size(); i++) {
    Point bateria = baterias.get(i);

    Point a;
    Point b;

    if (!bateriavertical.get(i)) {
        // Terminales de batería horizontal
        a = new Point(bateria.x - 25, bateria.y);
        b = new Point(bateria.x + 25, bateria.y);
    } else {
        // Terminales de batería vertical
        a = new Point(bateria.x, bateria.y - 25);
        b = new Point(bateria.x, bateria.y + 25);
    }

    double da = punto.distance(a);
    double db = punto.distance(b);

    if (da < distanciaMinima) {
        distanciaMinima = da;
        mejor = a;
    }

    if (db < distanciaMinima) {
        distanciaMinima = db;
        mejor = b;
    }
}
        for (Point[] cable : cables) {
    Point a = cable[0];
    Point b = cable[1];

    double distancia = distancia(punto, a, b);

    if (distancia < distanciaMinima) {
        distanciaMinima = distancia;
        mejor = puntocerca(punto, a, b);
    }
}

    return mejor;
}
        

public boolean conectados(Point a, Point b) {
    if (a.distance(b) < 10) {
        return true;
    }

    List<Point> pendientes = new ArrayList<>();
    List<Point> visitados = new ArrayList<>();

    pendientes.add(new Point(a));

    while (!pendientes.isEmpty()) {
        Point actual = pendientes.remove(0);

        boolean yaVisitado = false;
        for (Point visitado : visitados) {
            if (visitado.distance(actual) < 10) {
                yaVisitado = true;
                break;
            }
        }

        if (yaVisitado) {
            continue;
        }

        visitados.add(actual);

        if (actual.distance(b) < 10) {
            return true;
        }

        for (Point[] cable : cables) {
            Point inicio = cable[0];
            Point fin = cable[1];

            // Recorrer el cable si llegamos a cualquiera de sus extremos.
            if (actual.distance(inicio) < 10) {
                pendientes.add(new Point(fin));
            }

            if (actual.distance(fin) < 10) {
                pendientes.add(new Point(inicio));
            }

            // Reconocer si el punto actual está sobre el segmento.
            if (distancia(actual, inicio, fin) < 5) {
                pendientes.add(new Point(inicio));
                pendientes.add(new Point(fin));
            }
        }
    }

    return false;
}   

   public String detectarcircuito() {
    int n = resistencias.size();

    if (n < 2) {
        return "Circuito insuficiente";
    }

    int conexionesSerie = 0;
    int conexionesParalelo = 0;

    for (int i = 0; i < n; i++) {
        Point terminalAI;
        Point terminalBI;

        Point ri = resistencias.get(i);

        if (resistenciasvertical.get(i)) {
            terminalAI = new Point(ri.x, ri.y - 40);
            terminalBI = new Point(ri.x, ri.y + 40);
        } else {
            terminalAI = new Point(ri.x - 40, ri.y);
            terminalBI = new Point(ri.x + 40, ri.y);
        }

        for (int j = i + 1; j < n; j++) {
            Point terminalAJ;
            Point terminalBJ;

            Point rj = resistencias.get(j);

            if (resistenciasvertical.get(j)) {
                terminalAJ = new Point(rj.x, rj.y - 40);
                terminalBJ = new Point(rj.x, rj.y + 40);
            } else {
                terminalAJ = new Point(rj.x - 40, rj.y);
                terminalBJ = new Point(rj.x + 40, rj.y);
            }

            boolean serie =
                    conectados(terminalAI, terminalAJ)
                    || conectados(terminalAI, terminalBJ)
                    || conectados(terminalBI, terminalAJ)
                    || conectados(terminalBI, terminalBJ);

            if (serie) {
                conexionesSerie++;
            }

            boolean paralelo =
                    (conectados(terminalAI, terminalAJ)
                    && conectados(terminalBI, terminalBJ))
                    || (conectados(terminalAI, terminalBJ)
                    && conectados(terminalBI, terminalAJ));

            if (paralelo) {
                conexionesParalelo++;
            }
        }
    }
    int totalPares = n * (n - 1) / 2;

if (conexionesParalelo == totalPares) {
    return "Paralelo";
}

    if (conexionesParalelo > 0 && conexionesSerie > 0) {
        return "Mixto";
    }

    if (conexionesParalelo > 0) {
        return "Paralelo";
    }

    if (conexionesSerie > 0) {
        return "Serie";
    }

    return "Conexiones no reconocidas";
}
   
public List<Point[]> obtenerCables() {
    List<Point[]> copia = new ArrayList<>();

    for (Point[] cable : cables) {
        copia.add(new Point[]{
            new Point(cable[0]),
            new Point(cable[1])
        });
    }

    return copia;
}

public boolean siConectados(Point a, Point b) {
    return conectados(a, b);
}

   private double distancia(Point p, Point a, Point b) {
    double dx = b.x - a.x;
    double dy = b.y - a.y;

    if (dx == 0 && dy == 0) {
        return p.distance(a);
    }

    double t = ((p.x - a.x) * dx + (p.y - a.y) * dy)
            / (dx * dx + dy * dy);

    t = Math.max(0, Math.min(1, t));

    double x = a.x + t * dx;
    double y = a.y + t * dy;

    return p.distance(x, y);
}

private Point puntocerca(Point p, Point a, Point b) {
    double dx = b.x - a.x;
    double dy = b.y - a.y;

    if (dx == 0 && dy == 0) {
        return new Point(a);
    }

    double t = ((p.x - a.x) * dx + (p.y - a.y) * dy)
            / (dx * dx + dy * dy);

    t = Math.max(0, Math.min(1, t));

    int x = (int) Math.round(a.x + t * dx);
    int y = (int) Math.round(a.y + t * dy);

    return new Point(x, y);
} 

public void borrarcircuito() {
    resistencias.clear();
    valoresresistencias.clear();
    resistenciasvertical.clear();
    nombresresistencias.clear();
    cables.clear();
    baterias.clear();
    bateriavertical.clear();
    nodos.clear();

    resistenciaseleccionada = -1;
    inicioCable = null;
    mouseActual = null;
    desplazamiento = null;
    herramienta = "";

    repaint();
}
public void activarborrar() {
    herramienta = "Borrar";
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}

}
