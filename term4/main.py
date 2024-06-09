import numpy as np
import matplotlib.pyplot as plt
from scipy.integrate import quad
import time


def bin_check(value, n):
    bin_val = ''
    while value and len(bin_val) < n:
        value *= 2
        if value >= 1:
            bin_val += '1'
            value -= 1
        else:
            bin_val += '0'
    if bin_val.count('1') == n:
        return False
    first_one_pos = bin_val.find('1')
    if first_one_pos == -1 or '0' in bin_val[first_one_pos:]:
        return False
    return True


def f_n(x, n):
    if bin_check(x, n):
        return 0
    else:
        return np.exp(x)


n_values = [3, 5, 7, 11, 13, 17, 19]

for n in n_values:
    x_vals = np.linspace(0, 1, 1000)
    y_vals = np.array([f_n(x, n) for x in x_vals])
    plt.plot(x_vals, y_vals, label=f'n={n}')
plt.legend()
plt.title('График функции f_n(x)')
plt.xlabel('x')
plt.show()


def compute_integral_and_duration(n, limit=100):
    start_time = time.time()
    result, error = quad(f_n, 0, 1, args=(n,), limit=limit)
    end_time = time.time()
    duration = end_time - start_time
    return result, duration


n_values_integral = [3, 5, 7, 11, 13, 17]

for n in n_values_integral:
    integral_result, duration = compute_integral_and_duration(n, limit=1000)
    print(f"Интеграл для n={n}: {integral_result}, вычислено за {duration} секунд")


def f(x):
    if x <= 0.8:
        return np.sqrt(np.ceil(5 * x))
    else:
        return np.exp(x)


def integrate_with_discontinuities(s, f, a, b, n):
    step = (b - a) / n
    total_integral = 0
    for i in range(n):
        left_x = a + i * step
        right_x = left_x + step
        left_H = f(left_x)
        right_H = f(right_x)
        if left_H != right_H:
            total_integral += s(left_x, n) * (right_H - left_H)
    return total_integral


def trapezoid_integration(a, b, n):
    step = (b - a) / n
    x_points = np.linspace(a, b, n + 1)
    y_points = np.exp(2 * x_points)
    return step * (0.5 * y_points[0] + np.sum(y_points[1:-1]) + 0.5 * y_points[-1])


for n in [1500, 3000]:
    start_time = time.time()
    integral_part1 = integrate_with_discontinuities(f_n, f, 0, 0.8, n)
    integral_part2 = trapezoid_integration(0.8, 1, n)
    final_integral = integral_part1 + integral_part2
    end_time = time.time()
    print(f"Интеграл Лебега-Стильтьеса для n={n}: {final_integral}, вычислено за {end_time - start_time} секунд")
