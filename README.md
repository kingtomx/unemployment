# unemployment tool to review yearly USA unemployment data at state and county levels

The tool schedules the creation of two csv files that will contain both datasets (path can be modified in the application.properties files). Also two REST Endpoints have been included to provide the creation of the files.
Both Endpoints can be called using "http://localhost:8080/unemployment/county/{startYear}/{endYear}" and "http://localhost:8080/unemployment/state/{startYear}/{endYear}"
