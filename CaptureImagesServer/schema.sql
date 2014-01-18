CREATE TABLE files (
	filename		varchar(255),
	filepath		varchar(2999),
	id			varchar(100),
	zone			varchar(100),
	fetched			decimal(1),
	filesize		decimal(15),
	slideshow		decimal(1),
	PRIMARY KEY (filename, id, zone)
);
