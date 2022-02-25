Poolmate Web
------------
>Mobile web pool management app using Cask, uPickle, Scalikejdbc, ScalaJs, Laminar, Waypoint, Scaffeine, JoddMail and Postgresql.

Install
-------
1. brew install postgresql
2. brew install node
3. npm install jsdom ( **locally** )
4. graalvm ( https://www.graalvm.org/docs/getting-started/ )

Test
----
1. sbt clean test

Build
-----
1. sbt clean test fastLinkJS package fastjs | sbt clean test fullLinkJS package fulljs

Run
---
1. sbt jvm/run ( curl -v http://localhost:7272/ )

Package ( Tar )
-------
>See sbt-native-packager ( www.scala-sbt.org/sbt-native-packager/formats/universal.html )
1. sbt jvm/universal:packageZipTarball

Package ( Graalvm Image )
-------
1. sbt 'show graalvm-native-image:packageBin'

Execute
-------
1. ./target/graalvm-native-image/scala.graalvm

Client
------
* Command => Event

Server
------
>Resource url: /
>Api url: /command

Account
-------
* Register( email ) => Registering()
* Login( email, pin ) => LoggedIn( account )
* Deactivate( license ) => Deactivated( account )
* Reactivate( license ) => Reactivated( account )

Model
-----
1. Model

View(Menu)
----------
1. Home
   * Index(Login, Register)
2. Pool
   * Pools(Account) -> Pool(Pools)
   * Surfaces(Pool) -> Surface(Surfaces)
   * Pumps(Pool) -> Pump(Pumps)
   * Timers(Pool) -> Timer(Timers) -> TimerSettings(Timer) -> HeaterSetting(TimerSettings)
   * Heaters(Pool) -> Heater(Heaters) -> HeaterSettings(Heater) -> HeaterSetting(HeaterSettins)
3. Maintenance **
   * Measurements(Pool) -> Measurement(Measurements)
   * Cleanings(Pool) -> Cleaning(Cleanings)
   * Chemicals(Pool) -> Chemical(Chemicals)
4. Expenses **
   * Supplies(Pool) -> Supply(Supplies)
   * Repairs(Pool) -> Repair(Repairs)

** Charts:
1. measurements, cleanings, chemicals
2. supplies, repairs

Entity
------
* Pool 1..n ---> 1 Account **
* Pool 1 ---> 1..n Surface, Pump, Timer, TimerSetting, Heater, HeaterSetting, Measurement, Cleaning, Chemical, Supply, Repair
* Email 1..n ---> 1 Account **
* Fault
* UoM ( unit of measure )
>** Account contains a globally unique license.

Model
-----
* Router 1 ---> 1 Dispatcher, Store
* Service 1 ---> 1 Store
* Authorizer 1 ---> 1 Service
* Handler 1 ---> 1 EmailSender, Service
* Dispatcher 1 ---> 1 Authorizer, Validator, Handler
* Scheduler 1 ---> 1 EmailProcesor 1 ---> 1 Store
* Server 1 ---> 1 Router
* Client

Sequence
--------
1. Client --- Command ---> Server
2. Server --- Command ---> Router
3. Router --- Command ---> Dispatcher
4. Dispatcher --- Command ---> Authorizer, Validator, Handler
5. Handler --- Command ---> EmailSender, Service
6. EmailSender, Service --- Either[Throwable, T] ---> Handler
7. Handler --- Event ---> Dispatcher
8. Dispatcher --- Event ---> Router
9. Router --- Event ---> Server
10. Server --- Event ---> Client
11. Scheduler ---> EmailProcessor

Measurements
------------
1. total hardness 0 - 1000      ok = 250 - 500      ideal = 375
2. total chlorine 0 - 10        ok = 1 - 5          ideal = 3
3. total bromine 0 - 20         ok = 2 - 10         ideal = 5
4. free chlorine 0 - 10         ok = 1 - 5          ideal = 3
5. ph 6.2 - 8.4                 ok = 7.2 - 7.6      ideal = 7.4
6. total alkalinity 0 - 240     ok = 80 - 120       ideal = 100
7. cyanuric acid 0 - 300        ok = 30 - 100       ideal = 50
8. temp 0 - 100

** Units of Measure - oz, gl, lb

Chemicals
---------
1. Chlorine for pool.
2. Chlorine tablets for pool filtration system.
3. Pool Shock

Solutions
---------
>Suggested solutions to chemical imbalances.
1. high ph - Sodium Bisulfate
2. low ph - Sodium Carbonate, Soda Ash
3. high alkalinity - Muriatic Acid, Sodium Bisulfate
4. low alkalinity - Sodium Bicarbonate, Baking Soda
5. calcium hardness - Calcium Chloride
6. low chlorine - Chlorine Tablets, Granules, Liquid
7. algae - Algaecide, Shock
8. stains - Stain Identification Kit, Stain Remover

Descriptions
------------
* cleanings, measurements

Images
------
* add, edit, chart

Charts
------
1. measurements - line chart ( x = date, y = chemical )
2. cleanings - line chart ( x = date, y = month )
3. chemicals - bar chart ( x = date, y = amount, c = chemical )
4. supplies - bar chart ( x = date, y = cost, c = item )
5. repairs - line chart ( x = date, y = cost )

Date
----
1. Format: yyyy-MM-dd
2. String: 1999-01-01, 1999-12-16
3. Int: 19990101, 19991216

Time
----
1. Format: HH:mm
2. String: 01:01, 19:14
3. Int: 101, 1914

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
>See jvm/Resources.cache and jvm/Store.cache

Resources
---------
> See jvm/Resources and jvm/ResourceRouter on resource loading:
1. index.html at /public/index.html
2. web resources at /public/

Logs
----
>See logs at /target/
1. jvm.log
2. test.jvm.log
2. test.shared.log

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