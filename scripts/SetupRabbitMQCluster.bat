set ERLANG_HOME=C:\Program Files\Erlang OTP
set RABBITMQ_HOME=C:\Program Files\RabbitMQ Server\rabbitmq_server-3.13.5-node-1
set Path=%ERLANG_HOME%\bin;%RABBITMQ_HOME%\Sbin;%Path%
rabbitmqctl -n rabbit2@localhost join_cluster rabbit@rabbit1@localhost
rabbitmqctl -n rabbit3@localhost join_cluster rabbit@rabbit1@localhost

