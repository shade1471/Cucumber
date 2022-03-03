#language:ru

Функциональность: Авторизация

  # Авторизация валидного пользователя (позитивный)
  Сценарий: : Авторизация в личном кабинете (позитивный)
    Пусть открыта страница с формой авторизации "http://localhost:9999"
    Когда пользователь пытается авторизоваться с именем "vasya" и паролем "qwerty123"
    И пользователь вводит проверочный код 'из смс' "12345"
    Тогда происходит успешная авторизация и пользователь попадает на страницу 'Личный кабинет'

    # Авторизация невалидного пользователя (негативный)
  Сценарий: : Авторизация в личном кабинете (негативный)
    Пусть открыта страница с формой авторизации "http://localhost:9999"
    Когда пользователь пытается авторизоваться с несуществующим логином и паролем
    Тогда получает сообщение об ошибке авторизации

    # Авторизация валидного пользователя c неправильным проверочным кодом (негативный)
  Сценарий: : Авторизация в личном кабинете c неправильным проверочным кодом (негативный)
    Пусть открыта страница с формой авторизации "http://localhost:9999"
    Когда пользователь пытается авторизоваться с именем "vasya" и паролем "qwerty123"
    И пользователь вводит проверочный код 'из смс' "1245"
    Тогда получает сообщение об ошибке в наборе кода