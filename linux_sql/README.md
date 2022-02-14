# Introduction
As a developer I am tasked with designing, implementing, and demonstrating an MVP (minimal viable product) that helps the LCA (Linux Cluster Administration) team to meet their business needs. The LCA team manages a cluster of 10 nodes/servers all of which are running CentOS 7 and connected through a switch and can communicate through IPV4. 

The LCA team needs an application that records hardware specifications of each node and monitor the resource usage (eg. CPU, memory...) every five minutes. The recorded data is then stored in an RDBMS database and used to generate reports for future resource planning purposes.

#### Tools used:
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/docker/docker.png" 
     />
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/git/git.png"/>
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/sql/sql.png"/>
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/postgresql/postgresql.png"/>
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/bash/bash.png"/>
<img height="32" width="32" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/linux/linux.png"/>

# Quick Start
#### Start, stop and create a psql instance using psql_docker.sh
```
# start the stoped psql docker container (print error message if the container is not created)

./scripts/psql_docker.sh start

# stop the running psql docker container 

./scripts/psql_docker.sh stop (print error message if the container is not created)

# create a psql docker container with the given username and password (print error message if the container is already created)

./scripts/psql_docker.sh create db_username db_password
```
#### Database commands
```
# Create tables using ddl.sql

psql -h localhost -U postgres -d host_agent -f sql/ddl.sql

# Query from the DDL table using queries.sql

psql -h host_name -p psql_port(5432) -U psql_user  -p psql_password -d host_agent -f /sql/queries.sql
```
#### host usage/info scripts
```
# Insert hardware specs data into the DB using host_info.sh

bash scripts/host_info.sh localhost 5432 host_agent postgres password

# Insert hardware usage data into the DB using host_usage.sh

bash scripts/host_usage.sh localhost 5432 host_agent postgres password
```
#### Crontab setup
```
# In Bash type the command:

crontab -e

# Copy paste the text into Crontab

* * * * * bash /home/centos/dev/jarvis_data_eng_KarlMerhi/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```
# Implemenation
The project was implemented by creating a script (psql_docker.sh) in order to easily start, stop or create a psql docker instance. Once the PSQL docker instance is created, I used ddl.sql to connect and insert two tables, one to record the nodes/hosts/servers hardware information and another to record the usage data. 

I then implemented the scripts host_usage.sh and host_info.sh into the PSQL instance in order to retrieve data from these hosts and insert it into the database (Both scripts are installed on every node/host/server). The host_usage script is then run every 5 minutes using crontab to ensure that the host usage data is up to date. Lastly, queries.sql is used to retrieve the group hosts by hardware info, average memory usage, and to detect host failures.

TL;DR
I implemented the program using Linux command lines, Bash scripts, PostgreSQL, Docker, IntelliJ, DBeaver, etc...

## Architecture
![Architecture Diagram](./assets/ArchitectureDiagram.png)

## Scripts
Shell script description and usage (use markdown code block for script usage)
- psql_docker.sh - This script is used to start, stop or create a PSQL instance using docker. It will also return an error message depending on if there is already an instance created.
- host_info.sh - This script is responsible for collecting the hardware specifications from the host and storing the information in a PSQL table. The script requires the host, port, database, username, and password to call.
- host_usage.sh - This script is used to collect the host usage information from the system and store it into the host_usage table. The script requires the host port, database, username, and password to call.
- crontab - The crontab command is used to run the host_usage.sh script at an interval of 5 minutes in order to store updated usage data.
- queries.sql - Is used to query different information from the host (hardware info, average memory usage, host failures, etc...)
- ddl.sql - This is used to create the two tables that are stored within the PSQL Docker instance.

## Database Modeling
The schema of each table:
#### `host_info`

Column Name | Data type | Constraint
| :--- | ---: | :---:
id  | serial | primary key, not null
hostname  | varchar | unique, not null
cpu_number  | int | not null
cpu_architecture  | varchar | not null
cpu_model  | varchar | not null
cpu_mhz  | float | not null
l2_cache  | int | not null
total_mem  | int | not null
timestamp  | timestamp | not null
#### `host_usage`

Column Name | Data type | Constraint
| :--- | ---: | :---:
timestamp  | timestamp | not null
host_id  | serial | foreign key, not null
memory_free  | serial | not null
cpu_idle  | int | not null
cpu_kernel  | int | not null
disk_io  | int | not null
disk_available  | int | not null

# Test
I was able to test my program on my machine since it is a minimum viable product and did not require Linux clusters, however, my program is compatible with a cluster environment. I have tested all bash scripts functionalities manually within the terminal using the commands highlighted under QuickStart. I also tested/verified all of the queries by connecting to my PSQL database using DBeaver using my own test data. 

# Deployment
How did you deploy your app? (e.g. Github, crontab, docker)
I used Github and Docker to deploy my app. All of the SQL files and Bash scripts are available for download on Github. Assuming firewall and other configurations are set properly the application should run with all of the resources provided. The only prerequisite is setting up the PSQL Docker image. This can be pulled with the command `docker pull Postgres from the docker registry.

# Improvements
Write at least three things you want to improve 
e.g. 
- handle hardware update 
- restrict hardware heavy processes running in the background
- script to be able to query different information from the terminal (eg. total_mem over 1 min intervals)
