Work Order
----------
>**WIP** This is reworking of the work.order project using Scalajs and Scala 3.

Install
-------
1. brew install postgresql
2. brew install node
3. npm install jsdom ( must install **locally** )
4. graalvm ( https://www.graalvm.org/docs/getting-started/ )
5. npm install ( in project root directory )
>See **package.json** for installable dependencies.

Build
-----
1. npm install ( only when package.json changes )
2. sbt clean compile fastLinkJS
>See **js/target/public** directory.

Test
----
1. sbt clean test fastLinkJS

Dev
---
1. sbt jvm/run ( new session, curl -v http://localhost:7272/now )
2. sbt ( new session )
3. ~ js/fastLinkJS
4. npx snowpack dev ( new session )
>Edits are reflected in the **fastLinkJS** and **snowpack** sessions.
>See **snowpack.config.json** and [Snowpack Config](https://www.snowpack.dev/reference/configuration) for configurable options.

Package Server
--------------
>See sbt-native-packager ( www.scala-sbt.org/sbt-native-packager/formats/universal.html )
1. sbt clean test fullLinkJS
2. sbt jvm/universal:packageZipTarball | sbt 'show graalvm-native-image:packageBin'
>**Optionally** execute Graalvm image: ./jvm/target/graalvm-native-image/scala.graalvm

Package Client
--------------
1. sbt clean test fullLinkJS
2. npx snowpack build ( see **build** directory )

Routes
------
* Todo

Use Cases
---------
* Tod

Model
-----
1. Todo

Sequence
--------
* Todo

Postgresql
----------
1. conf:
    1. on osx intel: /usr/local/var/postgres/postgresql.conf : listen_addresses = ‘localhost’, port = 5432
    2. on osx m1: /opt/homebrew/var/postgres/postgresql.conf : listen_addresses = ‘localhost’, port = 5432
2. build.sbt:
    1. IntegrationTest / javaOptions += "-Dquill.binds.log=true"
3. run:
    1. brew services start postgresql
4. logs:
    1. on osx intel: /usr/local/var/log/postgres.log
    2. on m1: /opt/homebrew/var/log/postgres.log

Database
--------
>Example database url: postgresql://localhost:5432/poolmate?user=mycomputername&password=poolmate"
1. psql postgres
2. CREATE DATABASE poolmate OWNER [your computer name];
3. GRANT ALL PRIVILEGES ON DATABASE poolmate TO [your computer name];
4. \l
5. \q
6. psql poolmate
7. \i ddl.sql
8. \q

DDL
---
>Alternatively run: psql -d poolmate -f ddl.sql
1. psql poolmate
2. \i ddl.sql
3. \q

Drop
----
1. psql postgres
2. drop database poolmate;
3. \q

Config
------
>See these files:
1. jvm/src/main/resoures/server.conf
2. jvm/src/test/resources/test.server.conf

Cache
-----
>See jvm/Store.cache

Cors Handler
------------
* See poolmate.CorsHandler and poolmate.Server
* Also see https://github.com/Download/undertow-cors-filter

Logs
----
>See logs at /target/
1. jvm.log
2. test.jvm.log
3. test.shared.log

Documentation
-------------
1. Cask - https://com-lihaoyi.github.io/cask/index.html
2. uPickle - https://com-lihaoyi.github.io/upickle/
3. Requests - https://github.com/com-lihaoyi/requests-scala
4. ScalikeJdbc - http://scalikejdbc.org
5. H2 - https://h2database.com/html/main.html
6. Scala-Java-Time - https://github.com/cquiroz/scala-java-time
7. Scaffeine - https://github.com/blemale/scaffeine
8. Packager - https://www.scala-sbt.org/sbt-native-packager/formats/graalvm-native-image.html
9. Gaalvm - https://www.graalvm.org/docs/introduction/
10. Snowpack - https://www.snowpack.dev/
