You can set a more verbose logging by configuring the loglevel. Structr uses Log4j internally.

Create a file named ``logging.properties`` in Structr's main directory with the following content:


	.handlers = java.util.logging.ConsoleHandler, java.util.logging.FileHandler

	# Default log level
	.level = INFO

	############################################################
	# Handler specific properties.
	# Describes specific configuration info for Handlers.
	############################################################

	java.util.logging.FileHandler.pattern = logs/log4j.log
	java.util.logging.FileHandler.limit = 50000
	java.util.logging.FileHandler.count = 1
	java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter

	java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter


Change the above settings to fit your requirements.