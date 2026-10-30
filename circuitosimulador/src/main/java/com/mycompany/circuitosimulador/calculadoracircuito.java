/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.circuitosimulador;

/**
 *
 * @author maryo
 */
public class calculadoracircuito {
    
public static double calcularResistenciaEquivalente(
        double[][] resistenciasEntreNodos,
        int cantidadNodos,
        int nodoPositivo,
        int nodoNegativo) {

    if (resistenciasEntreNodos == null
            || cantidadNodos < 2
            || nodoPositivo < 0
            || nodoNegativo < 0
            || nodoPositivo >= cantidadNodos
            || nodoNegativo >= cantidadNodos
            || nodoPositivo == nodoNegativo) {
        throw new IllegalArgumentException("Nodos no válidos.");
    }

    double[][] matriz = new double[cantidadNodos][cantidadNodos];
    double[] vector = new double[cantidadNodos];

    // El nodo negativo se toma como referencia: voltaje cero.
    // Se inyecta 1 A en el nodo positivo para obtener la resistencia equivalente.
    for (int i = 0; i < resistenciasEntreNodos.length; i++) {
        double[] r = resistenciasEntreNodos[i];

        if (r == null || r.length != 3) {
            throw new IllegalArgumentException(
                    "Cada resistencia debe indicar nodo inicial, nodo final y ohmios.");
        }

        int a = (int) r[0];
        int b = (int) r[1];
        double ohmios = r[2];

        if (a < 0 || a >= cantidadNodos
                || b < 0 || b >= cantidadNodos
                || a == b || !Double.isFinite(ohmios) || ohmios <= 0) {
            throw new IllegalArgumentException("Conexión o resistencia no válida.");
        }

        double conductancia = 1.0 / ohmios;

        if (a != nodoNegativo) {
            matriz[a][a] += conductancia;
        }
        if (b != nodoNegativo) {
            matriz[b][b] += conductancia;
        }
        if (a != nodoNegativo && b != nodoNegativo) {
            matriz[a][b] -= conductancia;
            matriz[b][a] -= conductancia;
        }
    }

    vector[nodoPositivo] = 1.0;

    // Resolver el sistema de ecuaciones por eliminación de Gauss.
    int[] indices = new int[cantidadNodos - 1];
    int k = 0;

    for (int i = 0; i < cantidadNodos; i++) {
        if (i != nodoNegativo) {
            indices[k++] = i;
        }
    }

    int n = indices.length;

    for (int col = 0; col < n; col++) {
        int pivote = col;

        for (int fila = col + 1; fila < n; fila++) {
            if (Math.abs(matriz[indices[fila]][indices[col]])
                    > Math.abs(matriz[indices[pivote]][indices[col]])) {
                pivote = fila;
            }
        }

        int temp = indices[col];
        indices[col] = indices[pivote];
        indices[pivote] = temp;

        int filaPivote = indices[col];
        double divisor = matriz[filaPivote][filaPivote];

        if (Math.abs(divisor) < 1e-12) {
            throw new IllegalArgumentException(
                    "La red está desconectada o no tiene una solución única.");
        }

        for (int fila = col + 1; fila < n; fila++) {
            int filaActual = indices[fila];
            double factor = matriz[filaActual][filaPivote] / divisor;

            for (int j = 0; j < cantidadNodos; j++) {
                matriz[filaActual][j] -= factor * matriz[filaPivote][j];
            }

            vector[filaActual] -= factor * vector[filaPivote];
        }
    }

    double[] voltajes = new double[cantidadNodos];

    for (int fila = n - 1; fila >= 0; fila--) {
        int nodo = indices[fila];
        double suma = vector[nodo];

        for (int j = fila + 1; j < n; j++) {
            suma -= matriz[nodo][indices[j]] * voltajes[indices[j]];
        }

        voltajes[nodo] = suma / matriz[nodo][nodo];
    }

    return voltajes[nodoPositivo];
}


