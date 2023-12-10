import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.knowm.xchart.*;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class IntegralSums {
    private static double function(double x) {
        return 3 * x - 2 * x * x;
    }

    public static double countIntegral(double a, double b, int n, String type) {
        double h = (b - a) / n;
        double sum = 0;
        switch (type) {
            case "левые" -> {
                for (int i = 0; i < n; i++) {
                    sum += function(a + i * h);
                }
            }
            case "правые" -> {
                for (int i = 1; i <= n; i++) {
                    sum += function(a + i * h);
                }
            }
            case "средние" -> {
                for (int i = 0; i < n; i++) {
                    sum += function(a + (i + 0.5) * h);
                }
            }
            case "случайные" -> {
                for (int i = 0; i < n; i++) {
                    sum += function(a + (i + Math.random()) * h);
                }
            }
        }
        return sum * h;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Не забывайте задать функцию в коде перед запуском программы!");
        System.out.println("Введите значения концов отрезка: ");
        double a = scan.nextDouble();
        double b = scan.nextDouble();
        System.out.println("Введите размер разбиения: ");
        int n = scan.nextInt();
        System.out.println("Введите способ выбора оснащения: ");
        System.out.println("1 - левые, 2 - правые, 3 - средние, 4 - случайные");
        int typeI = scan.nextInt();
        String type = "";
        if (typeI == 1) {
            type = "левые";
        } else if (typeI == 2) {
            type = "правые";
        } else if (typeI == 3) {
            type = "средние";
        } else if (typeI == 4) {
            type = "случайные";
        }
        scan.close();
        System.out.println(countIntegral(a, b, n, type));
        XYChart chart = new XYChart(1900, 1000);
        addPoints(chart, "f(x)", a, b);
        double h = (b - a) / n;
        for (int i = 0; i < n; i++) {
            double x = a + i * h;
            double height = switch (type) {
                case "левые" -> function(x);
                case "правые" -> function(x + h);
                case "средние" -> function(x + h / 2);
                case "случайные" -> function(x + Math.random() * h);
                default -> 0;
            };
            chart.addSeries("r" + i, new double[]{x, x + h, x + h, x}, new double[]{0, 0, height, height});
        }
        addHorizontalLine(chart, "y = 0", a, b, 0);
        new SwingWrapper<>(chart).displayChart();

        // Сохраним график в .png картинку
        try {
            BitmapEncoder.saveBitmap(chart, "./picture1", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException ex) {
            System.out.println("Could not save chart: " + ex.getMessage());
        }
        System.out.println();
        System.out.println("                   ПОГРЕШНОСТИ");
        System.out.println("n       левые       правые      средние     случайные");
        for (int k = 10; k <= 100; k += 10) {
            System.out.printf("%-8d%-12.5f%-12.5f%-12.5f%-12.5f%n", k,
                    Math.abs(countIntegral(a, b, k, "левые") + 1.5),
                    Math.abs(countIntegral(a, b, k, "правые") + 1.5),
                    Math.abs(countIntegral(a, b, k, "средние") + 1.5),
                    Math.abs(countIntegral(a, b, k, "случайные") + 1.5));
        }
    }

    private static void addPoints(XYChart chart, String seriesName, double from, double to) {
        // Заведем массивы для координат точек и заполним их
        List<Double> x = new ArrayList<>((int) (to - from));
        List<Double> y = new ArrayList<>((int) (to - from));
        for (double n = from; n < to; n += 0.1) {
            x.add(n);
            y.add(function(n));
        }

        XYSeries series = chart.addSeries(seriesName, x, y); // Добавим точки на график
        series.setLineStyle(SeriesLines.SOLID); // Соедини точки сплошной линией
        series.setMarker(SeriesMarkers.NONE);
    }

    private static void addHorizontalLine(XYChart chart, String name, double xFrom, double xTo, double yHeight) {
        // Отрезок задается двумя точками
        List<Double> x = List.of(xFrom, xTo);
        List<Double> y = List.of(yHeight, yHeight);

        XYSeries series = chart.addSeries(name, x, y); // Добавь точки отрезка на график
        series.setLineStyle(SeriesLines.SOLID); // Соедини точки сплошной линией
        series.setMarker(SeriesMarkers.NONE); // Не отображай сами точки
    }
}
