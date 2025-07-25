Swimmer
-------
>Swimmer app using ScalaFx, ScalikeJdbc, Jsoniter, JoddMail, PostgreSql, HikariCP, Helidon, Ox and Scala 3.

Todo
----
1. Test client.

Model
-----
>A session represents a workout, requiring the user to **replicate** data across sessions.
* Account 1 ---> * Swimmer
* Swimmer 1 ---> * Session

Alternative Model
-----------------
>A workout represents an optional **template** for a session, removing the need to **replicate** data across sessions.
* Account 1 ---> * Swimmer | Workout
* Swimmer 1 ---> * Session

Calculations
------------
1. [Calories Burned Swimming](https://www.calculatorpro.com/calculator/calories-burned-swimming-calculator/)
2. [Distance / Time](https://www.calculatorsoup.com/calculators/math/speed-distance-time-calculator.php)
   
Build
-----
1. sbt clean compile

Test
----
1. sbt clean test

Server Run
----------
1. sbt server/run

Client Run
----------
1. sbt client/run

Package Server
--------------
1. sbt server/universal:packageBin
2. see server/target/universal

Client Assembly
---------------
1. sbt clean test assembly copyAssemblyJar

Execute Client
--------------
1. java -jar .assembly/swimmer-$version.jar ( or double-click executable jar )

Deploy
------
>Consider these options:
1. [jDeploy](https://www.npmjs.com/package/jdeploy)
2. [Conveyor](https://hydraulic.software/index.html)

Postgresql
----------
1. config:
    1. on osx intel: /usr/local/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
    2. on osx m1: /opt/homebrew/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
2. run:
    1. brew services start postgresql@14
3. logs:
    1. on osx intel: /usr/local/var/log/postgres.log
    2. on m1: /opt/homebrew/var/log/postgres.log

Database
--------
>Example database url: postgresql://localhost:5432/swimmer?user=mycomputername&password=swimmer"
1. psql postgres
2. CREATE DATABASE swimmer OWNER [your computer name];
3. GRANT ALL PRIVILEGES ON DATABASE swimmer TO [your computer name];
4. \l
5. \q
6. psql swimmer
7. \i ddl.sql
8. \q

DDL
---
>Alternatively run: psql -d swimmer -f ddl.sql
1. psql swimmer
2. \i ddl.sql
3. \q

Drop
----
1. psql postgres
2. drop database swimmer;
3. \q

Environment
-----------
>The following environment variables must be defined:
```
export SWIMMER_HOST="127.0.0.1"
export SWIMMER_PORT=7171
export SWIMMER_ENDPOINT="/command"

export SWIMMER_CACHE_INITIAL_SIZE=4
export SWIMMER_CACHE_MAX_SIZE=10
export SWIMMER_CACHE_EXPIRE_AFTER=24

export SWIMMER_POSTGRESQL_DRIVER="org.postgresql.ds.PGSimpleDataSource"
export SWIMMER_POSTGRESQL_URL="jdbc:postgresql://localhost:5432/swimmer"
export SWIMMER_POSTGRESQL_USER="yourusername"
export SWIMMER_POSTGRESQL_PASSWORD="swimmer"

export SWIMMER_EMAIL_HOST="your-email.provider.com"
export SWIMMER_EMAIL_ADDRESS="your-email@provider.com"
export SWIMMER_EMAIL_PASSWORD="your-email-password"
```

Resources
---------
* [JavaFX](https://openjfx.io/index.html)
* [JavaFX Tutorial](https://jenkov.com/tutorials/javafx/index.html)
* [ScalaFX](http://www.scalafx.org/)
* [ScalikeJdbc](http://scalikejdbc.org/)

License
-------
>Copyright (c) [2023, 2024, 2025] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.