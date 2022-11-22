# panache-reactive-bulk-insert Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

# Application Details

This application was created with the intent to reproduce an error when using Panache Reactive with Hibernate ORM and using bulk insert into multiple nested tables.

The table scripts and the seed data is in `main/resources/db` folder.

This application uses PostgreSQL, one of the supported reactive databases. The database configuration is maintained in a .env file within the root of the project. The below listed keys and values are used and the values can be replaced with any relevant details

DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=demodb
DATABASE_USER=demouser
DATABASE_PASSWORD=demopassword

There are 4 tables in this application, A, B, C, and D. A is the parent of B, B is the parent C and C is the parent of D. Each child has a connection to all parents in the ancestry.

Each of these tables is populated with some seed data that is setup for the application.

There is one api that is used to setup application specific data with POST request (no body needed) to the URL `http://localhost:28080/v1/tenant-setup`

Once this API gets called, the application, retrieves the data from each of the tables and inserts starting from tables A all the way to table D.

The error that is currently being produced is

```
or id: a27354f5-6e08-410a-97a4-98ad8e004885-1: java.lang.NullPointerException
	at org.hibernate.engine.internal.StatefulPersistenceContext.replaceDelayedEntityIdentityInsertKeys(StatefulPersistenceContext.java:1550)
	at org.hibernate.action.internal.EntityIdentityInsertAction.postInsert(EntityIdentityInsertAction.java:146)
	at org.hibernate.reactive.engine.impl.ReactiveEntityIdentityInsertAction.lambda$reactiveExecute$3(ReactiveEntityIdentityInsertAction.java:79)
	at java.base/java.util.concurrent.CompletableFuture$UniAccept.tryFire(CompletableFuture.java:714)
	at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:506)
	at java.base/java.util.concurrent.CompletableFuture.complete(CompletableFuture.java:2073)
	at io.vertx.core.Future.lambda$toCompletionStage$3(Future.java:384)
	at io.vertx.core.impl.future.FutureImpl$3.onSuccess(FutureImpl.java:141)
	at io.vertx.core.impl.future.FutureBase.emitSuccess(FutureBase.java:60)
	at io.vertx.core.impl.future.FutureImpl.tryComplete(FutureImpl.java:211)
	at io.vertx.core.impl.future.PromiseImpl.tryComplete(PromiseImpl.java:23)
	at io.vertx.sqlclient.impl.QueryResultBuilder.tryComplete(QueryResultBuilder.java:100)
	at io.vertx.sqlclient.impl.QueryResultBuilder.tryComplete(QueryResultBuilder.java:34)
	at io.vertx.core.Promise.complete(Promise.java:66)
	at io.vertx.core.Promise.handle(Promise.java:51)
	at io.vertx.core.Promise.handle(Promise.java:29)
	at io.vertx.core.impl.future.FutureImpl$3.onSuccess(FutureImpl.java:141)
	at io.vertx.core.impl.future.FutureBase.emitSuccess(FutureBase.java:60)
	at io.vertx.core.impl.future.FutureImpl.tryComplete(FutureImpl.java:211)
	at io.vertx.core.impl.future.PromiseImpl.tryComplete(PromiseImpl.java:23)
	at io.vertx.core.impl.future.PromiseImpl.onSuccess(PromiseImpl.java:49)
	at io.vertx.core.impl.future.PromiseImpl.handle(PromiseImpl.java:41)
	at io.vertx.sqlclient.impl.TransactionImpl.lambda$wrap$0(TransactionImpl.java:72)
	at io.vertx.core.impl.future.FutureImpl$3.onSuccess(FutureImpl.java:141)
	at io.vertx.core.impl.future.FutureBase.lambda$emitSuccess$0(FutureBase.java:54)
	at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:174)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:167)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:470)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:569)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:829)


2022-11-22 10:36:25,877 ERROR [org.jbo.res.rea.com.cor.AbstractResteasyReactiveContext] (vert.x-eventloop-thread-2) Request failed: java.lang.NullPointerException
	at org.hibernate.engine.internal.StatefulPersistenceContext.replaceDelayedEntityIdentityInsertKeys(StatefulPersistenceContext.java:1550)
	at org.hibernate.action.internal.EntityIdentityInsertAction.postInsert(EntityIdentityInsertAction.java:146)
	at org.hibernate.reactive.engine.impl.ReactiveEntityIdentityInsertAction.lambda$reactiveExecute$3(ReactiveEntityIdentityInsertAction.java:79)
	at java.base/java.util.concurrent.CompletableFuture$UniAccept.tryFire(CompletableFuture.java:714)
	at java.base/java.util.concurrent.CompletableFuture.postComplete(CompletableFuture.java:506)
	at java.base/java.util.concurrent.CompletableFuture.complete(CompletableFuture.java:2073)
	at io.vertx.core.Future.lambda$toCompletionStage$3(Future.java:384)
	at io.vertx.core.impl.future.FutureImpl$3.onSuccess(FutureImpl.java:141)
	at io.vertx.core.impl.future.FutureBase.emitSuccess(FutureBase.java:60)
	at io.vertx.core.impl.future.FutureImpl.tryComplete(FutureImpl.java:211)
	at io.vertx.core.impl.future.PromiseImpl.tryComplete(PromiseImpl.java:23)
	at io.vertx.sqlclient.impl.QueryResultBuilder.tryComplete(QueryResultBuilder.java:100)
	at io.vertx.sqlclient.impl.QueryResultBuilder.tryComplete(QueryResultBuilder.java:34)
	at io.vertx.core.Promise.complete(Promise.java:66)
	at io.vertx.core.Promise.handle(Promise.java:51)
	at io.vertx.core.Promise.handle(Promise.java:29)
	at io.vertx.core.impl.future.FutureImpl$3.onSuccess(FutureImpl.java:141)
	at io.vertx.core.impl.future.FutureBase.emitSuccess(FutureBase.java:60)
	at io.vertx.core.impl.future.FutureImpl.tryComplete(FutureImpl.java:211)
	at io.vertx.core.impl.future.PromiseImpl.tryComplete(PromiseImpl.java:23)
	at io.vertx.core.impl.future.PromiseImpl.onSuccess(PromiseImpl.java:49)
	at io.vertx.core.impl.future.PromiseImpl.handle(PromiseImpl.java:41)
	at io.vertx.sqlclient.impl.TransactionImpl.lambda$wrap$0(TransactionImpl.java:72)
	at io.vertx.core.impl.future.FutureImpl$3.onSuccess(FutureImpl.java:141)
	at io.vertx.core.impl.future.FutureBase.lambda$emitSuccess$0(FutureBase.java:54)
	at io.netty.util.concurrent.AbstractEventExecutor.runTask(AbstractEventExecutor.java:174)
	at io.netty.util.concurrent.AbstractEventExecutor.safeExecute(AbstractEventExecutor.java:167)
	at io.netty.util.concurrent.SingleThreadEventExecutor.runAllTasks(SingleThreadEventExecutor.java:470)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:569)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:829)


2022-11-22 10:36:25,883 ERROR [io.qua.mut.run.MutinyInfrastructure] (vert.x-eventloop-thread-2) Mutiny had to drop the following exception: io.vertx.pgclient.PgException: ERROR: insert or update on table "table_b" violates foreign key constraint "table_b_x_table_a_fkey" (23503)
	at io.vertx.pgclient.impl.codec.ErrorResponse.toException(ErrorResponse.java:31)
	at io.vertx.pgclient.impl.codec.QueryCommandBaseCodec.handleErrorResponse(QueryCommandBaseCodec.java:57)
	at io.vertx.pgclient.impl.codec.ExtendedQueryCommandCodec.handleErrorResponse(ExtendedQueryCommandCodec.java:90)
	at io.vertx.pgclient.impl.codec.PgDecoder.decodeError(PgDecoder.java:246)
	at io.vertx.pgclient.impl.codec.PgDecoder.decodeMessage(PgDecoder.java:132)
	at io.vertx.pgclient.impl.codec.PgDecoder.channelRead(PgDecoder.java:112)
	at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:788)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:724)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:562)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:997)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.base/java.lang.Thread.run(Thread.java:829)
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/panache-reactive-bulk-insert-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A JAX-RS implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Liquibase ([guide](https://quarkus.io/guides/liquibase)): Handle your database schema migrations with Liquibase
- Reactive PostgreSQL client ([guide](https://quarkus.io/guides/reactive-sql-clients)): Connect to the PostgreSQL database using the reactive pattern

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
