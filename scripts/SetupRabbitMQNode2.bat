set ERLANG_HOME=C:\Program Files\Erlang OTP
set RABBITMQ_HOME=C:\Program Files\RabbitMQ Server\rabbitmq_server-3.13.5-node-2
set RABBITMQ_ADVANCED_CONFIG_FILE=%RABBITMQ_HOME%\advanced.config
set RABBITMQ_CONFIG_FILE=%RABBITMQ_HOME%\config\rabbitmq
set RABBITMQ_NODE_PORT=5682
set RABBITMQ_DIST_PORT=25682
set RABBITMQ_ENABLED_PLUGINS_FILE=%RABBITMQ_HOME%\enabled_plugins
set RABBITMQ_LOG_BASE=%RABBITMQ_HOME%\Logs
set RABBITMQ_MNESIA_BASE=%RABBITMQ_HOME%\Mnesia
set RABBITMQ_MNESIA_DIR=%RABBITMQ_HOME%\Mnesia\data
set RABBITMQ_NODENAME=rabbit2@localhost
set Path=%ERLANG_HOME%\bin;%RABBITMQ_HOME%\Sbin;%Path%
rabbitmq-plugins enable rabbitmq_management
rabbitmq-plugins enable rabbitmq_stream
rabbitmq-plugins enable rabbitmq_consistent_hash_exchange
rabbitmq-server start