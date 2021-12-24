-- :name create-user!* :! :n
-- :doc creates a new user record
INSERT INTO users
(login, password)
VALUES (:login, :password)

-- :name get-user-for-auth* :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE login = :login

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id
