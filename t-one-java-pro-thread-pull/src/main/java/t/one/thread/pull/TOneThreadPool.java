package t.one.thread.pull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/*
Попробуйте реализовать собственный пул потоков. В качестве аргументов конструктора пулу
передается его емкость (количество рабочих потоков). Как только пул создан,
он сразу инициализирует и запускает потоки. Внутри пула очередь задач на исполнение организуется через LinkedList.
При выполнении у пула потоков метода execute(Runnabler), указанная задача должна попасть в очередь исполнения,
и как только появится свободный поток – должна быть выполнена. Также необходимо реализовать метод shutdown(),
после выполнения которого новые задачи больше не принимаются пулом
(при попытке добавить задачу можно бросать IllegalStateException),
и все потоки для которых больше нет задач завершают свою работу.
Дополнительно можно добавить метод awaitTermination() без таймаута, работающий аналогично стандартным пулам потоков
 */
public class TOneThreadPool {
    private final int capacity;
    private final Queue<Runnable> taskQueue;
    private final Worker[] workers;
    private final ReentrantLock lock;
    private final Condition terminationCondition;
    private volatile boolean isShutdown;

    public TOneThreadPool(int capacity) {
        this.capacity = capacity;
        this.taskQueue = new LinkedList<>();
        this.workers = new Worker[capacity];
        this.lock = new ReentrantLock();
        this.terminationCondition = lock.newCondition();
        this.isShutdown = false;

        for (int i = 0; i < capacity; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }
        lock.lock();
        try {
            if (isShutdown) {
                throw new IllegalStateException("Пул потоков остановлен. Новые задачи не принимаются.");
            }
            taskQueue.offer(task);
            terminationCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();
        try {
            isShutdown = true;
            terminationCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void awaitTermination() throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                boolean allTerminated = true;
                for (Worker worker : workers) {
                    if (worker.isAlive()) {
                        allTerminated = false;
                        break;
                    }
                }
                if (allTerminated) {
                    break;
                }
                terminationCondition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Runnable task;
                lock.lock();
                try {
                    while (taskQueue.isEmpty() && !isShutdown) {
                        terminationCondition.await();
                    }
                    if (isShutdown && taskQueue.isEmpty()) {
                        break;
                    }
                    task = taskQueue.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } finally {
                    lock.unlock();
                }

                if (task != null) {
                    try {
                        task.run();
                    } catch (Exception e) {
                        System.err.printf("[%s] Ошибка при выполнении задачи: %s%n", Thread.currentThread().getName(), e.getMessage());
                    }
                }
            }

            lock.lock();
            try {
                terminationCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}
