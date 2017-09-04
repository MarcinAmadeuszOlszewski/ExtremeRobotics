
CREATE TABLE er_currency(
	id serial PRIMARY KEY NOT NULL,
	date date NOT NULL,
	usd NUMERIC (6, 4) NULL,
	eur NUMERIC (6, 4) NULL,
	chf NUMERIC (6, 4) NULL,
	uah NUMERIC (6, 4) NULL,
	czk NUMERIC (6, 4) NULL,
	hrk NUMERIC (6, 4) NULL,
	php NUMERIC (6, 4) NULL,
	zar NUMERIC (6, 4) NULL,
	rub NUMERIC (6, 4) NULL,
	cny NUMERIC (6, 4) NULL,
	tab_no varchar(20) NOT NULL);

 CREATE INDEX dateCluster ON er_currency(date);

