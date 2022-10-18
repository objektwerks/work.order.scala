Work Order
----------
>Work order (mobile) web app linking homeowners and service providers via work orders and email notification.
>Reimplementation of [work.order](https://github.com/objektwerks/work.order) using Cask, uPickle, Scalikejdbc, ScalaJs, Laminar, Waypoint, W3.CSS,
>Scaffeine, JoddMail, Mysql, Snowpack and Scala 3

Todo
----
1. Views

Install
-------
1. brew install postgresql
2. brew install node
3. npm install ( in project root directory )
>See **package.json** for installable dependencies.

Build
-----
1. npm install ( only when package.json changes )
2. sbt clean compile fastLinkJS
>See **js/target/public** directory.

Test
----
>Failed to successfully use scala.sys.process.Process to run mysql script.
1. 1. mysql -u workorder -p
2. \. ddl.sql
3. exit
4. sbt clean test

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
2. npx snowpack build
> See **build** directory.

Roles
-----
>A role can invoke a set of actions.
1. **homeowner** — add, select and edit *work orders*
2. **service provider** — select and edit *work orders*
3. **app** - has super powers :)

Features [ Roles ]
------------------
>A feature maps to a set of roles.
1. **register user** — [ homeowner, service provider ]
2. **login user** — [ homeowner, service provider ]
3. **edit user** — [ homeowner, service provider ]
4. **add work order** — [ homeowner ]
5. **edit work order** — [ homeowner, service provider ]
6. **list work orders** - [ homeowner, service provider ]
7. **registration email notification*** - [ app ]
8. **new work order email notification** - [ app ]
9. **work order (updated and closed) email notifications** - [ app ]

Forms
-----
1. **register** — role, name, email address, street address
2. **login**— email address, pin
3. **user** - role, name, email address, street address, registered
4. **work order** — number, homeowner, service provider, issue, street address, image url, resolution, opened, closed

Routes
------
1. post - /register
2. post - /login
3. post - /users/save
4. post - /workorders/add
5. post - /workorders/save
6. post  - /workorders

Sequences
---------
1. **client** --- register --> server --- registered ---> client --- email ---> homeowner or service provider
2. **client** --- login --> server --- logged in --> client
3. **client** --- save user --> server --- user saved --> client
4. **client** --- (add) save work order --> server --- work order saved --> client --- email ---> homeowner and service provider
5. **client** --- save work order --> server --- work order saved --> client --- email ---> homeowner and service provider
6. **client** --- list work orders --> server --- work orders listed --> client

Registration
------------
>A prospective user must register with a/an:
1. role
2. name
3. email address
4. street address
>If the email address is valid, the new user will receive a ***pin*** via a **Work Order Registration** email.

Authentication
--------------
>A user must login with a/an:
1. email address
2. pin

Authorization
-------------
>The following routes are authorized by a user's **license**:
1. /users/save
2. /workorders/add
3. /workorders/save
4. /workorders

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
* See workorder.CorsHandler and workorder.Server
* Also see https://github.com/Download/undertow-cors-filter

Logs
----
>See logs at /target/
1. jvm.log
2. test.jvm.log
3. test.shared.log

Mysql Schema
------------
1. work_order_db
2. user
3. work_order
>See **user.sql** and **ddl.sql** for details.

Mysql Setup
-----------
>Built using Mysql 8.0.30
1. sudo mysql -u root
2. \. user.sql
3. \. ddl.sql
4. exit

Mysql Connection Url
--------------------
* mysql://workorder:workorder@127.0.0.1:3306/work_order_db

Mysql Update
------------
1. mysql -u workorder -p
2. \. ddl.sql
3. exit

Mysql Log
---------
>Apple M1, macOS, Big Sur - /opt/homebrew/var/mysql/computername.local.err

Date Time
---------
>ISO standard: YYYY-MM-DDTHH:mm:ss.sssZ ( 2022-04-15T11:07:36.639Z )

Photos
------
>The following image file types are supported:
1. **jpeg**
2. **jpg**
3. **png**
>Only **1** image is allowed ***per*** work order. The app stores ***images*** in **WORK_ORDER_IMAGES_DIR** defined below.

Environment
-----------
>The following environment variables ***must*** be defined:

export WORK_ORDER_DATABASE_URL="mysql://workorder:workorder@127.0.0.1:3306/work_order_db"
export WORK_ORDER_DATABASE_USER="workorder"
export WORK_ORDER_DATABASE_PASSWORD="workorder"
export WORK_ORDER_DATABASE_POOL_INITIAL_SIZE=9
export WORK_ORDER_DATABASE_POOL_MAX_SIZE=32
export WORK_ORDER_DATABASE_POOL_CONNECTION_TIMEOUT_MILLIS=30000

export WORK_ORDER_EMAIL_HOST="youremailhost.com"
export WORK_ORDER_EMAIL_PORT=587
export WORK_ORDER_EMAIL_SENDER="youremailaddress@youremailhost.com"
export WORK_ORDER_EMAIL_PASSWORD="youremailpassword"

export WORK_ORDER_SERVICE_PROVIDER_EMAIL="testemailaddress1@youremailhost.com"
export WORK_ORDER_HOME_OWNER_EMAIL="testemailaddress2@youremailhost.com"

export WORK_ORDER_DIR=$HOME/.workorder
export WORK_ORDER_IMAGES_DIR=$WORK_ORDER_DIR/images
export WORK_ORDER_LOGS_DIR=$WORK_ORDER_DIR/logs

>All variables are for production less: WORK_ORDER_SERVICE_PROVIDER_EMAIL and WORK_ORDER_HOME_OWNER_EMAIL,
>which are for the integration test.

Resources
---------
1. Cask - https://com-lihaoyi.github.io/cask/index.html
2. uPickle - https://com-lihaoyi.github.io/upickle/
3. ScalikeJdbc - http://scalikejdbc.org
4. Mysql - https://www.w3schools.com/mySQl/default.asp
5. Scala-Java-Time - https://github.com/cquiroz/scala-java-time
6. Scaffeine - https://github.com/blemale/scaffeine
7. Packager - https://www.scala-sbt.org/sbt-native-packager/formats/graalvm-native-image.html
8. Snowpack - https://www.snowpack.dev/

License
-------
> Copyright (c) [2022] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations u\nder the License.