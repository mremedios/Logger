[![pipeline status](https://gitlab.com/mremedios/logger/badges/solution/pipeline.svg)](https://gitlab.com/mremedios/logger/-/commits/solution)
[![coverage report](https://gitlab.com/mremedios/logger/badges/solution/coverage.svg)](https://gitlab.com/mremedios/logger/-/commits/solution)

# Logger

Для использования скачать пакет `logger`

##### Использование

Готовые реализации логгера:

1. `Log.getSimpleLogger`
    - консольный вывод
    - учет значения переменной окружения LOG_LEVEL, если она задана
    - вывод вместе с меткой времени и указанием уровня журналирования 
    
2. `Log.getExtendedLogger`
    - на консоль выводятся все записи
    - начиная с уровня `warning` записи выводяется еще и в файл `error.log`
    
3. `Log.getStreamDividedLogger`
    - записи уровнями ниже `info` никуда не выводятся 
    - записи уровня `info` выводятся в стандартный поток вывода
    - записи уровня выше `info` — в стандартный поток ошибок
    
4. `Log.getFileDividedLogger` \
   Параметризуется префиксом пути файлов
    - в файл с суффиксом `.debug.log` записываются события уровня ниже `info`
    - в файл с суффиксом `.info.log` записываются события уровня `info`
    - в файл с суффиксом `.error.log` записываются события уровня выше `info`


Пример создания своего логгера:
```aidl
    val handler = new Handler(
      new StreamPrinter(System.out),
      new Formatter(enableLevel = true, enableDate = false, enableComponent = false),
      LogLevel.info,
    )
    new Logger(
      "LoggerName",
      List(handler)
    )
```
Логгер может иметь любое количество `handler`'ов с различными способами вывода и форматирования сообщения.\
Для быстрого создания `handler` можно воспользовать классом `HandlerBuilder`
```aidl
   HandlerBuilder.builder
      .addLevel()
      .addDate()
      .addComponent()
      .addMinLevel(warning)
      .buildStreamHandler(System.out)
```

Создание лога:
```aidl
    log.log(LogLevel.warning, "some log message");
    
    или
    
    log.warning("some log message");
```