    public static String resolvermixto 
     (double voltaje,
        double[][] resistencias,
        int cantidadNodos,
        int nodoPositivo,
        int nodoNegativo) {

    if (!Double.isFinite(voltaje) || voltaje <= 0
            || resistencias == null || resistencias.length == 0
            || cantidadNodos < 2
            || nodoPositivo < 0 || nodoPositivo >= cantidadNodos
            || nodoNegativo < 0 || nodoNegativo >= cantidadNodos
            || nodoPositivo == nodoNegativo) {
        return "Error: datos del circuito no válidos.";
    }

    int[] nodos = new int[cantidadNodos];
    int cantidadDesconocidos = 0;

    for (int i = 0; i < cantidadNodos; i++) {
        if (i != nodoPositivo && i != nodoNegativo) {
            nodos[cantidadDesconocidos++] = i;
        }
    }

    double[][] matriz = new double[cantidadDesconocidos]
            [cantidadDesconocidos];
    double[] vector = new double[cantidadDesconocidos];

    for (double[] r : resistencias) {
        if (r == null || r.length != 3) {
            return "Error: cada resistencia necesita dos nodos y su valor.";
        }

        int a = (int) r[0];
        int b = (int) r[1];
        double ohmios = r[2];

        if (a < 0 || a >= cantidadNodos
                || b < 0 || b >= cantidadNodos || a == b
                || !Double.isFinite(ohmios) || ohmios <= 0
                || r[0] != a || r[1] != b) {
            return "Error: resistencia o nodo no válido.";
        }

        double g = 1.0 / ohmios;
        int ia = -1;
        int ib = -1;

        for (int i = 0; i < cantidadDesconocidos; i++) {
            if (nodos[i] == a) ia = i;
            if (nodos[i] == b) ib = i;
        }

        if (ia >= 0) {
            matriz[ia][ia] += g;
            if (b == nodoPositivo) vector[ia] += g * voltaje;
            if (ib >= 0) matriz[ia][ib] -= g;
        }

        if (ib >= 0) {
            matriz[ib][ib] += g;
            if (a == nodoPositivo) vector[ib] += g * voltaje;
            if (ia >= 0) matriz[ib][ia] -= g;
        }
    }

    // Resolver las ecuaciones por eliminación de Gauss.
    for (int col = 0; col < cantidadDesconocidos; col++) {
        int pivote = col;

        for (int fila = col + 1; fila < cantidadDesconocidos; fila++) {
            if (Math.abs(matriz[fila][col])
                    > Math.abs(matriz[pivote][col])) {
                pivote = fila;
            }
        }

        double[] temporal = matriz[col];
        matriz[col] = matriz[pivote];
        matriz[pivote] = temporal;

        double temp = vector[col];
        vector[col] = vector[pivote];
        vector[pivote] = temp;

        if (Math.abs(matriz[col][col]) < 1e-12) {
            return "Error: circuito desconectado o sin solución única.";
        }

        for (int fila = col + 1; fila < cantidadDesconocidos; fila++) {
            double factor = matriz[fila][col] / matriz[col][col];

            for (int j = col; j < cantidadDesconocidos; j++) {
                matriz[fila][j] -= factor * matriz[col][j];
            }

            vector[fila] -= factor * vector[col];
        }
    }

    double[] v = new double[cantidadNodos];
    v[nodoPositivo] = voltaje;
    v[nodoNegativo] = 0;

    for (int i = cantidadDesconocidos - 1; i >= 0; i--) {
        double suma = vector[i];

        for (int j = i + 1; j < cantidadDesconocidos; j++) {
            suma -= matriz[i][j] * v[nodos[j]];
        }

        v[nodos[i]] = suma / matriz[i][i];
    }

    double corrienteTotal = 0;

    for (double[] r : resistencias) {
        int a = (int) r[0];
        int b = (int) r[1];

        if (a == nodoPositivo) {
            corrienteTotal += (v[a] - v[b]) / r[2];
        } else if (b == nodoPositivo) {
            corrienteTotal += (v[b] - v[a]) / r[2];
        }
    }

    if (corrienteTotal <= 0 || !Double.isFinite(corrienteTotal)) {
        return "Error: no se pudo calcular la corriente de la fuente.";
    }

    double equivalente = voltaje / corrienteTotal;
    StringBuilder resultado = new StringBuilder();

    resultado.append("RESULTADOS DEL CIRCUITO MIXTO\n\n");
    resultado.append(String.format(java.util.Locale.US,
            "Voltaje de la fuente: %.2f V\n", voltaje));
    resultado.append(String.format(java.util.Locale.US,
            "Resistencia equivalente: %.2f ohmios\n", equivalente));
    resultado.append(String.format(java.util.Locale.US,
            "Corriente total: %.4f A\n\n", corrienteTotal));

    for (int i = 0; i < resistencias.length; i++) {
        int a = (int) resistencias[i][0];
        int b = (int) resistencias[i][1];
        double r = resistencias[i][2];
        double vr = v[a] - v[b];
        double ir = vr / r;

        resultado.append(String.format(java.util.Locale.US,
                "R%d: %.2f ohmios\nVoltaje: %.3f V\nCorriente: %.4f A\n\n",
                i + 1, r, Math.abs(vr), Math.abs(ir)));
    }

    return resultado.toString();
}
    
     
    public static String resolverserie(double voltaje, java.util.List<Double> resistencias){
        if (!Double.isFinite(voltaje) || voltaje<0 || resistencias == null || resistencias.isEmpty()){
            return "Error: dibuja al menos una resistencia e ingresa un voltaje valido ";}
        
        double total = 0;
        for(double r: resistencias){
            if (!Double.isFinite(r) || r<=0 ){
                return "Error: todas las resistencias deben ser mayores a cero";}
            
            total += r;
            }
        
        double corriente = voltaje/total;
        StringBuilder resultado = new StringBuilder();
        resultado.append("RESULTADOS DEL CIRCUITO EN SERIE\n\n ");
        resultado.append(String.format(java.util.Locale.US, "Voltaje de la fuente: %.2f V\n ", voltaje ));
        resultado.append(String.format(java.util.Locale.US, "Resistencia total: %.2f ohmios\n", total));
        resultado.append(String.format(java.util.Locale.US, "Corriente total: %.4f A\n\n ", corriente ));
        
        for (int i =0; i<resistencias.size(); i++){
            double r= resistencias.get(i);
            double voltajeresistencia= corriente * r;
            
            resultado.append(String.format(java.util.Locale.US, "R%d: %.2f ohmios\n Voltaje: %.3f V\n "+ "Corriente: %.4f A\n\n", i+1, r,
           voltajeresistencia, corriente));
        }
        return resultado.toString();
        }
    
