CREATE TABLE IF NOT EXISTS miso_market_price_five_minutes(originaldatetime TIMESTAMP NOT NULL, hubname VARCHAR NOT NULL, lmp DECIMAL(5, 2) NOT NULL, loss DECIMAL(4, 2) NOT NULL, congestion DECIMAL(4, 2) NOT NULL, PRIMARY KEY (originaldatetime, hubname))