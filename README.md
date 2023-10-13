Swimmer
-------
>Swimmer app using Scala3, ScalaFX, ScalikeJdbc, Jsoniter, JoddMail and PostgreSql.

Todo
----
1. Client

Model
-----
>A session represents a **free form** workout, requiring the user to **replicate** data across sessions.
* Swimmer 1 ---> * Session

Alternative Model
-----------------
>A workout represents a **template** for a session, allowing the user to select a **workout template**
>for each session.
* Workout 1 ---> * Swimmer 1 ---> * Session

Calories
--------
*[Calories Burned Swimming](https://www.calculatorpro.com/calculator/calories-burned-swimming-calculator/)
   
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
>To build for a "mac", "m1', "win" or "linux" os target:
1. sbt -Dtarget="mac" clean test assembly copyAssemblyJar
2. sbt -Dtarget="m1" clean test assembly copyAssemblyJar
3. sbt -Dtarget="win" clean test assembly copyAssemblyJar
4. sbt -Dtarget="linux" clean test assembly copyAssemblyJar

Execute Client
--------------
>To execute an assembled jar locally:
1. java -jar .assembly/pool-balance-mac-0.1.jar
2. java -jar .assembly/pool-balance-m1-0.1.jar
3. java -jar .assembly/pool-balance-win-0.1.jar
4. java -jar .assembly/pool-balance-linux-0.1.jar

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
    1. brew services start postgresql
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
export SWIMMER_PORT=7272

export SWIMMER_POSTGRESQL_URL="jdbc:postgresql://localhost:5432/poolbalance"
export SWIMMER_POSTGRESQL_USER="yourusername"
export SWIMMER_POSTGRESQL_PASSWORD="poolbalance"
export SWIMMER_POSTGRESQL_DRIVER="org.postgresql.ds.PGSimpleDataSource"
export SWIMMER_POSTGRESQL_DB_NAME="poolbalance"
export SWIMMER_POSTGRESQL_HOST="127.0.0.1"
export SWIMMER_POSTGRESQL_PORT=5432
export SWIMMER_POSTGRESQL_POOL_INITIAL_SIZE=9
export SWIMMER_POSTGRESQL_POOL_MAX_SIZE=32
export SWIMMER_POSTGRESQL_POOL_CONNECTION_TIMEOUT_MILLIS=30000

export SWIMMER_EMAIL_HOST="your-email.provider.com"
export SWIMMER_EMAIL_ADDRESS="your-email@provider.com"
export SWIMMER_EMAIL_PASSWORD="your-email-password"
```

Resources
---------
1. [JavaFX](https://openjfx.io/index.html)
2. [ScalaFX](http://www.scalafx.org/)
3. [ScalikeJdbc](http://scalikejdbc.org/)
4. [jDeploy](https://www.jdeploy.com/)
5. [JavaFX Tutorial](https://jenkov.com/tutorials/javafx/index.html)

License
-------
>Copyright (c) [2023] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.