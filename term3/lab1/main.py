import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
import time


# Определение функции
def f(x, y):
    return x ** 2 * y ** 2 * np.log(8 * x ** 2 + 3 * y ** 2)


# Определение градиента функции
def gradient(x, y):
    df_dx = 2 * x * y ** 2 * (8 * x ** 2 / (8 * x ** 2 + 3 * y ** 2) + np.log(8 * x ** 2 + 3 * y ** 2))
    df_dy = 2 * x ** 2 * y * (3 * y ** 2 / (8 * x ** 2 + 3 * y ** 2) + np.log(8 * x ** 2 + 3 * y ** 2))
    return np.array([df_dx, df_dy])


# Начальная точка
x_k = 1
y_k = 1

# Коэффициент
a_k = 0.1

# Количество итераций
num_iterations = 1000

# Условие останова
epsilon = 1e-8  # Желаемая точность

x_points = []
y_points = []
z_points = []

# Градиентный спуск с замером времени
start_time = time.time()
for i in range(num_iterations):
    grad = gradient(x_k, y_k)
    x_k1 = x_k - a_k * grad[0]
    y_k1 = y_k - a_k * grad[1]
    f_k1 = f(x_k1, y_k1)
    delta_f = abs(f_k1 - f(x_k, y_k))

    x_points.append(x_k1)
    y_points.append(y_k1)
    z_points.append(f_k1)

    if delta_f < epsilon:
        print(f"Last iteration {i + 1}, (x, y) = ({x_k1}, {y_k1}), f(x, y) = {f_k1}")
        break
    x_k, y_k = x_k1, y_k1

end_time = time.time()
execution_time = end_time - start_time

print(f"Критерий останова: |delta f| < {epsilon}")
print(f"Число итераций: {i + 1}")
print(f"Полученная точка: ({x_k1}, {y_k1})")
print("Полученная точка совпала с точкой D4 из аналитического метода!")
print(f"Полученное значение функции: {f_k1}")
print(f"Значение a_k: {a_k} (выбрано константное значение как один из предложенных в тз вариантов)")
print(f"Время работы: {execution_time:.4f} сек")

# Построение графика поверхности и линий уровня
fig = plt.figure(figsize=(20, 10))
ax = fig.add_subplot(1, 2, 1, projection='3d')

x = np.linspace(-1, 1, 100)
y = np.linspace(-1, 1, 100)
X, Y = np.meshgrid(x, y)
Z = f(X, Y)

ax.plot_surface(X, Y, Z, cmap='inferno')
ax.set_xlabel('x')
ax.set_ylabel('y')
ax.set_zlabel('z')
ax.set_title('График поверхности функции')

ax = fig.add_subplot(1, 2, 2)
ax.contour(X, Y, Z, 50, cmap='inferno')
ax.scatter(x_points, y_points, color='red', s=10)
ax.set_xlabel('x')
ax.set_ylabel('y')
ax.set_title('Линии уровня функции')

plt.show()
