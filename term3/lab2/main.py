from math import asin, pi, cos, sin
from prettytable import PrettyTable
import numpy as np
import matplotlib.pyplot as plt
import time

# Аналитический ответ для сравнения
analytic_answer = 1.259

# Радиус окружности
radius = 2


def p(x, y):
    return x + y


def q(x, y):
    return 2 * x * y


def calc1(delta):
    int_sum = 0

    # интегральная сумма на линии
    partition = []
    step_x = delta
    step_y = delta  # x = y
    start_x = -np.sqrt(2)  # начальные координаты x
    start_y = -np.sqrt(2)  # начальные координаты y

    step = 0
    partition.append((start_x, start_y))
    while start_y + step_y * step < np.sqrt(2):
        partition.append((start_x + step_x * step, start_y + step_y * step))
        step += 1
    int_sum += count_intgram_sum(partition)

    # интегральная сумма на полуокружности,
    partition = []
    step_phi = 2 * asin(delta / (2 * radius))  # шаг
    start_phi = pi / 4  # начальные координаты

    step = 0
    while step * step_phi < pi:
        partition.append((
            radius * cos(start_phi + step * step_phi),
            radius * sin(start_phi + step * step_phi)
        ))
        step += 1
    partition.append((start_x, start_y))
    int_sum += count_intgram_sum(partition)
    return int_sum


def count_intgram_sum(partition):
    res = 0
    for i in range(1, len(partition)):
        x = partition[i][0]
        y = partition[i][1]
        dx = x - partition[i - 1][0]
        dy = y - partition[i - 1][1]
        res += p(x, y) * dx + q(x, y) * dy
    return res


# Функция для вычисления двойного интеграла
def calc2(delta):
    # Создание сетки точек
    grid_x = np.arange(-2, 2, delta)
    grid_y = np.arange(-2, 2, delta)
    # Инициализация суммы интеграла
    integral_sum = 0
    # Перебор всех точек сетки
    for i in range(len(grid_x)):
        for j in range(len(grid_y)):
            x_value = grid_x[i] + delta / 2
            y_value = grid_y[j] + delta / 2
            # Проверка условия внутри области интегрирования
            if x_value ** 2 + y_value ** 2 <= 4 and x_value <= y_value:
                integral_sum += (2 * y_value - 1) * delta ** 2

    return integral_sum


# Значения delta для тестирования
deltas = [0.1, 0.01, 0.001]

# Таблица для криволинейного интеграла
line_table = PrettyTable()
line_table.field_names = ["Delta", "Numerical Answer", "Error", "Time"]

# Вычисление линейного интеграла для каждого значения delta
for delta_value in deltas:
    start_time = time.time()
    numerical_answer = calc1(delta_value)
    used_time = time.time() - start_time
    error = abs(analytic_answer - numerical_answer)
    line_table.add_row([delta_value, numerical_answer, error, used_time])

print("Line Integral:")
print(line_table)

# Таблица для двойного интеграла
double_table = PrettyTable()
double_table.field_names = ["Delta", "Numerical Answer", "Error", "Time"]

# Вычисление двойного интеграла для каждого значения delta
for delta_value in deltas:
    start_time = time.time()
    numerical_answer = calc2(delta_value)
    used_time = time.time() - start_time
    error = abs(analytic_answer - numerical_answer)
    double_table.add_row([delta_value, numerical_answer, error, used_time])

print("Double Integral:")
print(double_table)


# Построение графика для визуализации двойного интеграла
def plot_integral(delta):
    grid_x = np.arange(-2, 2, delta)
    grid_y = np.arange(-2, 2, delta)
    integral_sum = np.zeros((len(grid_x), len(grid_y)))
    for i in range(len(grid_x)):
        for j in range(len(grid_y)):
            x_value = grid_x[i] + delta / 2
            y_value = grid_y[j] + delta / 2
            if x_value ** 2 + y_value ** 2 <= 4 and x_value <= -y_value:
                integral_sum[i, j] = (2 * y_value - 1) * delta ** 2
            else:
                integral_sum[i, j] = np.nan

    plt.imshow(integral_sum, extent=(-2, 2, -2, 2))
    plt.colorbar()
    plt.title("Double Integral (delta = " + str(delta) + ")")
    plt.show()


plot_integral(0.1)
plot_integral(0.01)
