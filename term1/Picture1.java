import org.knowm.xchart.*;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Picture1 {
    /* Функция, возвращающая элемент последовательности по его номеру */
    private static double function(int n) {
       return ((2.0 * n + 1) / (n + 2)) * (Math.PI/2 - Math.atan(Math.sqrt(2 + Math.pow(-1, n))));
    }
    private static double kFunction(int k) {
        return ((2.0 * k + 1) / (k + 2)) * (Math.PI/2 - Math.atan(Math.sqrt(2 + Math.pow(-1, k))));
    }
    /* Функция, добавляющая на график точки последовательности */
    private static void addPoints(XYChart chart, String seriesName, int from, int to) {
        // Заведем массивы для координат точек и заполним их
        List<Double> x = new ArrayList<>(to - from);
        List<Double> y = new ArrayList<>(to - from);
        for (int n = from; n < to; n++) {
            x.add((double) n);
            y.add(function(n));
        }
            XYSeries series = chart.addSeries(seriesName, x, y); // Добавим точки на график
            series.setLineStyle(SeriesLines.NONE);  // Не соединять точки линиями
            series.setMarker(SeriesMarkers.CIRCLE); // Формат точки - кружочек
        }
        private static void addPointsK(XYChart chart, String seriesNameK, int fromK, int toK) {
        List<Double> xk = new ArrayList<>(toK - fromK);
        List<Double> yk = new ArrayList<>(toK - fromK);
        for (int k = fromK; k < toK; k+=2) {
            xk.add((double) k);
            yk.add(kFunction(k));
        }
        XYSeries kSeries = chart.addSeries(seriesNameK, xk, yk);
        kSeries.setLineStyle(SeriesLines.NONE);
        kSeries.setMarker(SeriesMarkers.CIRCLE);
    }

    /* Функция, добавляющая на график горизонтальный отрезок */
    private static void addHorizontalLine(XYChart chart, String name, double xFrom, double xTo, double yHeight) {
        // Отрезок задается двумя точками
        List<Double> x = List.of(xFrom, xTo);
        List<Double> y = List.of(yHeight, yHeight);

        XYSeries series = chart.addSeries(name, x, y); // Добавь точки отрезка на график
        series.setLineStyle(SeriesLines.SOLID); // Соедини точки сплошной линией
        series.setMarker(SeriesMarkers.NONE); // Не отображай сами точки
    }

    public static void main(String[] args) {
        XYChart chart = new XYChart(1000, 400); // Создадим пустой график
        addPoints(chart, "x_n", 1, 101); // Добавим на график первые 100 точек последовательности
        addPointsK(chart, "x_k", 1, 101);
        addHorizontalLine(chart, "inf", 1, 101, 5 * Math.PI/24); // Добавим на график inf
        addHorizontalLine(chart, "sup/lim_up", 1, 101,   Math.PI/2);
        addHorizontalLine(chart, "lim_down", 1, 101,   Math.PI/3);
        // Отобразим график в отдельном окошке
        new SwingWrapper<>(chart).displayChart();

        // Сохраним график в .png картинку
        try {
            BitmapEncoder.saveBitmap(chart, "./picture1", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException ex) {
            System.out.println("Could not save chart: " + ex.getMessage());
        }
    }
}
