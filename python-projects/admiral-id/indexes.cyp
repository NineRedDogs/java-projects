CREATE CONSTRAINT ON ( address:Address ) ASSERT address.houseId IS UNIQUE;
CREATE CONSTRAINT ON ( device:Device ) ASSERT device.id IS UNIQUE;
CREATE CONSTRAINT ON ( identity:Identity ) ASSERT identity.id IS UNIQUE;
# CREATE CONSTRAINT ON ( match:MATCHES ) ASSERT match.matchid IS UNIQUE;
CREATE CONSTRAINT ON ( product:Product ) ASSERT product.id IS UNIQUE;
CREATE CONSTRAINT ON ( vehicle:Vehicle ) ASSERT vehicle.regNum IS UNIQUE;

CREATE INDEX ON :Address(postcode);
CREATE INDEX ON :MATCHES(score);
CREATE INDEX ON :Person(fullname);
CREATE INDEX ON :Person(mobile);
CREATE INDEX ON :Person(postcode);

