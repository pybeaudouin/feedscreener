SELECT
	FORMATDATETIME(originaldatetime, 'yyyy-MM-dd', 'en', 'CST') as d,
	EXTRACT(
		HOUR
		FROM DATEADD('SECOND', -1, originaldatetime)
	)+1 AS hourending,
	hubname,
	AVG(lmp)
FROM MISO_MARKET_PRICE_FIVE_MINUTES
GROUP BY
	d,
	hourending,
	originaldatetime,
	hubname
ORDER BY originaldatetime DESC, hubname ASC