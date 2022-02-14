#!/bin/bash

#Setup and validate arguments (again, don't copy comments)
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#Check # of args
if [ $# -ne  5 ];then
  echo "invalid number of arguments"
  exit 1
fi

#Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)
hostname=$(hostname -f)

#Retrieve hardware specification variables
#xargs is a trick to trim leading and trailing white spaces

lscpu_out=$(lscpu)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' |  xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{$1=$2=""; print $0}' | tail -n1 | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU MHz:" | awk '{print $NF}' |  xargs)
L2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{$1=$2=""; print $0}' | sed 's/K//' | tail -n1 |  xargs)
total_mem=$(cat /proc/meminfo  | egrep "^MemTotal:" | awk '{print $2}' |  xargs)

#Current time in `2019-11-26 14:40:19` UTC format
timestamp=$(vmstat -t | awk '{print $(NF-1), $(NF)}' | tail -n1 | xargs)

#PSQL command: Inserts server usage data into host_usage table
#Note: be careful with double and single quotes
insert_stmt="INSERT INTO host_info(hostname, cpu_number, cpu_architecture, cpu_model,cpu_mhz,L2_cache,total_mem,timestamp)
VALUES('$hostname', $cpu_number, '$cpu_architecture', '$cpu_model', $cpu_mhz, $L2_cache, $total_mem, '$timestamp')"

#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?
