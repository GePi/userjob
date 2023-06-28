Реализация [тестового задания](task.md)  

Микросервис с тремя методами.  

1. Создание записи - /api/v1/create-userjob
- принимает данные вида dev.gepi.userjob.api.dto.UserJobDTO;
- возвращает http-коды ответов 201,409,400. 

2. Обновление записи - /api/v1/update-userjob 
- принимает данные вида dev.gepi.userjob.api.dto.UserJobDTO;
- возвращает List<Strings> содержащий список измененных полей.

3. Получение данных о пользователе или компании - /api/v1/get-userjob
- при вызове c параметром userId=... возвращает JSON вида dev.gepi.userjob.api.dto.UserWithCompaniesDTO
- при вызове с параметром idCompany=... возвращает JSON вида dev.gepi.userjob.api.dto.CompanyWithUsersDTO

Пример использования -  dev.gepi.userjob.example.UserJobClient.  
Примеры JSON для взаимодействия с сервисом можно найти в методах тестового класса dev.gepi.userjob.UserjobApplicationTests: createRequestContent, getResponseContent, getAfterUpdateResponseContent, updateRequestContent.

