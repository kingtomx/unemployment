# unemployment tool to review yearly USA unemployment data at state and county levels

This tool schedules the creation of two .csv files that will contain both datasets (path can be modified in application.properties). Also, a couple of REST Endpoints have been included to provide the creation of the files ("http://localhost:8080/unemployment/county/{startYear}/{endYear}" and "http://localhost:8080/unemployment/state/{startYear}/{endYear}")
