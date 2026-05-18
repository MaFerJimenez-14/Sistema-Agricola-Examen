package com.finca.reporte;

import com.finca.modelo.LaborAgricola;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneradorInforme {

    public static void generarTXT(List<LaborAgricola> labores, String ruta) throws IOException {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("========================================\n");
            fw.write("   INFORME DE LABORES AGRÍCOLAS\n");
            fw.write("========================================\n\n");
            for (LaborAgricola l : labores) {
                fw.write("Código:          " + l.getCodigo() + "\n");
                fw.write("Fecha:           " + l.getFecha() + "\n");
                fw.write("Parcela:         " + l.getParcela().getNombre() + "\n");
                fw.write("Cultivo:         " + l.getCultivo().getNombre() + "\n");
                fw.write("Responsable:     " + l.getResponsable().getNombreCompleto() + "\n");
                fw.write("Tipo de labor:   " + l.getTipoLabor() + "\n");
                fw.write("Costo estimado:  " + l.getCostoEstimado() + "\n");
                fw.write("----------------------------------------\n");
            }
        }
    }

    public static void generarCSV(List<LaborAgricola> labores, String ruta) throws IOException {
        try (FileWriter fw = new FileWriter(ruta)) {
            fw.write("codigo,fecha,parcela,cultivo,responsable,tipo_labor,costo_estimado\n");
            for (LaborAgricola l : labores) {
                fw.write(
                    l.getCodigo() + "," +
                    l.getFecha() + "," +
                    l.getParcela().getNombre() + "," +
                    l.getCultivo().getNombre() + "," +
                    l.getResponsable().getNombreCompleto() + "," +
                    l.getTipoLabor() + "," +
                    l.getCostoEstimado() + "\n"
                );
            }
        }
    }
}