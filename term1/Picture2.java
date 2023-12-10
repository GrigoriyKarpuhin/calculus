import org.knowm.xchart.*;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Picture2 {
    /* Функция, возвращающая элемент последовательности по его номеру */
    private static double function(int k) {
        return ((2.0 * k + 1) / (k + 2)) * (Math.PI/2 - Math.atan(Math.sqrt(2 + Math.pow(-1, k))));
    }

    /* Функция, добавляющая на график точки последовательности */
    private static void addPoints(XYChart chart, String seriesName, int from, int to) {
        // Заведем массивы для координат точек и заполним их
        List<Double> x = new ArrayList<>(to - from);
        List<Double> y = new ArrayList<>(to - from);
        for (int k = from; k < to; k+=2) {
            x.add((double) k);
            y.add(function(k));
        }

        XYSeries series = chart.addSeries(seriesName, x, y); // Добавим точки на график
        series.setLineStyle(SeriesLines.NONE);  // Не соединять точки линиями
        series.setMarker(SeriesMarkers.CIRCLE); // Формат точки - кружочек
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
        double e = 0.01;
        int n0 = 1;
        for (int n = 1; n < Integer.MAX_VALUE; n++) {
            if (function(n) > (Math.PI / 2) - e) {
                n0 = n;
                break;
            }
        }
        XYChart chart = new XYChart(1000, 400); // Создадим пустой график
        addPoints(chart, "x_k", n0, n0 + 100); // Добавим на график первые 100 точек последовательности
        addHorizontalLine(chart, "lim", n0, n0 + 100, Math.PI / 2); // Добавим на график inf

        // Отобразим график в отдельном окошке
        new SwingWrapper<>(chart).displayChart();

        // Сохраним график в .png картинку
        try {
            BitmapEncoder.saveBitmap(chart, "./picture2", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException ex) {
            System.out.println("Could not save chart: " + ex.getMessage());
        }
    }
}
