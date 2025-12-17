package t.one;

import t.one.thread.pull.TOneThreadPool;
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

public class Main {
    public static final int CAPACITY = 3;
    public static void main(String[] args) {
        TOneThreadPool pool = gettOneThreadPool();

        pool.shutdown();
        try {
            pool.awaitTermination();
        } catch (InterruptedException e) {
            System.err.println("Не все задачи завершены");
            throw new RuntimeException(e);
        }
        System.out.println("Все задачи завершены");
    }

    private static TOneThreadPool gettOneThreadPool() {
        TOneThreadPool pool = new TOneThreadPool(Main.CAPACITY);

        for (int i = 0; i < Main.CAPACITY * 3; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println("Выполняется задача " + taskId + " в потоке " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        return pool;
    }
}