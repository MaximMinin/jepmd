jepmd
=====

Minimal java implementation of epmd(erlang portmapper daemon).

This is a simple maven project, to build it run for example "mvn install".

The epmd is implemented as a singleton.

To start epmd on default port (4346) call:

```java
EpmdServer.startEpmd();
```

The epmd can be started on prot XXXX also:

```java
EpmdServer.startEpmd(XXXX);
```

To stop epmd call:

```java
EpmdServer.stopEpmd();
```
