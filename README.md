jepmd
=====

Minimal java implementation of epmd(erlang portmapper daemon).
This is a simple maven project, to build it run for example '''mvn install'''.
The epmd is implemented as a singolton.
To start epmd on default port(4346) call '''EpmdServer.startEpmd();'''.
The epmd can be started on prot XXXX also '''EpmdServer.startEpmd(XXXX);'''.
To stop epmd call '''EpmdServer.EpmdServer()'''.

