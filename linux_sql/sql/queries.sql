--add more data
-- 1) GROUP HOSTS BY HARDWARE INFO
SELECT cpu_number, id, total_mem 
FROM host_info
GROUP BY cpu_number, id
ORDER BY cpu_number, total_mem DESC;

-- round current ts every 5 mins
--SELECT date_trunc('hour', timestamp) + date_part('minute', timestamp):: int / 5 * interval '5 min' 
--FROM host_usage;

-- you can also create a function for convenience purposes so your qeury looks cleaner
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
    RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
    LANGUAGE PLPGSQL;

-- verify rount5 function
--SELECT host_id, timestamp, round5(timestamp)
--FROM host_usage;

-- 2) AVERAGE MEMORY USAGE
SELECT hu.host_id, hi.hostname, round5(hu.timestamp) as timestamp5min,
ROUND(AVG((hi.total_mem :: float - hu.memory_free*1000 :: float)*100/
(hi.total_mem))) as avg_used_mem_percentage
FROM host_usage hu
JOIN host_info hi on hu.host_id = hi.id
GROUP BY hu.host_id, hi.hostname, timestamp5min, hi.total_mem, hu.memory_free 
ORDER BY hu.host_id, avg_used_mem_percentage; 


-- 3) DETECT HOST FAILURE
SELECT host_id, ts, COUNT(*) AS num_data_points
FROM (SELECT host_id, round5(host_usage.timestamp) AS ts FROM host_usage) AS alias
GROUP BY ts, alias.host_id
ORDER BY host_id;

