SELECT *
FROM Adopts;

--check update successful
SELECT *
FROM Animal
WHERE animalid IN (
	SELECT animalid
	FROM Adopts);

--UNDO TEST QUERY
UPDATE animal
SET isadopted = '0'
WHERE animalid = 706918;

DELETE FROM Adopts
WHERE animalid = 706918;

--check undo test query
SELECT *
FROM Adopts;

SELECT *
FROM Animal
WHERE animalid = 706918;

SELECT *
FROM Complaint;