    public static String resolverparalelo (double voltaje, java.util.List<Double> resistencias){
         if (!Double.isFinite(voltaje) || voltaje<0 || resistencias == null || resistencias.isEmpty()){
            return "Error: dibuja al menos una resistencia e ingresa un voltaje valido ";}
        
        double suma = 0;
        for(double r: resistencias){
            if (!Double.isFinite(r) || r<=0 ){
                return "Error: todas las resistencias deben ser mayores a cero";}
            
            suma += 1.0/r;
            }
        
        double resistenciatotal = 1.0/suma;
        double corrientetotal = voltaje/resistenciatotal;
        StringBuilder resultado = new StringBuilder();
        resultado.append("RESULTADOS DEL CIRCUITO EN PARALELO\n\n ");
        resultado.append(String.format(java.util.Locale.US, "Voltaje de la fuente: %.2f V\n ", voltaje ));
        resultado.append(String.format(java.util.Locale.US, "Resistencia total: %.2f ohmios\n", resistenciatotal));
        resultado.append(String.format(java.util.Locale.US, "Corriente total: %.4f A\n\n ", corrientetotal ));
        
        for (int i =0; i<resistencias.size(); i++){
            double r= resistencias.get(i);
            double corriente = voltaje/ r;
            
            resultado.append(String.format(java.util.Locale.US, "R%d: %.2f ohmios\n Voltaje: %.3f V\n "+ "Corriente: %.4f A\n\n", i+1, r,
           voltaje, corriente));
        }
        return resultado.toString();
    }
}  

  
    

